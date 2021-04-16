package Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

public class ExtraWarehouseSpaceLeaderCard extends LeaderCard {

    private final String requisite;
    private final String resourceSpace;

    public ExtraWarehouseSpaceLeaderCard(String resourceCost, String resourceSpace) {
        super(3);

        this.requisite=resourceCost;
        this.resourceSpace=resourceSpace;
    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        int totalResources=0;
        totalResources=totalResources+playerboard.getWareHouse().getWarehouseResources().get(this.requisite);
        totalResources=totalResources+playerboard.getChest().getChestResources().get(this.requisite);
        return totalResources >= 5;
    }

    @Override
    public void activateAbility(Player player) {
        player.getPlayerboard().getWareHouse().getWarehouseResources().put("extra"+this.resourceSpace,0);
        this.setPlayed(true);
    }
}
