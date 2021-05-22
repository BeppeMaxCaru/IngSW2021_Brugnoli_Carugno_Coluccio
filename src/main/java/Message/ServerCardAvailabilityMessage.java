package Message;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerCardAvailabilityMessage implements Serializable {

    private final ArrayList<Integer> cardPositions;

    public ServerCardAvailabilityMessage(ArrayList<Integer> positions) {
        this.cardPositions=positions;
    }

    public ArrayList<Integer> getCardPositions() {
        return cardPositions;
    }
}
