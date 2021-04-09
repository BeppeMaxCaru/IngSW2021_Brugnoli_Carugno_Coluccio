package Brugnoli;

public class FaithPath {

    private final Cell[] faithPath;
    private int crossPosition;

    public FaithPath(Cell[] faithPath) {

        this.faithPath = new Cell[25];
        this.faithPath[0] = new Cell(0,false,
                false,0);
        this.faithPath[1] = new Cell();
        this.faithPath[2] = new Cell();

        this.crossPosition = 0;
    }

    public Cell[] getFaithPath() {
        return this.faithPath;
    }

    public int getCrossPosition( ) {
        return this.crossPosition;
    }

    public void moveCross(int redCrossPosition) {

    }

    public boolean checkPopeSpace(Cell[] faithPath, int crossPosition) {

    }

    public boolean checkVictoryPoints(Cell[] faithPath, int crossPosition) {

    }

    public int getVictoryPoints(Cell[] faithPath, int crossPosition) {

    }

    public boolean checkRelationWithVatican(Cell[] faithPath, int crossPosition) {

    }




}
