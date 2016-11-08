package Wieczorek.Jakub.ChatApplication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller 
{
    View theView;
    Model theModel;
    
    public Controller(String userName)
    {
        this.theModel = new Model(userName);
        
        try 
        {
            this.theView = new View(theModel.client.getInputStream());
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
                theModel.client.sendMsg(theView.getMessageToSend());
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
       
    }
}
