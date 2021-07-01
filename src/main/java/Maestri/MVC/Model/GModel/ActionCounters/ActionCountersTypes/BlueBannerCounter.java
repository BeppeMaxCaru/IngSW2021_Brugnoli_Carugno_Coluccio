package Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
/**
 * Represents the blue action counter
 * It discards blue development cards
 */
public class BlueBannerCounter extends ActionCounter {

    /**
     * Initialize the blue action counter
     * @param value indicates how many blue development cards this action counter discards
     */
    public BlueBannerCounter(int value) {
        super(value, "BlueBanner");
    }

    @Override
    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
        developmentCardsDecksGrid.removeDevelopmentCards(super.getValue());
    }
}
