import junit.framework.*;
public class SimonSaysTest extends TestCase{

	
	//gameLength is set to 0 to start with.
	public void testGetGameLength()
	{
		SimonSays game = new SimonSays();
		assertTrue(game.getGameLength() == 0);
	}
	
	//current round is set to 0 to start with
	public void testGetCurrentRound()
	{
		SimonSays game = new SimonSays();
		assertTrue(game.getCurrentRound() == 0);
	}
	
	//current Round is 0, the value is default set to 4.
	public void testGetCurrentRoundValue()
	{
		SimonSays game = new SimonSays();
		assertTrue(game.getCurrentRoundValue() == 4);
	}
	
	public void testgetGameOverFalse()
	{
		SimonSays game = new SimonSays();
		assertFalse(game.getGameOver());
	}
	
	public void testGetGameOverTrue()
	{
		SimonSays game = new SimonSays();
		game.createNewRound();
		game.stageWon(4);
		assertTrue(game.getGameOver());
	}
	
	
	public void testGetGameState()
	{
		SimonSays game = new SimonSays();
		for(int i = 0; i<10 ; i++)
		{
			if(game.getGameState()[i] != 4)
			{
				assert(false);
			}			
		}
		assert(true);
	}
	
	//This test generates a new round, thus increasing the gameLength and tests
	public void testCreateNewRound()
	{
		SimonSays game = new SimonSays();
		game.createNewRound();
		assertTrue( 0 <= game.getCurrentRoundValue() && game.getCurrentRoundValue() <= 3);
	}
	
	public void testStageWonSuccess()
	{
		SimonSays game = new SimonSays();
		game.createNewRound();
		int input = game.getCurrentRoundValue();
		assertTrue(game.stageWon(input));
		
	}
	
	public void testStageWonFailure()
	{
		SimonSays game = new SimonSays();
		game.createNewRound();
		assertFalse(game.stageWon(4));
	}
	
	//RoundWon is successful when gamelength is equal to currentRound
	public void testRoundWonSuccess()
	{
		SimonSays game = new SimonSays();
		assertTrue(game.roundWon());
	}
	
	public void testRoundWonFailure()
	{
		SimonSays game = new SimonSays();
		game.createNewRound();
		assertFalse(game.roundWon());
	
	}
	
	public void testGameWonFalse()
	{
		SimonSays game = new SimonSays();
		assertFalse(game.gameWon());
	}
	
	//Game won will only be true after successfully compleating 10 rounds. 
	public void testGameWonTrue()
	{
		int input;
		SimonSays game = new SimonSays();
		for(int i = 0; i<10; i++)
		{
			game.createNewRound();
			input = game.getCurrentRoundValue();
			if(!game.stageWon(input))
			{
				assert(false);
			}
		}
		assertTrue(game.gameWon());
	}
}
