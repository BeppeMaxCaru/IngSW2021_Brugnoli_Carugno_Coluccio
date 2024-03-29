package Message;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;

import java.io.Serializable;

public class ServerStartingMessage extends Message implements Serializable {

    //public int getPlayerNumber;
    private final int playerNumber;
    private final LeaderCard[] leaderCards;

    public ServerStartingMessage (int playerNumber, LeaderCard[] leaderCards) {
        this.playerNumber = playerNumber;
        this.leaderCards = leaderCards;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public LeaderCard[] getLeaderCards() {
        return this.leaderCards;
    }
}
