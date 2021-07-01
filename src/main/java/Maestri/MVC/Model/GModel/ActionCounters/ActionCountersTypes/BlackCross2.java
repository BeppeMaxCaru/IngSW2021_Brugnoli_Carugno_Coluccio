package Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
/**
 * Represents the second black action counter
 */
public class BlackCross2 extends ActionCounter {

    /**
     * Initialize the second black action counter
     * @param value indicates how many positions Lorenzo gains
     */
    public BlackCross2 (int value) {
        super(value, "BlackCross2");
    }

    @Override
    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
        playerboard.getFaithPath().moveCross(super.getValue());
    }
}
