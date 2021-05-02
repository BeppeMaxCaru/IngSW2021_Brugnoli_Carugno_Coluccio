package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Contains the resources that players receive from the market
 */
public class WareHouse {

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
     * @param warehouseResources - the resources to set
     */
    public void setWarehouseResources(Map <String, Integer> warehouseResources) {
        this.warehouseResources = warehouseResources;
    }

    /**
     * Checks that adding a new resource doesn't violate warehouse limit
     * @param newResource - resource to add
     * @return true the resource is discarded instead of collected
     */
    public boolean checkConstraints(String newResource, Scanner in, PrintWriter out) {

        String[] res={"COINS", "SHIELDS", "SERVANTS", "STONES"};
        String[] r;
        r=new String[3];
        int k=0;
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
        Integer extraSpace = this.warehouseResources.get(extraRes);

        int resourceChoice;

        //if LeaderCard extra space is available
        if(this.warehouseResources.get((extraRes))!=null)
        {
            do {
                out.println("Where do you go to collect this resource? Insert 0 for LeaderCard, 1 for Warehouse");
                resourceChoice = in.nextInt();
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
        return discard;
    }

}
