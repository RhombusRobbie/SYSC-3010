package servermain;
import java.lang.*;
import java.net.DatagramPacket;
import java.util.*;
public class main {
	static DatagramPacket packet;
	DB db;
	String emotion = "";
	public static void main(String[] args){
		// inside receive loop.

		
		
		
		//----------------------//
	}
	
	
	public void search(DatagramPacket p){
		byte[] data = new byte[p.getLength()-1];
		System.arraycopy(p.getData(), 1, data, 0, p.getLength());
		String str = data.toString();
		String[] keywords = str.split("/");
		for(String s: keywords){
			if((emotion = db.contains(s)) != null){
				// it contains a word in the database. 
				//1)Store that in GUI textField
				
				
				
				
			}
		}
		
		
		
		
		
	}
	
	
}
