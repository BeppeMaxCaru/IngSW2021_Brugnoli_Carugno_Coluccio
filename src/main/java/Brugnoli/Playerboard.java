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
        String fromWhat;
        String resourceInput;
        String resourceOutput;
        Scanner in = new Scanner(System.in);
        int i = 2;
        int numResources;

        while(i > 0) {
            numResources = 0;
            System.out.println("Do you want to choose the resource from warehouse or from chest?:");
            fromWhat = in.nextLine();
            if(fromWhat.equals("warehouse")) {
                System.out.println("Choose a resource from warehouse:");
                resourceInput = in.nextLine();
                while(numResources == 0) {
                    numResources = wareHouse.getWarehouseResources().get(resourceInput);
                    if(numResources == 0) {
                        System.out.println("Choose another resource from warehouse, that one you chose was over:");
                        resourceInput = in.nextLine();
                    }
                }
                wareHouse.getWarehouseResources().put(resourceInput, numResources - 1);
                i--;
            }
            else {
                System.out.println("Choose a resource from chest:");
                resourceInput = in.nextLine();
                while(numResources == 0) {
                    numResources = chest.getChestResources().get(resourceInput);
                    if(numResources == 0) {
                        System.out.println("Choose another resource from chest, that one you chose was over:");
                        resourceInput = in.nextLine();
                    }
                }
                chest.getChestResources().put(resourceInput, numResources - 1);
                i--;
            }
        }

        System.out.println("Choose one resource:");
        resourceOutput = in.nextLine();
        numResources = chest.getChestResources().get(resourceOutput);
        chest.getChestResources().put(resourceOutput, numResources + 1);
    }
}
