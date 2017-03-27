import java.io.IOException;
import java.net.*;


public class ComSendReceive {

	
	private int serverPort;
	private DatagramSocket socket;
	private InetAddress serverIP;
	private static int SERVER_DEFAULT = 2002;
	
	//A default constructor that tests on the same system
	public ComSendReceive()
	{
		
		this(SERVER_DEFAULT, null);
		
		
	}
	
	//Takes the servers port and IP  to instantiate itself.
	public ComSendReceive(int port, String host)
	{
		
		serverPort = port;		
		try{
			socket  = new DatagramSocket();
			socket.setSoTimeout(1000);
		} catch(SocketException se)		{
			se.printStackTrace();
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
		}
	}
	
	//Called at the start of operation by the handler, will shut the system off if the packet is not
	//acknowledged properly.
	public void establishConnection()
	{
		//this byte array functions as the greeting to the server to start establish a link.
		byte[] signature = new byte[]{0,1,0,0};		
		DatagramPacket contactPacket = new DatagramPacket(signature, signature.length, serverIP, serverPort);
		
		send(contactPacket);
		
		//try three times to connect to the server, shutting down if 
		//after three tries packet is still null
		contactPacket = null;
		int i = 0;
		while(contactPacket == null && i < 3)
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
			
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		if(!timedOut)
		{
			packet = null;
		}
		return packet;
	}
	
	public boolean validatePacket(DatagramPacket packet)
	{
		//checks for the servers ID which is 1 1 at the start of the packet.
		if(packet.getData()[0] != 1 || packet.getData()[1] !=1)
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
