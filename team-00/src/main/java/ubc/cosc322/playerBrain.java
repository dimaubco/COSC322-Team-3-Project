package ubc.cosc322;

import java.util.*;

public class playerBrain {

    private GBoard board;
    private Random rand = new Random();

    public playerBrain(GBoard board) {
        this.board = board;
    }
    
    public playerMove bestMove (int playerId) {
    	List<playerMove> possibleMoves = new ArrayList<>();
    	for(int i = 1; i<=10; i++) {
    		for(int j = 1; j<=10; j++) {
    			if (board.gboard[i][j] == playerId) {
    				possibleMoves.addAll(actionFactory.generateMoves(i, j, board));
    			}
    		}
    	}
    	if(possibleMoves.isEmpty()) {
    		System.out.println("I'm out of moves :(");
    		return null;
    	}
    	return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }
}
