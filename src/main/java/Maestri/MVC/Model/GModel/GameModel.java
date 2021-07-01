package Maestri.MVC.Model.GModel;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the state of game "Maestri del Rinascimento"
 */
public class GameModel{

    private final int numberOfPlayers;
    private Player[] players;
    private final DevelopmentCardsDecksGrid developmentCardsDecksGrid;
    private final LeaderCardDeck leaderCardDeck;
    private final Market market;

    private Player currentWinner = null;

    public GameModel(Player[] players, int numberOfPlayers) {

        this.players = players;
        this.numberOfPlayers = numberOfPlayers;
        this.leaderCardDeck = new LeaderCardDeck();
        this.setStartingLeaderCards();
        this.developmentCardsDecksGrid = new DevelopmentCardsDecksGrid();
        this.market = new Market();

    }

    public Player[] getPlayers() {
        return this.players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public void setStartingLeaderCards() {
        for (int i=0;i<this.numberOfPlayers;i++) {
            for (int j=0;j<this.players[i].getPlayerLeaderCards().length;j++) {
                this.players[i].setPlayerLeaderCard(j, this.leaderCardDeck.drawOneLeaderCard());
            }
        }
    }

    public DevelopmentCardsDecksGrid getDevelopmentCardsDecksGrid() {
        return this.developmentCardsDecksGrid;
    }

    public LeaderCardDeck getLeaderCardDeck() {
        return this.leaderCardDeck;
    }

    public Market getMarket() {
        return this.market;
    }

    public boolean buyDevelopmentCardAction(int index, int column, int l, int p, String[] wclChoice) {
        return this.players[index].buyDevelopmentCard(this.getDevelopmentCardsDecksGrid(), column, l, p, wclChoice);
    }

    public void relationWithVatican(int crossPosition) {
        for(int i = 0; i < players.length; i++) {
            if(getPlayers()[i].getPlayerBoard().getFaithPath().getFaithPathTrack()[crossPosition].isPopeSpace())
                getPlayers()[i].getPlayerBoard().getFaithPath().checkRelationWithVatican(crossPosition, getPlayers()[i].getPlayerBoard());
        }
    }

    public boolean checkEndPlay() {
        int remainingPlayers = 4;
        for (Player player : this.players) {
            if (player!=null) {
                if (player.getPlayerBoard().getFaithPath().getCrossPosition() == 24
                        || player.getPlayerBoard().getDevelopmentCardsBought() == 7)
                    return true;
            } else {
                remainingPlayers = remainingPlayers - 1;
                //Test updated version with added messages
                //
                //Remove + winner + disconnect
                //
                //Check garbage
                //
                if (remainingPlayers==1) return true;
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
        int playerWithMaxVictoryPoints = -1;

        for(int i = 0; i < this.players.length; i++) {
            if (this.players[i]!=null) {
                if (maxVictoryPoints < this.players[i].sumAllVictoryPoints()) {
                    maxVictoryPoints = this.players[i].sumAllVictoryPoints();
                    playerWithMaxVictoryPoints = i;
                } else if (maxVictoryPoints == this.players[i].sumAllVictoryPoints()) {
                    if (playerWithMaxVictoryPoints < 0) {
                        playerWithMaxVictoryPoints = i;
                    } else {
                        if (this.players[i].numResourcesReserve() > this.players[playerWithMaxVictoryPoints].numResourcesReserve())
                            playerWithMaxVictoryPoints = i;
                    }
                }
            }
        }

        return playerWithMaxVictoryPoints;
    }

}
