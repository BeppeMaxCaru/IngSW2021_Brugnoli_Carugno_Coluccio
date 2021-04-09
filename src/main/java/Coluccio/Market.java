package Coluccio;

import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Market is composed by the grid of 12 marbles (3r x 4c) and the 13th excess marble
 */
public class Market {

    private Marble[][] marketArrangement;
    private Marble excessMarble;

    final int row=3;
    final int column=4;

    public Market(){

        Marble[] marbleArray;

        marbleArray = new Marble[row*column+1];
        marketArrangement = new Marble[row][column];

        /**
         * I create an array that contains all the marbles, for mixing randomly the arrangement of the market
         * There are 4 white m., 1 red m., 2 yellow m., 2 blue m., 2 grey m. and 2 violet m.
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

        /**
         * Instructions for mixing the array of marbles, converting it into a list
         */
        List<Marble> marbleList = Arrays.asList(marbleArray);
        Collections.shuffle(marbleList);
        marbleList.toArray(marbleArray);

        /**
         * I assign to the market arrangement all the marble array positions
         */
        int n=0;
        for(int i=0; i<row; i++)
        {
            for(int j=0; j<column; j++)
            {
                marketArrangement[i][j] = marbleArray[n];
                n++;
            }
        }
        /**
         * The 13th marble of the array becomes the excess marble
         */
        excessMarble=marbleArray[n+1];

    }

    /**
     * Method that returns the arrangement of the market grid
     */
    public Marble[][] getMarketArrangement() {
        return marketArrangement;
    }

    /**
     * Method that updates the market arrangement managing the user's choice of resources
     */
    public void setMarketArrangement(Marble[][] marketArrangement, Marble excessMarble){
        int x;//To choose index
        int i=0;
        int rc;//Row/Column choice
        Marble temp;//Temporary marble to save the previous excess marble
        temp=excessMarble;

        Scanner keyboard = new Scanner(System.in);

        /**
         * The program asks to the user to choose the row/column of resources to collect
         */
        System.out.println("Digit 0 for the row, 1 for the column and the row/column index");
        //Scegli riga/colonna
        rc = keyboard.nextInt();
        //Scegli indice x
        x = keyboard.nextInt();

        /**
         * If the users select to pick a row
         */
        if(rc==0)//Scelta riga x
        {
            /**
             * I save the excess marble on a temporary position
             * It is activated the effect of the first marble of the row, that becomes the excess marble
             */
            marketArrangement[x][i].drawMarble();
            excessMarble=marketArrangement[x][i];
            for(i=0; i<column-1; i++)
            {
                /**
                 * It is activated the effect of every marble of the row
                 * Every marble slides to an upper position of the market grid
                 */
                marketArrangement[x][i].drawMarble();
                marketArrangement[x][i]=marketArrangement[x][i+1];
            }
            /**
             * The previous excess Marble is inserted into the lower position of the market row
             */
            i++;
            marketArrangement[x][i]=temp;
        }

        /**
         * If the users select to pick a column
         */
        if(rc==1)//Scelta colonna x
        {
            /**
             * I save the excess marble on a temporary position
             * It is activated the effect of the first marble of the column, that becomes the excess marble
             */
            marketArrangement[i][x].drawMarble();
            excessMarble=marketArrangement[i][x];
            for(i=0; i<row-1; i++)
            {
                /**
                 * It is activated the effect of every marble of the column
                 * Every marble slides to an upper position of the market grid
                 */
                marketArrangement[i][x].drawMarble();
                marketArrangement[i][x]=marketArrangement[i+1][x];
            }
            /**
             * The previous excess Marble is inserted into the lower position of the market column
             */
            i++;
            marketArrangement[i][x]=temp;
        }
    }

    /**
     * Method the returns the excess marble of the market
     */
    public Marble getExcessMarble() {
        return excessMarble;
    }
}
