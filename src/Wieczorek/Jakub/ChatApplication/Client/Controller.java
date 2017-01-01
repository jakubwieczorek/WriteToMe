package Wieczorek.Jakub.ChatApplication.Client;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.IOException;

/**
 * @author Jakub Wieczorek
 * 
 * @version 1.1
 * 
 * Controller part of MVC pattern for user.
 */
public class Controller 
{
    private View theView;
    private Model theModel;
    
    
    /**
     * Default constructor. During creation instance of this class controller is communicating with
     * server in order to get username and password.
     */
    Controller()
    {
        this.theModel = new Model();
        
        String userName;
        String password;
                
        try
        {
            this.theView = new View(theModel.getInputStream(), this);        
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

                this.theModel.setUserNameAndPassword(userName, password);
                
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
    
    /**
     * Method is invoking from view level after for example clicking a button. Then model is updated
     * by the newest message and immidiately sends message to server.
     * 
     * @param msg is contents of the msg.
     * @param toWho is person who gets message.
     */
    void upgradeModelMsg(String msg, String toWho)
    {
        this.theModel.sendMsg(new Message(Protocol.MESSAGE, toWho + ":" + msg));              
    }
    
    /**
     * Method is invoking from view level after for example clicking a button. Then model is updated
     * by the newest person inquiry.
     * 
     * @param person is username of seeking person.
     */
    void upgradeModelMateInquire(String person)
    {
        this.theModel.sendMsg(new Message(Protocol.PERSON_INQUIRE, person));               
    }
    
    private void startConversation()
    {        
        this.theView.receiverMessages.thread.start();
        
        this.theModel.startSendingConnection();
        
        this.theView.receiverMessages.startReceivingSignals();
    }

    /**
     * Method is invoking from view level after for example clicking a button. 
     * This is answer for invitation.
     * 
     * @param person is person who should receive answer.
     * @param flag is proper flag from Protocol class.
     * 
     * @see Wieczorek.Jakub.ChatApplication.Protocol
     */
    void upgradeModelMateAnswer(String person, char flag) 
    {
        theModel.sendMsg(new Message(Protocol.ANSWER, ""));

        theModel.sendMsg(new Message(flag, person));              
    }

    /**
     * Method sends signal to server when user exit program. 
     */
    void upgradeModelExitMsg()
    {
        this.theModel.sendMsg(new Message(Protocol.EXIT, ""));
    }

    /**
     * Method is invoking from view level after for example clicking button, when user wants
     * remove any mate.
     * 
     * @param userName is mates userName who will be removed.
     */
    void upgradeModelMateRemove(String userName) 
    {
        this.theModel.sendMsg(new Message(Protocol.REMOVE_MATE, userName));
    }
}
