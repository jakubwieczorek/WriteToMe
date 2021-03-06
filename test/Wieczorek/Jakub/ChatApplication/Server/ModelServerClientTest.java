/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Wieczorek.Jakub.ChatApplication.Server;

import java.io.PrintWriter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * @author Jakub Wieczorek
 * 
 * @version 1.1
 */
public class ModelServerClientTest 
{
    private ModelServerClient instance;
        
    @Before
    public void create()
    {
        this.instance = new ModelServerClient(new PrintWriter(System.out));
    }

    /**
     * Test of splitUserNameAndMessage method, of class ModelServerClient.
     */
    @Test
    public void testSplitUserNameAndMessage() 
    {
        String msg = "Kuba:Zdam";
        
        String [] expResult = {"Kuba", "Zdam"};
        String [] result = instance.splitUserNameAndMessage(msg);
        
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test two of splitUserNameAndMessage method, of class ModelServerClient.
     */
    @Test
    public void test2SplitUserNameAndMessage() 
    {
        String msg = ":Zdam";

        String [] expResult = {"", "Zdam"};
        String [] result = instance.splitUserNameAndMessage(msg);
        
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test three of splitUserNameAndMessage method, of class ModelServerClient.
     */
    @Test
    public void test3SplitUserNameAndMessage() 
    {
        String msg = ":";

        String [] expResult = {"", ""};
        String [] result = instance.splitUserNameAndMessage(msg);
        
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test four of splitUserNameAndMessage method, of class ModelServerClient.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test4SplitUserNameAndMessage() 
    {
        String msg = "KubaZdam";
        instance.splitUserNameAndMessage(msg);
    }
}
