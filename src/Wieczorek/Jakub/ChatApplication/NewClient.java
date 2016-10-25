package Wieczorek.Jakub.ChatApplication;

import java.net.Socket;
import java.io.DataOutputStream;
import java.io.IOException;

public class NewClient implements Runnable{
    Socket socket;
    String name;
    ChatServer parent;
    
    /**
     * Constructor to the NewClient class. 
     * 
     * @param socket is an instance of the Socket class.
     * @param parent is an object where NewClient instance is created.
     * 
     * @see Wieczorek.Jakub.ChatApplication.ChatServer
     */
    public NewClient(Socket socket, ChatServer parent)
    {
        this.parent = parent;
        this.socket = socket;
    }
    
    @Override
    public void run()
    {   
        try
        {            
            // if isn't connected all user should get this information
            if(!socket.isConnected())
            {
                parent.clients.remove(this); // if isn't connected shouldn't be in list of clients

                // here all active clients learnt that these customer isn't connected
                for(NewClient client : parent.clients)
                {
                    DataOutputStream tempOutput = new DataOutputStream(client.socket.getOutputStream());
                    tempOutput.writeUTF(this.socket.getLocalAddress().getHostName() + " is Disconnected");
                }
            }        
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }    
    }
    
    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }
    
    public Socket getSocket()
    {
        return this.socket;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
}
