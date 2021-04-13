package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;

/**
 * RedMarble produces faithPoints
 */
public class RedMarble extends Marble {

    /**
     * The only effect of the red marble is to increase the faithPath position of one step
     */
    @Override
    public void drawMarble(Player[] players, int playerNumber)
    {
        players[playerNumber].getPlayerboard().getFaithPath().moveCross(1);
    }
}