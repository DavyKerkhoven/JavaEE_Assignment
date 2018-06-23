/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 *
 * @author Davy
 * A message that welcomes the user when they connect to the server.
 */
public class WelcomeMessage extends ServerMessage
{    
    public WelcomeMessage(String clientID)
    {
        super("Welcome to the chat channel: " + clientID + "!");
    }    
}
