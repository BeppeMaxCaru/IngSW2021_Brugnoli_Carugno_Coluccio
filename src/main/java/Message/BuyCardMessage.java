package Message;

import java.io.Serializable;

public class BuyCardMessage extends Message implements Serializable {

    private final int playerNumber;
    private final int column;
    private final int level;
    private final int[] quantity;
    private final String[] shelf;
    private final int playerboardPosition;

    public BuyCardMessage(int col, int lev, int num, int[] q, String[] s, int playerboardPosition){
        this.column=col;
        this.level=lev;
        this.playerNumber=num;
        this.quantity=q;
        this.shelf=s;
        this.playerboardPosition = playerboardPosition;
    }

    public int getColour() {
        return this.column;
    }

    public int getLevel() {
        return level;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
    public int[] getQuantity() {
        return quantity;
    }
    public String[] getShelf() {
        return shelf;
    }

    public int getPlayerboardPosition() {
        return this.playerboardPosition;
    }
}
