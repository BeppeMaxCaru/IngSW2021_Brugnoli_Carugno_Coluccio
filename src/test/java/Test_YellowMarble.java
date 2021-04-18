import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.YellowMarble;
import org.junit.jupiter.api.Test;

public class Test_YellowMarble {

    Player[] players = new Player[2];

    @Test
    public void generalMarble(){
        Marble marble = new Marble();
        assertEquals(Marble.class, marble.getClass());
    }

    @Test
    public void initializeMarbleSubclass() {
        YellowMarble yellowMarble = new YellowMarble();
        assertEquals(YellowMarble.class, yellowMarble.getClass());
    }

    //Try yellow marble
    @Test
    public void tryDrawMarble() {
        players[0] = new Player("Beppe",0);
        Marble marble = new YellowMarble();
        marble.drawMarble(players,0);
        marble.drawMarble(players,0);
        assertEquals(2,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
   }

}
