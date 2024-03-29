package Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.io.Serializable;

/**
 * This leader card allows players to pay development cards 1 resource less than their cost
 */
public class DiscountDevelopmentCardsLeaderCard extends LeaderCard implements Serializable {

    /**
     * Colours of the development cards required to play the leader card
     */
    private final String[] requisite;

    /**
     * Resource to discount
     */
    private final String discount;

    /**
     * Image of the card
     */
    private final String image;

    /**
     * Initializes this leader card type
     * @param firstRequiredDevelopmentCard first development card's colour required
     * @param secondRequiredDevelopmentCard second development card's colour required
     * @param resource resource to discount
     * @param image image of the card
     */
    public DiscountDevelopmentCardsLeaderCard(String firstRequiredDevelopmentCard,
                                              String secondRequiredDevelopmentCard,
                                              String resource, String image) {
        super(2);

        //Association of two input required strings to the class attribute
        this.requisite = new String[2];
        this.requisite[0] = firstRequiredDevelopmentCard;
        this.requisite[1] = secondRequiredDevelopmentCard;

        //Association of the input discount string to the class attribute
        this.discount = resource;

        this.image = image;
    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        boolean check=false;
        //Check that he own both of the cards
        for(String card : this.requisite)
        {
            check=false;
            //Check in his personal 3x3 grid, level doesn't matter
            for(int i=0; i<3; i++)
            {
                for(int k=0; k<3; k++)
                {
                    if((playerboard.getPlayerBoardDevelopmentCards()[i][k]!=null)&&(playerboard.getPlayerBoardDevelopmentCards()[i][k].getDevelopmentCardColour().equals(card)))
                        check=true;
                }
            }
            if(!check)
                return false;
        }
        return check;
    }

    @Override
    public void activateAbility(Playerboard playerboard) {
        int i = 0;
        //Check the correct position of the array and insert the Resource discount
        while(playerboard.getDevelopmentCardDiscount()[i] != null)
            i++;
        playerboard.getDevelopmentCardDiscount()[i] = this.discount;
        //This card is activated, player can't activate it again
        this.setPlayed(true);
    }

    @Override
    public void printLeaderCard(){
        System.out.println("DISCOUNT DEVELOPMENT CARD");
        System.out.println("req: 1 "+this.requisite[0]+" 1 "+this.requisite[1]);
        System.out.println("Victory Points: "+this.getVictoryPoints());
        System.out.println("Discount -1 "+this.discount);
        System.out.println();
    }

    @Override
    public String getImage() {
        return this.image;
    }

    /**
     * Returns the resource that can be discounted
     * @return the resource that can be discounted
     */
    public String getDiscount() {
        return this.discount;
    }
}
