package Message;

import java.util.ArrayList;

public class ServerCardAvailabilityMessage {

    private final ArrayList<Integer> cardPositions;

    public ServerCardAvailabilityMessage(ArrayList<Integer> positions) {
        this.cardPositions=positions;
    }

    public ArrayList<Integer> getCardPositions() {
        return cardPositions;
    }
}
