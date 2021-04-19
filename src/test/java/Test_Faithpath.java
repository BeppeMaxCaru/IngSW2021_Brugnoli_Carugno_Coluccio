import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.FaithPath;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Test_Faithpath {

    @Test
    public void moveCrossTrial() {
        FaithPath faithpath = new FaithPath();

        faithpath.moveCross(2);

        assertEquals(2, faithpath.getCrossPosition());
    }

    @Test
    public void RelationInVaticanTrialRemoveCard() {
        Playerboard playerboard = new Playerboard();
        Map<Integer, Integer> discard = new HashMap<>();
        discard.put(2, 0);
        discard.put(3, 3);
        discard.put(4, 4);

        playerboard.getFaithPath().moveCross(4);

        playerboard.getFaithPath().checkRelationWithVatican(8, playerboard);

        assertEquals(playerboard.getFaithPath().discardVaticanCard, discard);

    }

    @Test
    public void RelationInVaticanTrialVictoryPointsInVaticanSection() {
        Playerboard playerboard = new Playerboard();

        playerboard.getFaithPath().moveCross(5);

        playerboard.getFaithPath().checkRelationWithVatican(8, playerboard);

        assertEquals(2, playerboard.getVictoryPoints());
    }

    @Test
    public void RelationInVaticanTrialVictoryPointsAfterVaticanSection() {
        Playerboard playerboard = new Playerboard();

        playerboard.getFaithPath().moveCross(10);

        playerboard.getFaithPath().checkRelationWithVatican(8, playerboard);

        assertEquals(2, playerboard.getVictoryPoints());
    }
}


