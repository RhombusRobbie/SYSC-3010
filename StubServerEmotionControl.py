import socket
from random import randint
from time import sleep

UDP_IP = "127.0.0.1"
UDP_PORT = 5005

NEUTRAL = bytearray([0, 1, 0, 1])
HAPPY = bytearray([0, 1, 0, 2])
SAD = bytearray([0, 1, 0, 3])
ANGRY = bytearray([0, 1, 0, 4])
SHUTDOWN = bytearray([0, 1, 0, 9])

randomTest = False

print ("UDP target IP:", UDP_IP)
print ("UDP target port:", UDP_PORT)

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

if randomTest:
	for i in range(10):
		rand = randint(1, 4)
		if rand == 1:
			sock.sendto(NEUTRAL, (UDP_IP, UDP_PORT))
			print("Neutral")
		elif rand == 2:
			sock.sendto(HAPPY, (UDP_IP, UDP_PORT))
			print("Happy")
		elif rand == 3:
			sock.sendto(SAD, (UDP_IP, UDP_PORT))
			print("Sad")
		elif rand == 4:
			sock.sendto(ANGRY, (UDP_IP, UDP_PORT))
			print("Angry")
			
		sleep(3)

else:
	sock.sendto(SAD, (UDP_IP, UDP_PORT))
	print("Sad")
	sleep(3)
	sock.sendto(HAPPY, (UDP_IP, UDP_PORT))
	print("Happy")
	sleep(3)
	sock.sendto(NEUTRAL, (UDP_IP, UDP_PORT))
	print("Neutral")
	sleep(3)
	sock.sendto(ANGRY, (UDP_IP, UDP_PORT))
	print("Angry")
	sleep(3)
	sock.sendto(NEUTRAL, (UDP_IP, UDP_PORT))
	print("Neutral")
	sleep(3)
	sock.sendto(HAPPY, (UDP_IP, UDP_PORT))
	print("Happy")
	sleep(3)
	sock.sendto(SAD, (UDP_IP, UDP_PORT))
	print("Sad")
	sleep(3)
	sock.sendto(SAD, (UDP_IP, UDP_PORT))
	print("Sad")
	sleep(3)

print("Shutdown")
sock.sendto(SHUTDOWN, (UDP_IP, UDP_PORT))
