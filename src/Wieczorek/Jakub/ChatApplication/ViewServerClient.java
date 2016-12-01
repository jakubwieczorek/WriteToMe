package Wieczorek.Jakub.ChatApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jakub
 */
public class ViewServerClient
{
    private BufferedReader bufferedReader;
    
    public ViewServerClient(BufferedReader bufferedReader)
    {
        this.bufferedReader = bufferedReader;
    }

    String getUserName() throws IOException
    {
        try
        {
            return bufferedReader.readLine();
        }
        catch(IOException ex)
        {
            throw new IOException("Not be able to read userName");
        }
    }

    Message getMessage() 
    {
        Message msg = new Message();
        
        try
        {
            msg.receive(this.bufferedReader);
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
        
        return msg;
    }

    void sendMessage(String fromWho, PrintWriter printWriter, String msg) 
    {
        // from who and contents
        new Message(Protocol.MESSAGE, fromWho + " wrote:" + msg).send(printWriter);
    }
}
