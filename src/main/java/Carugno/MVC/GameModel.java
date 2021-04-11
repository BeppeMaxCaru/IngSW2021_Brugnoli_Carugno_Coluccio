package Carugno.MVC;

import Brugnoli.Playerboard;
import Carugno.ActionCounters.ActionCountersDeck;
import Brugnoli.Player;
import Carugno.DevelopmentCards.DevelopmentCard;
import Carugno.DevelopmentCards.DevelopmentCardsDecksGrid;

import java.util.Map;

public class GameModel {

    private Player[] players;
    private DevelopmentCardsDecksGrid developmentCardsDecksGrid;
    private SharedLeaderCardsDeck leaderCardsDeck;
    private ActionCountersDeck actionCountersDeck;
    private SharedMarbleMarket marbleMarket;
    private boolean BlackCrossPawn;

    //Who chooses tha action????? from the controller!
    //private Map<Integer, String> playerActions

    public Player[] getPlayers() {
        return players;
    }

    public void buyDevelopmentCardAction(Player player) {
        Playerboard playerboardToModify = player.getPlayerboard();
        this.developmentCardsDecksGrid.buyDevelopmentCard(playerboardToModify);
    }

    public DevelopmentCardsDecksGrid getDevelopmentCardsDecksGrid() {
        return this.developmentCardsDecksGrid;
    }
}
