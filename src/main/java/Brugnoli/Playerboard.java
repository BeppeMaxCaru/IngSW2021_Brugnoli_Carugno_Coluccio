package Brugnoli;

public class Playerboard {

    private DevCardDeck devCardDeck1;
    private DevCardDeck devCardDeck2;
    private DevCardDeck devCardDeck3;
    private Chest chest;
    private WareHouse wareHouse;
    private FaithPath faithPath;

    public Playerboard(DevCardDeck devCardDeck1, DevCardDeck devCardDeck2, DevCardDeck devCardDeck3, Chest chest, WareHouse wareHouse,FaithPath faithPath) {
        this.devCardDeck1 = devCardDeck1;
        this.devCardDeck2 = devCardDeck2;
        this.devCardDeck3 = devCardDeck3;
        this.chest = chest;
        this.wareHouse = wareHouse;
        this.faithPath = faithPath;
    }

    public DevCardDeck getDevCardDeck1() {
        return devCardDeck1;
    }

    public void setDevCardDeck1(DevCardDeck devCardDeck1) {
    }

    public DevCardDeck getDevCardDeck2() {
        return devCardDeck2;
    }

    public void setDevCardDeck2(DevCardDeck devCardDeck2) {
    }

    public DevCardDeck getDevCardDeck3() {
        return devCardDeck3;
    }

    public void setDevCardDeck3(DevCardDeck devCardDeck3) {
    }

    public Chest getChest() {
        return chest;
    }

    public WareHouse getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(WareHouse wareHouse) {
    }

    public FaithPath getFaithPath() {
        return faithPath;
    }

    public void activateBasicProductionPower(WareHouse wareHouse, Chest chest) {

    }
}
