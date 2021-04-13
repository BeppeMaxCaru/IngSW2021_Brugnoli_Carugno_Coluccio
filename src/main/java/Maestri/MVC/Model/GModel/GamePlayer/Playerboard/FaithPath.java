package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

public class FaithPath {

    private final Cell[] faithPath;
    private int crossPosition;

    public FaithPath(Cell[] faithPath) {

        this.faithPath = new Cell[25];
        this.faithPath[0] = new Cell(0,false, false,0);
        this.faithPath[1] = new Cell(0, false, false, 0);
        this.faithPath[2] = new Cell(0, false, false, 0);
        this.faithPath[3] = new Cell(1, false, false, 0);
        this.faithPath[4] = new Cell(0, false, false, 0);
        this.faithPath[5] = new Cell(0, false, true, 2);
        this.faithPath[6] = new Cell(2, false, true, 2);
        this.faithPath[7] = new Cell(0, false, true, 2);
        this.faithPath[8] = new Cell(0, true, true, 2);
        this.faithPath[9] = new Cell(4, false, false, 0);
        this.faithPath[10] = new Cell(0, false, false, 0);
        this.faithPath[11] = new Cell(0, false, false, 0);
        this.faithPath[12] = new Cell(6, false, true, 3);
        this.faithPath[13] = new Cell(0, false, true, 3);
        this.faithPath[14] = new Cell(0, false, true, 3);
        this.faithPath[15] = new Cell(9, false, true, 3);
        this.faithPath[16] = new Cell(0, true, true, 3);
        this.faithPath[17] = new Cell(0, false, false, 0);
        this.faithPath[18] = new Cell(12, false, false, 0);
        this.faithPath[19] = new Cell(0, false, true, 4);
        this.faithPath[20] = new Cell(0, false, true, 4);
        this.faithPath[21] = new Cell(16, false, true, 4);
        this.faithPath[22] = new Cell(0, false, true, 4);
        this.faithPath[23] = new Cell(0, false, true, 4);
        this.faithPath[24] = new Cell(20, true, true, 4);

        this.crossPosition = 0;
    }

    public Cell[] getFaithPath() {
        return this.faithPath;
    }

    public int getCrossPosition( ) {
        return this.crossPosition;
    }

    /** This method moves the red cross on the faith path. */

    public void moveCross(int i) {
        crossPosition = crossPosition + i;
    }

    public void generalCheckOnFaithPath(Playerboard playerboard) {
        if(faithPath[crossPosition].getVictoryPoints() != 0)
            getVictoryPoints(crossPosition, playerboard);
        if(faithPath[crossPosition].isPopeSpace())
            // metodo model
    }

    /** This method adds victory points, from the faith path's cell, to the total count on the playerboard. */

    public void getVictoryPoints(int crossPosition, Playerboard playerboard) {
        playerboard.setVictoryPoints(faithPath[crossPosition].getVictoryPoints());
    }

    public boolean checkRelationWithVatican(Cell[] faithPath, int crossPosition) {
        // Bisogna passargli la crossPosition del giocatore che ha fatto scattare il rapporto con il vaticano, come si fa??

    }
}
