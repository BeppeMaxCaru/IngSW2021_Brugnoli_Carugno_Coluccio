package Maestri.MVC.Model.GModel.LeaderCards;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
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

    public boolean checkRequisites(Playerboard playerboard) {
        return false;
    }

    public void activateAbility(Player player) {
    }

    public void discard(Playerboard playerboard) {
        playerboard.getFaithPath().moveCross(1);
    }

}
