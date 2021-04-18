package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

public class Cell {

    /**
     * Cell's victory points.
     */
    private final int victoryPoints;
    /**
     * Cell where there's a relation in Vatican.
     */
    private final boolean popeSpace;
    /**
     * Victory points of the vatican section's card.
     */
    private final int vaticanSectionVictoryPoints;


    public Cell(int victoryPoints, boolean popeSpace, int vaticanSectionVictoryPoints) {
        this.victoryPoints = victoryPoints;
        this.popeSpace = popeSpace;
        this.vaticanSectionVictoryPoints = vaticanSectionVictoryPoints;
    }

    /**
     * @return the cell's victory points.
     */

    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * @return a boolean that verify if the cross is in a pope space.
     */

    public boolean isPopeSpace() {
        return popeSpace;
    }

    /**
     * @return the victory points of the vatican section's card.
     */

    public int getVaticanSectionVictoryPoints() {
        return vaticanSectionVictoryPoints;
    }
}

