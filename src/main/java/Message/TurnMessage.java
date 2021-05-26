package Message;

import java.io.PrintWriter;
import java.io.Serializable;
import Maestri.MVC.Model.GModel.GameModel;

public class TurnMessage extends Message implements Serializable {

    private final int playerNumber;
    GameModel gameModel;
    private final int itsYourTurn;
    PrintWriter out;

    public TurnMessage(int playerNumber, int itsYourTurn) {
        this.playerNumber = playerNumber;
        this.itsYourTurn = itsYourTurn;
    }

    public void printMarket() {
        out.println("MARKET GRID:");
        gameModel.getMarket().printMarket();
    }

    public void printGrid() {
        out.println("DEVELOPMENT CARDS GRID:");
        gameModel.getDevelopmentCardsDecksGrid().printGrid();
    }

    public void printResource() {
        gameModel.getPlayers()[this.playerNumber].getPlayerBoard().printAll();
    }

    public int getPlayerNumber() { return this.playerNumber; }

    public int getItsYourTurn() { return this.itsYourTurn; }

}
