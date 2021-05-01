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
     */
    @Override
    public void drawMarble(Player[] players, int playerNumber)
    {
        players[playerNumber].getPlayerBoard().getFaithPath().moveCross(1);
        System.out.println("You picked: "+this.getClass());
    }

    @Override
    public String getColour(){
        return " RED    ";
    }
}