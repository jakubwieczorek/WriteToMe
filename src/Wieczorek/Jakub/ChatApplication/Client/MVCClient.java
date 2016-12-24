package Wieczorek.Jakub.ChatApplication.Client;

public class MVCClient 
{
    public static void main(String [] args)
    {   
        javax.swing.SwingUtilities.invokeLater
        (
            ()-> 
            {
                new Controller();
            }
        );
    }
}
