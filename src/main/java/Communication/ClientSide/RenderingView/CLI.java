package Communication.ClientSide.RenderingView;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CLI implements RenderingView{

    @Override
    public void welcome(Stage stage, String nickName) {
        System.out.println("Hi " + nickName + "!");
        System.out.println("Welcome to Master of Renaissance!");
    }

    @Override
    public void error(Exception e){
        e.printStackTrace();
        System.err.println("Error");
    }

    @Override
    public void receiverError(Exception e){
        e.printStackTrace();
        System.err.println("Error in receiver");
    }

    @Override
    public void senderError(Exception e){
        e.printStackTrace();
        System.err.println("Not valid parameter");
    }

    @Override
    public void matchHasStarted(Stage stage, int playerNumber){
        System.out.println("\nMatch has started, your player number is " + playerNumber);
    }

    @Override
    public String start(Stage stage){
        String res;
        Scanner input = new Scanner(System.in);
        System.out.println("Which starting resource do you want to pick?");
        System.out.println("Write COINS, STONES, SERVANTS or SHIELDS.");
        res = input.nextLine().toUpperCase();
        while (!res.equals("COINS") && !res.equals("STONES") && !res.equals("SERVANTS") && !res.equals("SHIELDS")) {
            System.out.println("Choose a correct resource");
            System.out.println("Which starting resource do you want to pick?");
            System.out.println("Write COINS, STONES, SERVANTS or SHIELDS.");
            res = input.nextLine().toUpperCase();
        }
        return res;
    }

    @Override
    public int[] discardStartingLeaders(Stage stage, LeaderCard[] leaderCards){
        Scanner input = new Scanner(System.in);
        int[] discarded = new int[2];

        System.out.println("Which starting leader card do you want to discard?\n");
        for (int i = 0; i < leaderCards.length; i++) {
            System.out.println("Write " + i + " for this: ");
            this.printLeaderCard(leaderCards[i]);
        }
        int card;
        try {
            card = input.nextInt();
            while (card < 0 || card > 3) {
                System.out.println("Chose a correct card.");
                card = input.nextInt();
            }
            discarded[0]=card;
        } catch (Exception e) {
            this.error(e);
        }

        System.out.println("Which starting leader card do you want to discard?");
        for (int i = 0; i < leaderCards.length; i++) {
            if(i < discarded[0]) {
                System.out.println("Write " + i + " for this: ");
                this.printLeaderCard(leaderCards[i]);
            } else if (i>discarded[0]) {
                int k=i-1;
                System.out.println("Write " + k + " for this: ");
                this.printLeaderCard(leaderCards[i]);
            }
        }
        try {
            card = input.nextInt();
            while (card < 0 || card > 2) {
                System.out.println("Chose a correct card.");
                card = input.nextInt();
            }
            discarded[1]=card;
        } catch (Exception e) {
            this.error(e);
        }
        return discarded;
    }

    @Override
    public String nick(Stage stage){
        Scanner input = new Scanner(System.in);
        System.out.println("Insert your nickname");
        return input.nextLine();
    }

    @Override
    public String actionChoice(Stage stage) {
        Scanner input = new Scanner(System.in);
        String action;

        System.out.println("Which action do you want to do?");
        System.out.println("Write 'Play leader card'");
        System.out.println("Write 'Discard leader card'");
        System.out.println("Write 'pick resources from Market'");
        System.out.println("Write 'Buy development card'");
        System.out.println("Write 'Activate production power'");
        System.out.println("Write 'END TURN' at the end of your turn");
        action = input.nextLine().toUpperCase();

        while(!action.equals("PLAY LEADER CARD") && !action.equals("DISCARD LEADER CARD") &&
                !action.equals("PICK RESOURCES FROM MARKET") && !action.equals("BUY DEVELOPMENT CARD") &&
                !action.equals("ACTIVATE PRODUCTION POWER") && !action.equals("END TURN") && !action.equals("QUIT"))
        {
            System.err.println("Write a correct action.");
            System.out.println("Which action do you want to do?");
            System.out.println("Write 'Play leader card'");
            System.out.println("Write 'Discard leader card'");
            System.out.println("Write 'pick resources from Market'");
            System.out.println("Write 'Buy development card'");
            System.out.println("Write 'Activate production power'");
            System.out.println("Write 'END TURN' at the end of your turn");
            action = input.nextLine().toUpperCase();
        }
        return action;
    }

    @Override
    public int playLeader(Stage stage, LeaderCard[] cards) {
        Scanner input = new Scanner(System.in);

        System.out.println("Which card do you want to play?");
        for(int index =0; index<cards.length; index++)
        {
            if(cards[index]!=null && !cards[index].isPlayed())
            {
                System.out.println("Write "+index+" for this");
                this.printLeaderCard(cards[index]);
            }
        }
        String parameter = input.nextLine();

        while (!parameter.equals("0") && !parameter.equals("1"))
        {
            System.err.println("Choose a correct card.");
            System.out.println("Which card do you want to play?");
            for(int index =0; index<cards.length; index++)
            {
                if(cards[index]!=null && !cards[index].isPlayed())
                {
                    System.out.println("Write "+index+" for this");
                    this.printLeaderCard(cards[index]);
                }
            }
            parameter = input.nextLine();
        }
        return Integer.parseInt(parameter);
    }

    @Override
    public int discardLeader(Stage stage, LeaderCard[] cards) {
        Scanner input = new Scanner(System.in);

        System.out.println("Which card do you want to discard?");
        for(int index =0; index<cards.length; index++)
        {
            if(cards[index]!=null && !cards[index].isPlayed())
            {
                System.out.println("Write "+index+" for this");
                this.printLeaderCard(cards[index]);
            }
        }
        String parameter = input.nextLine();

        while (!parameter.equals("0") && !parameter.equals("1"))
        {
            System.err.println("Choose a correct card.");
            System.out.println("Which card do you want to discard?");
            for(int index =0; index<cards.length; index++)
            {
                if(cards[index]!=null && !cards[index].isPlayed())
                {
                    System.out.println("Write "+index+" for this");
                    this.printLeaderCard(cards[index]);
                }
            }
            parameter = input.nextLine();
        }
        return Integer.parseInt(parameter);
    }

    @Override
    public int[] marketCoordinates(Stage stage, Market market) {
        Scanner input = new Scanner(System.in);
        int[] coordinates = new int[2];

        this.printMarket(market);

        //Receives column or row
        System.out.println("Do you want to pick row resources or column resources?");
        System.out.println("Write 'ROW' or 'COLUMN'.");
        String parameter = input.nextLine().toUpperCase();
        while (!parameter.equals("ROW") && !parameter.equals("COLUMN")) {
            System.err.println("Not valid parameter");
            System.out.println("Write 'ROW' or 'COLUMN'.");
            parameter = input.nextLine().toUpperCase();
        }

        //Receives index
        if (parameter.equals("ROW")) {
            System.out.println("Which row do you want to pick?");
            System.out.println("Write a number between 0 and 2.");
        } else {
            coordinates[0]=1;
            System.out.println("Which column do you want to pick?");
            System.out.println("Write a number between 0 and 3.");
        }
        String par = input.nextLine();
        int index;
        try {
            //Checks if the leader card position exists
            index = Integer.parseInt(par);
            if (parameter.equals("ROW"))
            {
                while(index < 0 || index > 2) {
                    System.out.println("Value not valid. Write a number between 0 and 2.");
                    par = input.nextLine();
                    index = Integer.parseInt(par);
                }
            }
            if (parameter.equals("COLUMN"))
            {
                while (index < 0 || index > 3) {
                    System.out.println("Value not valid. Write a number between 0 and 3.");
                    par = input.nextLine();
                    index = Integer.parseInt(par);
                }
            }
            coordinates[1] = index;
        } catch (Exception e) {
            this.senderError(e);
        }
        return coordinates;
    }

    @Override
    public String resourcesDestination(Stage stage, LeaderCard[] cards, String parameter) {

        Scanner input = new Scanner(System.in);
        String wlChoice;

        System.out.println("YOUR ACTIVATED LEADER CARDS:");
        for(int ind =0; ind<cards.length; ind++)
            if(cards[ind]!=null && cards[ind].isPlayed())
                this.printLeaderCard(cards[ind]);
        System.out.println();

        //Receives deposit
        System.out.println("If you activated your extra warehouse space, where do you want to store your resources?");
        if (parameter.equals("ROW"))
            System.out.println("Write w for warehouse, l for leader card, for each of 4 resources you picked");
        else
            System.out.println("Write w for warehouse, l for leader card, for each of 3 resources you picked");
        wlChoice = input.nextLine().toUpperCase();
        try {
            //Checks if player has written only 'w' and 'l' chars
            if (parameter.equals("ROW"))
            {
                while (wlChoice.length() != 4 && !this.checkShelf(wlChoice)){
                    System.out.println("Write w for warehouse, l for leader card, for each of 4 resources you picked");
                    wlChoice = input.nextLine().toUpperCase();
                }
            }
            if (parameter.equals("COLUMN"))
            {
                while (wlChoice.length() != 3 && !this.checkShelf(wlChoice)){
                    System.out.println("Write w for warehouse, l for leader card, for each of 3 resources you picked");
                    wlChoice = input.nextLine().toUpperCase();
                }
            }

        } catch (Exception e) {
            this.senderError(e);
        }

        return wlChoice;
    }

    @Override
    public String whiteMarbleChoice(Stage stage) {
        Scanner input = new Scanner(System.in);
        String chosenMarble;

        //Receives position of leader cards to activate to receive a resource from a white marble
        System.out.println("If you activated both your white marble resources leader card, which one do you want to activate for each white marble you picked?");
        System.out.println("if you activated only one white marble leader card, do you want to activate it?");
        System.out.println("Write 0 for activate your fist leader card, 1 for activate your second leader card, for each white marble you picked");
        System.out.println("Write X if you don't want to activate any leader card effect");
        chosenMarble = input.nextLine().toUpperCase();
        try {
            //Checks if player has written only '0', '1' or 'x' chars
            while (!this.checkMarbleChoice(chosenMarble))
            {
                System.err.println("not valid input.");
                System.out.println("Write 0 for activate your fist leader card, 1 for activate your second leader card, for each white marble you picked");
                System.out.println("Write X if you don't want to activate any leader card effect");
                chosenMarble = input.nextLine().toUpperCase();
            }
        } catch (Exception e) {
            this.senderError(e);
        }
        return chosenMarble;
    }

    @Override
    public int[] developmentCardsGridCoordinates(Stage stage, DevelopmentCardsDecksGrid grid, Playerboard board) {
        Scanner input = new Scanner(System.in);
        int[] coordinates = new int[2];

        this.printPlayerboard(board);
        this.printDevCardGrid(grid);

        System.out.println("Which card do you want to buy?");
        System.out.println("Write the correct colour: GREEN, YELLOW, BLUE or PURPLE, if existing in the grid");
        String colour = input.nextLine().toUpperCase();
        while (!colour.equals("GREEN") && !colour.equals("YELLOW") && !colour.equals("BLUE") && !colour.equals("PURPLE")) {
            //Resets controller
            System.err.println("Not valid colour");
            System.out.println("Write the correct colour: GREEN, YELLOW, BLUE or PURPLE, if existing in the grid");
            colour = input.nextLine().toUpperCase();
        }
        int column = grid.getDevelopmentCardsColours().get(colour);
        coordinates[0] = column;

        System.out.println("Which level do you want to buy?");
        System.out.println("Write the correct number between 1 and 3, if existing in the grid");
        String lev = input.nextLine();
        int level;
        try {
            level = Integer.parseInt(lev);
            while (level < 1 || level > 3){
                System.out.println("Write the correct number between 1 and 3, if existing in the grid");
                lev = input.nextLine();
                level = Integer.parseInt(lev);
            }
            coordinates[1] = level;
        } catch (Exception e) {
            this.senderError(e);
        }

        return coordinates;
    }

    @Override
    public String[][] payDevelopmentCard(Stage stage, LeaderCard[] cards) {
        Scanner input = new Scanner(System.in);
        String[][] pickedResources = new String[2][4];
        for (int r=0; r<2; r++)
            for(int c=0; c<4; c++)
                pickedResources[r][c] = "";
        int res = 0;

        Map<String, Integer> resources = new HashMap<>();
        resources.put("COINS", 0);
        resources.put("SERVANTS", 1);
        resources.put("SHIELDS", 2);
        resources.put("STONES", 3);

        //Which resource do you want to take
        String parameter = "";
        String quantity;
        while (!parameter.equalsIgnoreCase("STOP") || res>4) {

            System.out.println("Which resource do you want to pick to pay the development card? Write STOP at the end.");
            parameter = input.nextLine().toUpperCase();
            if (parameter.equals("COINS") || parameter.equals("STONES") || parameter.equals("SERVANTS") || parameter.equals("SHIELDS") || parameter.equals("STOP")) {
                res++;

                //Receives now quantity
                System.out.println("How much " + parameter + " do you want to pick?");
                System.out.println("Write the correct value.");
                quantity = input.nextLine();
                try {
                    while (!quantity.equals("0") && !quantity.equals("1") && !quantity.equals("2") && !quantity.equals("3") &&
                            !quantity.equals("4") && !quantity.equals("5") && !quantity.equals("6") && !quantity.equals("7"))
                    {
                        System.err.println("Not valid input");
                        System.out.println("Write the correct value.");
                        quantity = input.nextLine();
                    }
                    pickedResources[0][resources.get(parameter)] = quantity;
                } catch (Exception e) {
                    this.senderError(e);
                    break;
                }

                //Player available leader cards
                System.out.println("YOUR ACTIVATED LEADER CARDS:");
                for(int ind =0; ind<cards.length; ind++)
                    if(cards[ind]!=null && cards[ind].isPlayed())
                        this.printLeaderCard(cards[ind]);
                System.out.println();
                String shelf;
                //Keeps asking a place to take from resources
                for(int s = 0; s < Integer.parseInt(quantity); s++)
                {
                    System.out.println("From which store do you want to pick this resource?");
                    System.out.println("Write WAREHOUSE, CHEST, or LEADER CARD if you activate your extra warehouse space leader card.");
                    shelf = input.nextLine().toUpperCase();
                    while(!shelf.equals("CHEST") && !shelf.equals("WAREHOUSE") && !shelf.equals("LEADER CARD")){
                        System.err.println("Not valid parameter");
                        System.out.println("Write WAREHOUSE, CHEST, or LEADER CARD if you activate your extra warehouse space leader card.");
                        shelf = input.nextLine().toUpperCase();
                    }
                    pickedResources[1][resources.get(parameter)] = pickedResources[1][resources.get(parameter)] + shelf.charAt(0);
                }
            } else {
                System.err.println("Not existing resource");
            }
        }
        return pickedResources;
    }

    @Override
    public int choosePosition(Stage stage) {
        Scanner input = new Scanner(System.in);
        String parameter;
        int position = 0;

        System.out.println("In which position of your development card grid do you want to place the bought card?");
        System.out.println("You can put a level 1 card in an empty position or a level 2/3 card on a level 1/2 card.");
        System.out.println("Write a correct position between 0 and 2.");
        parameter = input.nextLine();

        try {
            while (!parameter.equals("0") && !parameter.equals("1") && !parameter.equals("2"))
            {
                System.err.println("Not valid position");
                System.out.println("Write a correct position between 0 and 2.");
                parameter = input.nextLine();
            }
            position = Integer.parseInt(parameter);
        } catch (Exception e) {
            this.senderError(e);
        }
        return position;
    }

    public void printLeaderCard(LeaderCard card){
        card.printLeaderCard();
    }

    public void printMarket(Market market){
        market.printMarket();
    }

    public void printPlayerboard(Playerboard playerboard){
        playerboard.printAll();
    }

    public void printDevCardGrid(DevelopmentCardsDecksGrid grid){
        grid.printGrid();
    }

    public boolean checkShelf(String wlChoice){
        for (int k = 0; k < wlChoice.length(); k++){
            if (!String.valueOf(wlChoice.charAt(k)).equals("W") && !String.valueOf(wlChoice.charAt(k)).equals("L"))
                return false;
        }
        return true;
    }

    public boolean checkMarbleChoice(String chosenMarble){
        if (chosenMarble.length() != 0)
        {
            for (int k = 0; k < chosenMarble.length(); k++)
            {
                if (!String.valueOf(chosenMarble.charAt(k)).equals("0")
                        && !String.valueOf(chosenMarble.charAt(k)).equals("1")
                        && !String.valueOf(chosenMarble.charAt(k)).equals("X"))
                    return false;
            }
        }
        return true;
    }
}
