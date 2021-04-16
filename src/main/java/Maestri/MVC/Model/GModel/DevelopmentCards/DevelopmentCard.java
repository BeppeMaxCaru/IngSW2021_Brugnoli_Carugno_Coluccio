package Maestri.MVC.Model.GModel.DevelopmentCards;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.util.HashMap;
import java.util.Map;

public class DevelopmentCard {

    private String colour;
    private int level;
    private Map<String, Integer> cost;
    private Map<String, Integer> input;
    private Map<String, Integer> output;
    private int faithPoints;
    private int victoryPoints;

    public DevelopmentCard(String colour, int level) {
        this.colour = colour;
        this.level = level;
    }

    public DevelopmentCard(String colour, int level,
                           int coinsCost, int stonesCost, int servantsCost, int shieldsCost,
                           int coinsInput, int stonesInput, int servantsInput, int shieldsInput,
                           int coinsOutput, int stonesOutput, int servantsOutput, int shieldsOutput,
                           int faithPoints,
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

    public String getDevelopmentCardColour() {
        return this.colour;
    }

    public int getDevelopmentCardLevel() {
        return this.level;
    }

    public Map<String, Integer> getDevelopmentCardCost() {
        return this.cost;
    }

    public Map<String, Integer> getDevelopmentCardInput() {
        return this.input;
    }

    public Map<String, Integer> getDevelopmentCardOutput() {return this.output; }

    public boolean checkResourcesAvailability(Playerboard playerboard, Map<String, Integer> requirements) {

        Map<String, Integer> warehouseResources = playerboard.getWareHouse().getWarehouseResources();
        Map<String, Integer> chestResources = playerboard.getChest().getChestResources();
        //Sum the two maps to get the total player resources
        Map<String, Integer> allPlayerResources = new HashMap<>(warehouseResources);
        chestResources.forEach((key, value) -> allPlayerResources.merge(key, value, (v1, v2) -> v1+v2));

        //Check if one map is contained into the other one
        //to see if player has enough resources
        for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            int resourceOfPlayer = allPlayerResources.getOrDefault(key,0);
            if (resourceOfPlayer == 0 || resourceOfPlayer<value) {
                //Non si può comprare/scambiare
                System.out.println("Not enough resources to buy this card");
                System.out.println("");
                return false;
            }
        }
        return true;
    }

    public boolean checkPlayerboardDevelopmentCardsCompatibility(Playerboard playerboard) {
        DevelopmentCard[][] playerDevelopmentCards = playerboard.getPlayerboardDevelopmentCards();
        if(this.getDevelopmentCardLevel()==1)
        {
            for(int i=0; i<3; i++)
            {
                if(playerDevelopmentCards[this.getDevelopmentCardLevel()-1][i]==null)
                    return true;
            }
        }
        else
        {
            for(int i=0; i<3; i++)
            {
                if((playerDevelopmentCards[this.getDevelopmentCardLevel()-2][i]!=null)&&
                        (playerDevelopmentCards[this.getDevelopmentCardLevel()-1][i]==null))
                    return true;
            }
        }

        System.out.println("Card not compatible with playerboard cards");
        System.out.println("");
        return false;
    }

    //Metodo che toglie le risorse al player se l'acquisto va a buon fine
    //Check sono già stati fatti tutti da playerboard e carddecksgrid
    //faccio avvenire il pagamento direttamente nella playerboard
    public void payDevelopmentCard(Playerboard playerboard) {

        //Toglie al player le risorse nella mappa
        for (String key : this.cost.keySet()) {
            int resourcesToRemove = this.cost.get(key);
            for (int i=0;i<resourcesToRemove;i++) {
                playerboard.pickResourceToPay(key);
            }
        }
    }

    public void activateProduction(Playerboard playerboard) {

            //Toglie al player le risorse nella mappa
        for (String key : this.input.keySet()) {
            int resourcesToRemove = this.input.get(key);
            for (int i=0;i<resourcesToRemove;i++) {
                playerboard.pickResourceToPay(key);
            }
        }

        playerboard.getFaithPath().moveCross(this.faithPoints);

        //unisco output a chest!
        playerboard.getFaithPath().moveCross(this.faithPoints);
        Map<String, Integer> resourcesAfterProduction = playerboard.getChest().getChestResources();
        this.output.forEach((key, value) -> resourcesAfterProduction.merge(key, value, Integer::sum));
        playerboard.getChest().setChestResources(resourcesAfterProduction);

    }

    public void printDevelopmentCard() {
        //Bisogna allineare
        System.out.println("     ");
        System.out.println("     Colour: " + this.colour);
        System.out.println("     Level:  " + this.colour);
        System.out.println("     Cost:              " + this.cost);
        System.out.println("     Production input:  " + this.input);
        System.out.println("     Production output: " + this.output);
        System.out.println("     Faith points:      " + this.faithPoints);
        System.out.println("     Victory points:    " + this.victoryPoints);
        System.out.println("     ");
    }

}
