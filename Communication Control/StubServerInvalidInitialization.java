import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class StubServerInvalidInitialization extends Thread
{
	DatagramSocket socket;
	private static byte[] INVALID_HANDSHAKE = {5,9};
	private static int HANDSHAKE_LENGTH = 2;
	private static int WELL_KNOWN_PORT = 2008;
	
	public StubServerInvalidInitialization()
	{		
		try{
			socket  = new DatagramSocket(WELL_KNOWN_PORT);
		} catch(SocketException se)		{
			se.printStackTrace();
			System.exit(0);
		}
	}
	
	//This thread acts as if the server has already had a communicator sub system assigned to it
	//it waits for a packet and sends the invalid handshake back.
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
		
		
		packet.setData(INVALID_HANDSHAKE);
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