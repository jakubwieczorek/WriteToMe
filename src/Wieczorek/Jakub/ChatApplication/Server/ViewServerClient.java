package Wieczorek.Jakub.ChatApplication.Server;

import Wieczorek.Jakub.ChatApplication.Message;
import java.io.BufferedReader;
import java.io.IOException;

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
            return getBufferedReader().readLine();
        }
        catch(IOException ex)
        {
            throw new IOException("Not be able to read userName");
        }
    }

    Message getMessage() throws IOException
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
