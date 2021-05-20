package Message;

import java.io.Serializable;

public class OutputChoiceResourceMessage implements Serializable {

    private final int playerNumber;
    private final int resource;

    public OutputChoiceResourceMessage(int playerNumber, int resource) {
        this.playerNumber = playerNumber;
        this.resource = resource;
    }

    public int getPlayerNumber() { return playerNumber; }
    public int getResource() { return resource; }
}