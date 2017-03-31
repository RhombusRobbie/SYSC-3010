from time import sleep
import pifacedigitalio, socket, sys

"""
	SYSC 3010 Sherlock Emotion Control
	
	Author: Robert Graham
		100981086
		Group F3
	Date: 	31 March 2017
	
	Controller for the emotions of Sherlock. i.e. Two stepper motors
	which control the position of the eyebrows and mouth seperately.
	Emotion codes are recieved by the server and then emotions are updated
	accordingly. Sherlock starts out in a default emotion and returns to
	that emotion when shut down so that the state is always known.
	
	All degrees are measured from the horizontal facing right. i.e.
	Positive is counter-clockwise and negative is clockwise
"""

# UDP constants
SERVER_IP = "10.0.0.1" # TO-DO Confirm
SERVER_PORT = 2008
BUFFER_SIZE = 1024

# Number of attempts to make an initial connection to the server
CONNECTION_ATTEMPTS = 3

# IDs of sysetem machines
EMOTION_CONRTOL_ID = 2
SERVER_ID = 5

# UDP emotion codes
NEUTRAL_CODE = 1
HAPPY_CODE = 2
SAD_CODE = 3
ANGRY_CODE = 4
SHUTDOWN_CODE = 9

# Positions of eyebrows and mouth respectively
HAPPY_POS = (-15, 0)
SAD_POS = (-45, 0)
NEUTRAL_POS = (0, 0)
ANGRY_POS = (45, 0)

# Position set to at shutdown
DEFAULT_POS = NEUTRAL_POS

# Stepper motor pulses per second delay (min 0.0125 s = max 800 pps)
PPS_DELAY = 0.02

# PiFace
pfd = pifacedigitalio.PiFaceDigital()

"""
	Rotates the eyebrows by the specified amount of degrees. Eyebrow
	Stepper motor must be connected to pins 4-8.
"""
def moveEyebrows(degrees):
	# Double the amount of steps required. Double because two steps are
	# performed in each loop.
	dSteps = int(float(degrees) / 360 * 512)
	
	# Counter-clockwise
	if dSteps >= 0:
		pfd.output_pins[4].value = 1
		for i in range(steps):
			pfd.output_pins[(i - 1) % 4 + 4].value = 0
			pfd.output_pins[(i + 1) % 4 + 4].value = 1
			sleep(PPS_DELAY)
		
	# Clockwise
	else:
		pfd.output_pins[6].value = 1
		for i in range(-dSteps, 0, -1):
			pfd.output_pins[(i - 1) % 4 + 4].value = 1
			pfd.output_pins[(i + 1) % 4 + 4].value = 0
			sleep(PPS_DELAY)
	
	# Turn off all pins
	for i in range(4, 8):
		pfd.output_pins[i].value = 0

	return

"""
	Rotates the mouth by the specified amount of degrees. Mouth
	stepper motor must be connected to pins 0-3.
"""
def moveMouth(degrees):
	# Double the amount of steps required. Double because two steps are
	# performed in each loop.
	dSteps = int(float(degrees) / 360 * 512)

	# Counter-clockwise
	if dSteps >= 0:
		pfd.output_pins[0].value = 1
		for i in range(dSteps):
			pfd.output_pins[(i - 1) % 4].value = 0
			pfd.output_pins[(i + 1) % 4].value = 1
			sleep(PPS_DELAY)

	# Clockwise
	else:
		pfd.output_pins[6].value = 1
		for i in range(-steps, 0, -1):
			pfd.output_pins[(i - 1) % 4].value = 1
			pfd.output_pins[(i + 1) % 4].value = 0
			sleep(PPS_DELAY)
	
	# Turn off all pins		
	for i in range(4):
		pfd.output_pins[i].value = 0

	return

"""
	Attempts to connect to the server
	Waits for an emotion code from the server via UDP. Updates the 
	eyebrows and mouth accordingly. When the shutdown code is recieved,
	reset emotion to default position.
"""
def main():
	pifacedigitalio.init()
	# Current position of the eyebrows and mouth respectively
	currentPosition = list(DEFAULT_POS)
	
	# Address family: Internet protocol
	# Socket kind: Datagram
	socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	# Bind to any available port on the local machine
	socket.bind(('localhost', 0))
	
	# Link to server
	for i in range(CONNECTION_ATTEMPTS):
		socket.sendto(EMOTION_CONRTOL_ID, (TRAGET_IP, TARGET_PORT))
		data, addr = socket.recvfrom(BUFFER_SIZE)
		if data[0] == SERVER_ID:
			break
		elif i == CONNECTION_ATTEMPTS:
			raise RuntimeError("Unable to connect to server")
		else:
			raise RuntimeError("Invalid server ID")

	# Wait for emotion updates
	while True:
		data, addr = socket.recvfrom(1024)
		emotionCode = data[3]
		
		if emotionCode == HAPPY_CODE:
			moveEyebrows(HAPPY_POS[0] - currentPosition[0])
			currentPosition[0] = HAPPY_POS[0]
		
		elif emotionCode == SAD_CODE:
			moveEyebrows(SAD_POS[0] - currentPosition[0])
			currentPosition[0] = SAD_POS[0]
		
		elif emotionCode == NEUTRAL_CODE:
			moveEyebrows(NEUTRAL_POS[0] - currentPosition[0])
			currentPosition[0] = NEUTRAL_POS[0]
		
		elif emotionCode == ANGRY_CODE:
			moveEyebrows(ANGRY_POS[0] - currentPosition[0])
			currentPosition[0] = ANGRY_POS[0]
		
		# Reset to default emotion and exit loop
		elif emotionCode == SHUTDOWN_CODE:
			moveEyebrows(DEFAULT_POS[0] - currentPosition[0])
			break
	
	return

main()
