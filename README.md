# WriteToMe!

## About
WriteToMe! is a server-client standalone chat application with functionality like sending messages between friends, inviting or deleting them it also shows in real time when the friend logged in or out with a few seconds delay. It was my first project written in java on second year of my studies for event programming subject. The front-end layer - user interface was written in swing library and the logic part of the application especially the network side with the help of low-level server socket structures, so complitely from scratch without any high level network libraries support. Project was written following the MVC pattern and constist of two subprojects: the client and server side.

## Get started

### Run the server

Executable files are available in the root directory of the project. In order to run application first of all you have to start the server. By default the server listens on the localhost on the port 1550.
```
java -jar Write2MeServer.jar
```
That command runs main server thread which now logged information that is waiting for new clients - new users to logged in:

<img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/1.png" width="700" />

During the life of the application server will logg a lot of informations on the terminal, if you want to have that information in the file just forward the stream to the specific log file.

### Run the client

After run the server in second terminal window run the client application:
```
java -jar Write2Me.jar
```
The window will display with request of filling the details, so username and password. 

<img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/2.png" width="700" />

You can log out by closing main window and if next time you open the application just type your credentials and window with your mates will appear. The server does not keep the history of conversations. Application has basic validation for the filling the credentials, especially if you type username which is occupied the application will inform you about that fact. 

### Invite and remove friends

Next you can invite the friends, by typing in the input field in the middle bottom of the window and clicking invite button:

<img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/3.png" width="700" />

The list of sent and received invitations are in the right up corner. There you can accept or refuse the invitation. If someone add you to the mates or accept your offer the application will inform you about that on the main screen. You can also remove the mates. To do that just right click on the mate and click once again on appeared small window. 

All the time your mates are illuminated on green or red depending whether mate is accesible or not. The logic behind that mechanism is continuesly sending by your running application ping signals to the server. If after specific time approximately 20 seconds server will not receive signal then notifies all user's mates about that fact and finally that people will see you logged out. From the other hand if server in some way will stop working all users will be notified about that event and them windows will be frozen with possibility to close the application. 

### Send messages

Main feature of the WriteToMe! application is sending messages between users. To that type the message on the input box (the same input field for inviting and sending messages), then choose the user from the left panel and click send button:

<img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/4.png" width="700" />

## Build
I wrote special script for automative building by one command. In command line type:
```
./build
```
In the result two subprojects client and server side will be compile and pack into jars.

## Questions or need help?
Don't hesitate to send me a mail on jakub.wieczorek0101@gmail.com.

## Copyright and license
WriteToMe! project is copyright to Jakub Wieczorek under the [MIT License](https://opensource.org/licenses/MIT).

[wiki]: https://github.com/jakubwieczorek/WriteToMe/wiki
