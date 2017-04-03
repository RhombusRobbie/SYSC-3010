import java.io.IOException;
import java.net.*;


public class ComSendReceive {

	
	private int serverPort;
	private DatagramSocket socket;
	private InetAddress serverIP;
	private DatagramPacket lastSentPacket;
	private static int SERVER_DEFAULT = 2008;
	private static int MAX_RETRIES = 4;
	private static int SOCKET_TIMEOUT = 1000;
	private static String SERVER_IP = "10.0.0.1";
	private static byte[] COM_SIGNITURE = {1};	
	private static boolean debug = true;
	
	
	//A default constructor that tests on the same system
	public ComSendReceive()
	{
		
		this(SERVER_DEFAULT, SERVER_IP);	
	}
	
	//Takes the servers port and IP  to instantiate itself.
	public ComSendReceive(int port, String host)
	{
		
		serverPort = port;
		try{
			socket  = new DatagramSocket();
			socket.setSoTimeout(SOCKET_TIMEOUT);
		} catch(SocketException se)		{
			se.printStackTrace();
			System.exit(0);
		}			
		try{
			if(debug)
			{
				serverIP = InetAddress.getLocalHost();
			}else
			{
				serverIP = InetAddress.getByName(host);
			}
		}catch(UnknownHostException uhe)
		{
			if(debug)
			{
				uhe.printStackTrace();
			}
			System.exit(0);
		}
	}
	
	//Called at the start of operation by the handler, will shut the system off if the packet is not
	//acknowledged properly.
	public boolean establishConnection()
	{
		//this byte array functions as the greeting to the server to start establish a link.
			
		DatagramPacket contactPacket = new DatagramPacket(COM_SIGNITURE, COM_SIGNITURE.length, serverIP, serverPort);
		
		send(contactPacket);
		
		//try three times to connect to the server, shutting down if 
		//after three tries packet is still null
		contactPacket = null;
		int i = 0;
		
		//If there is no response re-send the last packet, 
		while(contactPacket == null && i < MAX_RETRIES)
		{
			contactPacket = receive();
			i++;
			this.send(lastSentPacket);
		}
		if(contactPacket == null)
		{
			if(debug)
			{
				System.out.println("There was no response, shutting down.");
			}
			System.exit(0);
		}
		if(debug)
		{
			System.out.println("The packet from the server was received");
		}
		return(this.validatePacket(contactPacket));	
	}
	
	//A function that simply sends the packet provided and sets that packet as the last sent packet. 
	public void send(DatagramPacket packet)
	{
		try
		{
			socket.send(packet);
		} catch (IOException ioe)
		{
			if(debug)
			{
				ioe.printStackTrace();
			}
			System.exit(0);
		}	
		lastSentPacket = packet;
	}
	
	//Creates a datagramPacket using the stored information of serverIP and serverPort along with data.length. 
	//ComMain's create data is explicitly coded such that the final size of the byte array is equal
	//to the contents. 
	public DatagramPacket createPacket(byte[] data)
	{
		return new DatagramPacket(data, data.length, serverIP, serverPort);
	}
	
	//simple receive function that tries to receive for one second before giving up and returning null.
	public DatagramPacket receive()
	{
		byte data[] = new byte[500];
		boolean timedOut = true;
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try{
			socket.receive(packet);
			timedOut = false;
			
		}catch(SocketTimeoutException t)
		{
			
		}catch(IOException ioe)
		{
			if(debug)
			{
				ioe.printStackTrace();
			}
			System.exit(1);
		}
		if(timedOut)
		{
			packet = null;
		}
		return packet;
	}
	
	public boolean validatePacket(DatagramPacket packet)
	{
		//checks for the servers ID which is 5 byte at the start of the packet.
		if(packet.getData()[0] != 5)
		{
			if(debug)
			{
				System.out.println("The packets first byte of data was not the servers ID");
			}
			return false;
		}
		//checks if the packet is terminated properly with a null character.
		if(packet.getData()[packet.getLength()-1] != 0)
		{
			if(debug)
			{
				System.out.println("the packet was not terminated with a null character");
			}
			return false;
		}
		if(debug)
		{
			System.out.println("The packet was of the correct format");
		}
		return true;
	}
	
	
	public DatagramPacket getLastSentPacket()
	{
		return lastSentPacket;
	}
}
