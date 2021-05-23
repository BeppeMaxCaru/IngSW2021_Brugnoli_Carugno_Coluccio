package Message;

import java.io.Serializable;

public class MarketResourcesMessage extends Message implements Serializable {
    private final int playerNumber;
    private final String rowColumnChoice;
    private final int index;
    private final String warehouseLeaderChoice;
    private final String whichWhiteMarbleChoice;

    public MarketResourcesMessage (int num, String rcChoice, int i, String wlChoice, String wmChoice) {
        this.playerNumber = num;
        this.rowColumnChoice = rcChoice;
        this.index = i;
        this.warehouseLeaderChoice = wlChoice;
        this.whichWhiteMarbleChoice = wmChoice;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getRowColumnChoice() {
        return rowColumnChoice;
    }

    public int getIndex() {
        return index;
    }

    public String getWarehouseLeaderChoice() {
        return warehouseLeaderChoice;
    }

    public String getWhichWhiteMarbleChoice() {
        return whichWhiteMarbleChoice;
    }

}
