package Wieczorek.Jakub.ChatApplication;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ClientReceiver implements Runnable
{
    Socket socket;
    Thread thread;
    
    public ClientReceiver(Socket socket)
    {
        this.socket = socket;
        this.thread = new Thread(this);
    }
    
    @Override
    public void run()
    {   
        try
        {
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
           String incomingMsg;
           
           while(true)
           {
               incomingMsg = bufferedReader.readLine();
               System.out.println(incomingMsg);
           }
           
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
}
