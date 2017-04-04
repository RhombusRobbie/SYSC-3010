import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class StubServerComValidInitialization extends Thread{

	DatagramSocket socket;
	private static byte[] HANDSHAKE = {5,0};
	private static int HANDSHAKE_LENGTH = 2;
	private static int WELL_KNOWN_PORT = 2008;
	
	public StubServerComValidInitialization(){
		
		try{
			socket  = new DatagramSocket(WELL_KNOWN_PORT);
		} catch(SocketException se)		{
			se.printStackTrace();
			System.exit(0);
		}
	}
	
	//A simple thread that waits on a packet on the Servers well known port, sends a response containing a valid 
	public void run()
	{
		byte[] buffer = new byte[200];
		DatagramPacket packet = new DatagramPacket(buffer, 200);
		
		try{
			socket.receive(packet);
		}catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		packet.setData(HANDSHAKE);
		packet.setLength(HANDSHAKE_LENGTH);
		try{
			socket.send(packet);
		}catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		socket.close();
	}
}
