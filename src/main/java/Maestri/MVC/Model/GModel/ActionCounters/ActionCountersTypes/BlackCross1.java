package Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;

import java.io.PrintWriter;

public class BlackCross1 extends ActionCounter {

    public BlackCross1 (int value) {
        super(value);
    }

    public void activate(ActionCountersDeck actionCountersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid, PrintWriter out) {
        //modifica la playerboard spostando la croce nera
        actionCountersDeck.shuffle();
        playerboard.getFaithPath().moveCross(1);
        out.println("Drawn BlackCross1");
    }

}
