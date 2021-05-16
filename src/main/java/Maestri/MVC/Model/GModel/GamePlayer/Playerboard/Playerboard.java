package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.io.PrintWriter;
import java.util.*;

/**
 * Includes all the player's game components
 */
public class Playerboard {

    /**
     * Number of development cards bought
     */
    private int developmentCardsBought;

    /**
     * Development cards spaces where to place development cards bought
     */
    private final DevelopmentCard[][] playerboardDevelopmentCards;

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
     * Places a development card on the player board
     * @param developmentCard - the development card to place
     */
    public void placeNewDevelopmentCard(DevelopmentCard developmentCard, int position, PrintWriter out) {

        //Checks the chosen space
        if(!this.isCardBelowCompatible(position, developmentCard)) {
            out.println("Placement not possible!");
        }
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
     */
    public void pickResource(String resource, String fromWhat, PrintWriter out) {

        Integer whRes = this.wareHouse.getWarehouseResources().get(resource);
        Integer esRes = this.wareHouse.getWarehouseResources().get("extra" + resource);
        Integer numResources;
        int i = 1;


        while(i > 0) {
            if (!fromWhat.equals("C")) {
                numResources = 0;
                numResources = numResources + whRes;
                if(esRes!=null)
                    numResources=numResources + esRes;
                if (numResources != 0) {
                    if ((whRes != 0) && (esRes == null || esRes == 0))
                            this.wareHouse.getWarehouseResources().put(resource, whRes - 1);
                    else if (whRes == 0)
                        this.wareHouse.getWarehouseResources().put("extra" + resource, esRes - 1);
                    else {
                        if (fromWhat.equals("W"))
                            this.wareHouse.getWarehouseResources().put(resource, whRes - 1);
                        else
                            this.wareHouse.getWarehouseResources().put("extra" + resource, esRes - 1);
                    }
                    i = 0;
                }
                else {
                    out.println("You have run out of" + resource + "in the warehouse, pick the others from the chest:");
                    fromWhat = "1";
                }
            }
            else {
                numResources = this.chest.getChestResources().get(resource);
                if (numResources != 0) {
                    this.chest.getChestResources().put(resource, numResources - 1);
                    i = 0;
                }
                else {
                    out.println("You have run out of" + resource + "in the chest, pick the others from the warehouse:");
                    fromWhat = "0";
                }
            }
        }
    }

    /**
     * Activates the basic production power of the player board
     * @return a list with the first two resources to discard and the third one to receive
     */
    public List<String> activateBasicProductionPower(Scanner in, PrintWriter out) {
        String resourceOutputNum;
        int i;
        String resourceInputNum;
        List<String> resourceChoice = new ArrayList<>();

        // Two input
        for (i = 0; i < 2; i++) {
            out.println("Choose one resource for pay: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
            resourceInputNum = in.nextLine();
            while(!resourceInputNum.equals("0") && !resourceInputNum.equals("1") && !resourceInputNum.equals("2") && !resourceInputNum.equals("3")) {
                out.println("Number not valid!");
                out.println("Choose one resource to pay: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
                resourceInputNum = in.nextLine();
            }
            switch (resourceInputNum) {
                case "0":
                    resourceChoice.add("COINS");
                    break;
                case "1":
                    resourceChoice.add("SHIELDS");
                    break;
                case "2":
                    resourceChoice.add("SERVANTS");
                    break;
                case "3":
                    resourceChoice.add("STONES");
                    break;
            }
        }


        // output
        out.println("Choose one resource to pick: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES, 4 for REDCROSS");
        resourceOutputNum = in.nextLine();
        while(!resourceOutputNum.equals("0") && !resourceOutputNum.equals("1") && !resourceOutputNum.equals("2") && !resourceOutputNum.equals("3")) {
            out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES, 4 for REDCROSS");
            resourceOutputNum = in.nextLine();
        }

        switch (resourceOutputNum) {
            case "0":
                resourceChoice.add("COINS");
                break;
            case "1":
                resourceChoice.add("SHIELDS");
                break;
            case "2":
                resourceChoice.add("SERVANTS");
                break;
            case "3":
                resourceChoice.add("STONES");
                break;
            default:
                resourceChoice.add("REDCROSS");
                break;
        }

        return resourceChoice;
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
}
