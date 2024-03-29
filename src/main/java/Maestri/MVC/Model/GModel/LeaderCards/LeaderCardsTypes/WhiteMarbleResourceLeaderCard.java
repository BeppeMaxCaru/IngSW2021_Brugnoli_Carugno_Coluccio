package Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.io.Serializable;

/**
 * This leader card allows players to receive one resource from the white marble
 */
public class WhiteMarbleResourceLeaderCard extends LeaderCard implements Serializable {

    /**
     * Contains the two colours of the development cards required to play the leader card
     */
    private final String[] requisite;

    /**
     * Marble in which the player can convert white marble
     */
    private final Marble whiteMarbleResource;

    /**
     * Image of the card
     */
    private final String image;

    /**
     * Initializes this leader card type
     * @param firstRequiredDevelopmentCard first development card's colour required
     * @param secondRequiredDevelopmentCard second development card's colour required
     * @param resourceFromWhiteMarble resource to receive from white marble
     * @param image image of the card
     */
    public WhiteMarbleResourceLeaderCard(String firstRequiredDevelopmentCard,
                                         String secondRequiredDevelopmentCard,
                                         Marble resourceFromWhiteMarble, String image) {
        super(5);

        this.requisite = new String[2];
        this.requisite[0] = firstRequiredDevelopmentCard;
        this.requisite[1] = secondRequiredDevelopmentCard;
        this.whiteMarbleResource= resourceFromWhiteMarble;
        this.image = image;
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
                if ((playerboard.getPlayerBoardDevelopmentCards()[k][i]!=null)&&(playerboard.getPlayerBoardDevelopmentCards()[k][i].getDevelopmentCardColour().equals(this.requisite[index])))
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
                    if ((playerboard.getPlayerBoardDevelopmentCards()[i][k]!=null)&&(playerboard.getPlayerBoardDevelopmentCards()[i][k].getDevelopmentCardColour().equals(this.requisite[index])))
                        check=true;
                }
            }
        }
        else check = false;

        System.out.println(check);

        return check;
    }

    @Override
    public void activateAbility(Playerboard playerboard) {
        int i = 0;
        while(playerboard.getResourceMarbles()[i] != null)
            i++;
        playerboard.getResourceMarbles()[i] = this.whiteMarbleResource;
        this.setPlayed(true);
    }

    @Override
    public void printLeaderCard(){
        System.out.println("WHITE MARBLE RESOURCE");
        System.out.println("req: 2 "+this.requisite[0] + " 1 "+this.requisite[1]);
        System.out.println("Victory Points: "+this.getVictoryPoints());
        System.out.println("white marble: "+this.whiteMarbleResource.getColour());
        System.out.println();
    }

    @Override
    public String getImage() {
        return this.image;
    }

    /**
     * Returns the marble that the player can pick instead of white marbles
     * @return the marble that the player can pick instead of white marbles
     */
    public Marble getWhiteMarbleResource() { return this.whiteMarbleResource; }
}
