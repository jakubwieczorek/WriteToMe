package Wieczorek.Jakub.ChatApplication.Server;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Model part of MVC pattern for server.
 * 
 * @author Jakub Wieczorek
 * 
 * @version 1.1
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
    
    /**
     * sets loggedSignalNumber to 0.
     */
    public void resetLoggedSignalNumber()
    {
        this.loggedSignalNumber = 0;
    }
    
    /**
     * Constructor
     * 
     * @param printWriter is printWriter from users socket.
     */
    public ModelServerClient(PrintWriter printWriter) 
    {
        this.mates = new ConcurrentHashMap<>();
        this.invitesFromMe = new ConcurrentHashMap<>();
        this.invitesToMe = new ConcurrentHashMap<>();
        
        this.printWriter = printWriter;
    }
    
    /**
     * Splits msg for two Strings: before : character and after.
     * 
     * @param msg is String which should contains : character.
     * @return Two elements Strings array. 
     * @throws IllegalArgumentException when : character doesn't occur.
     */
    public String [] splitUserNameAndMessage(String msg) throws IllegalArgumentException
    {
        if(msg.contains(":"))
        {
            String userAndMsg [] = msg.split(":", 2);
            return userAndMsg;
        }

        throw new IllegalArgumentException("Received message doesn't contains ':' character, so I can't find useruserName.");
    }

    /**
     * @return mate who has gotten userName.
     * 
     * @param userName is userName of seeking mate.
     */
    public Server.ControllerServerClient findPerson(String userName)
    {
        return this.mates.get(userName);
    }
    
    /**
     * Setter for userName
     * 
     * @param userName is users username.
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    /**
     * @return userName
     */
    public String getUserName()
    {
        return this.userName;
    }

    /**
     * Send information about existance to this user.
     * 
     * @param msg is message which will be sent to this user.
     */
    public void returnInformationAboutExistance(String msg) 
    {   
        new Message(Protocol.PERSON_INQUIRE, msg).send(this.getPrintWriter());
    }

    /**
     * Returns information to this user about invitation both to me and from me.
     * 
     * @param msg is message which will be sent to this user for example user who answered for invitation
     * @param msgTwo is second message.
     * @param flag is flag from Protocol.
     * 
     * @see Wieczorek.Jakub.ChatApplication.Protocol
     */
    public void giveInviteInformation(String flag, String msg, String msgTwo) 
    {   
        // message type invitation
        new Message(Protocol.PERSON_INVITATION, msg).send(this.printWriter);
        
        new Message(flag, msgTwo).send(this.printWriter);
    }

    /**
     * sends information about lost connection to mates.
     */
    public void sendInformationAboutExitToMates() 
    {
        try
        {
            this.mates.entrySet().forEach
            (
                (mate) -> 
                {
                    new Message(Protocol.EXIT, this.getUserName()).send(mate.getValue().getTheModel().getPrintWriter());
                }
            );
        }
        catch(NullPointerException ex)
        {}
    }

    /**
     * Model per each second sends information about connection in order to inform server
     * whether user has internet connection or computer breaks down etc.
     */
    public void startSendingConnection()
    {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate
        (
            new TimerTask() 
            {
                @Override
                public void run() 
                {
                    sendMessage(Protocol.SERVER_WORKS, "");
                }
            }
        , 5000, 5000);
    }
    
    /**
     * Sends message to me.
     * @param msg is second message.
     * @param flag is flag from Protocol.
     * 
     * @see Wieczorek.Jakub.ChatApplication.Protocol
     */
    void sendMessage(String flag, String string) 
    {
        new Message(flag, string).send(this.printWriter);
    }
    
    /**
     * Returns information to this user about answer on invitation.
     * 
     * @param userName is userName who accepted or refused invitation.
     * @param msg is message.
     * @param flag is flag from Protocol.
     * @param who is flag from Protocol TO_ME or FROM_ME
     * 
     * @see Wieczorek.Jakub.ChatApplication.Protocol
     */
    public void giveAddedInformation(String who, String flag, String userName, String msg) 
    {
        new Message(Protocol.ANSWER, "").send(this.printWriter);
        
        new Message(who, msg).send(this.printWriter);
        
        new Message(flag, userName).send(this.printWriter);
    }
    
     /**
     * Returns information to this user about occurrance of given username.
     * 
     * @param msg is message which will be sent to this user.
     * @param flag is flag from Protocol.
     * 
     * @see Wieczorek.Jakub.ChatApplication.Protocol
     */
    public void returnInformationAboutUserName(String flag, String msg) 
    {
        new Message(flag, msg).send(this.printWriter);
    }

    
    /**
     * Initiates users data: mates and invitations.
     */
    public void initiateUsersData() 
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
                        System.out.println(mate.getValue().getTheModel().getUserName() + " Logged");
                        new Message(Protocol.LOGGED, "").send(this.printWriter);
                    }
                    else
                    {
                        System.out.println(mate.getValue().getTheModel().getUserName() + " Unlogged");
                        new Message(Protocol.UNLOGGED, "").send(this.printWriter);
                    }
                }
            );
        }
        catch(NullPointerException ex)
        {}
        
        new Message(Protocol.PERSON_INVITATION, "").send(this.printWriter);
        
        // send invities for me username
        this.sendDataLoop(Protocol.TO_ME, invitesToMe);
        
        // send invities from me username
        this.sendDataLoop(Protocol.FROM_ME, invitesFromMe);
        
        new Message(Protocol.INITIATE, "Initiation complete.").send(this.printWriter);
    }
    
    private void sendDataLoop(String flag, ConcurrentHashMap<String, Server.ControllerServerClient>mates)
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
        {}
    }
    
    /**
     * @return the password
     */
    public String getPassword() 
    {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) 
    {
        this.password = password;
    }

    /**
     * @return the printWriter
     */
    public PrintWriter getPrintWriter() 
    {
        return printWriter;
    }

    /**
     * @param printWriter the printWriter to set
     */
    public void setPrintWriter(PrintWriter printWriter) 
    {
        this.printWriter = printWriter;
    }

    /**
     * @return the socket
     */
    public Socket getSocket() 
    {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) 
    {
        this.socket = socket;
    }
    
    
    /**
     * If user again will logged mates must receive information about socket, because on his computer
     * socket entirely different.
     */
    public void sendSocketInformationToMates() 
    {
        try
        {
            mates.entrySet().forEach
            (
                (mate)-> 
                {
                    mate.getValue().getTheModel().mates.get(this.getUserName()).getTheModel().setSocket(this.socket); 
                    mate.getValue().getTheModel().mates.get(this.getUserName()).getTheModel().setPrintWriter(this.printWriter);
                    mate.getValue().getTheModel().mates.get(this.getUserName()).getTheView().setBufferedReader(parent.getTheView().getBufferedReader());
                    
                    mate.getValue().getTheModel().mates.get(this.getUserName()).getTheModel().setLogged(true);
                    
                    new Message(Protocol.LOGGED, this.getUserName()).send(mate.getValue().getTheModel().getPrintWriter());
                }
            );
        }
        catch(NullPointerException ex)
        {}
        
        try
        {
            invitesFromMe.entrySet().forEach
            (
                (mate)-> 
                {
                    mate.getValue().getTheModel().invitesFromMe.get(this.getUserName()).getTheModel().setSocket(this.socket);
                    mate.getValue().getTheModel().invitesFromMe.get(this.getUserName()).getTheModel().setPrintWriter(this.printWriter);
                    mate.getValue().getTheModel().invitesFromMe.get(this.getUserName()).getTheView().setBufferedReader(parent.getTheView().getBufferedReader());
                }
            );
        }
        catch(NullPointerException ex)
        {}
        
        try
        {
            invitesToMe.entrySet().forEach
            (
                (mate)-> 
                {
                    mate.getValue().getTheModel().invitesToMe.get(this.getUserName()).getTheModel().setSocket(this.socket);
                    mate.getValue().getTheModel().invitesToMe.get(this.getUserName()).getTheModel().setPrintWriter(this.printWriter);
                    mate.getValue().getTheModel().invitesToMe.get(this.getUserName()).getTheView().setBufferedReader(parent.getTheView().getBufferedReader());
                }
            );
        }
        catch(NullPointerException ex)
        {}
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
    
    /**
     * Returns information to this user about invitation both to me and from me.
     * 
     * @param mateToRemove is mates username who will be removed.
     */
    public void giveRemoveInformationToMates(String mateToRemove) 
    {
        new Message(Protocol.REMOVE_MATE, this.getUserName()).send(this.mates.get(mateToRemove).getTheModel().getPrintWriter());

        this.mates.remove(this.mates.get(mateToRemove).getTheModel().getUserName());
    }
    
    /**
     * @return loggedSignalNumber
     */
    public int getLoggedSignalNuber()
    {
        return this.loggedSignalNumber;
    }
    
    /**
     * increase loggedSignalNumber;
     */
    public void increaseSignalLogged() 
    {
        this.loggedSignalNumber++;
    }

    /**
     * @return the parent
     */
    public Server.ControllerServerClient getParent() 
    {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Server.ControllerServerClient parent) 
    {
        this.parent = parent;
    }
}
