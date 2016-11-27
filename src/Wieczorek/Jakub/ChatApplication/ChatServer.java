package Wieczorek.Jakub.ChatApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

final public class ChatServer {
    /**
     * Keeping all clients as a new threads.
     * 
     * @see Wieczorek.Jakub.ChatApplication.ChatServerClient
     */
    public ArrayList<ChatServerClient>clients; 
    
    /**
     * PORT to server field.
     */
    final int PORT;
    ServerSocket server;
    
    public ChatServer()
    {
        this.clients = new ArrayList<>();
        this.PORT = 1550;
    }// mac = numer karty sieciowej -  rozmawia z najblizszym komputerem
    
    // skm i skm, tin, bss
    
    public static void main(String [] args)
    {   
        ChatServer chatServer = new ChatServer(); // create instance
        chatServer.runServer(chatServer);
    }
    
    void runServer(ChatServer chatServer)
    {
        try
        {   
            chatServer.server = new ServerSocket(chatServer.PORT); // create server - only one for all customers
            
            System.out.println("Waiting for clients..");
            
            while(true)
            {   
                // here server is waiting for client
                Socket socket = chatServer.server.accept();    
                
                // newClient is created with gotten socket 
                ChatServerClient newClient = new ChatServerClient(socket, chatServer);
                
                // newClient is added to database of clients
                chatServer.clients.add(newClient);

                // now client in server lives independently, when is called .start() method in newClient is called .run() method
                newClient.thread.start();
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
}