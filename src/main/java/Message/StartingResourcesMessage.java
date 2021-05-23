package Message;

import java.io.Serializable;
import java.util.ArrayList;

public class StartingResourcesMessage extends Message implements Serializable {

    private int playerNumber;
    private ArrayList<String> startingRes = new ArrayList<>();

    public StartingResourcesMessage(int num, ArrayList<String> resources){
        this.playerNumber = num;
        this.startingRes = resources;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public ArrayList<String> getStartingRes() {
        return startingRes;
    }
}
