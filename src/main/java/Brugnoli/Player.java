package Brugnoli;

public class Player {
    private final String nickname;
    private final int playerNumber;
    private Playerboard playerboard;
    private LeaderCardsDeck playerLeaderCards;

    public Player(String nickname, int playerNumber, Playerboard playerboard, LeaderCardsDeck playerLeaderCards) {
        this.nickname = nickname;
        this.playerNumber = playerNumber;
        this.playerboard = playerboard;
        this.playerLeaderCards = playerLeaderCards;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Playerboard getPlayerboard() {
        return playerboard;
    }

    public LeaderCardsDeck getPlayerLeaderCards() {
        return playerLeaderCards;
    }

    public void setPlayerLeaderCardsDeck(LeaderCardsDeck playerLeaderCards) {

    }

    public void setStartingPlayerBoard(int playerNumber) {

    }

    public int getAction( ) {

    }

    public void pickLineFromMarket(GameModel game) {

    }

    public DevelopmentCardDeck buyDevelopmentCard(GameModel game) {

    }

    public void activateProduction(Playerboard playerboard) {

    }

    public int getLeaderAction( ) {

    }

    public void playLeaderCard(LeaderCardDeck playerLeaderCards) {

    }

    public void discardLeaderCard(LeaderCardDeck playerLeaderCards, Playerboard playerboard) {

    }

    public boolean checkWinConditions(Playerboard playerboard) {

    }

}
