package Wieczorek.Jakub.ChatApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Model 
{
    Client client;
    
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
        
        public void sendMsg(String msgToSend) throws IOException
        {
            this.printWriter.println(msgToSend);
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
