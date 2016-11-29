package Wieczorek.Jakub.ChatApplication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller 
{
    ViewGui theView;
    Model theModel;
    
    /**
     * Constructor
     */
    public Controller()
    {
        new ViewGui.GetUserNameWindow(this);
    }
    
    void upgradeModelMsg(String msg, String toWho)
    {
        theModel.client.sendMsg(new Message(Protocol.MESSAGE, toWho + ":" + msg));              
    }
    
    void upgradeModelMateInquire(String person)
    {
        theModel.client.sendMsg(new Message(Protocol.PERSON_INQUIRE, person));               
    }
    
    /**
     * Metod created for shorten code lines, creates Model and View with obtained userName.
     * 
     * @param userName The username for user which will logg to the server.
     */
    void createViewAndModel(String userName)
    {
        this.theModel = new Model(userName);
        
        try 
        {
            this.theView = new ViewGui(userName, theModel.client.getInputStream(), this);    
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
    * To receive messages
    */
    public void startConversation()
    {        
        this.theView.receiverMessages.thread.start();
    }
}
