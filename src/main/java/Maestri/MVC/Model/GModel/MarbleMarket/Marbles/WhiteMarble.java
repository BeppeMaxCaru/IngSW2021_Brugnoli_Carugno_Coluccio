package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.io.Serializable;

/**
 * This white marble doesn't produce any resource until the related LeaderCard is activated
 */
public class WhiteMarble extends Marble implements Serializable {

    /**
     * Initializes the white marble
     */
    public WhiteMarble() {
        super(" WHITE ");
    }

    @Override
    public Boolean drawMarble(Player[] players, int playerNumber, String wlChoice, String chosenMarble) {
        if(!chosenMarble.equals("X"))
        {
            int choice = Integer.parseInt(chosenMarble);
            players[playerNumber].getPlayerBoard().getResourceMarbles()[choice].drawMarble(players, playerNumber, wlChoice, chosenMarble);
        }
        return true;
    }
}