package Wieczorek.Jakub.ChatApplication;

import java.io.IOException;
import javax.swing.DefaultListModel;
/**
 *  This is view for user. I've sepperated graphical contents and control part from customer,
 *  thus logical part of classes Client and ClientView is in Client class, GUI is in ClientView.
 * 
 *  @see Wieczorek.Jakub.ChatApplication.Client
 */
public class ClientView extends javax.swing.JFrame 
{    
    /**
     *  Gui is connected with logical part so in ClientView class is one static member CLIENT (in 
     *  addition final, because ClientView shouldn't change state of logical part).
     */
    static final private Client CLIENT = new Client();
    
    /**
     * This is model for listOfMates.
     */
    static DefaultListModel model = new DefaultListModel();
    
    /**
     *  Instance of this class.
     */
    public static ClientView clientView = new ClientView();
    
    private String outputMsg;
    private String mate;
    
    public ClientView() 
    {
        this.outputMsg = "";
        this.mate = "Server";
        initComponents();
        
        ClientView.listOfMates.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        textToSend = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        listOfMates = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane1.setViewportView(textArea);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        textToSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textToSendActionPerformed(evt);
            }
        });

        listOfMates.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listOfMatesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(listOfMates);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                    .addComponent(textToSend))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textToSend)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * This method is invoked when user click on Send button. Then message from textToSend in sending 
     * to server.
     */
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        outputMsg = textToSend.getText();
        try
        {   
            // if user chose mate who should receive outputMsg
            if(!"".equals(mate))
            {
                CLIENT.getOutput().writeUTF(mate);
                CLIENT.getOutput().writeUTF(outputMsg);
            }
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_sendButtonActionPerformed

    private void textToSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textToSendActionPerformed
        // Thanks to this method user can send message by clicked enter.
        this.sendButtonActionPerformed(evt); 
    }//GEN-LAST:event_textToSendActionPerformed

    private void listOfMatesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listOfMatesValueChanged
        
        if(!evt.getValueIsAdjusting())
        { // assign to mate user who should receive msg
            mate = listOfMates.getSelectedValue();
        }
    }//GEN-LAST:event_listOfMatesValueChanged

    public static void main(String args[]) 
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientView().setVisible(true);
            }
        });
            // Bellowing code is reliable for manage input stream.
            try
            {
                String msgInput;
                String fromWho;
                while(true)
                {   
                    fromWho = ClientView.CLIENT.getInput().readUTF();
                    msgInput = ClientView.CLIENT.getInput().readUTF();
                    switch(fromWho)
                    {
                        case "fromServer":
                        {
                            model.addElement(msgInput); // create new mate in listOfMates
                            break;
                        }
                        default:
                        {   // display conversation with all mates.
                            ClientView.textArea.setText(ClientView.textArea.getText().trim() +
                                    fromWho + msgInput);
                            break;
                        }
                    }
                }
            }catch(IOException ex)
            {
                System.err.println(ex.getMessage());
            }
            
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JList<String> listOfMates;
    private static javax.swing.JButton sendButton;
    private static javax.swing.JTextArea textArea;
    private static javax.swing.JTextField textToSend;
    // End of variables declaration//GEN-END:variables
}
