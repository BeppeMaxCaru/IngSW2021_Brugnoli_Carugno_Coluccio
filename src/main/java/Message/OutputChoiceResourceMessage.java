package Message;

import java.io.Serializable;

public class OutputChoiceResourceMessage implements Serializable {

    private final int playerNumber;
    private final String resource;

    public OutputChoiceResourceMessage(int playerNumber, String resource) {
        this.playerNumber = playerNumber;
        this.resource = resource;
    }

    public int getPlayerNumber() { return playerNumber; }
    public String getResource() { return resource; }
}