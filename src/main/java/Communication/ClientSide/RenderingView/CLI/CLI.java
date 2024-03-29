package Communication.ClientSide.RenderingView.CLI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.RenderingView;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.GameOverMessage;

import java.io.ObjectInputStream;
import java.util.*;

/**
 * Contains all the CLI messages and receives keyboard inputs from the user
 */
public class CLI implements RenderingView {

    /**
     * Contains the client-side game elements
     */
    private final ClientMain main;

    /**
     * Source of user inputs
     */
    private final Scanner input;

    /**
     * Initialized the CLI class
     * @param main contains the client-side game elements
     */
    public CLI(ClientMain main){
        this.main = main;
        this.input = new Scanner(System.in);
    }

    /**
     * Returns the nickname inserted by the player
     * @return the nickname inserted by the player
     */
    public String getNickName(){
        System.out.println("Insert your nickname");
        return this.input.nextLine();
    }

    /**
     * Prints the first welcome message
     */
    public void setClientStarted(){
        System.out.println("Hi " + this.main.getNickname() + "!");
        System.out.println("Welcome to Master of Renaissance!");
    }

    /**
     * Returns the game mode
     * @return the game mode
     */
    public int getGameMode() {
        String gameMode;

        System.out.println("Write 0 for single-player or 1 for multiplayer: ");
        gameMode = this.input.nextLine();
        while (!gameMode.equals("0") && !gameMode.equals("1")) {
            System.out.println("Number not valid!");
            System.out.println("Write 0 for single-player or 1 for multiplayer: ");
            gameMode = this.input.nextLine();
        }
        return Integer.parseInt(gameMode);
    }

    /**
     * Prints this message when the game started
     */
    public void setGameStarted() {
        System.out.println("\nMatch has started, your player number is " + this.main.getPlayerNumber());
    }

    /**
     * Returns chosen starting resources
     * @return chosen starting resources
     */
    public ArrayList<String> getStartingResource(){

        ArrayList<String> playerStartingResources = new ArrayList<>();

        //Asks starting resources
        Map<Integer, Integer> startingResources = new HashMap<>();
        startingResources.put(0, 0);
        startingResources.put(1, 1);
        startingResources.put(2, 1);
        startingResources.put(3, 2);
        String res;

        for (int resources = 0; resources < startingResources.get(this.main.getPlayerNumber()); resources++) {

            System.out.println("Which starting resource do you want to pick?");
            System.out.println("Write COINS, STONES, SERVANTS or SHIELDS.");
            res = this.input.nextLine().toUpperCase();
            while (!res.equals("COINS") && !res.equals("STONES") && !res.equals("SERVANTS") && !res.equals("SHIELDS")) {
                System.out.println("Choose a correct resource");
                System.out.println("Which starting resource do you want to pick?");
                System.out.println("Write COINS, STONES, SERVANTS or SHIELDS.");
                res = this.input.nextLine().toUpperCase();
            }
            playerStartingResources.add(res);
        }
        return playerStartingResources;
    }

    /**
     * Returns the discarded leader card
     * @return the discarded leader card
     */
    public int discarder() {

        int discardedCards = 4;

        for (int i = 0; i < this.main.getLeaderCards().length; i++) {
            if (this.main.getLeaderCards()[i] == null) {
                discardedCards = discardedCards - 1;
            }
        }

        if (discardedCards == 0) {
            System.out.println("You discarded all your leader cards");
            return 0;
        }
        System.out.println("Which starting leader card do you want to discard?\n");
        for (int i = 0; i < this.main.getLeaderCards().length; i++) {
            if (this.main.getLeaderCards()[i] != null && !this.main.getLeaderCards()[i].isPlayed()) {
                System.out.println("Write " + i + " for this: ");
                this.printLeaderCard(this.main.getLeaderCards()[i]);
            }
        }

        String card;
        int index;

        while (true) {
            card = this.input.nextLine();
            try {
                index = Integer.parseInt(card);
                if (this.main.getLeaderCards()[index] != null && !this.main.getLeaderCards()[index].isPlayed()) {
                    //System.out.println("Valid input");
                    return index;
                }
                else throw new Exception();
            } catch (Exception e) {
                System.err.println("Choose a card!");
            }
        }
    }

    /**
     * Returns the played leader card
     * @return the played leader card
     */
    public int cardPlayer() {

        int discardedCards = 4;

        for (int i = 0; i < this.main.getLeaderCards().length; i++) {
            if (this.main.getLeaderCards()[i] == null) {
                discardedCards = discardedCards - 1;
            }
        }

        if (discardedCards == 0) {
            System.out.println("You discarded all your leader cards");
            return 0;
        }

        System.out.println("Which starting leader card do you want to play?\n");
        for (int i = 0; i < this.main.getLeaderCards().length; i++) {
            if (this.main.getLeaderCards()[i] != null && !this.main.getLeaderCards()[i].isPlayed()) {
                System.out.println("Write " + i + " for this: ");
                this.printLeaderCard(this.main.getLeaderCards()[i]);
            }
        }

        String card;
        int index;

        while (true) {
            card = this.input.nextLine();
            try {
                index = Integer.parseInt(card);
                if (this.main.getLeaderCards()[index] != null && !this.main.getLeaderCards()[index].isPlayed()) {
                    //System.out.println("Valid input");
                    return index;
                }
                else throw new Exception();
            } catch (Exception e) {
                System.err.println("Choose a card!");
            }
        }
    }

    /**
     * Returns the action that the player wants to do
     * @return the action that the player wants to do
     */
    public String getActionChoice() {
        this.printActions();
        String action;

        action = this.input.nextLine().toUpperCase();
        while (!action.equals("PLAY LEADER CARD") && !action.equals("DISCARD LEADER CARD") &&
                !action.equals("PICK RESOURCES FROM MARKET") && !action.equals("BUY DEVELOPMENT CARD") &&
                !action.equals("ACTIVATE PRODUCTION POWER") && !action.equals("END TURN") && !action.equals("QUIT") &&
                !action.equals("P") && !action.equals("D") && !action.equals("M") && !action.equals("B") && !action.equals("A")) {
            System.err.println("Write a correct action.");
            this.printActions();
            action = this.input.nextLine().toUpperCase();
        }
        return action;
    }

    /**
     * Prints the action that the player can do
     */
    public void printActions(){
        System.out.println("Which action do you want to do?");
        System.out.println("Write 'Play leader card'");
        System.out.println("Write 'Discard leader card'");
        System.out.println("Write 'pick resources from Market'");
        System.out.println("Write 'Buy development card'");
        System.out.println("Write 'Activate production power'");
        System.out.println("Write 'END TURN' at the end of your turn");
    }

    /**
     * Returns the coordinates of the picked resources
     * @return the coordinates of the picked resources
     */
    public int[] getMarketCoordinates() {
        int[] coordinates = new int[2];

        this.printMarket(this.main.getMarket());

        //Receives column or row
        System.out.println("Do you want to pick row resources or column resources?");
        System.out.println("Write 'ROW' or 'COLUMN'.");
        System.out.println("Write BACK if you want to chose another action");
        String parameter = this.input.nextLine().toUpperCase();
        while (!parameter.equals("ROW") && !parameter.equals("COLUMN") && !parameter.equals("BACK")) {
            System.err.println("Not valid parameter");
            System.out.println("Write 'ROW' or 'COLUMN'.");
            System.out.println("Write BACK if you want to chose another action");
            parameter = this.input.nextLine().toUpperCase();
        }

        if(parameter.equals("BACK"))
                return new int[]{-1, 0};

        //Receives index
        if (parameter.equals("ROW")) {
            System.out.println("Which row do you want to pick?");
            System.out.println("Write a number between 0 and 2.");
        } else {
            coordinates[0]=1;
            System.out.println("Which column do you want to pick?");
            System.out.println("Write a number between 0 and 3.");
        }
        String par = this.input.nextLine();
        int index;
        try {
            //Checks if the leader card position exists
            index = Integer.parseInt(par);
            if (parameter.equals("ROW"))
            {
                while(index < 0 || index > 2) {
                    System.out.println("Value not valid. Write a number between 0 and 2.");
                    par = this.input.nextLine();
                    index = Integer.parseInt(par);
                }
            }
            if (parameter.equals("COLUMN"))
            {
                while (index < 0 || index > 3) {
                    System.out.println("Value not valid. Write a number between 0 and 3.");
                    par = this.input.nextLine();
                    index = Integer.parseInt(par);
                }
            }
            coordinates[1] = index;
        } catch (Exception e) {
            this.invalidInputError(e);
        }
        return coordinates;
    }

    /**
     * Returns the destination of the picked resources
     * @param parameter indicates if the player picked a row or a column
     * @return the destination of the picked resources
     */
    public String getResourcesDestination(String parameter) {

        String wlChoice;

        int extraSpace=0;
        for(String res : this.main.getPlayerboard().getWareHouse().getWarehouseResources().keySet())
        {
            if (res.contains("extra")) {
                extraSpace ++;
            }
        }
        if(extraSpace == 0){
            //System.out.println("No extra spaces");
            if(parameter.equals("ROW"))
                return "WWWW";
            else return "WWW";
        }

        this.printActivatedLeaderCard(this.main.getLeaderCards());

        //Receives deposit
        System.out.println("If you activated your extra warehouse space, where do you want to store your resources?");
        if (parameter.equals("ROW"))
            System.out.println("Write w for warehouse, l for leader card, for each of 4 resources you picked");
        else
            System.out.println("Write w for warehouse, l for leader card, for each of 3 resources you picked");
        wlChoice = this.input.nextLine().toUpperCase();
        try {
            //Checks if player has written only 'w' and 'l' chars
            if (parameter.equals("ROW"))
            {
                while (wlChoice.length() != 4 && this.checkShelf(wlChoice)){
                    System.out.println("Write w for warehouse, l for leader card, for each of 4 resources you picked");
                    wlChoice = this.input.nextLine().toUpperCase();
                }
            }
            if (parameter.equals("COLUMN"))
            {
                while (wlChoice.length() != 3 && this.checkShelf(wlChoice)){
                    System.out.println("Write w for warehouse, l for leader card, for each of 3 resources you picked");
                    wlChoice = this.input.nextLine().toUpperCase();
                }
            }

        } catch (Exception e) {
            this.senderError(e);
        }

        return wlChoice;
    }

    /**
     * Returns if the player wants to activate white marble leader effect
     * @return if the player wants to activate white marble leader effect
     */
    public String getWhiteMarbleChoice() {
        String chosenMarble;

        int marbles=-1;
        if(this.main.getPlayerboard().getResourceMarbles()[0]==null && this.main.getPlayerboard().getResourceMarbles()[1]==null)
            return "";
        if(this.main.getPlayerboard().getResourceMarbles()[0]!=null && this.main.getPlayerboard().getResourceMarbles()[1]==null)
            marbles=0;
        if(this.main.getPlayerboard().getResourceMarbles()[0]!=null && this.main.getPlayerboard().getResourceMarbles()[1]!=null)
            marbles=1;

        //Receives position of leader cards to activate to receive a resource from a white marble
        if(marbles==0)
        {
            System.out.println("If you activated only one white marble leader card, do you want to activate it?");
            System.out.println("Write 0 for activate your fist leader card.");
        } else if (marbles==1) {
            System.out.println("If you activated both your white marble resources leader card, which one do you want to activate for each white marble you picked?");
            System.out.println("Write 0 for activate your fist leader card, 1 for activate your second leader card, for each white marble you picked.");
        }
        System.out.println("Write X if you don't want to activate any leader card effect");

        chosenMarble = this.input.nextLine().toUpperCase();
        try {
            //Checks if player has written only '0', '1' or 'x' chars
            while (!this.checkMarbleChoice(chosenMarble))
            {
                System.err.println("Not valid input.");
                if(marbles==0)
                    System.out.println("Write 0 for activate your fist leader card.");
                else if (marbles==1)
                    System.out.println("Write 0 for activate your fist leader card, 1 for activate your second leader card, for each white marble you picked.");
                System.out.println("Write X if you don't want to activate any leader card effect");
                chosenMarble = this.input.nextLine().toUpperCase();
            }
        } catch (Exception e) {
            this.senderError(e);
        }
        return chosenMarble;
    }

    /**
     * Returns the coordinates of the card that the player wants to buy
     * @return the coordinates of the card that the player wants to buy
     */
    public int[] getDevelopmentCardsGridCoordinates() {
        int[] coordinates = new int[2];

        this.printPlayerboard(this.main.getPlayerboard());
        this.printDevCardGrid(this.main.getDevelopmentCardsDecksGrid());
        this.printActivatedLeaderCard(this.main.getLeaderCards());

        System.out.println("Which card do you want to buy?");
        System.out.println("Write the correct colour: GREEN, YELLOW, BLUE or PURPLE, if existing in the grid");
        System.out.println("Write BACK if you want to chose another action");
        String colour = input.nextLine().toUpperCase();
        while (!colour.equals("GREEN") && !colour.equals("YELLOW") && !colour.equals("BLUE") && !colour.equals("PURPLE") && !colour.equals("BACK")) {
            //Resets controller
            System.err.println("Not valid colour");
            System.out.println("Write the correct colour: GREEN, YELLOW, BLUE or PURPLE, if existing in the grid");
            System.out.println("Write BACK if you want to chose another action");
            colour = input.nextLine().toUpperCase();
        }
        if(colour.equals("BACK"))
            return new int[]{-1, 0};

        int column = this.main.getDevelopmentCardsDecksGrid().getDevelopmentCardsColours().get(colour);
        coordinates[0] = column;

        System.out.println("Which level do you want to buy?");
        System.out.println("Write the correct number between 1 and 3, if existing in the grid");
        String lev = this.input.nextLine();
        int level;
        try {
            level = Integer.parseInt(lev);
            while (level < 1 || level > 3){
                System.out.println("Write the correct number between 1 and 3, if existing in the grid");
                lev = this.input.nextLine();
                level = Integer.parseInt(lev);
            }
            coordinates[1] = level;
        } catch (Exception e) {
            this.senderError(e);
        }

        return coordinates;
    }

    /**
     * Returns the resources that the player wants to pay
     * @return the resources that the player wants to pay
     */
    public String[][] getPayedResources() {
        String[][] pickedResources = new String[2][4];
        for (int r=0; r<2; r++)
            for(int c=0; c<4; c++)
                if(r==0)
                    pickedResources[r][c] = "0";
                else pickedResources[r][c] = "";
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

            System.out.println("Which resource do you want to pay? Write STOP at the end.");
            parameter = this.input.nextLine().toUpperCase();
            if (parameter.equals("COINS") || parameter.equals("STONES") || parameter.equals("SERVANTS") || parameter.equals("SHIELDS") || parameter.equals("STOP")) {
                res++;

                if(parameter.equals("STOP"))
                    break;

                //Receives now quantity
                System.out.println("How much " + parameter + " do you want to pick?");
                System.out.println("Write the correct value.");
                quantity = this.input.nextLine();
                try {
                    while (!quantity.equals("0") && !quantity.equals("1") && !quantity.equals("2") && !quantity.equals("3") &&
                            !quantity.equals("4") && !quantity.equals("5") && !quantity.equals("6") && !quantity.equals("7"))
                    {
                        System.err.println("Not valid input");
                        System.out.println("Write the correct value.");
                        quantity = this.input.nextLine();
                    }
                    pickedResources[0][resources.get(parameter)] = quantity;
                } catch (Exception e) {
                    this.senderError(e);
                    break;
                }

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

    /**
     * Returns the position on which the player wants to put the card
     * @return the position on which the player wants to put the card
     */
    public int getChosenPosition() {
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

    /**
     * Prints player's activated cards
     * @param cards player's cards
     */
    public void printActivatedLeaderCard(LeaderCard[] cards){
        System.out.println("YOUR ACTIVATED LEADER CARDS:");
        for (LeaderCard card : cards)
            if (card != null && card.isPlayed())
                this.printLeaderCard(card);
        System.out.println();
    }

    /**
     * Prints a card
     * @param card card to be printed
     */
    public void printLeaderCard(LeaderCard card){
        card.printLeaderCard();
    }

    /**
     * Prints the market
     * @param market market to be printed
     */
    public void printMarket(Market market){
        market.printMarket();
    }

    /**
     * Prints the playerBoard
     * @param playerboard playerBoard to be printed
     */
    public void printPlayerboard(Playerboard playerboard){
        playerboard.printAll();
    }

    /**
     * Prints the development cards grid
     * @param grid development cards grid to be printed
     */
    public void printDevCardGrid(DevelopmentCardsDecksGrid grid){
        grid.printGrid();
    }

    /**
     * Returns true if the player inserted a correct input
     * @param wlChoice string to be checked
     * @return true if the player inserted a correct input
     */
    public boolean checkShelf(String wlChoice){
        for (int k = 0; k < wlChoice.length(); k++){
            if (!String.valueOf(wlChoice.charAt(k)).equals("W") && !String.valueOf(wlChoice.charAt(k)).equals("L"))
                return true;
        }
        return false;
    }

    /**
     * Returns true if the command to activate the marble to activate is correct
     * @param chosenMarble command to activate the marble
     * @return true if the command to activate the marble to activate is correct
     */
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

    /**
     * Returns the production power to activate
     * @param activation production powers activated
     * @return the production power to activate
     */
    public int getActivationProd(int[] activation) {
        String prodPower;

        this.printPlayerboard(this.main.getPlayerboard());
        this.printActivatedLeaderCard(this.main.getLeaderCards());

        System.out.println("Which production power do you want to activate?");
        if (activation[0] == 0)
            System.out.println("Write p0 if you want to activate the first production of your grid, if it's available");
        if (activation[1] == 0)
            System.out.println("Write p1 if you want to activate the second production of your grid, if it's available");
        if (activation[2] == 0)
            System.out.println("Write p2 if you want to activate the third production of your grid, if it's available");
        if (activation[3] == 0)
            System.out.println("Write b if you want to activate the basic production power");
        if (activation[4] == 0)
            System.out.println("Write e0 if you want to activate the first extra production power, if it's available");
        if (activation[5] == 0)
            System.out.println("Write e1 if you want to activate the second extra production power, if it's available");
        System.out.println("Write STOP if you don't want to activate production powers");
        prodPower = input.nextLine().toUpperCase();
        int check = this.checkProduction(prodPower, activation);
        while(check == -1){
            System.err.println("Not valid input.");
            System.out.println("Write a correct production power code.");
            prodPower = input.nextLine().toUpperCase();
            check = this.checkProduction(prodPower, activation);
        }
        return check;
    }

    /**
     * Returns true if the player inserted a correct input to activate the production power
     * @param prodPower production power to be activated
     * @param activation production powers activated
     * @return true if the player inserted a correct input to activate the production power
     */
    public int checkProduction(String prodPower, int[] activation){
        switch(prodPower){
            case "P0":
            {
                if(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[2][0]==null &&
                        this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[1][0]==null &&
                        this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[0][0]==null)
                    return -1;

                if(activation[0]==0)
                {
                    if(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[2][0]!=null)
                        System.out.println(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[2][0].printCardProductionPower());
                    else if (this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[1][0]!=null)
                        System.out.println(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[1][0].printCardProductionPower());
                        else if (this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[0][0]!=null)
                            System.out.println(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[0][0].printCardProductionPower());
                    return 0;
                }
                else return -1;
            }
            case "P1":
            {
                if(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[2][1]==null &&
                        this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[1][1]==null &&
                        this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[0][1]==null)
                    return -1;

                if(activation[1]==0)
                {
                    if(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[2][1]!=null)
                        System.out.println(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[2][1].printCardProductionPower());
                    else if (this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[1][1]!=null)
                        System.out.println(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[1][1].printCardProductionPower());
                    else if (this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[0][1]!=null)
                        System.out.println(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[0][1].printCardProductionPower());
                    return 1;
                }
                else return -1;
            }
            case "P2":
            {
                if(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[2][2]==null &&
                        this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[1][2]==null &&
                        this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[0][2]==null)
                    return -1;

                if(activation[2]==0)
                {
                    if(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[2][2]!=null)
                        System.out.println(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[2][2].printCardProductionPower());
                    else if (this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[1][2]!=null)
                        System.out.println(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[1][2].printCardProductionPower());
                    else if (this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[0][2]!=null)
                        System.out.println(this.main.getPlayerboard().getPlayerBoardDevelopmentCards()[0][2].printCardProductionPower());
                    return 2;
                }
                else return -1;
            }
            case "B":
            {
                if(activation[3]==0)
                {
                    System.out.println("1? 1? } 1? (NO FP)");
                    return 3;
                }
                else return -1;
            }
            case "E0":
            {
                if(this.main.getPlayerboard().getExtraProductionPowerInput()[0]==null)
                    return -1;

                if(activation[4]==0)
                {
                    {
                        System.out.println("1"+this.main.getPlayerboard().getExtraProductionPowerInput()[0].substring(0,2)+" } 1? 1FP");
                        return 4;
                    }
                }
                else return -1;
            }
            case "E1":
            {
                if(this.main.getPlayerboard().getExtraProductionPowerInput()[1]==null)
                    return -1;

                if(activation[5]==0)
                {
                    System.out.println("1"+this.main.getPlayerboard().getExtraProductionPowerInput()[1].substring(0,2)+" } 1? 1FP");
                    return 5;
                }
                else return -1;
            }
            case "STOP":
            {
                return 6;
            }
            default:
            {
                return -1;
            }
        }
    }

    /**
     * Returns the chosen output of the production power
     * @return the chosen output of the production power
     */
    public String getInputResourceProd() {
        StringBuilder whichInput = new StringBuilder();
        String res;

        do{

            System.out.println("Which input resource do you want to spend?");
            System.out.println("Write COINS, STONES, SERVANTS or SHIELDS.");
            System.out.println("Write STOP at the end.");
            res = input.nextLine().toUpperCase();
            while (!res.equals("COINS") && !res.equals("STONES") && !res.equals("SERVANTS") && !res.equals("SHIELDS") && !res.equals("STOP")) {
                System.out.println("Choose a correct resource");
                System.out.println("Which input resource do you want to spend?");
                System.out.println("Write COINS, STONES, SERVANTS or SHIELDS.");
                System.out.println("Write STOP at the end.");
                res = input.nextLine().toUpperCase();
            }

            switch (res){
                case "COINS": {
                    whichInput.append("0");
                    break;
                }
                case "SERVANTS": {
                    whichInput.append("1");
                    break;
                }
                case "SHIELDS": {
                    whichInput.append("2");
                    break;
                }
                case "STONES": {
                    whichInput.append("3");
                    break;
                }
            }

            if (!res.equals("STOP")) {
                System.out.println("How much of them do you want to pick?");
                String quant = this.input.nextLine(); //quantity

                while(!quant.equals("1") && !quant.equals("2")) {
                    System.err.println("Not valid input.");
                    System.out.println("How much of them do you want to pick?");
                    quant = this.input.nextLine(); // quantity
                }
                whichInput.append(quant);

                if (quant.equals("1"))
                    System.out.println("From which store do you want to pick this resource?");
                else
                    System.out.println("From which store do you want to pick these resources?");
                System.out.println("Write WAREHOUSE, CHEST, or LEADER CARD if you activate your extra warehouse space leader card.");

                String shelf = input.nextLine().toUpperCase();

                while(!shelf.equals("WAREHOUSE") && !shelf.equals("CHEST") && !shelf.equals("LEADER CARD") &&
                        !shelf.equals("W") && !shelf.equals("C") && !shelf.equals("L")) {
                    System.err.println("Not valid input.");
                    if (quant.equals("1"))
                        System.out.println("From which store do you want to pick this resource?");
                    else
                        System.out.println("From which store do you want to pick these resources?");
                    shelf = input.nextLine(); // quantity
                }
                whichInput.append(shelf.charAt(0));
            }

        } while (!res.equals("STOP"));

        return whichInput.toString();
    }

    /**
     * Returns the chosen output of the production power
     * @param prod production power
     * @return the chosen output of the production power
     */
    public String getOutputResourceProd(int prod) {

        String res;

        System.out.println("Which output resource do you want to pick?");
        if(prod!=3) System.out.println("Write COINS, STONES, SERVANTS, SHIELDS or REDCROSS.");
        else System.out.println("Write COINS, STONES, SERVANTS or SHIELDS.");
        res = input.nextLine().toUpperCase();
        if(prod!=3) {
            while (!res.equals("COINS") && !res.equals("STONES") && !res.equals("SERVANTS") && !res.equals("SHIELDS") && !res.equals("REDCROSS")) {
                System.err.println("Choose a correct resource");
                System.out.println("Write COINS, STONES, SERVANTS, SHIELDS or REDCROSS.");
                res = input.nextLine().toUpperCase();
            }
        } else {
            while (!res.equals("COINS") && !res.equals("STONES") && !res.equals("SERVANTS") && !res.equals("SHIELDS")) {
                System.out.println("Choose a correct resource");
                System.out.println("Write COINS, STONES, SERVANTS or SHIELDS.");
                res = input.nextLine().toUpperCase();
            }
        }

        switch (res){
            case "COINS": {
                return "0";
            }
            case "SERVANTS": {
                return "1";
            }
            case "SHIELDS": {
                return "2";
            }
            case "STONES": {
                return "3";
            }
            default:{
                return "4";
            }
        }
    }

    @Override
    public void endTurn(String turn) {
        switch (turn){
            case "SETUP":
            {
                System.out.println("Your setup has ended, wait for other players setup.");
                break;
            }
            case "TURN":
            {
                System.out.println("Your turn has ended, wait for other players.");
                break;
            }
        }
    }

    @Override
    public void quit() {
        System.out.println("You left the Game");
        System.exit(0);
    }

    @Override
    public void notValidAction() {
        System.err.println("Not valid action");
    }

    /**
     * Prints Lorenzo the Magnificent faith points
     */
    public void lorenzoFaithPoints() {
        System.out.println("LORENZO FAITH POINTS: " + this.main.getLocalPlayers()[1].getPlayerBoard().getFaithPath().getCrossPosition());
    }

    @Override
    public void endLocalGame(String localWinner) {
        System.out.println("Match has ended.");
        if(localWinner.equals(this.main.getLocalPlayers()[0].getNickname()))
            System.out.println("You win the Game, with "+this.main.getVictoryPoints()+" Victory points.");
        else {
            System.out.println("Lorenzo the Magnificent wins the Game.");
            System.out.println("You made " + this.main.getVictoryPoints());
        }
    }

    @Override
    public void notYourTurn() {
        System.err.println("It's not your turn.");
    }

    @Override
    public void endMultiplayerGame(GameOverMessage gameOverMessage) {
        System.out.println("The winner is " + gameOverMessage.getWinner());
        System.out.println("You made " + gameOverMessage.getVictoryPoints() + " victory points");
    }

    @Override
    public void itsYourTurn() {
        System.out.println("It's your turn!!");
    }


    @Override
    public void connectionError(Exception e) {
        //e.printStackTrace();
        System.err.println("Connection error");
        System.exit(-1);
    }

    @Override
    public void setupError(Exception e) {
        //e.printStackTrace();
        System.err.println("Error during setup");
        System.exit(-1);
    }

    @Override
    public void gameError(Exception e) {
        //e.printStackTrace();
        System.err.println("The application encountered a problem");
        System.out.println(-3);
    }

    @Override
    public void serverError(String error) {
        System.err.println(error);
        System.exit(-5);
    }

    @Override
    public void invalidInputError(Exception e) {
        System.err.println("Not valid input");
    }

    @Override
    public void error(Exception e){
        e.printStackTrace();
        System.err.println("Error");
    }

    @Override
    public void receiverError(Exception e){
        //e.printStackTrace();
        System.err.println("Error occurred while receiving data");
        System.exit(-3);
    }

    @Override
    public void senderError(Exception e){
        //e.printStackTrace();
        System.err.println("Error occurred while sending data");
        System.exit(-2);
    }

    @Override
    public void receivePing(ObjectInputStream objectInputStream) {
        try {
            objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ping failed");
        }
    }

    /**
     * Prints which action counter it is drawn
     * @param counter counter to be printed
     */
    public void actionCounter (ActionCounter counter){
        System.out.println("Drawn " + counter.getCounter());
    }
}
