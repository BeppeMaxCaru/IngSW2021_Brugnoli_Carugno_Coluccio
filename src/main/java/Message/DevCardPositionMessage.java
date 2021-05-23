package Message;

import java.io.Serializable;

public class DevCardPositionMessage extends Message implements Serializable {

    private final int playerNumber;
    private final int cardPosition;

    public DevCardPositionMessage(int num, int position) {
        this.playerNumber=num;
        this.cardPosition=position;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getCardPosition() {
        return cardPosition;
    }
}
