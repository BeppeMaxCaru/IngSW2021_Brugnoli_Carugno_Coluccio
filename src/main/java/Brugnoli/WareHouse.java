package Brugnoli;

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

    }

    public void checkConstraints(Map <String, Integer> warehouseResources) {

    }
}
