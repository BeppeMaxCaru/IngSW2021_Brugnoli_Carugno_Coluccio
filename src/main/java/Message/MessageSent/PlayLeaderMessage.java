package Message.MessageSent;

import Message.Message;

import java.io.Serializable;

public class PlayLeaderMessage extends Message implements Serializable {

    private final int playerNumber;
    private final int played;

    public PlayLeaderMessage(int playerNumber, int card) {
        this.playerNumber = playerNumber;
        this.played = card;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
    public int getPlayed() {
        return played;
    }

}
