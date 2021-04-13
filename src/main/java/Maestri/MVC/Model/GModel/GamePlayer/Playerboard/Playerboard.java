package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;

import java.util.*;


public class Playerboard {

    private int developmentCardsBought;
    //private DevelopmentCard[] playerboardDevelopmentCards;
    private Map<Integer, DevelopmentCard> playerboardDevelopmentCards;
    //Mappa da aggiungere con tutti i depositi disponibili
    //private Map<Integer, Deposits> availablePlayerboardDeposits;
    private Chest chest;
    private WareHouse wareHouse;
    private FaithPath faithPath;
    private int victoryPoints;

    //Update Giuseppe: synchronizing DevelopmentCards in playerboard
    public Playerboard(DevelopmentCard[] playerDevelopmentCards, Chest chest, WareHouse wareHouse, FaithPath faithPath, int victoryPoints) {
        this.developmentCardsBought = 0;
        //this.playerboardDevelopmentCards = playerDevelopmentCards;

        this.playerboardDevelopmentCards = new HashMap<>();
        this.playerboardDevelopmentCards.put(1,null);
        this.playerboardDevelopmentCards.put(2,null);
        this.playerboardDevelopmentCards.put(3,null);

        this.chest = chest;
        this.wareHouse = wareHouse;
        this.faithPath = faithPath;
        this.victoryPoints = victoryPoints;
    }

    public Map<Integer, DevelopmentCard> getPlayerDevelopmentCards() {
        return this.playerboardDevelopmentCards;
    }

    public void payNewDevelopmentCard(DevelopmentCard developmentCard) {
        Map<String, Integer> resourcesToPay = new HashMap<>();
        while(!resourcesToPay.equals(developmentCard.getDevelopmentCardCost())) {
            //Metodo che fa prendere risorse da tutti gli slot disponibili
            //this.pickResources();
        }

    }

    public boolean placeNewDevelopmentCard(DevelopmentCard developmentCard) {
        System.out.println(this.playerboardDevelopmentCards);
        System.out.println("Choose space number where to place new development card: ");
        Scanner playerInput = new Scanner(System.in);
        System.out.println("");
        int spaceChoosenFromPlayer = playerInput.nextInt();
        while (!this.playerboardDevelopmentCards.containsKey(spaceChoosenFromPlayer)) {
            System.out.println("Space not existing!");
            System.out.println("Choose valid space number where to place new development card: ");
            spaceChoosenFromPlayer = playerInput.nextInt();
            System.out.println("");
        }
        //Caso in cui spazio vuoto e caso in cui spazio con carta già presente
        if (this.playerboardDevelopmentCards.get(spaceChoosenFromPlayer).equals(null)) {
            this.playerboardDevelopmentCards.put(spaceChoosenFromPlayer, developmentCard);
            return true;
        } else if (!this.playerboardDevelopmentCards.get(spaceChoosenFromPlayer).equals(null)) {
            DevelopmentCard developmentCardAlreadyInSpace = this.playerboardDevelopmentCards.get(spaceChoosenFromPlayer);
            //Caso in cui può piazzarla
            if (developmentCard.getDevelopmentCardColour().equals(developmentCardAlreadyInSpace.getDevelopmentCardColour())) {

                if (developmentCard.getDevelopmentCardLevel()==(developmentCardAlreadyInSpace.getDevelopmentCardLevel()+1)) {
                    this.playerboardDevelopmentCards.put(spaceChoosenFromPlayer, developmentCard);
                    this.developmentCardsBought = this.developmentCardsBought + 1;
                    System.out.println("New development card available!");
                    System.out.println("");
                    return true;
                } else if (developmentCard.getDevelopmentCardLevel()!=(developmentCardAlreadyInSpace.getDevelopmentCardLevel()+1)) {
                    System.out.println("Existing card not compatible!");
                    System.out.println("");
                    return false;
                }

            } else if (!(developmentCard.getDevelopmentCardColour().equals(developmentCardAlreadyInSpace.getDevelopmentCardColour()))) {
                System.out.println("Existing card not compatible!");
                System.out.println("");
                return false;
                //spaceChoosenFromPlayer = playerInput.nextInt();
                //rifaccio il controllo dell'inserimento con while in sopraclasse!!!!!
                //Da mettere in player
            }
        }
        return false;
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

    public void setVictoryPoints(int i) {
        victoryPoints = victoryPoints + i;
    }

    public void activateBasicProductionPower(WareHouse wareHouse, Chest chest) {
        int fromWhat = -1;
        int resourceInputNum = -1;
        int resourceOutputNum = -1;
        int numResources;
        int i = 2;
        List<String> availableResourceWarehouse = null;
        List<String> availableResourceChest = null;
        Scanner in = new Scanner(System.in);

        // Available resources from warehouse
        for (String key : wareHouse.getWarehouseResources().keySet()) {
            numResources = wareHouse.getWarehouseResources().get(key);
            if(numResources != 0) {
                availableResourceWarehouse = new ArrayList<>();
                availableResourceWarehouse.add(key);
            }
        }

        // Available resources from chest
        for (String key : chest.getChestResources().keySet()) {
            numResources = chest.getChestResources().get(key);
            if(numResources != 0) {
                availableResourceChest = new ArrayList<>();
                availableResourceChest.add(key);
            }
        }

        // two input
        while(i > 0) {
            resourceInputNum = -1;
            while(fromWhat != 0 && fromWhat != 1) {
                System.out.println("Do you want to choose the resource from warehouse or from chest? Write 0 for warehouse or 1 for chest:");
                fromWhat = in.nextInt();
            }
            if(fromWhat == 0 && availableResourceWarehouse != null) {
                while(resourceInputNum < 0 || resourceInputNum > availableResourceWarehouse.size() - 1) {
                    System.out.println("Choose a resource from warehouse:");
                    for(int j = 0; j < availableResourceWarehouse.size(); j++) {
                        System.out.println("Write" + j + "for" + availableResourceWarehouse.get(j));
                    }
                    resourceInputNum = in.nextInt();
                }

                numResources = wareHouse.getWarehouseResources().get(availableResourceWarehouse.get(resourceInputNum));
                wareHouse.getWarehouseResources().put(availableResourceWarehouse.get(resourceInputNum), numResources - 1);
                i--;
            }
            else if(availableResourceChest != null) {
                while(resourceInputNum < 0 || resourceInputNum > availableResourceChest.size() - 1) {
                    System.out.println("Choose a resource from chest:");
                    for(int j = 0; j < availableResourceChest.size(); j++) {
                        System.out.println("Write" + j + "for" + availableResourceChest.get(j));
                    }
                    resourceInputNum = in.nextInt();
                }
                numResources = chest.getChestResources().get(availableResourceChest.get(resourceInputNum));
                chest.getChestResources().put(availableResourceChest.get(resourceInputNum), numResources - 1);
                i--;
            }
        }

        // output
        while(resourceOutputNum < 0 || resourceOutputNum > 3) {
            System.out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
            resourceOutputNum = in.nextInt();
        }

        i = 0;
        for (String key : chest.getChestResources().keySet()) {
            if(i == resourceOutputNum) {
                numResources = chest.getChestResources().get(key);
                chest.getChestResources().put(key, numResources + 1);
                break;
            }
            i++;
        }
    }
}
