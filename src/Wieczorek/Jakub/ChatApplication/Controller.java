package Wieczorek.Jakub.ChatApplication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller 
{
    ViewGui theView;
    Model theModel;
    
    public Controller(String userName)
    {
        this.theModel = new Model(userName);
        
        try 
        {
            this.theView = new ViewGui(theModel.client.getInputStream());
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void startConversation()
    {        
        try
        {
            theView.receiverMessages.thread.start();
        
            while(true)
            {
                String msg = theView.getMessageToSend();
                //System.out.println(msg);
                theModel.client.sendMsg(msg);
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
       
    }
}
