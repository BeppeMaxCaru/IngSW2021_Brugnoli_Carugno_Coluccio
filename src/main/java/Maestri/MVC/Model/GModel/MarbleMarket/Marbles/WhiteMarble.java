package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;

/**
 * White marbles has no effects until the related LeaderCard is activated
 */
public class WhiteMarble extends Marble {

    /**
     * WhiteMarbles do nothing
     */
    @Override
    public void drawMarble(Player[] players, int playerNumber) {
        }
}
