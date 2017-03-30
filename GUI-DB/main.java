import java.net.*;
import java.nio.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.net.DatagramPacket;
import java.lang.Object;
import java.lang.System;
import java.net.InetAddress;
import com.google.code.chatterbotapi.*;


public class main
{
    // instance variables - replace the example below with your own
    static mainFrame mf;
    static DatagramSocket socket;
    private final static int PACKETSIZE = 100 ;
	static String[] str;
	static String emotion = "";
	static DB db;
	static InetAddress appAddress, commAddress; // taken by input
	static int appPort, commPort; // taking by input
	static ChatterBotSession botSession;
	static final String pandorabotAPI = "b0dafd24ee35a477";
	static String msg;
	
    /**
     * Constructor for objects of class main
     */
    public static void main(String args[])throws Exception
    {
           db = new DB();
           mf = new mainFrame();
           Login lgn = new Login(mf,db);
           
           //------------ Bot Initialize --------//
           ChatterBotFactory factory = new ChatterBotFactory();
           ChatterBot bot = factory.create(ChatterBotType.PANDORABOTS, pandorabotAPI);
           botSession = bot.createSession();
           //-----------------------------------//
           
           
           
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
           switch((packet.getData()[0] & 0xff)){// convert byte id to int id.
               case 1:
            	// from communicator
            	   System.out.println("Received from: Communicator");
            	   if(dbSearch(packet)){
            		   // keyword found
            		   // 1) add event to history Database, 2) notify App.
            		   Update("Keyword matched with one in list, he/she feels "+ emotion);
            	   }
            	   
            	   // normal communication sending back random sentence.
            	   sendPacket(botSession.think(msg), commAddress, commPort);
            	   
            	   
               case 2:
                // from game
            	   System.out.println("Received from: Game");
            	   // pass it to Communicator.
               case 3:
                // from mobile app
            	   System.out.println("Received from: App");
            	   /** TODO:
            	    * 1) login verification.
            	    * 2) access to DB history log.
            	    */
               default:
                //invalid request.
            	   System.out.println("Received from: ERROR.");
            }
           
        }
           
     }catch(Exception e){System.out.println(e); }
           
           
           
           
    }

    /**
     * Method to Add events to Database, and then notifies Mobile App.
     * @throws Exception 
     */
    public static void Update(String event){
    	// first adding it to main frame, then database.
    	mf.update(event);
    	db.setEvent(event);
    	
    	// sending notification to App after here.//
        
    	//----------------------------------------//
    }
    
    
    
    
    /**
     * Packet passed contains keyword per index, this method is to search each keyword with the program's local database.
     * @param p DatagramPacket
     * @return true, if keyword in packet matched with keyword in Database.
     * @return false, otherwise.
     */
    public static boolean dbSearch(DatagramPacket p){
    	byte[] data = new byte[p.getLength()-1];
    	System.arraycopy(p.getData(), 1, data, 0, p.getLength());
    	msg = data.toString();
    	String[] keywords = msg.split("/"); // ask alex what regex is using.
    	for(String s: keywords){
    		if((emotion=db.getEmotion(s)) != null){
    			return true;
    		}
    	}
    	return false;
    }
    
    public static void sendPacket(String e, InetAddress ip, int port){
        byte[] data = e.getBytes();
        DatagramPacket p = new DatagramPacket(data, 0, data.length, ip, port);
    	try{
    		socket.send(p);
    		
    	}catch(Exception j){
    		System.out.println(j);
    	}
    	
    }
    

}
