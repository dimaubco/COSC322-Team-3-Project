//package ubc.cosc322;
//
//import java.util.*;
//
//public class playerBrain {
//
//    private GBoard board;
//    private Random rand = new Random();
//    private static final int MAX_DEPTH = 3;
//
//    public playerBrain(GBoard board) {
//        this.board = board;
//    }
//    
//    public playerMove bestMove (int playerId) {
//    	List<playerMove> possibleMoves = actionFactory.generateAllMoves(board, playerId);
//    	if(possibleMoves.isEmpty()) {
//    		System.out.println("I'm out of moves :(");
//    		return null;
//    	}
//    	return possibleMoves.get(rand.nextInt(possibleMoves.size()));
//    }
//}

package ubc.cosc322;

import java.util.*;

public class playerBrain {

    private GBoard board;
    private Random rand = new Random();
    private long TIME_OUT = 30000; // 30 seconds
    private int enemyId;

    public playerBrain(GBoard board) {
        this.board = board;
    }

    public playerMove makeMove(int playerId) {
    	if (playerId == 1) {
    		this.enemyId = 2;
    	} else {
    		this.enemyId = 1;
    	}
    	
        long startTimer = System.currentTimeMillis(); // timer
        
        List<playerMove> allPossibleMoves = actionFactory.generateAllMoves(board, playerId);
        if (allPossibleMoves.isEmpty()) {
            System.out.println("I run out of moves. I can't move anywhere :(");
            return null;
        }
        
        playerMove bestMove = null;
        int minimumEnemyMoves = Integer.MAX_VALUE;

        for (playerMove move : allPossibleMoves) {
            long runTime = System.currentTimeMillis() - startTimer;
            if (runTime > TIME_OUT - 5000) { // 25 seconds max. to perform moves
                if (bestMove == null) {
                    bestMove = allPossibleMoves.get(rand.nextInt(allPossibleMoves.size())); // fallback behavior so that if running out of time, produce random move.
                    System.out.println("Generated random move to avoid timeout");
                }
                break;
            }
            
            GBoard tempBoard = new GBoard(board);
            tempBoard.applyMove(move);
           
            int enemyMovesCount = actionFactory.countPossibleMoves(tempBoard, this.enemyId);

            if (enemyMovesCount < minimumEnemyMoves) {
                minimumEnemyMoves = enemyMovesCount;
                bestMove = move;
            }
        }
        
        return bestMove;
    }
}