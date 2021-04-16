package Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

public class DiscountDevelopmentCardsLeaderCard extends LeaderCard {

    private final String[] requisite;
    private final String discount;

    public DiscountDevelopmentCardsLeaderCard(String firstRequiredDevelopmentCard,
                                              String secondRequiredDevelopmentCard,
                                              String resource) {
        super(2);

        this.requisite = new String[2];
        this.requisite[0] = firstRequiredDevelopmentCard;
        this.requisite[1] = secondRequiredDevelopmentCard;

        this.discount = resource;
    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        boolean check=false;
        for(String card : this.requisite)
        {
            check=false;
            for(int i=0; i<3; i++)
            {
                for(int k=0; k<3; k++)
                {
                    if(playerboard.getPlayerboardDevelopmentCards()[i][k].getDevelopmentCardColour().equals(card))
                        check=true;
                }
            }
            if(!check)
                return false;
        }
        return check;
    }

    @Override
    public void activateAbility(Player player) {
        int i=0;
        while(player.getPlayerboard().getDevelopmentCardDiscount()[i]!=null)
            i++;
        player.getPlayerboard().getDevelopmentCardDiscount()[i]=this.discount;
        this.setPlayed(true);
    }
}
