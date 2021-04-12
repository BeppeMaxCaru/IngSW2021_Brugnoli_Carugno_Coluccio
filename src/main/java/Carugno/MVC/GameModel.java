package Carugno.MVC;

import Brugnoli.Playerboard;
import Carugno.ActionCounters.ActionCountersDeck;
import Brugnoli.Player;
import Carugno.DevelopmentCards.DevelopmentCardsDecksGrid;
import Carugno.LeaderCards.LeaderCardDeck;
import Coluccio.Market;

public class GameModel {

    private Player[] players;
    private DevelopmentCardsDecksGrid developmentCardsDecksGrid;
    private LeaderCardDeck leaderCardDeck;
    private ActionCountersDeck actionCountersDeck;
    private Market market;
    private boolean BlackCrossPawn;

    //Who chooses tha action????? from the controller!
    //private Map<Integer, String> playerActions

    public GameModel(Player[] players, DevelopmentCardsDecksGrid developmentCardsDecksGrid, LeaderCardDeck leaderCardDeck, ActionCountersDeck actionCountersDeck, Market market, boolean blackCrossPawn) {
        this.players = players;
        this.developmentCardsDecksGrid = developmentCardsDecksGrid;
        this.leaderCardDeck = leaderCardDeck;
        this.actionCountersDeck = actionCountersDeck;
        this.market = market;
        BlackCrossPawn = blackCrossPawn;
    }

    public Player[] getPlayers() {
        return this.players;
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

    public void buyDevelopmentCardAction(Player player) {
        Playerboard playerboardToModify = player.getPlayerboard();
        this.developmentCardsDecksGrid.buyDevelopmentCard(playerboardToModify);
    }

    public DevelopmentCardsDecksGrid getDevelopmentCardsDecksGrid() {
        return this.developmentCardsDecksGrid;
    }
}
