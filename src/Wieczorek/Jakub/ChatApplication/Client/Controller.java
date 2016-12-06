package Wieczorek.Jakub.ChatApplication.Client;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller 
{
    View theView;
    Model theModel;
    
    /**
     * Constructor
     */
    public Controller()
    {
        this.theModel = new Model();
        
        String userName;
                
        try
        {
            this.theView = new View(theModel.client.getInputStream(), this);        
            this.theView.setVisible(true);
            
            String msgToDialog = "";
            
            while(true)
            { 
                while(true)
                {
                    try
                    {
                        userName = this.theView.getUserName(msgToDialog);

                        if(!userName.equals(""))
                            break;
                    }
                    catch(NullPointerException ex){}
                }

                this.theModel.client.setUserName(userName);
                
                Message returnInfo = new Message();
                returnInfo.receive(this.theView.getBufferedReaeder());
                
                System.out.println(returnInfo.getFlag());
                
                if(Protocol.convert(returnInfo.getFlag()) == Protocol.PERSON_DONT_EXIST)
                {
                    this.theView.setUserName(userName);
                    break;
                }
                
                msgToDialog = "This username is occupied.\n";
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
        
        this.startConversation();
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
    * Start the thread in thread instance in theView.
    * To receive messages
    */
    public void startConversation()
    {        
        this.theView.receiverMessages.thread.start();
    }
}
