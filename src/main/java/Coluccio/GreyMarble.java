package Coluccio;

import Brugnoli.WareHouse;

import java.util.Map;

public class GreyMarble extends Marble{

    public void drawMarble(WareHouse wareHouse) {
        /**
         * Override of the Marble method drawMarble
         */

        Integer numOfResources;
        Map<String, Integer> whResources;
        boolean discard=false;

        whResources=wareHouse.getWarehouseResources();
        numOfResources= whResources.get("STONES");
        /**
         * Import in numOfResources the cardinality of the resource STONES
         * This number can be only 0,1,2,3
         * switch to check warehouse capacity
         */

        switch(numOfResources){
            case 0:
                /**
                 * If there are no STONES in warehouse
                 * If all other resources have cardinality=1, resource has to be discarded
                 */
                if((whResources.get("COINS")==1)&&(whResources.get("SHIELDS")==1)&&(whResources.get("SERVANTS")==1))
                    discard=true;
                break;
            case 1:
                /**
                 * If there is 1 STONES in warehouse
                 * If there are already two resources with cardinality=2, resource has to be discarded
                 */
                if(((whResources.get("COINS")==2)&&(whResources.get("SHIELDS")==2))||
                        ((whResources.get("COINS")==2)&&(whResources.get("SERVANTS")==2))||
                        ((whResources.get("SERVANTS")==2)&&(whResources.get("SHIELDS")==2)))
                    discard=true;
                break;
            case 2:
                /**
                 * If there are 2 STONES in warehouse
                 * If there is already a resource with cardinality=3, resource has to be discarded
                 */
                if((whResources.get("COINS")==3)||(whResources.get("SHIELDS")==3)||(whResources.get("SERVANTS")==3))
                    discard=true;
                break;
            case 3:
                /**
                 * If there are already 3 STONES in warehouse, the resource has to be discarded
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
            whResources.put("STONES", numOfResources);
            wareHouse.setWarehouseResources(whResources);
        }
        else
        {
            /**
             * If the resource has to be discarded, other players obtain 1 faithPoint
             */
            //Aggiungo faithPoints agli altri giocatori
        }

    }
}
