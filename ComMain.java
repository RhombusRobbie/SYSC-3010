import java.net.DatagramPacket;

public class ComMain {

	private ComIO ioHandler;
	private ComSendReceive udpHandler;
	public ComMain()
	{
		ioHandler = null;
		udpHandler = null;
		
	}		
	/*create data takes a string given by the user and creates a byte array
	with only words (including apostrophes) and null characters to 
	separate them. It will also prepend the communication controls ID
	and append a final null character*/
	public byte[] createData(String s)
	{
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
		for(holdPos = 0; holdPos<holder.length; holdPos++)
		{
			//65 and 90 are the ascii values of A and Z, 
			//these could all be one if, but for readability I split them up.
			if(holder[holdPos] >= 65 && holder[holdPos] <= 90)
			{
				data[dataPos] = holder[holdPos];
				dataPos++;
			}
			//97 and 122 are the ascii values of a and z
			else if(holder[holdPos] >= 97 && holder[holdPos] <= 122)
			{
				data[dataPos] = holder[holdPos];
				dataPos++;
			}
			else if(holder[holdPos] == 39)
			{
				data[dataPos] = holder[holdPos];
				dataPos++;
			}
			else
			{
				//only one null character separates each word.
				if(data[dataPos-1]!=0)
				{
					data[dataPos] = 0;
					dataPos++;
				}				
			}			
		}	
		
		//Append either one or two null characters to signify the end of the data. 
		if(data[dataPos-1] == 0)
		{
			data[dataPos] = 0;		
			dataPos++;
		}else
		{
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
	}
	
	
/*	Data from server is the servers ID (11) the string it is outputting
	and a terminating null character, the only thing this function
	is interested in is the String, as validation happens elsewhere.  */		
	public String extractData(byte[] data)
	{
		//first find where data's string ends (it's terminated by a null character.)
		for(int i = 2; i<data.length; i++)
		{
			
		}
		byte[] stringArray = new byte[data.length - 3];
		System.arraycopy(data, 2, stringArray, 0, data.length - 3);		
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
	public void talk()
	{
		//this will test whether or not the server is currently online, the program will shut down if not. 
		udpHandler.establishConnection();
		String currentInput, currentOutput;
		DatagramPacket packet;
		
		
		//loops forever, passing the input back to the server and printing it's responses. 
		while(true)
		{
			currentInput = ioHandler.getInput();
			byte[] dataToServer = this.createData(currentInput);
			
			//creates the packet and sends the packet to the server.
			udpHandler.send(udpHandler.createPacket(dataToServer));			
			packet = udpHandler.receive();		
			udpHandler.validatePacket(packet);			
			currentOutput = this.extractData(packet.getData());			
			ioHandler.output(currentOutput);
			
		}
	}
	
	public static void main(String args[])
	{
		ComMain c = new ComMain();
		ComSendReceive sr = new ComSendReceive();
		ComIO io = new ComIO();
		c.setIOHandler(io);
		c.setUDPHandler(sr);
		c.talk();		
	}
}
