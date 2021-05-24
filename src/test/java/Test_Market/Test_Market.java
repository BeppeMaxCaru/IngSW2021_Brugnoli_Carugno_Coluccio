package Test_Market;

import static org.junit.jupiter.api.Assertions.*;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.Scanner;

public class Test_Market {
    Player[] players= new Player[2];
    Market market = new Market();

    @Test
    public void test_dimension(){
        assertEquals(12,market.getMarketArrangement().length * market.getMarketArrangement()[0].length);
    }

    @Test
    public void print_arrangement(){
        market.printMarket();
    }

    @Test
    public void test_updateColumn(){
        players[0] = new Player("Beppe", 0);

        market.printMarket();

        //market.updateColumn(0, players, 0, new Scanner(System.in), new PrintWriter(System.out));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("STONES"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SHIELDS"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SERVANTS"));
        System.out.println(players[0].getPlayerBoard().getFaithPath().getCrossPosition());

        market.printMarket();

    }

    @Test
    public void test_updateRow(){
        players[0]= new Player("Simo", 0);

        market.printMarket();

        //market.updateRow(0, players, 0, new Scanner(System.in), new PrintWriter(System.out));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("STONES"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SHIELDS"));
        System.out.println(players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SERVANTS"));
        System.out.println(players[0].getPlayerBoard().getFaithPath().getCrossPosition());

        market.printMarket();

    }

}
