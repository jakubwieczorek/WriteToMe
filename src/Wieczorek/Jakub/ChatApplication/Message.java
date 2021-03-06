package Wieczorek.Jakub.ChatApplication;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * @author Jakub Wieczorek
 * 
 * @version 1.1
 */
public class Message 
{
    private String contents;
    
    private String flag;
    
    /**
     * Constructor
     * 
     * @param contents is text of this message
     * @param flag is flag for this message determining type of this message.
     */
    public Message(String flag, String contents)
    {
        this.flag = flag;
        this.contents = contents;
    }
    
    
    /** 
     * Default constructor created in order to reading message from buffered reader
     */
    public Message(){}
    
    /**
     * getter for contents field
     * 
     * @return text for these message
     */
    public String getText()
    {
        return this.contents;
    }
    
     /**
     * getter for flag field
     * 
     * @return flag for these message
     */
    public String getFlag()
    {
        return this.flag;
    }
    
     /**
     * setter for flag field
     * 
     * @param flag is new flag
     */
    public void setFlag(String flag)
    {
        this.flag = flag;
    }
    
    /**
     * setter for contents field
     * 
     * @param text is new contents for this message
     */    
    public void setText(String text)
    {
        this.contents = text;
    }
    
    /**
     * send message by given printwriter.
     * 
     * @param printWriter is sockets printWriter
     */
    public void send(PrintWriter printWriter)
    {
        printWriter.println(this.flag + ";" + this.contents);
    }
    
    /**
     * Receive message from given bufferedReader.
     * 
     * @param bufferedReader is sockets bufferedReader
     */
    public void receive(BufferedReader bufferedReader)
    {
        try
        {
            String line = bufferedReader.readLine();
            if(line != null)
            {
                String [] flagAndContents = this.splitFlagAndContents(line);
                this.contents = flagAndContents[1];
                this.flag = flagAndContents[0];
            }
        }
        catch(IOException | NullPointerException | IllegalArgumentException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Splits msg for two Strings: before : character and after.
     * 
     * @param msg is String which should contains : character.
     * @return Two elements Strings array. 
     * @throws IllegalArgumentException when : character doesn't occur.
     */
    public String [] splitFlagAndContents(String msg) throws IllegalArgumentException
    {
        if(msg.contains(";"))
        {
            String flagAndMsg [] = msg.split(";", 2);
            return flagAndMsg;
        }

        throw new IllegalArgumentException("Received message doesn't contains ';'");
    }
}
