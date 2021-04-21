package Maestri.MVC.Model.GModel.LeaderCards;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

/**
 * Composes the leader card deck
 */
public class LeaderCard {

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
     * @param victoryPoints - victory points of the leader card
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
     * @param playerboard - player's player board
     * @return true if the player can play the leader card
     */
    public boolean checkRequisites(Playerboard playerboard) {
        return false;
    }

    /**
     * Gives the permanent perk of the leader card to the player
     * @param playerboard - player's playerboard
     */
    public void activateAbility(Playerboard playerboard) {
    }

    /**
     * Moves the player's red cross one cell forward on the faith path if the player chooses to discard the leader card
     * @param playerboard - player's player board
     */
    public void discard(Playerboard playerboard) {
        playerboard.getFaithPath().moveCross(1);
    }

}
