package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

/**
 * White marbles has no effects until the related LeaderCard is activated
 */

/**
 * Unlucky white marble that doesn't produce any resource
 */
public class WhiteMarble extends Marble {

    /**
     * Checks if the player can still receive a resource instead of nothing thanks to a leader card activated before
     * @param players - players playing the game
     * @param playerNumber - number of the current player
     * @param chosenMarble
     * @return
     */
    @Override
    public Boolean drawMarble(Player[] players, int playerNumber, String wlChoice, String chosenMarble) {
        if(!chosenMarble.equals("X"))
        {
            int choice = Integer.parseInt(chosenMarble);
            players[playerNumber].getPlayerBoard().getResourceMarbles()[choice].drawMarble(players, playerNumber, wlChoice, chosenMarble);
        }
        return true;
    }

    @Override
    public String getColour(){
        return " WHITE  ";
    }
}