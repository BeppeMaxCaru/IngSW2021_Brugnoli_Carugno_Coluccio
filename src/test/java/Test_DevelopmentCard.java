import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

public class Test_DevelopmentCard {

    @Test
    public void developmentCardConstructors() {

        DevelopmentCard testPartialDevelopmentCard = new DevelopmentCard("YELLOW",1);

        DevelopmentCard testFullDevelopmentCard = new DevelopmentCard("YELLOW",1,
                1,1,1,1,
                1,1,1,1,
                1,1,1,1,
                2,4, null);
        //Checks initialization
        assertEquals(testPartialDevelopmentCard.getClass(), testFullDevelopmentCard.getClass());
        //Checks colours
        assertEquals(testPartialDevelopmentCard.getDevelopmentCardColour(), testFullDevelopmentCard.getDevelopmentCardColour());
        //Check levels
        assertEquals(testPartialDevelopmentCard.getDevelopmentCardLevel(), testFullDevelopmentCard.getDevelopmentCardLevel());
    }

    @Test
    public void developmentCardAttributesAndGetters() {

        DevelopmentCard developmentCard1 = new DevelopmentCard("RED",1,
                1,1,1,0,
                2,0,2,0,
                2,3,2,3,
                0,1, null);
        DevelopmentCard developmentCard2 = new DevelopmentCard("RED",1,
                1,1,1,0,
                2,0,2,0,
                2,3,2,3,
                0,1, null);

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

}
