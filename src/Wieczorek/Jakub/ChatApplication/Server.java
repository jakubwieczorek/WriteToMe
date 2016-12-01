package Wieczorek.Jakub.ChatApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author jakub
 */
public class Server 
{   
    /**
     * Keeping all clients as a new threads.
     */
    public ArrayList<ControllerServerClient>clients;
    
    /**
     * PORT to server field.
     */
    final int PORT;
    ServerSocket server;
    
    public Server()
    {
        this.clients = new ArrayList<>();
        this.PORT = 1550;
        
        this.runServer();
    }
    
    private void runServer()
    {
        try
        {   
            this.server = new ServerSocket(this.PORT); // create server - only one for all customers
            
            System.out.println("Waiting for clients..");
            
            while(true)
            {   
                // here server is waiting for client
                Socket socket = this.server.accept();    
                
                // newClient is created with gotten socket 
                Server.ControllerServerClient newClient = new Server.ControllerServerClient(socket, this);
                
                // newClient is added to database of clients
                this.clients.add(newClient);

                // now client in server lives independently, when is called .start() method in newClient is called .run() method
                newClient.thread.start();
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    public class ControllerServerClient implements Runnable
    {
        ViewServerClient theView;
        ModelServerClient theModel;
        Server parent;
        
        Socket socket;
        BufferedReader bufferedReader;
        PrintWriter printWriter;
        
        String userName;
        Thread thread;
        ArrayList<ControllerServerClient>mates;

        /**
         * Constructor to the NewClient class. 
         * 
         * @param socket is the same socket as in client who registered to server
         * @param userName is useruserName of client, now he must send it by simple message. In future will be sent automatically. 
         * @param mates is list of mates.
         * 
         * @see Wieczorek.Jakub.ChatApplication.ChatServer
         */
        public ControllerServerClient(Socket socket, Server parent)
        {
            this.socket = socket;
            this.parent = parent;
            // I use threads, because all users can communicate with each other independently
            this.thread = new Thread(this); 

            this.mates = new ArrayList<>();
            
            // try initiate theView
            try
            {
                this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);

                // getting from client input reader, so when client send message by for instance PrintWriter object
                // in this thread that message appears in InputStream 
                this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                this.theView = new ViewServerClient(this.bufferedReader);
                
                // get userName from theView
                this.userName = this.theView.getUserName();
                System.out.println("New client logged as " + this.userName);
            }
            catch(IOException ex)
            {
                System.err.println(ex.getMessage());
            }
           
            this.theModel = new ModelServerClient(this.userName, this.printWriter);
        }
        
        @Override
        public void run()
        {   
            try
            {           
                while(true)
                {
                    
                    Message messageFromMe = this.theView.getMessage();

                    switch(Protocol.convert(messageFromMe.getFlag()))
                    {
                        case Protocol.MESSAGE:
                        {
                            // here this client who send message, must find mate who should receive message.
                            String [] userNameAndMessage = new String[2];

                            // here split user and message
                            try
                            {
                                userNameAndMessage = theModel.splitUserNameAndMessage(messageFromMe.getText());

                                String userName = userNameAndMessage[0];
                                String msg = userNameAndMessage[1];

                                try
                                {
                                    // here server is looking for a receiver
                                    ControllerServerClient receiver = theModel.findPerson(userName, parent.clients, this.mates);

                                    // send msg to proper person
                                    theView.sendMessage(this.userName,
                                            new PrintWriter(receiver.socket.getOutputStream(), true),
                                                msg);
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
                        case Protocol.PERSON_INQUIRE:
                        {
                            try
                            {   
                                // find mate
                                ControllerServerClient receiver = theModel.findPerson(messageFromMe.getText(), parent.clients, this.mates);
                                PrintWriter matePrintWriter = new PrintWriter(receiver.socket.getOutputStream(), true);

                                // return information about existance
                                theModel.returnInformationAboutExistance(true, messageFromMe.getText());
                                
                                theModel.giveInviteInformation(matePrintWriter);
                            }
                            catch(NullPointerException ex)
                            {
                                theModel.returnInformationAboutExistance(false, "");
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
    }
}
