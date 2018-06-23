/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 *
 * @author Davy
 * This message will only go to the specified receiver.
 */
public class PrivateMessage extends Message
{
    private String receiver;
    
    public PrivateMessage(String message, String sender, String receiver)
    {
        super(message, sender);
        this.receiver = receiver;
    }
    
    public String getReceiver()
    {
        return receiver;
    }
}
