package Brugnoli;

import Carugno.DevelopmentCards.DevelopmentCard;

import java.util.*;


public class Playerboard {

    private DevelopmentCard[] devCardDeck1;
    private DevelopmentCard[] devCardDeck2;
    private DevelopmentCard[] devCardDeck3;
    private Chest chest;
    private WareHouse wareHouse;
    private FaithPath faithPath;
    private int victoryPoints;

    public Playerboard(DevelopmentCard[] devCardDeck1, DevelopmentCard[] devCardDeck2, DevelopmentCard[] devCardDeck3, Chest chest, WareHouse wareHouse, FaithPath faithPath, int victoryPoints) {
        this.devCardDeck1 = devCardDeck1;
        this.devCardDeck2 = devCardDeck2;
        this.devCardDeck3 = devCardDeck3;
        this.chest = chest;
        this.wareHouse = wareHouse;
        this.faithPath = faithPath;
        this.victoryPoints = victoryPoints;
    }

    public DevelopmentCard[] getDevCardDeck1() {
        return devCardDeck1;
    }

    public void setDevCardDeck1(DevelopmentCard[] devCardDeck1) {
    }

    public DevelopmentCard[] getDevCardDeck2() {
        return devCardDeck2;
    }

    public void setDevCardDeck2(DevelopmentCard[] devCardDeck2) {
    }

    public DevelopmentCard[] getDevCardDeck3() {
        return devCardDeck3;
    }

    public void setDevCardDeck3(DevelopmentCard[] devCardDeck3) {
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
        int resourceInputNum;
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
