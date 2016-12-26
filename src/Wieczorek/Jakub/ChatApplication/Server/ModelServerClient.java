package Wieczorek.Jakub.ChatApplication.Server;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jakub
 */
public class ModelServerClient 
{
    private String userName;
    private String password;
    private PrintWriter printWriter;
    private boolean logged;
    private Socket socket;
    private int loggedSignalNumber;
    private Server.ControllerServerClient parent;
    
    ConcurrentHashMap<String, Server.ControllerServerClient>mates, invitesFromMe, invitesToMe;
    
    
    void resetLoggedSignalNumber()
    {
        this.loggedSignalNumber = 0;
    }
    
    public ModelServerClient(PrintWriter printWriter) 
    {
        this.mates = new ConcurrentHashMap<>();
        this.invitesFromMe = new ConcurrentHashMap<>();
        this.invitesToMe = new ConcurrentHashMap<>();
        
        this.printWriter = printWriter;
    }
    
    String [] splitUserNameAndMessage(String msg) throws IllegalArgumentException
    {
        if(msg.contains(":"))
        {
            String userAndMsg [] = msg.split(":", 2);
            return userAndMsg;
        }

        throw new IllegalArgumentException("Received message doesn't contains ':' character, so I can't find useruserName.");
    }

    Server.ControllerServerClient findPerson(String userName)
    {
        return this.mates.get(userName);
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
        try
        {
            this.mates.entrySet().forEach
            (
                (mate) -> 
                {
                    new Message(Protocol.EXIT, this.userName).send(mate.getValue().getTheModel().getPrintWriter());
                }
            );
        }
        catch(NullPointerException ex)
        {
            System.err.println("sendInformationAboutExitToMates");
        }
    }

    void giveAddedInformation(PrintWriter printWriter, char who, char flag, String userName, String msg) 
    {
        new Message(Protocol.ANSWER, "").send(printWriter);
        
        new Message(who, msg).send(printWriter);
        
        new Message(flag, userName).send(printWriter);
    }
    
    void returnInformationAboutUserName(char flag, String msg) 
    {
        new Message(flag, msg).send(this.printWriter);
    }

    void initiateUsersData() 
    {
        new Message(Protocol.INITIATE, "").send(this.printWriter);
        
        try
        {
            mates.entrySet().forEach
            (
                (mate)-> 
                {
                    new Message(Protocol.MATE, mate.getValue().getTheModel().getUserName()).send(this.printWriter);
                    
                    if(mate.getValue().getTheModel().isLogged() == true)
                    {
                        new Message(Protocol.LOGGED, "").send(this.printWriter);
                    }
                    else
                    {
                        new Message(Protocol.UNLOGGED, "").send(this.printWriter);
                    }
                }
            );
        }
        catch(NullPointerException ex)
        {
            System.err.println("sendDataLoop");
        }
        
        new Message(Protocol.PERSON_INVITATION, "").send(this.printWriter);
        
        // send invities for me username
        this.sendDataLoop(Protocol.TO_ME, invitesToMe);
        
        // send invities from me username
        this.sendDataLoop(Protocol.FROM_ME, invitesFromMe);
        
        new Message(Protocol.INITIATE, "Initiation complete.").send(this.printWriter);
    }
    
    private void sendDataLoop(char flag, ConcurrentHashMap<String, Server.ControllerServerClient>mates)
    {
        try
        {
            mates.entrySet().forEach
            (
                (mate)-> 
                {
                    new Message(flag, mate.getValue().getTheModel().getUserName()).send(this.printWriter);
                }
            );
        }
        catch(NullPointerException ex)
        {
            System.err.println("sendDataLoop");
        }
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
    public void setSocket(Socket socket) 
    {
        this.socket = socket;
    }

    void setLists(Server.ControllerServerClient person) 
    {
        this.mates = person.getTheModel().mates;
        this.invitesFromMe = person.getTheModel().invitesFromMe;
        this.invitesToMe = person.getTheModel().invitesToMe;
    }
    
    void sendSocketInformationToMates() 
    {
        try
        {
            mates.entrySet().forEach
            (
                (mate)-> 
                {
                    mate.getValue().getTheModel().mates.get(this.userName).getTheModel().setSocket(this.socket); 
                    mate.getValue().getTheModel().mates.get(this.userName).getTheModel().setPrintWriter(this.printWriter);
                    mate.getValue().getTheModel().mates.get(this.userName).getTheView().setBufferedReader(parent.getTheView().getBufferedReader());
                    
                    mate.getValue().getTheModel().mates.get(this.userName).getTheModel().setLogged(true);
                    
                    new Message(Protocol.LOGGED, this.userName).send(mate.getValue().getTheModel().getPrintWriter());
                }
            );
            
            System.out.println("Sended");
        }
        catch(NullPointerException ex)
        {
            System.err.println("sendSocketInformationToMates + mates");
        }
        
        try
        {
            invitesFromMe.entrySet().forEach
            (
                (mate)-> 
                {
                    mate.getValue().getTheModel().invitesFromMe.get(this.userName).getTheModel().setSocket(this.socket);
                    mate.getValue().getTheModel().invitesFromMe.get(this.userName).getTheModel().setPrintWriter(this.printWriter);
                    mate.getValue().getTheModel().invitesFromMe.get(this.userName).getTheView().setBufferedReader(parent.getTheView().getBufferedReader());
                }
            );
        }
        catch(NullPointerException ex)
        {
            System.err.println("sendSocketInformationToMates + ivitesFromMe");
        }
        
        try
        {
            invitesToMe.entrySet().forEach
            (
                (mate)-> 
                {
                    mate.getValue().getTheModel().invitesToMe.get(this.userName).getTheModel().setSocket(this.socket);
                    mate.getValue().getTheModel().invitesToMe.get(this.userName).getTheModel().setPrintWriter(this.printWriter);
                    mate.getValue().getTheModel().invitesToMe.get(this.userName).getTheView().setBufferedReader(parent.getTheView().getBufferedReader());
                }
            );
        }
        catch(NullPointerException ex)
        {
            System.err.println("sendSocketInformationToMates + invitesToMe");
        }
    }
    
    /**
     * @return the logged
     */
    public boolean isLogged() 
    {
        return logged;
    }

    /**
     * @param logged the logged to set
     */
    public void setLogged(boolean logged) 
    {
        this.logged = logged;
    }

    void giveRemoveInformationToMates(String mateToRemove) 
    {
        new Message(Protocol.REMOVE_MATE, this.userName).send(this.mates.get(mateToRemove).getTheModel().getPrintWriter());

        this.mates.remove(this.mates.get(mateToRemove).getTheModel().getUserName());
    }
    
    int getLoggedSignalNuber()
    {
        return this.loggedSignalNumber;
    }
    
    void increaseSignalLogged() 
    {
        this.loggedSignalNumber++;
    }

    void sendMessage(char flag, String string) 
    {
        new Message(flag, string).send(this.printWriter);
    }

    /**
     * @return the parent
     */
    public Server.ControllerServerClient getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Server.ControllerServerClient parent) {
        this.parent = parent;
    }
}
