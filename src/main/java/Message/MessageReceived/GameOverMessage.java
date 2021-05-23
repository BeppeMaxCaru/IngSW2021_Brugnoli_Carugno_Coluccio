package Message.MessageReceived;

import Message.Message;

import java.io.Serializable;

public class GameOverMessage extends Message implements Serializable {

    private String winner;
    private int victoryPoints;

    public GameOverMessage (String winner, int victoryPoints) {
        this.winner = winner;
        this.victoryPoints = victoryPoints;
    }

    public String getWinner() {
        return this.winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    /*public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }*/
}
