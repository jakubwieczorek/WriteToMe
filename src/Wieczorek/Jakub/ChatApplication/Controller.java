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
    
    /**
     * @param userName The username for user which will logg to the server.
     * 
     */
    public Controller(String userName)
    {
        this.theModel = new Model(userName);
        
        try 
        {
            // here is implementation actionListener for the buttons. 
            this.theView = new ViewGui
            (userName, theModel.client.getInputStream(), 
            
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        if(e.getSource() == theView.sendButton)
                        {
                            /* if user click for the sendButton, method in theModel instance will
                               send the message to the server: ToWho:text
                            */
                            theModel.client.sendMsg(theView.listOfMates.getSelectedValue().toString() + ":" + theView.getMessageToSend());
                        }
                        else
                        {
                            if(e.getSource() == theView.addMateButton)
                            {
                                /* if user click for the addMateButton, method in theModel instance will
                                   send the inquiry to the server, wheter that person exist or not.
                                */
                                theModel.client.sendPersonInquire(theView.getPersonInquiry());
                            }
                        } 
                    }  
                }   
            );
        }
        catch(IOException ex) 
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    * Start the thread in thread instance in theView.
    * 
    */
    public void startConversation()
    {        
        theView.receiverMessages.thread.start();
    }
}
