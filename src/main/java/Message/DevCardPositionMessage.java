package Message;

public class DevCardPositionMessage {

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
