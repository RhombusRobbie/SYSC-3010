import java.io.IOException;
import java.net.*;


public class ComSendReceive {

	
	private int serverPort;
	private DatagramSocket socket;
	private InetAddress serverIP;
	private static int SERVER_DEFAULT = 2008;
	private static int MAX_RETRIES = 4;
	private static int SOCKET_TIMEOUT = 1000;
	private static String SERVER_IP = "10.0.0.1";
	private static byte[] COM_SIGNITURE = {1};	
	
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
		if(host == null)
			{
				serverIP = InetAddress.getLocalHost();
			}else
			{
				serverIP = InetAddress.getByName(host);
			}
		}catch(UnknownHostException uhe)
		{
			uhe.printStackTrace();
			System.exit(0);
		}
	}
	
	//Called at the start of operation by the handler, will shut the system off if the packet is not
	//acknowledged properly.
	public void establishConnection()
	{
		//this byte array functions as the greeting to the server to start establish a link.
			
		DatagramPacket contactPacket = new DatagramPacket(COM_SIGNITURE, COM_SIGNITURE.length, serverIP, serverPort);
		
		send(contactPacket);
		
		//try three times to connect to the server, shutting down if 
		//after three tries packet is still null
		contactPacket = null;
		int i = 0;
		while(contactPacket == null && i < MAX_RETRIES)
		{
			contactPacket = receive();
			i++;
		}
		if(contactPacket == null)
		{
			System.out.println("There was no response, shutting down.");
			System.exit(0);
		}
		this.validatePacket(contactPacket);	
	}
	
	public void send(DatagramPacket packet)
	{
		try
		{
			socket.send(packet);
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		}		
	}
	
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
			ioe.printStackTrace();
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
		//checks for the servers ID which is 1 1 at the start of the packet.
		if(packet.getData()[0] != 5)
		{
			return false;
		}
		//checks if the packet is terminated properly with a null character.
		if(packet.getData()[packet.getLength()-1] != 0)
		{
			return false;
		}
		return true;
	}
}
