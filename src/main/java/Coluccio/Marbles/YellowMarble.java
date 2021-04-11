package Coluccio.Marbles;

import Brugnoli.Player;
import Brugnoli.WareHouse;
import Carugno.MVC.GameModel;
import Coluccio.Marble;

import java.util.Map;

/**
 * YellowMarbles produce COINS
 */
public class YellowMarble extends Marble {

    @Override
    public void drawMarble(GameModel gameModel, Player player) {
        /**
         * Override of the Marble method drawMarble
         */

        Map<String, Integer> whResources=player.getPlayerboard().getWareHouse().getWarehouseResources();
        Integer numOfResources = whResources.get("COINS");
        /**
         * Saving cardinality of COINS in a temporary integer
         */

        boolean discard;
        discard=WareHouse.checkConstraints(player.getPlayerboard().getWareHouse(), "COINS", "STONES", "SERVANTS", "SHIELDS");
        /**
         * Calling the Warehouse method for checking the warehouse capacity
         */

        if(!discard)
        {
            /**
             * If the resource hasn't to be discarded it is increased in warehouse
             */
            numOfResources++;
            whResources.put("COINS", numOfResources);
            player.getPlayerboard().getWareHouse().setWarehouseResources(whResources);
        }
        else
        {
            /**
             * If the resource has to be discarded, other players obtain 1 faithPoint
             * pn contains the playerNumber of the player that discards the marble
             */
            int pn = player.getPlayerNumber();

            for(Player players : gameModel.getPlayers())
            {
                /**
                 * for-each player in the game
                 * If he isn't the one who discards the marble, he obtains 1 faithPoint
                 */
                if(pn!= players.getPlayerNumber())
                    players.getPlayerboard().getFaithPath().moveCross(1);
            }
        }
    }
}
