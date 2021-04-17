package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.util.*;


public class Playerboard {

    private int developmentCardsBought;
    //private DevelopmentCard[] playerboardDevelopmentCards;
    private DevelopmentCard[][] playerboardDevelopmentCards;
    //Mappa da aggiungere con tutti i depositi disponibili
    //private Map<Integer, Deposits> availablePlayerboardDeposits;
    private Chest chest;
    private WareHouse wareHouse;
    private FaithPath faithPath;
    private int victoryPoints;
    private final Marble[] resourceMarbles = new Marble[2];
    private final String[] developmentCardDiscount =new String[2];

    private String[] leaderCardsDiscounts = new String[2];
    private String[] whiteMarblePossibleResources = new String[2];

    //Update Giuseppe: synchronizing DevelopmentCards in playerboard
    public Playerboard(DevelopmentCard[] playerboardDevelopmentCard, Chest chest, WareHouse wareHouse, FaithPath faithPath, int victoryPoints) {
        this.developmentCardsBought = 0;
        //this.playerboardDevelopmentCards = playerDevelopmentCards;

        this.playerboardDevelopmentCards = new DevelopmentCard[3][3];

        this.chest = chest;
        this.wareHouse = wareHouse;
        this.faithPath = faithPath;
        this.victoryPoints = victoryPoints;
    }

    /**
     * This method returns which Marbles player has to pick instead of WhiteMarbles
     */
    public Marble[] getResourceMarbles(){
        return this.resourceMarbles;
    }

    public String[] getDevelopmentCardDiscount() {
        return this.developmentCardDiscount;
    }

    public int getDevelopmentCardsBought() {
        return this.developmentCardsBought;
    }

    public DevelopmentCard[][] getPlayerboardDevelopmentCards() {
        return this.playerboardDevelopmentCards;
    }

    /*public void payNewDevelopmentCard(DevelopmentCard developmentCard) {
        Map<String, Integer> resourcesToPay = new HashMap<>();
        while(!resourcesToPay.equals(developmentCard.getDevelopmentCardCost())) {
            //Metodo che fa prendere risorse da tutti gli slot disponibili
            //this.pickResources();
        }

    }*/

    public void placeNewDevelopmentCard(DevelopmentCard developmentCard) {
        System.out.println("Choose space number where to place new development card: ");
        Scanner playerInput = new Scanner(System.in);
        System.out.println("");
        int spaceChoosenFromPlayer = playerInput.nextInt();
        while (spaceChoosenFromPlayer<0||spaceChoosenFromPlayer>2) {
            System.out.println("Space not existing!");
            System.out.println("Choose valid space number where to place new development card: ");
            spaceChoosenFromPlayer = playerInput.nextInt();
            System.out.println("");
        }
        if(developmentCard.checkPlayerboardDevelopmentCardsCompatibility(this)){
            this.getPlayerboardDevelopmentCards()[developmentCard.getDevelopmentCardLevel()][spaceChoosenFromPlayer]=developmentCard;
        }

    }

    public Chest getChest() {
        return chest;
    }

    public WareHouse getWareHouse() {
        return this.wareHouse;
    }

    public void setWareHouse(WareHouse wareHouse) {
    }

    public FaithPath getFaithPath() {
        return faithPath;
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    public void sumVictoryPoints(int i) {
        victoryPoints = victoryPoints + i;
    }

    public String[] getLeaderCardsDiscounts() {
        return this.leaderCardsDiscounts;
    }

    public void addLeaderCardsDiscounts(String discount) {
        for (int i=0;i<this.leaderCardsDiscounts.length;i++) {
            if (this.leaderCardsDiscounts[i].equals(null)) {
                this.leaderCardsDiscounts[i] = discount;
                return;
            }
        };
    }

    public String[] getWhiteMarblePossibleResources() {
        return this.whiteMarblePossibleResources;
    }

    public void addWhiteMarblePossibleResources(String possibleResources) {
        for (int i=0;i<this.whiteMarblePossibleResources.length;i++) {
            if (this.whiteMarblePossibleResources[i].equals(null)) {
                this.whiteMarblePossibleResources[i] = possibleResources;
                return;
            }
        };
    }

    /** This method picks resources from warehouse/chest to pay the player's development cards. */

    public void pickResource(String resource) {
        int fromWhat = -1;
        int fromWhichWarehouse = -1;
        Integer whRes=wareHouse.getWarehouseResources().get(resource);
        Integer esRes=wareHouse.getWarehouseResources().get("extra"+resource);
        Integer numResources;
        Scanner in = new Scanner(System.in);

        while(fromWhat != 0 && fromWhat != 1) {
            System.out.println("Do you want to pick the resource from warehouse or from chest? Write 0 for warehouse or 1 for chest:");
            fromWhat = in.nextInt();
        }

        if(fromWhat == 0) {
            numResources = 0;
            numResources=numResources+whRes+esRes;
            if(numResources != 0)
            {
                if((whRes!=0)&& (esRes==0))
                    wareHouse.getWarehouseResources().put(resource, whRes - 1);
                else if ((whRes==0)&& (esRes!=0))
                    wareHouse.getWarehouseResources().put("extra"+resource, esRes - 1);
                else
                {
                    while((fromWhichWarehouse<0)||(fromWhichWarehouse>1))
                    {
                        System.out.println("Do you want to pick the resource from standard Warehouse or from extra Warehouse space? Write 0 for Warehouse or 1 for extra space:");
                        fromWhichWarehouse = in.nextInt();
                        if (fromWhichWarehouse==0)
                            wareHouse.getWarehouseResources().put(resource, whRes - 1);
                        else
                            wareHouse.getWarehouseResources().put("extra"+resource, esRes - 1);
                    }
                }
            }
            else {
                System.out.println("You have run out of" + resource + "in the warehouse, pick the others from the chest:");
                numResources = chest.getChestResources().get(resource);
                chest.getChestResources().put(resource, numResources - 1);
            }
        }
        else {
            numResources = chest.getChestResources().get(resource);
            if(numResources != 0)
                chest.getChestResources().put(resource, numResources - 1);
            else {
                System.out.println("You have run out of" + resource + "in the chest, pick the others from the warehouse:");
                numResources = wareHouse.getWarehouseResources().get(resource);
                wareHouse.getWarehouseResources().put(resource, numResources - 1);
            }
        }
    }

    /** This method activates the basic production power of the playerboard. */

    public List<String> activateBasicProductionPower() {
        int resourceOutputNum = -1;
        int numResources;
        int i;
        int resourceInputNum = -1;
        int fromWhat = -1;
        List<String> availableResourceWarehouse = null;
        List<String> availableResourceChest = null;
        List<String> resourceChoice = null;
        Scanner in = new Scanner(System.in);

        // Available resources from warehouse
        for (String key : wareHouse.getWarehouseResources().keySet()) {
            numResources = wareHouse.getWarehouseResources().get(key);
            if (numResources != 0) {
                availableResourceWarehouse = new ArrayList<>();
                availableResourceWarehouse.add(key);
            }
        }

        // Available resources from chest
        for (String key : chest.getChestResources().keySet()) {
            numResources = chest.getChestResources().get(key);
            if (numResources != 0) {
                availableResourceChest = new ArrayList<>();
                availableResourceChest.add(key);
            }
        }

        // two input
        for (i = 0; i < 2; i++) {
            while (fromWhat != 0 && fromWhat != 1) {
                System.out.println("Do you want to choose the resource from warehouse or from chest? Write 0 for warehouse or 1 for chest:");
                fromWhat = in.nextInt();
            }
            if (fromWhat == 0 && availableResourceWarehouse != null) {
                while (resourceInputNum < 0 || resourceInputNum > availableResourceWarehouse.size() - 1) {
                    System.out.println("Choose a resource from warehouse:");
                    for (int j = 0; j < availableResourceWarehouse.size(); j++) {
                        System.out.println("Write" + j + "for" + availableResourceWarehouse.get(j));
                    }
                    resourceInputNum = in.nextInt();
                }
                resourceChoice = new ArrayList<>();
                resourceChoice.add(availableResourceWarehouse.get(resourceInputNum));
            } else if (availableResourceChest != null) {
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
            for (String key : chest.getChestResources().keySet()) {
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
}
