/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 *
 * @author Davy
 * Used to notify when the client wants to leave the server.
 */
public class DisconnectMessage extends ServerMessage
{
    
    public DisconnectMessage()
    {
        super("You have disconnected from the server!");
    }
    
}
