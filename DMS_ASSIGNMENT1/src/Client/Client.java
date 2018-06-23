/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Messages.BroadcastMessage;
import Messages.DisconnectMessage;
import Messages.IDAlreadyUsedMessage;
import Messages.Message;
import Messages.PrivateMessage;
import Messages.WelcomeMessage;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Davy
 * This class handles the connections to the server
 * as well as what types of messages it sends and receive to/from the server.
 * A message can be sent to everyone (broadcast) or to a particular person (private).
 * 
 */
public class Client implements Runnable
{
    public static final int PORT = 2018;
    private static String clientID = "";
    private static String hostIP = "";
    private static ClientSet connectedClients = new ClientSet();
    
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean connected;
    private ClientGUI gui;
    
    @Override
    public void run()
    {
        try
        {
            Message received;
            output.writeObject(getClientID()); //Send the clients name
            received = (Message) input.readObject();// recieve the message
            
            if (received instanceof WelcomeMessage)
            {
                gui.updateMessageScreen(received); //send welcome message to the clientGUI
            }
            else if (received instanceof IDAlreadyUsedMessage)
            {
                connected = false;
                JOptionPane.showMessageDialog(gui.getFrame(), "The username you have chosen is already in use! " +
                        "\nPlease try reconnecting using a different name");
                System.exit(-1);
            }
            
            while (connected)
            {
                received = (Message) input.readObject(); // Receive message
                gui.updateMessageScreen(received); // Print message to GUI
                Thread.sleep(300);
            }
            
        }catch (EOFException e)
        {
            connected = false; // End of stream reached. Disconnect.
        }
        catch (IOException | ClassNotFoundException | InterruptedException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void connectToServer()
    {
        Socket socket;
        
        try
        {
            socket = new Socket(hostIP, PORT);
             try
            {

                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                connected = true;
                new Thread(new UpdateList(this)).start(); // Start receiving status updates from server
                new Thread(this).start(); // Start receiving messages from server
            } catch (IOException ex)
            {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(gui.getFrame(), "Could not connect to a chat server running at: " + getHostIP());
        }       
    }
    
    public void sendBroadcastMessage()
    {
        if (output != null)
        {
            try 
            {
                if (connectedClients.size() == 0)
                {
                    JOptionPane.showMessageDialog(gui.getFrame(), "There is currently no one else online!");
                }
                else
                {
                    BroadcastMessage message = new BroadcastMessage(gui.getUserInput().getText(), getClientID());
                    gui.getUserInput().setText(""); // Clear user input box
                    output.writeObject(message); // Send broadcast
                }
            } catch (IOException ex)
            {
                JOptionPane.showMessageDialog(gui.getFrame(), "Error: You are not connected to the chat server!");
            }
        }
    }
    
    public void sendPrivateMessage()
    {
        if (output != null)
        {
            try
            {
                if (gui.getListOfClients().getSelectedValue() == null)
                {
                    JOptionPane.showMessageDialog(gui.getFrame(), "Please select someone to send a message to or click broadcast to send to everyone!");
                }
                else
                {
                    PrivateMessage message = new PrivateMessage(gui.getUserInput().getText(), clientID, gui.getListOfClients().getSelectedValue());
                    gui.getUserInput().setText("");// Clear user input box
                    output.writeObject(message); // Send broadcast
                    updateMessageScreen(message); // Write broadcast to GUI
                }
            } catch (IOException ex)
            {
                JOptionPane.showMessageDialog(gui.getFrame(), "Error: You are not connected to the chat server!");
            }               
        }
    }
    
    
    public void disconnect()
    {
        if(output != null)
        {
            try
            {
                if(connected)
                {
                    connected = false;
                    DisconnectMessage dis = new DisconnectMessage();
                    output.writeObject(dis); // Request that you disconnect from the server
                    gui.updateMessageScreen(dis);
                }
            } catch (IOException ex)
            {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void updateMessageScreen(Message message)
    {
        gui.updateMessageScreen(message);
    }
    
    public void setClientGUI(ClientGUI clientGUI)
    {
        this.gui = clientGUI;
    }
    
    public ClientSet getClientSet()
    {
        return connectedClients;
    }
    
    public boolean getConnected()
    {
        return connected;
    }
    
    //GETTERS AND SETTERS
    public static void setClientID(String id) { clientID = id; }
    public static void setHostIP(String ip) { hostIP = ip; }
    public String getClientID() { return clientID; }
    public String getHostIP() { return hostIP; }

    
}
