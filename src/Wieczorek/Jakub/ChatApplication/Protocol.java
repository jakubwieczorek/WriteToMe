package Wieczorek.Jakub.ChatApplication;

/**
 * Class created in order to systematize data transfering between programs.
 * Before each message program's sending determined flag. Then program
 * who should receive message, before that message gets proper flag. So question about
 * mate, simple message has unique flag.
 * 
 * @author Jakub Wieczorek
 * @version 1.1
 */
public class Protocol 
{
    /**
     * Flag, which indicated that after this flag is message.
     */
    public static final char MESSAGE = '2';
    
    /**
     * Flag, which indicated that person exist.
     */
    public static final char PERSON_EXIST = '1';
    
    /**
     * Flag, which indicated that person exist.
     */
    public static final char PERSON_INQUIRE = '3';

    /**
     * Flag, which indicated that person don't exist.
     */
    public static final char PERSON_DONT_EXIST = '0';
    
    /**
     * Flag which indicate that person have jast logged out.
     */
    public static final char EXIT = '4';
    
    /**
     * Flag which indicate that agreed to add following person to mates.
     */
    public static final char AGREE = '5';
    
    /**
    * Flag which indicate that user disagree to add following person to mates.
    */
    public static final char DISAGREE = '6';
    
    /**
    * Flag which indicate that after this flag is answer.
    */
    public static final char ANSWER = '7';
    
    /**
    * Flag which indicate that after this flag is person who invie.
    */
    public static final char PERSON_INVITATION = '8';
    
    /**
    * Flag which indicate that after this flag is person who invie.
    */
    public static final char FROM_ME = 'a';
    
    /**
    * Flag which indicate that after this flag is person who invie.
    */
    public static final char TO_ME = 'b';
    
    /**
    * Flag which indicate that after this flag is person who invie.
    */
    public static final char INITIATE = 'c';
    
    /**
    * Flag which indicate that after this flag is person who invie.
    */
    public static final char MATE = 'd';
    
    
    /**
    * Flag which indicate that user is logged in different device.
    */
    public static final char LOGGED_IN_DIFFERENT_DEVICE = 'e';
}
