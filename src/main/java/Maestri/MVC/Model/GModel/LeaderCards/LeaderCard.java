package Maestri.MVC.Model.GModel.LeaderCards;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.util.HashMap;
import java.util.Map;

public class LeaderCard {

    private boolean played;
    private final int victoryPoints;
    //private final int leaderCardType;

    public LeaderCard(int victoryPoints) {
        this.victoryPoints = victoryPoints;
        //this.leaderCardType = leaderCardType;
    }

    public boolean isPlayed() {
        return this.played;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    //Serve per giocare la carta
    //NB giocare la carta e attivarla sono due azioni diverse!!!!!!!!!!!!!!
    public boolean checkRequisites(Playerboard playerboard) {
        if (true) {
            return true;
        } else {
            return false;
        }
    }

    public void activateAbility(Playerboard playerboard) {

    }

    public void discard(Playerboard playerboard) {
        playerboard.getFaithPath().moveCross(1);
    }



}
