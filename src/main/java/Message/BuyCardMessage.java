package Message;

import java.io.Serializable;

public class BuyCardMessage implements Serializable {

    private final int playerNumber;
    private final String colour;
    private final int level;
    private final int[] quantity;
    private final String[] shelf;

    public BuyCardMessage(String col, int lev, int num, int[] q, String[] s){
        this.colour=col;
        this.level=lev;
        this.playerNumber=num;
        this.quantity=q;
        this.shelf=s;
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
}
