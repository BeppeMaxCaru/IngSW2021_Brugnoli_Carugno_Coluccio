package Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
/**
 * Represents the purple action counter
 * It discards purple development cards
 */
public class PurpleBannerCounter extends ActionCounter {
    /**
     * Initialize the purple action counter
     * @param value indicates how many purple development cards this action counter discards
     */
    public PurpleBannerCounter(int value) {
        super(value, "PurpleBanner");
    }

    @Override
    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
        developmentCardsDecksGrid.removeDevelopmentCards(super.getValue());
    }




}
