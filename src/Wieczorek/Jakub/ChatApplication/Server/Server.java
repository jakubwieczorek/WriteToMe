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
            if(person.getTheModel().getUserName().equals(userName))
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
                
                try
                {
                    // newClient is created with gotten socket 
                    Server.ControllerServerClient newClient = new Server.ControllerServerClient(socket, this);
                
                    // newClient is added to database of clients
                    this.clients.add(newClient);

                    // now client in server lives independently, when is called .start() method in newClient is called .run() method
                    newClient.thread.start();
                }
                catch(NullPointerException ex)
                {
                    System.err.println(ex.getMessage());
                }
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    public class ControllerServerClient implements Runnable
    {
        private ViewServerClient theView;
        private ModelServerClient theModel;
        Server parent;
        Thread thread;

        /**
         * Constructor to the NewClient class. 
         * 
         * @param socket is the same socket as in client who registered to server
         * @param userName is userls
         * Name of client, now he must send it by simple message. In future will be sent automatically. 
         * @param mates is list of mates.
         * 
         * @see Wieczorek.Jakub.ChatApplication.ChatServer
         */
        public ControllerServerClient(Socket socket, Server parent) throws NullPointerException
        {
            // try initiate theView and theModel
            try
            {    
                this.theModel = new ModelServerClient(new PrintWriter(socket.getOutputStream(), true));
                
                this.theModel.setSocket(socket);
                this.parent = parent;
                // I use threads, because all users can communicate with each other independently
                this.thread = new Thread(this); 

                this.theModel.mates = new ArrayList<>();
                this.theModel.invitesFromMe = new ArrayList<>();
                this.theModel.invitesToMe = new ArrayList<>();

                this.theView = new ViewServerClient(new BufferedReader(new InputStreamReader(this.theModel.getSocket().getInputStream())));
                
                // get userName and password from theView
                try
                {
                    String userNameAndPass [] = this.theModel.splitUserNameAndMessage(this.theView.getUserName());
                    
                    this.theModel.setUserName(userNameAndPass[0]);
                    this.theModel.setPassword(userNameAndPass[1]);
                }
                catch(IllegalArgumentException ex)
                {
                    System.out.println(ex.getMessage());
                }
                
                boolean newUser = true;
                ControllerServerClient person;
                // user in the same message must send username:password
                // if userName exists user must input password
                while(true)
                {
                    // if userName exist, server must check password
                    person = parent.findPerson(this.theModel.getUserName());
                    
                    // if person dont exist user is new user
                    if(person == null)
                    {
                        System.out.println("New client logged as " + this.theModel.getUserName());
                        break;
                    }
                    
                    // if user exist and he inputed proper password
                    if(this.theModel.getPassword().equals(person.getTheModel().getPassword()))
                    {
                        System.out.println(this.theModel.getUserName() + " input proper password");
                        // set proper flag in order to invoke function which will initate users data
                        newUser = false;
                        break;
                    }
                    
                    this.theModel.returnInformationAboutUserName(Protocol.PERSON_EXIST, "This username is occupied.");
                    try
                    {
                       String userNameAndPass [] = this.theModel.splitUserNameAndMessage(this.theView.getUserName());
                       this.theModel.setUserName(userNameAndPass[0]);
                       this.theModel.setPassword(userNameAndPass[1]);
                    }
                    catch(IllegalArgumentException ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                }
                
                this.theModel.returnInformationAboutUserName(Protocol.PERSON_DONT_EXIST, "");
                
                if(!newUser)
                {
                    if(person.getTheModel().isLogged())
                    {
                        person.getTheModel().returnInformationAboutUserName(Protocol.LOGGED_IN_DIFFERENT_DEVICE, "You've just logged at different device.");
                    }
                    
                    this.theModel.initiateUsersData(person);
                    // also in all mates list of mates now is neccessary to switch me and meoldversion
                    
                    this.theModel.sendSocketInformationToMates(person);
                    
                    this.theModel.setLists(person);
                    
                    person.getTheView().getBufferedReader().close();
                    
                    parent.clients.remove(person);
                }
                else
                {
                    this.theModel.setLogged(true);
                }
            }
            catch(IOException ex)
            {
                System.err.println(ex.getMessage());
            }
        }
        
        private void directMessage(Message messageFromMe) throws IOException
        {
            // here this client who send message, must find mate who should receive message.
            String [] userNameAndMessage = new String[2];

            // here split user and message
            try
            {
                userNameAndMessage = getTheModel().splitUserNameAndMessage(messageFromMe.getText());

                String userName = userNameAndMessage[0];
                String msg = userNameAndMessage[1];

                // here server is looking for a receiver
                ControllerServerClient receiver = getTheModel().findPerson(userName, this.theModel.mates);
                if(receiver != null)
                {
                    // send msg to proper person
                    getTheView().sendMessage(this.theModel.getUserName(),
                            new PrintWriter(receiver.getTheModel().getSocket().getOutputStream(), true),
                                msg);
                } 
            }
            catch(IllegalArgumentException ex)
            {
                System.err.println(ex.getMessage());
            } 
        }

        private void directPersonInquire(Message messageFromMe) throws IOException
        {
            if(messageFromMe.getText().equals(this.theModel.getUserName()))
            {
                this.getTheModel().returnInformationAboutExistance("You can't add yourself to mates.");
                return;
            }
                
            
            // find mate
            ControllerServerClient receiver = parent.findPerson(messageFromMe.getText());
                
            if(receiver != null)
            {
                PrintWriter matePrintWriter = new PrintWriter(receiver.getTheModel().getSocket().getOutputStream(), true);

                // give invite information to mate
                this.getTheModel().giveInviteInformation(matePrintWriter, Protocol.TO_ME, this.theModel.getUserName() + " invited You to mates!", this.theModel.getUserName());
               
                // give invite information to me 
                this.getTheModel().giveInviteInformation(this.theModel.getPrintWriter(), Protocol.FROM_ME, "You invited " + receiver.getTheModel().getUserName() + " to mates!", receiver.getTheModel().getUserName());

                receiver.getTheModel().invitesToMe.add(this);
                this.theModel.invitesFromMe.add(receiver);
            }
            else
                this.getTheModel().returnInformationAboutExistance("This person doesn't exist.");
        }

        private void directAnswer() throws IOException
        {   
            // mates name and agreement
            Message answer = this.getTheView().getMessage();

            ControllerServerClient mate = parent.findPerson(answer.getText());

            if(mate != null)
            {
                PrintWriter matePrintWriter = new PrintWriter(mate.theModel.getSocket().getOutputStream(), true);

                if(answer.getFlag() == Protocol.AGREE)
                {   
                    //System.out.println("ANSWER + AGREE");
                    // send information to me, becouse mate send to me invitation and I accept this invitation.
                    getTheModel().giveAddedInformation(this.theModel.getPrintWriter(), Protocol.TO_ME, Protocol.AGREE, mate.getTheModel().getUserName(), "You added " + mate.getTheModel().getUserName() + " to mates!");
                    // add mate to mates
                    getTheModel().addMate(this.theModel.mates, mate);
                    // send information to mate, flag is FROM_ME becouse he send information 
                    getTheModel().giveAddedInformation(matePrintWriter, Protocol.FROM_ME, Protocol.AGREE, this.theModel.getUserName(), this.theModel.getUserName() + " add You to mates!");
                    // add me to mate's mates
                    mate.getTheModel().addMate(mate.getTheModel().mates, this);
                }
                else
                if(answer.getFlag() == Protocol.DISAGREE)
                {
                    System.out.println("ANSWER + DISAGREE");
                    // send information TO_ME. I received information and I refuse it.
                    getTheModel().giveAddedInformation(this.theModel.getPrintWriter(), Protocol.TO_ME, Protocol.DISAGREE, mate.getTheModel().getUserName(), "You refused " + mate.getTheModel().getUserName() + " invitation.");

                    getTheModel().giveAddedInformation(matePrintWriter, Protocol.FROM_ME, Protocol.DISAGREE, this.theModel.getUserName(), this.theModel.getUserName() + " refuse your invitation.");
                }
                
                mate.getTheModel().invitesFromMe.remove(this);
                this.theModel.invitesToMe.remove(mate);
            }
        }                 
                            
        @Override
        public void run()
        {   
            try
            {           
                while(true)
                {
                    Message messageFromMe = this.getTheView().getMessage();

                    switch(messageFromMe.getFlag())
                    {
                        case Protocol.MESSAGE:
                        {
                            this.directMessage(messageFromMe);
                            
                            break;
                        }
                        case Protocol.PERSON_INQUIRE:
                        {
                            this.directPersonInquire(messageFromMe);

                            break;
                        }
                        case Protocol.ANSWER:
                        {
                            this.directAnswer();
                            
                            break;
                        }
                        case Protocol.EXIT:
                        {
                            getTheModel().setLogged(false);
                            getTheModel().sendInformationAboutExitToMates();
                            
                            break;
                        }
                        case Protocol.REMOVE_MATE:
                        {
                            this.directRemoveMate(messageFromMe);
                            
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

        /**
         * @return the theView
         */
        public ViewServerClient getTheView() {
            return theView;
        }

        /**
         * @param theView the theView to set
         */
        public void setTheView(ViewServerClient theView) {
            this.theView = theView;
        }

        /**
         * @return the theModel
         */
        public ModelServerClient getTheModel() 
        {
            return theModel;
        }

        /**
         * @param theModel the theModel to set
         */
        public void setTheModel(ModelServerClient theModel) 
        {
            this.theModel = theModel;
        }

        private void directRemoveMate(Message messageFromMe) 
        {
            this.theModel.giveRemoveInformationToMates(messageFromMe.getText());
        }
    }
}
