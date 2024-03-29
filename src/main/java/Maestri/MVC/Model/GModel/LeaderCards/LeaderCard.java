package Maestri.MVC.Model.GModel.LeaderCards;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.io.Serializable;

/**
 * Composes the leader card deck
 */
public class LeaderCard implements Serializable {

    /**
     * True if the leader card has been played
     */
    private boolean played;

    /**
     * Victory points of the leader card
     */
    private final int victoryPoints;

    /**
     * Initializes the leader card as not played and its victory points
     * @param victoryPoints victory points of the leader card
     */
    public LeaderCard(int victoryPoints) {
        this.played=false;
        this.victoryPoints = victoryPoints;
    }

    /**
     * Returns true if the leader card has been played
     * @return true if the leader card has been played
     */
    public boolean isPlayed() {
        return this.played;
    }

    /**
     * Sets the leader card as played
     */
    public void setPlayed(boolean play){
        this.played=play;
    }

    /**
     * Returns the leader card's victory points
     * @return the leader card's victory points
     */
    public int getVictoryPoints(){
        return this.victoryPoints;
    }

    /**
     * Checks if the player has on his player board the requisites needed to play the leader card
     * @param playerboard player's playerBoard
     * @return true if the player can play the leader card
     */
    public boolean checkRequisites(Playerboard playerboard) {
        return false;
    }

    /**
     * Gives the permanent perk of the leader card to the player
     * @param playerboard player's playerBoard
     */
    public void activateAbility(Playerboard playerboard) {
    }

    /**
     * Prints the leader card
     */
    public void printLeaderCard(){
    }

    /**
     * Returns the image of the card
     * @return the image of the card
     */
    public String getImage() { return ""; }

}
