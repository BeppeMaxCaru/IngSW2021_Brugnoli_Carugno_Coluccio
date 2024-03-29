package Test_Market;

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
        Marble marble = new Marble(null);
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
        players[0] = new Player("Beppe", 0);
        marble.drawMarble(players,0, "W", null);
        assertEquals(1,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
   }

    //Try the YellowMarble effect
    @Test
    public void tryDrawMarble2() {
        Marble marble = new YellowMarble();
        players[0] = new Player("Beppe", 0);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        assertEquals(3,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
    }

    //Check that other players don't obtain COINS
    @Test
    public void tryDrawMarble3() {
        Marble marble = new YellowMarble();
        players[0] = new Player("Beppe", 0);
        players[1] = new Player("Simo", 1);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        assertEquals(0,players[1].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
    }

    //Try to discard the marble
    @Test
    public void tryDiscardResource1() {
        Marble marble = new YellowMarble();
        players[0] = new Player("Beppe", 0);
        players[1] = new Player("Simo", 1);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        assertEquals(3,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
    }

    //Check that other players obtain faithPoints
    @Test
    public void tryDiscardResource2() {
        Marble marble = new YellowMarble();
        players[0] = new Player("Beppe", 0);
        players[1] = new Player("Simo", 1);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        assertEquals(1,players[1].getPlayerBoard().getFaithPath().getCrossPosition());
    }

    //Check that player doesn't obtain faithPoints
    @Test
    public void tryDiscardResource3() {
        Marble marble = new YellowMarble();
        players[0] = new Player("Beppe", 0);
        players[1] = new Player("Simo", 1);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        marble.drawMarble(players,0, "W", null);
        assertEquals(0,players[0].getPlayerBoard().getFaithPath().getCrossPosition());
    }

}
