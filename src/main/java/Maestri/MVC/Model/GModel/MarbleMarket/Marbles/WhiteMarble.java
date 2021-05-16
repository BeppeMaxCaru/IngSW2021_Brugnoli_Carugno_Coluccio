package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.util.Scanner;

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
     */
    @Override
    public void drawMarble(Player[] players, int playerNumber, String wlChoice, int chosenMarble) {
        if(players[playerNumber].getPlayerBoard().getResourceMarbles()[0]!=null)
        {
            players[playerNumber].getPlayerBoard().getResourceMarbles()[chosenMarble].drawMarble(players, playerNumber, wlChoice, chosenMarble);
        }
    }

    @Override
    public String getColour(){
        return " WHITE  ";
    }
}