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
    
    private char flag;
    
    /**
     * Constructor
     * 
     * @param contents is text of this message
     * @param flag is flag for this message determining type of this message.
     */
    public Message(char flag, String contents)
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
    public int getFlag()
    {
        return this.flag;
    }
    
     /**
     * setter for flag field
     * 
     * @param flag is new flag
     */
    public void setFlag(char flag)
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
        printWriter.print(this.flag);
        printWriter.println(this.contents);
    }
    
    /**
     * Receive message from given bufferedReader.
     * 
     * @param bufferedReader is sockets bufferedReader
     * @throws IOException throw when IOException occurres
     */
    public void receive(BufferedReader bufferedReader) throws IOException
    {
        try
        {
            this.flag = (char)bufferedReader.read();
            this.contents = bufferedReader.readLine();
        }
        catch(IOException ex)
        {
            throw new IOException("Lack of ability to read message");
        }
    }
}
