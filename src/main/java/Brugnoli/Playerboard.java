package Brugnoli;

import Carugno.DevelopmentCards.DevelopmentCard;

import java.util.Scanner;


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
