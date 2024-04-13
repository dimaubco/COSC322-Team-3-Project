package ubc.cosc322;

import java.util.*;

public class ActionFactory {
	public static List<PlayerMove> generateMoves(int x, int y, GBoard board){
		List<PlayerMove> moves = new ArrayList<>();
		
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
			int newX = x + dir[0]; //move by one horizontal direction
			int newY = y + dir[1]; //move by one vertical direction
			while(board.isInsideBoard(newX, newY) && (board.gboard[newX][newY] == board.blank)){ //while the new position is inside board and the new position is blank
				if (board.isClear(x, y, newX, newY)) { //make sure that board is clear
					List<int[]> arrowShots = generateShots(newX, newY, board); //generate arrow shots
					for (int[] shot : arrowShots) {
						if(board.isClear(newX, newY, shot[0], shot[1])) { //make sure board is clear
							moves.add(new PlayerMove(x, y, newX, newY, shot[0], shot[1]));
						}
					}
				}
				newX += dir[0]; //move by one horizontal direction
				newY += dir[1]; //move by one vertical direction
			}
		}
		
		return moves;
	}
	
	public static List<PlayerMove> generateAllMoves(GBoard board, int playerId){
		List<PlayerMove> possibleMoves = new ArrayList<>();
		for(int i = 1; i<=10; i++) {
    		for(int j = 1; j<=10; j++) {
    			if (board.gboard[i][j] == playerId) {
    				possibleMoves.addAll(ActionFactory.generateMoves(i, j, board)); //add all possible moves
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
	    List<PlayerMove> possibleMoves = generateAllMoves(board, playerId);
	    return possibleMoves.size();
	}
	
}