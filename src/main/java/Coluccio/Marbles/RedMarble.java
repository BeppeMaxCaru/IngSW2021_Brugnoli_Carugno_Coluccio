package Coluccio.Marbles;

import Brugnoli.FaithPath;
import Brugnoli.Player;
import Carugno.MVC.GameModel;
import Coluccio.Marble;

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