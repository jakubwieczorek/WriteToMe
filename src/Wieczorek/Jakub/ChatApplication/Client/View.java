package Wieczorek.Jakub.ChatApplication.Client;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JPasswordField;

/**
 * View part of MVC pattern for user.
 * 
 * @author Jakub Wieczorek
 * 
 * @version 1.1
 */
public class View extends javax.swing.JFrame {

    private String userName;

    /**
     * Instance as a thread. It is made for receiving messages and displaing all messages on 
     * historyOfConversation. 
     */
    public View.ReceiverMessages receiverMessages; 
    
    private DefaultListModel<Mate>model = new DefaultListModel<>();
    
    private ConcurrentHashMap<String, Mate>modelMap = new ConcurrentHashMap();

    private class MyListCellRenderer extends DefaultListCellRenderer 
    {
        @Override
        public Component getListCellRendererComponent(JList<?> list,
            Object value, int index, boolean isSelected, boolean cellHasFocus) 
        {
            Component superRenderer = super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
            
            if(((Mate)value).isIsLogged()) 
            {
                this.setBackground(Color.GREEN);
            }
            else
            {
                this.setBackground(Color.RED);
            }

            return superRenderer;
        }
    }
    
    private class Mate
    {
        private String userName;
        private boolean isLogged;
        
        /**
         * Constructor
         * 
         * @param userName is mates userName
         */
        Mate(String userName)
        {
            this.userName = userName;
        }

        /**
         * @return the userName
         */
        String getUserName() 
        {
            return userName;
        }

        /**
         * @param userName the userName to set
         */
        void setUserName(String userName) 
        {
            this.userName = userName;
        }

        /**
         * @return the isLogged
         */
        boolean isIsLogged() 
        {
            return isLogged;
        }

        /**
         * @param isLogged the isLogged to set
         */
        void setIsLogged(boolean isLogged) 
        {
            this.isLogged = isLogged;
        }
        
        @Override
        public String toString()
        {
            return this.userName;
        }
    }
    
    /*
     * Reference for controller who direct model and view. It is neccessary, becouse
     * if View didn't know about controller where this View would created, liseners for
     * buttons would have to be created in controller. For maintance the code and flexibility 
     * reference for controller in View class is better.
     * 
     * javax.swing.DefaultListModel
     */
    private Controller controller;
    
    /**
     * Constructor. 
     * 
     * @param inputStream made for creating BufferedReader instance in receiverMessages.
     * @param controller is reference for parent (controller).
     */
    public View(InputStream inputStream, Controller controller) 
    {
        initComponents();
        
        this.listOfMates.setModel(this.model);
        this.listOfMates.setCellRenderer(new MyListCellRenderer());
        
        this.receiverMessages = new View.ReceiverMessages(inputStream);
        
        this.controller = controller;
        
        this.sendButton.addActionListener
        (
            (event)->
            {
                if(event.getSource() == this.sendButton)
                {
                    /* if user click for the sendButton, method in theModel instance will
                       send the message to the server: ToWho:text
                    */
                    String msg = this.getMessageToSend();
                    String toWho = this.listOfMates.getSelectedValue().toString();
                    
                    // invoke proper method to upgrade model
                    this.controller.upgradeModelMsg(msg, toWho);
                    
                    this.historyOfConversation.append("You send to " + toWho + " " + msg + "\n");
                    this.textToSend.setText("");
                }
            }
        );
        
        this.addMateButton.addActionListener
        (
            (event)->
            {
                if(event.getSource() == this.addMateButton)
                {
                    /* if user click for the addMateButton, method in theModel instance will
                       send the inquiry to the server, wheter that person exist or not.
                    */
                    this.controller.upgradeModelMateInquire(this.getPersonInquiry());
                    this.textToSend.setText("");
                }
            }       
        );
        
        // if textToSend is empty buttons should be unenable.
        this.textToSend.getDocument().addDocumentListener
        (
            new DocumentListener()
            {
                @Override
                public void insertUpdate(DocumentEvent e) 
                {
                    this.change();
                }

                @Override
                public void removeUpdate(DocumentEvent e) 
                {
                    this.change();
                }

                @Override
                public void changedUpdate(DocumentEvent e) 
                {
                    this.change();                        
                }

                private void change()
                {
                    if(textToSend.getText().equals(""))
                    {
                        sendButton.setEnabled(false);
                        addMateButton.setEnabled(false);
                    }
                    else
                    {
                        addMateButton.setEnabled(true);
                        
                        if(!listOfMates.isSelectionEmpty())
                        {
                            if(((Mate)listOfMates.getSelectedValue()).isLogged == false)
                                sendButton.setEnabled(false);
                            else
                                sendButton.setEnabled(true);
                        }       
                        else
                            sendButton.setEnabled(false);
                    }
                    
                    listOfMates.clearSelection();
                }
            }
        );
        
        this.listOfMates.addListSelectionListener
        (
            (event)->
            {
                
                if(textToSend.getText().equals(""))
                {
                    sendButton.setEnabled(false);
                }
                else
                {
                    if(!listOfMates.isSelectionEmpty())
                    {
                        if(((Mate)listOfMates.getSelectedValue()).isLogged == false)
                            sendButton.setEnabled(false);
                        else
                            sendButton.setEnabled(true);
                    }       
                    else
                        sendButton.setEnabled(false);
                }
            }
        );
        
        class PopUpDemo extends JPopupMenu 
        {
            JMenuItem anItem;
            
            public PopUpDemo(String userName)
            {
                anItem = new JMenuItem("Remove " + userName + " from mates");
                anItem.addActionListener
                (
                    (event)->
                    {
                        for(Object mate : model.toArray())
                        {
                            if(((Mate)mate).getUserName().equals(userName))
                                model.removeElement((Mate)mate);
                        }
                        controller.upgradeModelMateRemove(userName);
                    }
                ); // with - python coś jak del.
                
                this.add(anItem);
            }
        }
        
        this.listOfMates.addMouseListener
        ( 
            new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    if(SwingUtilities.isRightMouseButton(e))
                    {   
                        try
                        {
                            listOfMates.setSelectedIndex(listOfMates.locationToIndex(e.getPoint()));

                            PopUpDemo menu = new PopUpDemo(model.get(listOfMates.getSelectedIndex()).toString());
                            menu.show(e.getComponent(), e.getX(), e.getY());
                        }
                        catch(ArrayIndexOutOfBoundsException ex)
                        {
                            
                        }
                    }
                }
            }
        );
    
        this.addWindowListener
        (
            new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    controller.upgradeModelExitMsg();
                    System.exit(0);
                    e.getWindow().dispose();
                }
            }
        );
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        listOfMatesScrollPane = new javax.swing.JScrollPane();
        listOfMates = new javax.swing.JList();
        sendButton = new javax.swing.JButton();
        textToSend = new javax.swing.JTextField();
        addMateButton = new javax.swing.JButton();
        historyOfConversationScrollPane = new javax.swing.JScrollPane();
        historyOfConversation = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        menuInvitations = new javax.swing.JMenu();
        invitationsSended = new javax.swing.JMenu();
        invitationsReceived = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Write2Me!");
        setResizable(false);

        listOfMatesScrollPane.setToolTipText("");

        listOfMates.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listOfMates.setToolTipText("\"All your mates. Green mate is logged, red is unlogged.\"");
        listOfMatesScrollPane.setViewportView(listOfMates);

        sendButton.setText("Send");
        sendButton.setToolTipText("\"Enter the message and click in order to send.\"");
        sendButton.setEnabled(false);

        textToSend.setToolTipText("\"Write something. See instruction in case of doubts.\"");

        addMateButton.setText("Invite");
        addMateButton.setToolTipText("\"Enter username of seeking person, then click.\"");
        addMateButton.setEnabled(false);

        historyOfConversation.setEditable(false);
        historyOfConversation.setColumns(20);
        historyOfConversation.setRows(5);
        historyOfConversation.setToolTipText("");
        historyOfConversationScrollPane.setViewportView(historyOfConversation);

        menuInvitations.setText("Invitations");

        invitationsSended.setText("Sended");
        menuInvitations.add(invitationsSended);

        invitationsReceived.setText("Received");
        menuInvitations.add(invitationsReceived);

        menuBar.add(menuInvitations);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listOfMatesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addMateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textToSend)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton))
                    .addComponent(historyOfConversationScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(listOfMatesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                    .addComponent(historyOfConversationScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendButton)
                    .addComponent(textToSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addMateButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * @return bufferedReader
     */
    public BufferedReader getBufferedReaeder() 
    {
        return this.receiverMessages.bufferedReader;
    }
    
    /**
     * Class to receive messages with setting messages to object in parent class (View)
     */
    public class ReceiverMessages implements Runnable
    {
        private BufferedReader bufferedReader;
        
        /**
         * Instance of thread.
         * 
         * @see java.lang.Thread
         */
        Thread thread;
        
        /**
         * Constructor
         * 
         * @param inputStream is inputStream where all messages will be being sended.
         */
        public ReceiverMessages(InputStream inputStream)
        {
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.thread = new Thread(this);
        }
        
        private volatile boolean isRunning = true;
        
        public void kill() 
        {
            isRunning = false;
        }
        
        @Override
        public void run()
        {
            while(isRunning)
            {
                try
                {
                    Message msg = new Message();

                    msg.receive(this.bufferedReader);
                    if(msg != null && msg.getFlag() != null)
                    switch(msg.getFlag())
                    {
                        case Protocol.MESSAGE:
                        {
                            historyOfConversation.append(msg.getText() + "\n");
                            
                            break;
                        }
                        case Protocol.PERSON_INQUIRE:
                        {
                            historyOfConversation.append(msg.getText() + "\n");
                            
                            break;
                        }
                        case Protocol.PERSON_INVITATION:
                        {
                            this.directPersonInvitation(msg);
                            
                            break;
                        }
                        case Protocol.ANSWER:
                        {
                            this.directAnswer(msg);
                            
                            break;
                        }
                        case Protocol.INITIATE:
                        {
                            this.directInitiation(msg);
                            
                            break;
                        }
                        case Protocol.EXIT:
                        {
                            this.directExit(msg);
                            
                            break;
                        }
                        case Protocol.LOGGED_IN_DIFFERENT_DEVICE:
                        {
                            historyOfConversation.append(msg.getText() + "\n");
                            
                            setEnabled(false);
                            
                            this.kill();
                            
                            break;
                        }
                        case Protocol.REMOVE_MATE:
                        {
                            this.directRemoveMate(msg);
                            
                            break;
                        }
                        case Protocol.LOGGED:
                        {
                            this.directLogged(msg);
                        }
                        case Protocol.SERVER_WORKS:
                        {
                            this.serverWorksSignalNumber++;
                            
                            break;
                        }
                    }
                }
                catch(IOException ex)
                {
                    System.err.println(ex.getMessage() + "Not all data may be read");
                }
            }
        }

        private void directPersonInvitation(Message msg) throws IOException
        {
            historyOfConversation.append(msg.getText() + "\n");
                            
            Message invitation = new Message();
            invitation.receive(this.bufferedReader);

            JMenu mate = new JMenu(invitation.getText());

            switch(invitation.getFlag())
            {
                case Protocol.FROM_ME:
                {
                    invitationsSended.add(new JMenuItem(mate.getText()));

                    break;
                }
                case Protocol.TO_ME:
                {
                    this.directPersonInvitationToMe(mate);
                    
                    break;
                }
            }
        }
        
        private void directPersonInvitationToMe(JMenu mate)
        {   
            JMenuItem mateAdd = new JMenuItem("Accept");
            JMenuItem mateRefuse = new JMenuItem("Refuse");

            mateAdd.addActionListener
            (
                (event)->
                {
                    controller.upgradeModelMateAnswer(mate.getText(), Protocol.AGREE);
                }   
            );

            mateRefuse.addActionListener
            (
                (event)->
                {
                    controller.upgradeModelMateAnswer(mate.getText(), Protocol.DISAGREE);
                }
            );

            mate.add(mateAdd);
            mate.add(mateRefuse);
            invitationsReceived.add(mate);
        }   

        private void directAnswer(Message msg) throws IOException
        {
            historyOfConversation.append(msg.getText() + "\n");
                            
            // from me or to me
            Message source = new Message();
            source.receive(this.bufferedReader);

            historyOfConversation.append(source.getText() + "\n");

            // mates name
            Message agree = new Message();
            agree.receive(this.bufferedReader);

            if(source.getFlag().equals(Protocol.FROM_ME))
            {  
                // mate seeking
                for(Component menuComponent : invitationsSended.getMenuComponents()) 
                {
                    if(((JMenuItem)menuComponent).getText().equals(agree.getText()))
                    {
                        invitationsSended.remove(menuComponent);
                    }
                }
            }else
            if(source.getFlag().equals(Protocol.TO_ME))
            { 
                // mate seeking
                for(Component menuComponent : invitationsReceived.getMenuComponents()) 
                {
                    if(((JMenuItem)menuComponent).getText().equals(agree.getText()))
                    {
                        invitationsReceived.remove(menuComponent);
                    }
                }
            } 

            if(agree.getFlag().equals(Protocol.AGREE))
            {
                Mate mate = new Mate(agree.getText());
                
                source.receive(this.bufferedReader);
                
                mate.setIsLogged(source.getFlag().equals(Protocol.LOGGED));
                
                model.addElement(mate);
            }
        }

        private void directInitiation(Message msg) throws IOException 
        {
            historyOfConversation.append(msg.getText() + "\n");
            
            Message section = new Message();
            
            section.receive(this.bufferedReader);
            
            while(!section.getFlag().equals(Protocol.INITIATE))
            {
                switch(section.getFlag())
                {
                    case Protocol.MATE:
                    {
                        Mate mate = new Mate(section.getText());
                        
                        section.receive(this.bufferedReader);
                        
                        mate.setIsLogged(section.getFlag().equals(Protocol.LOGGED));
                        
                        System.out.println(section.getFlag());
                        
                        model.addElement(mate);
                        
                        break;
                    }
                    case Protocol.TO_ME:
                    {
                        JMenu mate = new JMenu(section.getText());
                        
                        this.directPersonInvitationToMe(mate);
                        
                        break;
                    }
                    case Protocol.FROM_ME:
                    {
                        JMenu mate = new JMenu(section.getText());
                        
                        invitationsSended.add(new JMenuItem(mate.getText()));

                        break;
                    }
                }
                
                section.receive(this.bufferedReader);
            }
        }

        private void directLogged(Message msg) 
        {
            System.out.println("Logged");
            
            
            for(Object mate : model.toArray())
            {
                if(((Mate)mate).userName.equals(msg.getText()))
                {
                    ((Mate)mate).setIsLogged(true);
                }
            }
        }
        
        private void directExit(Message matesUserName) 
        {
            System.out.println("Exit");
            
            for(Object mate : model.toArray())
            {
                if(((Mate)mate).userName.equals(matesUserName.getText()))
                    ((Mate)mate).setIsLogged(false);
            }
        }

        private void directRemoveMate(Message msg) 
        {
            historyOfConversation.append(msg.getText() + " remove you from mates." + "\n");
                           
            for(Object mate : model.toArray())
            {
                if(((Mate)mate).userName.equals(msg.getText()))
                    model.removeElement((Mate)mate);
            }
        } 
        
        private int serverWorksSignalNumber = 0;
        
        /**
        * Model per 5 seconds check wheter information about connection
        * was received.
        */
        public void startReceivingSignals()
        {
            Timer timer = new Timer();

            timer.scheduleAtFixedRate
            (
                new TimerTask() 
                {
                    
                    private int oldValue = 0;
                    private boolean signalReceived = false;
                    
                    @Override
                    public void run() 
                    {
                        if(oldValue -  serverWorksSignalNumber == 0 && signalReceived == false && isRunning == true)
                        {
                            setEnabled(false);
                            historyOfConversation.append("\n Server's just broken down.");
                            signalReceived = true;
                        }

                        oldValue = serverWorksSignalNumber;
                    }
                }
            , 15000, 15000);
        }
    }
    
    /**
     * Show dialog which gets userName.
     * 
     * @param msg is message for user.
     * 
     * @return username or password
     */
    public String getUserName(String msg)
    {
	if(msg.equals("Input password:"))
	{	
		JPasswordField jpf = new JPasswordField(24);
		int okCxl = JOptionPane.showConfirmDialog(this, jpf, msg, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if(okCxl == JOptionPane.OK_OPTION) 
		{
			return new String(jpf.getPassword());
		}
		
		return null;
	}

        return (String)JOptionPane.showInputDialog(this, null, msg, JOptionPane.PLAIN_MESSAGE, null, null,"");
    }
      
    /**
     * setter for userName. This method sets also title for window.
     * 
     * @param userName is username for user.
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
        this.setTitle(this.getTitle() + " Logged as " + this.userName);
    }
    
    /**
     * getter for textToSend.
     * 
     * @return string from textToSend.
     */
    public String getMessageToSend()
    {
        return textToSend.getText();
    }
    
     /**
     * getter for textToSend.
     * 
     * @return string from textToSend.
     */
    public String getPersonInquiry() 
    {
        return this.getMessageToSend();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addMateButton;
    private javax.swing.JTextArea historyOfConversation;
    private javax.swing.JScrollPane historyOfConversationScrollPane;
    private javax.swing.JMenu invitationsReceived;
    private javax.swing.JMenu invitationsSended;
    private javax.swing.JList listOfMates;
    private javax.swing.JScrollPane listOfMatesScrollPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuInvitations;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField textToSend;
    // End of variables declaration//GEN-END:variables
}
