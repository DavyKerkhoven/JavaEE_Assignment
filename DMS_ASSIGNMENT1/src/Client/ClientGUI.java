/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Messages.BroadcastMessage;
import Messages.Message;
import Messages.PrivateMessage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Davy
 * Provides a GUI for the client;
 */
public class ClientGUI extends JPanel implements  ActionListener
{
    private Client client;
    private static final int WIDTH = 1150;
    private static final int HEIGHT = 640;
    private JFrame frame;
    private JList<String> listOfClients;
    private JTextArea messageScreen;
    private JTextArea userInput;
    private JButton sendButton;
    private JButton broadcastButton;
    
    
    public ClientGUI(Client client)
    {
        this.client = client;
        client.setClientGUI(this);
        
        frame = new JFrame("D&MS Assignment 1");
        
    }
    
    public void startGUI()
    {
        System.out.println("Starting client main GUI");
        
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                client.disconnect();
                System.exit(0);
            }
        });
        
        //MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(WIDTH, HEIGHT);
        
        //TOP PANEL (has the name Messenger)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(WIDTH, 30));
        topPanel.setBackground(Color.DARK_GRAY);
        
        JLabel title = new JLabel("   MESSENGER");
        title.setForeground(Color.white);
        topPanel.add(title, BorderLayout.WEST);
        
        mainPanel.add(topPanel);
        
        //BOT PANEL
        JPanel botPanel = new JPanel();
        //USER LIST PANEL
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setPreferredSize(new Dimension(WIDTH / 4, HEIGHT));
        listPanel.setBackground(Color.DARK_GRAY);
        listPanel.setForeground(Color.RED);
        
        listOfClients = new JList<String>(client.getClientSet().getClientListModel()); // online users list
        listOfClients.setForeground(Color.RED);
        JLabel listTitle = new JLabel("OTHER USERS:");
        listTitle.setForeground(Color.WHITE);
        listPanel.add(listTitle, BorderLayout.NORTH);
        listPanel.add(listOfClients, BorderLayout.CENTER);
        listOfClients.setBackground(Color.darkGray);
        listOfClients.setForeground(Color.white);
        
        //MESSAGEPANEL
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setSize(new Dimension(WIDTH/2, HEIGHT/3));
        messagePanel.setBackground(Color.red);
       
        messageScreen = new JTextArea();
        messageScreen.setEditable(false);
        messageScreen.setLineWrap(true);
        JScrollPane scrollTextPane = new JScrollPane(messageScreen);
        scrollTextPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTextPane.setPreferredSize(new Dimension(WIDTH*2/3, HEIGHT*2/3));
        scrollTextPane.setBorder(BorderFactory.createTitledBorder("Logged in as: " + client.getClientID()));
        messagePanel.add(scrollTextPane, BorderLayout.NORTH);
      
        // USER INPUT PANEL
        JPanel inputPanel = new JPanel(new FlowLayout());
        
        userInput = new JTextArea();
        userInput.setLineWrap(true);
        userInput.setSize(700, 100);
        JScrollPane inputPane = new JScrollPane(userInput);
        inputPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //inputPane.setPreferredSize(new Dimension(FRAME_WIDTH - LIST_WIDTH - 110, 60));
        inputPanel.add(inputPane);
        
        // BUTTONS
        sendButton = new JButton("   Private   ");
        sendButton.setPreferredSize(new Dimension(105, 29));
        sendButton.addActionListener(this);
        broadcastButton = new JButton("Broadcast");
        broadcastButton.setPreferredSize(new Dimension(105, 29));
        broadcastButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new  BoxLayout(buttonPanel, BoxLayout.Y_AXIS));   
        buttonPanel.add(sendButton);
        buttonPanel.add(broadcastButton);
        buttonPanel.setPreferredSize(new Dimension(105, 60));

        inputPanel.add(buttonPanel);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Message"));
        messagePanel.add(inputPanel, BorderLayout.CENTER);

        JPanel rightPane = new JPanel();
        rightPane.setSize(new Dimension(WIDTH, HEIGHT/2));
        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
        rightPane.add(messagePanel);
        
        //SPLIT PANE
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listPanel, rightPane);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        
        botPanel.add(splitPane, BorderLayout.EAST);
        mainPanel.add(botPanel);
  
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

    }
    
    /**
     * Displays incoming message to the screen.
     * Checks whether to append "Me" or "<Private>" or "<Public>" to the messages.
     * @param message The message being sent to the screen.
     */
    synchronized public void updateMessageScreen(Message message)
    {
        if (message.getSender().equals(client.getClientID())) // If it is a message you sent prefix it with "Me"
        {
            messageScreen.append("Me");
        }
        else
        {
            messageScreen.append(message.getSender());
        }
        
        if(message instanceof PrivateMessage)
        {
            if(message.getSender().equals(client.getClientID()))
            {
                messageScreen.append(" <Private> ");
            }else
            {
                messageScreen.append(" <Private to " + ((PrivateMessage) message).getReceiver() + "> ");
            }                    
        }else if (message instanceof BroadcastMessage)
        {
            messageScreen.append(" <Public> ");
        }
        messageScreen.append(": " + message.getMessage() + "\n");
    }
    
    

    /**
     * Button actions.
     * The same listener is used for both buttons
     * and sorts out which button was pressed in the method.
     * Also does a check if the user has put anything in the textfields.
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if (source == sendButton)
        {
            if(userInput.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(frame, "Please enter a valid message!");
            }
            else
            {
                client.sendPrivateMessage();
            }
        }
        else if (source == broadcastButton)
        {
            if (userInput.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(frame, "Please enter a valid message!");
            }
            else
            {
                client.sendBroadcastMessage();
            }
        }
    }
    
    //GETTERS AND SETTERS
    public JList<String> getListOfClients()
    {
        return listOfClients;
    }
    
    public JTextArea getUserInput()
    {
        return userInput;
    }

    public JFrame getFrame()
    {
        return frame;
    }
}
