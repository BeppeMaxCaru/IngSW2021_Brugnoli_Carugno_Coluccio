package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GameModel;

/**
 * RedMarble produces faithPoints
 */
public class RedMarble extends Marble {

    /**
     * Override of the Marble method drawMarble
     */
    @Override
    public void drawMarble(GameModel gameModel, Player player)
    {
        /**
        * The only effect of the red marble is to increase the faithPath position of one step
        */
        player.getPlayerboard().getFaithPath().moveCross(1);
    }
}