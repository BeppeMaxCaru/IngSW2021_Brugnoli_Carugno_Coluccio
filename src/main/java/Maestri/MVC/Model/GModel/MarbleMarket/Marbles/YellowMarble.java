package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.io.Serializable;
import java.util.Map;

/**
 * This yellow marble produces COINS
 */
public class YellowMarble extends Marble implements Serializable {

    /**
     * Initializes the yellow marble
     */
    public YellowMarble() {
        super(" YELLOW ");
    }

    @Override
    public Boolean drawMarble(Player[] players, int playerNumber, String wlChoice, String chosenMarble) {

        Map<String, Integer> whResources;
        whResources=players[playerNumber].getPlayerBoard().getWareHouse().getWarehouseResources();
        Integer numOfResources = whResources.get("COINS");
        Integer numOfExtraRes = whResources.get("extraCOINS");
        boolean discard;

        //Calling the Warehouse method for checking the warehouse capacity
        discard= players[playerNumber].getPlayerBoard().getWareHouse().checkConstraints("COINS", wlChoice);

        if(!discard)
        {
            //If the resource hasn't to be discarded it is increased in warehouse
            if(wlChoice.equals("W"))
            {
                numOfResources++;
                whResources.put("COINS", numOfResources);
            } else {
                numOfExtraRes++;
                whResources.put("extraCOINS", numOfExtraRes);
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
