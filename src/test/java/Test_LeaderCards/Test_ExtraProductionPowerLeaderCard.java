package Test_LeaderCards;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraProductionPowerLeaderCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Test_ExtraProductionPowerLeaderCard {

    Player[] players = new Player[2];

    @Test
    public void try_creation() {
        players[0] = new Player("Beppe", 0);

        ExtraProductionPowerLeaderCard card=new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW", 2), "SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        assertEquals(ExtraProductionPowerLeaderCard.class, card.getClass());
    }

    @Test
    public void try_constraints1(){
        players[0] = new Player("Beppe", 0);

        players[0].getPlayerBoard().getPlayerBoardDevelopmentCards()[0][0]=new DevelopmentCard("YELLOW",1,
                0,2,0,0,
                0,0,1,0,
                0,0,0,0,
                1,1, null);
        players[0].getPlayerBoard().getPlayerBoardDevelopmentCards()[1][0]=new DevelopmentCard("BLUE",2,
                0,4,0,0,
                0,0,0,1,
                0,0,0,0,
                2,5, null);

        ExtraProductionPowerLeaderCard card=new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW", 2 ), "SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        assertFalse(card.checkRequisites(players[0].getPlayerBoard()));
    }

    @Test
    public void try_constraints2(){
        players[0] = new Player("Beppe", 0);

        players[0].getPlayerBoard().getPlayerBoardDevelopmentCards()[0][0]=new DevelopmentCard("YELLOW",1,
                0,2,0,0,
                0,0,1,0,
                0,0,0,0,
                1,1, null);
        players[0].getPlayerBoard().getPlayerBoardDevelopmentCards()[1][0]=new DevelopmentCard("YELLOW",2,
                0,4,0,0,
                0,0,0,1,
                0,0,0,0,
                2,5, null);

        ExtraProductionPowerLeaderCard card=new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW", 2 ), "SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        assertTrue(card.checkRequisites(players[0].getPlayerBoard()));
    }

    @Test
    public void try_array() {
        players[0] = new Player("Beppe", 0);

        ExtraProductionPowerLeaderCard card=new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW", 2 ), "SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        players[0].getPlayerLeaderCards()[0]=card;
        assertNotNull(players[0].getPlayerLeaderCards()[0]);
    }

    @Test
    public void try_array2() {
        players[0] = new Player("Beppe", 0);

        ExtraProductionPowerLeaderCard card=new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW", 2 ), "SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        players[0].getPlayerLeaderCards()[0]=card;
        assertNull(players[0].getPlayerLeaderCards()[1]);
    }

    @Test
    public void try_discard1() {
        players[0] = new Player("Beppe", 0);

        ExtraProductionPowerLeaderCard card=new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW", 2 ), "SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        players[0].getPlayerLeaderCards()[0]=card;
        players[0].discardLeaderCard(0);
        assertEquals(1, players[0].getPlayerBoard().getFaithPath().getCrossPosition());
    }

    @Test
    public void try_discard2() {
        players[0] = new Player("Beppe", 0);

        ExtraProductionPowerLeaderCard card=new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW", 2), "SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        players[0].getPlayerLeaderCards()[0]=card;
        players[0].discardLeaderCard(0);
        assertNull(players[0].getPlayerLeaderCards()[0]);
    }

    @Test
    public void try_activation1(){
        players[0] = new Player("Beppe", 0);

        assertNull(players[0].getPlayerBoard().getExtraProductionPowerInput()[0]);

    }

    @Test
    public void try_activation2(){
        players[0] = new Player("Beppe", 0);

        ExtraProductionPowerLeaderCard card=new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW", 2), "SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        card.activateAbility(players[0].getPlayerBoard());

        assertNotNull(players[0].getPlayerBoard().getExtraProductionPowerInput()[0]);
    }

    @Test
    public void try_activation3(){
        players[0] = new Player("Beppe", 0);

        ExtraProductionPowerLeaderCard card=new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW", 2), "SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        card.activateAbility(players[0].getPlayerBoard());

        assertNull(players[0].getPlayerBoard().getExtraProductionPowerInput()[1]);
    }

}
