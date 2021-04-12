package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import java.util.HashMap;
import java.util.Map;

public class WareHouse {

    public Map <String, Integer> warehouseResources;

    public WareHouse() {
        warehouseResources = new HashMap<>();
        warehouseResources.put("COINS", 0);
        warehouseResources.put("SHIELDS", 0);
        warehouseResources.put("SERVANTS", 0);
        warehouseResources.put("STONES", 0);
    }

    public Map <String, Integer> getWarehouseResources() {
        return this.warehouseResources;
    }

    public void setWarehouseResources(Map <String, Integer> warehouseResources) {
        this.warehouseResources = warehouseResources;
    }

    public static boolean checkConstraints(WareHouse wareHouse, String res1) {

        String[] res={"COINS", "SHIELDS", "SERVANTS", "STONES"};
        String[] r;
        r=new String[3];

        int k=0;
        for(int i=0; i<4; i++)
        {
            if(res[i].equals(res1))
            {
                r[k]=res[i];
                k++;
            }
        }

        boolean discard=false;
        Map<String, Integer> whResources=wareHouse.getWarehouseResources();
        Integer numOfResources = whResources.get(res1);

        switch (numOfResources) {
            case 0:
                /*
                 * If there aren't any res1 in warehouse
                 * If all other resources have cardinality=1, resource has to be discarded
                 */
                if ((whResources.get(r[0]) == 1) && (whResources.get(r[1]) == 1) && (whResources.get(r[2]) == 1))
                    discard=true;
                break;
            case 1:
                /*
                 * If there is 1 res1 in warehouse
                 * If there are already two resources with cardinality=2, resource has to be discarded
                 */
                if (((whResources.get(r[0]) == 2) && (whResources.get(r[1]) == 2)) ||
                        ((whResources.get(r[0]) == 2) && (whResources.get(r[2]) == 2)) ||
                        ((whResources.get(r[2]) == 2) && (whResources.get(r[1]) == 2)))
                    discard=true;
                break;
            case 2:
                /*
                 * If there are 2 res2 in warehouse
                 * If there is already a resource with cardinality=3, resource has to be discarded
                 */
                if ((whResources.get(r[0]) == 3) || (whResources.get(r[1]) == 3) || (whResources.get(r[2]) == 3))
                    discard=true;
                break;
            case 3:
                /*
                 * If there are already 3 res2 in warehouse, the resource has to be discarded
                 */
                discard=true;
                break;
            default:
                discard=false;
                break;
        }
        return discard;
    }

}
