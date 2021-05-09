package Maestri.MVC.Model.GModel;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the state of game "Maestri del Rinascimento"
 */
public class GameModel implements Runnable {

    int numberOfPlayers;
    private Player[] players = new Player[4];
    int currentPlayer = 0;
    private DevelopmentCardsDecksGrid developmentCardsDecksGrid;
    private LeaderCardDeck leaderCardDeck;
    private Market market;
    private ActionCountersDeck actionCountersDeck;
    private boolean BlackCrossPawn = false;
    //Who chooses the action????? from the controller!
    //private Map<Integer, String> playerActions
    private ArrayList<Player> clientsPlayingTheGame = new ArrayList<>();
    ExecutorService executor = Executors.newFixedThreadPool(4);

    public GameModel(List<Player> clientsWaiting) {

        //Gets until 4 players
        for (int i=0;i<this.players.length;i++) {
            try {
                //If there is a player adds it
                if (!clientsWaiting.isEmpty()) this.players[i] = clientsWaiting.remove(0);
            } catch (Exception e) {
                //If no clients waiting set other players null
                this.players[i] = null;
            }
        }

        this.setStartingLeaderCards();
        this.developmentCardsDecksGrid = new DevelopmentCardsDecksGrid();
        this.leaderCardDeck = new LeaderCardDeck();
        this.market = new Market();
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public void setPlayer(Player player, int index) {
        this.players[index] = player;
    }


    public void setStartingLeaderCards() {
        for (int i=0;i<4;i++) {
            for (int j=0;j<this.numberOfPlayers;j++) {
                this.players[j].setPlayerLeaderCard(i, this.leaderCardDeck.drawOneLeaderCard());
            }
        }
    }

    public DevelopmentCardsDecksGrid getDevelopmentCardsDecksGrid() {
        return this.developmentCardsDecksGrid;
    }

    public LeaderCardDeck getLeaderCardDeck() {
        return this.leaderCardDeck;
    }

    public ActionCountersDeck getActionCountersDeck() {
        return this.actionCountersDeck;
    }

    public Market getMarket() {
        return this.market;
    }

    public boolean isBlackCrossPawn() {
        return this.BlackCrossPawn;
    }

    public void buyDevelopmentCardAction(Scanner in, PrintWriter out) {
        int index = 0;
        this.players[index].buyDevelopmentCard(this.developmentCardsDecksGrid, in ,out);
        //this.developmentCardsDecksGrid.buyDevelopmentCard(playerboardToModify);
    }

    public void pickResourcesFromMarket(Scanner in, PrintWriter out){
        int index=0;
        this.players[index].pickLineFromMarket(this.market, this.players, in, out);
    }

    public void addClient(Socket clientSocket) {
        try {
            //for (Player player : players)
            //Player newClient = new Player();
        } catch (Exception e) {
            //clientSocket;
        }
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * This method sets each player number, randomly.
     */

    public void setPlayersNumber() {
        List<Integer> numbers = new ArrayList<>(getPlayers().length);
        int j = 0;

        for (int i = 0; i < 10; i++)
            numbers.add(i);
        Collections.shuffle(numbers);

        for(int i: numbers) {
           getPlayers()[j].setPlayerNumber(i);
           j++;
        }
    }

    public void relationWithVatican(int crossPosition) {
        for(int i = 0; i < players.length; i++) {
            if(getPlayers()[i].getPlayerBoard().getFaithPath().getFaithPathTrack()[crossPosition].isPopeSpace())
                getPlayers()[i].getPlayerBoard().getFaithPath().checkRelationWithVatican(crossPosition, getPlayers()[i].getPlayerBoard());
        }
    }

    public boolean checkEndPlay() {
        for (Player player : this.players) {
            if(player!=null)
                if (player.getPlayerBoard().getFaithPath().getCrossPosition() == 24 || player.getPlayerBoard().getDevelopmentCardsBought() == 7) {
                    return true;
            }
        }
        return false;
    }

    /**
     * This method proclaims the winner.
     * @return the winner's player number.
     */

    public int checkWinner() {
        int maxVictoryPoints = 0;
        int playerWithMaxVictoryPoints = 0;

        for(int i = 0; i < this.players.length; i++) {
            if(maxVictoryPoints < getPlayers()[i].sumAllVictoryPoints()) {
                maxVictoryPoints = getPlayers()[i].sumAllVictoryPoints();
                playerWithMaxVictoryPoints = i;
            }
            else if(maxVictoryPoints == getPlayers()[i].sumAllVictoryPoints()) {
                if(getPlayers()[i].numResourcesReserve() > getPlayers()[playerWithMaxVictoryPoints].numResourcesReserve())
                    playerWithMaxVictoryPoints = i;
            }
        }

        return playerWithMaxVictoryPoints;
    }

    //Method that cycles the players
    public void run() {

        System.out.println("This is the new game");

        do {
            for (int i=0;i<this.players.length;i++) {
                if (this.players[i]!=null) {
                    try {

                        //Scanner in = new Scanner(new InputStreamReader(this.players[i].getClientSocket().getInputStream()));
                        //PrintWriter out = new PrintWriter(this.players[i].getClientSocket().getOutputStream(), true);

                        if (this.players[i].getPlayerLeaderCards()[0] != null) {
                            //for (int i = 0; i < 2; i++) {
                            if (this.players[i].getPlayerLeaderCards()[1] == null && !this.players[i].getPlayerLeaderCards()[0].isPlayed()) {
                                this.players[i].getLeaderAction(this.players[i].getInScannerReader(), this.players[i].getOutPrintWriter()); // Remove with timer

                            } else if (this.players[i].getPlayerLeaderCards()[1] != null && (!this.players[i].getPlayerLeaderCards()[0].isPlayed() || !this.players[i].getPlayerLeaderCards()[1].isPlayed())) {
                                this.players[i].getLeaderAction(this.players[i].getInScannerReader(), this.players[i].getOutPrintWriter()); // Remove with timer

                            } else this.players[i].getOutPrintWriter().println("You have activated all your Leader cards. You can't do a Leader Action.");

                        } else this.players[i].getOutPrintWriter().println("You have discarded all your Leader cards. You can't do a Leader Action.");

                        //for (int i = 0; i < 2; i++) {
                        boolean correctAction = true;
                        do {
                            switch (this.players[i].getAction(this.players[i].getInScannerReader(), this.players[i].getOutPrintWriter())) {
                                case "0":
                                    //startTime = System.currentTimeMillis();
                                    correctAction = this.players[i].pickLineFromMarket(this.market, this.players, this.players[i].getInScannerReader(), this.players[i].getOutPrintWriter());
                                    //endTime = System.currentTimeMillis() - startTime;
                                    break;
                                case "1":
                                    //startTime = System.currentTimeMillis();
                                    correctAction = this.players[i].buyDevelopmentCard(this.developmentCardsDecksGrid, this.players[i].getInScannerReader(), this.players[i].getOutPrintWriter());
                                    //endTime = System.currentTimeMillis() - startTime;
                                    break;
                                case "2":
                                    //startTime = System.currentTimeMillis();
                                    correctAction = this.players[i].activateProduction(this.players[i].getInScannerReader(), this.players[i].getOutPrintWriter());
                                    //endTime = System.currentTimeMillis() - startTime;
                                    break;
                            }
                        } while (!correctAction); // Remove with timer
                        //while ((System.currentTimeMillis() - startTime) < maximumTime * 1000 && !correctAction);
                        //i = checkStatusPlayer(endTime, i, out);
                        //}

                        if (this.players[i].getPlayerLeaderCards()[0] != null) {
                            //for (int i = 0; i < 2; i++) {
                            if (this.players[i].getPlayerLeaderCards()[1] == null && !this.players[i].getPlayerLeaderCards()[0].isPlayed()) {
                                this.players[i].getLeaderAction(this.players[i].getInScannerReader(), this.players[i].getOutPrintWriter()); // Remove with timer
                            /* startTime = System.currentTimeMillis();
                            while ((System.currentTimeMillis() - startTime) < maximumTime * 1000 && !player.getLeaderAction(in, out)) ;
                            endTime = System.currentTimeMillis() - startTime; */
                            } else if (this.players[i].getPlayerLeaderCards()[1] != null && (!this.players[i].getPlayerLeaderCards()[0].isPlayed() || !this.players[i].getPlayerLeaderCards()[1].isPlayed())) {
                                this.players[i].getLeaderAction(this.players[i].getInScannerReader(), this.players[i].getOutPrintWriter()); // Remove with timer
                            /* startTime = System.currentTimeMillis();
                            while ((System.currentTimeMillis() - startTime) < maximumTime * 1000 && !player.getLeaderAction(in, out)) ;
                            endTime = System.currentTimeMillis() - startTime; */
                            }
                            // i = checkStatusPlayer(endTime, i, out);
                            //}
                        }

                        this.players[i].getOutPrintWriter().println("Game over.");
                        //There is a winner
                        this.players[i].getOutPrintWriter().println(this.players[this.checkWinner()].getNickname() + " wins the game with " + this.players[this.checkWinner()].sumAllVictoryPoints() + " Victory Points.");
                        for (int pn = 0; pn < this.players.length; pn++)
                            if (pn != this.checkWinner())
                                this.players[i].getOutPrintWriter().println(this.players[pn].getNickname() + " obtains " + this.players[pn].sumAllVictoryPoints() + " Victory Points.");
                    }catch (Exception e){
                        System.err.println(e.getMessage());
                    }
                }

            }
        } while (this.checkEndPlay());
    }

    public int checkStatusPlayer(float endTime, int i, PrintWriter out) {
        int maximumTime = 180;

        if (endTime != 0 && endTime < maximumTime * 1000 && i == 0) {
            out.println("Are you there?!");
            out.println();
            return 0;
        }
        else if (endTime != 0 && endTime < maximumTime * 1000 && i == 1) {
            out.println("You are expelled by the game!");
            // Gestione espulsione giocatore / Gestione connessione
            return 1;
        }
        else return 2;
    }
}
