/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

import java.io.Serializable;

/**
 *
 * @author Davy
 * A class that holds a message and who it is from.
 * It (and all subclasses)  must be serializable so that it can be sent over the network.
 */
abstract public class Message implements Serializable
{
    protected String message;
    protected String sender;
    
    Message(String message, String sender)
    {
        this.message = message;
        this.sender = sender;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public String getSender()
    {
        return sender;
    }
}
