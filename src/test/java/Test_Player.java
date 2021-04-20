import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import org.junit.jupiter.api.Test;


public class Test_Player {

    Player[] players = new Player[1];

    @Test
    public void sumAllVictoryPointsTrial() {
        players[0] = new Player("Ali", 0);

        players[0].getPlayerBoard().getFaithPath().moveCross(3);

        players[0].getPlayerBoard().sumVictoryPoints(1);

        assertEquals(2, players[0].sumAllVictoryPoints());
    }
}

