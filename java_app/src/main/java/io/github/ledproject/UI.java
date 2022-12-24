package io.github.ledproject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import org.apache.commons.lang3.StringUtils;

public class UI extends JFrame
{
    // Display areas for the on/off LED images.
    private JLabel[] ledLabels;
    
    // Queue for text to be displayed.
    private Queue<String> displayQueue;
    
    // Whether the display queue is busy.
    private boolean queueBusy;
    
    // On/off images for the LED.
    private final static ImageIcon LED_ON_IMAGE = new ImageIcon("assets/led_on.png");
    private final static ImageIcon LED_OFF_IMAGE = new ImageIcon("assets/led_off.png");
    
    /**
     * Setup for the LED Controller UI frame.
     * 
     * @param title - name of the frame window.
     */
    public UI(String title)
    {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ledLabels = new JLabel[] {new JLabel(), new JLabel()};
        displayQueue = new LinkedList<String>();
        queueBusy = false;
        turnOffLED();
    }
    
    public static void main(String[] args)
    {
        // Create and set up a frame window.
        UI frame = new UI("LED Controller");
        
        // Create a label at the top of the frame.
        JLabel titleLabel = new JLabel();
        titleLabel.setText("What do you want to see?");
        titleLabel.setHorizontalTextPosition(JLabel.CENTER);
        titleLabel.setVerticalTextPosition(JLabel.TOP);
        titleLabel.setForeground(new Color(0x004aad));
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 40));
        
        // Define buttons with fixed LED display text.
        String[] fixedText = {"AtSign", "UMass Boston", "Hello World!"};
        JButton[] fixedButtons = new JButton[3];
        
        for (int i = 0; i < fixedButtons.length; i++)
        {
            fixedButtons[i] = frame.fixedButton(fixedText[i]);
        }
        
        // Define custom text area.
        JTextField textField = new JTextField("", 20);
        textField.setFont(new Font("Calibri", Font.PLAIN, 20));
        
        // Define custom text submission button.
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Calibri", Font.PLAIN, 32));
        submitButton.addActionListener(e ->
        {
            // First clear text field, then flash LED with the custom text.
            String displayText = textField.getText();
            textField.setText("");
            frame.flashLED(displayText);
        });
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Define quit button.
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Calibri", Font.PLAIN, 40));
        quitButton.addActionListener(e -> System.exit(0));
        
        // Define panels to hold each component.
        JPanel ledPanel1 = new JPanel();
        JPanel titlePanel = new JPanel();
        JPanel ledPanel2 = new JPanel();
        JPanel fixedButtonPanel = new JPanel();
        JPanel customTextPanel = new JPanel();
        JPanel quitPanel = new JPanel();
        
        // Set up title for the custom text panel.
        customTextPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Custom Text",
                TitledBorder.CENTER, TitledBorder.CENTER, new Font("Calibri", Font.PLAIN, 20)));
        
        // Set up the layouts for each panel.
        titlePanel.setLayout(new GridBagLayout());
        fixedButtonPanel.setLayout(new BoxLayout(fixedButtonPanel, BoxLayout.Y_AXIS));
        customTextPanel.setLayout(new GridBagLayout());
        quitPanel.setLayout(new GridBagLayout());
        
        // Put the title label in the title panel.
        titlePanel.add(titleLabel);
        
        // Put the LED images in the LED panels.
        ledPanel1.add(frame.ledLabels[0]);
        ledPanel2.add(frame.ledLabels[1]);
        
        // Add the fixed buttons into the fixed button panel with center alignment.
        for (JButton button : fixedButtons)
        {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            fixedButtonPanel.add(button);
            fixedButtonPanel.add(Box.createRigidArea(new Dimension(30, 30)));
        }
        
        // Put the custom text field adjacent to the submission button in the panel.
        customTextPanel.add(textField);
        customTextPanel.add(submitButton);
        
        // Put the quit button in the quit panel.
        quitPanel.add(quitButton);
        
        // Add each panel into the frame.
        frame.setLayout(new GridLayout(2, 3));
        frame.add(ledPanel1);
        frame.add(titlePanel);
        frame.add(ledPanel2);
        frame.add(fixedButtonPanel);
        frame.add(customTextPanel);
        frame.add(quitPanel);
        
        // Make the frame window visible.
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("assets/led_icon.png").getImage());
    }
    
    /**
     * Creates a button with fixed LED display text.
     * 
     * @param text - the text listed on the button.
     * @return a button with fixed LED display text.
     */
    private JButton fixedButton(String text)
    {
        JButton jb = new JButton(text);
        jb.setFont(new Font("Calibri", Font.PLAIN, 48));
        jb.addActionListener(e -> flashLED(text));
        return jb;
    }
    
    /**
     * Sets both LED images to the "on" version.
     */
    private void turnOnLED()
    {
        ledLabels[0].setIcon(LED_ON_IMAGE);
        ledLabels[1].setIcon(LED_ON_IMAGE);
    }
    
    /**
     * Sets both LED images to the "off" version.
     */
    private void turnOffLED()
    {
        ledLabels[0].setIcon(LED_OFF_IMAGE);
        ledLabels[1].setIcon(LED_OFF_IMAGE);
    }
    
    /**
     * Causes the LED images to flash on/off when display text is sent to the pico.
     * 
     * @param text - text that is sent to the pico's atsign.
     */
    private void flashLED(String text)
    {
        @SuppressWarnings("rawtypes")
        SwingWorker sw = new SwingWorker()
        {
            @Override
            protected Object doInBackground() throws Exception
            {
                queueBusy = true; // Lock thread to prevent concurrent text display.
                
                // Loop until all display text has been processed by order of button press.
                while (!displayQueue.isEmpty())
                {
                    String text = displayQueue.poll();
                    App.displayText(text); // Send display text to the pico.
                    Thread.sleep(4750); // Wait for pico to initialize text.
                    turnOnLED(); // Turn LED images "on."
                    App.delay(text); // Wait for the display text to fully scroll.
                    turnOffLED(); // Turn LED images "off."
                }
                
                return null;
            }
            
            @Override
            protected void done()
            {
                queueBusy = false; // Release thread lock.
            }
        };
        
        // Add non-blank text to the display queue, and start
        // up a display thread if one is not already running.
        if (StringUtils.isNotBlank(text)) displayQueue.add(text);
        if (!queueBusy && displayQueue.size() == 1) sw.execute();
    }
}
