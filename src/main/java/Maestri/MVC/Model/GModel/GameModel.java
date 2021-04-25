package Maestri.MVC.Model.GModel;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    //Who chooses the action????? from the controller!
    //private Map<Integer, String> playerActions

    public GameModel(int numberOfPlayers) {
        this.players = new Player[numberOfPlayers];
        for (int i=0;i<numberOfPlayers;i++) {
            //this.players[i] = new Player(this.players[i].chooseNickname(), i);
            //this.players[i].setStartingPlayerResources();
        }
        this.setStartingLeaderCards();
        this.developmentCardsDecksGrid = new DevelopmentCardsDecksGrid();
        this.leaderCardDeck = new LeaderCardDeck();
        this.market = new Market();
        if (numberOfPlayers==1) {
            this.actionCountersDeck = new ActionCountersDeck();
            this.BlackCrossPawn = true;
        }
    }

    public Player[] getPlayers() {
        return this.players;
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

    public void buyDevelopmentCardAction() {
        int index = 0;
        this.players[index].buyDevelopmentCard(this.developmentCardsDecksGrid);
        //this.developmentCardsDecksGrid.buyDevelopmentCard(playerboardToModify);
    }

    public void pickResourcesFromMarket(){
        int index=0;
        this.players[index].pickLineFromMarket(this.market, this.players);
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

    public void checkEndPlay(int playerNumber) {
        // Try/Catch??
        // Tutti i giocatori fino al giocatore a destra del primo giocatore giocano il loro ultimo turno. Come si fa??

        checkWinner();
    }

    /**
     * This method proclaims the winner.
     * @return the winner's player number.
     */

    public int checkWinner() {
        int maxVictoryPoints = 0;
        int playerWithMaxVictoryPoints = 0;

        for(int i = 0; i < players.length; i++) {
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
    public void gameInProgress() {
        while (true) {
            this.players[this.currentPlayer].getLeaderAction();
            this.players[this.currentPlayer].getAction();
            this.players[this.currentPlayer].getLeaderAction();
            this.currentPlayer++;
            if (this.currentPlayer==this.players.length) {
                this.currentPlayer = 0;
            }
        }
        //There is a winner

    }

}
