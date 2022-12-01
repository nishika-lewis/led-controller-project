package io.github.ledproject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UI
{
    public static void main(String[] args)
    {
        // Create and set up a frame window
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("LED Controller");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel label = new JLabel();// creates a label
        
        label.setText("What do you want to see?"); // set Text of label
        label.setHorizontalTextPosition(JLabel.CENTER);// set text to left, center or right of imageicon
        label.setVerticalTextPosition(JLabel.TOP);// set text to Top, Center or Bottom of image icon
        label.setForeground(new Color(0, 74, 173));// set color of text
        label.setFont(new Font("Calibri", Font.PLAIN, 20));// set font of text
        
        // Define new buttons with different width on help of the ---
        JButton jb1 = new JButton("At Sign");
        jb1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("At Sign");
            }
        });
        JButton jb2 = new JButton("UMass Boston");
        jb2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("UMass Boston");
            }
        });
        JButton jb3 = new JButton("Hello World");
        jb3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("Hello World");
            }
        });
        
        JTextField text = new JTextField();
        
        JButton jb4 = new JButton("Submit");
        jb4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println(text.getText());
            }
        });
        JButton jb5 = new JButton("Quit Application");
        jb5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        });
        
        // Define the panel to hold the buttons
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        
        // Set up the title for different panels
        panel2.setBorder(BorderFactory.createTitledBorder("CUSTOM TEXT"));
        // panel3.setBorder(BorderFactory.createTitledBorder("RIGHT"));
        
        // Set up the BoxLayout
        BoxLayout layout1 = new BoxLayout(panel1, BoxLayout.Y_AXIS);
        BoxLayout layout2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
        BoxLayout layout3 = new BoxLayout(panel3, BoxLayout.Y_AXIS);
        panel1.setLayout(layout1);
        panel2.setLayout(layout2);
        panel3.setLayout(layout3);
        
        // Add the buttons into the panel with three different alignment options
        jb1.setAlignmentX(Component.LEFT_ALIGNMENT);
        jb2.setAlignmentX(Component.LEFT_ALIGNMENT);
        jb3.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel1.add(Box.createRigidArea(new Dimension(0, 10)));
        panel1.add(label);
        panel1.add(Box.createRigidArea(new Dimension(0, 15)));
        panel1.add(jb1);
        panel1.add(Box.createRigidArea(new Dimension(0, 15)));
        panel1.add(jb2);
        panel1.add(Box.createRigidArea(new Dimension(0, 15)));
        panel1.add(jb3);
        panel1.add(Box.createRigidArea(new Dimension(0, 15)));
        
        jb4.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel2.add(text);
        panel2.add(Box.createRigidArea(new Dimension(20, 0)));
        panel2.add(jb4);
        panel2.add(Box.createRigidArea(new Dimension(20, 0)));
        
        jb5.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel3.add(Box.createRigidArea(new Dimension(0, 20)));
        panel3.add(jb5);
        
        // Add the three panels into the frame
        frame.setLayout(new GridLayout(3, 1));
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        
        // Set the window to be visible as the default to be false
        frame.pack();
        frame.setVisible(true);
        //frame.setSize(500, 500);
        
        frame.setResizable(false);// prevents frame from being Resized
        frame.setLocationRelativeTo(null);// pops the frame in the center of the screen
        
        ImageIcon image = new ImageIcon("assets/led.png");// create an image icon - assets/led
        frame.setIconImage(image.getImage());// change icon of frame
    }
}
