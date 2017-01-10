/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Wieczorek.Jakub.ChatApplication;

import java.io.BufferedReader;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/** 
 * @author Jakub Wieczorek
 * 
 * @version 1.1
 */
public class MessageTest 
{
    @Test
    public void testReceive() throws Exception 
    {
        //Given
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.when(bufferedReader.readLine()).thenReturn("2;Kuba");
        Message instance = new Message();
        
        //When
        instance.receive(bufferedReader);

        //Then
        String[] expResult = {"2", "Kuba"};
        String[] result = {instance.getFlag(), instance.getText()};
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testSplitFlagAndContents()
    {
        //Given
        String msg = "2;Kuba";
        Message instance = new Message();
        
        //when
        String [] result = instance.splitFlagAndContents(msg);
        String [] expResult = {"2", "Kuba"};
        
        //Then
        assertArrayEquals(expResult, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test2SplitFlagAndContents()
    {
        //Given
        String msg = "2Kuba";
        Message instance = new Message();
        
        //when
        String [] result = instance.splitFlagAndContents(msg);
        String [] expResult = {"2", "Kuba"};
        
        //Then
        assertArrayEquals(expResult, result);
    }
}
