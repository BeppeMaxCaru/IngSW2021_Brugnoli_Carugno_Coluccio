package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
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
     * @param chosenMarble
     * @return
     */
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
            if(wlChoice.equals("w"))
            {
                numOfResources++;
                whResources.put("COINS", numOfResources);
                players[playerNumber].getPlayerBoard().getWareHouse().setWarehouseResources(whResources);
            } else {
                numOfExtraRes++;
                whResources.put("extraCOINS", numOfExtraRes);
                players[playerNumber].getPlayerBoard().getWareHouse().setWarehouseResources(whResources);
            }

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
        return false;
    }
    @Override
    public String getColour(){
        return " YELLOW ";
    }
}
