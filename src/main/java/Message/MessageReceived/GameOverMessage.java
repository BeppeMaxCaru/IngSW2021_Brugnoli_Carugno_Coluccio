package Message.MessageReceived;

import Message.Message;

import java.io.Serializable;

public class GameOverMessage extends Message implements Serializable {

    private final String winner;
    private final int victoryPoints;

    public GameOverMessage (String winner, int victoryPoints) {
        this.winner = winner;
        this.victoryPoints = victoryPoints;
    }

    public String getWinner() {
        return this.winner;
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

}
