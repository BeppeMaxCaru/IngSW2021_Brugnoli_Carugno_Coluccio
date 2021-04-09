package Carugno.LeaderCards;

import Brugnoli.Playerboard;

public class LeaderCard {

    private boolean played;
    private final int victoryPoints;

    public LeaderCard(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public boolean isPlayed() {
        return this.played;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean checkRequisites(Playerboard playerboard) {
        if () {

        } else {
            return false;
        }
    }

    public void activateAbility(Playerboard playerboard) {

    }

    public void discard() {

    }



}
