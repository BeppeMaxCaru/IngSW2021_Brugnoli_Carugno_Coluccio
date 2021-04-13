package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

public class Cell {

    private final int victoryPoints;
    private final boolean popeSpace;
    private final boolean vaticanSection;
    private final int vaticanSectionVictoryPoints;


    public Cell(int victoryPoints, boolean popeSpace, boolean vaticanSection, int vaticanSectionVictoryPoints) {
        this.victoryPoints = victoryPoints;
        this.popeSpace = popeSpace;
        this.vaticanSection = vaticanSection;
        this.vaticanSectionVictoryPoints = vaticanSectionVictoryPoints;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isPopeSpace() {
        return popeSpace;
    }

    public boolean isVaticanSection() {
        return vaticanSection;
    }

    public int getVaticanSectionVictoryPoints() {
        return vaticanSectionVictoryPoints;
    }
}

