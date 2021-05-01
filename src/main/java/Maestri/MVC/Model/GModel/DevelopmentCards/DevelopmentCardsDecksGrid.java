package Maestri.MVC.Model.GModel.DevelopmentCards;

import java.io.PrintWriter;
import java.util.*;

/**
 * Represents all the development cards of the game
 */
public class DevelopmentCardsDecksGrid {
    /**
     * All the development cards grouped orderly in decks according to their level and colour
     */
    private final DevelopmentCard[][][] developmentCardsDecks;
    /**
     * All the available development card colours
     */
    private final Map<String, Integer> developmentCardsColours;
    /**
     * All the available development cards levels
     */
    private final Collection<Integer> developmentCardsLevels;

    //private final Scanner consoleInput = new Scanner(System.in);

    /**
     * Initializes the development cards in a 3(levels)x4(colours) ordered matrix and groups them in decks of 4
     */
    public DevelopmentCardsDecksGrid() {
        //Initializes the grid where they are distributed
        this.developmentCardsDecks = new DevelopmentCard[3][4][4];
        //Initializes the possible colours and levels
        this.developmentCardsColours = new HashMap<>();
        this.developmentCardsLevels = new HashSet<>();
        //Level 1 GREEN
        this.developmentCardsDecks[2][0][0] = new DevelopmentCard("GREEN",1,
                0,0,0,2,
                1,0,0,0,
                0,0,0,0,
                1,1);
        this.developmentCardsDecks[2][0][1] = new DevelopmentCard("GREEN",1,
                0,1,1,1,
                0,1,0,0,
                0,0,1,0,
                0,2);
        this.developmentCardsDecks[2][0][2] = new DevelopmentCard("GREEN",1,
                0,0,0,3,
                0,0,2,0,
                1,1,0,1,
                0,3);
        this.developmentCardsDecks[2][0][3] = new DevelopmentCard("GREEN",1,
                2,0,0,2,
                0,1,1,0,
                2,0,0,0,
                1,4);

        //Level 2 GREEN
        this.developmentCardsDecks[1][0][0] = new DevelopmentCard("GREEN",2,
                0,0,0,4,
                0,1,0,0,
                0,0,0,0,
                2,5);
        this.developmentCardsDecks[1][0][1] = new DevelopmentCard("GREEN",2,
                0,0,2,3,
                0,0,1,1,
                0,3,0,0,
                0,6);
        this.developmentCardsDecks[1][0][2] = new DevelopmentCard("GREEN",2,
                0,0,0,5,
                2,0,0,0,
                0,2,0,0,
                2,7);
        this.developmentCardsDecks[1][0][3] = new DevelopmentCard("GREEN",2,
                3,0,0,3,
                1,0,0,0,
                0,0,0,2,
                1,8);

        //Level 3 GREEN
        this.developmentCardsDecks[0][0][0] = new DevelopmentCard("GREEN",3,
                0,0,0,6,
                2,0,0,0,
                0,3,0,0,
                2,9);
        this.developmentCardsDecks[0][0][1] = new DevelopmentCard("GREEN",3,
                0,0,2,5,
                1,0,1,0,
                0,2,0,2,
                1,10);
        this.developmentCardsDecks[0][0][2] = new DevelopmentCard("GREEN",3,
                0,0,0,7,
                0,0,1,0,
                1,0,0,0,
                3,11);
        this.developmentCardsDecks[0][0][3] = new DevelopmentCard("GREEN",3,
                4,0,0,4,
                0,1,0,0,
                3,0,0,1,
                0,12);

        //Level 1 PURPLE
        this.developmentCardsDecks[2][3][0] = new DevelopmentCard("PURPLE",1,
                0,0,2,0,
                0,1,0,0,
                0,0,0,0,
                1,1);
        this.developmentCardsDecks[2][3][1] = new DevelopmentCard("PURPLE",1,
                1,0,1,1,
                1,0,0,0,
                0,0,0,1,
                0,2);
        this.developmentCardsDecks[2][3][2] = new DevelopmentCard("PURPLE",1,
                0,0,3,0,
                2,0,0,0,
                0,1,1,1,
                0,3);
        this.developmentCardsDecks[2][3][3] = new DevelopmentCard("PURPLE",1,
                0,2,2,0,
                1,0,0,1,
                0,2,0,0,
                1,4);

        //Level 2 PURPLE
        this.developmentCardsDecks[1][3][0] = new DevelopmentCard("PURPLE",2,
                0,0,4,0,
                1,0,0,0,
                0,0,0,0,
                2,5);
        this.developmentCardsDecks[1][3][1] = new DevelopmentCard("PURPLE",2,
                2,0,3,0,
                1,0,1,0,
                0,0,0,3,
                0,6);
        this.developmentCardsDecks[1][3][2] = new DevelopmentCard("PURPLE",2,
                0,0,5,0,
                0,2,0,0,
                2,0,0,0,
                2,7);
        this.developmentCardsDecks[1][3][3] = new DevelopmentCard("PURPLE",2,
                0,0,3,3,
                0,1,0,0,
                0,0,2,0,
                1,8);

        //Level 3 PURPLE
        this.developmentCardsDecks[0][3][0] = new DevelopmentCard("PURPLE",3,
                0,0,6,0,
                0,2,0,0,
                3,0,0,0,
                2,9);
        this.developmentCardsDecks[0][3][1] = new DevelopmentCard("PURPLE",3,
                2,0,5,2,
                0,1,0,1,
                2,0,2,0,
                1,10);
        this.developmentCardsDecks[0][3][2] = new DevelopmentCard("PURPLE",3,
                0,0,7,0,
                1,0,0,0,
                0,1,0,0,
                3,11);
        this.developmentCardsDecks[0][3][3] = new DevelopmentCard("PURPLE",3,
                0,0,4,4,
                1,0,0,0,
                0,3,1,0,
                0,12);

        //Level 1 BLUE
        this.developmentCardsDecks[2][1][0] = new DevelopmentCard("BLUE",1,
                2,0,0,0,
                0,0,0,1,
                0,0,0,0,
                1,1);
        this.developmentCardsDecks[2][1][1] = new DevelopmentCard("BLUE",1,
                1,1,1,0,
                0,0,1,0,
                0,1,0,0,
                0,2);
        this.developmentCardsDecks[2][1][2] = new DevelopmentCard("BLUE",1,
                3,0,0,0,
                0,2,0,0,
                1,0,1,1,
                0,3);
        this.developmentCardsDecks[2][1][3] = new DevelopmentCard("BLUE",1,
                2,0,2,0,
                0,1,0,1,
                0,0,2,0,
                1,4);

        //Level 2 BLUE
        this.developmentCardsDecks[1][1][0] = new DevelopmentCard("BLUE",2,
                4,0,0,0,
                0,0,1,0,
                0,0,0,0,
                2,5);
        this.developmentCardsDecks[1][1][1] = new DevelopmentCard("BLUE",2,
                3,2,0,0,
                1,1,0,0,
                0,0,3,0,
                0,6);
        this.developmentCardsDecks[1][1][2] = new DevelopmentCard("BLUE",2,
                5,0,0,0,
                0,0,2,0,
                0,0,0,2,
                2,7);
        this.developmentCardsDecks[1][1][3] = new DevelopmentCard("BLUE",2,
                3,3,0,0,
                0,0,1,0,
                0,2,0,0,
                1,8);

        //Level 3 BLUE
        this.developmentCardsDecks[0][1][0] = new DevelopmentCard("BLUE",3,
                6,0,0,0,
                0,0,2,0,
                0,0,0,3,
                2,9);
        this.developmentCardsDecks[0][1][1] = new DevelopmentCard("BLUE",3,
                5,2,0,0,
                1,0,0,1,
                0,2,2,0,
                1,10);
        this.developmentCardsDecks[0][1][2] = new DevelopmentCard("BLUE",3,
                7,0,0,0,
                0,1,0,0,
                0,0,0,1,
                3,11);
        this.developmentCardsDecks[0][1][3] = new DevelopmentCard("BLUE",3,
                4,4,0,0,
                0,0,1,0,
                1,0,0,3,
                0,12);

        //Level 1 YELLOW
        this.developmentCardsDecks[2][2][0] = new DevelopmentCard("YELLOW",1,
                0,2,0,0,
                0,0,1,0,
                0,0,0,0,
                1,1);
        this.developmentCardsDecks[2][2][1] = new DevelopmentCard("YELLOW",1,
                1,1,0,1,
                0,0,0,1,
                1,0,0,0,
                0,2);
        this.developmentCardsDecks[2][2][2] = new DevelopmentCard("YELLOW",1,
                0,3,0,0,
                0,0,0,2,
                1,1,1,0,
                0,3);
        this.developmentCardsDecks[2][2][3] = new DevelopmentCard("YELLOW",1,
                0,2,0,2,
                1,0,1,0,
                0,0,0,2,
                1,4);

        //Level 2 YELLOW
        this.developmentCardsDecks[1][2][0] = new DevelopmentCard("YELLOW",2,
                0,4,0,0,
                0,0,0,1,
                0,0,0,0,
                2,5);
        this.developmentCardsDecks[1][2][1] = new DevelopmentCard("YELLOW",2,
                0,3,0,2,
                0,1,0,1,
                3,0,0,0,
                0,6);
        this.developmentCardsDecks[1][2][2] = new DevelopmentCard("YELLOW",2,
                0,5,0,0,
                0,0,0,2,
                0,0,2,0,
                2,7);
        this.developmentCardsDecks[1][2][3] = new DevelopmentCard("YELLOW",2,
                0,3,3,0,
                0,0,0,1,
                2,0,0,0,
                1,8);

        //Level 3 YELLOW
        this.developmentCardsDecks[0][2][0] = new DevelopmentCard("YELLOW",3,
                0,6,0,0,
                0,0,0,2,
                0,0,3,0,
                2,9);
        this.developmentCardsDecks[0][2][1] = new DevelopmentCard("YELLOW",3,
                0,5,2,0,
                0,1,1,0,
                2,0,0,2,
                1,10);
        this.developmentCardsDecks[0][2][2] = new DevelopmentCard("YELLOW",3,
                0,7,0,0,
                0,0,0,1,
                0,0,1,0,
                3,11);
        this.developmentCardsDecks[0][2][3] = new DevelopmentCard("YELLOW",3,
                0,4,4,0,
                0,0,0,1,
                0,1,3,0,
                0,12);

        //Shuffle each development cards deck in the grid
        for (int i=0;i<3;i++) {
            //Level is capped at 3
            //Colours can be infinite
            //Adds one since levels start from one but grid from zero so the levels saved are correct
            this.developmentCardsLevels.add(i+1);
            for (int j=0;j<4;j++) {
                List<DevelopmentCard> developmentCardsList = Arrays.asList(this.developmentCardsDecks[i][j]);
                Collections.shuffle(developmentCardsList);
                this.developmentCardsDecks[i][j] = developmentCardsList.toArray(this.developmentCardsDecks[i][j]);
                //Saves all the possible colours
                this.developmentCardsColours.put(this.developmentCardsDecks[i][j][0].getDevelopmentCardColour(), j);
            }
        }
    }

    /**
     * Returns the orderly distributed development cards
     * @return the orderly distributed development cards
     */
    public DevelopmentCard[][][] getDevelopmentCardsDecks() {
        return this.developmentCardsDecks;
    }

    /**
     * Returns the available development card colours
     * @return the available development card colours
     */
    public Map<String, Integer> getDevelopmentCardsColours() {
        return this.developmentCardsColours;
    }

    /**
     * Returns the available development card levels
     * @return the available development card levels
     */
    public Collection<Integer> getDevelopmentCardsLevels() {
        return this.developmentCardsLevels;
    }

    /**
     * Checks if there are still development cards in a grid's deck
     * @param developmentCards - development cards deck
     * @return if there are still development cards in a grid's deck
     */
    public boolean stillCardsInTheDeck(DevelopmentCard[] developmentCards) {
        //Checks if the deck is empty or not
        return developmentCards[0] != null;
    }

    /**
     * Removes 2 development card from the available ones
     * @param column - column to remove the development cards from
     */
    public void removeDevelopmentCards(int column) {
        int removedCards = 0;
        //Scrolls the column up starting from level 1 cards
        for (int i=2;i>=0;i--) {
            List<DevelopmentCard> deckToReduce = new ArrayList<>(Arrays.asList(this.developmentCardsDecks[i][column]));
            if (removedCards==2) return;
            if (deckToReduce.size()!=0) {
                deckToReduce.remove(0);
                removedCards = removedCards+1;
            }
            if (deckToReduce.size()!=0) {
                deckToReduce.remove(0);
                removedCards = removedCards+1;
            }
            this.developmentCardsDecks[i][column] = deckToReduce.toArray(this.developmentCardsDecks[i][column]);
        }
    }

    public void printGrid(PrintWriter out){

        for(int row=0; row<3; row++)
        {
            int rowNumber=3-row;
            out.println("| LEVEL "+rowNumber+" - GREEN       | LEVEL "+rowNumber+" - BLUE        | LEVEL "+rowNumber+" - YELLOW      | LEVEL "+rowNumber+" - PURPLE      |");
            for(int k=0; k<4; k++){
                if(this.developmentCardsDecks[row][k][0]!=null)
                    out.print("| Req: "+this.getDevelopmentCardsDecks()[row][k][0].printCardReq()+" ");
                else out.print("|                       ");
            }
            out.print("|");
            out.println();

            for(int k=0; k<4; k++){
                if(this.developmentCardsDecks[row][k][0]!=null)
                    out.print("| "+this.getDevelopmentCardsDecks()[row][k][0].printCardProductionPower()+" ");
                else out.print("|                       ");
            }
            out.print("|");
            out.println();

            for(int k=0; k<4; k++){
                if(this.developmentCardsDecks[row][k][0]!=null)
                {
                    out.print("| Victory Points: "+this.getDevelopmentCardsDecks()[row][k][0].getVictoryPoints()+"    ");
                    if(this.developmentCardsDecks[row][k][0].getVictoryPoints()<10)
                        out.print(" ");
                }
                else out.print("|                       ");
            }
            out.print("|");
            out.println();

            out.println();
        }

    }

}
