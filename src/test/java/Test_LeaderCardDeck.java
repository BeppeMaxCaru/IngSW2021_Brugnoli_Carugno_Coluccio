import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import org.junit.jupiter.api.Test;

public class Test_LeaderCardDeck {

    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();

    @Test
    public void testLeaderCardsDeckInit() {
        assertEquals(new LeaderCard[15], this.leaderCardDeck.getLeaderCardsDeck());
        assertEquals(new LeaderCard(3), this.leaderCardDeck.getLeaderCardsDeck()[4]);
        assertEquals(new LeaderCard(4), this.leaderCardDeck.getLeaderCardsDeck()[12]);
        assertEquals(new LeaderCard(2), this.leaderCardDeck.drawOneLeaderCard());
    }

}
