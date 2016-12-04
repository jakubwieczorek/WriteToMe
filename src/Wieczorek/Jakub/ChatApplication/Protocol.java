package Wieczorek.Jakub.ChatApplication;

/**
 * Class created in order to systematize data transfering between programs.
 * Before each message program's sending determined flag. Then program
 * who should receive message, before that message gets proper flag. So question about
 * mate, simple message has unique flag.
 * 
 * 
 * @author Jakub Wieczorek
 * @version 1.1
 */
public class Protocol 
{
    /**
     * Flag, which indicated that after this flag is message.
     */
    public static final int MESSAGE = 2;
    /**
     * Flag, which indicated that after this flag is information about any user.
     */
    public static final int PERSON_INQUIRE = 3;
    
    /**
     * Flag, which indicated that person exist.
     */
    public static final int PERSON_EXIST = 1;

    /**
     * Flag, which indicated that person don't exist.
     */
    public static final int PERSON_DONT_EXIST = 0;
    
    /**
     * Flag which indicate that person have jast logged out.
     */
    public static final int EXIT = 4;
    
    /**
     * Flag which indicate that user want to add following person to mates.
     */
    public static final int AGREE = 5;
    
    /**
     * Socket receives flag converted to char from ascii so there is need to
     * convert it.
     * 
     * @param flag is flag who should be coverted.
     */
    public static final int convert(int flag)
    {
        return flag - 48;
    }
}
