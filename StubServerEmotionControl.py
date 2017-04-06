import socket, binascii
from random import randint
from time import sleep

LOCAL_IP = "127.0.0.1"
LOCAL_PORT = 2008
BUFFERSIZE = 1024

# IDs of sysetem machines
EMOTION_CONTROL_ID = b'\x02'
SERVER_ID = b'\x05'

# TO-DO Change to tuple of byte arrays
NEUTRAL = bytearray([5, 0])
HAPPY = bytearray([5, 1])
SAD = bytearray([5, 2])
ANGRY = bytearray([5, 3])
SHUTDOWN = bytearray([5, 255])

CODES = (b'\x05\x00', b'\x05\x01', b'\x05\x02', b'\x05\x03', b'\x05\xFF')

RANDOM_TEST = 	True
NUMBER_OF_TESTS = 5
NEW_EMOTION_DELAY = 4

def main():
	print "Emotion Controller Mock Server"
	print "Author:	Robert Graham 100981086"
	print "Date:	4 March 2017\n"
	
	print "Local IP:\t" + LOCAL_IP
	print "Local port:\t%d" % LOCAL_PORT

	while True:
		# Wait for connection request from emotion control
		sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
		sock.bind((LOCAL_IP, LOCAL_PORT))
		print "\nWaiting for request..."
		data, (emotionControlIP, emotionControlPort) = sock.recvfrom(BUFFERSIZE)
		print "Request received"
		print "Data:\t" + binascii.hexlify(data)
		print "IP:\t" + emotionControlIP
		print "Port:\t%d" % emotionControlPort

		# Handshake
		print "\nSending handshake..."
		print "Data:\t" + binascii.hexlify(SERVER_ID)
		print "IP:\t" + emotionControlIP
		print "Port:\t%d" % emotionControlPort
		sock.sendto(SERVER_ID, (emotionControlIP, emotionControlPort))

		sleep(2)

		if RANDOM_TEST:
			for i in range(NUMBER_OF_TESTS):
				rand = randint(0, 3)
				sock.sendto(CODES[rand], (emotionControlIP, emotionControlPort))
				print rand
				sleep(NEW_EMOTION_DELAY)

		else:
			sock.sendto(SAD, (emotionControlIP, emotionControlPort))
			print("Sad")
			sleep(NEW_EMOTION_DELAY)
			sock.sendto(HAPPY, (emotionControlIP, emotionControlPort))
			print("Happy")
			sleep(NEW_EMOTION_DELAY)
			sock.sendto(NEUTRAL, (emotionControlIP, emotionControlPort))
			print("Neutral")
			sleep(NEW_EMOTION_DELAY)
			sock.sendto(ANGRY, (emotionControlIP, emotionControlPort))
			print("Angry")
			sleep(NEW_EMOTION_DELAY)
			sock.sendto(NEUTRAL, (emotionControlIP, emotionControlPort))
			print("Neutral")
			sleep(NEW_EMOTION_DELAY)
			sock.sendto(HAPPY, (emotionControlIP, emotionControlPort))
			print("Happy")
			sleep(NEW_EMOTION_DELAY)
			sock.sendto(SAD, (emotionControlIP, emotionControlPort))
			print("Sad")
			sleep(NEW_EMOTION_DELAY)
			sock.sendto(SAD, (emotionControlIP, emotionControlPort))
			print("Sad")
			sleep(NEW_EMOTION_DELAY)

		print("Shutdown")
		sock.sendto(SHUTDOWN, (emotionControlIP, emotionControlPort))
		
		sock.close()
		
	return

if __name__ == "__main__":
	main()
