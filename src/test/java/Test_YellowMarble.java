import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.RedMarble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.YellowMarble;
import org.junit.jupiter.api.Test;

public class Test_YellowMarble {

    YellowMarble testYellowMarble = new YellowMarble();
    Marble marble = new Marble();
    Player[] players = new Player[2];

    @Test
   public void initializeMarbleSubclass() {
       assertEquals(marble, testYellowMarble);
   }

   //Try yellow marble and red cross marble
   @Test
    public void tryDrawMarble() {
       players[0] = new Player("Beppe",1);
       Marble marble = new YellowMarble();
       //Attenzione player number deve essere diminuito di uno in draw marble
       //dato che se i giocatori sono salvati in un vettore il primo
       //viene salvato in posizione 0 nonostante sia il numero uno
       marble.drawMarble(players,1);
       assertEquals(1,players[1].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
       Marble marble1 = new RedMarble();
       marble1.drawMarble(players,1);
       assertEquals(1,players[1].getPlayerBoard().getFaithPath().getCrossPosition());

   }

}
