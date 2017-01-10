package Wieczorek.Jakub.ChatApplication;

/**
 * Class created in order to systematize data transfering between programs.
 * Before each message program's sending determined flag.
 * 
 * @author Jakub Wieczorek
 * @version 1.1
 */
public class Protocol 
{
    /**
     * Flag, which indicated that after this flag is message.
     */
    public static final String MESSAGE = "2";
    
    /**
     * Flag, which indicated that person exist.
     */
    public static final String PERSON_EXIST = "1";
    
    /**
     * Flag, which indicated that after is querry about person.
     */
    public static final String PERSON_INQUIRE = "3";

    /**
     * Flag, which indicated that person don't exist or user input proper pass.
     */
    public static final String PERSON_DONT_EXIST = "0";
    
    /**
     * Flag which indicate that person have jast logged out.
     */
    public static final String EXIT = "4";
    
    /**
     * Flag which indicate agreement to add following person to mates.
     */
    public static final String AGREE = "5";
    
    /**
    * Flag which indicate disagreement to add following person to mates.
    */
    public static final String DISAGREE = "6";
    
    /**
    * Flag which indicate that after this flag is answer.
    */
    public static final String ANSWER = "7";
    
    /**
    * Flag which indicate that after this flag is person who invite.
    */
    public static final String PERSON_INVITATION = "8";
    
    /**
    * Flag which indicate that after this flag is my username.
    */
    public static final String FROM_ME = "a";
    
    /**
    * Flag which indicate that after this flag is person username who want for example add me to mates.
    */
    public static final String TO_ME = "b";
    
    /**
    * Flag which indicate that after this flag is initiation or end of initiation.
    */
    public static final String INITIATE = "c";
    
    /**
    * Flag which indicate that after this flag is mate.
    */
    public static final String MATE ="d";
      
    /**
    * Flag which indicate that user is logged in different device.
    */
    public static final String LOGGED_IN_DIFFERENT_DEVICE = "e";
    
    /**
    * Flag which indicate thate after this flag username who for example want to remove me from mates.
    */
    public static final String REMOVE_MATE = "f";
    
    /**
    * Flag which indicate that user is logged.
    */
    public static final String LOGGED = "g";
    
    /**
    * Flag which indicate that user isn't logged.
    */
    public static final String UNLOGGED = "h";
    
    /**
    * Flag which indicate server's just broken down.
    */
    public static final String SERVER_WORKS = "i";
    
}
