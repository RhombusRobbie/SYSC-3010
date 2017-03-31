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
    private final static int PACKETSIZE = 100 , serverID = 5, serverPort = 2008;
	static String[] str;
	static String emotion = "";
	static DB db;
	static InetAddress appAddress, commAddress, gameAddress, emotionAddress; // taken by input
	static int appPort, commPort, gamePort, emotionPort; // taking by input
	static ChatterBotSession botSession;
	static final String pandorabotAPI = "b0dafd24ee35a477";
	static String message;
	/**
	 * First UDP communication flag. 
	 * each (ID-1) corresponds to specific flag. this used to know if we received our first message from sub-system.
	 * For example, Communicator sends a packet contains only its ID, say 1, so at index 0 is the flag for Communicator.
	 * we get InetAddress, Port of the packet and store it for later, then we set the flag at index 0 to 1 to avoid repeating. 
	 */
	static int[] initialCommunicationFlags = {0, 0, 0, 0}; 
	
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
           
           socket = new DatagramSocket(serverPort);
           
           
        
           for(;;){
               
           System.out.println( "Receiving on port " + serverPort ) ;
           
           DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE ) ;
           
           
           socket.receive(packet);
           
           System.out.println( packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()).trim());
           // action ///////////////// ASSUMING PACKET IS [ID, DATA....].
           switch((packet.getData()[0] & 0xff)){// convert byte id to int id.
               case 1:
            	   // from communicator
            	   System.out.println("Received from: Communicator");
            	   if(initialCommunicationFlags[0] != 1){
            		   // means first time.
            		   commPort = packet.getPort();
            		   commAddress = packet.getAddress();
            		   // flag to 1
            		   initialCommunicationFlags[0] = 1;
            	   }
            	   if(dbSearch(packet)){
            		   // keyword found
            		   // 1) add event to history Database, 2) notify App.
            		   Update("Keyword matched with one in list, he/she feels "+ emotion);
            	   }
            	   
            	   // normal communication sending back random sentence.
            	   // we need to know what emotion should be given based on bot answer.
            	   sendPacket(botSession.think(message), commAddress, commPort);
            	   
            	   
               case 2:
                // from game
            	   System.out.println("Received from: Game");
            	   if(initialCommunicationFlags[1] != 1){
            		   // means first time.
            		   gamePort = packet.getPort();
            		   gameAddress = packet.getAddress();
            		   // flag to 1
            		   initialCommunicationFlags[1] = 1;
            	   }
            	   
            	   
            	   
            	   
            	   
            	   
            	   
            	   
            	   // pass it to Communicator.
               case 3:
                // from mobile app
            	   System.out.println("Received from: App");
            	   if(initialCommunicationFlags[2] != 1){
            		   // means first time.
            		   appPort = packet.getPort();
            		   appAddress = packet.getAddress();
            		   // flag to 1
            		   initialCommunicationFlags[2] = 1;
            	   }
            	   // extracting packet's data.//
            	    byte[] data = new byte[packet.getLength()-1];
               		System.arraycopy(packet.getData(), 1, data, 0, packet.getLength());
               		String[] login = (new String(data)).trim().split(" "); // username at [0], password at [1]
               		//-------------------------//
            	   // login verification.
            	   if(db.contains(login[0], login[1])){
            		   // send back YES message.
            		   sendPacket("YES", appAddress, appPort);
            	   }else{
            		   // send back NO message.
            		   sendPacket("NO", appAddress, appPort);
            	   }
               case 4:
            	   // from emotionController
            	   System.out.println("Received from: EmotionControl");
            	   if(initialCommunicationFlags[3] != 1){
            		   // means first time.
            		   emotionPort = packet.getPort();
            		   emotionAddress = packet.getAddress();
            		   // flag to 1
            		   initialCommunicationFlags[3] = 1;
            	   }
               default:
                //invalid request.
            	   System.out.println("Received from: ERROR.");
            }
           
        }
           
     }catch(Exception e){System.out.println(e); }
           
           
           
           
    }

    /**
     * Method to update mainframe, add the event to Database, and then notifies Mobile App.
     * @throws Exception 
     */
    public static void Update(String event){
    	// first adding it to main frame, then database.
    	mf.update(event);
    	db.setEvent(event);
    	
    	// sending notification to App after here.//
    	sendPacket(event, appAddress, appPort);
    	//----------------------------------------//
    }
    
    
    
    
    /**
     * Packet passed contains keyword per index, this method is to search each keyword with the program's local database.
     * @param p DatagramPacket
     * @return true, if keyword in packet matched with keyword in Database.
     * @return false, otherwise.
     */
    public static boolean dbSearch(DatagramPacket p){
    	int length = p.getLength()-1;
    	byte[] data = new byte[p.getLength()-1];
    	System.arraycopy(p.getData(), 1, data, 0, p.getLength());
    	message = new String(data); // whole sentence with punctuation, to be sent to Bot.
    	String msg = new String(createData(data, length)); // sentence without punctuation, to be converted to list of words for DB search.
    	String[] keywords = msg.split(" ");
    	for(String s: keywords){
    		if((emotion=db.getEmotion(s)) != null){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * takes out punctuation from string. leaves Spaces only between keywords to be used for "searching purposes only"
     * passing: I am very well, thank you.
	 * returns: I am very well thank you
	 * @author Alex
     */
	public static byte[] createData(byte[] originalData, int length){
		int newLength = 0;
		byte[] newData = new byte[length];
		for(int i=0 ; i<length; i++){
			if(originalData[i] >= 65 && originalData[i] <= 90 ){
				newData[newLength++] = originalData[i];
			}else if(originalData[i] >= 97 && originalData[i] <= 122){
				newData[newLength++] = originalData[i];
			}else if(originalData[i] == 32){
				newData[newLength++] = originalData[i];
			}else{
				// do nothing. [ its punctuation ] 
			}	
		}
		byte[] finalData = new byte[newLength];
		for(int j = 0; j<newLength; j++){
			finalData[j] = newData[j];
		}
		return finalData;
		
	}
    
    public static void sendPacket(String e, InetAddress ip, int port){
    	byte[] data = new byte[e.length()+1];
    	data[0] = 5; // id
        byte[] dataHolder = e.getBytes();
        System.arraycopy(dataHolder, 0, data, 1, data.length);
        
        DatagramPacket p = new DatagramPacket(data, 0, data.length, ip, port);
    	try{
    		socket.send(p);
    		
    	}catch(Exception j){
    		System.out.println(j);
    	}
    	
    }
    

}
