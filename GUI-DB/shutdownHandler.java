import java.io.IOException;
import java.lang.Thread;
/**
   * Thread created to be a shutdown hook thread. Whenever System.exit() is called it will activate the thread.
   * The thread main job is to export the database to file, to avoid getting data lost during a sudden shutdown of the software.
   */
public class shutdownHandler extends Thread{
	DB db; main m;
	public shutdownHandler(DB db, main m){
		this.db = db;
		this.m = m;
	}
	
	
	public void run(){
		try {
			db.saveTables();
			/*
			 * Sending termination packets to the system.
			 */
			//m.sendPacket("termination", m.getAddress(1), m.getPort(1));
			m.sendBytePacket((byte) 0xff, m.getAddress(2), m.getPort(2) );
			m.sendBytePacket((byte) 0xff, m.getAddress(3), m.getPort(3));
			m.closeSocket();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
