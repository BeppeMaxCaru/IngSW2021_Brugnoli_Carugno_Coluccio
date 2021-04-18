import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.DiscountDevelopmentCardsLeaderCard;
import org.junit.jupiter.api.Test;


public class Test_LeaderCard {

    Player[] players = new Player[3];

    @Test
    public void discardTrial() {

        //sostituire numero playernumber in metodo draw marble con (playernumber-1)
        //siccome player numero 1 è in posizione 0 nel vettore?
        players[0] = new Player("Beppe",0);
        players[1] = new Player("Ali",1);
        players[2] = new Player("Simo",2);

        players[0].setPlayerLeaderCard(0,new DiscountDevelopmentCardsLeaderCard("YELLOW","BLUE","COINS"));
        assertEquals(new DiscountDevelopmentCardsLeaderCard("YELLOW","BLUE","COINS"),players[0].getPlayerLeaderCards()[0]);
        players[0].setPlayerLeaderCard(3, new DiscountDevelopmentCardsLeaderCard("YELLOW","PURPLE","SHIELDS"));
        assertEquals(null,players[0].getPlayerLeaderCards()[3]);
    }

}
