package Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

/**
 * Represents the yellow action counter
 * It discards yellow development cards
 */
public class YellowBannerCounter extends ActionCounter {

    /**
     * Initialize the yellow action counter
     * @param value indicates how many yellow development cards this action counter discards
     */
    public YellowBannerCounter(int value) {
        super(value, "YellowBanner");
    }

    @Override
    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
        developmentCardsDecksGrid.removeDevelopmentCards(super.getValue());
    }
}
