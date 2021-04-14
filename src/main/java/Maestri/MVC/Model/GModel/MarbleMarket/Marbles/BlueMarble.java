package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.WareHouse;

import java.util.Map;

/**
 * BlueMarbles produce SHIELDS
 */
public class BlueMarble extends Marble {

    /**
     *After checking the warehouse capacity, this method adds SHIELDS to warehouse or discards the marble and add faithPoints to other players
     */
    @Override
    public void drawMarble(Player[] players, int playerNumber) {

        Map<String, Integer> whResources;
        whResources=players[playerNumber].getPlayerboard().getWareHouse().getWarehouseResources();
        Integer numOfResources = whResources.get("SHIELDS");
        boolean discard;

        discard=WareHouse.checkConstraints(players[playerNumber].getPlayerboard().getWareHouse(),"SHIELDS");
        //Calling the Warehouse method for checking the warehouse capacity

        if(!discard)
        {
            //If the resource hasn't to be discarded it is increased in warehouse
            numOfResources++;
            whResources.put("SHIELDS", numOfResources);
            players[playerNumber].getPlayerboard().getWareHouse().setWarehouseResources(whResources);
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
                    p.getPlayerboard().getFaithPath().moveCross(1);
            }
        }
    }
}