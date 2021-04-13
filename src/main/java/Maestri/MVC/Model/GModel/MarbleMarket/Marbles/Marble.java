package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

/**
 * Marbles compose the Market. Players can pick them to collect resources in their player boards.
 * Marble class hasn't any attributes. Marbles are distinguished by their effects
 */
public class Marble {

    /**
     * This method add resources to players' boards.
     * It is defined in each of its subclasses
     */
    public void drawMarble(GameModel gameModel, int playerNumber){
    }
}