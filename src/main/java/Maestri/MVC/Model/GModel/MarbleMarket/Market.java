package Maestri.MVC.Model.GModel.MarbleMarket;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents the Market game component
 */
public class Market {

    /**
     * Grid containing 12 marbles
     */
    private final Marble[][] marketArrangement;
    /**
     * 13th excess marble used to shuffle the market after someone draws
     */
    private Marble excessMarble;
    /**
     * Rows composing the market grid
     */
    final int row = 3;
    /**
     * Columns composing the market grid
     */
    final int column = 4;

    /**
     * Initializes and shuffles the market
     */
    public Market() {

        Marble[] marbleArray;
        marbleArray = new Marble[row * column + 1];
        this.marketArrangement = new Marble[row][column];

        /*
        I create an array that contains all the marbles, for mixing randomly the arrangement of the market
        There are 4 white m., 1 red m., 2 yellow m., 2 blue m., 2 grey m. and 2 violet m.
         */
        marbleArray[0] = new WhiteMarble();
        marbleArray[1] = new WhiteMarble();
        marbleArray[2] = new WhiteMarble();
        marbleArray[3] = new WhiteMarble();
        marbleArray[4] = new RedMarble();
        marbleArray[5] = new BlueMarble();
        marbleArray[6] = new BlueMarble();
        marbleArray[7] = new GreyMarble();
        marbleArray[8] = new GreyMarble();
        marbleArray[9] = new YellowMarble();
        marbleArray[10] = new YellowMarble();
        marbleArray[11] = new PurpleMarble();
        marbleArray[12] = new PurpleMarble();

        //Instructions for mixing the array of marbles, converting it into a list
        List<Marble> marbleList = Arrays.asList(marbleArray);
        Collections.shuffle(marbleList);
        marbleList.toArray(marbleArray);

        //I assign to the market arrangement all the marble array positions
        int n = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                this.marketArrangement[i][j] = marbleArray[n];
                n++;
            }
        }
        //The 13th marble of the array becomes the excess marble
        this.excessMarble = marbleArray[n];

    }

    /**
     * Returns the current market state
     * @return the current market state
     */
    public Marble[][] getMarketArrangement() {
        return this.marketArrangement;
    }

    /**
     * Returns the current excess marble
     * @return the current excess marble
     */
    public Marble getExcessMarble(){
        return excessMarble;
    }

    /**
     * Updates the market after a player draws marbles from a row
     * @param row          - row from where the player draws the marbles
     * @param players      - players playing the game
     * @param playerNumber - number of the current player that is drawing marbles
     */
    public void updateRow(int row, Player[] players, int playerNumber) {
        int i = 0;
        Marble temp;//Temporary marble to save the previous excess marble
        temp = this.excessMarble;

        //I save the excess marble on a temporary position.
        this.excessMarble=this.marketArrangement[row][i];

        /*
         It is activated the effect of each marble of the row.
         Every marble slides to an upper position of the market grid.
         */
        for (i = 0; i < column-1 ; i++){
            this.marketArrangement[row][i].drawMarble(players, playerNumber);
            this.marketArrangement[row][i] = this.marketArrangement[row][i+1];
        }

        //The previous excess Marble is inserted into the lowest position of the market row
        i++;
        this.marketArrangement[row][i].drawMarble(players, playerNumber);
        this.marketArrangement[row][i] = temp;
    }

    /**
     * Updates the market after a player draws marbles from a column
     * @param column - column from where the player draws the marbles
     * @param players - players playing the game
     * @param playerNumber - number of the current player that is drawing marbles
     */
    public void updateColumn(int column, Player[] players, int playerNumber){
        int i=0;
        Marble temp;//Temporary marble to save the previous excess marble
        temp=this.excessMarble;

        //I save the excess marble on a temporary position.
        this.excessMarble=this.marketArrangement[i][column];

        for(i=0; i<row-1; i++)
        {
            /*
             It is activated the effect of every marble of the column.
             Every marble slides to an upper position of the market grid
             */
            this.marketArrangement[i][column].drawMarble(players, playerNumber);
            this.marketArrangement[i][column]=this.marketArrangement[i+1][column];
        }

        //The previous excess Marble is inserted into the lowest position of the market column
        i++;
        this.marketArrangement[i][column].drawMarble(players, playerNumber);
        this.marketArrangement[i][column]=temp;
    }


}
