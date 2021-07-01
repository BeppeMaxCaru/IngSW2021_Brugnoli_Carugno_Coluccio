package Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;

/**
 * Represents the first black action counter
 */
public class BlackCross1 extends ActionCounter {

    /**
     * Initializes the first black counter
     * @param value indicates how many positions Lorenzo gains
     */
    public BlackCross1 (int value) {
        super(value, "BlackCross1");
    }

    @Override
    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
        //Moves black cross of 1 position and shuffles the deck
        actionCountersDeck.shuffle();
        playerboard.getFaithPath().moveCross(super.getValue());
    }
}
