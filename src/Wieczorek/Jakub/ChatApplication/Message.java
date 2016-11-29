package Wieczorek.Jakub.ChatApplication;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * @author jakub
 */
public class Message 
{
    /**
     * The contents of the message.
     */
    private String contents;
    
    /**
     * Flag from proper protocol. It is sent always before contents of the message.
     */
    private int flag;
    
    /**
     * Constructor
     * 
     * @param contents is text of this message
     * @param flag is flag for this message determining type of this message.
     */
    public Message(int flag, String contents)
    {
        this.flag = flag;
        this.contents = contents;
    }
    
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
     * send message by given printwriter.
     * 
     * @param printWriter
     */
    void send(PrintWriter printWriter)
    {
        printWriter.print(this.flag);
        printWriter.println(this.contents);
    }
    
    /**
     * Receive message from given bufferedReader.
     * 
     * @param bufferedReader
     * @throws IOException
     */
    void receive(BufferedReader bufferedReader) throws IOException
    {
        this.flag = bufferedReader.read();
        this.contents = bufferedReader.readLine();
    }
}
