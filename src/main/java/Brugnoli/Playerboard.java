package Brugnoli;

import Carugno.DevelopmentCards.DevelopmentCard;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


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

    public int getVictoryPoints(){

    }

    public void setVictoryPoints(int victoryPoints) {

    }

    public void activateBasicProductionPower(WareHouse wareHouse, Chest chest) {
        int fromWhat = -1;
        int resourceInputNum = -1;
        int resourceOutputNum = -1;
        int numResources;
        String resourceInput;
        String resourceOutput;
        Scanner in = new Scanner(System.in);
        int i = 2;

        // two input
        while(i > 0) {
            while(fromWhat != 0 && fromWhat != 1) {
                System.out.println("Do you want to choose the resource from warehouse or from chest? Write 0 for warehouse or 1 for chest:");
                fromWhat = in.nextInt();
            }
            if(fromWhat == 0) {
                while(resourceInputNum < 0 || resourceInputNum > 3) {
                    System.out.println("Choose a resource from warehouse: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
                    resourceInputNum = in.nextInt();
                }
                if(resourceInputNum == 0)
                    resourceInput = "COINS";
                else if(resourceInputNum == 1)
                    resourceInput = "SHIELDS";
                else if(resourceInputNum == 2)
                    resourceInput = "SERVANTS";
                else
                    resourceInput = "STONES";
                numResources = 0;
                while(numResources == 0) {
                    numResources = wareHouse.getWarehouseResources().get(resourceInput);
                    if(numResources == 0) {
                        resourceInputNum = -1;
                        while(resourceInputNum < 0 || resourceInputNum > 3) {
                            System.out.println("Choose another resource from warehouse, that one you chose was over: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
                            resourceInputNum = in.nextInt();
                        }
                        if(resourceInputNum == 0)
                            resourceInput = "COINS";
                        else if(resourceInputNum == 1)
                            resourceInput = "SHIELDS";
                        else if(resourceInputNum == 2)
                            resourceInput = "SERVANTS";
                        else
                            resourceInput = "STONES";
                    }
                }
                wareHouse.getWarehouseResources().put(resourceInput, numResources - 1);
                i--;
            }
            else {
                while(resourceInputNum < 0 || resourceInputNum > 3) {
                    System.out.println("Choose a resource from chest: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
                    resourceInputNum = in.nextInt();
                }
                if(resourceInputNum == 0)
                    resourceInput = "COINS";
                else if(resourceInputNum == 1)
                    resourceInput = "SHIELDS";
                else if(resourceInputNum == 2)
                    resourceInput = "SERVANTS";
                else
                    resourceInput = "STONES";
                numResources = 0;
                while(numResources == 0) {
                    numResources = chest.getChestResources().get(resourceInput);
                    if(numResources == 0) {
                        resourceInputNum = -1;
                        while(resourceInputNum < 0 || resourceInputNum > 3) {
                            System.out.println("Choose another resource from chest, that one you chose was over: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
                            resourceInputNum = in.nextInt();
                        }
                        if(resourceInputNum == 0)
                            resourceInput = "COINS";
                        else if(resourceInputNum == 1)
                            resourceInput = "SHIELDS";
                        else if(resourceInputNum == 2)
                            resourceInput = "SERVANTS";
                        else
                            resourceInput = "STONES";
                    }
                }
                chest.getChestResources().put(resourceInput, numResources - 1);
                i--;
            }
        }

        // output
        while(resourceOutputNum < 0 || resourceOutputNum > 3) {
            System.out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
            resourceOutputNum = in.nextInt();
        }
        if(resourceOutputNum == 0)
            resourceOutput = "COINS";
        else if(resourceOutputNum == 1)
            resourceOutput = "SHIELDS";
        else if(resourceOutputNum == 2)
            resourceOutput = "SERVANTS";
        else
            resourceOutput = "STONES";
        numResources = chest.getChestResources().get(resourceOutput);
        chest.getChestResources().put(resourceOutput, numResources + 1);
    }
}
