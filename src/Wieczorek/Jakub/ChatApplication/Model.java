package Wieczorek.Jakub.ChatApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Model 
{
    Client client;
    static final char SEND_MESSAGE = '1';
    static final char SEND_PERSON = '2';
    
    public Model(String userName)
    {
        this.client = new Client(userName);
    }
    
    class Client 
    {
        Socket socket;
        String userName;
        final int PORT;
        PrintWriter printWriter;
        
        public Client(String userName)
        {
            this.PORT = 1550;

            try
            {
                this.socket = new Socket("localhost", this.PORT);
                this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);
            }
            catch(IOException ex)
            {
                System.err.println(ex.getMessage());
            }
            
            this.userName = userName;
            
            printWriter.println(userName);
        }
        
        public InputStream getInputStream() throws IOException
        {
            return this.socket.getInputStream();
        }
        
        public void sendMsg(String msgToSend)
        {
            this.printWriter.print(SEND_MESSAGE);
            this.printWriter.println(msgToSend);
        }
        
        public void sendPersonInquire(String person)
        {
            this.printWriter.print(SEND_PERSON);
            this.printWriter.println(person);
        }
        
        public void setUserName(String name)
        {
            this.userName = name;
        }

        public String getUserName()
        {
            return this.userName;
        }
    }
}
