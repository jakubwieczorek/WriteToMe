package Wieczorek.Jakub.ChatApplication.Client;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
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
                    this.historyOfConversation.append("You send to " + toWho + " " + msg);
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
        
        this.jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
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
        
        this.jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        listOfMates = new javax.swing.JList();
        sendButton = new javax.swing.JButton();
        textToSend = new javax.swing.JTextField();
        addMateButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        historyOfConversation = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Write2Me!");
        setResizable(false);

        listOfMates.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listOfMates.setToolTipText("\"All your mates.\"");
        jScrollPane2.setViewportView(listOfMates);

        sendButton.setText("Send");
        sendButton.setToolTipText("\"Enter message to send then click on send, or mates username then click on add.\"");
        sendButton.setEnabled(false);

        textToSend.setToolTipText("\"Write something. See instruction.\"");

        addMateButton.setText("Add");
        addMateButton.setToolTipText("\"Enter the message and click me.\"");
        addMateButton.setEnabled(false);

        historyOfConversation.setColumns(20);
        historyOfConversation.setRows(5);
        jScrollPane1.setViewportView(historyOfConversation);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addMateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textToSend)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
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
                    
                    int typeOfMsg = Protocol.convert(msg.getFlag());
                    
                    System.out.println(msg.getFlag());
                    
                    switch(typeOfMsg)
                    {
                        case Protocol.MESSAGE:
                        {
                            historyOfConversation.append(msg.getText() + "\n");
                            break;
                        }
                        case Protocol.PERSON_INQUIRE:
                        {
                            historyOfConversation.append(msg.getText() + "\n");
                            
                            Message isExist = new Message();
                            isExist.receive(this.bufferedReader);

                            if(Protocol.convert(isExist.getFlag()) == Protocol.PERSON_EXIST)
                            {
                                model.addElement(isExist.getText());
                            }
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
    }
    
    String getUserName(String msg) throws NullPointerException
    {
        try
        {
            return (String)JOptionPane.showInputDialog
            (
                this, msg +
                "Input your username:",
                "Write2Me!",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""
            );
        }
        catch(NullPointerException ex)
        {
            throw new NullPointerException("Username wasn't read");
        }  
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList listOfMates;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField textToSend;
    // End of variables declaration//GEN-END:variables
}
