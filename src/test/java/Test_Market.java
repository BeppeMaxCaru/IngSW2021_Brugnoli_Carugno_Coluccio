import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.MarbleMarket.Marble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.*;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import org.junit.jupiter.api.Test;

public class Test_Market {
    Market market = new Market();

    @Test
    public void test_dimension(){
        assertEquals(12,market.getMarketArrangement().length * market.getMarketArrangement()[0].length);
    }

    @Test
    public void print_arrangement(){
        int n = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(market.getMarketArrangement()[i][j]);
                n++;
            }
            System.out.println("\n");
        }
        System.out.println(market.getExcessMarble());
    }

}
