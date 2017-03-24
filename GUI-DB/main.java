import java.net.*;
import java.nio.*;
import java.util.*;
import java.net.DatagramPacket;
import java.lang.Object;
import java.lang.System;

public class main
{
    // instance variables - replace the example below with your own
    static mainFrame mf;
    static DatagramSocket socket;
    private final static int PACKETSIZE = 100 ;
	static String[] str;
	static String emotion = "";
	static DB db;
    /**
     * Constructor for objects of class main
     */
    public static void main(String args[])throws Exception
    {
           db = new DB();
           db.createTable();
           mf = new mainFrame();
           Login lgn = new Login(mf);
           
           //-----------------------//
           // wait to receive something
        try{   
           int p = Integer.parseInt(args[0]);
           socket = new DatagramSocket(p);
           
           
        
           for(;;){
               
           System.out.println( "Receiving on port " + p ) ;
           
           DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;
           
           
           socket.receive(packet);
           
           System.out.println( packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()).trim());
           // action ///////////////// ASSUMING PACKET IS [ID, DATA....].
           switch(packet.getData()[0]){
               case 1:
            	// from communicator
            	   
               case 2:
                // from game
            	   
               case 3:
                // from mobile app
            	   
               default:
                //invalid request.
               
            }
           
        }
           
     }catch(Exception e){System.out.println(e); }
           
           
           
           
    }

    public static void packetAnalyzer(DatagramPacket p){ // PACKET, and its PACKET ID
		   byte[] data = new byte[PACKETSIZE];
		   str = new String(data, 1, p.getLength()).split("/"); // ask alex what regex is using.
		   int pid = p.getData()[0] & 0xff; // convert byte to int.
		   System.out.println("Pid is: " + pid);




    }
    
    public static boolean search(DatagramPacket p){
    	byte[] data = new byte[p.getLength()-1];
    	System.arraycopy(p.getData(), 1, data, 0, p.getLength());
    	String str = data.toString();
    	String[] keywords = str.split("/");
    	for(String s: keywords){
    		if((emotion=db.contains(s)) != null){
    			return true;
    			
    		}
    		
    	}
    	
    	
    	return false;
    	
    }
    

}
