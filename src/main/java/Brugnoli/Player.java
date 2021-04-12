package Brugnoli;

import Carugno.DevelopmentCards.DevelopmentCardsDeck;
import Carugno.LeaderCards.LeaderCardDeck;
import Carugno.MVC.GameModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;
import java.util.*;

public class Player {
    private String nickname;
    private Integer playerNumber;
    private Playerboard playerboard;
    private LeaderCardDeck playerLeaderCards;

    public Player(String nickname, Integer playerNumber, Playerboard playerboard, LeaderCardDeck playerLeaderCards) {
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

    public Integer getPlayerNumber() {
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

    public void setStartingPlayerBoard(Integer playerNumber) {
        Map<Integer, Integer[]> startingResources =  new HashMap<>();
        int numChosenResources;
        int numInitialRedCross;
        int resourceNum;
        int resourceNumWarehouse;
        int i;
        Scanner in = new Scanner(System.in);

        startingResources.put(0, new Integer[] {0, 0});
        startingResources.put(1, new Integer[] {1, 0});
        startingResources.put(2, new Integer[] {1, 1});
        startingResources.put(3, new Integer[] {2, 1});

        for (Integer key : startingResources.keySet()){
            if(playerNumber.equals(key)) {
                numChosenResources = startingResources.get(key)[0];
                numInitialRedCross = startingResources.get(key)[1];
                while(numChosenResources > 0) {
                    resourceNum = -1;
                    while(resourceNum < 0 || resourceNum > 3) {
                        System.out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
                        resourceNum = in.nextInt();
                    }
                    i = 0;
                    for (String key2 : getPlayerboard().getWareHouse().getWarehouseResources().keySet()) {
                        if(i == resourceNum) {
                            resourceNumWarehouse = getPlayerboard().getWareHouse().getWarehouseResources().get(key2);
                            getPlayerboard().getWareHouse().getWarehouseResources().put(key2, resourceNumWarehouse + 1);
                        }
                        i++;
                    }
                    numChosenResources--;
                }
                if(numInitialRedCross == 1) {
                    getPlayerboard().getFaithPath().moveCross(1);
                }
                break;
            }
        }
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
