/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Wieczorek.Jakub.ChatApplication.Server;

import Wieczorek.Jakub.ChatApplication.Protocol;
import Wieczorek.Jakub.ChatApplication.Server.Server.ControllerServerClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Jakub Wieczorek
 * 
 * @version 1.1
 */
@RunWith(MockitoJUnitRunner.class)
public class ServerTest 
{
    @Mock
    Server server;
    
    @Mock
    Socket socket;
    
    @Test
    public void testReadUserNameAndPass() throws IOException
    {
        //Given
        Mockito.when(socket.getInputStream()).thenReturn(System.in);
        Mockito.when(socket.getOutputStream()).thenReturn(System.out);
        ControllerServerClient client = server.new ControllerServerClient(socket, server);
        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.when(bufferedReader.readLine()).thenReturn("Kuba:pass");
        client.getTheView().setBufferedReader(bufferedReader);
        Mockito.when(server.findPerson("Kuba")).thenReturn(null);
        
        //When
        ControllerServerClient otherPerson = client.readUserNameAndPass();

        //Then
        assertEquals(client.getTheModel().getUserName(), "Kuba");
        assertEquals(client.getTheModel().getPassword(), "pass");
        assertEquals(otherPerson, null);
    }
}
