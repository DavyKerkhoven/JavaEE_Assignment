/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 * Uses UDP to update the clients of the latest client list.
 */
public class SendClientList implements Runnable
{
    Server server;
    DatagramSocket udpDatagramSocket;
    
    public SendClientList(Server server, DatagramSocket udpDatagramSocket)
    {
        this.server = server;
        this.udpDatagramSocket = udpDatagramSocket;
    }
    @Override
    public void run()
    {
        while(server.getRunning()) //while the server is running
        {
            try
            {
                byte[] receivedRequest = new byte[100];
                DatagramPacket receivedPacket = new DatagramPacket(receivedRequest, receivedRequest.length);
                udpDatagramSocket.receive(receivedPacket); // Wait until update request is received
                
                // Convert list of connected clients to a byte array
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
                objectOutputStream.writeObject(new ArrayList<>(server.getConnectedClients().keySet()));
                byte[] buffer = byteOutputStream.toByteArray();
                
                // send packet containing update to the the client who requested it
                DatagramPacket clientStatus = new DatagramPacket(buffer, buffer.length, receivedPacket.getAddress(), receivedPacket.getPort());                       
                udpDatagramSocket.send(clientStatus);
                Thread.sleep(500); // Wait before starting over
                    
            } catch (IOException | InterruptedException ex)
            {
                Logger.getLogger(SendClientList.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
}
