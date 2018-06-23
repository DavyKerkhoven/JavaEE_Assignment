/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 *
 * @author Davy
 * A message to the client that another client has left the server.
 */
public class DisconnectClientMessage extends ServerMessage
{
    
    public DisconnectClientMessage(String clientID)
    {
        super(clientID + " has disconnected from the channel!");
    }
    
}
