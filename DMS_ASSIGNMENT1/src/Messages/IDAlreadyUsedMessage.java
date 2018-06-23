/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 *
 * @author Davy
 * Used to notify the client that they cannot use the name they chose.
 */
public class IDAlreadyUsedMessage extends ServerMessage
{
    
    public IDAlreadyUsedMessage()
    {
        super("That Name Is Already Being Used!");
    }
    
}
