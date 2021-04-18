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
    public void drawMarble(Player[] players, int playerNumber) {
        int chosenMarble=-1;
        if(players[playerNumber].getPlayerBoard().getResourceMarbles().length>0)
        {
            if(players[playerNumber].getPlayerBoard().getResourceMarbles().length==1)
                players[playerNumber].getPlayerBoard().getResourceMarbles()[0].drawMarble(players, playerNumber);
            else {
                while(chosenMarble<0||chosenMarble>1){
                    System.out.println("Which resource do you want to pick? Press 0 for the first res., press 1 for the second res.");
                    Scanner playerInput = new Scanner(System.in);
                    chosenMarble = playerInput.nextInt();
                }
                players[playerNumber].getPlayerBoard().getResourceMarbles()[chosenMarble].drawMarble(players, playerNumber);
            }
        }
    }
}