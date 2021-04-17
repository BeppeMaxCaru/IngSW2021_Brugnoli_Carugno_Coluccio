package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;


import java.util.HashMap;
import java.util.Map;

public class FaithPath {

    /**
     * Faith path as a cell's array.
     */
    private final Cell[] faithPath;
    /**
     * Cross's position on faith path.
     */
    private int crossPosition;
    /**
     * A map where are disabled the Vatican's cards.
     */
    public Map<Integer, Integer> discardVaticanCard;

    public FaithPath() {

        this.faithPath = new Cell[25];
        this.faithPath[0] = new Cell(0,false,0);
        this.faithPath[1] = new Cell(0, false, 0);
        this.faithPath[2] = new Cell(0, false, 0);
        this.faithPath[3] = new Cell(1, false, 0);
        this.faithPath[4] = new Cell(1, false,  0);
        this.faithPath[5] = new Cell(1, false, 2);
        this.faithPath[6] = new Cell(2, false, 2);
        this.faithPath[7] = new Cell(2, false, 2);
        this.faithPath[8] = new Cell(2, true,  2);
        this.faithPath[9] = new Cell(4, false, 0);
        this.faithPath[10] = new Cell(4, false,  0);
        this.faithPath[11] = new Cell(4, false,  0);
        this.faithPath[12] = new Cell(6, false,  3);
        this.faithPath[13] = new Cell(6, false,  3);
        this.faithPath[14] = new Cell(6, false,  3);
        this.faithPath[15] = new Cell(9, false,  3);
        this.faithPath[16] = new Cell(9, true, 3);
        this.faithPath[17] = new Cell(9, false,  0);
        this.faithPath[18] = new Cell(12, false,  0);
        this.faithPath[19] = new Cell(12, false,  4);
        this.faithPath[20] = new Cell(12, false,  4);
        this.faithPath[21] = new Cell(16, false, 4);
        this.faithPath[22] = new Cell(16, false,  4);
        this.faithPath[23] = new Cell(16, false,  4);
        this.faithPath[24] = new Cell(20, true,  4);

        this.crossPosition = 0;

        discardVaticanCard = new HashMap<>();
        discardVaticanCard.put(2, 2);
        discardVaticanCard.put(3, 3);
        discardVaticanCard.put(4, 4);
    }

    /**
     * @return faith path's cell.
     */

    public Cell[] getFaithPath() {
        return this.faithPath;
    }

    /**
     * @return cross position on faith path.
     */

    public int getCrossPosition( ) {
        return this.crossPosition;
    }

    /**
     * @param i number of positions the cross has to advance.
     */

    public void moveCross(int i) {
        crossPosition = crossPosition + i;
    }

    /**
     * This method checks on the faith path if the player has the possibility to turn (or remove) the card after a relation in Vatican.
     * @param crossPositionPlayerX the cross's position of the player that just had a relation in Vatican.
     * @param playerboard the playerboard to increase the total number of victory points if the player can turn the card.
     */

    public void checkRelationWithVatican(int crossPositionPlayerX, Playerboard playerboard) {
        if(discardVaticanCard.get(faithPath[crossPositionPlayerX].getVaticanSectionVictoryPoints()) != 0) {
            if (crossPosition > crossPositionPlayerX || faithPath[crossPosition].getVaticanSectionVictoryPoints() == faithPath[crossPositionPlayerX].getVaticanSectionVictoryPoints())
                playerboard.sumVictoryPoints(faithPath[crossPositionPlayerX].getVictoryPoints());
            else
                discardVaticanCard.put(faithPath[crossPositionPlayerX].getVaticanSectionVictoryPoints(), 0);
        }
    }
}
