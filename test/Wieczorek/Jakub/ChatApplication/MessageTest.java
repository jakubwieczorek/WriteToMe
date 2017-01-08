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
 *
 * @author jakub
 */
public class MessageTest {
     @Test
    public void testReceive() throws Exception {
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.when(bufferedReader.readLine()).thenReturn("Kuba");
        Mockito.when(bufferedReader.read()).thenReturn(2);

        Message instance = new Message();
        instance.receive(bufferedReader);

        String[] expResult = {String.valueOf(2), "Kuba"};
        String[] result = {String.valueOf(instance.getFlag()), instance.getText()};

        assertArrayEquals(expResult, result);
    }
}
