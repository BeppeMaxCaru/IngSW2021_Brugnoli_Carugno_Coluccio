package Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.io.Serializable;

/**
 * Adds two extra spaces to the warehouse for a resource
 */
public class ExtraWarehouseSpaceLeaderCard extends LeaderCard implements Serializable {

    /**
     * Quantity of the resource required for the activation
     */
    private final int quantity = 5;

    /**
     * Resource cost of the leader card
     */
    private final String requisite;

    /**
     * Extra resource type
     */
    private final String resourceSpace;

    /**
     * Image of the card
     */
    private final String image;

    /**
     * Initializes this leader card type
     * @param resourceCost type of resource to pay to activate the leader card
     * @param resourceSpace type of the extra resource space
     * @param image image of the card
     */
    public ExtraWarehouseSpaceLeaderCard(String resourceCost, String resourceSpace, String image) {
        super(3);

        //Association of the required resource to the attribute
        this.requisite=resourceCost;
        //Association of the extra space to the attribute
        this.resourceSpace=resourceSpace;
        this.image = image;
    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        int totalResources=0;
        //Collection of Warehouse+Chest Resources
        totalResources = totalResources+playerboard.getWareHouse().getWarehouseResources().get(this.requisite);
        System.out.println("Resources in warehouse " + totalResources);
        totalResources = totalResources+playerboard.getChest().getChestResources().get(this.requisite);
        System.out.println("Resources in warehouse + chest" + totalResources);
        //Control if Player stored the required Resource in LeaderCard extra space
        if(playerboard.getWareHouse().getWarehouseResources().containsKey("extra"+this.requisite)) {
            totalResources = totalResources + playerboard.getWareHouse().getWarehouseResources().get("extra" + this.requisite);
            System.out.println("Resources in warehouse + chest + extra " + totalResources);
        }
        //Player needs 5 of the required Resource
        return totalResources >= this.quantity;
    }

    @Override
    public void activateAbility(Playerboard playerboard) {
        playerboard.getWareHouse().getWarehouseResources().put("extra"+this.resourceSpace,0);
        //This card is activated, player can't activate it again
        this.setPlayed(true);
    }

    @Override
    public void printLeaderCard() {
        System.out.println("EXTRA WAREHOUSE SPACE");
        System.out.println("req: " + this.quantity + " " + this.requisite);
        System.out.println("Victory Points: " + this.getVictoryPoints());
        System.out.println("extra " + this.resourceSpace);
        System.out.println();
    }

    @Override
    public String getImage() {
        return this.image;
    }

    /**
     * Returns the resource stored in the leader card
     * @return the resource stored in the leader card
     */
    public String getResourceSpace() {return this.resourceSpace; }
}
