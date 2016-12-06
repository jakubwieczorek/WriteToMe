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

    void returnInformationAboutExistance(boolean isExist, String matesUserName) 
    {
        Message returnInformation = new Message();
            
        returnInformation.setFlag(Protocol.PERSON_INQUIRE);
        if(isExist)
        {
            returnInformation.setText("This mate exists!");
            returnInformation.send(this.printWriter);

            returnInformation.setFlag(Protocol.PERSON_EXIST);
        }
        else
        {
            returnInformation.setText("This mate doesn't exist");
            returnInformation.send(this.printWriter);

            returnInformation.setFlag(Protocol.PERSON_DONT_EXIST);
        }
        
        returnInformation.setText(matesUserName);
        returnInformation.send(this.printWriter);
    }

    void giveInviteInformation(PrintWriter matePrintWriter) 
    {
        Message informationToAddedMate = new Message();
        
        // give information that this client add him to friends
        informationToAddedMate.setFlag(Protocol.PERSON_INQUIRE);
        informationToAddedMate.setText(this.userName + " add You to mates!");
        informationToAddedMate.send(matePrintWriter);

        informationToAddedMate.setFlag(Protocol.PERSON_EXIST);
        informationToAddedMate.setText(this.userName);
        informationToAddedMate.send(matePrintWriter);
    }

    void sendInformationAboutExitToMates(ArrayList<Server.ControllerServerClient> mates) 
    {
        
    }

    void giveAddedInformation(PrintWriter matePrintWriter) 
    {
        Message informationToAddedMate = new Message();
        
        // give information that this client add him to friends
        informationToAddedMate.setFlag(Protocol.AGREE);
        informationToAddedMate.setText(this.userName + " add You to mates!");
        informationToAddedMate.send(matePrintWriter);

        informationToAddedMate.setFlag(Protocol.PERSON_EXIST);
        informationToAddedMate.setText(this.userName);
        informationToAddedMate.send(matePrintWriter);
    }

    void returnInformationAboutUserName(boolean isExist) 
    {
        Message returnInformation = new Message();
            
        if(!isExist)
        {
            returnInformation.setText("This username is free");
            returnInformation.setFlag(Protocol.PERSON_DONT_EXIST);
        }
        else
        {
            returnInformation.setText("This username is occupied");
            returnInformation.setFlag(Protocol.PERSON_EXIST);
        }

        returnInformation.send(this.printWriter);
    }
}
