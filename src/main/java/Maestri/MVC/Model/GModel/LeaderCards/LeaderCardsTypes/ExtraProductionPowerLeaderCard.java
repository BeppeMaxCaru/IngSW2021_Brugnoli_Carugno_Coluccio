package Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;

import java.io.Serializable;

/**
 * These LeaderCards give an extra Production Power to the player
 * He spends a specific resource and he obtains a faithPoint and a Resource to be chosen
 */
public class ExtraProductionPowerLeaderCard extends LeaderCard implements Serializable {

    /**
     * Development card required to play the leader card
     */
    private final DevelopmentCard requisite;

    /**
     * Input resource required to activate the production
     */
    private final String input;

    /**
     * Image of the card
     */
    private final String image;

    /**
     * Initializes this leader card type
     * @param requisite development card required
     * @param resourceInput input resource
     * @param image image of the card
     */
    public ExtraProductionPowerLeaderCard(DevelopmentCard requisite, String resourceInput, String image) {
        super(4);
        this.requisite = requisite;
        this.input=resourceInput;
        this.image = image;
    }

    public String getInput() { return this.input;}

    @Override
    public void activateAbility(Playerboard playerboard) {

        int j=0;
        while(playerboard.getExtraProductionPowerInput()[j] != null)
            j++;
        playerboard.getExtraProductionPowerInput()[j]=this.input;

        this.setPlayed(true);
    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        boolean check = false;
        for(int i = 0; i < 3; i++)
        {

            if (playerboard.getPlayerBoardDevelopmentCards()[this.requisite.getDevelopmentCardLevel()-1][i]!=null) {
                System.out.println(playerboard.getPlayerBoardDevelopmentCards()[this.requisite.getDevelopmentCardLevel()-1][i].getDevelopmentCardLevel());
                if (playerboard.getPlayerBoardDevelopmentCards()[this.requisite.getDevelopmentCardLevel()-1][i].getDevelopmentCardColour().equals(this.requisite.getDevelopmentCardColour())) {
                    System.out.println(this.requisite.getDevelopmentCardColour());
                    System.out.println(playerboard.getPlayerBoardDevelopmentCards()[this.requisite.getDevelopmentCardLevel()-1][i].getDevelopmentCardColour());
                    check = true;
                }
            }
        }
        System.out.println(check);
        return check;
    }

    @Override
    public void printLeaderCard(){
        System.out.println("EXTRA PRODUCTION POWER");
        System.out.println("req: 1 "+this.requisite.getDevelopmentCardColour()+" lev."+this.requisite.getDevelopmentCardLevel());
        System.out.println("Victory Points: "+this.getVictoryPoints());
        System.out.println("1 "+this.input+" } 1? 1FaithPoint");
        System.out.println();
    }

    @Override
    public String getImage() {
        return this.image;
    }
}

