package Maestri.MVC.Model.GModel;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Represents the state of game "Maestri del Rinascimento"
 */
public class GameModel {

    int numberOfPlayers;
    private Player[] players;
    int currentPlayer = 0;
    private DevelopmentCardsDecksGrid developmentCardsDecksGrid;
    private LeaderCardDeck leaderCardDeck;
    private Market market;
    private ActionCountersDeck actionCountersDeck;
    private boolean BlackCrossPawn = false;
    private Socket socket;

    //Who chooses the action????? from the controller!
    //private Map<Integer, String> playerActions

    public GameModel() {
        this.players = new Player[4];
        /*for (int i=0;i<numberOfPlayers;i++) {
            //this.players[i] = new Player(this.players[i].chooseNickname(), i);
            //this.players[i].setStartingPlayerResources();
        }*/
        this.setStartingLeaderCards();
        this.developmentCardsDecksGrid = new DevelopmentCardsDecksGrid();
        this.leaderCardDeck = new LeaderCardDeck();
        this.market = new Market();
        /*if (numberOfPlayers==1) {
            this.actionCountersDeck = new ActionCountersDeck();
            this.BlackCrossPawn = true;
        }*/
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public boolean addNewPlayer(Player newPlayer) {
        for (int i=0;i<this.players.length;i++) {
            if (this.players[i].equals(null)) {
                this.players[i] = newPlayer;
                this.players[i].setPlayerNumber(i);
                return true;
            }
        }
        return false;
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
            if (player.getPlayerBoard().getFaithPath().getCrossPosition() == 24 || player.getPlayerBoard().getDevelopmentCardsBought() == 7) {
                // ??
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
    public void gameInProgress(Scanner in, PrintWriter out) {

        while (this.checkEndPlay()) {
            for (Player player : this.players) {

                if (player.getPlayerLeaderCards()[0] != null) {
                    if (player.getPlayerLeaderCards()[1] == null && !player.getPlayerLeaderCards()[0].isPlayed())
                        player.getLeaderAction(in, out);
                    else if (player.getPlayerLeaderCards()[1] != null &&
                            (!player.getPlayerLeaderCards()[0].isPlayed() || !player.getPlayerLeaderCards()[1].isPlayed()))
                        player.getLeaderAction(in, out);
                    else out.println("You have activated all your Leader cards. You can't do a Leader Action.");
                } else out.println("You have discarded all your Leader cards. You can't do a Leader Action.");

                boolean correctAction = true;
                do {
                    switch (player.getAction(in, out)) {
                        case 0:
                            player.pickLineFromMarket(this.market, this.players, in, out);
                            break;
                        case 1:
                            correctAction = player.buyDevelopmentCard(this.developmentCardsDecksGrid, in, out);
                            break;
                        case 2:
                            correctAction = player.activateProduction(in, out);
                            break;
                    }
                } while (!correctAction);

                if (player.getPlayerLeaderCards()[0] != null)
                    if (player.getPlayerLeaderCards()[1] == null && !player.getPlayerLeaderCards()[0].isPlayed())
                        player.getLeaderAction(in, out);
                    else if (player.getPlayerLeaderCards()[1] != null &&
                            (!player.getPlayerLeaderCards()[0].isPlayed() || !player.getPlayerLeaderCards()[1].isPlayed()))
                        player.getLeaderAction(in, out);

                out.println();
            }
        }
        out.println("Game over.");
        //There is a winner
        out.println(this.players[this.checkWinner()].getNickname() + " wins the game with " + this.players[this.checkWinner()].sumAllVictoryPoints() + " Victory Points.");
        for (int pn=0; pn<this.players.length; pn++)
            if(pn!=this.checkWinner())
                out.println(this.players[pn].getNickname() + " obtains " + this.players[pn].sumAllVictoryPoints() + " Victory Points.");
    }

}
