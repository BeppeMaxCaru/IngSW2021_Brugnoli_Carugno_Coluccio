package Test_Market;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.BlueMarble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.WhiteMarble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.YellowMarble;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_WhiteMarble {

    Player[] players = new Player[2];

    //Try the subclass YellowMarble
    @Test
    public void initializeMarbleSubclass() {
        WhiteMarble whiteMarble = new WhiteMarble();
        assertEquals(WhiteMarble.class, whiteMarble.getClass());
    }

    //Try the WhiteMarble effect
    @Test
    public void tryDrawMarble1() {
        Marble marble = new WhiteMarble();
        players[0] = new Player("Beppe", 0);
        marble.drawMarble(players,0, null, "X");
        assertEquals(0,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
    }

    //Try the WhiteMarble effect
    @Test
    public void tryDrawMarble2() {
        Marble marble = new WhiteMarble();
        players[0] = new Player("Beppe", 0);
        marble.drawMarble(players,0, null, "X");
        assertEquals(0,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("STONES"));
    }

    //Try the WhiteMarble effect
    @Test
    public void tryDrawMarble3() {
        Marble marble = new WhiteMarble();
        players[0] = new Player("Beppe", 0);
        marble.drawMarble(players,0, null, "X");
        assertEquals(0,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SERVANTS"));
    }

    //Try the WhiteMarble effect
    @Test
    public void tryDrawMarble4() {
        Marble marble = new WhiteMarble();
        players[0] = new Player("Beppe", 0);
        marble.drawMarble(players,0, null, "X");
        assertEquals(0,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SHIELDS"));
    }

    //Try the WhiteMarble effect
    @Test
    public void tryDrawMarble5() {
        Marble marble = new WhiteMarble();
        players[0] = new Player("Beppe", 0);
        marble.drawMarble(players,0, null, "X");
        assertEquals(0,players[0].getPlayerBoard().getFaithPath().getCrossPosition());
    }

    //Try the WhiteMarble effect if a LeaderCard is activated
    @Test
    public void tryResourceWhiteMarble(){
        Marble marble = new WhiteMarble();
        Marble resMarble = new YellowMarble();
        players[0] = new Player("Beppe", 0);
        players[0].getPlayerBoard().setResourceMarbles(resMarble);
        marble.drawMarble(players,0, "W", "0");
        assertEquals(1,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
    }

    //Try the WhiteMarble effect if two LeaderCards are activated
    @Test
    public void tryResourceWhiteMarble2(){
        Marble marble = new WhiteMarble();
        Marble resMarble = new YellowMarble();
        Marble resMarble2 = new BlueMarble();
        players[0] = new Player("Beppe", 0);
        players[0].getPlayerBoard().setResourceMarbles(resMarble);
        players[0].getPlayerBoard().setResourceMarbles(resMarble2);
        marble.drawMarble(players,0, null, "X");
    }
}
