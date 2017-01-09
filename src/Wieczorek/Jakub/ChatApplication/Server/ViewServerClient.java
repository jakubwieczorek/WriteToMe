package Wieczorek.Jakub.ChatApplication.Server;

import Wieczorek.Jakub.ChatApplication.Message;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * View part of MVC pattern for server.
 * 
 * @author Jakub Wieczorek
 * 
 * @version 1.1
 */
public class ViewServerClient
{
    private BufferedReader bufferedReader;
    
    /**
     * Constructor
     * 
     * @param bufferedReader is sockets bufferedReader
     */
    public ViewServerClient(BufferedReader bufferedReader)
    {
        this.bufferedReader = bufferedReader;
    }

    /**
     * @return userName
     * 
     * @throws IOException
     */
    String getUserName() throws IOException
    {
        try
        {
            return getBufferedReader().readLine();
        }
        catch(IOException ex)
        {
            throw new IOException("Not be able to read userName");
        }
    }

    /**
     * @return msg
     */
    Message getMessage()
    {
        Message msg = new Message();
        
        msg.receive(this.getBufferedReader());
        
        return msg;
    }

    /**
     * @return the bufferedReader
     */
    public BufferedReader getBufferedReader() 
    {
        return bufferedReader;
    }

    /**
     * @param bufferedReader the bufferedReader to set
     */
    public void setBufferedReader(BufferedReader bufferedReader) 
    {
        this.bufferedReader = bufferedReader;
    }
}
