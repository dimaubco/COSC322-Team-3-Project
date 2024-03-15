package ubc.cosc322;

import java.util.*;

public class actionFactory {
	public static List<playerMove> generateMoves(int x, int y, GBoard board){
		List<playerMove> moves = new ArrayList<>();
		
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
				if (board.isClear(x, y, newX, newY)) {
					List<int[]> arrowShots = generateShots(newX, newY, board);
					for (int[] shot : arrowShots) {
						if(board.isClear(newX, newY, shot[0], shot[1])) {
							moves.add(new playerMove(x, y, newX, newY, shot[0], shot[1]));
						}
					}
				}
				newX += dir[0];
				newY += dir[1];
			}
		}
		
		return moves;
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
	
}