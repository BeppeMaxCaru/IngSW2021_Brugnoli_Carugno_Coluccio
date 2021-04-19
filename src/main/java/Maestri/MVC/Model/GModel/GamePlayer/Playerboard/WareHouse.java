package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WareHouse {
    /**
     * A map where you can find resources and the relative quantity in the warehouse.
     */
    public Map <String, Integer> warehouseResources;

    public WareHouse() {
        this.warehouseResources = new HashMap<>();
        this.warehouseResources.put("COINS", 0);
        this.warehouseResources.put("SHIELDS", 0);
        this.warehouseResources.put("SERVANTS", 0);
        this.warehouseResources.put("STONES", 0);
    }

    /**
     * @return a map where you can find resources and the relative quantity in the warehouse.
     */

    public Map <String, Integer> getWarehouseResources() {
        return this.warehouseResources;
    }

    /**
     * this method sets the warehouse's resources.
     * @param warehouseResources a map where you can find resources and the relative quantity in the warehouse
     */

    public void setWarehouseResources(Map <String, Integer> warehouseResources) {
        this.warehouseResources = warehouseResources;
    }

    /**
     * this method checks if the player can collect resources or if he has to discard them and to give other players faithPoints
     * @param res1 resource to be checked
     * @return true if the resource has to be discarded, false if the player can collect it
     */
    public boolean checkConstraints(String res1) {

        String[] res={"COINS", "SHIELDS", "SERVANTS", "STONES"};
        String[] r;
        r=new String[3];
        int k=0;
        for(int i=0; i<4; i++)
        {
            if(!res[i].equals(res1))
            {
                r[k] = res[i];
                k++;
            }
        }
        String extraRes="extra"+res1;

        boolean discard=false;

        Integer numOfResources = this.warehouseResources.get(res1);
        Integer extraSpace = this.warehouseResources.get(extraRes);

        Scanner keyboard = new Scanner(System.in);
        int resourceChoice;

        //if LeaderCard extra space is available
        if(this.warehouseResources.get((extraRes))!=null)
        {
            do {
                System.out.println("Where do you go to collect this resource? Insert 0 for LeaderCard, 1 for Warehouse");
                resourceChoice = keyboard.nextInt();
                if(resourceChoice==0)
                {
                    if(extraSpace==2)
                        //If the users wants to store resource in full location, it has to be discarded
                        discard=true;
                    else
                    //If the location isn't full, the resource hasn't to be discarded
                    {
                        extraSpace++;
                        this.warehouseResources.put(extraRes, extraSpace);
                    }
                }
            }while((resourceChoice < 0) || (resourceChoice > 1));
        }

        switch (numOfResources) {
            case 0:
                /*
                 If there aren't any res1 in warehouse
                 If all other resources have cardinality=1, resource has to be discarded
                 */
                if ((this.warehouseResources.get(r[0]) == 1) && (this.warehouseResources.get(r[1]) == 1) && (this.warehouseResources.get(r[2]) == 1))
                    discard=true;
                break;
            case 1:
                /*
                 If there is 1 res1 in warehouse
                 If there are already two resources with cardinality=2, resource has to be discarded
                 */
                if (((this.warehouseResources.get(r[0]) == 2) && (this.warehouseResources.get(r[1]) == 2)) ||
                        ((this.warehouseResources.get(r[0]) == 2) && (this.warehouseResources.get(r[2]) == 2)) ||
                        ((this.warehouseResources.get(r[2]) == 2) && (this.warehouseResources.get(r[1]) == 2)))
                    discard=true;
                break;
            case 2:
                /*
                 If there are 2 res2 in warehouse
                 If there is already a resource with cardinality=3, resource has to be discarded
                 */
                if ((this.warehouseResources.get(r[0]) == 3) || (this.warehouseResources.get(r[1]) == 3) || (this.warehouseResources.get(r[2]) == 3))
                    discard=true;
                break;
            case 3:
                //If there are already 3 res2 in warehouse, the resource has to be discarded
                discard=true;
                break;
        }
        return discard;
    }

}
