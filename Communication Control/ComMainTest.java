import junit.framework.TestCase;


public class ComMainTest extends TestCase{
	
	//this class will test the individual methods of ComMain as well as the system as a whole using stubs for the server and IO.
	
	
	//tests for a valid initialization packet being received. 
	public void testValidInitPacketReceived()
	{
		System.out.println("Test 1: a valid establishContact using a Stub Server\n");
		boolean debug = true;
		ComSendReceive sr = new ComSendReceive(debug);
		StubServerComValidInitialization stubServ = new StubServerComValidInitialization();
		stubServ.start();
		assertTrue("The valid message was not received from the stub server\n",sr.establishConnection());
		
	}
	
	public void testInvalidInitPacketReceived()
	{ 
		System.out.println("Test 2: an invalid establishContact using a Stub Server");
		boolean debug = true;
		ComSendReceive sr = new ComSendReceive(debug);
		StubServerInvalidInitialization stubServ = new StubServerInvalidInitialization();
		stubServ.start();
		assertFalse("The message returned as valid despite supposedly being invalid.\n", sr.establishConnection());
		
	}
	
	public void testGetQuit(){
		
		System.out.println("Test 3: getQuit()");
		boolean debug = true;
		ComSendReceive sr = new ComSendReceive(debug);
		ComIO io = new ComIO();
		ComMain c = new ComMain(io,sr);
		
		assertFalse("quit is defaulted to false in the constructor\n", c.getQuit());
	}
	
	public void testSetQuit(){
		System.out.println("Test 4: setQuit()");
		boolean debug = true;
		ComSendReceive sr = new ComSendReceive(debug);
		ComIO io = new ComIO();
		ComMain c = new ComMain(io,sr);
		c.setQuit(true);
		assertTrue("SetQuit() was not properly executed, quit remained false.\n", c.getQuit());
	}
	
	public void testCreateData()
	{
		System.out.println("Test 5: createData() with valid input");
		boolean debug = true;
		ComSendReceive sr = new ComSendReceive(debug);
		ComIO io = new ComIO();
		ComMain c = new ComMain(io,sr);
		String s = "hi";
		byte[] w = c.createData(s);
		assertTrue("The create data method should append a null character, prepend a 1 byte and turn the array into bytes.\n",w[0] == 1 && w[1] == 'h' && w[2] == 'i' && w[3] == 0);
		
	}
	
	public void testExtractData()
	{
		
	}
	
	

}
