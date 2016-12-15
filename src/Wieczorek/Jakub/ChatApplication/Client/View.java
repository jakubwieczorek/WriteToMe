package Wieczorek.Jakub.ChatApplication.Client;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.DefaultListModel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author jakub
 */
public class View extends javax.swing.JFrame {

    /**
     * The username for user.
     */
    String userName;

    /**
     * Instance as a thread. It is made for receiving messages and displaing all messages on 
     * historyOfConversation.
     * 
     * @see Wieczorek.Jakub.ChatApplication.View#historyOfConversation
     * @see Wieczorek.Jakub.ChatApplication.View.ReceiverMessages#run() 
     */
    View.ReceiverMessages receiverMessages; 
    
    /**
     * Model for listOfMates.
     * 
     * @see javax.swing.DefaultListModel
     */
    DefaultListModel model = new DefaultListModel();
    
    /**
     * Reference for controller who direct model and view. It is neccessary, becouse
     * if View didn't know about controller where this View would created, liseners for
     * buttons would have to be created in controller. For maintance the code and flexibility 
     * reference for controller in View class is better.
     * 
     * @see javax.swing.DefaultListModel
     */
    Controller controller;
    
    /**
     * Constructor. 
     * 
     * @param inputSream made for creating BufferedReader instance in receiverMessages.
     * @see Wieczorek.Jakub.ChatApplication.View.ReceiverMessages#run()
     */
    public View(InputStream inputStream, Controller controller) 
    {
        initComponents();
        
        this.listOfMates.setModel(this.model);
        
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
                            sendButton.setEnabled(true);
                    } 
                }
            }
        );
        
        this.listOfMates.addListSelectionListener
        (
            (event)->
            {
                if(!textToSend.getText().equals(""))
                    sendButton.setEnabled(true);
                else
                    sendButton.setEnabled(false);
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
        listOfMates.setToolTipText("\"All your mates.\"");
        listOfMatesScrollPane.setViewportView(listOfMates);

        sendButton.setText("Send");
        sendButton.setToolTipText("\"Enter message to send then click on send, or mates username then click on add.\"");
        sendButton.setEnabled(false);

        textToSend.setToolTipText("\"Write something. See instruction.\"");

        addMateButton.setText("Invite");
        addMateButton.setToolTipText("\"Enter the message and click me.\"");
        addMateButton.setEnabled(false);

        historyOfConversation.setEditable(false);
        historyOfConversation.setColumns(20);
        historyOfConversation.setRows(5);
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

    BufferedReader getBufferedReaeder() 
    {
        return this.receiverMessages.bufferedReader;
    }
    
    // class to receive messages with setting messages to object in parent class (View)
    public class ReceiverMessages implements Runnable
    {
        BufferedReader bufferedReader;
        Thread thread;
        
        public ReceiverMessages(InputStream inputStream)
        {
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.thread = new Thread(this);
        }
        
        @Override
        public void run()
        {
            while(true)
            {
                try
                {
                    Message msg = new Message();

                    msg.receive(this.bufferedReader);
                    
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
                        }
                        case Protocol.LOGGED_IN_DIFFERENT_DEVICE:
                        {
                            historyOfConversation.append(msg.getText() + "\n");
                            
                            setEnabled(false);
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

            if(source.getFlag() == Protocol.FROM_ME)
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
            if(source.getFlag() == Protocol.TO_ME)
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

            if(agree.getFlag() == Protocol.AGREE)
            {
                model.addElement(agree.getText());
            }
        }

        private void directInitiation(Message msg) throws IOException 
        {
            historyOfConversation.append(msg.getText() + "\n");
            
            Message section = new Message();
            
            
            section.receive(this.bufferedReader);
            
            while(section.getFlag() != Protocol.INITIATE)
            {
                switch(section.getFlag())
                {
                    case Protocol.MATE:
                    {
                        model.addElement(section.getText());
                        
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

        private void directExit(Message matesUserName) 
        {
            for(int i = 0; i < model.getSize(); i++)
            {
                System.out.println(model.getElementAt(i).toString());
                
                //if(matesUserName.getText().equals(model.getElementAt(i)));
                   // System.out.println(model.get(i).getClass().toString());
                    //((Component)model.getElementAt(i)).setEnabled(false); - here find out how setEnabled 
            }
        }
    }
    
    String getUserName(String msg)
    {
        return (String)JOptionPane.showInputDialog(this, msg, "Write2Me!", JOptionPane.PLAIN_MESSAGE, null, null,"");
    }
    
        
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
