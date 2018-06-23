/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davy
 * An entry point for the client side of the program
 */
public class MAIN
{

    public static boolean setupFinished = false;

    public static void main(String[] args)
    {
        Client client = new Client();
        ConnectScreen connectScreen = new ConnectScreen(client); //start the connection details screen

        //busy wait to make sure the user has input their details in the connection details screen
        while (!setupFinished)
        {
            try
            {
                Thread.sleep(500);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(MAIN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        client.connectToServer();
        ClientGUI clientGUI = new ClientGUI(client);
        clientGUI.startGUI();
    }
}
