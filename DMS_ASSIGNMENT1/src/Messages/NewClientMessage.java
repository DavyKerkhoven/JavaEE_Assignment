/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 *
 * @author Davy
 * A message sent when a new client joins the chat channel.
 */
public class NewClientMessage extends ServerMessage
{
    public NewClientMessage(String clientID)
    {
        super(clientID + " has entered the chat channel!");
    }
}
