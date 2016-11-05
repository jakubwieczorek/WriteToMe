package Wieczorek.Jakub.ChatApplication;

public class ClientGui 
{
    public static void main(String [] args)
    {
        new ClientGui().runGui(args);
    }
    
    public void runGui(String [] args)
    {
        Client client = new Client();
        client.setUserName(args[0]);
        client.startConversation();
    }
}
