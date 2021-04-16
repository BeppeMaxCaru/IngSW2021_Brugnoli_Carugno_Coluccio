package Maestri.MVC.Model.GModel.LeaderCards;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

public class LeaderCard {

    /**
     * Attribute that indicates if LeaderCard is been played by the player
     */
    private boolean played;

    /**
     * Attribute that indicates victoryPoints of the LeaderCard
     */
    private final int victoryPoints;

    /**
     * Constructor assigns inpun victoryPoints to the associate attribute and sets that LeaderCard isn't played yet
     */
    public LeaderCard(int victoryPoints) {
        this.played=false;
        this.victoryPoints = victoryPoints;
    }

    /**
     * This method returns if LeaderCard is been played
     */
    public boolean isPlayed() {
        return this.played;
    }

    /**
     * This method sets the playing of the LeaderCard
     */
    public void setPlayed(boolean play){
        this.played=play;
    }

    public int getVictoryPoints(){
        return this.victoryPoints;
    }

    public boolean checkRequisites(Playerboard playerboard) {
        return false;
    }

    public void activateAbility(Player player) {
    }

    /**
     * If the player chooses to discard one LeaderCard, he obtains 1 faithPoint
     */
    public void discard(Playerboard playerboard) {
        playerboard.getFaithPath().moveCross(1);
    }

}
