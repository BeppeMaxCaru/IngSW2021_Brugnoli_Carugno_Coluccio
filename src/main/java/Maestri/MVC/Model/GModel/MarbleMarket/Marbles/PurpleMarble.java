package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.io.Serializable;
import java.util.Map;

/**
 * This purple marble produces SERVANTS
 */
public class PurpleMarble extends Marble implements Serializable {

    /**
     * Initializes the purple marble
     */
    public PurpleMarble() {
        super(" PURPLE ");
    }

    @Override
    public Boolean drawMarble(Player[] players, int playerNumber, String wlChoice, String chosenMarble) {

        Map<String, Integer> whResources;
        whResources=players[playerNumber].getPlayerBoard().getWareHouse().getWarehouseResources();
        Integer numOfResources = whResources.get("SERVANTS");
        Integer numOfExtraRes = whResources.get("extraSERVANTS");
        boolean discard;

        //Calling the Warehouse method for checking the warehouse capacity
        discard= players[playerNumber].getPlayerBoard().getWareHouse().checkConstraints("SERVANTS", wlChoice);

        if(!discard)
        {
            //If the resource hasn't to be discarded it is increased in warehouse
            if(wlChoice.equals("W"))
            {
                numOfResources++;
                whResources.put("SERVANTS", numOfResources);
            } else {
                numOfExtraRes++;
                whResources.put("extraSERVANTS", numOfExtraRes);
            }
            players[playerNumber].getPlayerBoard().getWareHouse().setWarehouseResources(whResources);

        }
        else
        {
            //If the resource has to be discarded, other players obtain 1 faithPoint
            for(Player p : players)
            {
                if(p==null)
                    break;
                /*
                 for-each player in the game
                 If he isn't the one who discards the marble, he obtains 1 faithPoint
                 */
                if(playerNumber!= p.getPlayerNumber())
                    p.getPlayerBoard().getFaithPath().moveCross(1);
            }
        }
        return false;
    }
}