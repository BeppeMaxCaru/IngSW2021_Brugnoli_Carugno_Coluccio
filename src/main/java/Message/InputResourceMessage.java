package Message;

import java.io.Serializable;

public class InputResourceMessage implements Serializable {

    private final int playerNumber;
    private final String inputs;

    public InputResourceMessage(int playerNumber, String whichInput) {
        this.playerNumber = playerNumber;
        this.inputs = whichInput;
    }

    public int getPlayerNumber() { return playerNumber; }
    public String getInputs() { return inputs; }
}
