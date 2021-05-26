package Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.io.Serializable;

/**
 * Adds two extra spaces to the warehouse for a resource
 */
public class ExtraWarehouseSpaceLeaderCard extends LeaderCard implements Serializable {

    /**
     * Resource cost of the leader card
     */
    private final String requisite;

    /**
     * Extra resource type
     */
    private final String resourceSpace;

    /**
     * Initializes this leader card type
     * @param resourceCost - type of resource to pay to activate the leader card
     * @param resourceSpace - type of the extra resource space
     */
    public ExtraWarehouseSpaceLeaderCard(String resourceCost, String resourceSpace) {
        super(3);

        //Association of the required resource to the attribute
        this.requisite=resourceCost;
        //Association of the extra space to the attribute
        this.resourceSpace=resourceSpace;
    }

    /**
     * Checks if the player has enough resources to play the leader card
     * @param playerboard - player's player board
     * @return true if the player can play the leader card
     */
    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        int totalResources=0;
        //Collection of Warehouse+Chest Resources
        totalResources=totalResources+playerboard.getWareHouse().getWarehouseResources().get(this.requisite);
        totalResources=totalResources+playerboard.getChest().getChestResources().get(this.requisite);
        //Control if Player stored the required Resource in LeaderCard extra space
        if(playerboard.getWareHouse().getWarehouseResources().containsKey("extra"+this.requisite))
            totalResources=totalResources+playerboard.getWareHouse().getWarehouseResources().get("extra"+this.requisite);
        //Player needs 5 of the required Resource
        return totalResources >= 5;
    }

    /**
     * Adds the extra spaces to the warehouse
     * @param playerboard - player playing the leader card's playerboard
     */
    @Override
    public void activateAbility(Playerboard playerboard) {
        playerboard.getWareHouse().getWarehouseResources().put("extra"+this.resourceSpace,0);
        //This card is activated, player can't activate it again
        this.setPlayed(true);
    }

    @Override
    public void printLeaderCard() {
        System.out.println("EXTRA WAREHOUSE SPACE");
        System.out.println("req: 5 " + this.requisite);
        System.out.println("Victory Points: " + this.getVictoryPoints());
        System.out.println("extra " + this.resourceSpace);
        System.out.println();
    }
}
