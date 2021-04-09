package Brugnoli;

import java.util.HashMap;
import java.util.Map;

public class WareHouse {

    private Map <String, Integer> warehouseResources;

    public WareHouse() {
        warehouseResources = new HashMap<>();
    }

    public Map <String, Integer> getWarehouseResources() {
        return warehouseResources;
    }

    public void setWarehouseResources(Map <String, Integer> warehouseResources) {

    }

    public void checkConstraints(Map <String, Integer> warehouseResources) {

    }
}
