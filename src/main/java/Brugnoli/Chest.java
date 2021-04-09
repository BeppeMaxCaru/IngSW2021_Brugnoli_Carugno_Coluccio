package Brugnoli;

import java.util.HashMap;
import java.util.Map;

public class Chest {

    private Map<String, Integer> chestResources;

    public Chest() {
        chestResources = new HashMap<>();
    }

    public Map<String, Integer> getChestResources() {
        return chestResources;
    }

    public void setChestResources(Map<String, Integer> chestResources) {

    }
}
