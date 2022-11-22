package io.github.ledproject;

import javax.swing.JButton;
import javax.swing.JFrame;
import org.atsign.client.api.AtClient;
import org.atsign.common.AtSign;
import org.atsign.common.KeyBuilders;
import org.atsign.common.Keys.PublicKey;

public class App
{
    public static void main(String args[])
    {
        JFrame frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        JButton button = new JButton("Press");
        frame.getContentPane().add(button); // Adds Button to content pane of frame
        frame.setVisible(true);
    }

    public static void connect() throws Exception
    {
        AtSign atSign = new AtSign("@computer0");
        AtClient atClient = AtClient.withRemoteSecondary(atSign);
        PublicKey pk = new KeyBuilders.PublicKeyBuilder(atSign).key("instructions").build();
        int instruction = 0;

        while (true) {
            Thread.sleep(5000);
            String value = "" + instruction;
            String response = atClient.put(pk, value).get();
            System.out.println(response);
        }
    }
}
