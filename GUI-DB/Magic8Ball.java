package servermain;
import java.util.*;
import java.util.Random;

public class Magic8Ball
{
	String emotion = "";
	public Magic8Ball(String s){
		emotion = s;

	}
	
	public String getNext ( )
	{
		Random r = new Random();
		String response = "";
		
		
		if(emotion.equalsIgnoreCase("neutral")){
			int choice = 1 + r.nextInt(15);
			switch( choice){
			
			case 1: response = "It is certain";
			case 2: response = "It is decidedly so";
			case 3: response = "Without a doubt";
			case 4: response = "Yes - definitely";
			case 5: response = "You may rely on it";
			case 6: response = "As I see it, yes";
			case 7: response = "Most likely";
			case 8: response = "Outlook good";
			case 9: response = "Signs point to yes";
			case 10: response = "Yes";
			case 11: response = "Reply hazy, try again";
			case 12: response = "Ask again later";
			case 13: response = "Better not tell you now";
			case 14: response = "Cannot predict now";
			case 15: response = "Concentrate and ask again";
			default: response = "8-BALL ERROR!";
			}
		}else if(emotion.equalsIgnoreCase("happy")){
			int choice = 1 + r.nextInt(5);
			switch(choice){
			case 1: response = "";
			case 2: response = "";
			case 3: response = "";
			case 4: response = "";
			case 5: response = "";
			default: response = "8-BALL ERROR!";
			}
		}else if(emotion.equalsIgnoreCase("sad")){
			int choice = 1 + r.nextInt(5);
			switch(choice){
			case 1: response = "";
			case 2: response = "";
			case 3: response = "";
			case 4: response = "";
			case 5: response = "";
			default: response = "8-BALL ERROR!";
			}
		}else{
			// something wrong with 8BALL choice
			System.out.println("Magic8Ball: Error in choice.");
		}
		
		return response;
	}
}
