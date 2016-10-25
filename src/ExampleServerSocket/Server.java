package ExampleServerSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Server {
    static ServerSocket server;
    static Socket client;
    static DataInputStream inputStream;
    static DataOutputStream outputStream;
    
    public static void main(String [] args)
    {
        try
        {
            Server server = new Server();
            server.run();
        }catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    void run() throws Exception // have to be non static !!!!
    {
        try
        {
            server = new ServerSocket(1500); // create server with port 1500 of our local net
            client = server.accept(); // the server is waiting for clients if someone make request
            // server initiate above object by requestung client. He link both objects.
            // and now we have access  to the client.
             
            inputStream = new DataInputStream(client.getInputStream()); // I create stream for 
            // incoming data.
            outputStream = new DataOutputStream(client.getOutputStream());
            
            String msgInput = inputStream.readUTF(); // inputStream waiting for message
            System.out.println(msgInput);
            
            String msgOutput = "Thanks client for message !";
            outputStream.writeUTF(msgOutput);
            
        }catch(Exception ex)
        {
            throw ex;
        }
    }
}
