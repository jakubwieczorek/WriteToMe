package Wieczorek.Jakub.ChatApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

final public class ChatServer {
    /**
     * Keeping all clients as a new threads.
     * 
     * @see Wieczorek.Jakub.ChatApplication.NewClient
     */
    public ArrayList<NewClient>clients; 
    
    /**
     * PORT to server field.
     */
    final int PORT;
    ServerSocket server;
    
    public ChatServer()
    {
        this.clients = new ArrayList<>();
        this.PORT = 1550;
    }
    
    public static void main(String [] args)
    {   
        try
        {   
            ChatServer chatServer = new ChatServer(); // create instance
            chatServer.server = new ServerSocket(chatServer.PORT); // create server - only one for all customers
            
            System.out.println("Waiting for clients..\n");
            
            while(true)
            {   
                Socket socket = chatServer.server.accept();               
                NewClient newClient = new NewClient(socket, chatServer);
                chatServer.clients.add(newClient);
                System.out.println("New Client !!\n");

                // Now Server must get from new user his name, and announce his name to different users.
                chatServer.getNameFromNewUser(newClient); // here can be IOExceptions
                
                // I use threads, because all users can communicate with each other independently
                Thread thread = new Thread(newClient); 
                thread.start();
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    private void getNameFromNewUser(NewClient newClient) throws IOException
    {
        DataInputStream input = new DataInputStream(newClient.socket.getInputStream()); // input, server receives messages.
        DataOutputStream output; 
        
        String newClientsName = input.readUTF(); // client should send his name for server
        
        newClient.setName(newClientsName); // this name is assigned to newClient from invokeing method.
        System.out.println("New Client's name " + newClientsName);
        // now server have to send message that new user has just logged
        for(NewClient client : this.clients) 
        {
            output = new DataOutputStream(client.socket.getOutputStream()); // so server must get outputstream from all clients
            output.writeUTF(newClientsName + " is logged."); // and send them message.
        }
    }
}