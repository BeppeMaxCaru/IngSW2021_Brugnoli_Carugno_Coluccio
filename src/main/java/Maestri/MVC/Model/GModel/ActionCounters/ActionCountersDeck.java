package Maestri.MVC.Model.GModel.ActionCounters;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//
public class ActionCountersDeck {

    private int top;
    private ActionCounter[] actionCountersDeck;

    public ActionCountersDeck() {
        this.actionCountersDeck = new ActionCounter[6];
        this.actionCountersDeck[0] = new BlackCross1(1);
        this.actionCountersDeck[1] = new BlackCross2(2);
        this.actionCountersDeck[2] = new BlueBannerCounter(2);
        this.actionCountersDeck[3] = new GreenBannerCounter(2);
        this.actionCountersDeck[4] = new PurpleBannerCounter(2);
        this.actionCountersDeck[5] = new YellowBannerCounter(2);
        this.top = 5;
    }

    public ActionCounter drawCounter() {
        return this.actionCountersDeck[this.top--];
    }

    public void shuffle() {
        List<ActionCounter> actionCountersList = Arrays.asList(this.actionCountersDeck);
        Collections.shuffle(actionCountersList);
        actionCountersList.toArray(this.actionCountersDeck);
        this.top = 5;
    }

}
