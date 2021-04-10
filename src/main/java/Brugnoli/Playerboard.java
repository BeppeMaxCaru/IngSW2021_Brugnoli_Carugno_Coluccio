package Brugnoli;

import Carugno.DevelopmentCards.DevelopmentCardsDeck;

public class Playerboard {

    private DevelopmentCardsDeck devCardDeck1;
    private DevelopmentCardsDeck devCardDeck2;
    private DevelopmentCardsDeck devCardDeck3;
    private Chest chest;
    private WareHouse wareHouse;
    private FaithPath faithPath;

    public Playerboard(DevelopmentCardsDeck devCardDeck1, DevelopmentCardsDeck devCardDeck2, DevelopmentCardsDeck devCardDeck3, Chest chest, WareHouse wareHouse,FaithPath faithPath) {
        this.devCardDeck1 = devCardDeck1;
        this.devCardDeck2 = devCardDeck2;
        this.devCardDeck3 = devCardDeck3;
        this.chest = chest;
        this.wareHouse = wareHouse;
        this.faithPath = faithPath;
    }

    public DevelopmentCardsDeck getDevCardDeck1() {
        return devCardDeck1;
    }

    public void setDevCardDeck1(DevelopmentCardsDeck devCardDeck1) {
    }

    public DevelopmentCardsDeck getDevCardDeck2() {
        return devCardDeck2;
    }

    public void setDevCardDeck2(DevelopmentCardsDeck devCardDeck2) {
    }

    public DevelopmentCardsDeck getDevCardDeck3() {
        return devCardDeck3;
    }

    public void setDevCardDeck3(DevelopmentCardsDeck devCardDeck3) {
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

    public void activateBasicProductionPower(WareHouse wareHouse, Chest chest) {

    }
}
