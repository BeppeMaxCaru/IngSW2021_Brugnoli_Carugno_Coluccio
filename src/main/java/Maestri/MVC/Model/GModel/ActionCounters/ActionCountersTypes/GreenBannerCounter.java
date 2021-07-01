package Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
/**
 * Represents the green action counter
 * It discards green development cards
 */
public class GreenBannerCounter extends ActionCounter {

    /**
     * Initialize the green action counter
     * @param value indicates how many green development cards this action counter discards
     */
    public GreenBannerCounter(int value) {
        super(value, "GreenBanner");
    }

    @Override
    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
        developmentCardsDecksGrid.removeDevelopmentCards(super.getValue());
    }
}
