package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

/**
 * This red marble produces faithPoints
 */
public class RedMarble extends Marble {

    /**
     * Moves the current player's red cross one cell forward in the faith path
     * @param players - players playing the game
     * @param playerNumber - number of the current player
     * @param chosenMarble
     * @return
     */
    @Override
    public Boolean drawMarble(Player[] players, int playerNumber, String wlChoice, String chosenMarble)
    {
        players[playerNumber].getPlayerBoard().getFaithPath().moveCross(1);
        return false;
    }

    @Override
    public String getColour(){
        return " RED    ";
    }
}