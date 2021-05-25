package Message.MessageSent;

import Message.Message;

import java.io.Serializable;

public class EndTurnMessage extends Message implements Serializable {
    int playerNumber;

    public EndTurnMessage(int num){
        this.playerNumber=num;
    }

    @Override
    public int getPlayerNumber() {
        return playerNumber;
    }
}
