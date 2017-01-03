package Wieczorek.Jakub.ChatApplication.Server;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Server class.
 * 
 * @author Jakub Wieczorek
 * 
 * @version 1.1
 */
public class Server 
{   
    private  ConcurrentHashMap<String, ControllerServerClient>clients;
    
    private final int PORT;
    private ServerSocket server;
    
    /**
     * Constructor
     */
    public Server()
    {
        this.clients = new ConcurrentHashMap<>();
        this.PORT = 1550;
        
        this.runServer();
    }
    
    /**
     * @return seeking person.
     * 
     * @param userName is is username of seeking person.
     */
    synchronized public ControllerServerClient findPerson(String userName)
    {
        return this.clients.get(userName);
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
                    this.clients.put(newClient.getTheModel().getUserName(), newClient);

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

    /**
     * Client as a thread.
     */
    public class ControllerServerClient implements Runnable
    {
        private ViewServerClient theView;
        private ModelServerClient theModel;
        private Server parent;
        
        /**
         * Thread.
         * 
         * @see java.lang.Thread
         */
        Thread thread;
        private TimerTaskImpl timerTask;

        /**
         * Constructor to the NewClient class. 
         * 
         * @param socket is the same socket as in client who registered to server
         * @param parent is server. 
         * 
         * @throws NullPointerException when something will go wrong.
         * 
         * @see Wieczorek.Jakub.ChatApplication.Server.Server
         */
        public ControllerServerClient(Socket socket, Server parent) throws NullPointerException
        {
            // try initiate theView and theModel
            try
            {    
                this.theModel = new ModelServerClient(new PrintWriter(socket.getOutputStream(), true));
                
                this.theModel.setSocket(socket);
                this.theModel.setParent(this);
                            
                this.theModel.setLogged(true);  
                
                this.timerTask = new TimerTaskImpl();
                
                this.parent = parent;
                // I use threads, because all users can communicate with each other independently
                this.thread = new Thread(this); 

                this.theView = new ViewServerClient(new BufferedReader(new InputStreamReader(this.theModel.getSocket().getInputStream())));
                
                // get userName and password from theView
                String userNameAndPass [] = this.theModel.splitUserNameAndMessage(this.theView.getUserName());

                this.theModel.setUserName(userNameAndPass[0]);
                this.theModel.setPassword(userNameAndPass[1]);
                
                
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
                    
                    userNameAndPass = this.theModel.splitUserNameAndMessage(this.theView.getUserName());
                    this.theModel.setUserName(userNameAndPass[0]);
                    this.theModel.setPassword(userNameAndPass[1]);
                }
                
                this.theModel.returnInformationAboutUserName(Protocol.PERSON_DONT_EXIST, "");
                
                if(person != null && newUser != true)
                {
                    if(person.getTheModel().isLogged())
                    {
                        person.getTheModel().returnInformationAboutUserName(Protocol.LOGGED_IN_DIFFERENT_DEVICE, "You've just logged at different device.");
                    } 
                    
                    person.getTheModel().setLogged(true);
                    
                    person.getTheModel().setSocket(socket);
                    
                    person.getTheView().setBufferedReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
                    
                    person.getTheModel().setPrintWriter(new PrintWriter(socket.getOutputStream(), true));
                    
                    // also in all mates list of mates now is neccessary to switch me and meoldversion
                    person.getTheModel().sendSocketInformationToMates();
                    
                    person.getTheModel().initiateUsersData();
                    
                    person.getTimerTask().setSignalSended(false);
                }
                
                if(person != null && newUser != true)
                    throw new NullPointerException("Same user");
                
                this.theModel.startSendingConnection();
                
            }
            catch(IOException | IllegalArgumentException | NullPointerException ex)
            {
                System.err.println(ex.getMessage());
                
                throw new NullPointerException("Error during logging");
            }
        }
        
        private void getUserNameAndPass(Socket socket) throws IOException, NullPointerException
        {
            boolean newUser = true;
                
            ControllerServerClient person = null;
            
            String userNameAndPass [] = this.theModel.splitUserNameAndMessage(this.theView.getUserName());
            this.theModel.setUserName(userNameAndPass[0]);
            this.theModel.setPassword(userNameAndPass[1]);

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

                userNameAndPass = this.theModel.splitUserNameAndMessage(this.theView.getUserName());
                this.theModel.setUserName(userNameAndPass[0]);
                this.theModel.setPassword(userNameAndPass[1]);
            }
            
            this.theModel.sendMessage(Protocol.PERSON_DONT_EXIST, "");
            
            if(person != null && newUser == false)
                this.constructorForExistingClient(person, socket);
        }
        
        private void constructorForExistingClient(ControllerServerClient person, Socket socket) throws IOException 
        {
            if(person.getTheModel().isLogged())
            {
                person.getTheModel().returnInformationAboutUserName(Protocol.LOGGED_IN_DIFFERENT_DEVICE, "You've just logged at different device.");
            } 

            person.getTheModel().setLogged(true);

            person.getTheModel().setSocket(socket);

            person.getTheView().setBufferedReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));

            person.getTheModel().setPrintWriter(new PrintWriter(socket.getOutputStream(), true));

            // also in all mates list of mates now is neccessary to switch me and meoldversion
            person.getTheModel().sendSocketInformationToMates();

            person.getTheModel().initiateUsersData();

            person.getTimerTask().setSignalSended(false);
            person.getTheModel().resetLoggedSignalNumber();
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
                ControllerServerClient receiver = getTheModel().findPerson(userName);
                if(receiver != null)
                {
                    // send msg to proper person
                    receiver.getTheModel().sendMessage(Protocol.MESSAGE, this.theModel.getUserName() + " wrote: " + msg);
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
                // give invite information to mate
                receiver.getTheModel().giveInviteInformation(Protocol.TO_ME, this.theModel.getUserName() + " invited You to mates!", this.theModel.getUserName());
               
                // give invite information to me 
                this.getTheModel().giveInviteInformation(Protocol.FROM_ME, "You invited " + receiver.getTheModel().getUserName() + " to mates!", receiver.getTheModel().getUserName());

                receiver.getTheModel().invitesToMe.put(this.theModel.getUserName(), this);
                this.theModel.invitesFromMe.put(receiver.getTheModel().getUserName(), receiver);
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
                if(answer.getFlag() == Protocol.AGREE)
                {   
                    // send information to me, because mate send to me invitation and I accept this invitation.
                    this.getTheModel().giveAddedInformation(Protocol.TO_ME, Protocol.AGREE, mate.getTheModel().getUserName(), "You added " + mate.getTheModel().getUserName() + " to mates!");

                    if(mate.getTheModel().isLogged() == true)
                        getTheModel().sendMessage(Protocol.LOGGED, "");
                    else
                        getTheModel().sendMessage(Protocol.UNLOGGED, "");
                    
                    // add mate to mates
                    getTheModel().mates.put(mate.getTheModel().getUserName(), mate);
                    // send information to mate, flag is FROM_ME because he send information 
                    mate.getTheModel().giveAddedInformation(Protocol.FROM_ME, Protocol.AGREE, this.theModel.getUserName(), this.theModel.getUserName() + " add You to mates!");
                    
                    if(this.getTheModel().isLogged() == true)
                        mate.getTheModel().sendMessage(Protocol.LOGGED, "");
                    else
                        mate.getTheModel().sendMessage(Protocol.UNLOGGED, "");
                    
                    // add me to mate's mates
                    mate.getTheModel().mates.put(this.theModel.getUserName(), this);
                }
                else
                if(answer.getFlag() == Protocol.DISAGREE)
                {
                    System.out.println("ANSWER + DISAGREE");
                    // send information TO_ME. I received information and I refuse it.
                    this.getTheModel().giveAddedInformation(Protocol.TO_ME, Protocol.DISAGREE, mate.getTheModel().getUserName(), "You refused " + mate.getTheModel().getUserName() + " invitation.");

                    mate.getTheModel().giveAddedInformation(Protocol.FROM_ME, Protocol.DISAGREE, this.theModel.getUserName(), this.theModel.getUserName() + " refuse your invitation.");
                }
                
                mate.getTheModel().invitesFromMe.remove(this.theModel.getUserName());
                this.theModel.invitesToMe.remove(mate.getTheModel().getUserName());
            }
        }                 
                            
        @Override
        public void run()
        {   
            Timer timer = new Timer();

            timer.scheduleAtFixedRate(getTimerTask(), 5000, 5000);
            
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
                            
                            break;
                        }
                        case Protocol.REMOVE_MATE:
                        {
                            this.directRemoveMate(messageFromMe);
                            
                            break;
                        }
                        case Protocol.LOGGED:
                        {
                            this.directLogged();
                            
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
        public ViewServerClient getTheView() 
        {
            return theView;
        }

        /**
         * @param theView the theView to set
         */
        public void setTheView(ViewServerClient theView) 
        {
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

        private void directLogged() 
        {
            this.theModel.increaseSignalLogged();
        }

        
        /**
         * Server must check connection between user and server. So after each for example 5 seconds
         * server should receive information from user. If flag occurs everything is okey 
         * otherwise information about logged out will be sent to mates.
         */
        public class TimerTaskImpl extends TimerTask 
        {
            /**
             * Constructor
             */
            public TimerTaskImpl() 
            {
                oldValue = theModel.getLoggedSignalNuber();
                signalSended = false;
            }
            
            private int oldValue;
            private boolean signalSended;

            @Override
            public void run()
            {
                if(oldValue - theModel.getLoggedSignalNuber() == 0 && signalSended == false)
                {
                    setSignalSended(true);
                    
                    theModel.setLogged(false);
                    
                    theModel.sendInformationAboutExitToMates();
                }
                
                oldValue = theModel.getLoggedSignalNuber();
            }

            /**
             * @param signalSended the signalSended to set
             */
            public void setSignalSended(boolean signalSended) 
            {
                this.signalSended = signalSended;
            }
        }

        /**
         * @return the timerTask
         */
        public TimerTaskImpl getTimerTask() 
        {
            return this.timerTask;
        }
    }
}
