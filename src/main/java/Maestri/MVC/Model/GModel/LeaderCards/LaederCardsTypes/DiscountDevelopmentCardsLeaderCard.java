package Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DiscountDevelopmentCardsLeaderCard extends LeaderCard {

    private final DevelopmentCard[] requisite;
    private final Map<String, Integer> discount;
    //private final int leaderCardType;

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

    //Controllo carta sviluppo Discount
    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        if (!playerboard.getPlayerboardDevelopmentCards().values().containsAll(Arrays.asList(this.requisite))) return false;
        return true;
    }

}
