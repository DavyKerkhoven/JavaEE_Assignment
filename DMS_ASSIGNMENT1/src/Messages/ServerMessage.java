/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

/**
 *
 * @author Davy
 * ServerMessage is an abstract class that helps define specific messages sent from the server to clients.
 */
abstract public class ServerMessage extends Message
{
    ServerMessage(String message)
    {
        super(message, "Server");
    }
}
