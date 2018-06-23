/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 *
 * @author Davy
 * A message the client sends when it wants an update from the server.
 */
public class RequestUpdateMessage extends Message
{
    public RequestUpdateMessage(String sender)
    {
        super("Requesting Update", sender);
    }  
}
