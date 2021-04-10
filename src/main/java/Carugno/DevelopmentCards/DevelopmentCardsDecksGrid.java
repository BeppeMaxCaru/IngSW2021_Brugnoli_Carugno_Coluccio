package Carugno.DevelopmentCards;

import Brugnoli.Playerboard;

import java.util.*;

public class DevelopmentCardsDecksGrid {

    private final DevelopmentCard[][][] developmentCardsDecksGrid;
    private Map<String, Integer> developmentCardsColours;
    private Collection<Integer> developmentCardsLevels;

    private Scanner consoleInput = new Scanner(System.in);
    //private String colour;
    //private int level;

    public DevelopmentCardsDecksGrid() {
        //costruisco tutta la griglia
        this.developmentCardsDecksGrid = new DevelopmentCard[3][4][4];

        this.developmentCardsColours = new HashMap<>();
        //Level 1 GREEN
        this.developmentCardsDecksGrid[2][0][0] = new DevelopmentCard("GREEN",1,
                0,0,0,2,
                1,0,0,0,
                0,0,0,0,
                1,1);
        this.developmentCardsDecksGrid[2][0][1] = new DevelopmentCard("GREEN",1,
                0,1,1,1,
                0,1,0,0,
                0,0,1,0,
                0,2);
        this.developmentCardsDecksGrid[2][0][2] = new DevelopmentCard("GREEN",1,
                0,0,0,3,
                0,0,2,0,
                1,1,0,1,
                0,3);
        this.developmentCardsDecksGrid[2][0][3] = new DevelopmentCard("GREEN",1,
                2,0,0,2,
                0,1,1,0,
                2,0,0,0,
                1,4);

        //Level 2 GREEN
        this.developmentCardsDecksGrid[1][0][0] = new DevelopmentCard("GREEN",2,
                0,0,0,4,
                0,1,0,0,
                0,0,0,0,
                2,5);
        this.developmentCardsDecksGrid[1][0][1] = new DevelopmentCard("GREEN",2,
                0,0,2,3,
                0,0,1,1,
                0,3,0,0,
                0,6);
        this.developmentCardsDecksGrid[1][0][2] = new DevelopmentCard("GREEN",2,
                0,0,0,5,
                2,0,0,0,
                0,2,0,0,
                2,7);
        this.developmentCardsDecksGrid[1][0][3] = new DevelopmentCard("GREEN",2,
                3,0,0,3,
                1,0,0,0,
                0,0,0,2,
                1,8);

        //Level 3 GREEN
        this.developmentCardsDecksGrid[0][0][0] = new DevelopmentCard("GREEN",3,
                0,0,0,6,
                2,0,0,0,
                0,3,0,0,
                2,9);
        this.developmentCardsDecksGrid[0][0][1] = new DevelopmentCard("GREEN",3,
                0,0,2,5,
                1,0,1,0,
                0,2,0,2,
                1,10);
        this.developmentCardsDecksGrid[0][0][2] = new DevelopmentCard("GREEN",3,
                0,0,0,7,
                0,0,1,0,
                1,0,0,0,
                3,11);
        this.developmentCardsDecksGrid[0][0][3] = new DevelopmentCard("GREEN",3,
                4,0,0,4,
                0,1,0,0,
                3,0,0,1,
                0,12);

        //Level 1 PURPLE
        this.developmentCardsDecksGrid[2][1][0] = new DevelopmentCard("PURPLE",1,
                0,0,2,0,
                0,1,0,0,
                0,0,0,0,
                1,1);
        this.developmentCardsDecksGrid[2][1][1] = new DevelopmentCard("PURPLE",1,
                1,0,1,1,
                1,0,0,0,
                0,0,0,1,
                0,2);
        this.developmentCardsDecksGrid[2][1][2] = new DevelopmentCard("PURPLE",1,
                0,0,3,0,
                2,0,0,0,
                0,1,1,1,
                0,3);
        this.developmentCardsDecksGrid[2][1][3] = new DevelopmentCard("PURPLE",1,
                0,2,2,0,
                1,0,0,1,
                0,2,0,0,
                1,4);

        //Level 2 PURPLE
        this.developmentCardsDecksGrid[1][1][0] = new DevelopmentCard("PURPLE",2,
                0,0,4,0,
                1,0,0,0,
                0,0,0,0,
                2,5);
        this.developmentCardsDecksGrid[1][1][1] = new DevelopmentCard("PURPLE",2,
                2,0,3,0,
                1,0,1,0,
                0,0,0,3,
                0,6);
        this.developmentCardsDecksGrid[1][1][2] = new DevelopmentCard("PURPLE",2,
                0,0,5,0,
                0,2,0,0,
                2,0,0,0,
                2,7);
        this.developmentCardsDecksGrid[1][1][3] = new DevelopmentCard("PURPLE",2,
                0,0,3,3,
                0,1,0,0,
                0,0,2,0,
                1,8);

        //Level 3 PURPLE
        this.developmentCardsDecksGrid[0][1][0] = new DevelopmentCard("PURPLE",3,
                0,0,6,0,
                0,2,0,0,
                3,0,0,0,
                2,9);
        this.developmentCardsDecksGrid[0][1][1] = new DevelopmentCard("PURPLE",3,
                2,0,5,2,
                0,1,0,1,
                2,0,2,0,
                1,10);
        this.developmentCardsDecksGrid[0][1][2] = new DevelopmentCard("PURPLE",3,
                0,0,7,0,
                1,0,0,0,
                0,1,0,0,
                3,11);
        this.developmentCardsDecksGrid[0][1][3] = new DevelopmentCard("PURPLE",3,
                0,0,4,4,
                1,0,0,0,
                0,3,1,0,
                0,12);

        //Level 1 BLUE
        this.developmentCardsDecksGrid[2][2][0] = new DevelopmentCard("BLUE",1,
                2,0,0,0,
                0,0,0,1,
                0,0,0,0,
                1,1);
        this.developmentCardsDecksGrid[2][2][1] = new DevelopmentCard("BLUE",1,
                1,1,1,0,
                0,0,1,0,
                0,1,0,0,
                0,2);
        this.developmentCardsDecksGrid[2][2][2] = new DevelopmentCard("BLUE",1,
                3,0,0,0,
                0,2,0,0,
                1,0,1,1,
                0,3);
        this.developmentCardsDecksGrid[2][2][3] = new DevelopmentCard("BLUE",1,
                2,0,2,0,
                0,1,0,1,
                0,0,2,0,
                1,4);

        //Level 2 BLUE
        this.developmentCardsDecksGrid[1][2][0] = new DevelopmentCard("BLUE",2,
                4,0,0,0,
                0,0,1,0,
                0,0,0,0,
                2,5);
        this.developmentCardsDecksGrid[1][2][1] = new DevelopmentCard("BLUE",2,
                3,2,0,0,
                1,1,0,0,
                0,0,3,0,
                0,6);
        this.developmentCardsDecksGrid[1][2][2] = new DevelopmentCard("BLUE",2,
                5,0,0,0,
                0,0,2,0,
                0,0,0,2,
                2,7);
        this.developmentCardsDecksGrid[1][2][3] = new DevelopmentCard("BLUE",2,
                3,3,0,0,
                0,0,1,0,
                0,2,0,0,
                1,8);

        //Level 3 BLUE
        this.developmentCardsDecksGrid[0][2][0] = new DevelopmentCard("BLUE",3,
                6,0,0,0,
                0,0,2,0,
                0,0,0,3,
                2,9);
        this.developmentCardsDecksGrid[0][2][1] = new DevelopmentCard("BLUE",3,
                5,2,0,0,
                1,0,0,1,
                0,2,2,0,
                1,10);
        this.developmentCardsDecksGrid[0][2][2] = new DevelopmentCard("BLUE",3,
                7,0,0,0,
                0,1,0,0,
                0,0,0,1,
                3,11);
        this.developmentCardsDecksGrid[0][2][3] = new DevelopmentCard("BLUE",3,
                4,4,0,0,
                0,0,1,0,
                1,0,0,3,
                0,12);

        //Level 1 YELLOW
        this.developmentCardsDecksGrid[2][3][0] = new DevelopmentCard("YELLOW",1,
                0,2,0,0,
                0,0,1,0,
                0,0,0,0,
                1,1);
        this.developmentCardsDecksGrid[2][3][1] = new DevelopmentCard("YELLOW",1,
                1,1,0,1,
                0,0,0,1,
                1,0,0,0,
                0,2);
        this.developmentCardsDecksGrid[2][3][2] = new DevelopmentCard("YELLOW",1,
                0,3,0,0,
                0,0,0,2,
                1,1,1,0,
                0,3);
        this.developmentCardsDecksGrid[2][3][3] = new DevelopmentCard("YELLOW",1,
                0,2,0,2,
                1,0,1,0,
                0,0,0,2,
                1,4);

        //Level 2 YELLOW
        this.developmentCardsDecksGrid[1][3][0] = new DevelopmentCard("YELLOW",2,
                0,4,0,0,
                0,0,0,1,
                0,0,0,0,
                2,5);
        this.developmentCardsDecksGrid[1][3][1] = new DevelopmentCard("YELLOW",2,
                0,3,0,2,
                0,1,0,1,
                3,0,0,0,
                0,6);
        this.developmentCardsDecksGrid[1][3][2] = new DevelopmentCard("YELLOW",2,
                0,5,0,0,
                0,0,0,2,
                0,0,2,0,
                2,7);
        this.developmentCardsDecksGrid[1][3][3] = new DevelopmentCard("YELLOW",2,
                0,3,3,0,
                0,0,0,1,
                2,0,0,0,
                1,8);

        //Level 3 YELLOW
        this.developmentCardsDecksGrid[0][3][0] = new DevelopmentCard("YELLOW",3,
                0,6,0,0,
                0,0,0,2,
                0,0,3,0,
                2,9);
        this.developmentCardsDecksGrid[0][3][1] = new DevelopmentCard("YELLOW",3,
                0,5,2,0,
                0,1,1,0,
                2,0,0,2,
                1,10);
        this.developmentCardsDecksGrid[0][3][2] = new DevelopmentCard("YELLOW",3,
                0,7,0,0,
                0,0,0,1,
                0,0,1,0,
                3,11);
        this.developmentCardsDecksGrid[0][3][3] = new DevelopmentCard("YELLOW",3,
                0,4,4,0,
                0,0,0,1,
                0,1,3,0,
                0,12);

        //Shuffle the grid
        for (int i=0;i<3;i++) {
            //Mi basta i+1 (siccome parto da 0) dato che il livello massimo è 3
            //mentre i colori possono essere potenzialmente infiniti
            this.developmentCardsLevels.add(i+1);
            for (int j=0;j<4;j++) {
                List<DevelopmentCard> developmentCardsList = Arrays.asList(developmentCardsDecksGrid[i][j]);
                Collections.shuffle(developmentCardsList);
                developmentCardsList.toArray(developmentCardsDecksGrid[i][j]);
                //Mette i colori nella lista di colori delle carte
                this.developmentCardsColours.put(this.developmentCardsDecksGrid[i][j][0].getColour(), j);
            }
        }
    }

    public void buyDevelopmentCard(Playerboard playerboard) {
        //Print the available cards
        for (int i=0;i<3;i++) {
            for (int j=0;j<4;j++) {
                this.developmentCardsDecksGrid[i][j][0].printDevelopmentCard();
            }
        }

        //Ask colour x
        //Ask level y
        //ciclo while che continua a chiedere se non valida
        System.out.println("Available development cards colours: " + this.developmentCardsColours);
        System.out.println("Choose development cards colour: ");
        String colour = this.consoleInput.nextLine();
        while (!this.developmentCardsColours.containsKey(colour)) {
            System.out.println("Card of this colour doesn't exist -> choose another colour: ");
            colour = this.consoleInput.nextLine();
        }
        System.out.println("Available development cards levels: " + this.developmentCardsLevels);
        System.out.println("Choose development card level: ");
        int level = this.consoleInput.nextInt();
        //check if the input card exists otherwise choose again
        while (!this.developmentCardsLevels.contains(level)) {
            System.out.println("Card of this level doesn't exist -> choose another level: ");
            level = this.consoleInput.nextInt();
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Mega ciclo while di controllo va qua dentro
        //Map<String, Integer> developmentCardCost = this.developmentCardsDecksGrid[level][this.developmentCardsColours.get(colour)][0].getDevelopmentCardCost();
        //while (this.developmentCardsDecksGrid[level][this.developmentCardsColours.get(colour)][0].checkResourcesAvailability(Playerboard playerboard)) {
            //While buy == false keep asking -> if true card has been bought
            //Make the player choose the deck where to put the card
        //}
    }

}
