//Simple game logic for simon says, 
//input is taken from python code else where
import java.util.*;
public class SimonSays {

	
	private int gameLength, currentRound;
	private int[] gameState;
	private Random roundGen;
	private boolean gameOver;
	private static int MAX_ROUNDS = 10;
	
	public SimonSays()
	{
		gameLength = 0;
		currentRound = 0;
		gameOver = false;
		//instantiate each item in the array to 4.
		gameState = new int[MAX_ROUNDS];
		for(int i = 0; i<MAX_ROUNDS; i++)
		{
			gameState[i] = 4;
		}		
		
		roundGen = new Random();
	}
	
	public int getGameLength()
	{
		return gameLength;
	}
	
	public boolean getGameOver()
	{
		return gameOver;
	}
	
	public int getCurrentRound()
	{
		return currentRound;
	}
	
	public int getCurrentRoundValue()
	{
		
		return gameState[currentRound];
	}
	
	public int[] getGameState()
	{
		return gameState;
	}
	
	//Creates a new value for the current round between 0 and 3
	//Sets the value of game length in game state to the created value
	//will do nothing after a full game has been generated
	public void createNewRound()
	{
		if(gameLength < MAX_ROUNDS)
		{
			gameState[gameLength] = roundGen.nextInt(4);
			gameLength++;
		}
	}
	
	//the game is the whole ten rounds.
	public boolean gameWon()
	{
		if(gameLength == MAX_ROUNDS && currentRound ==gameLength)
		{
			return true;
		}
		return false;
	}
	 //a round lasts till gamelength, it is made up of stages
	public boolean roundWon()
	{
		if(gameLength == currentRound)
		{
			currentRound = 0;
			return true;
		}
		return false;
	}
	
	//A stage is won when the input is the same as the currentRounds value
	public boolean stageWon(int input)
	{
		if(this.getCurrentRoundValue() == input)
		{
			currentRound++;
			return true;
		}
		gameOver = true;
		return false;
	}
	
	
}
