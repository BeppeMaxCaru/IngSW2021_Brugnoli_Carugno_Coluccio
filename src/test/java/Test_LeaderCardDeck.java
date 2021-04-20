import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import org.junit.jupiter.api.Test;

public class Test_LeaderCardDeck {

    LeaderCardDeck leaderCardDeck = new LeaderCardDeck();

    @Test
    public void testLeaderCardsDeckLength() {
        for(int i=0; i<16; i++)
            System.out.println(this.leaderCardDeck.getLeaderCardsDeck()[i].getClass());
        assertEquals(16, this.leaderCardDeck.getLeaderCardsDeck().length);
    }

    @Test
    public void testDrawCard1(){
        this.leaderCardDeck.drawOneLeaderCard();
        assertEquals(16, this.leaderCardDeck.getLeaderCardsDeck().length);
    }

    @Test
    public void testDrawCard2(){
        this.leaderCardDeck.drawOneLeaderCard();
        assertNull(this.leaderCardDeck.getLeaderCardsDeck()[15]);
    }

    @Test
    public void testDrawCard3(){
        for(int z=0; z<16; z++)
            this.leaderCardDeck.drawOneLeaderCard();

        int i=0;
        for(int k=0; k<this.leaderCardDeck.getLeaderCardsDeck().length; k++)
            if(this.leaderCardDeck.getLeaderCardsDeck()[k]!=null)
                i++;
        assertEquals(0,i);
    }


}
