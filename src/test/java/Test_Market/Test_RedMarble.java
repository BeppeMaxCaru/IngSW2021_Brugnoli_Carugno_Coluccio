package Test_Market;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.RedMarble;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_RedMarble {

    Player[] players = new Player[2];

    //Try the subclass RedMarble
    @Test
    public void initializeMarbleSubclass() {
        RedMarble redMarble = new RedMarble();
        assertEquals(RedMarble.class, redMarble.getClass());
    }

    //Check that player obtains faithPoints
    @Test
    public void tryDrawMarble1(){
        players[0] = new Player("Beppe",0);
        Marble marble = new RedMarble();
        marble.drawMarble(players,0);
        assertEquals(1,players[0].getPlayerBoard().getFaithPath().getCrossPosition());
    }

    //Check that other players don't obtain faithPoints
    @Test
    public void tryDrawMarble2(){
        players[0] = new Player("Beppe",0);
        players[1] = new Player("Simo",1);
        Marble marble = new RedMarble();
        marble.drawMarble(players,0);
        assertEquals(0,players[1].getPlayerBoard().getFaithPath().getCrossPosition());
    }
}
