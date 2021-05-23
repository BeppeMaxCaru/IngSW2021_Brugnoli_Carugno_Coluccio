package Message;

import java.io.Serializable;

public class BuyCardMessage extends Message implements Serializable {

    private final int playerNumber;
    private final String colour;
    private final int level;
    private final int[] quantity;
    private final String[] shelf;
    private int playerboardPosition;

    public BuyCardMessage(String col, int lev, int num, int[] q, String[] s, int playerboardPosition){
        this.colour=col;
        this.level=lev;
        this.playerNumber=num;
        this.quantity=q;
        this.shelf=s;
        this.playerboardPosition = playerboardPosition;
    }

    public String getColour() {
        return colour;
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
