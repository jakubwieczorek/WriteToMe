# WriteToMe!

## About
WriteToMe! is a server-client standalone chat application with functionality like sending messages between friends, inviting or deleting them it also shows in real time when the friend logged in or out with a few seconds delay. It was my first project written in java on second year of my studies for event programming subject. The front-end layer - user interface was written in swing library and the logic part of the application especially the network side with the help of low-level server socket structures, so complitely from scratch without any high level network libraries support. Project was written following the MVC pattern.

## Get started
Executable files are available in the root directory of the project. In order to run application first of all you have to start the server. By default the server listens on the localhost on the port 1550.
```
java -jar Write2MeServer.jar
```
That command runs main server thread which now logged information that is waiting for new clients, so new users to logged in:

<img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/1.png" width="700" />

After that in second terminal window run the client application:
```
java -jar Write2Me.jar
```
The window will display. Fill in the details, so username and password. 
<p>
  <img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/2.png" width="300" />
  <img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/3.png" width="300" />
</p>
You can log out by closing main window and if next time you open the application just type your credentials and window with your mates will appear. The server does not keep the history of conversations. Application has basic validation for the filling the credentials, especially if you type username which is occupied the application will inform you about that fact. 

Next you can invite the friends, by typing in the input field in the middle bottom of the window and clicking invite button:
<p>
  <img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/4.png" width="300" />
  <img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/5.png" width="300" />
</p>
The list of sent and received invitations are in the right up corner. There you can accept or refuse the invitation. If someone add you to the mates or accept your offer the application will inform you on the main screen. You can also remove the mate. To do that just right click on the mate and click once again on appeared small window. 

All the time your mates are illuminated on green or red depending whether mate is accesible or not. The logic behind that mechanism is continuesly sending by your running application ping signals to the server. If after specific time approximately 20 seconds server will not receive signal then notifies all user's mates about that fact and finally that people will see you logged out. From the other hand if server in some way will stop working all users will be notified about that event and them windows will be frozen with possibility to close the application. 

Main feature of the WriteToMe! application is sending messages between users. To that type the message on the input box (the same input field for inviting and sending messages), then choose the user from the left panel and click send button.


## Project structure
The application consists of two parts: the client and server side

## Questions or need help?
Don't hesitate to send me a mail on jakub.wieczorek0101@gmail.com.

## Copyright and license
WriteToMe! project is copyright to Jakub Wieczorek under the [MIT License](https://opensource.org/licenses/MIT).

[wiki]: https://github.com/jakubwieczorek/WriteToMe/wiki
