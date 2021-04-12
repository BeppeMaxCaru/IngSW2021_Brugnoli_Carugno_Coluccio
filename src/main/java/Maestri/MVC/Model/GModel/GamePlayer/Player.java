package Maestri.MVC.Model.GModel.GamePlayer;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.WhiteMarble;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

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

        // Non so come controllare se due player hanno lo stesso numero di player.

        return this.playerNumber;
    }

    public Playerboard getPlayerboard() {

        // Array di playerboard o nuovo attributo playerboardNumber?? Se no come faccio il controllo se due player hanno la stessa playerboard

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
        int actionNum = -1;
        Scanner in = new Scanner(System.in);

        while(actionNum < 0 || actionNum > 2) {
            System.out.println("What action do you want to do? Choose one of them:");
            System.out.println("Write 0 if you want to take resources from the market.");
            System.out.println("Write 1 if you want to buy a development card.");
            System.out.println("Write 2 if you want to activate the production.");
            actionNum = in.nextInt();
        }

        return actionNum;
    }

    public void pickLineFromMarket(GameModel gameModel) {
        Scanner in = new Scanner(System.in);
        int rowColumnChoice = -1;
        int columnNum = -1;
        int rowNum = -1;
        int resourcesNumWarehouse;
        String marbleWarehouse;

        // Scelta colonna/riga.
        while(rowColumnChoice != 0 && rowColumnChoice != 1) {
            System.out.println("Do you want to choose a column or a row from the market? Write 0 for column, 1 for row:");
            System.out.println(gameModel.getMarket());
            rowColumnChoice = in.nextInt();
        }

        // Scelta numero di colonna/riga e mette risorse nel warehouse.
        if(rowColumnChoice == 0) {
            while(columnNum < 0 || columnNum > 3) {
                System.out.println("Choose the column's number you want to get the resources from:");
                System.out.println(gameModel.getMarket());
                columnNum = in.nextInt();
            }
            for(int i = 0; i < 4; i++) {
                marbleWarehouse = gameModel.getMarket().getMarketArrangement()[i][columnNum].toString();
                // Come gestisco il caso della biglia bianca??
                resourcesNumWarehouse = getPlayerboard().getWareHouse().getWarehouseResources().get(marbleWarehouse);
                getPlayerboard().getWareHouse().getWarehouseResources().put(marbleWarehouse, resourcesNumWarehouse + 1);
            }
        }
        else {
            while(rowNum < 0 || rowNum > 2) {
                System.out.println("Choose the row's number you want to get the resources from:");
                System.out.println(gameModel.getMarket());
                rowNum = in.nextInt();
            }
            for(int i = 0; i < 3; i++) {
                marbleWarehouse = gameModel.getMarket().getMarketArrangement()[rowNum][i].toString();
                // Come gestisco il caso della biglia bianca??
                resourcesNumWarehouse = getPlayerboard().getWareHouse().getWarehouseResources().get(marbleWarehouse);
                getPlayerboard().getWareHouse().getWarehouseResources().put(marbleWarehouse, resourcesNumWarehouse + 1);
            }
        }

        // Devo gestire la biglia nello scivolo??
    }

    public boolean buyDevelopmentCard(GameModel gameModel) {
        return gameModel.getDevelopmentCardsDecksGrid().buyDevelopmentCard(this.playerboard);
    }

    public void activateProduction(Playerboard playerboard) {

    }

    public int getLeaderAction( ) {
        Scanner in = new Scanner(System.in);
        int leaderActionNum = -1;

        while(leaderActionNum != 0 && leaderActionNum != 1) {
            System.out.println("Do you want to do a leader action?: Write 1 if you want or 0 if you don't:");
            leaderActionNum = in.nextInt();
        }

        return leaderActionNum;
    }

    public void playLeaderCard(LeaderCardDeck playerLeaderCards) {

    }

    public void discardLeaderCard(LeaderCardDeck playerLeaderCards, Playerboard playerboard) {
        Scanner in = new Scanner(System.in);
        int numLeaderCard = -1;

        //Scelta della carta leader da scartare
        while(numLeaderCard < 0 || numLeaderCard > playerLeaderCards.getLeaderCardsDeck().length) {
            System.out.println("What leader card do you want to discard?:");
            for (int i = 0; i < playerLeaderCards.getLeaderCardsDeck().length; i++) {
                System.out.println("Write" + i + "for this:" + playerLeaderCards.getLeaderCardsDeck()[i]);
            }
            numLeaderCard = in.nextInt();
        }

        //Rimozione carta leader dal deck
        for (int i = 0; i < playerLeaderCards.getLeaderCardsDeck().length; i++) {
            /// ????
        }

        LeaderCard.discard(); /// ??

    }

    public boolean checkWinConditions(Playerboard playerboard) {
        return false;
    }

}
