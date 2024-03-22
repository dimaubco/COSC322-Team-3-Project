package ubc.cosc322;

import java.util.*;

public class actionFactoryWithDepth {
	public static List<playerMoveWithDepth> generateMoves(int x, int y, GBoard board){
		List<playerMoveWithDepth> moves = new ArrayList<>();
		List<playerMoveWithDepth> childMoves = new ArrayList<>();
		List<playerMoveWithDepth> tempMoves = new ArrayList<>();
		
		//possible move directions: up, down, left, right, diagonals
		int[][] moveDirections = {
				{0,1}, //Up
				{0,-1}, //Down
				{-1,0}, //Left
				{1,0}, //Right
				{1,1}, //Up-Right Diagonal
				{-1,-1}, //Down-Left Diagonal
				{1,-1}, //Up-Left Diagonal
				{-1,1}}; //Down-Right Diagonal
		
		int depth = 1;
		
		for (int[] dir : moveDirections) { // generate all the moves with depth = 1
			int newX = x + dir[0];
			int newY = y + dir[1];
			while(board.isInsideBoard(newX, newY) && (board.gboard[newX][newY] == board.blank)){
				if (board.isClear(x, y, newX, newY)) {
					List<int[]> arrowShots = generateShots(newX, newY, board);
					for (int[] shot : arrowShots) {
						if(board.isClear(newX, newY, shot[0], shot[1])) {
							moves.add(new playerMoveWithDepth(x, y, newX, newY, shot[0], shot[1], depth));
							childMoves.add(new playerMoveWithDepth(x, y, newX, newY, shot[0], shot[1], depth));
						}
					}
				}
				newX += dir[0];
				newY += dir[1];
			}
		}		
		// generate all child moves
		while(!childMoves.isEmpty()) { // end loop when all possible moves have been generated
			// ListIterator<playerMoveWithDepth> itr = moves.listIterator(); // create iterator for all the moves in the Arraylist
			tempMoves = childMoves; // copy the moves added in the previous depth level
			childMoves.clear();
			depth++; // increase depth
			int add = 1;
			
			for (playerMoveWithDepth move : tempMoves) { // loop through all the moves that were added in the previous depth level
				for (int[] dir : moveDirections) {
					int newX = move.getNewX() + dir[0];
					int newY = move.getNewY() + dir[1];
					while(board.isInsideBoard(newX, newY) && (board.gboard[newX][newY] == board.blank)){
						ListIterator<playerMoveWithDepth> itr = moves.listIterator(); // create iterator for all the moves in the ArrayList
						while(itr.hasNext() && add == 1) { // check that the position is not already present in the move ArrayList
							playerMoveWithDepth check = itr.next();
							if (newX == check.getInitX() && newY == check.getInitY()) {
								add = 0; // the position is already present in the move ArrayList, so don't add this move
							} else if (newX == check.getNewX() && newY == check.getNewY()) {
								add = 0;
							}
						}
						if (board.isClear(move.getNewX(), move.getNewY(), newX, newY) && add == 1) {
							List<int[]> arrowShots = generateShots(newX, newY, board);
							for (int[] shot : arrowShots) {
								if(board.isClear(newX, newY, shot[0], shot[1])) {
									moves.add(new playerMoveWithDepth(move.getNewX(), move.getNewY(), newX, newY, shot[0], shot[1], depth));
									childMoves.add(new playerMoveWithDepth(move.getNewX(), move.getNewY(), newX, newY, shot[0], shot[1], depth));
								}
							}
						}
						newX += dir[0];
						newY += dir[1];
					}
				}
			}
			tempMoves.clear();
		}
			
		return moves;
	}
	
	public static List<playerMoveWithDepth> generateAllMoves(GBoard board, int playerId){
		List<playerMoveWithDepth> possibleMoves = new ArrayList<>();
		List<playerMoveWithDepth> tempMoves = new ArrayList<>();
		ListIterator<playerMoveWithDepth> itr;
		for(int i = 1; i<=10; i++) {
    		for(int j = 1; j<=10; j++) {
    			if (board.gboard[i][j] == playerId) {
    				tempMoves = generateMoves(i, j, board);
    				for (playerMoveWithDepth move : tempMoves) { // check if another queen has a shorter route to the new spot or if the spot has not yet been reached
    					int add = 1; // If add = 1, move will be added to possibleMoves
    					itr = possibleMoves.listIterator();
    					while(itr.hasNext() && add == 1) {
    						playerMoveWithDepth check = itr.next();
    						if(move.getNewX() == check.getNewX() 
    								&& move.getNewY() == check.getNewY() 
    								&& move.getArrowX() == check.getArrowX() 
    								&& move.getArrowY() == check.getArrowY()
    								&& move.getDepth() < check.getDepth()){
    							itr.set(new playerMoveWithDepth(move.getInitX(), move.getInitY(), move.getNewX(), move.getNewY(), move.getArrowX(), move.getArrowY(), move.getDepth()));
    							add = 0; // This move has a shorter route to the spot than the move currently present in possibleMoves, the old move is replaced.
    						} else if(move.getNewX() == check.getNewX() 
    								&& move.getNewY() == check.getNewY() 
    								&& move.getArrowX() == check.getArrowX() 
    								&& move.getArrowY() == check.getArrowY()
    								&& move.getDepth() > check.getDepth()) {
    							add = 0; // There is already a move in possibleMoves with a shorter route to this spot, check next move.
    						}
    					}
    					if (add == 1) { // this spot has not yet been reached, add it to possible moves.
    						possibleMoves.add(new playerMoveWithDepth(move.getInitX(), move.getInitY(), move.getNewX(), move.getNewY(), move.getArrowX(), move.getArrowY(), move.getDepth()));
    					} 
    				}
    			}
    		}
		}
		return possibleMoves;
	}
    					
	
	private static List<int[]> generateShots(int x, int y, GBoard board){
		List<int[]> shots = new ArrayList<>();
		//possible move directions: up, down, left, right, diagonals
		int[][] moveDirections = {
				{0,1}, //Up
				{0,-1}, //Down
				{-1,0}, //Left
				{1,0}, //Right
				{1,1}, //Up-Right Diagonal
				{-1,-1}, //Down-Left Diagonal
				{1,-1}, //Up-Left Diagonal
				{-1,1}}; //Down-Right Diagonal
		
		for (int[] dir : moveDirections) {
			int newX = x + dir[0];
			int newY = y + dir[1];
			while(board.isInsideBoard(newX, newY) && (board.gboard[newX][newY] == board.blank)){
				shots.add(new int[] {newX, newY});
				newX += dir[0];
				newY += dir[1];
			}
		}
		
		return shots;
		
	}
	public static int countPossibleMoves(GBoard board, int playerId) {
	    List<playerMoveWithDepth> possibleMoves = generateAllMoves(board, playerId);
	    return possibleMoves.size();
	}
	
//	public static int[][] generateDepths(GBoard board, int playerId) {
//		
//	}
	
//	public static int minDistanceToPoint(GBoard board, int playerId, int targetX, int targetY) {
//		int depth = 0; // If depth of 0 is returned, target impossible to reach
//		int goal = 0; // goal initially set to 0 (target not reached)
//		List<playerMove> possibleMoves = generateAllMoves(board, playerId);
//	    if ((board.gboard[targetX][targetY] == board.blank) && board.gboard[targetX][targetY] != playerId) {
//	    	while(goal == 0) {
//	    		for(playerMove move : possibleMoves) {
//	    			if ((move.getNewX() == targetX) && (move.getNewY() == targetY)) {
//	    				depth ++;
//	    				goal = 1; // target reached
//	    			}
//	    		}
//	    		if (goal == 0) {
//	    			for(playerMove move : possibleMoves) {
//	    				
//	    				List<playerMove> moves = new ArrayList<>();
//	    				int x = move.getNewX();
//	    				int y = move.getNewY();
//	    				
//	    				//possible move directions: up, down, left, right, diagonals
//	    				int[][] moveDirections = {
//	    						{0,1}, //Up
//	    						{0,-1}, //Down
//	    						{-1,0}, //Left
//	    						{1,0}, //Right
//	    						{1,1}, //Up-Right Diagonal
//	    						{-1,-1}, //Down-Left Diagonal
//	    						{1,-1}, //Up-Left Diagonal
//	    						{-1,1}}; //Down-Right Diagonal
//	    				
//	    				for (int[] dir : moveDirections) {
//	    					int newX = x + dir[0];
//	    					int newY = y + dir[1];
//	    					while(board.isInsideBoard(newX, newY) && (board.gboard[newX][newY] == board.blank)){
//	    						if (board.isClear(x, y, newX, newY)) {
//	    							List<int[]> arrowShots = generateShots(newX, newY, board);
//	    							for (int[] shot : arrowShots) {
//	    								if(board.isClear(newX, newY, shot[0], shot[1])) {
//	    									moves.add(new playerMove(x, y, newX, newY, shot[0], shot[1]));
//	    								}
//	    							}
//	    						}
//	    						newX += dir[0];
//	    						newY += dir[1];
//	    					}
//	    				}
//		    		}
//	    		}
//	    	}
//	    }
//	    return depth;
//	}
	
}