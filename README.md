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
The window will display. Fill in the details about new user, so username and password. 
<img src="https://raw.githubusercontent.com/wiki/jakubwieczorek/WriteToMe/2.png" width="700" />

## Project structure
The application consists of two parts: the client and server side. Also in seperate directory are executable jars, which you can simply use and the code documentation.

## Questions or need help?
Don't hesitate to send me a mail on jakub.wieczorek0101@gmail.com.

## Copyright and license
WriteToMe! project is copyright to Jakub Wieczorek under the [MIT License](https://opensource.org/licenses/MIT).

[wiki]: https://github.com/jakubwieczorek/WriteToMe/wiki
