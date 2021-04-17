package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import java.util.HashMap;
import java.util.Map;

public class Chest {
    /**
     * A map where you can find resources and the relative quantity in the chest.
     */
    private Map<String, Integer> chestResources;

    public Chest() {
        chestResources = new HashMap<>();
        chestResources.put("COINS", 0);
        chestResources.put("SHIELDS", 0);
        chestResources.put("SERVANTS", 0);
        chestResources.put("STONES", 0);
    }

    /**
     * @return a map where you can find resources and the relative quantity in the chest.
     */

    public Map<String, Integer> getChestResources() {
        return chestResources;
    }

    /**
     * This method sets the chest's resources.
     * @param chestResources a map where you can find resources and the relative quantity in the chest.
     */
    public void setChestResources(Map<String, Integer> chestResources) {

    }
}
