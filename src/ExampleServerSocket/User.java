package ExampleServerSocket;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class User {
    static Socket socket;
    static DataInputStream inputStream;
    static DataOutputStream outputStream;
    
    public static void main(String [] args)
    {
        try
        {
            User user = new User();
            user.run();
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    void run() throws Exception
    {
        try
        {
            socket = new Socket("127.0.0.1", 1500);// local host is damanded or IP adress of our local net
          
            outputStream = new DataOutputStream(socket.getOutputStream());
            String outputMsg = "Hello server !";
            outputStream.writeUTF(outputMsg);
            
            inputStream = new DataInputStream(socket.getInputStream());
            String inputMsg = inputStream.readUTF();
            System.out.println(inputMsg);
            
        }catch(Exception ex)
        {
            throw ex;
        }
    }
}
