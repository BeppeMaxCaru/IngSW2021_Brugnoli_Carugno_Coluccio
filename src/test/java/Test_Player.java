import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import org.junit.jupiter.api.Test;


public class Test_Player {

    Player[] players = new Player[1];

    @Test
    public void constructorTrial(){
        players[0] = new Player("Ali", 0);
        assertEquals(Player.class, players[0].getClass());
    }

    @Test
    public void sumAllVictoryPointsTrial() {
        players[0] = new Player("Ali", 0);

        players[0].getPlayerBoard().getFaithPath().moveCross(3);

        players[0].getPlayerBoard().sumVictoryPoints(1);

        assertEquals(2, players[0].sumAllVictoryPoints());
    }

    @Test
    public void numResourceReserveTrial(){
        players[0] = new Player("Ali", 0);
        players[0].getPlayerBoard().getWareHouse().getWarehouseResources().put("COINS", 1);
        players[0].getPlayerBoard().getWareHouse().getWarehouseResources().put("STONES", 2);
        players[0].getPlayerBoard().getWareHouse().getWarehouseResources().put("SERVANTS", 3);
        players[0].getPlayerBoard().getWareHouse().getWarehouseResources().put("extraCOINS", 2);
        players[0].getPlayerBoard().getWareHouse().getWarehouseResources().put("extraSHIELDS", 2);
        players[0].getPlayerBoard().getChest().getChestResources().put("COINS", 3);
        players[0].getPlayerBoard().getChest().getChestResources().put("STONES", 3);
        players[0].getPlayerBoard().getChest().getChestResources().put("SHIELDS", 3);
        players[0].getPlayerBoard().getChest().getChestResources().put("SERVANTS", 3);
        assertEquals(22, players[0].numResourcesReserve());
        assertEquals(4, players[0].sumAllVictoryPoints());
        players[0].getPlayerBoard().getChest().getChestResources().put("SERVANTS", 6);
        assertEquals(5, players[0].sumAllVictoryPoints());
    }
}

