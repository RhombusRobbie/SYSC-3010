
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.Scanner;

public class Server {

	private final static int PACKETSIZE = 100 ;
	

	
	public static int packetIdentifier(InetAddress simonAddress, InetAddress playerAddress, DatagramPacket p){
		//System.out.println(simonAddress + " " + p.getAddress().toString());
		if(p.getAddress().toString().equals(simonAddress.toString())){
			System.out.println("Simon address Packet!");
			return 1;
		}else if(p.getAddress().toString().equals(playerAddress.toString())){
			System.out.println("Player address Packet!");
			return 0;
		}else{
			System.out.println("Unknow Address Packet");
		}
		return -1;
	}

	public static void udpSender(InetAddress host, int port, DatagramPacket p) throws UnknownHostException{
	      DatagramSocket socket = null ;
	      
	      //InetAddress ip = InetAddress.getByName(host);
	      //System.out.println(ip.toString());
	      
	      try
	      {
	         socket = new DatagramSocket() ;


    		 System.out.println("Passing the packet to: " + host.toString() + " " + port);
    		 DatagramPacket packet = new DatagramPacket( p.getData(), p.getData().length, host, port ) ;
    		 socket.send( packet ) ;

	         System.out.println ("Packet Sent!");
	      }
	      catch( Exception e )
	      {
	         System.out.println( e ) ;
	      }
	      if( socket != null )
	            socket.close() ;
	}
	
	
	// 5 Arguments are: serverPort, Player Address, Player Port, Simon Address, Simon Port. 
	public static void main( String args[] )
	{ 
	      // Check the arguments
	      if( args.length != 4 )
	      {
	         System.out.println( "usage: UDPReceiver port" ) ;
	         return ;
	      }
	      try
	      {
	         // Convert the argument to ensure that is it valid
	         int port = Integer.parseInt( args[0] ) ;
	         InetAddress playerAddress = InetAddress.getByName(args[1]);
	         System.out.println( "Player address:  " + playerAddress ) ;
	         int playerPort = Integer.parseInt( args[2] );
	         System.out.println( "Player port:  " + playerPort ) ;
	         InetAddress simonAddress = InetAddress.getByName(args[3]);
	         System.out.println( "Simon address:  " + simonAddress ) ;
	         int simonPort = 3001;
	         
	         // Construct the socket
	         DatagramSocket socket = new DatagramSocket( port );
	         byte comb[] = new byte[4]; int index = 0;

	         for( ;; )
	         {
		        System.out.println( "Receiving on port " + port ) ;
		        DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;
	            socket.receive( packet ) ;
	            if(simonPort == 0)
	            {
					simonPort = packet.getPort();
					System.out.println( "Simon Port:  " + simonPort ) ;
				}
	            System.out.println( packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()).trim() ) ;
	            if(packetIdentifier(simonAddress, playerAddress, packet) == 1 ){ // 1 is simon
	            	// send the new combination to Player
	            	// add simon's value to array.
	            	comb[index] = packet.getData()[0];
	            	index++;
	            	packet.setData(comb);
	            	packet.setLength(index);
	            	if(index>3) index = 0;
	            	udpSender(playerAddress, playerPort , packet);
	            	
	            }else{ // 0 is player
	            	// send the result to simon
	            	byte newData[]= new byte[1024];
	            	switch (packet.getData()[0]){
	            	case '1':
	            		// you win
	            		System.out.println("Player Won!..");
	            		newData[0] = 1;	
	            		index = 0;
	            		comb = new byte[4];
	            		break;
	            	case '2':
	            		// you lose
	            		System.out.println("Player Lost");
	            		newData[0] = 2;
	            		index = 0;
	            		comb = new byte[4];
	            		break;
	            	case '3':
	            		// get next level
	            		newData[0] = 3;
	            		System.out.println("Player Leveled Up..");
	            		break;
	            		
	            	default:
	            		System.out.println("Something is weird..");
	            	}
	            	// then we send to Simon // 
            		udpSender(simonAddress, simonPort, new DatagramPacket( newData, 1));
            		
	            }
	            
	            
	            
	        }  
	     }
	     catch( Exception e )
	     {
	        System.out.println( e ) ;
	     }
	      

  }
}

