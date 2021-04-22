package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

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
    private DevelopmentCard[][] playerboardDevelopmentCards;

    /**
     * Unlimited resources reserve
     */
    private Chest chest;

    /**
     * Limited resources reserve
     */
    private WareHouse wareHouse;

    /**
     * Tracks the player position
     */
    private FaithPath faithPath;

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
    private final String[] developmentCardDiscount =new String[2];

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
    public void placeNewDevelopmentCard(DevelopmentCard developmentCard) {
        System.out.println("Choose space number where to place new development card (0 to 2): ");
        Scanner playerInput = new Scanner(System.in);
        System.out.println("");
        int spaceChoosenFromPlayer = playerInput.nextInt();
        while (spaceChoosenFromPlayer<0||spaceChoosenFromPlayer>2) {
            System.out.println("Space not existing!");
            System.out.println("Choose valid space number where to place new development card: ");
            spaceChoosenFromPlayer = playerInput.nextInt();
            System.out.println("");
        }
        //Checks the chosen space
        while (!isCardBelowCompatible(spaceChoosenFromPlayer, developmentCard)) {
            System.out.println("Placement not possible!");
            System.out.println("Insert new valid place (0 to 3): ");
            spaceChoosenFromPlayer = playerInput.nextInt();
            System.out.println("");
        }
    }

    /**
     * Checks that the new development card can be placed on the top of a development card pile
     * @param pile - the chosen development card pile
     * @param developmentCard - the development card to place
     * @return true if the development card can be placed on top
     */
    public boolean isCardBelowCompatible(int pile, DevelopmentCard developmentCard) {
        for (int i=0;i<3;i++) {
            if (i==0) {
                if (this.playerboardDevelopmentCards[i][pile]==null) {
                    this.playerboardDevelopmentCards[i][pile] = developmentCard;
                    this.developmentCardsBought = this.developmentCardsBought + 1;
                    return true;
                }
            } else {
                DevelopmentCard developmentCardToUpgrade = this.playerboardDevelopmentCards[i-1][pile];
                if (developmentCardToUpgrade.getDevelopmentCardLevel()==(developmentCard.getDevelopmentCardLevel()-1)) {
                    if (developmentCardToUpgrade.getDevelopmentCardColour().equals(developmentCard.getDevelopmentCardColour())) {
                        this.playerboardDevelopmentCards[i][pile] = developmentCard;
                        this.developmentCardsBought = this.developmentCardsBought + 1;
                        return true;
                    }
                }
            }
        }
        return false;
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
    public void pickResource(String resource) {
        int fromWhat = -1;
        int fromWhichWarehouse = -1;
        Integer whRes = this.wareHouse.getWarehouseResources().get(resource);
        Integer esRes = this.wareHouse.getWarehouseResources().get("extra"+resource);
        Integer numResources;
        Scanner in = new Scanner(System.in);
        int i = 1;

        while(fromWhat != 0 && fromWhat != 1) {
            System.out.println("Do you want to pick the resource from warehouse or from chest? Write 0 for warehouse or 1 for chest:");
            fromWhat = in.nextInt();
        }

        while(i > 0) {
            if (fromWhat == 0) {
                numResources = 0;
                numResources = numResources + whRes + esRes;
                if (numResources != 0) {
                    if ((whRes != 0) && (esRes == 0))
                        this.wareHouse.getWarehouseResources().put(resource, whRes - 1);
                    else if ((whRes == 0) && (esRes != 0))
                        this.wareHouse.getWarehouseResources().put("extra" + resource, esRes - 1);
                    else {
                        while ((fromWhichWarehouse < 0) || (fromWhichWarehouse > 1)) {
                            System.out.println("Do you want to pick the resource from standard Warehouse or from extra Warehouse space? Write 0 for Warehouse or 1 for extra space:");
                            fromWhichWarehouse = in.nextInt();
                            if (fromWhichWarehouse == 0)
                                this.wareHouse.getWarehouseResources().put(resource, whRes - 1);
                            else
                                this.wareHouse.getWarehouseResources().put("extra" + resource, esRes - 1);
                        }
                    }
                    i=0;
                }
                else {
                    System.out.println("You have run out of" + resource + "in the warehouse, pick the others from the chest:");
                    fromWhat = 1;
                }
            }
            else {
                numResources = this.chest.getChestResources().get(resource);
                if (numResources != 0) {
                    this.chest.getChestResources().put(resource, numResources - 1);
                    i=0;
                }
                else {
                    System.out.println("You have run out of" + resource + "in the chest, pick the others from the warehouse:");
                    fromWhat = 0;
                }
            }
        }
    }

    /**
     * Activates the basic production power of the player board
     * @return a list with the first two resources to discard and the third one to receive
     */
    public List<String> activateBasicProductionPower() {
        int resourceOutputNum = -1;
        int numResources;
        int i;
        int resourceInputNum = -1;
        int fromWhat = -1;
        List<String> availableResourceWarehouse = new ArrayList<>();
        List<String> availableResourceChest = new ArrayList<>();
        List<String> resourceChoice = new ArrayList<>();
        Scanner in = new Scanner(System.in);

        // Available resources from warehouse
        for (String key : this.wareHouse.getWarehouseResources().keySet()) {
            numResources = this.wareHouse.getWarehouseResources().get(key);
            if (numResources != 0) {
                availableResourceWarehouse.add(key);
            }
            else {
                numResources = this.wareHouse.getWarehouseResources().get("extra" + key);
                if (numResources != 0) {
                    availableResourceWarehouse.add("extra" + key);
                }
            }
        }

        // Available resources from chest
        for (String key : this.chest.getChestResources().keySet()) {
            numResources = this.chest.getChestResources().get(key);
            if (numResources != 0) {
                availableResourceChest.add(key);
            }
        }

        // Two input
        for (i = 0; i < 2; i++) {
            while (fromWhat != 0 && fromWhat != 1) {
                System.out.println("Do you want to choose the resource from warehouse or from chest? Write 0 for warehouse or 1 for chest:");
                fromWhat = in.nextInt();
            }
            if (fromWhat == 0 && !availableResourceWarehouse.isEmpty()) {
                while (resourceInputNum < 0 || resourceInputNum > availableResourceWarehouse.size() - 1) {
                    System.out.println("Choose a resource from warehouse:");
                    for (int j = 0; j < availableResourceWarehouse.size(); j++) {
                        System.out.println("Write" + j + "for" + availableResourceWarehouse.get(j));
                    }
                    resourceInputNum = in.nextInt();
                }
                resourceChoice.add(availableResourceWarehouse.get(resourceInputNum));
            } else if (!availableResourceChest.isEmpty()) {
                while (resourceInputNum < 0 || resourceInputNum > availableResourceChest.size() - 1) {
                    System.out.println("Choose a resource from chest:");
                    for (int j = 0; j < availableResourceChest.size(); j++) {
                        System.out.println("Write" + j + "for" + availableResourceChest.get(j));
                    }
                    resourceInputNum = in.nextInt();
                }
                resourceChoice = new ArrayList<>();
                resourceChoice.add(availableResourceWarehouse.get(resourceInputNum));
            }
        }

        // output
        while (resourceOutputNum < 0 || resourceOutputNum > 4) {
            System.out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES, 4 for REDCROSS");
            resourceOutputNum = in.nextInt();
        }

        if(resourceOutputNum == 4)
            resourceChoice.add("REDCROSS");
        else {
            i = 0;
            for (String key : this.chest.getChestResources().keySet()) {
                if (i == resourceOutputNum) {
                    resourceChoice = new ArrayList<>();
                    resourceChoice.add(key);
                    break;
                }
                i++;
            }
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
}
