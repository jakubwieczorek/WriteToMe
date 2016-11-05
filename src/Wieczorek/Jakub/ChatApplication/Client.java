package Wieczorek.Jakub.ChatApplication;

import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {
    Socket socket;
    String userName;
    final int PORT;
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    ClientReceiver receiverMsges;
    
    public Client()
    {
        this.PORT = 1550;
        
        try
        {
            this.socket = new Socket("localhost", this.PORT);
            this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);
            
            // read from command line
            this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            
            receiverMsges = new ClientReceiver(this.socket);
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }  
    }
    
    public void startConversation()
    {
        this.receiverMsges.thread.start();
        
        // here have to implement thread who will be get messages 
        try
        {
           this.printWriter.println(this.userName);
            
            while(true)
            {
                // read msg from command line 
                String outputMsg = this.bufferedReader.readLine();
                // and send it
                this.printWriter.println(outputMsg);
            } 
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        } 
    }
    
    public void setUserName(String name)
    {
        this.userName = name;
    }
    
    public String getUserName()
    {
        return this.userName;
    }
}
