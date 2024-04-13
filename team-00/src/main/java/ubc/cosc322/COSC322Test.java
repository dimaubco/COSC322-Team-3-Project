
package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
    
    private GBoard gameBoard;
    private PlayerBrain brain;
    
    int playerId = 0;
    int playerMoveCounter = 0;
	int searchDepthCount = 1;
    
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	COSC322Test player = new COSC322Test(args[0], args[1]);
    	//HumanPlayer player = new HumanPlayer();
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
    /**
     * Any name and passwd 
     * @param userName
      * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    }

    @Override
    public void onLogin() {
    	System.out.println("Congratulations!!! "
    			+ "I am called because the server indicated that the login is successfully");
    	System.out.println("The next step is to find a room and join it: "
    			+ "the gameClient instance created in my constructor knows how!"); 
    	System.out.println();
    	
    	userName = gameClient.getUserName();
    	if(gamegui != null) {
	    	gamegui.setRoomInformation(gameClient.getRoomList());
    	}
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
	
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
    	System.out.println("Type: " + messageType);
    	System.out.println("Details: " + msgDetails);
    	switch (messageType) {
    		case GameMessage.GAME_STATE_BOARD:
	    		ArrayList<Integer> gameS = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);
	    		gamegui.setGameState(gameS); //set game state
	        	gameBoard = new GBoard(gameS); //create new board
	        	gameBoard.setGameBoard(gameS); // set the game board based on the game state
	        	System.out.println("GameBoard is Set!");
	        	this.brain = new PlayerBrain(this.gameBoard); //create a new player
	        	System.out.println("Brain is Set!");
	        	gameBoard.printGameBoard(); //print gameboard
	        	break;
	        	
    		case GameMessage.GAME_ACTION_START:
    			String BLACK = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK); //store the username of the black player
    			String WHITE = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE); //store the username of the white player
    			System.out.println("Black: " + BLACK + "   White: " + WHITE); 
    			if(BLACK.equals(userName)) { //If black is our username, Make sure this is the username of your program.
    				playerId = 1; //Set our player ID to 1
    				System.out.println("myPlayerId: " + playerId);
    				generateMove(playerId); //generate move for player ID 1
    				playerMoveCounter++; 
    			} else {
    				playerId = 2; //else, set our player ID to 2
    				System.out.println("myPlayerId: " + playerId);
    			}
    			playerMoveCounter = 0;
    			break;
    			
        	
    		case GameMessage.GAME_ACTION_MOVE:
	    		gamegui.updateGameState(msgDetails); //update the game state of our GUI based on the message
	    		ArrayList<Integer> OponnentQueenCurrPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR); // get and store the current position of opponent
	    		ArrayList<Integer> OponnentQueenNextPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT); // get and store the next position of opponent
	    		ArrayList<Integer> OponnentArrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS); // get and store the arrow opponent position
	    		
	    		//extract the position values
	    		int enemyCurrX = OponnentQueenCurrPos.get(0);
	    		int enemyCurrY = OponnentQueenCurrPos.get(1);
	    		int enemyNewX = OponnentQueenNextPos.get(0);
	    		int enemyNewY = OponnentQueenNextPos.get(1);
	    		int enemyArrowX = OponnentArrowPos.get(0);
	    		int enemyArrowY = OponnentArrowPos.get(1);
	    		
	    		//update gameboard
	    		gameBoard.updateGameBoard(enemyCurrX, enemyCurrY, enemyNewX, enemyNewY, enemyArrowX, enemyArrowY);
	    		gameBoard.printGameBoard();
	    		
	    		generateMove(playerId); //with the updated board, generate our move 
	    		playerMoveCounter++;
	    		break;
    	}
    	    	
    	return true;   	
    }
    
    public void generateMove (int playerId) {
    	PlayerMove move = null;
    	long startTimer = System.currentTimeMillis(); //timer to make move
    	move = brain.makeMove(playerId, searchDepthCount); //make move based on the searchDepthCount
    	long runTime = System.currentTimeMillis() - startTimer;
    	System.out.println("Current Search Depth: " + searchDepthCount);
    	System.out.println("Time to generate move: " + (double)runTime/1000 + " seconds.");
    	if (runTime < 3000 && playerMoveCounter > 3) { // if the runTime of the current search depth is below 3 seconds
    		searchDepthCount++; // increase the depth
    	}
    	if (runTime > 25000) { // if the run time is more than 25 seconds, go back to the previous search depth
    		searchDepthCount--;
    	}
    	
    	if (move != null) {
    		ArrayList<Integer> queenCurrentPos = new ArrayList<>(Arrays.asList(move.getInitX(), move.getInitY())); // get queen current position
    		ArrayList<Integer> queenNewPos = new ArrayList<>(Arrays.asList(move.getNewX(), move.getNewY())); // get queen next position
    		ArrayList<Integer> arrowPos = new ArrayList<>(Arrays.asList(move.getArrowX(), move.getArrowY())); //get the arrow position
    		
    		gameClient.sendMoveMessage(queenCurrentPos, queenNewPos, arrowPos); //send the positions to the server
    		gamegui.updateGameState(queenCurrentPos, queenNewPos, arrowPos); //update the game state with our move
    		
    		int myCurrX = queenCurrentPos.get(0);
    		int myCurrY = queenCurrentPos.get(1);
    		int myNewX = queenNewPos.get(0);
    		int myNewY = queenNewPos.get(1);
    		int myArrowX = arrowPos.get(0);
    		int myArrowY = arrowPos.get(1);
    		
    		gameBoard.updateGameBoard(myCurrX, myCurrY, myNewX, myNewY, myArrowX, myArrowY); //update our local gameboard
    		gameBoard.printGameBoard();

    		System.out.println("Move has been sent: QueenInitPos: " + queenCurrentPos + ", QueenFinalPos: " + queenNewPos + ", ArrowPos: " + arrowPos);
    		System.out.println("Player Move Count: " + playerMoveCounter);
    	} else {
    		System.out.println("I run out of moves :(");
    	}
    }
    
    @Override
    public String userName() {
    	return userName;
    }

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub
		return  this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);			
	}

 
}//end of class
