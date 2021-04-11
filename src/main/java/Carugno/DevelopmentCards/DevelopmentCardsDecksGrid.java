package Carugno.DevelopmentCards;

import Brugnoli.Playerboard;
import com.sun.tools.javac.util.ArrayUtils;

import java.util.*;

public class DevelopmentCardsDecksGrid {

    private final DevelopmentCard[][][] developmentCardsDecks;
    private Map<String, Integer> developmentCardsColours;
    private Collection<Integer> developmentCardsLevels;

    private Scanner consoleInput = new Scanner(System.in);
    //private String colour;
    //private int level;

    public DevelopmentCardsDecksGrid() {
        //costruisco tutta la griglia
        this.developmentCardsDecks = new DevelopmentCard[3][4][4];

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
        this.developmentCardsDecks[2][1][0] = new DevelopmentCard("PURPLE",1,
                0,0,2,0,
                0,1,0,0,
                0,0,0,0,
                1,1);
        this.developmentCardsDecks[2][1][1] = new DevelopmentCard("PURPLE",1,
                1,0,1,1,
                1,0,0,0,
                0,0,0,1,
                0,2);
        this.developmentCardsDecks[2][1][2] = new DevelopmentCard("PURPLE",1,
                0,0,3,0,
                2,0,0,0,
                0,1,1,1,
                0,3);
        this.developmentCardsDecks[2][1][3] = new DevelopmentCard("PURPLE",1,
                0,2,2,0,
                1,0,0,1,
                0,2,0,0,
                1,4);

        //Level 2 PURPLE
        this.developmentCardsDecks[1][1][0] = new DevelopmentCard("PURPLE",2,
                0,0,4,0,
                1,0,0,0,
                0,0,0,0,
                2,5);
        this.developmentCardsDecks[1][1][1] = new DevelopmentCard("PURPLE",2,
                2,0,3,0,
                1,0,1,0,
                0,0,0,3,
                0,6);
        this.developmentCardsDecks[1][1][2] = new DevelopmentCard("PURPLE",2,
                0,0,5,0,
                0,2,0,0,
                2,0,0,0,
                2,7);
        this.developmentCardsDecks[1][1][3] = new DevelopmentCard("PURPLE",2,
                0,0,3,3,
                0,1,0,0,
                0,0,2,0,
                1,8);

        //Level 3 PURPLE
        this.developmentCardsDecks[0][1][0] = new DevelopmentCard("PURPLE",3,
                0,0,6,0,
                0,2,0,0,
                3,0,0,0,
                2,9);
        this.developmentCardsDecks[0][1][1] = new DevelopmentCard("PURPLE",3,
                2,0,5,2,
                0,1,0,1,
                2,0,2,0,
                1,10);
        this.developmentCardsDecks[0][1][2] = new DevelopmentCard("PURPLE",3,
                0,0,7,0,
                1,0,0,0,
                0,1,0,0,
                3,11);
        this.developmentCardsDecks[0][1][3] = new DevelopmentCard("PURPLE",3,
                0,0,4,4,
                1,0,0,0,
                0,3,1,0,
                0,12);

        //Level 1 BLUE
        this.developmentCardsDecks[2][2][0] = new DevelopmentCard("BLUE",1,
                2,0,0,0,
                0,0,0,1,
                0,0,0,0,
                1,1);
        this.developmentCardsDecks[2][2][1] = new DevelopmentCard("BLUE",1,
                1,1,1,0,
                0,0,1,0,
                0,1,0,0,
                0,2);
        this.developmentCardsDecks[2][2][2] = new DevelopmentCard("BLUE",1,
                3,0,0,0,
                0,2,0,0,
                1,0,1,1,
                0,3);
        this.developmentCardsDecks[2][2][3] = new DevelopmentCard("BLUE",1,
                2,0,2,0,
                0,1,0,1,
                0,0,2,0,
                1,4);

        //Level 2 BLUE
        this.developmentCardsDecks[1][2][0] = new DevelopmentCard("BLUE",2,
                4,0,0,0,
                0,0,1,0,
                0,0,0,0,
                2,5);
        this.developmentCardsDecks[1][2][1] = new DevelopmentCard("BLUE",2,
                3,2,0,0,
                1,1,0,0,
                0,0,3,0,
                0,6);
        this.developmentCardsDecks[1][2][2] = new DevelopmentCard("BLUE",2,
                5,0,0,0,
                0,0,2,0,
                0,0,0,2,
                2,7);
        this.developmentCardsDecks[1][2][3] = new DevelopmentCard("BLUE",2,
                3,3,0,0,
                0,0,1,0,
                0,2,0,0,
                1,8);

        //Level 3 BLUE
        this.developmentCardsDecks[0][2][0] = new DevelopmentCard("BLUE",3,
                6,0,0,0,
                0,0,2,0,
                0,0,0,3,
                2,9);
        this.developmentCardsDecks[0][2][1] = new DevelopmentCard("BLUE",3,
                5,2,0,0,
                1,0,0,1,
                0,2,2,0,
                1,10);
        this.developmentCardsDecks[0][2][2] = new DevelopmentCard("BLUE",3,
                7,0,0,0,
                0,1,0,0,
                0,0,0,1,
                3,11);
        this.developmentCardsDecks[0][2][3] = new DevelopmentCard("BLUE",3,
                4,4,0,0,
                0,0,1,0,
                1,0,0,3,
                0,12);

        //Level 1 YELLOW
        this.developmentCardsDecks[2][3][0] = new DevelopmentCard("YELLOW",1,
                0,2,0,0,
                0,0,1,0,
                0,0,0,0,
                1,1);
        this.developmentCardsDecks[2][3][1] = new DevelopmentCard("YELLOW",1,
                1,1,0,1,
                0,0,0,1,
                1,0,0,0,
                0,2);
        this.developmentCardsDecks[2][3][2] = new DevelopmentCard("YELLOW",1,
                0,3,0,0,
                0,0,0,2,
                1,1,1,0,
                0,3);
        this.developmentCardsDecks[2][3][3] = new DevelopmentCard("YELLOW",1,
                0,2,0,2,
                1,0,1,0,
                0,0,0,2,
                1,4);

        //Level 2 YELLOW
        this.developmentCardsDecks[1][3][0] = new DevelopmentCard("YELLOW",2,
                0,4,0,0,
                0,0,0,1,
                0,0,0,0,
                2,5);
        this.developmentCardsDecks[1][3][1] = new DevelopmentCard("YELLOW",2,
                0,3,0,2,
                0,1,0,1,
                3,0,0,0,
                0,6);
        this.developmentCardsDecks[1][3][2] = new DevelopmentCard("YELLOW",2,
                0,5,0,0,
                0,0,0,2,
                0,0,2,0,
                2,7);
        this.developmentCardsDecks[1][3][3] = new DevelopmentCard("YELLOW",2,
                0,3,3,0,
                0,0,0,1,
                2,0,0,0,
                1,8);

        //Level 3 YELLOW
        this.developmentCardsDecks[0][3][0] = new DevelopmentCard("YELLOW",3,
                0,6,0,0,
                0,0,0,2,
                0,0,3,0,
                2,9);
        this.developmentCardsDecks[0][3][1] = new DevelopmentCard("YELLOW",3,
                0,5,2,0,
                0,1,1,0,
                2,0,0,2,
                1,10);
        this.developmentCardsDecks[0][3][2] = new DevelopmentCard("YELLOW",3,
                0,7,0,0,
                0,0,0,1,
                0,0,1,0,
                3,11);
        this.developmentCardsDecks[0][3][3] = new DevelopmentCard("YELLOW",3,
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
                List<DevelopmentCard> developmentCardsList = Arrays.asList(this.developmentCardsDecks[i][j]);
                Collections.shuffle(developmentCardsList);
                this.developmentCardsDecks[i][j] = developmentCardsList.toArray(this.developmentCardsDecks[i][j]);
                //Mette i colori nella lista di colori delle carte
                this.developmentCardsColours.put(this.developmentCardsDecks[i][j][0].getDevelopmentCardColour(), j);
            }
        }
    }

    public DevelopmentCard[][][] getDevelopmentCardsDecks() {
        return this.developmentCardsDecks;
    }

    public Map<String, Integer> getDevelopmentCardsColours() {
        return this.developmentCardsColours;
    }

    public void printDevelopmentCardsDecks() {
        for (int i=0;i<3;i++) {
            for (int j=0;j<4;j++) {
                this.developmentCardsDecks[i][j][0].printDevelopmentCard();
            }
        }
    }

    public boolean stillCardsInTheDeck(DevelopmentCard[] developmentCards) {
        if (developmentCards.length==0) {
            return false;
        } else if (developmentCards.length!=0) {
            return true;
        }
        return false;
    }

    //Helpful to simplify
    //public void chooseValidCardToBuy(int x, int y) {
        //Ask colour x
        //Ask level y
        //ciclo while che continua a chiedere se non valida
    //}

    //rendo tipo di ritorno boolean che mi dice se acquisto è stato fatto o no
    //e continuo a chiamare metodo buy in un while loop del player finchè un acquisto non viene effettuato
    public boolean buyDevelopmentCard(Playerboard playerboard) {
        //Print the available cards
        //Si può togliere e fare come metodo a parte
        for (int i=0;i<3;i++) {
            for (int j=0;j<4;j++) {
                this.developmentCardsDecks[i][j][0].printDevelopmentCard();
            }
        }

        //Ask colour x
        //Ask level y
        //ciclo while che continua a chiedere se non valida
        System.out.println("Available development cards colours: " + this.developmentCardsColours);
        System.out.println("Choose development card colour: ");
        String colour = this.consoleInput.nextLine();
        System.out.println("");
        while (!this.developmentCardsColours.containsKey(colour)) {
            System.out.println("Card of this colour doesn't exist!");
            System.out.println("Choose a valid development card colour: ");
            colour = this.consoleInput.nextLine();
            System.out.println("");
        }
        System.out.println("Available development cards levels: " + this.developmentCardsLevels);
        System.out.println("Choose development card level: ");
        int level = this.consoleInput.nextInt();
        System.out.println("");
        //check if the input card exists otherwise choose again
        while (!this.developmentCardsLevels.contains(level)) {
            System.out.println("Card of this level doesn't exist!");
            System.out.println("Choose a valid development card level: ");
            level = this.consoleInput.nextInt();
            System.out.println("");
        }
        int column = this.developmentCardsColours.get(colour);
        //Check if selected pile is empty
        while (this.developmentCardsDecks[level][column].length==0) {
            System.out.println("Empty development cards pile!");
            System.out.println("Choose new pile");
            System.out.println("");
            System.out.println("Available development cards colours: " + this.developmentCardsColours);
            System.out.println("Choose development card colour: ");
            colour = this.consoleInput.nextLine();
            System.out.println("");
            while (!this.developmentCardsColours.containsKey(colour)) {
                System.out.println("Card of this colour doesn't exist!");
                System.out.println("Choose a valid development card colour: ");
                colour = this.consoleInput.nextLine();
                System.out.println("");
            }
            System.out.println("Available development cards levels: " + this.developmentCardsLevels);
            System.out.println("Choose development card level: ");
            level = this.consoleInput.nextInt();
            System.out.println("");
            //check if the input card exists otherwise choose again
            while (!this.developmentCardsLevels.contains(level)) {
                System.out.println("Card of this level doesn't exist!");
                System.out.println("Choose a valid development card level: ");
                level = this.consoleInput.nextInt();
                System.out.println("");
            }
            column = this.developmentCardsColours.get(colour);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Mega ciclo while di controllo va qua dentro
        Map<String, Integer> developmentCardCost = this.developmentCardsDecks[level][column][0].getDevelopmentCardCost();

        if (this.developmentCardsDecks[level][column][0].checkResourcesAvailability(playerboard, developmentCardCost)) {
            if (this.developmentCardsDecks[level][column][0].checkPlayerboardDevelopmentCardsCompatibility(playerboard)) {
                playerboard.placeNewDevelopmentCard(this.developmentCardsDecks[level][column][0]);
                //aggiungere remove per togliere la carta
                List<DevelopmentCard> reducedDeck = Arrays.asList(this.developmentCardsDecks[level][column]);
                reducedDeck.remove(0);
                this.developmentCardsDecks[level][column] = reducedDeck.toArray(this.developmentCardsDecks[level][column]);
                //Bisogna togliere qui le risorse al player
                //!!!!!!!!!!!!!!!!!!!!!!!!
                //payDevelopmentCard
                //!!!!!!!!!!!!!!!!!!!!!!
                return true;
            } else if (!this.developmentCardsDecks[level][column][0].checkPlayerboardDevelopmentCardsCompatibility(playerboard)) {
                return false;
            }
        } else if (!this.developmentCardsDecks[level][column][0].checkResourcesAvailability(playerboard, developmentCardCost)) {
            return false;
        }
        return false;
        //If buy isn't possible action is denied and player has to choose new action and start all over
    }

}
