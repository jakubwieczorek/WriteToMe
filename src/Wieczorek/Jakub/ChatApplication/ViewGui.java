package Wieczorek.Jakub.ChatApplication;

import javax.swing.*;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

public class ViewGui extends JFrame
{   
    ReceiverMessages receiverMessages;
    JTextField textToSend;
    JTextArea historyOfConversation;
    JButton sendButton;
    
    public ViewGui(InputStream inputStream, ActionListener sendButtonAction)
    {
        this.initUI();
        this.receiverMessages = new ReceiverMessages(inputStream);
        this.sendButton.addActionListener(sendButtonAction);
    }
    
    public String getMessageToSend()
    {
        return textToSend.getText();
    }

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
        
        // textToSens
        this.textToSend = new JTextField(15);
        panel.add(this.textToSend);
        
        // historyOfConversation
        this.historyOfConversation = new JTextArea(15, 15);
        this.historyOfConversation.setLineWrap(true);
        this.historyOfConversation.setWrapStyleWord(true);
        JScrollPane scrollBar = new JScrollPane(this.historyOfConversation, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollBar);
        
        // sendButton
        this.sendButton = new JButton("Send");
        
        panel.add(this.sendButton);
        
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
            try
            {
                while(true)
                {
                    String msg = this.bufferedReader.readLine();
                    
                    historyOfConversation.append(msg + "\n");
                }
            }
            catch(IOException ex)
            {
                System.err.println(ex.getMessage());
            }
        }
    }
}
