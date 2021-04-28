package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.WareHouse;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.util.Map;

/**
 * This yellow marble produces COINS
 */
public class YellowMarble extends Marble {

    /**
     * If the drawing player has enough space in its warehouse it receives one coin otherwise other players receive one faith point
     * @param players - players playing the game
     * @param playerNumber - number of the current player
     */
    @Override
    public void drawMarble(Player[] players, int playerNumber) {

        Integer numOfResources = players[playerNumber].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS");
        boolean discard;

        discard= players[playerNumber].getPlayerBoard().getWareHouse().checkConstraints("COINS");
        //Calling the Warehouse method for checking the warehouse capacity
        System.out.println("You picked: "+this.getClass());
        if(!discard)
        {
            //If the resource hasn't to be discarded it is increased in warehouse
            numOfResources++;
            players[playerNumber].getPlayerBoard().getWareHouse().getWarehouseResources().put("COINS", numOfResources);
            players[playerNumber].getPlayerBoard().getWareHouse().setWarehouseResources(players[playerNumber].getPlayerBoard().getWareHouse().getWarehouseResources());
        }
        else
        {
            //If the resource has to be discarded, other players obtain 1 faithPoint
            for(Player p : players)
            {
                /*
                 for-each player in the game
                 If he isn't the one who discards the marble, he obtains 1 faithPoint
                 */
                if(playerNumber!= p.getPlayerNumber())
                    p.getPlayerBoard().getFaithPath().moveCross(1);
            }
        }
    }
}
