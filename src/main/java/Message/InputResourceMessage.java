package Message;

import java.io.Serializable;

public class InputResourceMessage implements Serializable {

    private final int playerNumber;
    private final char resource;
    private final char quantity;
    private final char store;

    public InputResourceMessage(int playerNumber, char resource, char quantity, char store) {
        this.playerNumber = playerNumber;
        this.resource = resource;
        this.quantity = quantity;
        this.store = store;
    }

    public int getPlayerNumber() { return playerNumber; }
    public char getResource() { return resource; }
    public char getQuantity() { return quantity; }
    public char getStore() { return store; }
}
