package Carugno.LeaderCards.LaederCardsTypes;

import Brugnoli.Playerboard;
import Carugno.DevelopmentCards.DevelopmentCardWithErrors;
import Carugno.LeaderCards.LeaderCard;

import java.util.HashMap;
import java.util.Map;

public class WhiteMarbleResourceLeaderCard extends LeaderCard {

    private final DevelopmentCardWithErrors[] requisite;
    private final Map<String, Integer> whiteMarbleResource;

    public WhiteMarbleResourceLeaderCard(DevelopmentCardWithErrors firstRequiredDevelopmentCard,
                                         DevelopmentCardWithErrors secondRequiredDevelopmentCard,
                                         String resourceFromWhiteMarble) {
        super(5);

        this.requisite = new DevelopmentCardWithErrors[3];
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
