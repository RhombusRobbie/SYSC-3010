# Trial Project - Two-Player Simon #

A two-player version of the classic 1980s game Simon by Milton Bradley. One player takes the role of Simon, the device, and chooses the sequence for the other player to replicate. Each game can run for a maximum of four levels until which point the player wins. Data is sent from one Raspberry Pi to another via the Linux machine using UDP. 

### Required Materials ###
* 2 Raspberry Pis model 2 or higher
* 2 PiFaces
* 2 Ethernet cables
* 1 Linux machine
* Player.py
* Server.java
* Simon.py

### Instructions ###
1.	Attach a PiFace to each of two Raspberry Pis. One will be the player and the other will be Simon.
2.	Connect the two Raspberry Pis to a Linux machine via Ethernet cable.
3.	On the Raspberry Pi for the player, compile Player.py with Python 3 and run it. No arguments are required.
4.	On the Linux machine, compile Server.java and run it with the following arguments: Server port, Simon address, Simon port, Player address, Player port. Note that all ports must be different from each other.
5.	On the Raspberry Pi for Simon, compile Simon.py with Python 3 and run it with the following arguments: Server address, Server Port.
6.	The game is now ready to play. The lights on the Simon PiFace will display a pattern. Simon begins pressing one of the four buttons on the PiFace which will be the first instruction in the sequence.
7.	The corresponding light on the PiFace of the Player will light up and the Player must press the corresponding button to advance the game. Note that the buttons and the lights are reversed from each other i.e. button 1 is on the left side while light 1 is on the right.
 1.	If the wrong button(s) are pressed, all lights will flash three times and the game will restart prompting Simon to choose the first instruction.
 2.	If the correct button(s) are pressed, then Simon will be prompted to choose the next value in the sequence.
    1.	If the Player correctly repeats the sequence for four levels, then they win the game. The lights on their PiFace will display a sequence and the game will restart.
