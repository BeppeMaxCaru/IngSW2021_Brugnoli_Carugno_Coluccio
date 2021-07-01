package Maestri.MVC.Model.GModel.ActionCounters;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersTypes.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Contains all the available action counters of the game
 */
public class ActionCountersDeck {

    /**
     * Indicates the upper position of the deck
     */
    private int top;

    /**
     * Contains all the action counters
     */
    private final ActionCounter[] actionCountersDeck;

    /**
     * Initializes the entire action counters starting deck
     */
    public ActionCountersDeck() {
        this.actionCountersDeck = new ActionCounter[6];
        this.actionCountersDeck[0] = new BlackCross1(1);
        this.actionCountersDeck[1] = new BlackCross2(2);
        this.actionCountersDeck[2] = new BlueBannerCounter(2);
        this.actionCountersDeck[3] = new GreenBannerCounter(2);
        this.actionCountersDeck[4] = new PurpleBannerCounter(2);
        this.actionCountersDeck[5] = new YellowBannerCounter(2);
        shuffle();
    }

    /**
     * Returns the upper action counter
     * @return the upper action counter
     */
    public ActionCounter drawCounter() {
        return this.actionCountersDeck[this.top--];
    }

    /**
     * Shuffles the entire action counter deck
     */
    public void shuffle() {
        List<ActionCounter> actionCountersList = Arrays.asList(this.actionCountersDeck);
        Collections.shuffle(actionCountersList);
        actionCountersList.toArray(this.actionCountersDeck);
        this.top = 5;
    }

    /**
     * Returns the action counters deck
     * @return the action counters deck
     */
    public ActionCounter[] getActionCountersDeck() {
        return actionCountersDeck;
    }

    /**
     * Returns the upper action counter position
     * @return the upper action counter position
     */
    public int getTop() {
        return top;
    }
}
