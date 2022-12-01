package io.github.ledproject;

import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UI
{
    public static void main(String[] args)
    {
        // ImageIcon bg = new ImageIcon("assets/background.png");
        // Border border = BorderFactory.createLineBorder(Color.cyan.darker(),2);//creates border
        
        JLabel label = new JLabel();// creates a label
        JLabel pic = new JLabel(new ImageIcon("assets/background.png"));
        label.setText("What do you want to see?"); // set Text of label
        // label.setIcon(bg);
        label.setHorizontalTextPosition(JLabel.CENTER);// set text to left, center or right of imageicon
        label.setVerticalTextPosition(JLabel.TOP);// set text to Top, Center or Bottom of image icon
        label.setForeground(new Color(0, 74, 173));// set color of text
        label.setFont(new Font("Corbel Light", Font.BOLD, 20));// set font of text
        label.setIconTextGap(-50);// set gap of text to image
        // label.setBorder(border);//sets borders
        label.setVerticalAlignment(JLabel.CENTER);// sets vertical position of icon+text within label
        label.setHorizontalAlignment(JLabel.CENTER);// sets horizontal position of icon+text within label
        
        JButton button1 = new JButton();
        button1.setBounds(100, 100, 108, 44);
        
        JPanel panel = new JPanel();
        
        panel.add(label);
        panel.add(button1);
        panel.add(pic);
        panel.setBackground(Color.white);
        
        // panel.setIcon(bg);
        // panel.setBorder(border);
        
        JFrame frame = new JFrame(); // creates the frame
        frame.setVisible(true);// makes frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Exit out of application
        frame.setTitle("LED Controller");// sets title of the frame
        frame.setSize(500, 500);// sets the x-dimensions, and y dimensions of frame
        frame.setResizable(false);// prevents frame from being Resized
        frame.setLocationRelativeTo(null);// pops the frame in the center of the screen
        // frame.setLayout(null);
        frame.add(panel);
        // frame.add(button1);
        
        ImageIcon image = new ImageIcon("assets/led.png");// create an image icon - logo
        frame.setIconImage(image.getImage());// change icon of frame
    }
}
