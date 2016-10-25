package Wieczorek.Jakub.ChatApplication;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Client {
    Socket socket;
    final int PORT;
    DataOutputStream output;
    DataInputStream input;
    
    public Client()
    {
        this.PORT = 1550;
        
        try
        {
            this.socket = new Socket("localhost", this.PORT);
            input = new DataInputStream(this.socket.getInputStream());
            output = new DataOutputStream(this.socket.getOutputStream());
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }  
    }
    
    DataOutputStream getOutput()
    {
        return output;
    }
    
    DataInputStream getInput()
    {
        return input;
    }
}
