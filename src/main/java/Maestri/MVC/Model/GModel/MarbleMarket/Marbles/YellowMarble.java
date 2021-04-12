package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.WareHouse;
import Maestri.MVC.Model.GModel.GameModel;

import java.util.Map;

/**
 * YellowMarbles produce COINS
 */
public class YellowMarble extends Marble {

    @Override
    public void drawMarble(GameModel gameModel, Player player) {

        Map<String, Integer> whResources=player.getPlayerboard().getWareHouse().getWarehouseResources();
        Integer numOfResources = whResources.get("COINS");
        /*
         * Saving cardinality of COINS in a temporary integer
         */

        boolean discard;
        discard=WareHouse.checkConstraints(player.getPlayerboard().getWareHouse(), "COINS");
        /*
         * Calling the Warehouse method for checking the warehouse capacity
         */

        if(!discard)
        {
            /*
             * If the resource hasn't to be discarded it is increased in warehouse
             */
            numOfResources++;
            whResources.put("COINS", numOfResources);
            player.getPlayerboard().getWareHouse().setWarehouseResources(whResources);
        }
        else
        {
            /*
             * If the resource has to be discarded, other players obtain 1 faithPoint
             * pn contains the playerNumber of the player that discards the marble
             */
            int pn = player.getPlayerNumber();

            for(Player players : gameModel.getPlayers())
            {
                /*
                 * for-each player in the game
                 * If he isn't the one who discards the marble, he obtains 1 faithPoint
                 */
                if(pn!= players.getPlayerNumber())
                    players.getPlayerboard().getFaithPath().moveCross(1);
            }
        }
    }
}
