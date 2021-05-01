package Maestri.MVC.Model.GModel.MarbleMarket;

import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Composes the market
 */
public class Marble {

    private String colour;

    /**
     * Generates one resource for the player that draws it
     * @param players - players playing the game
     * @param playerNumber - number of the current player
     */
    public void drawMarble(Player[] players, int playerNumber, Scanner in, PrintWriter out){}

    public String getColour(){
        return this.colour;
    }

}