package Wieczorek.Jakub.ChatApplication;

public class MVCClient 
{
    public static void main(String [] args)
    {   
        Controller theController = new Controller(args[0]);  
        
        theController.startConversation();
    }
}
