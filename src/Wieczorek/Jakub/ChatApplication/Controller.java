package Wieczorek.Jakub.ChatApplication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller 
{
    ViewGui theView;
    Model theModel;
    ViewGui.GetUserNameWindow getUserNameWindow;
    
    /**
     * Constructor
     */
    public Controller()
    {
        this.getUserNameWindow = new ViewGui.GetUserNameWindow
        (
            (event)->
            {
                if(event.getSource() == getUserNameWindow.okButton)
                {
                    String userName;
                    
                    getUserNameWindow.userName = getUserNameWindow.inputUserName.getText();
                    userName = getUserNameWindow.getUserName();
                    getUserNameWindow.dispose();
                    
                    this.createViewAndModel(userName);
                }
            }
        );
    }
    
    /**
     * Metod created for shorten code lines, creates Model and View with obtained userName.
     * 
     * @param userName The username for user which will logg to the server.
     */
    private void createViewAndModel(String userName)
    {
        this.theModel = new Model(userName);
        
        try 
        {
            // here is implementation actionListener for the buttons. 
            this.theView = new ViewGui
            (userName, theModel.client.getInputStream(), 
            
                (event)->
                {
                    if(event.getSource() == theView.sendButton)
                    {
                        /* if user click for the sendButton, method in theModel instance will
                           send the message to the server: ToWho:text
                        */
                        String msg = theView.getMessageToSend();
                        String toWho = theView.listOfMates.getSelectedValue().toString();
                        theModel.client.sendMsg(new Message(Protocol.MESSAGE, toWho + ":" + msg));
                        theView.historyOfConversation.append("You send to " + toWho + " " + msg);
                        theView.textToSend.setText("");
                    }
                    else
                    {
                        if(event.getSource() == theView.addMateButton)
                        
                            /* if user click for the addMateButton, method in theModel instance will
                               send the inquiry to the server, wheter that person exist or not.
                            */
                            theModel.client.sendMsg(new Message(Protocol.PERSON_INQUIRE, theView.getPersonInquiry()));
                        }
                    }
                }
            );
        }
        catch(IOException ex) 
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.startConversation();
    }
    
    /**
     * Constructor
     * 
     * @param userName The username for user which will logg to the server.
     */
    public Controller(String userName)
    {
        this.createViewAndModel(userName);
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
