package Maestri.MVC.Model.GModel;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Cell;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameModel {

    int numberOfPlayers;
    private Player[] players;
    private DevelopmentCardsDecksGrid developmentCardsDecksGrid;
    private LeaderCardDeck leaderCardDeck;
    private Market market;
    private ActionCountersDeck actionCountersDeck;
    private boolean BlackCrossPawn = false;

    //Who chooses tha action????? from the controller!
    //private Map<Integer, String> playerActions

    public GameModel(int numberOfPlayers) {
        this.players = new Player[numberOfPlayers];
        for (int i=0;i<numberOfPlayers;i++) {
            //this.players[i] = new Player();
            //setStartingPlayerResources();
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
                this.players[j].setPlayerLeaderCards(i, this.leaderCardDeck.drawOneLeaderCard());
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
            if(getPlayers()[i].getPlayerboard().getFaithPath().getFaithPath()[crossPosition].isPopeSpace())
                getPlayers()[i].getPlayerboard().getFaithPath().checkRelationWithVatican(crossPosition, getPlayers()[i].getPlayerboard());
        }
    }

    public void checkEndPlay(int playerNumber) {
        // Tutti i giocatori fino al giocatore a destra del primo giocatore giocano il loro ultimo turno. Come si fa??

        checkWinner();
    }

    /** This method proclaims the winner. */

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

}
