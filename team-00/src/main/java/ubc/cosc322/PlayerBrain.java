package ubc.cosc322;

import java.util.*;

public class PlayerBrain {

    private GBoard board;
    private int enemyId;
    private int playerId;
    private long TIME_OUT = 30000; // 30 seconds

    public PlayerBrain(GBoard board) {
        this.board = board;
    }

    private int evaluateBoard(GBoard board, int playerId) {
        if (playerId == 1) { 
        	enemyId = 2; // if player is black, then enemy is white
        } else {
        	enemyId = 1; // else, enemy is black
        }
        int playerPossibleMoves = ActionFactory.countPossibleMoves(board, playerId); // count all possible player moves
        int enemyPossibleMoves = ActionFactory.countPossibleMoves(board, enemyId); // count all possible enemy moves
        return playerPossibleMoves - enemyPossibleMoves; // calculate the score = count possible player moves - count possible enemy moves
    }

    private int minimax(GBoard board, int depthCount, boolean isMaximizing, int playerId, int alpha, int beta) {
        if (playerId == 1) { 
        	enemyId = 2; // if player is black, then enemy is white
        } else {
        	enemyId = 1; // else, enemy is black
        }

        if (depthCount == 0 || board.isGameOver()) {
            return evaluateBoard(board, playerId); // if depth count down reaches to 0, execute heuristic function.
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE; // negative infinity
            List<PlayerMove> allPossibleMoves = ActionFactory.generateAllMoves(board, playerId); //generate all possible moves
            for (PlayerMove move : allPossibleMoves) { // for each move in all possible moves
                GBoard tempBoard = new GBoard(board); // create a new temporary board
                tempBoard.applyMove(move); // apply the move in the temporary board
                int eval = minimax(tempBoard, depthCount - 1, false, playerId, alpha, beta); // call the minimax but with minimizing this time, go to the next depth (depth count - 1)
                maxEval = Math.max(maxEval, eval); // maximum score between current maxEval and the new eval
                alpha = Math.max(alpha, eval); // alpha beta pruning
                if(beta <= alpha){
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            List<PlayerMove> allPossibleMoves = ActionFactory.generateAllMoves(board, enemyId); //generate all possible enemy moves
            for (PlayerMove move : allPossibleMoves) { // for each move in all possible moves
                GBoard tempBoard = new GBoard(board); // create a new temporary board
                tempBoard.applyMove(move); // apply the move in the temporary board
                int eval = minimax(tempBoard, depthCount - 1, true, playerId, alpha, beta); // call the minimax but with maximizing this time, go to the next depth (depth count - 1)
                minEval = Math.min(minEval, eval); // minimum score between current minEval and the new eval
                beta = Math.min(beta, eval); // alpha beta pruning
                if(beta >= alpha){
                    break;
                }
            }
            return minEval;
        }
    }

    public PlayerMove makeMove(int playerId, int searchDepth) {
    	long startTimer = System.currentTimeMillis(); // timer
        this.playerId = playerId;
        if (this.playerId == 1) { 
        	this.enemyId = 2; // if player is black, then enemy is white
        } else {
        	this.enemyId = 1; // else, enemy is black
        }
        List<PlayerMove> allPossibleMoves = ActionFactory.generateAllMoves(board, playerId); // generate all possible moves of player
        PlayerMove bestMove = null; 
        int bestScore = Integer.MIN_VALUE; // score

        for (PlayerMove move : allPossibleMoves) { // for each move in all possible player moves
            GBoard tempBoard = new GBoard(board); // create a temporary board
            tempBoard.applyMove(move); // apply the move to the temporary board
            int moveScore = minimax(tempBoard, searchDepth - 1, false, playerId, Integer.MIN_VALUE, Integer.MAX_VALUE); // use minimax to go to the next depth
            if (moveScore > bestScore) { //if the moveScore is bigger than bestScore, replace the bestScore and the bestMove.
                bestScore = moveScore;
                bestMove = move;
            }
            long runTime = System.currentTimeMillis() - startTimer;
            if (runTime > TIME_OUT - 4000) { // 26 seconds max. to perform moves
            	return bestMove;
            }
        }
        return bestMove;
    }
}