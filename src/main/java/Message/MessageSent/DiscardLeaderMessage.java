package Message.MessageSent;

import Message.Message;

import java.io.Serializable;

public class DiscardLeaderMessage extends Message implements Serializable {

    private final int playerNumber;
    private final int discarded;

    public DiscardLeaderMessage(int playerNumber, int card) {
        this.playerNumber = playerNumber;
        this.discarded = card;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
    public int getDiscarded() {
        return discarded;
    }

}
