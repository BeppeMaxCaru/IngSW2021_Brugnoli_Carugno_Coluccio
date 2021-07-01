package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.io.Serializable;

/**
 * This red marble produces FAITH POINTS
 */
public class RedMarble extends Marble implements Serializable {

    /**
     * Initializes the red marble
     */
    public RedMarble() {
        super(" RED ");
    }

    @Override
    public Boolean drawMarble(Player[] players, int playerNumber, String wlChoice, String chosenMarble)
    {
        players[playerNumber].getPlayerBoard().getFaithPath().moveCross(1);
        return false;
    }
}