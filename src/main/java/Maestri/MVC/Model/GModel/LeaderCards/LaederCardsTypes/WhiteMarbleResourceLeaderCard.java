package Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.*;

/**
 * These LeaderCards allow players to collect resources instead of WhiteMarbles
 */
public class WhiteMarbleResourceLeaderCard extends LeaderCard {

    /**
     * Array that contains the two colours of required cards for the activation
     */
    private final String[] requisite;

    /**
     * Marble in which the player converts white marbles
     */
    private final Marble whiteMarbleResource;

    /**
     * Constructor associates inputs by LeaderCardDeck to attributes of the class
     */
    public WhiteMarbleResourceLeaderCard(String firstRequiredDevelopmentCard,
                                         String secondRequiredDevelopmentCard,
                                         String resourceFromWhiteMarble) {
        super(5);

        this.requisite = new String[2];
        this.requisite[0] = firstRequiredDevelopmentCard;
        this.requisite[1] = secondRequiredDevelopmentCard;

        switch(resourceFromWhiteMarble){
            case "COINS":
                this.whiteMarbleResource = new YellowMarble();
                break;
            case "STONES":
                this.whiteMarbleResource = new GreyMarble();
                break;
            case "SERVANTS":
                this.whiteMarbleResource = new PurpleMarble();
                break;
            case "SHIELDS":
                this.whiteMarbleResource = new BlueMarble();
                break;
            default:
                this.whiteMarbleResource = new WhiteMarble();
        }


    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        boolean check=false;
        int index=0;
        int cardsRequired=0;
        //Check that player has 2 development cards of the first requisite colour
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
        //Check that player has 1 development cards of the second requisite colour
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

    @Override
    public void activateAbility(Player player) {
        int i=0;
        while(player.getPlayerBoard().getResourceMarbles()[i]!=null)
            i++;
        player.getPlayerBoard().getResourceMarbles()[i]=this.whiteMarbleResource;
        this.setPlayed(true);
    }
}
