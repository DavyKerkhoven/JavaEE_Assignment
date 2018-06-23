/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Davy
 * The entry point for the Server side program
 */
public class MAIN
{
    public static void main(String[] args)
    {
        Server server = new Server();
        server.startServer();
    }
}
