package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.util.Scanner;

/**
 * White marbles has no effects until the related LeaderCard is activated
 */
public class WhiteMarble extends Marble {

    /**
     * This method checks if the player has ResourceMarbles to be collected instead of WhiteMarbles
     */
    @Override
    public void drawMarble(Player[] players, int playerNumber) {
        int chosenMarble=-1;
        if(players[playerNumber].getPlayerboard().getResourceMarbles().length>0)
        {
            if(players[playerNumber].getPlayerboard().getResourceMarbles().length==1)
                players[playerNumber].getPlayerboard().getResourceMarbles()[0].drawMarble(players, playerNumber);
            else {
                while(chosenMarble<0||chosenMarble>1){
                    System.out.println("Which resource do you want to pick? Press 0 for the first res., press 1 for the second res.");
                    Scanner playerInput = new Scanner(System.in);
                    chosenMarble = playerInput.nextInt();
                }
                players[playerNumber].getPlayerboard().getResourceMarbles()[chosenMarble].drawMarble(players, playerNumber);
            }
        }
    }
}