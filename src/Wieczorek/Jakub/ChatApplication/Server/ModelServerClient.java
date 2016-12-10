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
            //if he is in local mates
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
        new Message(flag, msg).send(getPrintWriter());
    }

    void initiateUsersData(Server.ControllerServerClient person) 
    {
        
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
}
