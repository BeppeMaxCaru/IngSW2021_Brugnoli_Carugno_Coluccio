package Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;

import java.util.HashMap;
import java.util.Map;

public class WhiteMarbleResourceLeaderCard extends LeaderCard {

    private final DevelopmentCard[] requisite;
    private final String whiteMarbleResource;
    //private final Map<String, Integer> whiteMarbleResource;

    public WhiteMarbleResourceLeaderCard(DevelopmentCard firstRequiredDevelopmentCard,
                                         DevelopmentCard secondRequiredDevelopmentCard,
                                         String resourceFromWhiteMarble) {
        super(5);

        this.requisite = new DevelopmentCard[3];
        this.requisite[0] = firstRequiredDevelopmentCard;
        this.requisite[1] = firstRequiredDevelopmentCard;
        this.requisite[2] = secondRequiredDevelopmentCard;

        this.whiteMarbleResource = resourceFromWhiteMarble;

        //this.whiteMarbleResource = new HashMap<>();
        //this.whiteMarbleResource.put(resourceFromWhiteMarble, 1);
    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        int firstCardRequisiteNumber = 0;
        int secondCardRequisiteNumber = 0;
        for (DevelopmentCard DevelopmentCard : this.requisite) {
            if (!playerboard.getPlayerboardDevelopmentCards().values().contains(this.requisite)) return false;
            if (playerboard.getPlayerboardDevelopmentCards().values().containsAll(this.requisite))

        }


        return true;
    }
}
