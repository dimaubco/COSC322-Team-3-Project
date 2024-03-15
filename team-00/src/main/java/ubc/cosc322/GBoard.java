package ubc.cosc322;

import java.util.*;

public class GBoard{
	public final int blank = 0;
	public final int white = 1;
	public final int black = 2;
	public final int arrow = 3;
	
	public int[][] gboard;
	
	public GBoard(ArrayList<Integer> gameState) {
		gboard = new int[10][10];
		setGameBoard(gameState);
	}
	
	public boolean applyMove(playerMove move) {
		if (isLegal(move)) {
			gboard[move.getNewX()][move.getNewY()] = gboard[move.getInitX()][move.getInitY()];
			gboard[move.getInitX()][move.getInitY()] = blank;
			gboard[move.getArrowX()][move.getArrowY()] = arrow;
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isLegal(playerMove move) {
		
		if(!isInsideBoard(move.getInitX(), move.getInitY())){ // check if initial position is inside board.
			return false;
		}
		if(!isInsideBoard(move.getNewX(), move.getNewY())){ // check if final position is inside board.
			return false;
		}
		if(!isInsideBoard(move.getArrowX(), move.getArrowY())){ // check if arrow position is inside board.
			return false;
		}
		
		if(move.getInitX() == move.getNewX() && move.getInitY() == move.getNewY()) { // check if initial and final position if the same.
			return false;
		}
		
		return isClear(move.getInitX(), move.getInitY(), move.getNewX(), move.getNewY());
		
	}
	
	private boolean isInsideBoard (int x, int y){
		return x>=0 && x<10 && y>=0 && y<10;
		
	}
	
	private boolean isClear(int initX, int initY, int newX, int newY) {
		int deltaX = newX - initX;
		int deltaY = newY - initY;
		
		if (deltaX == 0 && deltaY == 0) { // INITIAL AND NEW POSITION IS THE SAME
			return false;
		}
		
		if (Math.abs(deltaX)!=Math.abs(deltaY) && Math.abs(deltaX)!=0 && Math.abs(deltaY)!=0) {
			return false;
		}
		
		int stepX = Integer.signum(deltaX); // SIGNUM return -1 for negative and +1 for positive number
		int stepY = Integer.signum(deltaY);
		
		int x = initX + stepX;
		int y = initY + stepY;
		while(x != newX || y != newY) {
			if(gboard[x][y] != blank) {
				return false; // path is not clear
			}
			x += stepX;
			y += stepY;
		}
		
		if (gboard[newX][newY] != blank) { // check if final position is empty.
			return false;
		}
		
		return true;
	}
	
	
	public void setGameBoard(ArrayList<Integer> gameState) {
    	int idx = 0;
    	for (int i = 0; i<10; i++) {
    		for (int j = 0; j<10; j++) {
    			gboard[i][j] = gameState.get(idx++);
    		}
    	}
    }
    
    public void printGameBoard() {
        System.out.println("Game Board:");
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i=0; i<10; i++) {
            System.out.print(i + " ");
            for (int j=0; j<10; j++) {
                System.out.print(gboard[i][j] + " ");
            }
            System.out.println();
        }
    }
	
}