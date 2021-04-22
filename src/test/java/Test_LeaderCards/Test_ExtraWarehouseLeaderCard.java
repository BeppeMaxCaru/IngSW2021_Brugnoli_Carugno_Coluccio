package Test_LeaderCards;

import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraWarehouseSpaceLeaderCard;
import org.junit.jupiter.api.Test;


public class Test_ExtraWarehouseLeaderCard {

    Player[] players = new Player[3];

    @Test
    public void try_creation() {
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("C0INS", "STONES");
        assertEquals(ExtraWarehouseSpaceLeaderCard.class, card.getClass());
    }

    @Test
    public void try_constraints1(){
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("COINS", "STONES");
        players[0].getPlayerBoard().getChest().getChestResources().put("COINS", 4);
        assertFalse(card.checkRequisites(players[0].getPlayerBoard()));
    }

    @Test
    public void try_constraints2(){
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("COINS", "STONES");
        players[0].getPlayerBoard().getChest().getChestResources().put("COINS", 5);
        assertTrue(card.checkRequisites(players[0].getPlayerBoard()));
    }

    @Test
    public void try_constraints3(){
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("COINS", "STONES");
        players[0].getPlayerBoard().getChest().getChestResources().put("COINS", 3);
        players[0].getPlayerBoard().getWareHouse().getWarehouseResources().put("COINS", 2);
        assertTrue(card.checkRequisites(players[0].getPlayerBoard()));
    }

    @Test
    public void try_array() {
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("C0INS", "STONES");
        players[0].getPlayerLeaderCards()[0]=card;
        assertNotNull(players[0].getPlayerLeaderCards()[0]);
    }

    @Test
    public void try_array2() {
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("C0INS", "STONES");
        players[0].getPlayerLeaderCards()[0]=card;
        assertNull(players[0].getPlayerLeaderCards()[1]);
    }

    @Test
    public void try_discard1() {
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("C0INS", "STONES");
        players[0].getPlayerLeaderCards()[0]=card;
        players[0].discardLeaderCard();
        assertEquals(1, players[0].getPlayerBoard().getFaithPath().getCrossPosition());
    }

    @Test
    public void try_discard2() {
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("C0INS", "STONES");
        players[0].getPlayerLeaderCards()[0]=card;
        players[0].discardLeaderCard();
        assertNull(players[0].getPlayerLeaderCards()[0]);
    }

    @Test
    public void try_activation1(){
        players[0] = new Player("Beppe",0);

        assertNull(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("extraSTONES"));
    }

    @Test
    public void try_activation2(){
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("C0INS", "STONES");
        card.activateAbility(players[0].getPlayerBoard());
        assertNotNull(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("extraSTONES"));
    }

    @Test
    public void try_activation3(){
        players[0] = new Player("Beppe",0);

        ExtraWarehouseSpaceLeaderCard card=new ExtraWarehouseSpaceLeaderCard("C0INS", "STONES");
        card.activateAbility(players[0].getPlayerBoard());
        assertEquals(0,players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("extraSTONES"));
    }
}
