import pifacedigitalio
from time import sleep
from array import array
import socket, sys, time

#Create an object of pifacedigital
pfd = pifacedigitalio.PiFaceDigital()

start = input('Are you Ready to Rumble ?!')

# To start the Game. Turn on all the lights 
def startGAme():
    pfd.leds[0].toggle()
    sleep(.1)
    pfd.leds[1].toggle()
    sleep(.1)
    pfd.leds[2].toggle()
    sleep(.1)
    pfd.leds[3].toggle()
    sleep(.1)
    pfd.leds[4].toggle()
    sleep(.1)
    pfd.leds[5].toggle()
    sleep(.1)
    pfd.leds[6].toggle()
    sleep(.1)
    pfd.leds[7].toggle()
    sleep(.1)
    pfd.leds[0].toggle()
    sleep(.1)
    pfd.leds[1].toggle()
    sleep(.1)
    pfd.leds[2].toggle()
    sleep(.1)
    pfd.leds[3].toggle()
    sleep(.1)
    pfd.leds[4].toggle()
    sleep(.1)
    pfd.leds[5].toggle()
    sleep(.1)
    pfd.leds[6].toggle()
    sleep(.1)
    pfd.leds[7].toggle()
    sleep(.1)

    
def checkSwitch():
    while(1):
        if (pfd.input_pins[0].value == 1):
            pfd.leds[0].turn_on()
            return '1'
            
        elif(pfd.input_pins[1].value == 1):
            pfd.leds[1].turn_on()
            pfd.output_pins[1].set_high()
            return '2'
           
        elif(pfd.input_pins[2].value == 1):
            pfd.leds[2].turn_on()
            pfd.output_pins[2].set_high()
            return '3'
            
        elif(pfd.input_pins[3].value == 1):
            pfd.leds[3].turn_on()
            pfd.output_pins[3].set_high()
            return '4'
            

def udpSender(leds, s, server_address):

#s.sendall(data.encode('utf-8'))
    s.sendto(leds.encode(), server_address)
    print('Message Sent')

def udpReceiver(s):
    print ("Waiting to receive on port ")


    buf, address = s.recvfrom(2048)

    #print ("Received %s bytes from %s %s: " % (len(buf), address, buf ))
    return buf[0]

def main():

#To start the Game
    if (start == 'YES'):
        startGAme()

    
    host = sys.argv[1]
    textport = sys.argv[2]


    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    add = ('10.0.0.32', 3001)
    s.bind(add)
    port = int(textport)
    server_address = (host, port)
    while(1):
        pfd.leds[0].turn_off()
        pfd.leds[1].turn_off()
        pfd.leds[2].turn_off()
        pfd.leds[3].turn_off()
        udpSender(checkSwitch(), s, server_address)
        state = udpReceiver(s)
        if(state == 1):
            pfd.leds[0].turn_on()
            pfd.leds[1].turn_on()
            pfd.leds[2].turn_on()
            pfd.leds[3].turn_on()
            sleep(.5)
            pfd.leds[0].turn_off()
            pfd.leds[1].turn_off()
            pfd.leds[2].turn_off()
            pfd.leds[3].turn_off()
            pfd.leds[0].turn_on()
            pfd.leds[1].turn_on()
            pfd.leds[2].turn_on()
            pfd.leds[3].turn_on()
            sleep(.5)
        elif(state == 2):
            pfd.leds[0].turn_on()
            pfd.leds[1].turn_on()
            pfd.leds[2].turn_on()
            pfd.leds[3].turn_on()
            sleep(2)
        
main()

        
