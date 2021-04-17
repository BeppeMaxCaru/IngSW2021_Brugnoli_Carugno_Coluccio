import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import org.junit.jupiter.api.Test;


public class Test_DevelopmentCard {

    @Test
    public void trial() {

        DevelopmentCard card = new DevelopmentCard("YELLOW",1);

        DevelopmentCard testDevelopmentCard = new DevelopmentCard("YELLOW",1,
                1,1,1,1,
                1,1,1,1,
                1,1,1,1,
                2,4);

        assertEquals(card.getDevelopmentCardLevel(), testDevelopmentCard.getDevelopmentCardLevel());


    }

}
