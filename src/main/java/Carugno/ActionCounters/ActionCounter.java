package Carugno.ActionCounters;

import Carugno.DevelopmentCards.DevelopmentCardsDeck;

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
     * @param actionCountersDeck
     * @param playerboard
     * @param developmentCardsDecksGrid
     */
    public void activate(ActionCounter[] actionCountersDeck, Playerboard playerboard,
                         DevelopmentCardsDecksGrid developmentCardsDecksGrid) {

    }

}
