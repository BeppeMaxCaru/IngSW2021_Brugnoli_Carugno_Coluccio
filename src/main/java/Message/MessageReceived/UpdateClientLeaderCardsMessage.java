package Message.MessageReceived;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Message.Message;

import java.io.Serializable;

public class UpdateClientLeaderCardsMessage extends Message implements Serializable {

    private final LeaderCard[] leaderCards;

    public UpdateClientLeaderCardsMessage(LeaderCard[] leaderCards) {
        this.leaderCards = leaderCards;
    }

    public LeaderCard[] getLeaderCards() {
        return this.leaderCards;
    }
}
