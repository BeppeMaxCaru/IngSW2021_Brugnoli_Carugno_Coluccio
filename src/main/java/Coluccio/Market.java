package Coluccio;

import java.util.Scanner;

public class Market {

    private Marble[][] marketArrangement;
    private Marble excessMarble;

    final int row=3;
    final int column=4;

    public Market(){

        Marble[] marbleArray;

        marbleArray = new Marble[row*column+1];
        marketArrangement = new Marble[row][column];

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

        //Shuffle marbleArray

        int n=0;
        for(int i=0; i<row; i++)
        {
            for(int j=0; j<column; j++)
            {
                marketArrangement[i][j] = marbleArray[n];
                n++;
            }
        }
        excessMarble=marbleArray[n+1];

    }

    public Marble[][] getMarketArrangement() {
        return marketArrangement;
    }

    public void setMarketArrangement(Marble[][] marketArrangement, Marble excessMarble){
        int x;
        int i=0;
        int rc;
        Marble temp;
        temp=excessMarble;

        Scanner keyboard = new Scanner(System.in);

        System.out.println("Digita 0 per la riga, 1 per la colonna ed il rispettivo indice");
        //Scegli riga/colonna
        rc = keyboard.nextInt();
        //Scegli indice x
        x = keyboard.nextInt();

        if(rc==0)//Scelta riga x
        {
            excessMarble=marketArrangement[x][i];
            for(i=0; i<column-1; i++)
                marketArrangement[x][i]=marketArrangement[x][i+1];
            i++;
            marketArrangement[x][i]=temp;
        }

        if(rc==1)//Scelta colonna x
        {
            excessMarble=marketArrangement[i][x];
            for(i=0; i<row-1; i++)
                marketArrangement[i][x]=marketArrangement[i+1][x];
            i++;
            marketArrangement[i][x]=temp;
        }

    }

    public Marble getExcessMarble() {
        return excessMarble;
    }
}
