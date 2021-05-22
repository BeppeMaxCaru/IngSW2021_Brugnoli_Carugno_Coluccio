package Message;

import java.io.Serializable;

public class DiscardLeaderMessage implements Serializable {

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
