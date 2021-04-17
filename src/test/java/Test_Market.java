import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.MarbleMarket.Marble;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import org.junit.jupiter.api.Test;

public class Test_Market {

    Market testMarket = new Market();

    @Test
    public void marketPopulated() {
        assertEquals(new Marble(), testMarket.getMarketArrangement()[0][0]);
        assertEquals(new Marble(), testMarket.getMarketArrangement()[2][2]);
        assertEquals(new Marble(), testMarket.getMarketArrangement()[1][3]);
        assertEquals(null, testMarket.getMarketArrangement()[4][4]);
    }

}
