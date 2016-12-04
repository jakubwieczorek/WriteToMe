package Wieczorek.Jakub.ChatApplication.Client;

import Wieczorek.Jakub.ChatApplication.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Logic part of MVC patern for user.
 * 
 * @author Jakub Wieczorek
 * @version 1.1
 */
public class Model 
{
    /**
     * Instance of the Cliet class.
     * 
     * @see Wieczorek.Jakub.ChatApplication.Model.Client
     */
    Client client;
    
    /**
     * Constructor 
     * 
     * @param userName username for user.
     */
    public Model(String userName)
    {
        this.client = new Client(userName);
    }
    
    /**
     * Constructor 
     */
    public Model()
    {
        this.client = new Client();
    }
    
    /**
     * Inner class created to organise Model class.
     * 
     * @author Jakub Wieczorek
     * @version 1.1
     */
    class Client 
    {
        /**
         * Allows to connect with server.
         */
        Socket socket;
        
        /**
         * User username 
         */
        String userName;
        
        /**
         * The same port which server is still hearing.
         * 
         * @see Wieczorek.Jakub.ChatApplication.ChatServer
         */
        final int PORT;
        
        /**
         * By this PrintWriter instance, Client instance can send messages to the server.
         */
        PrintWriter printWriter;
        
        /**
         * Constructor.
         * 
         * @param userName is username for user. 
         */
        public Client(String userName)
        {
            this();
            
            this.userName = userName;
            
            // first thing after logged is to send username.
            this.printWriter.println(userName);
        }
        
        /**
         * Constructor. Programist must send UserName and send them on his own after create Client.
         */
        public Client()
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
        
        /**
         * @return inputstream for this socket.
         * @throws IOException 
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
         * @see Message
         */
        public void sendMsg(Message msg)
        {
            msg.send(this.printWriter);
        }
        
        /**
         * setter for username.
         * 
         * @param name is the new name for user
         */
        public void setUserName(String name)
        {
            this.userName = name;
            this.printWriter.println(this.userName);
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
}