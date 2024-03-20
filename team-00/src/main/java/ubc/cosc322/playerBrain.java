package ubc.cosc322;

import java.util.*;

public class playerBrain {

    private GBoard board;
    private Random rand = new Random();
    private static final int MAX_DEPTH = 3;

    public playerBrain(GBoard board) {
        this.board = board;
    }
    
    public playerMove bestMove (int playerId) {
    	List<playerMove> possibleMoves = actionFactory.generateAllMoves(board, playerId);
    	if(possibleMoves.isEmpty()) {
    		System.out.println("I'm out of moves :(");
    		return null;
    	}
    	return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }
}
