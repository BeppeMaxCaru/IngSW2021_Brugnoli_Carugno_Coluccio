package Carugno.ActionCounters.ActionCountersTypes;

import Brugnoli.Playerboard;
import Carugno.ActionCounters.ActionCounter;
import Carugno.ActionCounters.ActionCountersDeck;
import Carugno.DevelopmentCards.DevelopmentCardsDecksGrid;

public class BlackCross1 extends ActionCounter {

    public BlackCross1 (int value) {
        super(value);
    }

    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
        //modifica la playerboard spostando la croce nera
        actionCountersDeck.shuffle();

    }

}
