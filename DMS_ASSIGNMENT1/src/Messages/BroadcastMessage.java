/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 *
 * @author Davy
 * 
 * A message that is sent to all users on the server
 */
public class BroadcastMessage extends Message
{
    
    public BroadcastMessage(String message, String sender)
    {
        super(message, sender);
    }
    
}
