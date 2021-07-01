package Test_PlayerBoard;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Test_PlayerBoard {

    Player[] players = new Player[1];

    @Test
    public void isCardBelowCompatibleTrial(){
        players[0]=new Player("Ali", 0);
        DevelopmentCard card1 = new DevelopmentCard("BLUE", 1);
        DevelopmentCard card2 = new DevelopmentCard("YELLOW", 2);
        DevelopmentCard card3 = new DevelopmentCard("GREEN", 3);

        assertTrue(players[0].getPlayerBoard().isCardBelowCompatible(1,card1));
        for(int i=0; i<3; i++)
            System.out.println(players[0].getPlayerBoard().getPlayerBoardDevelopmentCards()[i][1]);
        System.out.println();

        assertTrue(players[0].getPlayerBoard().isCardBelowCompatible(1,card2));
        for(int i=0; i<3; i++)
            System.out.println(players[0].getPlayerBoard().getPlayerBoardDevelopmentCards()[i][1]);
        System.out.println();


        assertTrue(players[0].getPlayerBoard().isCardBelowCompatible(1,card3));
        for(int i=0; i<3; i++)
            System.out.println(players[0].getPlayerBoard().getPlayerBoardDevelopmentCards()[i][1]);
        System.out.println();

        //I can't insert a card on a full pile
        assertFalse(players[0].getPlayerBoard().isCardBelowCompatible(1,card2));

        //I can't insert a level 2 card on an empty pile
        assertFalse(players[0].getPlayerBoard().isCardBelowCompatible(2,card2));

        //I can't insert a level 3 card on an empty pile
        assertFalse(players[0].getPlayerBoard().isCardBelowCompatible(2,card3));

    }
}
