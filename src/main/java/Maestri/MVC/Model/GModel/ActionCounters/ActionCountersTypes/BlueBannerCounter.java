package Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.io.PrintWriter;

public class BlueBannerCounter extends ActionCounter {

    public BlueBannerCounter(int value) {
        super(value);
    }

    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid, PrintWriter out) {
        developmentCardsDecksGrid.removeDevelopmentCards(1);
        out.println("Drawn BlueBanner");
    }
}
