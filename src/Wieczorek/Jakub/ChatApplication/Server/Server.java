package Wieczorek.Jakub.ChatApplication.Server;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
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
    
    public ControllerServerClient findPerson(String userName)
    {
        for (ControllerServerClient person : this.clients) {
            if(person.userName.equals(userName))
                return person;
        }
        
        return null;
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
            
            // try initiate theView and theModel
            try
            {
                this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);
        
                this.theModel = new ModelServerClient(this.printWriter);
                
                this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                this.theView = new ViewServerClient(this.bufferedReader);
                
                // get userName from theView
                this.userName = this.theView.getUserName();
                
                while(parent.findPerson(this.userName) != null)
                {
                    this.theModel.returnInformationAboutUserName(true);
                    this.userName = this.theView.getUserName();
                }
                
                this.theModel.returnInformationAboutUserName(false);
                
                System.out.println("New client logged as " + this.userName);
            }
            catch(IOException ex)
            {
                System.err.println(ex.getMessage());
            }
            
            this.theModel.setName(this.userName);
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

                                // here server is looking for a receiver
                                ControllerServerClient receiver = theModel.findPerson(userName, this.mates);

                                // send msg to proper person
                                theView.sendMessage(this.userName,
                                        new PrintWriter(receiver.socket.getOutputStream(), true),
                                            msg);
                            }
                            catch(IllegalArgumentException ex)
                            {
                                System.err.println(ex.getMessage());
                            } 

                            break;
                        }
                        case Protocol.PERSON_INQUIRE:
                        {
                            // find mate
                            ControllerServerClient receiver = parent.findPerson(messageFromMe.getText());

                            if(receiver != null)
                            {
                                PrintWriter matePrintWriter = new PrintWriter(receiver.socket.getOutputStream(), true);

                                // return information about existance
                                theModel.returnInformationAboutExistance(true, messageFromMe.getText());
                                theModel.addMate(mates, receiver);

                                theModel.giveInviteInformation(matePrintWriter);
                                receiver.theModel.addMate(receiver.mates, this);
                            }
                            else
                                theModel.returnInformationAboutExistance(false, "");

                            break;
                        }
                        case Protocol.AGREE:
                        {
                            ControllerServerClient mate = parent.findPerson(messageFromMe.getText());
                            PrintWriter matePrintWriter = new PrintWriter(mate.socket.getOutputStream(), true);
                            
                            // add mate to mates
                            theModel.addMate(mates, mate);
                            theModel.giveAddedInformation(matePrintWriter);
                            // add me to mate's mates
                            mate.theModel.addMate(mate.mates, this);
                        }
                        
                        case Protocol.EXIT:
                        {
                            theModel.sendInformationAboutExitToMates(this.mates);
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
