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
    //Auxiliary variable
    //private Map<String, Integer> warehouseResources;
    //private Map<String, Integer> chestResources;
    //private Map<String, Integer> playerResources;
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

    /*public void setDevelopmentCard (int coinsCost, int stonesCost, int servantsCost, int shieldsCost,
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

    }*/

    public String getColour() {
        return this.colour;
    }

    public int getLevel() {
        return this.level;
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
    //Posso fare controllo direttamente nel metodo senza checkRequisite
    //se si può compra altrimenti niente
    public boolean checkCost(Playerboard playerboard) {
        //Get resources in chest e warehouse
        this.warehouseResources = playerboard.getWareHouse().getWarehouseResources();
        this.chestResources = playerboard.getChest().getChestResources();

        //Sum the two maps to get the total player resources
        this.playerResources = new HashMap<>(this.warehouseResources);
        for (Map.Entry<String, Integer> entry : this.chestResources.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            this.playerResources.merge(key, value, (v1, v2) -> v1 + v2);
        }

        //Check if one map is contained into the other one
        //to see if player has enough resources
        for (Map.Entry<String, Integer> entry : this.cost.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            //if (this.cost.entrySet().contains(key)) {
            //    if (value<this.cost.get)
            //}
            int resourceOfPlayer = this.playerResources.getOrDefault(key,0);
            if (resourceOfPlayer == 0 || resourceOfPlayer<value) {
                //Non si può comprare
                //Println("Risorse non sufficienti");
                //break;
                return false;
            }
        }

        return true;

    }

    /**
     * This method remove the resources needed to buy the card from the player
     */
    public void buy(Playerboard playerboard) {
        //Forse si possono unire checkResources e buy effettuando il controllo nello stesso metodo
        this.warehouseResources = playerboard.getWareHouse().getWarehouseResources();
        this.chestResources = playerboard.getChest().getChestResources();

        for (Map.Entry<String, Integer> warehouseResourceAvailability : this.warehouseResources.entrySet()) {
            String warehouseResourceAvailabilityKey = warehouseResourceAvailability.getKey();
            Integer warehouseResourceAvailabilityValue = warehouseResourceAvailability.getValue();
            //controlla se vado sotto zero in warehouse allora devo togliere da chest
            warehouseResourceAvailabilityValue = warehouseResourceAvailabilityValue - this.cost.get(warehouseResourceAvailabilityKey);
            if (warehouseResourceAvailabilityValue<0) {
                Integer toReduceFromChest = Math.abs(warehouseResourceAvailabilityValue);
                this.warehouseResources.put(warehouseResourceAvailabilityKey, 0);
                this.chestResources.put(warehouseResourceAvailabilityKey, toReduceFromChest);

            } else {
                //Fa overwrite con nuovo valore
                this.warehouseResources.put(warehouseResourceAvailabilityKey, warehouseResourceAvailabilityValue);
            }
        }

        playerboard.getWareHouse().setWarehouseResources(this.warehouseResources);
        playerboard.getChest().setChestResources(this.chestResources);

        int v = playerboard.getVictoryPoints();
        v = v + this.victoryPoints;
        playerboard.setVictoryPoints(v);

    }

    public boolean checkProduction(Playerboard playerboard) {

        //Get resources in chest e warehouse
        Map<String, Integer> warehouseResources = playerboard.getWareHouse().getWarehouseResources();
        Map<String, Integer> chestResources = playerboard.getChest().getChestResources();

        //Sum the two maps to get the total player resources
        this.playerResources = new HashMap<>(this.warehouseResources);
        for (Map.Entry<String, Integer> entry : this.chestResources.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            this.playerResources.merge(key, value, (v1, v2) -> v1 + v2);
        }

        //Check if one map is contained into the other one
        //to see if player has enough resources
        for (Map.Entry<String, Integer> entry : this.input.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            //if (this.cost.entrySet().contains(key)) {
            //    if (value<this.cost.get)
            //}
            int resourceOfPlayer = this.playerResources.getOrDefault(key,0);
            if (resourceOfPlayer == 0 || resourceOfPlayer<value) {
                //Non si può comprare
                //Println("Risorse non sufficienti");
                //break;
                return false;
            }
        }

        return true;

    }

    //OFFICIAL

    //Sums warehouse map and chest map and confronts them with a third map
    public boolean checkResourcesAvailability(Playerboard playerboard, Map<String, Integer> requirements) {

        Map<String, Integer> warehouseResources = playerboard.getWareHouse().getWarehouseResources();
        Map<String, Integer> chestResources = playerboard.getChest().getChestResources();
        //Sum the two maps to get the total player resources
        Map<String, Integer> allPlayerResources = new HashMap<>(warehouseResources);
        for (Map.Entry<String, Integer> entry : chestResources.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            allPlayerResources.merge(key, value, (v1, v2) -> v1 + v2);
        }

        //Check if one map is contained into the other one
        //to see if player has enough resources
        for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            int resourceOfPlayer = allPlayerResources.getOrDefault(key,0);
            if (resourceOfPlayer == 0 || resourceOfPlayer<value) {
                //Non si può comprare
                System.out.println("Not enough resources");
                //break;
                return false;
            }
        }
        return true;
    }

    public boolean activateProduction(Playerboard playerboard,
                                   Map<String, Integer> input,
                                   Map<String, Integer> output) {

        if (!checkResourcesAvailability(playerboard, this.input)) {
            System.out.println("Not enough resources to activate production");
            return false;
        } else {
            Map<String, Integer> resourcesChoosenFromPlayer = new HashMap<>();
        }

    }

}
