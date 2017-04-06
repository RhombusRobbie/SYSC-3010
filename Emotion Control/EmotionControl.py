from time import sleep
import pifacedigitalio, socket, sys, binascii

"""
	Eyebrows:
	All degrees are measured from the horizontal facing right. i.e.
	Positive is counter-clockwise and negative is clockwise
	Mouth:
	Vertical position down from mouth fully closed in 
	
"""

# Print information to console (for testing)
VERBOSE = True

# UDP constants
SERVER_IP = "127.0.0.1" #"10.0.0.1"
SERVER_PORT = 2008
BUFFER_SIZE = 1024

# Number of attempts to make an initial connection to the server
CONNECTION_ATTEMPTS = 3

# Socket timeout in seconds
TIMEOUT = 1

# IDs of sysetem machines
EMOTION_CONTROL_ID = b'\x02'
SERVER_ID = b'\x05'

# UDP emotion codes
NEUTRAL_CODE = b'\x00'
HAPPY_CODE = b'\x01'
SAD_CODE = b'\x02'
ANGRY_CODE = b'\x03'
SHUTDOWN_CODE = b'\xFF'

# Positions of eyebrows and mouth respectively
POSITIONS = ((0, 0.5), (0, 0), (-30, 1), (30, 0.5))

# Position set to at shutdown
DEFAULT_POS_INDEX = 0

# Stepper motor pulses per second delay (min 0.0125 s = max 800 pps)
PPS_DELAY = 0.02

# PiFace
pfd = pifacedigitalio.PiFaceDigital()
ON = 1
OFF = 0
MOUTH_STEPPER_OFFSET = 4

"""
	Rotates the eyebrows by the specified amount of degrees. Eyebrow
	Stepper motor must be connected to pins 4-8.
"""
def moveEyebrows(degrees):
	# Double the amount of steps required. Double because two steps are
	# performed in each loop.
    halfSteps = int(float(degrees) / 360 * 512)
    
	# Counter-clockwise
    if halfSteps >= 0:
        for i in range(halfSteps):
            pfd.output_pins[(i - 1) % 4 + MOUTH_STEPPER_OFFSET].value = OFF
            pfd.output_pins[(i + 1) % 4 + MOUTH_STEPPER_OFFSET].value = ON
            sleep(PPS_DELAY)
            
	# Clockwise
    else:
        for i in range(-halfSteps, 0, -1):
            pfd.output_pins[(i - 1) % 4 + MOUTH_STEPPER_OFFSET].value = ON
            pfd.output_pins[(i + 1) % 4 + MOUTH_STEPPER_OFFSET].value = OFF
            sleep(PPS_DELAY)
	
	# Turn off all pins
    for i in range(4, 8):
        pfd.output_pins[i].value = OFF
        
    return

"""
	Opens the mouth by the specified amount of openness. Mouth
	stepper motor must be connected to pins 0-3.
"""
def moveMouth(openness):
	# Double the amount of steps required. Double because two steps are
	# performed in each loop.
    halfSteps = int(openness * 200)
    
    # Counter-clockwise
    if halfSteps >= 0:
        for i in range(halfSteps):
            pfd.output_pins[(i - 1) % 4].value = OFF
            pfd.output_pins[(i + 1) % 4].value = ON
            sleep(PPS_DELAY)
            
	# Clockwise
    else:
        for i in range(-halfSteps, 0, -1):
            pfd.output_pins[(i - 1) % 4].value = ON
            pfd.output_pins[(i + 1) % 4].value = OFF
            sleep(PPS_DELAY)
	
	# Turn off all pins
    for i in range(4):
        pfd.output_pins[i].value = OFF
        
    return

"""
	Print to the console if in verbose mode. Should be used during testing only
"""
def Print(string):
	if VERBOSE:
		print string
	return

"""
	Attempts to connect to the server
	Waits for an emotion code from the server via UDP. Updates the 
	eyebrows and mouth accordingly. When the shutdown code is recieved,
	reset emotion to default position.
"""
def main():
	print "Emotion Controller"
	print "Author:	Robert Graham 100981086"
	print "Date:	4 March 2017\n"
	pifacedigitalio.init()
	# Current position of the eyebrows and mouth respectively
	currentPosition = POSITIONS[DEFAULT_POS_INDEX]

	# Address family: Internet protocol
	# Socket kind: Datagram
	sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	sock.settimeout(TIMEOUT)
	
    # Link to server
	Print("Request server connection\n")
	for i in range(CONNECTION_ATTEMPTS + 1):
		
		if i == CONNECTION_ATTEMPTS:
			raise RuntimeError("Unable to connect to server")
		
		Print("Connection attempt " + str(i + 1))
		
		# Send emotion control ID to server
		Print("Sending...")
		Print("Data:\t" + binascii.hexlify(EMOTION_CONTROL_ID))
		Print("IP:\t" + SERVER_IP)
		Print("Port:\t%d" % SERVER_PORT)
		sock.sendto(EMOTION_CONTROL_ID, (SERVER_IP, SERVER_PORT))
		
		# Attempt to receive a response from the server
		try:
			data, address = sock.recvfrom(BUFFER_SIZE)
		except socket.timeout:
			Print("Timed out\n")
			continue
		sock.settimeout(None)
		Print("\nPacket received")
		Print("Data:\t" + binascii.hexlify(data))
		Print("IP:\t" + address[0])
		Print("Port:\t%d" % address[1])
		# Check if message is actually from the server
		if data[0] == SERVER_ID:
			break
		else:
			#raise RuntimeError("Unknown response")
			Print("Unknown response")
			
	# Wait for emotion updates
	while True:
		Print("\nWaiting for emotion codes...")
		data, address = sock.recvfrom(BUFFER_SIZE)
		code = data[1]
		Print("Emotion code received")
		Print("Data:\t" + binascii.hexlify(data))
		Print("IP:\t" + address[0])
		Print("Port:\t%d" % address[1])
		
		index = int(code.encode('hex'), 16)
		
		if code >= b'\x00' and code <= b'\x04':
			Print("\nUpdating emotion...")
			moveEyebrows(POSITIONS[index][0] - currentPosition[0])
			moveMouth(POSITIONS[index][1] - currentPosition[1])
			currentPosition = POSITIONS[index]
			Print("Current position: (" + str(currentPosition[0]) + ", " + str(currentPosition[1]) + ")")
		
		elif code == b'\xFF':
			Print("Resetting emotion...")
			moveEyebrows(POSITIONS[DEFAULT_POS_INDEX][0] - currentPosition[0])
			moveMouth(POSITIONS[DEFAULT_POS_INDEX][1] - currentPosition[1])
			Print("Shut down")
			break
			
		else:
			Print("Invalid code")
	
	# CLean-up
	sock.close()
	for i in range(8):
		pfd.output_pins[i].value = 0

if __name__ == "__main__":
	main()
