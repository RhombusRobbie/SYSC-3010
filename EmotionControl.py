from time import sleep
import pifacedigitalio, socket, sys

"""
	SYSC 3010 Sherlock Emotion Control
	
	Author: Robert Graham
		100981086
		Group F3
	Date: 	26 March 2017
	
	Controller for the emotions of Sherlock. i.e. Two stepper motors
	which control the position of the eyebrows and mouth seperately.
	Emotion codes are recieved by the server and then emotions are updated
	accordingly. Sherlock starts out in a default emotion and returns to
	that emotion when shut down so that the state is always known.
	
	For all degrees
	Positive: counter-clockwise
	Negative: clockwise
"""

UDP_IP = "127.0.0.1"
UDP_PORT = 5005

pfd = pifacedigitalio.PiFaceDigital()

# Stepper motor pulses per second delay (max 0.0125 Hz = 800 pps)
PPS_DELAY = 0.02

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

DEFAULT_POS = NEUTRAL_POS

"""
	Rotates the eyebrows by the specified amount of degrees. Eyebrow
	stepper motor must be connected to pins 4-8.
"""
def moveEyebrows(degrees):
    steps = int(float(degrees) / 360 * 512)
	# Counter-clockwise
    if steps >= 0:
        pfd.output_pins[4].value = 1
        for i in range(steps):
            pfd.output_pins[(i - 1) % 4 + 4].value = 0
            pfd.output_pins[(i + 1) % 4 + 4].value = 1
            sleep(PPS_DELAY)
	# Clockwise
    else:
        pfd.output_pins[6].value = 1
        for i in range(-steps, 0, -1):
            pfd.output_pins[(i - 1) % 4 + 4].value = 1
            pfd.output_pins[(i + 1) % 4 + 4].value = 0
            sleep(PPS_DELAY)
			
    for i in range(4, 8):
        pfd.output_pins[i].value = 0
    return

"""
	Rotates the mouth by the specified amount of degrees. Mouth
	stepper motor must be connected to pins 0-3.
"""
def moveMouth(degrees):
    steps = int(float(degrees) / 360 * 512)
    # Counter-clockwise
    if steps >= 0:
        pfd.output_pins[0].value = 1
        for i in range(steps):
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
			
    for i in range(4):
        pfd.output_pins[i].value = 0
    return

"""
	Waits for an emotion code from the server via UDP. Updates the 
	eyebrows and mouth accordingly. When the shutdown code is recieved,
	reset emotion to default position.
"""
def main():
    pifacedigitalio.init()
    currentPosition = list(DEFAULT_POS)
	
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind((UDP_IP, UDP_PORT))
    
    while True:
        data, addr = sock.recvfrom(1024)
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
		
        # Reset to neutral emotion and exit loop
        elif emotionCode == SHUTDOWN_CODE:
            moveEyebrows(DEFAULT_POS[0] - currentPosition[0])
            break
    return

main()
