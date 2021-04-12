package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import java.util.HashMap;
import java.util.Map;

public class Chest {

    private Map<String, Integer> chestResources;

    public Chest() {
        chestResources = new HashMap<>();
        chestResources.put("COINS", 0);
        chestResources.put("SHIELDS", 0);
        chestResources.put("SERVANTS", 0);
        chestResources.put("STONES", 0);
    }

    public Map<String, Integer> getChestResources() {
        return chestResources;
    }

    public void setChestResources(Map<String, Integer> chestResources) {

    }
}
