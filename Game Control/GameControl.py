import socket, sys, pifacedigitalio
from time import sleep
from random import randint
from binascii import hexlify

"""
	Game Control
	Sherlock Child Monitoring System
	Author:		Robert Graham & Alex Robertson
	Date:		7 April 2017
	
	Game control is used for an implementation of Simon Says,
	a game where the player must copy a light sequence. As the
	game progresses, more steps are added to the sequence until
	either the player incorrectly reproduces the sequence or
	reaches the maximum number of rounds.
"""

# Game control extras	
ON = 1
OFF = 0
LED_OFFSET = 2	
MAX_ROUNDS = 5
VERBOSE = True

# UDP
SERVER_IP = "127.0.0.1"
SERVER_PORT = 2008
BUFFER_SIZE = 1024

# Server connection
CONNECTION_ATTEMPTS = 3
TIMEOUT = 1

# Component IDs
SIMON_ID = b'\x03'
SERVER_ID = b'\x05'
  

# Flash the lights to indicate the player lost the game
def failLights():
	for i in range(3):
		for j in range(3):
			pfd.output_pins[j + LED_OFFSET].value = ON
		sleep(0.5)
		for j in range(3):
			pfd.output_pins[j + LED_OFFSET].value = OFF
		sleep(0.5)
	return

# Flutter the lights to indicate the player won the game
def winLights():
	for i in range(3):
		for j in range(3):
			pfd.output_pins[j + LED_OFFSET].value = ON
			sleep(0.1)
			pfd.output_pins[j + LED_OFFSET].value = OFF
	return

# Flicker the lights to indicate the game is about to begin
def startLights():
	for i in range(3):
		pfd.output_pins[0 + LED_OFFSET].value = ON
		pfd.output_pins[1+ LED_OFFSET].value = OFF
		pfd.output_pins[2+ LED_OFFSET].value = ON
		pfd.output_pins[3+ LED_OFFSET].value = OFF
		sleep(0.1)
		pfd.output_pins[0 + LED_OFFSET].value = OFF
		pfd.output_pins[1+ LED_OFFSET].value = ON
		pfd.output_pins[2+ LED_OFFSET].value = OFF
		pfd.output_pins[3+ LED_OFFSET].value = ON
		sleep(0.1)
	return

def udpSend(s, address, state):
   address = (SERVER_IP , SERVER_PORT)
   s.sendto(state, address)

def Print(string):
	if VERBOSE:
		print string
	return

def main():
    pfd = pifacedigitalio.PiFaceDigital()
    # Instruction sequence
    lst = []
    rounds = 0
    lost = False
    buttonPressed = False
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.settimeout(TIMEOUT)
    Print("Request server connection\n")
    for i in range(CONNECTION_ATTEMPTS + 1):
		
        if i == CONNECTION_ATTEMPTS:
            raise RuntimeError("Unable to connect to server")
		
        Print("Connection attempt " + str(i + 1))
		
		# Send emotion control ID to server
        Print("Sending...")
        Print("Data:\t" + binascii.hexlify(SIMON_ID))
        Print("IP:\t" + SERVER_IP)
        Print("Port:\t%d" % SERVER_PORT)
        s.sendto(SIMON_ID, (SERVER_IP, SERVER_PORT))
		
		# Attempt to receive a response from the server
        try:
            data, address = s.recvfrom(BUFFER_SIZE)
        except socket.timeout:
            Print("Timed out\n")
            continue
        s.settimeout(None)
        Print("\nPacket received")
        Print("Data:\t" + binascii.hexlify(data))
        Print("IP:\t" + address[0])
        Print("Port:\t%d" % address[1])
		# Check if message is actually from the server
        if data[0] == SERVER_ID:
            i = 6
        else:
			#raise RuntimeError("Unknown response")
            Print("Unknown response")
	
	while True:
		
		lost = False
		# Receive on port into buf
		print ("Waiting to receive")
		data, address = s.recvfrom(BUFFER_SIZE)
		
		# Assert received from server
		if data[0] != SERVER_ID:
			Print("Invalid response")
			continue
		
		# New empty list for new game
		buf = []
		
		for rounds in range(MAX_ROUNDS):
			if lost:
			    break
			buf.append(random.randint(0, 3))
			#print ("value in column %s" % (buf))
			# Test the user's memory
			for i in range (len(buf)):
			    pfd.output_pins[buf[i]].value = ON
			    time.sleep(0.5)
			    pfd.output_pins[buf[i]].value = OFF
			for k in range(len(buf)):
				# Wait until a button is pressed
				while not buttonPressed:
					buttonPressed = pfd.input_pins[0].value == ON or pfd.input_pins[1].value == ON or pfd.input_pins[2].value == ON or pfd.input_pins[3].value == ON
				buttonPressed = False
				# A button has been pressed
				
				# Check each button to find out which has been pressed
				# If the right one had been pressed then turn the light on briefly and continue
				if pfd.input_pins[(buf[k] + (-1) ** (buf[k] + 1)) % 4].value == 1:
					pfd.output_pins[buf[k]].value = 1;
					time.sleep(0.5)
					pfd.output_pins[buf[k]].value = 0;
					continue
				# If the wrong one has been pressed then flash the fail lights	
				else:
					print ("Better luck next time")
					failLights()
					lost = True
					rounds = 0
					break
			
				print ("Winner winner chicken dinner!")
				rounds = 0
				winLights()
					
if __name__ == "__main__":
	main()
