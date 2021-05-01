package Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.io.PrintWriter;

public class GreenBannerCounter extends ActionCounter {

    public GreenBannerCounter(int value) {
        super(value);
    }

    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid, PrintWriter out) {
        developmentCardsDecksGrid.removeDevelopmentCards(0);
        out.println("Drawn GreenBanner");
    }
}
