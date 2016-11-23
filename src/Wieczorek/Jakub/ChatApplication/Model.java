package Wieczorek.Jakub.ChatApplication;

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
     * Flag, which indicated that user wants to send message.
     */
    static final char SEND_MESSAGE = '1';
    /**
     * Flag, which indicated that user wants to receive information about any user.
     */
    static final char SEND_PERSON = '2';
    
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
     * 
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
         * Constructor. Programist must send setUserName and send them on his own after create Client.
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
         * @param msgToSend must be like forWho:text.
         */
        public void sendMsg(String msgToSend)
        {
            this.printWriter.print(SEND_MESSAGE);
            this.printWriter.println(msgToSend);
        }
        
         /**
         * Firts send flag to the server, searching username.
         * 
         * @param msgToSend must be like forWho:text.
         */
        public void sendPersonInquire(String person)
        {
            this.printWriter.print(SEND_PERSON);
            this.printWriter.println(person);
        }
        
        /**
         * setter for username.
         * 
         * @param name is the new name for user
         */
        public void setUserName(String name)
        {
            this.userName = name;
            // add proper flag in order to server knows that user wants to change his username.
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
