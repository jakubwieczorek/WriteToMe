package Wieczorek.Jakub.ChatApplication.Client;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Model part of MVC patern for user.
 * 
 * @author Jakub Wieczorek
 * @version 1.1
 */
public class Model 
{
    /**
     * Constructor 
     */
    public Model()
    {
        this.PORT = 1550;

        try
        {
            this.socket = new Socket("localhost", this.PORT);
            this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    private Socket socket;

    private String userName;

    private final int PORT;

    private PrintWriter printWriter;

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
                    sendMsg(new Message(Protocol.LOGGED, ""));
                    
                    System.out.println(getUserName() + " sended signal");
                }
            }
        , 5000, 5000);
    }

    /**
     * @return inputstream for this socket.
     * @throws IOException is thrown when IOException occurrs.
     */
    public InputStream getInputStream() throws IOException
    {
        return this.socket.getInputStream();
    }

    /**
     * Firts send flag to the server, then contents of the message.
     * 
     * @param msg must be created, that the contents field like forWho:text and the flag field 
     * should be assigned by proper protocol parameter.
     * 
     * @see Wieczorek.Jakub.ChatApplication.Message
     */
    public void sendMsg(Message msg)
    {
        msg.send(this.printWriter);
    }

    /**
     * Setter for username and password. After setting model sends information to server about username
     * and password.
     * 
     * @param userName is username for user.
     * @param password is password for account.
     */
    public void setUserNameAndPassword(String userName, String password)
    {
        this.userName = userName;
        this.printWriter.println(this.userName + ":" + password);
    }

    /**
     * getter for userName.
     * 
     * @return userName
     */
    public String getUserName()
    {
        return this.userName;
    }
}