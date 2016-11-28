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
    static final int MESSAGE = 2;
    /**
     * Flag, which indicated that after this flag is information about any user.
     */
    static final int PERSON_INQUIRE = 3;
    
    /**
     * Flag, which indicated that person exist.
     */
    static final int PERSON_EXIST = 1;

    /**
     * Flag, which indicated that person don't exist.
     */
    static final int PERSON_DONT_EXIST = 0;
    
    
    /**
     * Socketreceives flag converted to char from ascii so there is needd to
     * convert it.
     * 
     * @param flag is flag who should be coverted.
     */
    static final int convert(int flag)
    {
        return flag - 48;
    }
}
