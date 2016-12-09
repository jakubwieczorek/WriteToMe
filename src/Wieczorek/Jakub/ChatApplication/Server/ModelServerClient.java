package Wieczorek.Jakub.ChatApplication.Server;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author jakub
 */
public class ModelServerClient 
{
    String userName;
    PrintWriter printWriter;
    
    public ModelServerClient(String userName, PrintWriter printWriter) 
    {
        this.userName = userName;
        this.printWriter = printWriter;
    }
    
    public ModelServerClient(PrintWriter printWriter) 
    {
        this.printWriter = printWriter;
    }
    
    String [] splitUserNameAndMessage(String msg) throws IllegalArgumentException
    {
        if(msg.contains(":"))
        {
            String userAndMsg [] = msg.split(":", 2);
            return userAndMsg;
        }
        else
        {
            throw new IllegalArgumentException("Received message doesn't contains ':' character, so I can't find useruserName.");
        }
    }

    Server.ControllerServerClient findPerson(String userName, ArrayList<Server.ControllerServerClient>mates)
    {
        if(mates != null)
        {
            //if he is in local mates
            for(Server.ControllerServerClient mate : mates)
            {
                if(mate.userName.equals(userName))
                {
                    return mate;
                }
            }
        } 

        return null;
    }
    
    void addMate(ArrayList<Server.ControllerServerClient>mates, Server.ControllerServerClient mate)
    {
        mates.add(mate);
    }

    public void setName(String userName)
    {
        this.userName = userName;
    }

    public String getName()
    {
        return this.userName;
    }

    void returnInformationAboutExistance(String msg) 
    {   
        new Message(Protocol.PERSON_INQUIRE, msg).send(this.printWriter);
    }

    void giveInviteInformation(PrintWriter printWriter, char flag, String msg, String msgTwo) 
    {   
        // message type invitation
        new Message(Protocol.PERSON_INVITATION, msg).send(printWriter);
        
        new Message(flag, msgTwo).send(printWriter);
    }

    void sendInformationAboutExitToMates(ArrayList<Server.ControllerServerClient> mates) 
    {
        
    }

    void giveAddedInformation(PrintWriter printWriter, char who, char flag, String userName, String msg) 
    {
        new Message(Protocol.ANSWER, "").send(printWriter);
        
        new Message(who, msg).send(printWriter);
        
        new Message(flag, userName).send(printWriter);
    }

    void returnInformationAboutUserName(char flag, String msg) 
    {
        new Message(flag, msg).send(printWriter);
    }
}
