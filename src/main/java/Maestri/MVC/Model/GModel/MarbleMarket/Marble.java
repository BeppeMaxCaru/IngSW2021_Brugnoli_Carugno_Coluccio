package Maestri.MVC.Model.GModel.MarbleMarket;

import Maestri.MVC.Model.GModel.GamePlayer.Player;

/**
 * Composes the market
 */
public class Marble {

    private String colour;

    /**
     * Generates one resource for the player that draws it
     * @param players - players playing the game
     * @param playerNumber - number of the current player
     * @param chosenMarble
     */
    public Boolean drawMarble(Player[] players, int playerNumber, String wlChoice, String chosenMarble){
        return false;
    }

    public String getColour(){
        return this.colour;
    }

}