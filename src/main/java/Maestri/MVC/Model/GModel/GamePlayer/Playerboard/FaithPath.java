package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;


import java.util.HashMap;
import java.util.Map;

/**
 * Represents the player board's path where to move the player's red cross
 */
public class FaithPath {

    /**
     * Contains all the 25 cells of the path
     */
    private final Cell[] faithPathTrack;

    /**
     * Red cross position on the faith path
     */
    private int crossPosition;

    /**
     * Tracks where vatican's card are not available
     */
    public Map<Integer, Integer> discardVaticanCard;

    /**
     * Initializes all the 25 cells of the faith path and the starting position of each player's red cross
     */
    public FaithPath() {

        this.faithPathTrack = new Cell[25];
        this.faithPathTrack[0] = new Cell(0,false,0);
        this.faithPathTrack[1] = new Cell(0, false, 0);
        this.faithPathTrack[2] = new Cell(0, false, 0);
        this.faithPathTrack[3] = new Cell(1, false, 0);
        this.faithPathTrack[4] = new Cell(1, false,  0);
        this.faithPathTrack[5] = new Cell(1, false, 2);
        this.faithPathTrack[6] = new Cell(2, false, 2);
        this.faithPathTrack[7] = new Cell(2, false, 2);
        this.faithPathTrack[8] = new Cell(2, true,  2);
        this.faithPathTrack[9] = new Cell(4, false, 0);
        this.faithPathTrack[10] = new Cell(4, false,  0);
        this.faithPathTrack[11] = new Cell(4, false,  0);
        this.faithPathTrack[12] = new Cell(6, false,  3);
        this.faithPathTrack[13] = new Cell(6, false,  3);
        this.faithPathTrack[14] = new Cell(6, false,  3);
        this.faithPathTrack[15] = new Cell(9, false,  3);
        this.faithPathTrack[16] = new Cell(9, true, 3);
        this.faithPathTrack[17] = new Cell(9, false,  0);
        this.faithPathTrack[18] = new Cell(12, false,  0);
        this.faithPathTrack[19] = new Cell(12, false,  4);
        this.faithPathTrack[20] = new Cell(12, false,  4);
        this.faithPathTrack[21] = new Cell(16, false, 4);
        this.faithPathTrack[22] = new Cell(16, false,  4);
        this.faithPathTrack[23] = new Cell(16, false,  4);
        this.faithPathTrack[24] = new Cell(20, true,  4);

        this.crossPosition = 0;

        this.discardVaticanCard = new HashMap<>();
        this.discardVaticanCard.put(2, 2);
        this.discardVaticanCard.put(3, 3);
        this.discardVaticanCard.put(4, 4);
    }

    /**
     * Returns the faith path track
     * @return the faith path track
     */
    public Cell[] getFaithPathTrack() {
        return this.faithPathTrack;
    }

    /**
     * Returns the red cross position
     * @return the red cross position
     */
    public int getCrossPosition() {
        return this.crossPosition;
    }

    /**
     * Advances the red cross of the player
     * @param i - number of positions that the player's red cross advances
     */
    public void moveCross(int i) {
        this.crossPosition = this.crossPosition + i;
    }

    /**
     * Assigns or not the vatican section victory points depending on the red cross position
     * @param crossPositionPlayerX - red cross position of the player
     * @param playerboard - the player's player board that receives the vatican section victory points
     */
    public void checkRelationWithVatican(int crossPositionPlayerX, Playerboard playerboard) {
        if(this.discardVaticanCard.get(this.faithPathTrack[crossPositionPlayerX].getVaticanSectionVictoryPoints()) != 0) {
            if (this.crossPosition > crossPositionPlayerX || this.faithPathTrack[this.crossPosition].getVaticanSectionVictoryPoints() == faithPathTrack[crossPositionPlayerX].getVaticanSectionVictoryPoints())
                playerboard.sumVictoryPoints(this.faithPathTrack[crossPositionPlayerX].getVictoryPoints());
            else
                this.discardVaticanCard.put(this.faithPathTrack[crossPositionPlayerX].getVaticanSectionVictoryPoints(), 0);
        }
    }
}
