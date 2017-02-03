import socket, sys, time, pifacedigitalio
			
# Flash the lights 3 times to indicate the user failed the level
def failLights():
	for i in range(7):
		pfd.output_pins[0].value = 1
		pfd.output_pins[1].value = 1
		pfd.output_pins[2].value = 1
		pfd.output_pins[3].value = 1
		time.sleep(0.5)
		pfd.output_pins[0].value = 0
		pfd.output_pins[1].value = 0
		pfd.output_pins[2].value = 0
		pfd.output_pins[3].value = 0
	return

def winLights():
	for i in range(7):
		pfd.output_pins[i].value = 1
		time.sleep(0.1)
		pfd.output_pins[i].value = 0
	return

def udpSend(s, address, state):
   address = ('10.0.0.1' , 2148)
   s.sendto(state, address)

pfd = pifacedigitalio.PiFaceDigital()

buttonPressed = False

# Port stuff
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
port = 2136
server_address = ('10.0.0.31', port)
s.bind(server_address)

# Instruction list
lst = []
rounds = 0
lost = False
while True:
	lost = False
	# Receive on port into buf
	print ("Waiting to receive")
	buf, address = s.recvfrom(2048)
	rounds+=1
	print ("value in column %s" % (buf))
	# Assert buf is not empty
	#buf = [0, 1, 2, 3]
	if not len(buf):
		break
	
	# Display each instruction for one second
	for i in range(rounds):
		print(i)
		print(buf[i]-49)
		#print(int.from_bytes(buf[i]))
		pfd.output_pins[buf[i]-49].value = 1
		time.sleep(1)
		pfd.output_pins[buf[i]-49].value = 0
	
	# Test the user's memory
	for k in range(rounds):
		# Wait until a button is pressed
		
		while not buttonPressed:
			buttonPressed = pfd.input_pins[0].value == 1 or pfd.input_pins[1].value == 1 or pfd.input_pins[2].value == 1 or pfd.input_pins[3].value == 1
		buttonPressed = False
		# A button has been pressed
		
		# Check each button to find out which has been pressed
		# If the right one had been pressed then turn the light on briefly and continue
		# If the wrong one has been pressed then flash the fail lights	
		if pfd.input_pins[buf[k]-49].value == 1:
			pfd.output_pins[buf[k]-49].value = 1;
			time.sleep(0.5)
			pfd.output_pins[buf[k]-49].value = 0;
			continue
		else:
			failLights()
			lost = True
			break
	if(rounds >= 4) and not lost:
		udpSend(s, address, b'1')
		rounds = 0
		winLights()
	elif lost:
		udpSend(s, address, b'2')
		rounds = 0
	else:
	    udpSend(s, address, b'3')

