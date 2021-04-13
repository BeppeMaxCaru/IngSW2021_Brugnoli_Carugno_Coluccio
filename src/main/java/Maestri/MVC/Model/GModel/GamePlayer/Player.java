package Maestri.MVC.Model.GModel.GamePlayer;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.WhiteMarble;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

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

    /** This method asks the player's nickname. */

    public String getNickname() {
        Scanner in = new Scanner(System.in);

        System.out.println("Player nickname:");
        nickname = in.nextLine();

        return this.nickname;
    }

    public Integer getPlayerNumber() {
        return this.playerNumber;
    }

    public void setPlayerNumber(Integer i) {
        playerNumber = i;
    }

    public Playerboard getPlayerboard() {
        return this.playerboard;
    }

    public LeaderCardDeck getPlayerLeaderCards() {
        return this.playerLeaderCards;
    }

    public void setPlayerLeaderCardsDeck(LeaderCardDeck playerLeaderCards) {

    }

    /** This method sets the player's initial resources due to his playerNumber. */

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

    /** This method asks the player what action he wants to do. */

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

    /** This method allows the player to pick a line from the market, after choosing the first action. */

    public void pickLineFromMarket(Market market, Player player[]) {
        Scanner in = new Scanner(System.in);
        int rowColumnChoice = -1;
        int columnNum = -1;
        int rowNum = -1;

        // Scelta colonna/riga.
        while(rowColumnChoice != 0 && rowColumnChoice != 1) {
            System.out.println("Do you want to choose a column or a row from the market? Write 0 for column, 1 for row:");
            System.out.println(market);
            rowColumnChoice = in.nextInt();
        }

        // Scelta numero di colonna/riga e mette risorse nel warehouse.
        if(rowColumnChoice == 0) {
            while(columnNum < 0 || columnNum > 3) {
                System.out.println("Choose the column's number you want to get the resources from:");
                System.out.println(market);
                columnNum = in.nextInt();
                market.updateColumn(columnNum, player, playerNumber);
            }
        }
        else {
            while(rowNum < 0 || rowNum > 2) {
                System.out.println("Choose the row's number you want to get the resources from:");
                System.out.println(market);
                rowNum = in.nextInt();
                market.updateRow(rowNum, player, playerNumber);
            }
        }

    }

    /** This method asks the player what development cards he wants to buy. */

    public boolean buyDevelopmentCard(GameModel gameModel) {
        return gameModel.getDevelopmentCardsDecksGrid().buyDevelopmentCard(this.playerboard);
    }

    public void activateProduction(Playerboard playerboard) {

    }

    /** This method asks the player if he wants to do a leader action. */

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

    /** This method allows the player to discard a leader card. */

    public void discardLeaderCard(LeaderCardDeck playerLeaderCards, GameModel gameModel) {
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
        gameModel.getLeaderCardDeck().getLeaderCardsDeck()[numLeaderCard].discard(playerboard);
    }
}
