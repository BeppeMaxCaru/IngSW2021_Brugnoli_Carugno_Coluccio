package Carugno.LeaderCards.LaederCardsTypes;

import Carugno.DevelopmentCards.DevelopmentCard;
import Carugno.LeaderCards.LeaderCard;

import java.util.HashMap;
import java.util.Map;

public class DiscountDevelopmentCardsLeaderCard extends LeaderCard {

    private final DevelopmentCard[] requisite;
    private final Map<String, Integer> discount;

    public DiscountDevelopmentCardsLeaderCard(DevelopmentCard firstRequiredDevelopmentCard,
                                              DevelopmentCard secondRequiredDevelopmentCard,
                                              String resource) {
        super(2);

        this.requisite = new DevelopmentCard[2];
        this.requisite[0] = firstRequiredDevelopmentCard; //Oppure costruisco le carte direttamente
        this.requisite[1] = secondRequiredDevelopmentCard; //qui dentro con loro costruttore

        this.discount = new HashMap<>();
        this.discount.put(resource, 1);
    }



}
