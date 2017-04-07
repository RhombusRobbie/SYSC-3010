import java.net.DatagramPacket;

public class ComMain {

	private ComIO ioHandler;
	private ComSendReceive udpHandler;
	private boolean quit;
	private static byte COM_ID = 1;
	private static byte NULL_BYTE = 0;
	private static int PACKET_FORMATING_SIZE = 2;
	public ComMain(ComIO cIO, ComSendReceive cSR)
	{
		ioHandler = cIO;
		udpHandler = cSR;
		quit = false;
		
	}		
	
	
	/*This method has been deprecated due to changes in our communication protocol.
	 *it now appears on the server as a means to prepare the input string for splitting.
	 *This is done to aid searching our database for keywords. 
	 * 
	 * create data takes a string given by the user and creates a byte array
	with only words (including apostrophes) and null characters to 
	separate them. It will also prepend the communication controls ID
	and append a final null character*/
	/*public byte[] createData(String s){
		//To extract all punctuation in order to help the Database on the server
		//this will iterate through holder, placing all acceptable characters in 
		//data, and replacing each group of unacceptable characters with one null character.
		byte[] holder = s.getBytes();
		byte[] data = new byte[200];
		//Start by setting the start of the array to Communication controller's ID
		data[0] = 0; 
		data[1] = 1;
		int holdPos;
		int dataPos = 2;
		for(holdPos = 0; holdPos<holder.length; holdPos++){
			//65 and 90 are the ascii values of A and Z, 
			//these could all be one if, but for readability I split them up.
			if(holder[holdPos] >= 65 && holder[holdPos] <= 90){
				data[dataPos] = holder[holdPos];
				dataPos++;
			}
			//97 and 122 are the ascii values of a and z
			else if(holder[holdPos] >= 97 && holder[holdPos] <= 122){
				data[dataPos] = holder[holdPos];
				dataPos++;
			}
			//39 is the value of an apostrophe in ascii.
			else if(holder[holdPos] == 39){
				data[dataPos] = holder[holdPos];
				dataPos++;
			}
			else{
				//only one null character separates each word.
				if(data[dataPos-1]!=0){
					data[dataPos] = 0;
					dataPos++;
				}				
			}			
		}			
		//Append One null character if the current ending character is a null,
		//Append two null characters if the current ending character is not a null.
		if(data[dataPos-1] == 0){
			data[dataPos] = 0;		
			dataPos++;
		}else{
			data[dataPos] = 0;
			data[dataPos+1] = 0;
			dataPos+=2;
		}		
		//finalData will be exactly the size of the string with its null characters
			byte[] finalData = new byte[dataPos];
			System.arraycopy(data, 0, finalData, 0, dataPos);		
			for (int j=0;j<finalData.length;j++) {
				 System.out.println("byte " + j + " " + finalData[j]);
			}
			String d = new String(finalData);
			System.out.println(d);		
		return finalData;
	}*/
	
	
	/*Creates a byte array with the format 	
		1"user input string"0
	1 is the communicators ID on the server and the terminating null character is for parsing.*/
	public byte[] createData(String s)
	{
		//We want the byte array to be the exact size 
		byte[] data = new byte[s.length() + PACKET_FORMATING_SIZE];
		data[0] = COM_ID;		
		//This arraycopy turns the string into bytes, reads from the 0th position
		//and starts storing it in the first position. 
		System.arraycopy(s.getBytes(), 0, data, 1, s.length());
		//Terminate the byte array with a null character byte.
		data[data.length - 1] = NULL_BYTE;
		return data;
		
	}
	
	
/*	Data from server is the servers ID (5) the string it is outputting
	and a terminating null character, the only thing this function
	is interested in is the String, as validation happens elsewhere.  */		
	public String extractData(DatagramPacket packet)
	{
		//The data has two extra bytes, one for the servers ID and one for the terminating null character, 
		//Those two are stripped out and then the resulting array is turned into a string. 
		byte[] stringArray = new byte[packet.getLength() - PACKET_FORMATING_SIZE];	
		System.arraycopy(packet.getData(), 1, stringArray, 0, packet.getLength() - PACKET_FORMATING_SIZE);		
		String s = new String(stringArray);	
		return s;
	}

	//simple set methods for the two tool classes. 
	public void setIOHandler(ComIO handler)
	{
		ioHandler = handler;
	}

	public void setUDPHandler(ComSendReceive handler)
	{
		udpHandler = handler;
	}
	
	//this method handles the general function of the communicator sub-system 
	//Verify that the server is up and we can connect to it, 
	//then enter the input send receive output loop.
	
	public void talk()
	{
		//this will test whether or not the server is currently online, the program will shut down if false is returned. 
		if(!udpHandler.establishConnection())
		{
			ioHandler.quit();
			udpHandler.quit();
			System.exit(0);
		}
		String currentInput, currentOutput;
		DatagramPacket packet;	
		
		//loops until a quit request is received, passing the input back to the server and printing it's responses. 
		while(!quit)
		{
			currentInput = ioHandler.getInput();
			if(currentInput.equalsIgnoreCase("quit")) 
				{
					System.out.println("ShuttingDown");
					break;
				}
			
			byte[] dataToServer = this.createData(currentInput);
			
			//creates the packet and sends the packet to the server.
			udpHandler.send(udpHandler.createPacket(dataToServer));			
			packet = udpHandler.receive();
			if(packet!=null)
			{	
				udpHandler.validatePacket(packet);			
				currentOutput = this.extractData(packet);			
				ioHandler.output(currentOutput);
			}
			
		}
		//close the socket and the scanner.
		ioHandler.quit();
		udpHandler.quit();		
	}
	
	//Changes the boolean value of quit that controls the main loop.
	public void setQuit(boolean stop)
	{
		quit = stop;		
	}
	
	public boolean getQuit()
	{
		return quit;
	}
	
	
	public static void main(String args[])
	{
		boolean debug = false;
		ComSendReceive sr = new ComSendReceive(debug);
		ComIO io = new ComIO();
		ComMain c = new ComMain(io, sr);
		sr.setComHandler(c);
		/*
		System.out.println(new String(w));
		DatagramPacket f = new DatagramPacket(w, w.length);
		System.out.println(c.extractData(f));*/
		
		c.talk();
	}
	
	
}
