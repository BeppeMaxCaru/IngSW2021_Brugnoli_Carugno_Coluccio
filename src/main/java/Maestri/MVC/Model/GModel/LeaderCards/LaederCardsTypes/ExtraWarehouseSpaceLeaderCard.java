package Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

/**
 * These LeaderCards add an extra warehouse space containing 2 resources to the player
 */
public class ExtraWarehouseSpaceLeaderCard extends LeaderCard {

    /**
     * DevelopmentCard required for the activation
     */
    private final String requisite;

    /**
     * Extra Resource type
     */
    private final String resourceSpace;

    /**
     * Constructor associates inputs by LeaderCardDeck to attributes of the class
     */
    public ExtraWarehouseSpaceLeaderCard(String resourceCost, String resourceSpace) {
        super(3);

        //Association of the required resource to the attribute
        this.requisite=resourceCost;
        //Association of the extra space to the attribute
        this.resourceSpace=resourceSpace;
    }

    /**
     * The player needs 5 of the specific resource for the activation of the LeaderCard
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
     * This method adds to the Player Warehouse the extra space
     */
    @Override
    public void activateAbility(Player player) {
        player.getPlayerboard().getWareHouse().getWarehouseResources().put("extra"+this.resourceSpace,0);
        //This card is activated, player can't activate it again
        this.setPlayed(true);
    }
}
