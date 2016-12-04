package Wieczorek.Jakub.ChatApplication.Client;

import Wieczorek.Jakub.ChatApplication.Message;
import Wieczorek.Jakub.ChatApplication.Protocol;
import javax.swing.*;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *  View part of MVC patern. 
 * 
 *  @author Jakub Wieczorek 
 *  @version 1.1
 */
public class ViewGui extends JFrame
{   
    /**
     * Instance as a thread. It is made for receiving messages and displaing all messages on 
     * historyOfConversation.
     * 
     * @see Wieczorek.Jakub.ChatApplication.ViewGui#historyOfConversation
     * @see Wieczorek.Jakub.ChatApplication.ViewGui.ReceiverMessages#run() 
     */
    ReceiverMessages receiverMessages;
    
    /**
     * Here user inputs the contents of the text to send. Both as a message and as a inquiry for
     * different user.
     */
    JTextField textToSend;
    
    /**
     * Here receiverMessages throws all receiving text.
     * 
     * @see receiverMessages
     */
    JTextArea historyOfConversation;
    
    /**
     * if user clicks this button, the proper actionListener method will be invoke. Implementation is in Controller part.
     * Briefly text from textField will be returned as a String.
     * 
     * @see Wieczorek.Jakub.ChatApplication.Controller#Controller(java.lang.String) 
     */
    JButton sendButton;
    
    /**
     * if user clicks this button, the proper actionListener method will be invoke. Implementation is in Controller part.
     * Shortly text from the textField will be returned as a String.
     * 
     * @see Wieczorek.Jakub.ChatApplication.Controller#Controller(java.lang.String) 
     */
    JButton addMateButton;
    
    /**
     * The username for user.
     */
    String userName;
    
    /**
     * List of mates. If user wants to send message must click for any mate. Then he can click for sendButton button.
     */
    JList listOfMates;
    
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
     * @see Wieczorek.Jakub.ChatApplication.ViewGui.ReceiverMessages#run()
     */
    public ViewGui(InputStream inputStream, Controller controller)
    {   
        // graphic contents for theView
        this.initUI();
        
        this.receiverMessages = new ReceiverMessages(inputStream);
        
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

    /**
     * graphics contents for theView.
     */
    private void initUI()
    {
        this.setSize(400, 400);
        
        this.setTitle("Write2Me!");
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        
        Dimension dimension = toolkit.getScreenSize();
        
        this.setLocation(dimension.width / 2 - this.size().width / 2, 
                dimension.height / 2 - this.size().height / 2);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // JPanel:
        JPanel panel = new JPanel();
        
        // textToSend
        this.textToSend = new JTextField(15);
        
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
        this.textToSend.setToolTipText("Write something. See instruction.");
        panel.add(this.textToSend);
        
        // historyOfConversation
        this.historyOfConversation = new JTextArea(15, 15);
        this.historyOfConversation.setLineWrap(true);
        this.historyOfConversation.setWrapStyleWord(true);
        this.historyOfConversation.setEditable(false);
        
        JScrollPane scrollBar = new JScrollPane(this.historyOfConversation, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollBar);
        
        // sendButton
        this.sendButton = new JButton("Send");
        this.sendButton.setEnabled(false);
        this.sendButton.setToolTipText("Enter message to send then click on send, or mates username then click on add.");
        panel.add(this.sendButton);
        
        this.addMateButton = new JButton("Add mate");
        this.addMateButton.setEnabled(false);
        this.addMateButton.setToolTipText("Enter the message and click me.");
        panel.add(this.addMateButton);
        
        
        // listOfMates
        this.listOfMates = new JList(this.model);
        this.listOfMates.setToolTipText("All your mates.");
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
        
        this.listOfMates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only one mate can be selected.
        
        this.listOfMates.setVisibleRowCount(4);
        this.listOfMates.setFixedCellHeight(30);
        this.listOfMates.setFixedCellWidth(150);
        
        JScrollPane listBar = new JScrollPane(this.listOfMates, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                
        panel.add(listBar);
        this.add(panel);
    }

    BufferedReader getBufferedReaeder() 
    {
        return this.receiverMessages.bufferedReader;
    }
    
    // class to receive messages with setting messages to object in parent class (ViewGui)
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
}
