package Message;

import java.io.Serializable;

public class ActivateProdMessage extends Message implements Serializable {

    private final int playerNumber;
    private final int[] activation;
    private final String[] inputs;
    private final int[] outputs;

    public ActivateProdMessage(int playerNumber, int[] activate, String[] whichInput, int[] whichOutput) {
        this.playerNumber = playerNumber;
        this.activation = activate;
        this.inputs = whichInput;
        this.outputs = whichOutput;
    }

    public int getPlayerNumber() { return playerNumber; }
    public String[] getInputs() { return inputs; }
    public int[] getOutputs() { return outputs; }
    public int[] getActivation() {
        return activation;
    }
}
