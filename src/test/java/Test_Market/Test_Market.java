package Test_Market;

import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import org.junit.jupiter.api.Test;

public class Test_Market {
    Player[] players= new Player[2];
    Market market = new Market();

    @Test
    public void test_dimension(){
        assertEquals(12,market.getMarketArrangement().length * market.getMarketArrangement()[0].length);
    }

    @Test
    public void print_arrangement(){

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(market.getMarketArrangement()[i][j]);
            }
            System.out.println("\n");
        }
        System.out.println(market.getExcessMarble());
    }

    @Test
    public void test_updateColumn(){
        players[0]= new Player("Simo", 0);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(market.getMarketArrangement()[i][j]);
            }
            System.out.println("\n");
        }
        System.out.println(market.getExcessMarble());

        market.updateColumn(0, players, 0);
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("STONES"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SHIELDS"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SERVANTS"));
        System.out.println(players[0].getPlayerBoard().getFaithPath().getCrossPosition());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(market.getMarketArrangement()[i][j]);
            }
            System.out.println("\n");
        }
        System.out.println(market.getExcessMarble());

    }

    @Test
    public void test_updateRow(){
        players[0]= new Player("Simo", 0);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(market.getMarketArrangement()[i][j]);
            }
            System.out.println("\n");
        }
        System.out.println(market.getExcessMarble());

        market.updateRow(0, players, 0);
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("STONES"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SHIELDS"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SERVANTS"));
        System.out.println(players[0].getPlayerBoard().getFaithPath().getCrossPosition());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(market.getMarketArrangement()[i][j]);
            }
            System.out.println("\n");
        }
        System.out.println(market.getExcessMarble());

    }

}
