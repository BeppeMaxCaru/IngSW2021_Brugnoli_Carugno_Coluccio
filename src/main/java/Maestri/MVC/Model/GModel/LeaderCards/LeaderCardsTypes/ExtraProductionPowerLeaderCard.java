package Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;

import java.util.*;

/**
 * These LeaderCards give an extra Production Power to the player
 * He spends a specific resource and he obtains a faithPoint and a Resource to be chosen
 */
public class ExtraProductionPowerLeaderCard extends LeaderCard {

    /**
     * Development card required to play the leader card
     */
    private final DevelopmentCard requisite;

    /**
     * Input resource required to activate the production
     */
    private final String input;

    /**
     * Initializes this leader card type
     * @param requisite - development card required
     * @param resourceInput - input resource
     */
    public ExtraProductionPowerLeaderCard(DevelopmentCard requisite, String resourceInput) {
        super(4);
        this.requisite = requisite;
        this.input=resourceInput;

    }

    /**
     * Gifts an extra production power to the player
     * @param playerboard - player playing the leader card
     */
    @Override
    public void activateAbility(Playerboard playerboard) {

        int numWarehouseResources;
        int numChestResources;
        int resourceOutputNum = -1;
        int numResources;
        int i = 0, j=0;

        Scanner in = new Scanner(System.in);

        while(playerboard.getExtraProductionPowerInput()[j] != null)
            j++;
        playerboard.getExtraProductionPowerInput()[j]=this.input;

        // Numero di risorse nel warehouse del tipo this.input
        numWarehouseResources = playerboard.getWareHouse().getWarehouseResources().get(this.input);
        numWarehouseResources = numWarehouseResources + playerboard.getWareHouse().getWarehouseResources().get("extra" + this.input);
        // Numero di risorse nel chest del tipo this.input
        numChestResources = playerboard.getChest().getChestResources().get(this.input);

        if((numChestResources > 0) || (numWarehouseResources > 0))
            playerboard.pickResource(this.input);

        // Scelta della risorsa in output
        while(resourceOutputNum < 0 || resourceOutputNum > 3) {
            System.out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
            resourceOutputNum = in.nextInt();
        }

        for (String key : playerboard.getChest().getChestResources().keySet()) {
            if(i == resourceOutputNum) {
                numResources = playerboard.getChest().getChestResources().get(key);
                playerboard.getChest().getChestResources().put(key, numResources + 1);
                break;
            }
            i++;
        }

        playerboard.getFaithPath().moveCross(1);

        this.setPlayed(true);
    }

    /**
     * Checks if the player can play the leader card
     * @param playerboard - player's player board
     * @return true if the player can play the leader card
     */
    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        boolean check = false;
        for(int i = 0; i < 3; i++)
        {
            if (playerboard.getPlayerboardDevelopmentCards()[this.requisite.getDevelopmentCardLevel()-1][i].getDevelopmentCardColour().equals(this.requisite.getDevelopmentCardColour()))
                check = true;
        }
        return check;
    }
}
