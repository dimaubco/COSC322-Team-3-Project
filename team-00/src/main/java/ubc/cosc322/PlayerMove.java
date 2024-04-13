package ubc.cosc322;

public class PlayerMove {
    private int initX; 
    private int initY;
    private int newX;
    private int newY;
    private int arrowX;
    private int arrowY;

    public PlayerMove(int initX, int initY, int newX, int newY, int arrowX, int arrowY) {
        this.initX = initX;
        this.initY = initY;
        this.newX = newX;
        this.newY = newY;
        this.arrowX = arrowX;
        this.arrowY = arrowY;
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
}
