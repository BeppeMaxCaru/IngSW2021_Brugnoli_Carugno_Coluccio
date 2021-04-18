import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Chest;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import org.junit.jupiter.api.Test;

import java.util.Scanner;


public class Test_DevelopmentCard {

    @Test
    public void developmentCardConstructors() {

        DevelopmentCard testPartialDevelopmentCard = new DevelopmentCard("YELLOW",1);

        DevelopmentCard testFullDevelopmentCard = new DevelopmentCard("YELLOW",1,
                1,1,1,1,
                1,1,1,1,
                1,1,1,1,
                2,4);
        //Checks initialization
        assertEquals(testPartialDevelopmentCard.getClass(), testFullDevelopmentCard.getClass());
        //Checks colours
        assertEquals(testPartialDevelopmentCard.getDevelopmentCardColour(), testFullDevelopmentCard.getDevelopmentCardColour());
        //Check levels
        assertEquals(testPartialDevelopmentCard.getDevelopmentCardLevel(), testFullDevelopmentCard.getDevelopmentCardLevel());
    }

    @Test
    public void developmentCardAttributesAndGetters() {

        DevelopmentCard developmentCard1 = new DevelopmentCard("RED",7,
                5,4,5,4,
                2,7,2,7,
                2,3,2,3,
                0,1);
        DevelopmentCard developmentCard2 = new DevelopmentCard("RED",7,
                5,4,5,4,
                2,7,2,7,
                2,3,2,3,
                0,1);

        //Checks initialization
        assertEquals(developmentCard1.getClass(), developmentCard2.getClass());
        //Checks colours
        assertEquals(developmentCard1.getDevelopmentCardColour(), developmentCard2.getDevelopmentCardColour());
        //Checks levels
        assertEquals(developmentCard1.getDevelopmentCardLevel(), developmentCard2.getDevelopmentCardLevel());
        //Checks cost
        assertEquals(developmentCard1.getDevelopmentCardCost(), developmentCard2.getDevelopmentCardCost());
        //Checks input
        assertEquals(developmentCard1.getDevelopmentCardInput(), developmentCard2.getDevelopmentCardInput());
        //Checks output
        assertEquals(developmentCard1.getDevelopmentCardOutput(), developmentCard2.getDevelopmentCardOutput());
        //Checks faith points
        assertEquals(developmentCard1.getFaithPoints(), developmentCard2.getFaithPoints());
        //Checks victory points
        assertEquals(developmentCard1.getVictoryPoints(), developmentCard2.getVictoryPoints());
    }

    @Test
    public void validBuy() {

        Playerboard playerboard = new Playerboard();
        playerboard.getChest().getChestResources().put("COINS", 4);
        playerboard.getChest().getChestResources().put("STONES", 5);
        playerboard.getWareHouse().getWarehouseResources().put("SERVANTS", 7);
        playerboard.getWareHouse().getWarehouseResources().put("SHIELDS", 6);

        DevelopmentCard developmentCard = new DevelopmentCard("BLUE",1,
                1,1,2,2,
                1,1,1,1,
                1,1,1,1,
                1,2);

        assertTrue(developmentCard.checkResourcesAvailability(playerboard, developmentCard.getDevelopmentCardCost()));
        assertTrue(developmentCard.checkPlayerboardDevelopmentCardsCompatibility(playerboard));
    }

    @Test
    public void invalidBuy() {
        Playerboard playerboard = new Playerboard();
        playerboard.getChest().getChestResources().put("COINS",4);
        playerboard.getChest().getChestResources().put("STONES", 5);
        playerboard.getWareHouse().getWarehouseResources().put("SERVANTS", 7);
        playerboard.getWareHouse().getWarehouseResources().put("SHIELDS", 6);
        DevelopmentCard developmentCard1 = new DevelopmentCard("BLUE",1,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
        DevelopmentCard developmentCard2 = new DevelopmentCard("BLUE",1,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
        DevelopmentCard developmentCard3 = new DevelopmentCard("BLUE",1,0,0,0,0,0,0,0,0,0,0,0,0,0,0);

        //Scanner in = new Scanner(System.in);
        //in.nextInt();

        playerboard.placeNewDevelopmentCard(developmentCard1);
        playerboard.placeNewDevelopmentCard(developmentCard2);
        playerboard.placeNewDevelopmentCard(developmentCard3);

        DevelopmentCard developmentCard = new DevelopmentCard("BLUE",1,
                4,5,8,6,
                1,1,1,1,
                1,1,1,1,
                1,2);

        assertFalse(developmentCard.checkResourcesAvailability(playerboard, developmentCard.getDevelopmentCardCost()));
        assertFalse(developmentCard.checkPlayerboardDevelopmentCardsCompatibility(playerboard));
    }

}
