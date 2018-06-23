/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Messages.DisconnectClientMessage;
import Messages.NewClientMessage;
import Messages.RequestUpdateMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 * A thread that periodically asks the client for an update and checks the servers
 * list of clients with the clients list of clients, and makes adjustments to the client
 * side so they are up to date.
 */
public class UpdateList implements Runnable
{
    DatagramSocket udpSocket;
    boolean initialConnect;
    Client client;
    
    public UpdateList(Client client)
    {
        initialConnect = true;
        this.client = client;
        try
        {
            udpSocket = new DatagramSocket();
        } catch (SocketException ex)
        {
            Logger.getLogger(UpdateList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {
        try
        {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(new RequestUpdateMessage(client.getClientID()));
            byte[] buffer = byteOutputStream.toByteArray();
            
            // Used to receive connected client list.
                byte[] receivedStatus = new byte[1024]; // 1024/8 = 128. So should be able to support up to 128 users
                DatagramPacket clientStatus = new DatagramPacket(receivedStatus, receivedStatus.length);
                while (client.getConnected()) 
                {
                    // Request for an update
                    String hostIP = client.getHostIP();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(hostIP), Client.PORT);
                    udpSocket.send(packet); // Send request

                    udpSocket.receive(clientStatus); // Wait for update

                    byte[] receivedUpdate = clientStatus.getData();
                    // Convert byte array into ArrayList
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(receivedUpdate);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
                    ArrayList<String> clientList = (ArrayList<String>) objectInputStream.readObject();

                    clientList.remove(client.getClientID()); // Remove current client from being compared and added to GUI

                    // Compare server status against client status
                    ClientListUpdate update = compareServerListWithClientList(clientList);

                    // Notify client of newly connected users
                    for(String newClient : update.getNewClients()) 
                    {
                        client.getClientSet().add(newClient);
                        if(!initialConnect) { // If just connected don't notify for all clients already connected
                            client.updateMessageScreen(new NewClientMessage(newClient));
                        }
                    } if(initialConnect) initialConnect = false;
                    // Notify client of users who have disconnected
                    for(String disconnectedClient : update.getDisconnectedClients()) {
                        client.getClientSet().remove(disconnectedClient);
                        client.updateMessageScreen(new DisconnectClientMessage(disconnectedClient));
                    }
                    Thread.sleep(300); // Wait before requesting another update
                }
        } catch (IOException | InterruptedException | ClassNotFoundException ex)
        {
            Logger.getLogger(UpdateList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ClientListUpdate compareServerListWithClientList(ArrayList<String> serverStatus)
    {
        ClientListUpdate update = new ClientListUpdate(client);
        for (String activeClient : serverStatus)
        {
            update.disconnectedClients.remove(activeClient);
            if(!client.getClientSet().contains(activeClient))
            {
                update.newClients.add(activeClient);
            }
        }
        return update;
    }
    
    /**
     * A helper class to help compare new clients with disconnected ones.
     */
    private class ClientListUpdate
    {
        ArrayList<String> newClients;
        ArrayList<String> disconnectedClients;
        Client client;
        
        public ClientListUpdate(Client client)
        {
            this.client = client;
            newClients = new ArrayList<>();
            disconnectedClients = client.getClientSet().getListOfClients();
        }
        
        ArrayList<String> getNewClients() { return newClients; }
        ArrayList<String> getDisconnectedClients() { return disconnectedClients; }
    }
}
