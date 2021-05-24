package Message;

import Message.Message;

import java.io.Serializable;

public class ActivateProdMessage extends Message implements Serializable {

    private final int playerNumber;
    private final String inputs;
    private final String outputs;

    public ActivateProdMessage(int playerNumber, String whichInput, String whichOutput) {
        this.playerNumber = playerNumber;
        this.inputs = whichInput;
        this.outputs = whichOutput;
    }

    public int getPlayerNumber() { return playerNumber; }
    public String getInputs() { return inputs; }
    public String getOutputs() { return outputs; }
}
