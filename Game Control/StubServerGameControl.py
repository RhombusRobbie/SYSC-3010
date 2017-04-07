import socket, binascii
from random import randint
from time import sleep

LOCAL_IP = "127.0.0.1"
LOCAL_PORT = 2008
BUFFERSIZE = 1024

GAME_CONTROL_ID = b'\x02'
SERVER_ID = b'\x05'

RANDOM_TEST =True
NUMBER_OF_TESTS = 5
NEW_EMOTION_DELAY = 4

def main():
	print "Game Controller Mock Server"
	print "Author:	Robert Graham 100981086 & Alex Robertson"
	print "Date:	6 March 2017\n"
	
	print "Local IP:\t" + LOCAL_IP
	print "Local port:\t%d" % LOCAL_PORT

	while True:
		# Wait for connection request from game control
		sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
		sock.bind((LOCAL_IP, LOCAL_PORT))
		print "\nWaiting for request..."
		data, (gameControlIP, gameControlPort) = sock.recvfrom(BUFFERSIZE)
		print "Request received"
		print "Data:\t" + binascii.hexlify(data)
		print "IP:\t" + gameControlIP
		print "Port:\t%d" % gameControlPort

		# Acknowledge
		print "\nSending acknowledge..."
		print "Data:\t" + binascii.hexlify(SERVER_ID)
		print "IP:\t" + gameControlIP
		print "Port:\t%d" % gameControlPort
		sock.sendto(SERVER_ID, (gameControlIP, gameControlPort))

		sleep(2)
		
		# Initiate game
		print "\nInitiating game..."
		print "Data:\t" + binascii.hexlify(SERVER_ID)
		print "IP:\t" + gameControlIP
		print "Port:\t%d" % gameControlPort
		sock.sendto(SERVER_ID, (gameControlIP, gameControlPort))

		print("Shutdown")
		sock.close()
		
	return

if __name__ == "__main__":
	main()
