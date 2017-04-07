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
    private final static int PACKETSIZE = 100 , serverPort = 2008;
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
	public main(){
        db = new DB();
        mf = new mainFrame();
        Login lgn = new Login(mf,db);
        shutdownHandler sdh = new shutdownHandler(db, this);
		Runtime.getRuntime().addShutdownHook(sdh);
		
        //------------ Bot Initialize --------//
        ChatterBotFactory factory = new ChatterBotFactory();
        ChatterBot bot = null;
		try {
			bot = factory.create(ChatterBotType.PANDORABOTS, pandorabotAPI);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        botSession = bot.createSession();
        //-----------------------------------//
		
	}
    /**
     * Constructor for objects of class main
     */
    public static void main(String args[])throws Exception
    {
    	main m = new main();
        int[] initialCommunicationFlags = {0, 0, 0, 0}; 
           
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
				   String triggeredKeyword = "";
            	   System.out.println("Received from: Communicator: "+ packet.getData());
            	   if(initialCommunicationFlags[0] != 1){
            		   // means first time.
            		   commPort = packet.getPort();
            		   commAddress = packet.getAddress();
            		   // flag to 1
            		   initialCommunicationFlags[0] = 1;
					   // sends acknowledge.
					   sendPacket("", commAddress, commPort);
					   break;  
            	   }else if((triggeredKeyword=dbSearch(packet))!=null){
            		   // keyword found
            		   // 1) add event to history Database, 2) notify App.
            		 
            			  Update(triggeredKeyword+ " has been said, the child feels "+ emotion + "The original sentence is: " + message);
            		   
            	   }else if(message.contains("game") && message.contains("play")){
            		   // play game.
            		   // send activation packet.
            		   if(gameAddress!=null){
            			   
            		   
            		   sendPacket("", gameAddress, gamePort); 
            		   }
					if(emotionAddress!=null){
						sendBytePacket((byte)1, emotionAddress, emotionPort);
					}
            	   }
                   // normal communication sending back random sentence.            		   
					sendPacket(botSession.think(message), commAddress, commPort);
					if(emotionAddress!=null){
						sendBytePacket(emotionIdentifier(), emotionAddress, emotionPort);
					}
            	   break;
               case 3:
                // from game
            	   System.out.println("Received from: Game");
            	   if(initialCommunicationFlags[2] != 1){
            		   // means first time.
            		   gamePort = packet.getPort();
            		   gameAddress = packet.getAddress();
            		   // flag to 1
            		   initialCommunicationFlags[2] = 1;
					   //sending ack
					   sendPacket("", gameAddress, gamePort);
            	   }
            	   break;
               case 4:
                // from mobile app
            	   System.out.println("Received from: App");

            	   // extracting packet's data.//
            	    byte[] data = new byte[packet.getLength()-2];
               		System.arraycopy(packet.getData(), 1, data, 0, packet.getLength()-2);
               		String[] login = (new String(data)).trim().split(" "); // username at [0], password at [1]
               		System.out.println(login[0]);
               		System.out.println(login[1]);
               		//-------------------------//
               		appAddress = packet.getAddress();
               		appPort = packet.getPort();
            	   // login verification.
            	   if(db.contains(login[0], login[1])){
            		   // send back YES message.
            		   sendBytePacket((byte) 1, appAddress, appPort);
            		   System.out.println("Sent YES to app");
            	   }else{
            		   // send back NO message.
            		   sendBytePacket((byte) 0 , appAddress, appPort);
            		   System.out.println("Sent YES to app");
            	   }
            	   break;
               case 2:
            	   // from emotionController
            	   System.out.println("Received from: EmotionControl");
            	   if(initialCommunicationFlags[1] != 1){
            		   // means first time.
            		   emotionPort = packet.getPort();
            		   emotionAddress = packet.getAddress();
            		   // flag to 1
            		   initialCommunicationFlags[1] = 1;
					   //sending ack
					   sendBytePacket((byte)5, emotionAddress, emotionPort);
            	   }
            	   break;
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
    	if(emotionAddress!=null){
			sendBytePacket(emotionIdentifier(), emotionAddress, emotionPort);
		}
    	// sending notification to App after here.//
    	if(appAddress!=null){
    	    sendPacket(event, appAddress, appPort);
    	}
    	//----------------------------------------//
    }
    		/**
		 * Neutral - byte 0
		 * Happy - byte 1
		 * Sad - byte 2
		 * Angry - byte 3
		 * ShutdownCode 0xFF, used in shutdownHandler.class
		 * */
    public static byte emotionIdentifier(){
		switch(emotion){
			case "happy":
				emotion="neutral";
				return 1;
			case "sad":
				emotion="neutral";
				return 2;
			case "neutral":
				emotion="neutral";
				return 0;
			case "angry":
				emotion="neutral";
				return 3;
			default:
				emotion="neutral";
				return 0;// neutral
		}
		
	}
    
    
    /**
     * Packet passed contains keyword per index, this method is to search each keyword with the program's local database.
     * @param p DatagramPacket
     * @return triggeredKeyword, if keyword in packet matched with keyword in Database.
     * @return null, otherwise.
     */
    public static String dbSearch(DatagramPacket p){
    	int length = p.getLength()-2;
    	byte[] data = new byte[p.getLength()-2];
    	System.arraycopy(p.getData(), 1, data, 0, p.getLength()-2);
    	message = new String(data); // whole sentence with punctuation, to be sent to Bot.
    	String msg = new String(createData(data, length)); // sentence without punctuation, to be converted to list of words for DB search.
    	String[] keywords = msg.split(" ");
    	for(String s: keywords){
    		emotion=db.getEmotion(s);
    		if(emotion != null && !emotion.equals("happy")){
    			return s;
    		}   
    	}
    	return null;
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
    // for both the App and Emotion Controller.
	// sendBytePacket
    public static void sendBytePacket(byte byteValue, InetAddress ip, int port){
		/**
		 * Neutral - byte 0
		 * Happy - byte 1
		 * Sad - byte 2
		 * Angry - byte 3
		 * ShutdownCode 0xFF
		 * */
		 byte[] dataholder = new byte[2];
		 dataholder[0] = 5;
		 dataholder[1] = byteValue;
		 DatagramPacket p = new DatagramPacket(dataholder, 0, dataholder.length, ip, port);
		 try{
			 System.out.println("Sending to:"+ p.getAddress() + " " + p.getPort() + ": " + new String(p.getData()).trim());
			 socket.send(p);
		 }catch(Exception j){
			 j.printStackTrace();
		 }
		 
	}
    public static void sendPacket(String e, InetAddress ip, int port){
    	byte[] dataholder = new byte[2+e.length()];
    	dataholder[0] = 5;
        System.arraycopy(e.getBytes(), 0, dataholder, 1, e.length());
        dataholder[dataholder.length - 1] = 0;
        DatagramPacket p = new DatagramPacket(dataholder, 0, dataholder.length, ip, port);
    	try{
    		System.out.println("Sending to:"+ p.getAddress() + " " + p.getPort() + ": " + new String(p.getData()).trim());
    		socket.send(p);
    		
    	}catch(Exception j){
    		System.out.println(j);
    	}
    	
    }
    /**
     * Used by shutdownHandler, to retrieve IP's and Ports of Communicator, EmotionControl and Game
     * @param id
     */
    public InetAddress getAddress(int id){
    	switch(id){
    	case 1:
    		return commAddress;
    	case 2:
    		return emotionAddress;
    	case 3:
    		return gameAddress;
    	}
		return null;
    }
    public int getPort(int id){
    	switch(id){
    	case 1:
    		return commPort;
    	case 2:
    		return emotionPort;
    	case 3:
    		return gamePort;
    	}
		return 0;
    }
    
    public void closeSocket(){
    	socket.close();
    }
    

}
