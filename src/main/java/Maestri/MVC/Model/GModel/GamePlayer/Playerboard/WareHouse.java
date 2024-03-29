package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains the resources that players receive from the market
 */
public class WareHouse implements Serializable {

    /**
     * Contains the resources types and their respective quantity
     */
    public Map <String, Integer> warehouseResources;

    /**
     * Initializes the warehouse as empty
     */
    public WareHouse() {
        this.warehouseResources = new HashMap<>();
        this.warehouseResources.put("COINS", 0);
        this.warehouseResources.put("SHIELDS", 0);
        this.warehouseResources.put("SERVANTS", 0);
        this.warehouseResources.put("STONES", 0);
    }

    /**
     * Returns the warehouse's resources
     * @return the warehouse's resources
     */
    public Map <String, Integer> getWarehouseResources() {
        return this.warehouseResources;
    }

    /**
     * Sets the warehouse's resources
     * @param warehouseResources the resources to set
     */
    public void setWarehouseResources(Map <String, Integer> warehouseResources) {
        this.warehouseResources = warehouseResources;
    }

    /**
     * Checks that adding a new resource doesn't violate warehouse limit
     * @param newResource resource to add
     * @param wlChoice indicates if player wants to store the resource in warehouse or in extra space
     * @return true the resource is discarded instead of collected
     */
    public boolean checkConstraints(String newResource, String wlChoice) {

        String[] res={"COINS", "SHIELDS", "SERVANTS", "STONES"};
        String[] r;
        r = new String[3];
        int k = 0;

        for(int i=0; i<4; i++)
        {
            if(!res[i].equals(newResource))
            {
                r[k] = res[i];
                k++;
            }
        }
        String extraRes="extra"+newResource;

        boolean discard=false;

        Integer numOfResources = this.warehouseResources.get(newResource);

        //if LeaderCard extra space is available
        if(wlChoice.equals("L")) {
            if (this.warehouseResources.get((extraRes)) == null)
                return true;
            else if (this.warehouseResources.get((extraRes)) >= 2)
                return true;

        } else {
            switch (numOfResources) {
                case 0:
                /*
                 If there aren't any res1 in warehouse
                 If all other resources have cardinality=1, resource has to be discarded
                 */
                    if ((this.warehouseResources.get(r[0]) >= 1) && (this.warehouseResources.get(r[1]) >= 1) && (this.warehouseResources.get(r[2]) >= 1))
                        discard=true;
                    break;
                case 1:
                /*
                 If there is 1 res1 in warehouse
                 If there are already two resources with cardinality=2, resource has to be discarded
                 */
                    if (((this.warehouseResources.get(r[0]) >= 2) && (this.warehouseResources.get(r[1]) >= 2)) ||
                            ((this.warehouseResources.get(r[0]) >= 2) && (this.warehouseResources.get(r[2]) >= 2)) ||
                            ((this.warehouseResources.get(r[2]) >= 2) && (this.warehouseResources.get(r[1]) >= 2)))
                        discard=true;
                    break;
                case 2:
                /*
                 If there are 2 res2 in warehouse
                 If there is already a resource with cardinality=3, resource has to be discarded
                 */
                    if ((this.warehouseResources.get(r[0]) >= 3) || (this.warehouseResources.get(r[1]) >= 3) || (this.warehouseResources.get(r[2]) >= 3))
                        discard=true;
                    break;
                case 3:
                    //If there are already 3 res2 in warehouse, the resource has to be discarded
                    discard=true;
                    break;
            }
        }

        return discard;
    }

}
