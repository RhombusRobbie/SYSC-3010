import java.io.IOException;
import java.lang.Thread;
/**
   * Thread created to be a shutdown hook thread. Whenever System.exit() is called it will activate the thread.
   * The thread main job is to export the database to file, to avoid getting data lost during a sudden shutdown of the software.
   */
public class shutdownHandler extends Thread{
	DB db;
	public shutdownHandler(DB db){
		this.db = db;
	}
	
	
	public void run(){
		try {
			db.saveTables();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
