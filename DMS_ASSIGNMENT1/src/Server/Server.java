/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 * Responsible for maintaining connections of clients
 */
class Server
{
    private static final int PORT = 2018;
    
    private boolean running = true;
    private SendClientList sendClientList;
    private ConcurrentHashMap<String, Connection> connectedClients;
    
    private DatagramSocket udpDatagramSocket;
    private ServerSocket serverSocket;
    
    public Server()
    {
        connectedClients = new ConcurrentHashMap<>();
        try {
            udpDatagramSocket = new DatagramSocket(PORT);
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendClientList = new SendClientList(this, udpDatagramSocket);
    }
    
    public void startServer()
    {
        try
        {
            System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + PORT);
            
            new Thread(sendClientList).start();
            while(running)
            {
                Socket socket = serverSocket.accept(); // Wait until a new client has connected
                System.out.println("Client with address: " + socket.getInetAddress() + " has connected");
                Connection connection = new Connection(socket, this); // Crete new connection for that client
                new Thread(connection).start(); // Begin connection thread between that client and the server
            }
            serverSocket.close();
            System.out.println("Server shutting down");
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //GETTERS AND SETTERS
    
    public boolean getRunning()
    {
        return running;
    }
    
    public ConcurrentHashMap<String, Connection> getConnectedClients()
    {
        return connectedClients;
    }
}
