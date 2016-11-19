package Wieczorek.Jakub.ChatApplication;

import javax.swing.*;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *  View part of MVC patern. 
 * 
 *  @author Jakub Wieczorek 
 *  @version 1.1
 */
public class ViewGui extends JFrame
{   
    /**
     * Instance as a thread. It is made for receiving messages and dipsplaing all messages on 
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
     * Constructor. 
     * 
     * @param inputSream made for creating BufferedReader instance in receiverMessages.
     * @param userName initiate userName field.
     * @param sendButtonAction is implementation of ActionListener for sendButton and addMateButton.
     * 
     * @see Wieczorek.Jakub.ChatApplication.ViewGui.ReceiverMessages#run()
     */
    public ViewGui(String userName, InputStream inputStream, ActionListener sendButtonAction)
    {
        this.userName = userName;
        
        // graphic contents for theView
        this.initUI();
        
        this.receiverMessages = new ReceiverMessages(inputStream);
        
        this.sendButton.addActionListener(sendButtonAction);
        this.addMateButton.addActionListener(sendButtonAction);
    }
    
    private class getUserNameFrame extends JFrame
    {
        String userName;
        
        public String getUserNameFrame()
        {
            this.initUI();
            
            return userName;
        }
        
        public void initUI()
        {
            this.setSize(200, 200);
            
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension dim = toolkit.getScreenSize();
            this.setLocation(dim.width / 2 - this.size().width / 2, dim.height / 2 - this.size().height);
        
            
            
        }
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
        
        this.setTitle("Write2Me! " + "logged as " + userName);
        
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
            new ListSelectionListener()
            {
                @Override
                public void valueChanged(ListSelectionEvent e) 
                {
                    if(!textToSend.getText().equals(""))
                        sendButton.setEnabled(true);
                    else
                        sendButton.setEnabled(false);
                }
            }
        );
        
        this.listOfMates.setVisibleRowCount(4);
        this.listOfMates.setFixedCellHeight(30);
        this.listOfMates.setFixedCellWidth(150);
        
        JScrollPane listBar = new JScrollPane(this.listOfMates, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                
        panel.add(listBar);
        this.add(panel);
        
        this.setVisible(true);
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
                    char typeOfMsg = (char)this.bufferedReader.read();

                    switch(typeOfMsg)
                    {
                        case Model.SEND_MESSAGE:
                        {
                            String msg = this.bufferedReader.readLine();

                            historyOfConversation.append(msg + "\n");
                            break;
                        }
                        case Model.SEND_PERSON:
                        {
                            String msg = this.bufferedReader.readLine();
                            historyOfConversation.append(msg + "\n");

                            char exist = (char)this.bufferedReader.read();

                            if(exist == ChatServerClient.TRUE)
                            {
                                model.addElement(this.bufferedReader.readLine());
                            }
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
}
