package Test_LeaderCards;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.DiscountDevelopmentCardsLeaderCard;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Test_DiscountLeaderCards {

    Player[] players = new Player[2];

    @Test
    public void try_creation() {
        players[0] = new Player("Beppe");

        DiscountDevelopmentCardsLeaderCard card=new DiscountDevelopmentCardsLeaderCard("YELLOW", "GREEN", "SERVANTS");
        assertEquals(DiscountDevelopmentCardsLeaderCard.class, card.getClass());
    }

    @Test
    public void try_constraints1(){
        players[0] = new Player("Beppe");

        players[0].getPlayerBoard().getPlayerboardDevelopmentCards()[0][0]=new DevelopmentCard("YELLOW",1,
                0,2,0,0,
                0,0,1,0,
                0,0,0,0,
                1,1);
        players[0].getPlayerBoard().getPlayerboardDevelopmentCards()[0][1]=new DevelopmentCard("PURPLE",1,
                0,4,0,0,
                0,0,0,1,
                0,0,0,0,
                2,5);

        DiscountDevelopmentCardsLeaderCard card=new DiscountDevelopmentCardsLeaderCard("YELLOW", "GREEN", "SERVANTS");
        assertFalse(card.checkRequisites(players[0].getPlayerBoard()));
    }

    @Test
    public void try_constraints2(){
        players[0] = new Player("Beppe");

        players[0].getPlayerBoard().getPlayerboardDevelopmentCards()[0][0]=new DevelopmentCard("GREEN",1,
                0,2,0,0,
                0,0,1,0,
                0,0,0,0,
                1,1);
        players[0].getPlayerBoard().getPlayerboardDevelopmentCards()[0][1]=new DevelopmentCard("YELLOW",1,
                0,4,0,0,
                0,0,0,1,
                0,0,0,0,
                2,5);

        DiscountDevelopmentCardsLeaderCard card=new DiscountDevelopmentCardsLeaderCard("YELLOW", "GREEN", "SERVANTS");
        assertTrue(card.checkRequisites(players[0].getPlayerBoard()));
    }

    @Test
    public void try_array() {
        players[0] = new Player("Beppe");

        DiscountDevelopmentCardsLeaderCard card=new DiscountDevelopmentCardsLeaderCard("YELLOW", "GREEN", "SERVANTS");
        players[0].getPlayerLeaderCards()[0]=card;
        assertNotNull(players[0].getPlayerLeaderCards()[0]);
    }

    @Test
    public void try_array2() {
        players[0] = new Player("Beppe");

        DiscountDevelopmentCardsLeaderCard card=new DiscountDevelopmentCardsLeaderCard("YELLOW", "GREEN", "SERVANTS");
        players[0].getPlayerLeaderCards()[0]=card;
        assertNull(players[0].getPlayerLeaderCards()[1]);
    }

    @Test
    public void try_discard1() {
        players[0] = new Player("Beppe");

        DiscountDevelopmentCardsLeaderCard card=new DiscountDevelopmentCardsLeaderCard("YELLOW", "GREEN", "SERVANTS");
        players[0].getPlayerLeaderCards()[0]=card;
        players[0].discardLeaderCard(new Scanner(System.in), new PrintWriter(System.out));
        assertEquals(1, players[0].getPlayerBoard().getFaithPath().getCrossPosition());
    }

    @Test
    public void try_discard2() {
        players[0] = new Player("Beppe");

        DiscountDevelopmentCardsLeaderCard card=new DiscountDevelopmentCardsLeaderCard("YELLOW", "GREEN", "SERVANTS");
        players[0].getPlayerLeaderCards()[0]=card;
        players[0].discardLeaderCard(new Scanner(System.in), new PrintWriter(System.out));
        assertNull(players[0].getPlayerLeaderCards()[0]);
    }

    @Test
    public void try_activation1(){
        players[0] = new Player("Beppe");

        DiscountDevelopmentCardsLeaderCard card=new DiscountDevelopmentCardsLeaderCard("YELLOW", "GREEN", "SERVANTS");
        card.activateAbility(players[0].getPlayerBoard());

        assertNotNull(players[0].getPlayerBoard().getDevelopmentCardDiscount()[0]);
    }

    @Test
    public void try_activation2(){
        players[0] = new Player("Beppe");

        DiscountDevelopmentCardsLeaderCard card=new DiscountDevelopmentCardsLeaderCard("YELLOW", "GREEN", "SERVANTS");
        card.activateAbility(players[0].getPlayerBoard());

        assertNull(players[0].getPlayerBoard().getDevelopmentCardDiscount()[1]);
    }

    @Test
    public void try_activation3(){
        players[0] = new Player("Beppe");

        assertNull(players[0].getPlayerBoard().getDevelopmentCardDiscount()[0]);
    }
}
