package Maestri.MVC.Model.GModel;

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

}
