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
    
    
    //---makeMoveWithDepth---
    
    
    public playerMove makeMoveWithDepth(int playerId) {
    	if (playerId == 1) {
    		this.enemyId = 2;
    	} else {
    		this.enemyId = 1;
    	}
    	
        long startTimer = System.currentTimeMillis(); // timer
        
        List<playerMoveWithDepth> allPossibleMoves = actionFactoryWithDepth.generateAllMoves(board, playerId);
        if (allPossibleMoves.isEmpty()) {
            System.out.println("I run out of moves. I can't move anywhere :(");
            return null;
        }
        
        playerMove bestMove = null;
        int minimumEnemyMoves = Integer.MAX_VALUE;

        for (playerMoveWithDepth i : allPossibleMoves) {
            long runTime = System.currentTimeMillis() - startTimer;
            if (runTime > TIME_OUT - 5000) { // 25 seconds max. to perform moves
//                if (bestMove == null) {
//                    bestMove = allPossibleMoves.get(rand.nextInt(allPossibleMoves.size())); // fallback behavior so that if running out of time, produce random move.
//                    System.out.println("Generated random move to avoid timeout");
//                }
                break;
            }
            
            playerMove move = new playerMove(i.getInitX(), i.getInitY(), i.getNewX(), i.getNewY(), i.getArrowX(), i.getArrowY());
            
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
    
    
    //---Minimax with min-distance heuristic---
    
    
    public playerMove miniMax(int playerId) {
    	if (playerId == 1) {
    		this.enemyId = 2;
    	} else {
    		this.enemyId = 1;
    	}
    	
        long startTimer = System.currentTimeMillis(); // timer
        
        List<playerMoveWithDepth> allPossiblePlayerMoves = actionFactoryWithDepth.generateAllMoves(board, playerId);
        List<playerMoveWithDepth> allPossibleEnemyMoves = actionFactoryWithDepth.generateAllMoves(board, enemyId);
        
        ListIterator<playerMoveWithDepth> itrEnemy = allPossibleEnemyMoves.listIterator();
        
        List<playerMoveWithDepth> playerPoints = null;
        List<playerMoveWithDepth> enemyPoints = null;
        List<playerMoveWithDepth> neutralPoints = null;
        int maxDepth = 0;
        
        if (allPossiblePlayerMoves.isEmpty()) {
            System.out.println("I run out of moves. I can't move anywhere :(");
            return null;
        }
        
        playerMove bestMove = null;
        int minimumEnemyMoves = Integer.MAX_VALUE;

        for (playerMoveWithDepth i : allPossiblePlayerMoves) {
            long runTime = System.currentTimeMillis() - startTimer;
            if (runTime > TIME_OUT - 5000) { // 25 seconds max. to perform moves
//                if (bestMove == null) {
//                    bestMove = allPossibleMoves.get(rand.nextInt(allPossibleMoves.size())); // fallback behavior so that if running out of time, produce random move.
//                    System.out.println("Generated random move to avoid timeout");
//                }
                break;
            }
            
            while(itrEnemy.hasNext()) { // See if enemy's move is better
            	playerMoveWithDepth enemy = itrEnemy.next();
            	if (i.getNewX() == enemy.getNewX() && i.getNewY() == enemy.getNewY() && i.getDepth() < enemy.getDepth()) { // player move is closer
            		playerPoints.add(new playerMoveWithDepth(i.getInitX(), i.getInitY(), i.getNewX(), i.getNewY(), i.getArrowX(), i.getArrowY(), i.getDepth()));
            	} else if (i.getNewX() == enemy.getNewX() && i.getNewY() == enemy.getNewY() && i.getDepth() > enemy.getDepth()) { // enemy move is closer
            		enemyPoints.add(new playerMoveWithDepth(enemy.getInitX(), enemy.getInitY(), enemy.getNewX(), enemy.getNewY(), enemy.getArrowX(), enemy.getArrowY(), enemy.getDepth()));
            	} else if (i.getNewX() == enemy.getNewX() && i.getNewY() == enemy.getNewY()) { // point is neutral
            		neutralPoints.add(new playerMoveWithDepth(i.getInitX(), i.getInitY(), i.getNewX(), i.getNewY(), i.getArrowX(), i.getArrowY(), i.getDepth())); 
            	}
            }
            
            maxDepth = i.getDepth();
        }
        
        List<playerMove> playerPointsMaxDepth = null;
        
        for (playerMoveWithDepth j : playerPoints) {
        	if (j.getDepth() == maxDepth) {
        		playerPointsMaxDepth.add(new playerMove(j.getInitX(), j.getInitY(), j.getNewX(), j.getNewY(), j.getArrowX(), j.getArrowY()));
        	}
        }
        
        for (playerMove move : playerPointsMaxDepth) {
            long runTime = System.currentTimeMillis() - startTimer;
            if (runTime > TIME_OUT - 5000) { // 25 seconds max. to perform moves
                if (bestMove == null) {
                    bestMove = playerPointsMaxDepth.get(rand.nextInt(playerPointsMaxDepth.size())); // fallback behavior so that if running out of time, produce random move.
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