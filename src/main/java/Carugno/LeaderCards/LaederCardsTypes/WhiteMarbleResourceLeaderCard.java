package Carugno.LeaderCards.LaederCardsTypes;

import Brugnoli.Playerboard;
import Carugno.DevelopmentCards.DevelopmentCard;
import Carugno.LeaderCards.LeaderCard;

import java.util.HashMap;
import java.util.Map;

public class WhiteMarbleResourceLeaderCard extends LeaderCard {

    private final DevelopmentCard[] requisite;
    private final Map<String, Integer> whiteMarbleResource;

    public WhiteMarbleResourceLeaderCard(DevelopmentCard firstRequiredDevelopmentCard,
                                         DevelopmentCard secondRequiredDevelopmentCard,
                                         String resourceFromWhiteMarble) {
        super(5);

        this.requisite = new DevelopmentCard[3];
        this.requisite[0] = firstRequiredDevelopmentCard;
        this.requisite[1] = firstRequiredDevelopmentCard;
        this.requisite[2] = secondRequiredDevelopmentCard;

        this.whiteMarbleResource = new HashMap<>();
        this.whiteMarbleResource.put(resourceFromWhiteMarble, 1);
    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
         return true;
    }
}
