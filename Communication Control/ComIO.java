import java.util.Scanner;


public class ComIO {

	/*This class exists for a couple of reasons, one is to easily allow for stubbing the inputs and outputs
	for testing purposes, the other is for the ability to go to Voice input or to a chatterbot 
	implementation if time permits.*/
	
	Scanner newInput;
	
	public ComIO()
	{
		voce.SpeechInterface.init("../../../lib", true, false, "", "");
		newInput  = new Scanner(System.in);
	}
	
	public String getInput()
	{
		//System.out.println("test");
		String s = newInput.nextLine();
		//System.out.println(s.length());
		return s;
	}
	
	public void output(String s)
	{
		voce.SpeechInterface.synthesize(s);
		System.out.println(s);
	}
	
	public void quit()
	{
		newInput.close();
	}
	
}
