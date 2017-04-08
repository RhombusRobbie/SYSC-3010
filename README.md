=================================================================================================================
=================================================================================================================
=================================================================================================================

README File for SYSc 3010 Project: Child Monitoring System

Project Members

        Husin
	
        Robbie
	
        Alex
	
        Adebola

Github navigation:
	Communication Control - contains all file used for running and testing the Communicator Control.
	Emotion Control - contains all the files used for running and testing the Emotion Control.
	GUI-DB contains all files used to set up and run the server.
	Game Control - contains all the files used for running and testing the Game Control.
	SherlockHolmes - contains the files for the Android app.
=================================================================================================================
=================================================================================================================
=================================================================================================================

______________________________________________
        CHILD MONITORING SYSTEM             
______________________________________________
OVERVIEW
Depression is a serious mental illness and can affect children as young as 6 years old. 
Depression is already a difficult mental illness to detect but in children it can prove 
to be even harder as children often have difficulty communicating their feelings.
The Purpose of this project is to design a system that makes it easier for Parent, guardian
and psychologist to monitor a child behaviour.

=================================================================================================================
=================================================================================================================
=================================================================================================================

RESPONSIBILITIES

Husin : Program the server that controls the entire system. 
Robbie: Emotion Control and the Game developement
Alex:   Communicator and Game development
Adebola: Smartphone Application

=================================================================================================================
=================================================================================================================
=================================================================================================================
RUNNING THE PROGRAM

All the diffirent components of the project(Emotion control, smartphone Application, Communication control) are first
connected to the server and the server sends acknowledgement to approve the connection.

STARTING THE SMARTPHONE

* Change Ip address of the smartphone Application in the MainActivity to match the IP address of the network where the program is running
      *  Run command prompt on the computer being used
            * select ipv4 of wireless or ethernet depending on the connection
	    
* Define a well known port for both the server and the Application

*  Edit Manifest and Enable/Disable the Call and Network Permission

*  Open res - > values -> drag a notification picture to this directory

* change the Emulator settings to Software 

* Install the APK

SET UP AND RUN FOR: EMOTION CONTROLLER

Edit the value of the server's IP to it's current IP by changing the value at SERVER_IP 

After that, simply run the python file via the command line.

SET UP AND RUN FOR: GAME CONTROLLER

Edit the value of the server's IP to it's current IP by changing the value at SERVER_IP 

After that, simply run the python file via the command line.

SET UP AND RUN FOR: COMMUNICATION CONTROL

Edit the IP in ComSendReceive, by chaning the value of SERVER_IP. 

compile the project into an executable jar and run it with java -jar jarName.jar 
