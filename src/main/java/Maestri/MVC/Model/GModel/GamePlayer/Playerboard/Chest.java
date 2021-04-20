package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the resources that players receives from production
 */
public class Chest {

    /**
     * Contains the resources types and their respective quantity
     */
    private Map<String, Integer> chestResources;

    /**
     * Initializes the chest as empty
     */
    public Chest() {
        this.chestResources = new HashMap<>();
        this.chestResources.put("COINS", 0);
        this.chestResources.put("SHIELDS", 0);
        this.chestResources.put("SERVANTS", 0);
        this.chestResources.put("STONES", 0);
    }

    /**
     * Returns the chest's resources
     * @return the chest's resources
     */
    public Map<String, Integer> getChestResources() {
        return this.chestResources;
    }

    /**
     * Sets the chest's resources
     * @param chestResources - resources to set
     */
    public void setChestResources(Map<String, Integer> chestResources) {
        this.chestResources = chestResources;
    }
}
