package Wieczorek.Jakub.ChatApplication.Client;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.IOException;

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
        String password;
                
        try
        {
            this.theView = new View(theModel.client.getInputStream(), this);        
            this.theView.setVisible(true);
            
            String msgToDialog = "Input your username:";
            String msgToDialogPassword = "Input password:";
            
            while(true)
            {
                while(true)
                {
                    userName = this.theView.getUserName(msgToDialog);
                    
                    try
                    {
                        if(!userName.equals(""))
                        {
                            password = this.theView.getUserName(msgToDialogPassword);
                            
                            if(!password.equals(""))
                                break;
                        }
                    }
                    catch(NullPointerException ex)
                    {
                        this.theView.dispose();
                        System.exit(0);
                    }
                }

                this.theModel.client.setUserName(userName, password);
                
                Message returnInfo = new Message();
                returnInfo.receive(this.theView.getBufferedReaeder());
                
                System.out.println(returnInfo.getFlag());
                
                if(returnInfo.getFlag() == Protocol.PERSON_DONT_EXIST)
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

    void upgradeModelMateAnswer(String person, char flag) 
    {
        theModel.client.sendMsg(new Message(Protocol.ANSWER, ""));

        theModel.client.sendMsg(new Message(flag, person));              
    }

    void upgradeModelExitMsg()
    {
        this.theModel.client.sendMsg(new Message(Protocol.EXIT, ""));
    }

    void upgradeModelMateRemove(String userName) 
    {
        this.theModel.client.sendMsg(new Message(Protocol.REMOVE_MATE, userName));
    }
}
