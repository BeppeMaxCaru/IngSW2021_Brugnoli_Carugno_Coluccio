package Brugnoli;

import Carugno.DevelopmentCards.DevelopmentCardsDeck;
import Carugno.LeaderCards.LeaderCardDeck;
import Carugno.MVC.GameModel;
import java.util.Scanner;
import java.util.Random;

public class Player {
    private String nickname;
    private int playerNumber;
    private Playerboard playerboard;
    private LeaderCardDeck playerLeaderCards;

    public Player(String nickname, int playerNumber, Playerboard playerboard, LeaderCardDeck playerLeaderCards) {
        this.nickname = nickname;
        this.playerNumber = playerNumber;
        this.playerboard = playerboard;
        this.playerLeaderCards = playerLeaderCards;
    }

    public String getNickname() {
        Scanner in = new Scanner(System.in);

        System.out.println("Player nickname:");
        nickname = in.nextLine();

        return this.nickname;
    }

    public int getPlayerNumber() {
        Random rand = new Random();
        int upperbound = 3;

        playerNumber = rand.nextInt(upperbound);

        // Non so come controllare se due player hanno lo stesso numero di player

        return this.playerNumber;
    }

    public Playerboard getPlayerboard() {

        // Array di playerboard?? Se no come faccio il controllo se due player hanno la stessa playerboard

        return this.playerboard;
    }

    public LeaderCardDeck getPlayerLeaderCards() {


        return this.playerLeaderCards;
    }

    public void setPlayerLeaderCardsDeck(LeaderCardDeck playerLeaderCards) {

    }

    public void setStartingPlayerBoard(int playerNumber) {

    }

    public int getAction( ) {

    }

    public void pickLineFromMarket(GameModel game) {

    }

    public DevelopmentCardsDeck buyDevelopmentCard(GameModel game) {

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
