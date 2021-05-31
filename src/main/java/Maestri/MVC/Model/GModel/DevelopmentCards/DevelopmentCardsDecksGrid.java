package Maestri.MVC.Model.GModel.DevelopmentCards;

import java.io.Serializable;
import java.util.*;

/**
 * Represents all the development cards of the game
 */
public class DevelopmentCardsDecksGrid implements Serializable {
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
                1,1, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.jpg");
        this.developmentCardsDecks[2][0][1] = new DevelopmentCard("GREEN",1,
                0,1,1,1,
                0,1,0,0,
                0,0,1,0,
                0,2, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-5-1.jpg");
        this.developmentCardsDecks[2][0][2] = new DevelopmentCard("GREEN",1,
                0,0,0,3,
                0,0,2,0,
                1,1,0,1,
                0,3, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-9-1.jpg");
        this.developmentCardsDecks[2][0][3] = new DevelopmentCard("GREEN",1,
                2,0,0,2,
                0,1,1,0,
                2,0,0,0,
                1,4, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-13-1.jpg");

        //Level 2 GREEN
        this.developmentCardsDecks[1][0][0] = new DevelopmentCard("GREEN",2,
                0,0,0,4,
                0,1,0,0,
                0,0,0,0,
                2,5, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-17-1.jpg");
        this.developmentCardsDecks[1][0][1] = new DevelopmentCard("GREEN",2,
                0,0,2,3,
                0,0,1,1,
                0,3,0,0,
                0,6, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-21-1.jpg");
        this.developmentCardsDecks[1][0][2] = new DevelopmentCard("GREEN",2,
                0,0,0,5,
                2,0,0,0,
                0,2,0,0,
                2,7, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-25-1.jpg");
        this.developmentCardsDecks[1][0][3] = new DevelopmentCard("GREEN",2,
                3,0,0,3,
                1,0,0,0,
                0,0,0,2,
                1,8, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-29-1.jpg");

        //Level 3 GREEN
        this.developmentCardsDecks[0][0][0] = new DevelopmentCard("GREEN",3,
                0,0,0,6,
                2,0,0,0,
                0,3,0,0,
                2,9, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-33-1.jpg");
        this.developmentCardsDecks[0][0][1] = new DevelopmentCard("GREEN",3,
                0,0,2,5,
                1,0,1,0,
                0,2,0,2,
                1,10, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-37-1.jpg");
        this.developmentCardsDecks[0][0][2] = new DevelopmentCard("GREEN",3,
                0,0,0,7,
                0,0,1,0,
                1,0,0,0,
                3,11, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-41-1.jpg");
        this.developmentCardsDecks[0][0][3] = new DevelopmentCard("GREEN",3,
                4,0,0,4,
                0,1,0,0,
                3,0,0,1,
                0,12, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-45-1.jpg");

        //Level 1 PURPLE
        this.developmentCardsDecks[2][3][0] = new DevelopmentCard("PURPLE",1,
                0,0,2,0,
                0,1,0,0,
                0,0,0,0,
                1,1, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-2-1.jpg");
        this.developmentCardsDecks[2][3][1] = new DevelopmentCard("PURPLE",1,
                1,0,1,1,
                1,0,0,0,
                0,0,0,1,
                0,2, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-6-1.jpg");
        this.developmentCardsDecks[2][3][2] = new DevelopmentCard("PURPLE",1,
                0,0,3,0,
                2,0,0,0,
                0,1,1,1,
                0,3, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-10-1.jpg");
        this.developmentCardsDecks[2][3][3] = new DevelopmentCard("PURPLE",1,
                0,2,2,0,
                1,0,0,1,
                0,2,0,0,
                1,4, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-14-1.jpg");

        //Level 2 PURPLE
        this.developmentCardsDecks[1][3][0] = new DevelopmentCard("PURPLE",2,
                0,0,4,0,
                1,0,0,0,
                0,0,0,0,
                2,5, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-18-1.jpg");
        this.developmentCardsDecks[1][3][1] = new DevelopmentCard("PURPLE",2,
                2,0,3,0,
                1,0,1,0,
                0,0,0,3,
                0,6, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-22-1.jpg");
        this.developmentCardsDecks[1][3][2] = new DevelopmentCard("PURPLE",2,
                0,0,5,0,
                0,2,0,0,
                2,0,0,0,
                2,7, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-26-1.jpg");
        this.developmentCardsDecks[1][3][3] = new DevelopmentCard("PURPLE",2,
                0,0,3,3,
                0,1,0,0,
                0,0,2,0,
                1,8, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-30-1.jpg");

        //Level 3 PURPLE
        this.developmentCardsDecks[0][3][0] = new DevelopmentCard("PURPLE",3,
                0,0,6,0,
                0,2,0,0,
                3,0,0,0,
                2,9, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-34-1.jpg");
        this.developmentCardsDecks[0][3][1] = new DevelopmentCard("PURPLE",3,
                2,0,5,2,
                0,1,0,1,
                2,0,2,0,
                1,10, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-38-1.jpg");
        this.developmentCardsDecks[0][3][2] = new DevelopmentCard("PURPLE",3,
                0,0,7,0,
                1,0,0,0,
                0,1,0,0,
                3,11, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-42-1.jpg");
        this.developmentCardsDecks[0][3][3] = new DevelopmentCard("PURPLE",3,
                0,0,4,4,
                1,0,0,0,
                0,3,1,0,
                0,12, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-46-1.jpg");

        //Level 1 BLUE
        this.developmentCardsDecks[2][1][0] = new DevelopmentCard("BLUE",1,
                2,0,0,0,
                0,0,0,1,
                0,0,0,0,
                1,1, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-3-1.jpg");
        this.developmentCardsDecks[2][1][1] = new DevelopmentCard("BLUE",1,
                1,1,1,0,
                0,0,1,0,
                0,1,0,0,
                0,2, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-7-1.jpg");
        this.developmentCardsDecks[2][1][2] = new DevelopmentCard("BLUE",1,
                3,0,0,0,
                0,2,0,0,
                1,0,1,1,
                0,3, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-11-1.jpg");
        this.developmentCardsDecks[2][1][3] = new DevelopmentCard("BLUE",1,
                2,0,2,0,
                0,1,0,1,
                0,0,2,0,
                1,4, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-15-1.jpg");

        //Level 2 BLUE
        this.developmentCardsDecks[1][1][0] = new DevelopmentCard("BLUE",2,
                4,0,0,0,
                0,0,1,0,
                0,0,0,0,
                2,5, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-19-1.jpg");
        this.developmentCardsDecks[1][1][1] = new DevelopmentCard("BLUE",2,
                3,2,0,0,
                1,1,0,0,
                0,0,3,0,
                0,6, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-23-1.jpg");
        this.developmentCardsDecks[1][1][2] = new DevelopmentCard("BLUE",2,
                5,0,0,0,
                0,0,2,0,
                0,0,0,2,
                2,7, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-27-1.jpg");
        this.developmentCardsDecks[1][1][3] = new DevelopmentCard("BLUE",2,
                3,3,0,0,
                0,0,1,0,
                0,2,0,0,
                1,8, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-31-1.jpg");

        //Level 3 BLUE
        this.developmentCardsDecks[0][1][0] = new DevelopmentCard("BLUE",3,
                6,0,0,0,
                0,0,2,0,
                0,0,0,3,
                2,9, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-35-1.jpg");
        this.developmentCardsDecks[0][1][1] = new DevelopmentCard("BLUE",3,
                5,2,0,0,
                1,0,0,1,
                0,2,2,0,
                1,10, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-39-1.jpg");
        this.developmentCardsDecks[0][1][2] = new DevelopmentCard("BLUE",3,
                7,0,0,0,
                0,1,0,0,
                0,0,0,1,
                3,11, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-43-1.jpg");
        this.developmentCardsDecks[0][1][3] = new DevelopmentCard("BLUE",3,
                4,4,0,0,
                0,0,1,0,
                1,0,0,3,
                0,12, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-47-1.jpg");

        //Level 1 YELLOW
        this.developmentCardsDecks[2][2][0] = new DevelopmentCard("YELLOW",1,
                0,2,0,0,
                0,0,1,0,
                0,0,0,0,
                1,1, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-4-1.jpg");
        this.developmentCardsDecks[2][2][1] = new DevelopmentCard("YELLOW",1,
                1,1,0,1,
                0,0,0,1,
                1,0,0,0,
                0,2, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-8-1.jpg");
        this.developmentCardsDecks[2][2][2] = new DevelopmentCard("YELLOW",1,
                0,3,0,0,
                0,0,0,2,
                1,1,1,0,
                0,3, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-12-1.jpg");
        this.developmentCardsDecks[2][2][3] = new DevelopmentCard("YELLOW",1,
                0,2,0,2,
                1,0,1,0,
                0,0,0,2,
                1,4, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-16-1.jpg");

        //Level 2 YELLOW
        this.developmentCardsDecks[1][2][0] = new DevelopmentCard("YELLOW",2,
                0,4,0,0,
                0,0,0,1,
                0,0,0,0,
                2,5, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-20-1.jpg");
        this.developmentCardsDecks[1][2][1] = new DevelopmentCard("YELLOW",2,
                0,3,0,2,
                0,1,0,1,
                3,0,0,0,
                0,6, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-24-1.jpg");
        this.developmentCardsDecks[1][2][2] = new DevelopmentCard("YELLOW",2,
                0,5,0,0,
                0,0,0,2,
                0,0,2,0,
                2,7, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-28-1.jpg");
        this.developmentCardsDecks[1][2][3] = new DevelopmentCard("YELLOW",2,
                0,3,3,0,
                0,0,0,1,
                2,0,0,0,
                1,8, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-32-1.jpg");

        //Level 3 YELLOW
        this.developmentCardsDecks[0][2][0] = new DevelopmentCard("YELLOW",3,
                0,6,0,0,
                0,0,0,2,
                0,0,3,0,
                2,9, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-36-1.jpg");
        this.developmentCardsDecks[0][2][1] = new DevelopmentCard("YELLOW",3,
                0,5,2,0,
                0,1,1,0,
                2,0,0,2,
                1,10, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-40-1.jpg");
        this.developmentCardsDecks[0][2][2] = new DevelopmentCard("YELLOW",3,
                0,7,0,0,
                0,0,0,1,
                0,0,1,0,
                3,11, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-44-1.jpg");
        this.developmentCardsDecks[0][2][3] = new DevelopmentCard("YELLOW",3,
                0,4,4,0,
                0,0,0,1,
                0,1,3,0,
                0,12, "Masters of Renaissance_Cards_FRONT_3mmBleed_1-48-1.jpg");

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
            if (this.developmentCardsDecks[i][column][0]!=null) {
                deckToReduce.remove(0);
                removedCards = removedCards+1;
            }
            this.developmentCardsDecks[i][column] = deckToReduce.toArray(this.developmentCardsDecks[i][column]);
            deckToReduce = new ArrayList<>(Arrays.asList(this.developmentCardsDecks[i][column]));

            if (this.developmentCardsDecks[i][column][0]!=null) {
                deckToReduce.remove(0);
                removedCards = removedCards+1;
            }
            this.developmentCardsDecks[i][column] = deckToReduce.toArray(this.developmentCardsDecks[i][column]);
        }
    }

    public void printGrid(){

        for(int row=0; row<3; row++)
        {
            int rowNumber=3-row;
            System.out.println("| LEVEL "+rowNumber+" - GREEN       | LEVEL "+rowNumber+" - BLUE        | LEVEL "+rowNumber+" - YELLOW      | LEVEL "+rowNumber+" - PURPLE      |");
            for(int k=0; k<4; k++){
                if(this.developmentCardsDecks[row][k][0]!=null)
                    System.out.print("| Req: "+this.getDevelopmentCardsDecks()[row][k][0].printCardReq()+" ");
                else System.out.print("|                       ");
            }
            System.out.print("|");
            System.out.println();

            for(int k=0; k<4; k++){
                if(this.developmentCardsDecks[row][k][0]!=null)
                    System.out.print("| "+this.getDevelopmentCardsDecks()[row][k][0].printCardProductionPower()+" ");
                else System.out.print("|                       ");
            }
            System.out.print("|");
            System.out.println();

            for(int k=0; k<4; k++){
                if(this.developmentCardsDecks[row][k][0]!=null)
                {
                    System.out.print("| Victory Points: "+this.getDevelopmentCardsDecks()[row][k][0].getVictoryPoints()+"    ");
                    if(this.developmentCardsDecks[row][k][0].getVictoryPoints()<10)
                        System.out.print(" ");
                }
                else System.out.print("|                       ");
            }
            System.out.print("|");
            System.out.println();

            System.out.println();
        }

    }

}
