package Maestri.MVC.Model.GModel.ActionCounters;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;

import java.io.PrintWriter;
import java.util.Scanner;

public class ActionCounter {

    /**
     *
     */
    private final int value;

    public ActionCounter(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    /**
     * This function receives the components that can be modified by the action counters and subsequently
     * modifies the components depending on the method overridden in the subclasses
     * @param actionCountersDeck actionCountersDeck
     * @param playerboard playerboard
     * @param developmentCardsDecksGrid developmentCardsDecksGrid
     */
    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard,
                         DevelopmentCardsDecksGrid developmentCardsDecksGrid, PrintWriter out) {

    }

    public void discardDevelopmentCard(DevelopmentCardsDecksGrid developmentCardsDecksGrid, String colour) {

        //developmentCardsDecksGrid.getDevelopmentCardsDecks()[2][developmentCardsDecksGrid.getDevelopmentCardsColours().get(colour)][0];
    }

}
