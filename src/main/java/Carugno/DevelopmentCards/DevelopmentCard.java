package Carugno.DevelopmentCards;

import Brugnoli.Playerboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a single development card that can be personalized through the constructor
 */
public class DevelopmentCard {

    private String colour;
    private int level;
    private Map<String, Integer> cost;
    private Map<String, Integer> input;
    private Map<String, Integer> output;
    private int victoryPoints;

    /**
     * This constructor builds the personalized development card
     */
    public DevelopmentCard(String colour, int level,
                           int coinsCost, int stonesCost, int servantsCost, int shieldsCost,
                           int coinsInput, int stonesInput, int servantsInput, int shieldsInput,
                           int coinsOutput, int stonesOutput, int servantsOutput, int shieldsOutput,
                           int faithOutput,
                           int victoryPoints) {

        this.cost = new HashMap<String, Integer>();
        this.cost.put("COINS", coinsCost);
        this.cost.put("STONES", stonesCost);
        this.cost.put("SERVANTS", servantsCost);
        this.cost.put("SHIELDS", shieldsCost);

        this.input = new HashMap<String, Integer>();
        this.input.put("COINS", coinsInput);
        this.input.put("STONES", stonesInput);
        this.input.put("SERVANTS", servantsInput);
        this.input.put("SHIELDS", shieldsInput);

        this.output = new HashMap<String, Integer>();
        this.output.put("COINS", coinsOutput);
        this.output.put("STONES", stonesOutput);
        this.output.put("SERVANTS", servantsOutput);
        this.output.put("SHIELDS", shieldsOutput);

    }

    public void setDevelopmentCard (int coinsCost, int stonesCost, int servantsCost, int shieldsCost,
                                    int coinsOutput, int stonesOutput, int servantsOutput, int shieldsOutput,
                                    int faithOutput,
                                    int victoryPoints) {

        this.cost = new HashMap<String, Integer>();
        this.cost.put("COINS", coinsCost);
        this.cost.put("STONES", stonesCost);
        this.cost.put("SERVANTS", servantsCost);
        this.cost.put("SHIELDS", shieldsCost);

        this.input = new HashMap<String, Integer>();
        this.input.put("COINS", coinsInput);
        this.input.put("STONES", stonesInput);
        this.input.put("SERVANTS", servantsInput);
        this.input.put("SHIELDS", shieldsInput);

        this.output = new HashMap<String, Integer>();
        this.output.put("COINS", coinsOutput);
        this.output.put("STONES", stonesOutput);
        this.output.put("SERVANTS", servantsOutput);
        this.output.put("SHIELDS", shieldsOutput);

    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    /**
     * This method checks if the player has enough resources in both his warehouse and chest
     * to cover the cost of the card and buy it
     * @param playerboard
     * @return
     */
    public boolean checkResources(Playerboard playerboard) {
        //Get resources in chest e warehouse
        Map<String, Integer> warehouseResources = playerboard.getWareHouse().getWarehouseResources();
        Map<String, Integer> chestResources = playerboard.getChest().getChestResources();

        //Sum the two maps to get the total player resources
        Map<String, Integer> playerResources = new HashMap<>(warehouseResources);
        for (Map.Entry<String, Integer> entry : chestResources.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            playerResources.merge(key, value, (v1, v2) -> v1 + v2);
        }

        //Check if one map is contained into the other one
        for (Map.Entry<String, Integer> entry : this.cost.entrySet()) {
            String key = entry.getKey();
        }



    }

    /**
     * This method remove the resources needed to buy the card from the player
     */
    public void buy() {
        //Forse si possono unire checkResources e buy effettuando il controllo nello stesso metodo
    }

}
