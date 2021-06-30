package Message.MessageReceived;

import Message.Message;

import java.io.Serializable;

public class TurnOverMessage extends Message implements Serializable {
    private final String turn;

    public TurnOverMessage(String turn){
        this.turn = turn;
    }

    public String getTurn() {
        return this.turn;
    }
}
