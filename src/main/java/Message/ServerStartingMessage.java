package Message;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;

public class ServerStartingMessage {

    //public int getPlayerNumber;
    private int playerNumber;
    private LeaderCard[] leaderCards = new LeaderCard[4];

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
