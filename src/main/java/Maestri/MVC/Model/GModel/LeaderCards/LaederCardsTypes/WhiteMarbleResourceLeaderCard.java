package Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;

import java.util.Arrays;

public class WhiteMarbleResourceLeaderCard extends LeaderCard {

    private final String[] requisite;
    private final String whiteMarbleResource;
    //private final Map<String, Integer> whiteMarbleResource;

    public WhiteMarbleResourceLeaderCard(String firstRequiredDevelopmentCard,
                                         String secondRequiredDevelopmentCard,
                                         String resourceFromWhiteMarble) {
        super(5);

        this.requisite = new String[2];
        this.requisite[0] = firstRequiredDevelopmentCard;
        this.requisite[1] = secondRequiredDevelopmentCard;

        this.whiteMarbleResource = resourceFromWhiteMarble;

    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        boolean check=false;
        int index=0;
        int cardsRequired=0;
        //Controllo che ci siano due carte di livello qualsiasi del colore dato dal primo requisito
        for(int i=0; i<3; i++)
        {
            for(int k=0; k<3; k++)
            {
                if (playerboard.getPlayerboardDevelopmentCards()[k][i].getDevelopmentCardColour().equals(this.requisite[index]))
                {
                    check=true;
                    cardsRequired++;
                }
            }
        }
        //Controllo che ci sia una sola carta di livello qualsiasi del colore dato dal secondo requisito
        index++;
        if(check&&cardsRequired>1)
        {
            check=false;
            for(int i=0; i<3; i++)
            {
                for(int k=0; k<3; k++)
                {
                    if (playerboard.getPlayerboardDevelopmentCards()[k][i].getDevelopmentCardColour().equals(this.requisite[index]))
                        check=true;
                }
            }
        }
        else check=false;

        return check;
    }
}
