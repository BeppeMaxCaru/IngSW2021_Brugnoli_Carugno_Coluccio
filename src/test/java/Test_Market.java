import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.*;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import org.junit.jupiter.api.Test;

public class Test_Market {
    private final Market market = new Market();

    @Test
    public void test_dimension(){
        assertEquals(13,market.getMarketArrangement().length * market.getMarketArrangement()[0].length);
    }

    @Test
    public void test_arrangement1(){
        WhiteMarble marble = new WhiteMarble();
        assertEquals(marble,market.getMarketArrangement()[0][0]);
    }

    @Test
    public void test_arrangement2(){
        WhiteMarble marble = new WhiteMarble();
        assertEquals(marble,market.getMarketArrangement()[0][1]);
    }

    @Test
    public void test_arrangement3(){
        WhiteMarble marble = new WhiteMarble();
        assertEquals(marble,market.getMarketArrangement()[0][2]);
    }

    @Test
    public void test_arrangement4(){
        WhiteMarble marble = new WhiteMarble();
        assertEquals(marble,market.getMarketArrangement()[0][3]);
    }

    @Test
    public void test_arrangement5(){
        RedMarble marble = new RedMarble();
        assertEquals(marble,market.getMarketArrangement()[1][0]);
    }

    @Test
    public void test_arrangement6(){
        BlueMarble marble = new BlueMarble();
        assertEquals(marble,market.getMarketArrangement()[1][1]);
    }


    @Test
    public void test_arrangement7(){
        BlueMarble marble = new BlueMarble();
        assertEquals(marble,market.getMarketArrangement()[1][2]);
    }

    @Test
    public void test_arrangement8(){
        GreyMarble marble = new GreyMarble();
        assertEquals(marble,market.getMarketArrangement()[1][3]);
    }

    @Test
    public void test_arrangement9(){
        GreyMarble marble = new GreyMarble();
        assertEquals(marble,market.getMarketArrangement()[2][0]);
    }

    @Test
    public void test_arrangement10(){
        YellowMarble marble = new YellowMarble();
        assertEquals(marble,market.getMarketArrangement()[2][1]);
    }

    @Test
    public void test_arrangement11(){
        YellowMarble marble = new YellowMarble();
        assertEquals(marble,market.getMarketArrangement()[2][2]);
    }

    @Test
    public void test_arrangement12(){
        VioletMarble marble = new VioletMarble();
        assertEquals(marble,market.getMarketArrangement()[2][3]);
    }

    @Test
    public void test_excessMarble(){
        VioletMarble marble = new VioletMarble();
        assertEquals(marble,market.getExcessMarble());
    }
}
