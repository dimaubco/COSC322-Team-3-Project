package ubc.cosc322;

public class playerMoveWithDepth {
    private int initX;
    private int initY;
    private int newX;
    private int newY;
    private int arrowX;
    private int arrowY;
    private int depth;

    public playerMoveWithDepth(int initX, int initY, int newX, int newY, int arrowX, int arrowY, int depth) {
        this.initX = initX;
        this.initY = initY;
        this.newX = newX;
        this.newY = newY;
        this.arrowX = arrowX;
        this.arrowY = arrowY;
        this.depth = depth;
    }

    // Getters
    public int getInitX() {
        return initX;
    }

    public int getInitY() {
        return initY;
    }

    public int getNewX() {
        return newX;
    }

    public int getNewY() {
        return newY;
    }

    public int getArrowX() {
        return arrowX;
    }

    public int getArrowY() {
        return arrowY;
    }
    
    public int getDepth() {
    	return depth;
    }
}
