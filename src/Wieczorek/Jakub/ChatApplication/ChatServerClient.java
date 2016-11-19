package Wieczorek.Jakub.ChatApplication;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.PrintWriter;

public class ChatServerClient implements Runnable
{
    Socket socket;
    String name;
    ChatServer parent;
    Thread thread;
    ArrayList<ChatServerClient>mates;
    
    static final char TRUE = 't';
    static final char FALSE = 'f';
    
    /**
     * Constructor to the NewClient class. 
     * 
     * @param socket is the same socket as in client who registered to server
     * @param parent is an object where NewClient instance is created, thus parent is server object or NewClient object who add his mate to mates ArrayList.
     * @param name is username of client, now he must send it by simple message. In future will be sent automatically. 
     * @param mates is list of mates.
     * 
     * @see Wieczorek.Jakub.ChatApplication.ChatServer
     */
    public ChatServerClient(Socket socket, ChatServer parent)
    {
        this.parent = parent;
        this.socket = socket;
        
        // I use threads, because all users can communicate with each other independently
        this.thread = new Thread(this); 
        
        mates = new ArrayList<>();
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
            throw new IllegalArgumentException("Received message doesn't contains ':' character, so I can't find username.");
        }
    }
    
    ChatServerClient findPerson(String userName) throws NullPointerException
    {
        //if he is in local mates
        for(ChatServerClient mate : mates)
        {
            if(mate.name.equals(userName))
            {
                return mate;
            }
        }

        for(ChatServerClient person : parent.clients)
        {
            if(person.name.equals(userName))
            {
                this.mates.add(person); // add him to mates
                return person;
            }
        }  
        
        NullPointerException ex = new NullPointerException("I didn't find matching username.");
        
        throw ex;
    }
    
    @Override
    public void run()
    {   
        String message = null;
        
        try
        {           
            // getting from client input reader, so when client send message by for instance PrintWriter object
            // in this thread that message appears in InputStream 
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            
            // first message from client is username in future will be send automatically. 
            this.name = bufferedReader.readLine();
            System.out.println("New client logged as " + this.name);
            
            while(true)
            {
                char flag = (char)bufferedReader.read();
                
                message = bufferedReader.readLine();
                
                switch(flag)
                {
                    case Model.SEND_MESSAGE:
                    {
                        // here this client who send message, must find mate who should receive message.
                        String [] userNameAndMessage = new String[2];

                        // here split user and message
                        try
                        {
                            userNameAndMessage = splitUserNameAndMessage(message);

                            String userName = userNameAndMessage[0];
                            String msg = userNameAndMessage[1];

                            try
                            {
                                // here server is looking for a receiver
                                ChatServerClient receiver = findPerson(userName);

                                // send msg to proper person
                                PrintWriter printWriter = new PrintWriter(receiver.socket.getOutputStream(), true);

                                // from who and contents
                                printWriter.print(flag);
                                printWriter.println(this.name + " write:" + msg);
                            }
                            catch(NullPointerException ex)
                            {
                                System.err.println(ex.getMessage());
                            }
                        }
                        catch(IllegalArgumentException ex)
                        {
                            System.err.println(ex.getMessage());
                        } 
                        
                        break;
                    }
                    case Model.SEND_PERSON:
                    {
                        PrintWriter printWriterWhoseQuestion = new PrintWriter(this.socket.getOutputStream(), true);
                        
                        try
                        {   
                            ChatServerClient receiver = findPerson(message);
                            PrintWriter printWriter = new PrintWriter(receiver.socket.getOutputStream(), true);

                            printWriterWhoseQuestion.print(flag);
                            printWriterWhoseQuestion.println("This mate exists!");
                            printWriterWhoseQuestion.print(TRUE);
                            printWriterWhoseQuestion.println(message);
                            
                            printWriter.print(flag);
                            printWriter.println(this.name + " add You to mates!");
                            printWriter.print(TRUE);
                            printWriter.println(this.name);  
                        }
                        catch(NullPointerException ex)
                        {
                            printWriterWhoseQuestion.print(flag);
                            printWriterWhoseQuestion.println(ex.getMessage());
                            printWriterWhoseQuestion.print(FALSE);
                        }
                        
                        break;
                    }
                }
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
            
        } 
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
}
