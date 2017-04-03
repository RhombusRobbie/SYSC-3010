import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class StubServerCom {

	DatagramSocket socket;
	
	public StubServerCom(){
		
		try{
			socket  = new DatagramSocket(2008);
		} catch(SocketException se)		{
			se.printStackTrace();
			System.exit(0);
		}
	}
	
	public void sendReceive()
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
		
		byte[] data = {5,0};
		packet.setData(data);
		packet.setLength(2);
		try{
			socket.send(packet);
		}catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
	
	public static void main(String args[])
	{
		StubServerCom s = new StubServerCom();
		s.sendReceive();
	}
}
