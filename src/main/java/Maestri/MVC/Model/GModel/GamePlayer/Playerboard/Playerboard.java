package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

/**
 * Includes all the player's game components
 */
public class Playerboard implements Serializable {

    /**
     * Number of development cards bought
     */
    private int developmentCardsBought;

    public void setPlayerboardDevelopmentCards(DevelopmentCard developmentCard, int pos) {
        this.playerboardDevelopmentCards[developmentCard.getDevelopmentCardLevel()-1][pos] = developmentCard;
    }

    /**
     * Development cards spaces where to place development cards bought
     */
    private DevelopmentCard[][] playerboardDevelopmentCards;

    /**
     * Unlimited resources reserve
     */
    private final Chest chest;

    /**
     * Limited resources reserve
     */
    private final WareHouse wareHouse;

    /**
     * Tracks the player position
     */
    private final FaithPath faithPath;

    /**
     * Victory points collected
     */
    private int victoryPoints;

    /**
     * Optional permanent perk received from leader cards that allows to receive one resource from a white marble
     */
    private final Marble[] resourceMarbles = new Marble[2];

    /**
     * Optional permanent perk received from leader cards that allows to receive a one resource discount to apply while buying a development card
     */
    private final String[] developmentCardDiscount = new String[2];

    /**
     * Optional permanent perk received from leader cards that indicates the input resource for extra production power
     */
    private final String[] extraProductionPowerInput =new String[2];

    /**
     * Initializes the player board
     */
    public Playerboard() {
        this.developmentCardsBought = 0;
        this.playerboardDevelopmentCards = new DevelopmentCard[3][3];
        this.chest = new Chest();
        this.wareHouse = new WareHouse();
        this.faithPath = new FaithPath();
        this.victoryPoints = 0;
    }

    /**
     * This method returns the number of development cards bought by the player
     * @return the number of development cards bought by the player
     */
    public int getDevelopmentCardsBought() {
        return this.developmentCardsBought;
    }

    /**
     * This method returns which Marbles player has to pick instead of WhiteMarbles
     * @return player's available marbles
     */
    public Marble[] getResourceMarbles(){
        return this.resourceMarbles;
    }

    /**
     * Returns the development cards discounts
     * @return the development cards discounts
     */
    public String[] getDevelopmentCardDiscount() {
        return this.developmentCardDiscount;
    }

    /**
     * Returns the extra production power inputs
     * @return the extra production power inputs
     */
    public String[] getExtraProductionPowerInput(){
        return this.extraProductionPowerInput;
    }

    /**
     * Returns the player board's development cards
     * @return the player board's development cards
     */
    public DevelopmentCard[][] getPlayerboardDevelopmentCards() {
        return this.playerboardDevelopmentCards;
    }


    /**
     * Checks that the new development card can be placed on the top of a development card pile
     * @param pile - the chosen development card pile
     * @param developmentCard - the development card to place
     * @return true if the development card can be placed on top
     */
    public boolean isCardBelowCompatible(int pile, DevelopmentCard developmentCard) {

        int i = 0;
        while ((i < 3) && (this.playerboardDevelopmentCards[i][pile] != null)) {
            i++;
        }
        //If the pile is full, card can't be inserted
        if (i == 3)
            return false;
        else {
            //If the first available position of the pile isn't compatible with the dev. card level, the card can't be inserted
            if (developmentCard.getDevelopmentCardLevel() != i + 1)
                return false;
            else
            {
                //If the pile isn't full and the first available position of the pile is compatible with the dev. card level, the card can be inserted
                this.playerboardDevelopmentCards[i][pile]=developmentCard;
                this.developmentCardsBought = this.developmentCardsBought + 1;
                return true;
            }
        }
    }

    /**
     * Returns the chest
     * @return the chest
     */
    public Chest getChest() {
        return this.chest;
    }

    /**
     * Returns the warehouse
     * @return the warehouse
     */
    public WareHouse getWareHouse() {
        return this.wareHouse;
    }

    public void setWareHouse(WareHouse wareHouse) {
    }

    /**
     * Returns the faith path
     * @return the faith path
     */
    public FaithPath getFaithPath() {
        return this.faithPath;
    }

    /**
     * Returns the victory points
     * @return the victory points
     */
    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    /**
     * Sums new victory points to the player board's victory points
     * @param victoryPoints - victory points to add
     */
    public void sumVictoryPoints(int victoryPoints) {
        this.victoryPoints = this.victoryPoints + victoryPoints;
    }

    /**
     * Removes a resource from the warehouse or the chest
     * @param resource - resource to remove
     * @param quantity
     */
    public void pickResource(String resource, String fromWhat, int quantity) {

        Integer whRes = this.wareHouse.getWarehouseResources().get(resource);
        Integer chRes = this.chest.getChestResources().get(resource);
        Integer esRes = this.wareHouse.getWarehouseResources().get("extra" + resource);

        fromWhat = fromWhat.toUpperCase();

        switch(fromWhat)
        {
            case "W":
            {
                this.wareHouse.getWarehouseResources().put(resource, whRes - quantity);
                break;
            }
            case "C":
            {
                this.chest.getChestResources().put(resource, chRes - quantity);
                break;
            }
            case "L":
            {
                this.wareHouse.getWarehouseResources().put("extra"+resource, esRes - quantity);
                break;
            }
        }
    }


    //For testing
    /**
     * Adds a white marble perk to the player
     * @param marble - the marble perk to add
     */
    public void setResourceMarbles(Marble marble){
        int i = 0;
        while((i < this.resourceMarbles.length) && (this.resourceMarbles[i] != null)){
            i++;
        }
        this.resourceMarbles[i] = marble;
    }

    /**
     * Checks if a player has enough resources on its player board to buy the leader card
     * @param requisites - development card's requisites (cost or input)
     * @return true if the player has enough resources to buy the development card
     */
    public boolean checkResourcesAvailability(Map<String, Integer> requisites, PrintWriter out) {
        //Gets the all possible places where the player can store resources
        Map<String, Integer> warehouseResources = this.getWareHouse().getWarehouseResources();
        Map<String, Integer> chestResources = this.getChest().getChestResources();
        //Sum the two maps to get the total player resources
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Needs to be fixed in case of leader card that add extra space
        Map<String, Integer> allPlayerResources = new HashMap<>(warehouseResources);
        chestResources.forEach((key, value) -> allPlayerResources.merge(key, value, (v1, v2) -> v1+v2));

        //Add leader cards resources
        for (String key : allPlayerResources.keySet()) {
            if (allPlayerResources.containsKey("extra"+key)) {
                Integer leaderCardResources = allPlayerResources.get("extra"+key);
                Integer keyResources = allPlayerResources.get(key);
                allPlayerResources.put(key, keyResources+leaderCardResources);
                allPlayerResources.remove("extra"+key);
            }
        }

        //Check if one map is contained into the other one
        //to see if player has enough resources
        for (String key : requisites.keySet()) {
            //Checks if the player has the current required resource type
            /*if (!allPlayerResources.containsKey(key)) {
                out.println("Not enough resources to buy this card");
                out.println();
                return false;
            }*/
            //Checks if the player has enough resources of the current required resource type
            if (allPlayerResources.get(key)<requisites.get(key)) {
                out.println("Not enough resources to buy this card");
                out.println();
                return false;
            }
        }
        //If true the player has enough resource to buy the card
        return true;
    }

    public void printAll(){
        System.out.println("YOUR RESOURCES:");
        System.out.println("COINS   : "+this.getWareHouse().getWarehouseResources().get("COINS")+" in warehouse, "+
                +this.getChest().getChestResources().get("COINS")+" in chest");
        System.out.println("SERVANTS: "+this.getWareHouse().getWarehouseResources().get("SERVANTS")+" in warehouse, "+
                +this.getChest().getChestResources().get("SERVANTS")+" in chest");
        System.out.println("SHIELDS : "+this.getWareHouse().getWarehouseResources().get("SHIELDS")+" in warehouse, "+
                +this.getChest().getChestResources().get("SHIELDS")+" in chest");
        System.out.println("STONES  : "+this.getWareHouse().getWarehouseResources().get("STONES")+" in warehouse, "+
                +this.getChest().getChestResources().get("STONES")+" in chest");
        System.out.println();

        System.out.println("YOUR DEVELOPMENT CARDS: ");
        this.printPlayerCards();
        System.out.println();
        System.out.println("YOUR FAITH PATH POSITION    : "+this.getFaithPath().getCrossPosition());

    }

    public void printPlayerCards()
    {
        int[] upper = new int[3];

        for(int col=0; col<3; col++)
        {
            int row;
            for(row=2; row>0; row--)
                if(this.getPlayerboardDevelopmentCards()[row][col] != null)
                    break;
            if(row==-1)
                row=0;
            upper[col]=row;
        }

        for(int k=0; k<3; k++)
        {
            if(this.getPlayerboardDevelopmentCards()[upper[k]][k]!=null)
                System.out.print("| "+this.getPlayerboardDevelopmentCards()[upper[k]][k].printCardProductionPower()+" ");
            else System.out.print("|                       ");
        }
        System.out.print("|");
        System.out.println();

        for(int k=0; k<3; k++) {
            if (this.getPlayerboardDevelopmentCards()[upper[k]][k] != null) {
                System.out.print("| Victory Points: " + this.getPlayerboardDevelopmentCards()[upper[k]][k].getVictoryPoints() + "    ");
                if (this.getPlayerboardDevelopmentCards()[upper[k]][k].getVictoryPoints() < 10)
                    System.out.print(" ");
            } else System.out.print("|                       ");
        }
        System.out.print("|");
        System.out.println();

    }
}
