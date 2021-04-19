import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.YellowMarble;
import org.junit.jupiter.api.Test;

public class Test_YellowMarble {

    Player[] players = new Player[2];

    //Try the superclass Marble
    @Test
    public void generalMarble(){
        Marble marble = new Marble();
        assertEquals(Marble.class, marble.getClass());
    }

    //Try the subclass YellowMarble
    @Test
    public void initializeMarbleSubclass() {
        YellowMarble yellowMarble = new YellowMarble();
        assertEquals(YellowMarble.class, yellowMarble.getClass());
    }

    //Try the YellowMarble effect
    @Test
    public void tryDrawMarble1() {
        Marble marble = new YellowMarble();
        players[0] = new Player("Beppe",0);
        marble.drawMarble(players,0);
        assertEquals(1,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
   }

    //Try the YellowMarble effect
   @Test
   public void tryDrawMarble2() {
       Marble marble = new YellowMarble();
       players[0] = new Player("Beppe",0);
       marble.drawMarble(players,0);
       marble.drawMarble(players,0);
       marble.drawMarble(players,0);
       assertEquals(3,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
   }

   //Check the discard of the marble
    @Test
    public void tryDiscardResource1() {
        Marble marble = new YellowMarble();
        players[0] = new Player("Beppe",0);
        players[1] = new Player("Simo",1);
        marble.drawMarble(players,0);
        marble.drawMarble(players,0);
        marble.drawMarble(players,0);
        marble.drawMarble(players,0);
        assertEquals(3,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
    }

    //Check that other players obtain faithPoints
    @Test
    public void tryDiscardResource2() {
        Marble marble = new YellowMarble();
        players[0] = new Player("Beppe",0);
        players[1] = new Player("Simo",1);
        marble.drawMarble(players,0);
        marble.drawMarble(players,0);
        marble.drawMarble(players,0);
        marble.drawMarble(players,0);
        assertEquals(1,players[1].getPlayerBoard().getFaithPath().getCrossPosition());
    }

}
