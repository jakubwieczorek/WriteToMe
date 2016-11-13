package Wieczorek.Jakub.ChatApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class View 
{
    BufferedReader bufferedReader;
    ReceiverMessages receiverMessages;
    
    public View(InputStream inputStream)
    {
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.receiverMessages = new ReceiverMessages(inputStream);
    }

    public String getMessageToSend() throws IOException
    {
        return bufferedReader.readLine();
    }
    
    public class ReceiverMessages implements Runnable
    {
        BufferedReader bufferedReader;
        Thread thread;
        
        public ReceiverMessages(InputStream inputStream)
        {  
            this.thread = new Thread(this);
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        }
        
        public void printMessage(String msg)
        {
            System.out.println(msg);
        }
        
        @Override
        public void run()
        {
            try 
            {
                while(true)
                {
                    printMessage(this.bufferedReader.readLine());
                }
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
