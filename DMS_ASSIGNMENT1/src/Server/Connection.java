/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Messages.BroadcastMessage;
import Messages.DisconnectMessage;
import Messages.IDAlreadyUsedMessage;
import Messages.Message;
import Messages.PrivateMessage;
import Messages.WelcomeMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 * Represents a connection between the client and the server.
 * It will handle what kinds of messages the client wants to send 
 * and make sure they are sent to the right people
 */
public class Connection implements Runnable
{
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;
    private Server server;
    String clientID;
    
    public Connection(Socket socket, Server server)
    {
        try
        {
            clientSocket = socket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch(IOException e) {
        }
        this.server = server;
    }
    
    @Override
    public void run()
    {
        try
        {
           Message receivedMessage;
           clientID = (String) in.readObject();
           
           if(server.getConnectedClients().keySet().contains(clientID))
           {
               // User with that ID is already online
                out.writeObject(new IDAlreadyUsedMessage());
           }
           else
           {
               System.out.println("Starting up new connection with " + clientID);
               server.getConnectedClients().put(clientID, this);
               out.writeObject(new WelcomeMessage(clientID));
               do
               {
                   receivedMessage = (Message) in.readObject();
                   if(receivedMessage instanceof PrivateMessage)
                   {
                       PrivateMessage privateMessage = (PrivateMessage)receivedMessage;
                       server.getConnectedClients().get(privateMessage.getReceiver()).out.writeObject(receivedMessage);
                   }
                   else if (receivedMessage instanceof BroadcastMessage)
                   {
                       for (String s : server.getConnectedClients().keySet())
                       {                         
                           Connection c = server.getConnectedClients().get(s);
                           c.out.writeObject(receivedMessage);
                       }
                   }
                   Thread.sleep(500);
               }while(!(receivedMessage instanceof DisconnectMessage)); // Stop after the user sends a disconnect request
           }
           
        } catch (IOException | ClassNotFoundException | InterruptedException ex)
        {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        { //Close the connections and threads
             try {
                if(out != null) out.close();
                if(in != null) in.close();
                if(clientSocket != null) clientSocket.close();
                if(server.getConnectedClients() != null && this.clientID != null) {
                    server.getConnectedClients().remove(this.clientID);
                }
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
    }
    
}
