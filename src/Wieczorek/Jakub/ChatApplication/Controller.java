package Wieczorek.Jakub.ChatApplication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Controller 
{
    ViewGui theView;
    Model theModel;
    
    public Controller(String userName)
    {
        this.theModel = new Model(userName);
        
        try 
        {
            this.theView = new ViewGui
            (theModel.client.getInputStream(), 
            
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        if(e.getSource() == theView.sendButton)
                        {
                            try
                            {
                                theModel.client.sendMsg(theView.getMessageToSend());
                            }
                            catch(IOException ex)
                            {

                            }
                        }
                    }  
                }   
            );
        }
        catch (IOException ex) 
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void startConversation()
    {        
        theView.receiverMessages.thread.start();
    }
}
