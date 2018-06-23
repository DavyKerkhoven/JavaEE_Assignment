/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author Davy
 */
public class ConnectScreen extends JPanel implements ActionListener
{
    private static final int WIDTH = 640;
    private static final int HEIGHT = 320;
    
    private Client client;
    
    JFrame frame;
    JLabel clientIDLabel;
    JLabel IPLabel;
    JTextField clientIDTextField;
    JTextField IPTextField;
    JButton enterButton;
    
    public ConnectScreen(Client client)
    {
        super();
        this.client = client;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        clientIDLabel = new JLabel("Username/ID:");
        IPLabel = new JLabel("Host IP:");
        clientIDTextField = new JTextField();
        clientIDTextField.setPreferredSize(new Dimension(WIDTH, HEIGHT/5));
        IPTextField = new JTextField();
        IPTextField.setText("192.168.1.73");
        IPTextField.setPreferredSize(new Dimension(WIDTH, HEIGHT/5));
        enterButton = new JButton("Connect");
        enterButton.setPreferredSize(new Dimension(WIDTH/2, HEIGHT/5));
        enterButton.addActionListener(this);
        add(clientIDLabel);
        add(clientIDTextField);
        add(IPLabel);
        add(IPTextField);
        add(enterButton);
        
        frame = new JFrame("Connect");
        frame.setFocusable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame.pack(); //resize frame appropriately for its content
        frame.setLocation(new Point((dimension.width/2)-(frame.getWidth()/2), (dimension.height/2)-(frame.getHeight()/2)));   // positions frame in center of screen             
        frame.setVisible(true);
        frame.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(source == enterButton)
        {
            if(getUsername().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(frame, "Please enter a username!");
            }
            else if(getHostIP().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(frame, "Please enter the servers IP Address!");
            }
            else
            {
                client.setClientID(getUsername());
                client.setHostIP(getHostIP());
                frame.dispose();
                MAIN.setupFinished = true;
            }
        }
    }
    
    
    
    //GETTERS
    public String getUsername()
    {
        return clientIDTextField.getText();
    }
    
    public String getHostIP()
    {
        return IPTextField.getText();
    }
    
}
