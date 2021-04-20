import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.YellowMarble;
import org.junit.jupiter.api.Test;


public class Test_Chest {
    Player[] players = new Player[3];

    @Test
    public void chestConstructor() {
        players[0] = new Player("Beppe",0);
        players[1] = new Player("Ali",1);
        players[2] = new Player("Simo",2);

        assertEquals(players[0].getPlayerBoard().getChest().getChestResources(), players[1].getPlayerBoard().getChest().getChestResources());
        assertEquals(players[0].getPlayerBoard().getChest().getChestResources(), players[2].getPlayerBoard().getChest().getChestResources());
    }
}
