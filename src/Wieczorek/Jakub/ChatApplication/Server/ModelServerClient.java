package Wieczorek.Jakub.ChatApplication.Server;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author jakub
 */
public class ModelServerClient 
{
    private String userName;
    private String password;
    private PrintWriter printWriter;
    
    private Socket socket;

    ArrayList<Server.ControllerServerClient>mates;
    ArrayList<Server.ControllerServerClient>invitesFromMe;
    ArrayList<Server.ControllerServerClient>invitesToMe;
    
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
            for(Server.ControllerServerClient mate : mates)
            {
                if(mate.getTheModel().getUserName().equals(userName))
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

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return this.userName;
    }

    void returnInformationAboutExistance(String msg) 
    {   
        new Message(Protocol.PERSON_INQUIRE, msg).send(this.getPrintWriter());
    }

    void giveInviteInformation(PrintWriter printWriter, char flag, String msg, String msgTwo) 
    {   
        // message type invitation
        new Message(Protocol.PERSON_INVITATION, msg).send(printWriter);
        
        new Message(flag, msgTwo).send(printWriter);
    }

    void sendInformationAboutExitToMates() 
    {
        this.mates.forEach
        (
            (mate)->
            {
                new Message(Protocol.EXIT, this.userName).send(this.printWriter);
            }
        );
    }

    void giveAddedInformation(PrintWriter printWriter, char who, char flag, String userName, String msg) 
    {
        new Message(Protocol.ANSWER, "").send(printWriter);
        
        new Message(who, msg).send(printWriter);
        
        new Message(flag, userName).send(printWriter);
    }

    void returnInformationAboutUserName(char flag, String msg) 
    {
        new Message(flag, msg).send(getPrintWriter());
    }

    void initiateUsersData(Server.ControllerServerClient person) 
    {
        new Message(Protocol.INITIATE, "").send(this.printWriter);
        
        // send mates username
        this.sendDataLoop(Protocol.MATE, person.getTheModel().mates);
        
        new Message(Protocol.PERSON_INVITATION, "").send(this.printWriter);
        
        // send invities for me username
        this.sendDataLoop(Protocol.TO_ME, person.getTheModel().invitesToMe);
        
        // send invities from me username
        this.sendDataLoop(Protocol.FROM_ME, person.getTheModel().invitesFromMe);
        
        new Message(Protocol.INITIATE, "Initiation complete.").send(this.printWriter);
    }
    
    private void sendDataLoop(char flag, ArrayList<Server.ControllerServerClient>mates)
    {
        mates.forEach
        (
            (mate) -> 
            {
                new Message(flag, mate.getTheModel().getUserName()).send(this.printWriter);
            }
        );
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the printWriter
     */
    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    /**
     * @param printWriter the printWriter to set
     */
    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    void setLists(Server.ControllerServerClient person) 
    {
        this.mates = person.getTheModel().mates;
        this.invitesFromMe = person.getTheModel().invitesFromMe;
        this.invitesToMe = person.getTheModel().invitesToMe;
    }
    
    //*************************************
    void sendSocketInformationToMates(Server.ControllerServerClient myOldVersion) 
    {
        myOldVersion.getTheModel().mates.forEach
        (
            (mate)-> 
            {
                this.sendInfoAboutSocket(mate.getTheModel().mates);
            }
        );
        
        myOldVersion.getTheModel().mates.forEach
        (
            (mate)-> 
            {
                this.sendInfoAboutSocket(mate.getTheModel().invitesFromMe);
            }
        );
                
        myOldVersion.getTheModel().mates.forEach
        (
            (mate)-> 
            {
                this.sendInfoAboutSocket(mate.getTheModel().invitesToMe);
            }
        );
    }
    
    private void sendInfoAboutSocket(ArrayList<Server.ControllerServerClient>person)
    {
        
        for(Server.ControllerServerClient mate : person)
        {
            if(mate.getTheModel().getUserName().equals(this.userName))
            {
                mate.getTheModel().setSocket(this.socket);
                break;
            }
        }
    }
    //**************************************
}
