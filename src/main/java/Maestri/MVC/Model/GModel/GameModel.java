package Maestri.MVC.Model.GModel;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

/**
 * Represents the state of game "Masters of Renaissance"
 */
public class GameModel{

    /**
     * Number of players of the game
     */
    private final int numberOfPlayers;

    /**
     * Players of the game
     */
    private Player[] players;

    /**
     * Development cards grid
     */
    private final DevelopmentCardsDecksGrid developmentCardsDecksGrid;

    /**
     * Leader cards deck
     */
    private final LeaderCardDeck leaderCardDeck;

    /**
     * Marble market
     */
    private final Market market;

    private Player currentWinner = null;

    /**
     * Initializes the game model
     * @param players players playing the game
     * @param numberOfPlayers number of players playing
     */
    public GameModel(Player[] players, int numberOfPlayers) {
        this.players = players;
        this.numberOfPlayers = numberOfPlayers;
        this.leaderCardDeck = new LeaderCardDeck();
        this.setStartingLeaderCards();
        this.developmentCardsDecksGrid = new DevelopmentCardsDecksGrid();
        this.market = new Market();
    }

    /**
     * Returns the players
     * @return the players
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Assigns the starting leaders to each player
     */
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

    public Market getMarket() {
        return this.market;
    }

    /**
     * Returns true if the player buys successfully a development card
     * @param index number of the player
     * @param column column of the chosen card
     * @param l level of the chosen card
     * @param p position on which put the development card
     * @param wclChoice choice of the shelf from where remove resources
     * @return true if the player buys successfully a development card
     */
    public boolean buyDevelopmentCardAction(int index, int column, int l, int p, String[] wclChoice) {
        return this.players[index].buyDevelopmentCard(this.getDevelopmentCardsDecksGrid(), column, l, p, wclChoice);
    }

    /**
     *
     * @param crossPosition
     */
    public void relationWithVatican(int crossPosition) {
        for(int i = 0; i < players.length; i++) {
            if(getPlayers()[i].getPlayerBoard().getFaithPath().getFaithPathTrack()[crossPosition].isPopeSpace())
                getPlayers()[i].getPlayerBoard().getFaithPath().checkRelationWithVatican(crossPosition, getPlayers()[i].getPlayerBoard());
        }
    }

    /**
     * Returns true if the game has ended
     * @return true if the game has ended
     */
    public boolean checkEndPlay() {
        int remainingPlayers = 4;
        for (Player player : this.players) {
            if (player!=null) {
                if (player.checkWinCondition())
                    return true;
            } else {
                remainingPlayers = remainingPlayers - 1;
                //Test updated version with added messages
                //Remove + winner + disconnect
                //Check garbage
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