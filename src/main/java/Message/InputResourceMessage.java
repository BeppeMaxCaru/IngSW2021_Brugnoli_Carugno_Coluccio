package Message;

import java.io.Serializable;

public class InputResourceMessage implements Serializable {

    private final int playerNumber;
    private final int resource;
    private final int quantity;
    private final char store;

    public InputResourceMessage(int playerNumber, int resource, int quantity, char store) {
        this.playerNumber = playerNumber;
        this.resource = resource;
        this.quantity = quantity;
        this.store = store;
    }

    public int getPlayerNumber() { return playerNumber; }
    public int getResource() { return resource; }
    public int getQuantity() { return quantity; }
    public char getStore() { return store; }
}
