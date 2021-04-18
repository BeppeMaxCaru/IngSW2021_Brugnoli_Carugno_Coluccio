package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.WareHouse;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.util.Map;

/**
 * This purple marble produces SERVANTS
 */
public class PurpleMarble extends Marble {

    /**
     * If the drawing player has enough space in its warehouse it receives one servant otherwise other players receive one faith point
     * @param players - players playing the game
     * @param playerNumber - number of the current player
     */
    @Override
    public void drawMarble(Player[] players, int playerNumber) {

        Map<String, Integer> whResources=players[playerNumber].getPlayerBoard().getWareHouse().getWarehouseResources();
        Integer numOfResources = whResources.get("SERVANTS");
        boolean discard;

        discard=WareHouse.checkConstraints(players[playerNumber].getPlayerBoard().getWareHouse(), "SERVANTS");
        //Calling the Warehouse method for checking the warehouse capacity

        if(!discard)
        {
            //If the resource hasn't to be discarded it is increased in warehouse
            numOfResources++;
            whResources.put("SERVANTS", numOfResources);
            players[playerNumber].getPlayerBoard().getWareHouse().setWarehouseResources(whResources);
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