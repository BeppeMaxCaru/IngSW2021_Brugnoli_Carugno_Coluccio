package Maestri.MVC.Model.GModel.MarbleMarket;

import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.Serializable;

/**
 * Composes the market
 */
public class Marble implements Serializable {

    /**
     * Colour of the marble
     */
    private final String colour;

    /**
     * Initializes the marble
     * @param colour - colour of the marble
     */
    public Marble(String colour) {
        this.colour = colour;
    }

    /**
     * Generates one resource for the player that draws it
     * @param players - players playing the game
     * @param playerNumber - number of the current player
     * @param wlChoice - choice between warehouse or extra space given by leaders
     * @param chosenMarble - choice of white marble leader to activate
     */
    public Boolean drawMarble(Player[] players, int playerNumber, String wlChoice, String chosenMarble){
        return false;
    }

    /**
     * Returns the colour of the marble
     * @return the colour of the marble
     */
    public String getColour(){
        return this.colour;
    }

}