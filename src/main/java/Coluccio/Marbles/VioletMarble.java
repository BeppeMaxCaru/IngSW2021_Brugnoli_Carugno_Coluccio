package Coluccio.Marbles;

import Brugnoli.FaithPath;
import Brugnoli.Player;
import Brugnoli.Playerboard;
import Brugnoli.WareHouse;
import Carugno.MVC.GameModel;
import Coluccio.Marble;

import java.util.Map;

public class VioletMarble extends Marble {

    public void drawMarble(WareHouse wareHouse, GameModel gameModel, Player player) {
        /**
         * Override of the Marble method drawMarble
         */

        Integer numOfResources;
        Map<String, Integer> whResources;
        boolean discard=false;

        whResources=wareHouse.getWarehouseResources();
        numOfResources= whResources.get("SERVANTS");
        /**
         * Import in numOfResources the cardinality of the resource SERVANTS
         * This number can be only 0,1,2,3
         * switch to check warehouse capacity
         */

        switch(numOfResources){
            case 0:
                /**
                 * If there are no SERVANTS in warehouse
                 * If all other resources have cardinality=1, resource has to be discarded
                 */
                if((whResources.get("COINS")==1)&&(whResources.get("SHIELDS")==1)&&(whResources.get("STONES")==1))
                    discard=true;
                break;
            case 1:
                /**
                 * If there is 1 SERVANTS in warehouse
                 * If there are already two resources with cardinality=2, resource has to be discarded
                 */
                if(((whResources.get("COINS")==2)&&(whResources.get("SHIELDS")==2))||
                        ((whResources.get("COINS")==2)&&(whResources.get("STONES")==2))||
                        ((whResources.get("STONES")==2)&&(whResources.get("SHIELDS")==2)))
                    discard=true;
                break;
            case 2:
                /**
                 * If there are 2 SERVANTS in warehouse
                 * If there is already a resource with cardinality=3, resource has to be discarded
                 */
                if((whResources.get("COINS")==3)||(whResources.get("SHIELDS")==3)||(whResources.get("STONES")==3))
                    discard=true;
                break;
            case 3:
                /**
                 * If there are already 3 SERVANTS in warehouse, the resource has to be discarded
                 */
                discard=true;
                break;
            default:
                discard=false;
                break;
        }

        if(!discard)
        {
            /**
             * If the resource hasn't to be discarded it is increased in warehouse
             */
            numOfResources++;
            whResources.put("SERVANTS", numOfResources);
            wareHouse.setWarehouseResources(whResources);
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