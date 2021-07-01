package Maestri.MVC.Model.GModel.ActionCounters;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;

/**
 * Represent the action counter
 */
public class ActionCounter {

    /**
     * In case of black cross, it indicates how many cells Lorenzo the Magnificent gains
     * In case of colour banner, it indicates how many development cards Lorenzo the Magnificent discards
     */
    private final int value;

    /**
     * Indicates the name of the counter
     */
    private final String counter;

    /**
     * Initializes the action counter
     * @param value value
     * @param counter counter
     */
    public ActionCounter(int value, String counter) {
        this.value = value;
        this.counter = counter;
    }

    /**
     * This function receives the components that can be modified by the action counters and subsequently
     * modifies the components depending on the method overridden in the subclasses
     * @param actionCountersDeck actionCountersDeck
     * @param playerboard playerboard
     * @param developmentCardsDecksGrid developmentCardsDecksGrid
     */
    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard,
                         DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
    }

    /**
     * Returns value
     * @return value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Returns the name of the counter
     * @return the name of the counter
     */
    public String getCounter() {
        return this.counter;
    }
}
