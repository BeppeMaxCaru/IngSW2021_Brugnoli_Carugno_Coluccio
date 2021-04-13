package Maestri.MVC.Model.GModel.MarbleMarket;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Market is composed by the grid of 12 marbles (3r x 4c) and the 13th excess marble
 */
public class Market {

    /**
     * The marketArrangement stores the grid composed by 12 Marbles
     */
    private final Marble[][] marketArrangement;
    /**
     * The excessMarble stores the 13th marble
     */
    private Marble excessMarble;

    final int row=3;
    final int column=4;

    /**
     * Constructor of class Market
     */
    public Market(){

        Marble[] marbleArray;
        marbleArray = new Marble[row*column+1];
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
        marbleArray[11] = new VioletMarble();
        marbleArray[12] = new VioletMarble();

        //Instructions for mixing the array of marbles, converting it into a list
        List<Marble> marbleList = Arrays.asList(marbleArray);
        Collections.shuffle(marbleList);
        marbleList.toArray(marbleArray);

        //I assign to the market arrangement all the marble array positions
        int n=0;
        for(int i=0; i<row; i++)
        {
            for(int j=0; j<column; j++)
            {
                this.marketArrangement[i][j] = marbleArray[n];
                n++;
            }
        }
        //The 13th marble of the array becomes the excess marble
        this.excessMarble=marbleArray[n+1];

    }

    /**
     * Method that updates the market arrangement if the player chooses to pick a row from the Market
     */
    public void updateRow(int x, GameModel gameModel, int playerNumber){
        int i=0;
        Marble temp;//Temporary marble to save the previous excess marble
        temp=this.excessMarble;

        //I save the excess marble on a temporary position. It is activated the effect of the first marble of the row, that becomes the excess marble
            this.marketArrangement[x][i].drawMarble(gameModel, playerNumber);
            this.excessMarble=this.marketArrangement[x][i];
            for(i=0; i<column-1; i++)
            {
                //It is activated the effect of every marble of the row. Every marble slides to an upper position of the market grid
                this.marketArrangement[x][i].drawMarble(gameModel, playerNumber);
                this.marketArrangement[x][i]=this.marketArrangement[x][i+1];
            }
            //The previous excess Marble is inserted into the lower position of the market row
            i++;
            this.marketArrangement[x][i]=temp;
        }

    /**
     * Method that updates the market arrangement if the player chooses to pick a row from the Market
     */
    public void updateColumn(int x, GameModel gameModel, int playerNumber){
        int i=0;
        Marble temp;//Temporary marble to save the previous excess marble
        temp=this.excessMarble;

        //I save the excess marble on a temporary position. It is activated the effect of the first marble of the column, that becomes the excess marble
        this.marketArrangement[i][x].drawMarble(gameModel, playerNumber);
        this.excessMarble=this.marketArrangement[i][x];
        for(i=0; i<row-1; i++)
        {
            //It is activated the effect of every marble of the column. Every marble slides to an upper position of the market grid
            this.marketArrangement[i][x].drawMarble(gameModel, playerNumber);
            this.marketArrangement[i][x]=this.marketArrangement[i+1][x];
        }
        //The previous excess Marble is inserted into the lower position of the market column
        i++;
        this.marketArrangement[i][x]=temp;
    }

}
