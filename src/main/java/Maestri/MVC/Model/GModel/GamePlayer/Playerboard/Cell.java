package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import java.io.Serializable;

/**
 * Composes the faith path
 */
public class Cell implements Serializable {

    /**
     * Victory points of the cell
     */
    private final int victoryPoints;
    /**
     * True if the cell is a pope space
     */
    private final boolean popeSpace;
    /**
     * Victory points of the vatican section's card
     */
    private final int vaticanSectionVictoryPoints;

    /**
     * Initializes the cell
     * @param victoryPoints victory points of the cell
     * @param popeSpace true if the cell is a pope space
     * @param vaticanSectionVictoryPoints victory points of the vatican section's card
     */
    public Cell(int victoryPoints, boolean popeSpace, int vaticanSectionVictoryPoints) {
        this.victoryPoints = victoryPoints;
        this.popeSpace = popeSpace;
        this.vaticanSectionVictoryPoints = vaticanSectionVictoryPoints;
    }

    /**
     * Returns the victory points of the cell
     * @return victory points of the cell
     */
    public int getVictoryPoints() {
        return this.victoryPoints;
    }


    /**
     * Returns true if the cell is a pope space
     * @return true if the cell is a pope space
     */
    public boolean isPopeSpace() {
        return this.popeSpace;
    }

    /**
     * Returns the victory points of the vatican section's card
     * @return the victory points of the vatican section's card
     */
    public int getVaticanSectionVictoryPoints() {
        return this.vaticanSectionVictoryPoints;
    }
}

