package io.github.ledproject;

import java.util.Scanner;
import org.atsign.client.api.AtClient;
import org.atsign.common.AtSign;
import org.atsign.common.KeyBuilders;
import org.atsign.common.Keys.PublicKey;

public class App
{
    private static final AtSign APP_ATSIGN = new AtSign("@computer0");
    private static final AtSign PICO_ATSIGN = new AtSign("@maximumcomputer");
    private static final String APP_KEY = "instructions";
    private static final String PICO_KEY = "led";
    private static final String PROMPT = "Greetings! What text would you like to display?\n1. AtSign\n2. UMass Boston\n3. Hello World!\n4. (Custom)\n5. Quit";
    
    public static void main(String args[]) throws Exception
    {
        startPrompt();
    }
    
    public static void startPrompt() throws Exception
    {
        System.out.println(PROMPT); // Display starting prompt.
        
        try (Scanner sc = new Scanner(System.in))
        {
            // Prompt user for input.
            while (sc.hasNext())
            {
                int input = sc.nextInt();
                
                // Dispatch on the digit received.
                switch (input)
                {
                    case 1:
                        displayText("AtSign");
                        break;
                    case 2:
                        displayText("UMass Boston");
                        break;
                    case 3:
                        displayText("Hello World!");
                        break;
                    case 4:
                        sc.nextLine(); // Clear input buffer.
                        System.out.println("Enter your custom text:");
                        displayText(sc.nextLine());
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        return; // Quit program.
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
                
                System.out.println("Enter a new option:");
            }
        }
    }
    
    /**
     * Displays the given text on the pico's LED matrix.
     * 
     * @param text - the text to display on the LED matrix.
     * @throws Exception - if there was an error communicating with the atPlatform.
     */
    public static void displayText(String text) throws Exception
    {
        // Connect to the remote secondary of the app's atsign.
        AtClient atClient = AtClient.withRemoteSecondary(APP_ATSIGN);
        
        // Receive beginning LED status from the pico's public key.
        PublicKey pk = new KeyBuilders.PublicKeyBuilder(PICO_ATSIGN).key(PICO_KEY).build();
        String beginStatus = getLEDStatus(atClient, pk), ledStatus = beginStatus;
        
        // Receive previous display text from the app's public key.
        String previousText = getLEDText(atClient);
        
        // Send new display text to the app's public key.
        sendLEDText(atClient, text.equals(previousText) ? ' ' + text : text);
        
        // Wait until the pico receives the display text.
        while (ledStatus.equals(beginStatus))
        {
            Thread.sleep(1000);
            ledStatus = getLEDStatus(atClient, pk);
        }
        
        // Wait for the LED text to fully display before returning.
        delay(text);
    }
    
    /**
     * Receives the LED status on the pico atsign's public key.
     * 
     * @param atClient - the atClient used to communicate.
     * @param pk       - the pico atsign's public key.
     * @return the text of the pico atsign's public key.
     * @throws Exception if there was an error response.
     */
    private static String getLEDStatus(AtClient atClient, PublicKey pk) throws Exception
    {
        atClient.executeCommand("delete:cached:public:" + PICO_KEY + PICO_ATSIGN, false);
        return atClient.get(pk).get();
    }
    
    /**
     * Receives the text currently on the app atsign's public key.
     * 
     * @param atClient - the atClient used to communicate.
     * @return the text of the app atsign's public key.
     * @throws Exception if there was an error response.
     */
    private static String getLEDText(AtClient atClient) throws Exception
    {
        PublicKey pk = new KeyBuilders.PublicKeyBuilder(APP_ATSIGN).key(APP_KEY).build();
        atClient.executeCommand("delete:cached:public:" + APP_KEY + APP_ATSIGN, false);
        return atClient.get(pk).get();
    }
    
    /**
     * Sets the app atsign's public key to the given text.
     * 
     * @param atClient - the atClient used to communicate.
     * @param text     - text to be put on the app atsign's public key.
     */
    private static void sendLEDText(AtClient atClient, String text)
    {
        PublicKey pk = new KeyBuilders.PublicKeyBuilder(APP_ATSIGN).key(APP_KEY).build();
        atClient.put(pk, text);
    }
    
    /**
     * Delays for the amount of time it takes for the given text to display.
     * 
     * @param text - text that is being displayed on the LED matrix.
     */
    public static void delay(String text) throws InterruptedException
    {
        int displayCharacters = text.trim().length();
        Thread.sleep(9000 + 800 * displayCharacters);
    }
}
