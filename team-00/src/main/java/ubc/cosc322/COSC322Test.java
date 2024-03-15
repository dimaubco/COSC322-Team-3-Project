
package ubc.cosc322;

import java.util.ArrayList;
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
    
    private int[][] gameBoard = new int[10][10];
    
	
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
//    	List<Room> roomlist = gameClient.getRoomList();
//    	System.out.println("List of rooms: " + roomlist);
//    	String roomtojoin = roomlist.get(1).toString();
//    	System.out.println("Joining room: " + roomtojoin);
//    	gameClient.joinRoom(roomtojoin);
//    	System.out.println("Congratualations!!! "
//    			+ "I am called because the server indicated that the login is successfully");
//    	System.out.println("The next step is to find a room and join it: "
//    			+ "the gameClient instance created in my constructor knows how!"); 
//    	System.out.println();
    	
    	userName = gameClient.getUserName();
    	if(gamegui != null) {
	    	gamegui.setRoomInformation(gameClient.getRoomList());
    	}
    	
    	System.out.println("Congratulation! login to server is succesful.");
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
	        	gamegui.setGameState(gameS);
	        	System.out.println("GameBoard is Set");
	        	break;
	        	
    		case GameMessage.GAME_ACTION_START:
    			String BLACK = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
    			String WHITE = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
    			System.out.println("Black: " + BLACK + "   White: " + WHITE);
    			break;
    			
        	
    		case GameMessage.GAME_ACTION_MOVE:
	    		gamegui.updateGameState(msgDetails);
	    		
	    		
	    		ArrayList<Integer> OponnentQueenCurrPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR); 
	    		ArrayList<Integer> OponnentQueenNextPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
	    		ArrayList<Integer> OponnentArrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
	    		
	    		
	    		
	    		
	    		break;
    	}
    	    	
    	return true;   	
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
