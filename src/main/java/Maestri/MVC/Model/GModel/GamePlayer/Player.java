package Maestri.MVC.Model.GModel.GamePlayer;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Represents a player with all its information
 */
public class Player implements Runnable {

    /**
     * Name that the player chooses to join the game
     */
    private String nickname;

    /**
     * Order in which the player joined the game
     */
    private Integer playerNumber;

    /**
     * Player board that the player is using to play the game
     */
    private Playerboard playerBoard;

    /**
     * Leader cards that the player has available
     */
    private LeaderCard[] playerLeaderCards;

    /**
     * Player total victory points
     */
    private int playerTotalVictoryPoints = 0;

    private Socket clientSocket;

    /**
     * Initializes a new player
     * @param clientSocket
     *
     */
    public Player(Socket clientSocket) {
        //this.playerNumber = playerNumber;
        this.playerBoard = new Playerboard();
        this.playerLeaderCards = new LeaderCard[4];

        this.clientSocket = clientSocket;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);

            out.println("Insert your nickname: ");
            this.nickname= in.readLine();
            out.println("Welcome! Waiting for the match (thread run)");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public Player(String nickname){
        this.nickname=nickname;
        this.playerBoard=new Playerboard();
        this.playerLeaderCards=new LeaderCard[4];
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    /**
     * Asks and sets the player's nickname
     */
    public void chooseNickname(Scanner in, PrintWriter out) {

        out.println("Choose your nickname to join the game: ");
        this.nickname = in.next();
    }

    public String getNickname(){
        return this.nickname;
    }

    /**
     * Returns the player's nickname
     * @return the player's nickname
     */
    public Integer getPlayerNumber() {
        return this.playerNumber;
    }

    /**
     * Sets the player's number
     * @param number - the player's number
     */
    public void setPlayerNumber(Integer number) {
        this.playerNumber = number;
    }

    /**
     * Returns the player's board
     * @return the player's board
     */
    public Playerboard getPlayerBoard() {
        return this.playerBoard;
    }

    /**
     * Returns the player's leader cards
     * @return the player's leader cards
     */
    public LeaderCard[] getPlayerLeaderCards() {
        return this.playerLeaderCards;
    }

    /**
     * Sets to the player a leader card in the specified position of his leader cards deck
     * @param index - index where to set the leader card
     * @param leaderCard - leader card to set
     */
    public void setPlayerLeaderCard(int index, LeaderCard leaderCard) {
        this.playerLeaderCards[index] = leaderCard;
    }

    /**
     * Sets to the player its starting board depending on its number
     */
    public void setStartingPlayerboard(Scanner in, PrintWriter out) {
        Map<Integer, Integer[]> startingResources =  new HashMap<>();
        int numChosenResources;
        int numInitialRedCross;
        String resourceNum = " ";
        String resource;
        int resourceNumWarehouse;

        startingResources.put(0, new Integer[] {0, 0});
        startingResources.put(1, new Integer[] {1, 0});
        startingResources.put(2, new Integer[] {1, 1});
        startingResources.put(3, new Integer[] {2, 1});

        for (Integer key : startingResources.keySet()){
            if(this.playerNumber.equals(key)) {
                numChosenResources = startingResources.get(key)[0];
                numInitialRedCross = startingResources.get(key)[1];
                while(numChosenResources > 0) {
                    while(!resourceNum.equals("0") && !resourceNum.equals("1") && !resourceNum.equals("2") && !resourceNum.equals("3")) {
                        out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
                        resourceNum = in.nextLine();
                    }

                    switch (resourceNum) {
                        case "0":
                            resource = "COINS";
                            break;
                        case "1":
                            resource = "SHIELDS";
                            break;
                        case "2":
                            resource = "SERVANTS";
                            break;
                        default:
                            resource = "STONES";
                            break;
                    }

                    resourceNumWarehouse = getPlayerBoard().getWareHouse().getWarehouseResources().get(resource);
                    getPlayerBoard().getWareHouse().getWarehouseResources().put(resource, resourceNumWarehouse + 1);

                    numChosenResources--;
                }
                if(numInitialRedCross == 1) {
                    getPlayerBoard().getFaithPath().moveCross(1);
                }
                break;
            }
        }
    }

    /**
     * Asks the player which action he wants to perform
     * @return the chosen action's number
     */
    public String getAction(Scanner in, PrintWriter out) {
        String actionNumber;

        out.println("What action do you want to do? Choose one of them:");
        out.println("Write 0 if you want to take resources from the market.");
        out.println("Write 1 if you want to buy a development card.");
        out.println("Write 2 if you want to activate the production.");
        actionNumber = in.nextLine();
        while(!actionNumber.equals("0") && !actionNumber.equals("1") && !actionNumber.equals("2")) {
            out.println("Number not valid!");
            out.println("What action do you want to do? Choose one of them:");
            out.println("Write 0 if you want to take resources from the market.");
            out.println("Write 1 if you want to buy a development card.");
            out.println("Write 2 if you want to activate the production.");
            actionNumber = in.nextLine();
        }

        return actionNumber;
    }

    /**
     * Draws marbles from the market
     * @param market - market from where to draw marbles
     * @param players - players playing the game
     * @return
     */
    public boolean pickLineFromMarket(Market market, Player[] players, Scanner in, PrintWriter out) {

        String rowColumnChoice;
        String columnNum;
        String rowNum;

        // Scelta colonna/riga.
        out.println("Do you want to choose a column or a row from the market? Write 0 for column, 1 for row:");
        rowColumnChoice = in.nextLine();
        while(!rowColumnChoice.equals("0") && !rowColumnChoice.equals("1")) {
            out.println("Number not valid!");
            out.println("Do you want to choose a column or a row from the market? Write 0 for column, 1 for row:");
            rowColumnChoice = in.nextLine();
        }

        // Scelta numero di colonna/riga e mette risorse nel warehouse.
        if(rowColumnChoice.equals("0")) {
            out.println("Choose the column's number you want to get the resources from:");
            //market.printMarket();
            columnNum = in.nextLine();
            while (!columnNum.equals("0") && !columnNum.equals("1") && !columnNum.equals("2") && !columnNum.equals("3")) {
                out.println("Number not valid!");
                out.println("Choose the column's number you want to get the resources from:");
                //market.printMarket();
                columnNum = in.nextLine();
            }
            int var = Integer.parseInt(columnNum);
            market.updateColumn(var, players, this.playerNumber, in, out);
        }
        else {
            out.println("Choose the row's number you want to get the resources from:");
            //market.printMarket();
            rowNum = in.nextLine();
            while (!rowNum.equals("0") && !rowNum.equals("1") && !rowNum.equals("2")) {
                out.println("Choose the row's number you want to get the resources from:");
                //market.printMarket();
                rowNum = in.nextLine();
            }
            int var = Integer.parseInt(rowNum);
            market.updateRow(var, players, this.playerNumber, in, out);
        }
        return true;

    }

    /**
     * Buys a development card
     * @param developmentCardsDecksGrid - developments cards available to buy
     * @return true if the player buys a card successfully
     */
    public boolean buyDevelopmentCard(DevelopmentCardsDecksGrid developmentCardsDecksGrid, Scanner in, PrintWriter out) {

        out.println("Available development cards colours: " + developmentCardsDecksGrid.getDevelopmentCardsColours());
        out.println("Choose development card colour: ");
        String colour = in.next();
        out.println();
        while (!developmentCardsDecksGrid.getDevelopmentCardsColours().containsKey(colour)) {
            out.println("Card of this colour doesn't exist!");
            out.println("Choose a valid development card colour: ");
            colour = in.next();
            out.println();
        }
        out.println("Available development cards levels: " + developmentCardsDecksGrid.getDevelopmentCardsLevels());
        out.println("Choose development card level: ");
        String level = in.next();
        out.println();
        while(!level.equals("1") && !level.equals("2") && !level.equals("3")) {
            out.println("Choose development card level: ");
            level = in.next();
            out.println();
        }
        int varLevel = Integer.parseInt(level);
        //check if the input card exists otherwise choose again
        while (!developmentCardsDecksGrid.getDevelopmentCardsLevels().contains(varLevel)) {
            out.println("Card of this level doesn't exist!");
            level = " ";
            while(!level.equals("1") && !level.equals("2") && !level.equals("3")) {
                out.println("Choose a valid development card level: ");
                level = in.next();
                out.println();
            }
            varLevel = Integer.parseInt(level);
        }

        varLevel = 3 - varLevel;
        int column = developmentCardsDecksGrid.getDevelopmentCardsColours().get(colour);
        //Check if selected pile is empty
        while (developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column][0] == null) {
            out.println("Empty development cards pile!");
            out.println("Choose new pile");
            out.println();
            out.println("Available development cards colours: " + developmentCardsDecksGrid.getDevelopmentCardsColours());
            out.println("Choose development card colour: ");
            colour = in.next();
            out.println();
            while (!developmentCardsDecksGrid.getDevelopmentCardsColours().containsKey(colour)) {
                out.println("Card of this colour doesn't exist!");
                out.println("Choose a valid development card colour: ");
                colour = in.next();
                out.println();
            }
            out.println("Available development cards levels: " + developmentCardsDecksGrid.getDevelopmentCardsLevels());
            out.println("Choose development card level: ");
            level = in.next();
            out.println();
            while(!level.equals("1") && !level.equals("2") && !level.equals("3")) {
                out.println("Choose development card level: ");
                level = in.next();
                out.println();
            }
            varLevel = Integer.parseInt(level);
            //check if the input card exists otherwise choose again
            while (!developmentCardsDecksGrid.getDevelopmentCardsLevels().contains(varLevel)) {
                out.println("Card of this level doesn't exist!");
                level = " ";
                while(!level.equals("1") && !level.equals("2") && !level.equals("3")) {
                    out.println("Choose a valid development card level: ");
                    level = in.next();
                    out.println();
                }
                varLevel = Integer.parseInt(level);
            }
            column = developmentCardsDecksGrid.getDevelopmentCardsColours().get(colour);
        }

        //Asks player if he wants to activate discount perks
        Map<String, Integer> developmentCardCost = developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column][0].getDevelopmentCardCost();
        Integer resourceCost;
        //Checks the discounts that the player has available
        if(this.playerBoard.getDevelopmentCardDiscount()[0] != null){
            for(String res : this.playerBoard.getDevelopmentCardDiscount()) {
                resourceCost = developmentCardCost.get(res);
                if(resourceCost != 0) {
                    out.println("Do you want to pay the reduced price for this resource? Insert 0 for no, 1 for yes");
                    String input = in.nextLine();
                    while(!input.equals("0") && !input.equals("1")) {
                        out.println("Number not valid!");
                        out.println("Do you want to pay the reduced price for this resource? Insert 0 for no, 1 for yes");
                        input = in.nextLine();
                    }
                    if(input.equals("1")) developmentCardCost.put(res, resourceCost-1);
                }
            }
        }


        //Checks if the player has enough resources to buy the new development card
        if (this.getPlayerBoard().checkResourcesAvailability(developmentCardCost, out)) {
            //Checks if the player can place the new development card on his board
            if (developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column][0].checkPlayerboardDevelopmentCardsCompatibility(playerBoard, out)) {
                //Removes the resources from the player who bought the development card
                //according to the development card cost
                developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column][0].payDevelopmentCard(this.playerBoard, in, out);
                //Ask the player where to place the new development card on his board
                playerBoard.placeNewDevelopmentCard(developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column][0], in, out);
                //Updates the player victory points by adding to them the
                //victory points obtained from the new development card
                playerBoard.sumVictoryPoints(developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column][0].getVictoryPoints());
                //Removes the development card from the grid by removing it
                //from the deck where it was bought
                List<DevelopmentCard> reducedDeck = new ArrayList<>(Arrays.asList(developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column]));
                reducedDeck.remove(0);
                developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column] = reducedDeck.toArray(developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column]);
                return true;
            } else if (!developmentCardsDecksGrid.getDevelopmentCardsDecks()[varLevel][column][0].checkPlayerboardDevelopmentCardsCompatibility(playerBoard, out)) {
                return false;
            }
        } else
            return false;
        //Returns false if the player wasn't able to buy the development card
        return false;
        //If buying the card isn't a possible action or is denied and player has to choose new action and start all over
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /**
     * Activates the production powers on the player's player board
     */
    public boolean activateProduction(Scanner in, PrintWriter out) {
        int i;
        int numResource;
        String numResourceChoice;
        String activateProduction;
        int numExtraProductionActivate = 0;
        int redCross = 0;
        Map<String, Integer> inputResources = new HashMap<>();
        inputResources.put("COINS", 0);
        inputResources.put("SHIELDS", 0);
        inputResources.put("SERVANTS", 0);
        inputResources.put("STONES", 0);
        Map<String, Integer> outputResources = new HashMap<>();
        outputResources.put("COINS", 0);
        outputResources.put("SHIELDS", 0);
        outputResources.put("SERVANTS", 0);
        outputResources.put("STONES", 0);

        for(int j = 0; j < 3; j++) {
            for (i = 0; getPlayerBoard().getPlayerboardDevelopmentCards()[i][j] != null; i++) {
                out.println("This is the resources you have to pay: " + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput());
                out.println("For this:" + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput());
                out.println("Do you want to activate this production power?: Write 1 if you want or 0 if you don't:");
                activateProduction = in.nextLine();
                while (!activateProduction.equals("0") && !activateProduction.equals("1")) {
                    out.println("Number not valid!");
                    out.println("Do you want to activate this production power?: Write 1 if you want or 0 if you don't:");
                    activateProduction = in.nextLine();
                }
                if (activateProduction.equals("1")) {
                    for (String key : getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput().keySet()) {
                        numResource = inputResources.get(key);
                        inputResources.put(key, numResource + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput().get(key));
                    }
                    for (String key : getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().keySet()) {
                        numResource = outputResources.get(key);
                        outputResources.put(key, numResource + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().get(key));
                    }
                    // Aggiunta alla conta totale i redCross
                    redCross = redCross + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getFaithPoints();
                }
            }
        }

        // Chiede se vuole attivare il potere di produzione base.
        out.println("Do you want to activate also the basic production power?: Write 1 if you want or 0 if you don't:");
        activateProduction = in.nextLine();
        while(!activateProduction.equals("0") && !activateProduction.equals("1")) {
            out.println("Number not valid!");
            out.println("Do you want to activate also the basic production power?: Write 1 if you want or 0 if you don't:");
            activateProduction = in.nextLine();
        }
        if(activateProduction.equals("1")) {
            i = 0;
            //Controllo checkResourcesAvailability
            for (String s : getPlayerBoard().activateBasicProductionPower(in, out)) {
                if(i == 2) { // Se siamo alla terza risorsa, che per forza è l'output...
                    if(s.equals("REDCROSS"))
                        // Aggiunta alla conta totale i redCross.
                        redCross = redCross + 1;
                    else {
                        numResource = outputResources.get(s);
                        outputResources.put(s, numResource + 1);
                    }
                }
                else {
                    numResource = inputResources.get(s);
                    inputResources.put(s, numResource + 1);
                }
                i++;
            }
        }

        // Chiede se vuole attivare l'extra production power.
        i = 0;
        while(this.playerBoard.getExtraProductionPowerInput()[i] != null) {
            out.println("Do you want to activate the extra production power that cost a" + this.playerBoard.getExtraProductionPowerInput()[i] + "?: Write 1 if you want or 0 if you don't:");
            activateProduction = in.nextLine();
            while(!activateProduction.equals("0") && !activateProduction.equals("1")) {
                out.println("Number not valid!");
                out.println("Do you want to activate the extra production power that cost a" + this.playerBoard.getExtraProductionPowerInput()[i] + "?: Write 1 if you want or 0 if you don't:");
                activateProduction = in.nextLine();
            }
            if(activateProduction.equals("1")) {
                numResource = inputResources.get(this.playerBoard.getExtraProductionPowerInput()[i]);
                inputResources.put(this.playerBoard.getExtraProductionPowerInput()[i], numResource + 1);
                numExtraProductionActivate++;
            }
            i++;
        }

        // Controllo risorse nel chest/warehouse, e se true toglie dal chest/warehouse.
        if(this.getPlayerBoard().checkResourcesAvailability(inputResources, out)) {
            for(String key: inputResources.keySet()) {
                numResource = inputResources.get(key);
                for(int j = 0; j < numResource; j++)
                    this.playerBoard.pickResource(key, in, out);
            }
        }
        else {
            out.println("Not enough resources to activate production!");
            return false;
        }

        // Scelta delle n risorse di extra production power e inserimento in outputResources
        while(numExtraProductionActivate > 0) {
            out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES, 4 for REDCROSS");
            numResourceChoice = in.nextLine();
            while(!numResourceChoice.equals("0") && !numResourceChoice.equals("1") && !numResourceChoice.equals("2") && !numResourceChoice.equals("3") && !numResourceChoice.equals("4")) {
                out.println("Number not valid!");
                out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES, 4 for REDCROSS");
                numResourceChoice = in.nextLine();
            }

            switch (numResourceChoice) {
                case "0":
                    outputResources.put("COINS", outputResources.get("COINS") + 1);
                    break;
                case "1":
                    outputResources.put("SHIELDS", outputResources.get("SHIELDS") + 1);
                    break;
                case "2":
                    outputResources.put("SERVANTS", outputResources.get("SERVANTS") + 1);
                    break;
                case "3":
                    outputResources.put("STONES", outputResources.get("STONES") + 1);
                    break;
                default:
                    redCross = redCross + 1;
                    break;
            }

            redCross = redCross + 1;
            numExtraProductionActivate--;
        }

        //Adds output resources to the chest
        for(String key : outputResources.keySet()) {
            numResource = getPlayerBoard().getChest().getChestResources().get(key);
            getPlayerBoard().getChest().getChestResources().put(key, numResource + outputResources.get(key));
        }

        //RedCross
        getPlayerBoard().getFaithPath().moveCross(redCross);

        return true;
    }

    /**
     * Asks the player to perform a leader action
     */
    public boolean getLeaderAction(Scanner in, PrintWriter out) {
        String leaderActionNum;
        String leaderNum;
        boolean correctLeaderAction = false;
        int attempts = 0;

        out.println("Do you want to do a leader action?: Write 1 if you want or 0 if you don't:");
        leaderActionNum = in.nextLine();
        while (!leaderActionNum.equals("0") && !leaderActionNum.equals("1")) {
            out.println("Number not valid!");
            out.println("Do you want to do a leader action?: Write 1 if you want or 0 if you don't:");
            leaderActionNum = in.nextLine();
        }

        if (leaderActionNum.equals("0"))
            return true;

        out.println("Which leader action do you want to play? Write 1 if you want to play a card, write 0 if you want to discard a card");
        leaderNum = in.nextLine();
        while (!leaderNum.equals("0") && !leaderNum.equals("1")) {
            out.println("Number not valid!");
            out.println("Which leader action do you want to play? Write 1 if you want to play a card, write 0 if you want to discard a card");
            leaderNum = in.nextLine();
        }
        while (!correctLeaderAction&&attempts < 4) {
            if(leaderNum.equals("1")) {
                correctLeaderAction = this.playLeaderCard(in, out);
                attempts++;
            }
            else {
                correctLeaderAction = this.discardLeaderCard(in, out);
                attempts++;
                this.getPlayerBoard().getFaithPath().moveCross(1);
            }
        }
        return true;
    }

    /**
     * Plays a leader card
     */
    public boolean playLeaderCard(Scanner in, PrintWriter out) {
        String numLeaderCard;
        int var;
        int k;
        int notPlayed = 0;

        for(k = 0; k < 4; k++) {
            if(this.playerLeaderCards[k] == null)
                break;
            if(!this.playerLeaderCards[k].isPlayed())
                notPlayed++;
        }

        //If there are cards that aren't played yet
        if(notPlayed < k) {
            //Scelta della carta leader da giocare
            out.println("What leader card do you want to play?:");
            for (int i = 0; i < k; i++) {
                if(!this.playerLeaderCards[i].isPlayed()) {
                    out.println("Write" + i + "for this: ");
                    this.playerLeaderCards[i].printLeaderCard(out);
                }
            }
            var = -1;
            numLeaderCard = in.nextLine();
            while(var < 0 || var > notPlayed - 1) {
                while (!numLeaderCard.equals("0") && !numLeaderCard.equals("1") && !numLeaderCard.equals("2") && !numLeaderCard.equals("3")) {
                    out.println("Number not valid!");
                    out.println("What leader card do you want to discard?:");
                    numLeaderCard = in.nextLine();
                }
                var = Integer.parseInt(numLeaderCard);
                if(var < 0 || var > notPlayed - 1) {
                    out.println("Number not valid!");
                    out.println("What leader card do you want to discard?:");
                    numLeaderCard = in.nextLine();
                }
            }

            if(this.playerLeaderCards[var] != null) {
                if((this.playerLeaderCards[var].checkRequisites(this.playerBoard))) {
                    this.playerLeaderCards[var].activateAbility(this.playerBoard);
                    this.playerBoard.sumVictoryPoints(this.playerLeaderCards[var].getVictoryPoints());
                    return true;
                }
                else {
                    out.println("Not enough resources for play this Leader Card");
                    return false;
                }
            }
            else {
                out.println("Number isn't correct!!");
                return false;
            }
        }
        return false;
    }

    /**
     * Discards a leader card
     */
    public boolean discardLeaderCard(Scanner in, PrintWriter out) {
        String numLeaderCard;
        int var;
        int k;
        int notPlayed = 0;

        for(k = 0; k < 4; k++) {
            if(this.playerLeaderCards[k] == null)
                break;
            if(!this.playerLeaderCards[k].isPlayed())
                notPlayed++;
        }

        //If deck isn't empty and there are cards not played yet
        if(k > 0 && notPlayed > 0) {
            if(k == 1) var = 0;
            else {
                //Scelta della carta leader da scartare
                out.println("What leader card do you want to discard?:");
                for (int i = 0; i < k; i++) {
                    if(!this.playerLeaderCards[i].isPlayed()) {
                        out.println("Write " + i + " for this: ");
                        this.playerLeaderCards[i].printLeaderCard(out);
                    }
                }
                var = -1;
                numLeaderCard = in.nextLine();
                while(var < 0 || var > notPlayed - 1) {
                    while (!numLeaderCard.equals("0") && !numLeaderCard.equals("1") && !numLeaderCard.equals("2") && !numLeaderCard.equals("3")) {
                        out.println("Number not valid!");
                        out.println("What leader card do you want to discard?:");
                        numLeaderCard = in.nextLine();
                    }
                    var = Integer.parseInt(numLeaderCard);
                    if(var < 0 || var > notPlayed - 1) {
                        out.println("Number not valid!");
                        out.println("What leader card do you want to discard?:");
                        numLeaderCard = in.nextLine();
                    }
                }
                if(this.playerLeaderCards[var].isPlayed()) {
                    out.println("You can't discard this card, you have already played it.");
                }
            }
            out.println("Discarded");
            this.playerLeaderCards[var].printLeaderCard(out);

            //Rimozione carta leader dal deck
            //this.playerLeaderCards[numLeaderCard].discard(this.playerBoard);
            List<LeaderCard> updatedPlayerLeaderCardList = new ArrayList<>(Arrays.asList(this.playerLeaderCards));
            updatedPlayerLeaderCardList.remove(var);
            this.playerLeaderCards = updatedPlayerLeaderCardList.toArray(this.playerLeaderCards);
            return true;
        }
        else return false;
    }

    /**
     * This method sums all the player's victory points at the end of the game
     * @return the total victory points
     */
    public int sumAllVictoryPoints() {
        int victoryPoints = 0;
        int numResources = 0;

        // Punti delle tessere del tracciato fede, carte sviluppo e carte leader.
        victoryPoints = victoryPoints + getPlayerBoard().getVictoryPoints();

        // Punti dalla posizione della croce.
        //Non serve se modifichi move cross in modo tale che aggiunga i victory points di una cella ai punti
        //della player board quando il giocatore ci capita sopra così li tieni aggiornati
        victoryPoints = victoryPoints + getPlayerBoard().getFaithPath().getFaithPathTrack()[getPlayerBoard().getFaithPath().getCrossPosition()].getVictoryPoints();

        //Punti dalle risorse rimaste nel warehouse, chest, extraWarehouse.
        numResources = numResources + numResourcesReserve();
        victoryPoints = victoryPoints + (numResources / 5);

        return victoryPoints;
    }

    /**
     * Returns all the player resources count
     * @return all the player resources count
     */
    public int numResourcesReserve() {
        int numResources = 0;
        //Watch out for extras!
        for(String key : getPlayerBoard().getWareHouse().getWarehouseResources().keySet()) {
            if (!key.contains("extra")) {
                numResources = numResources + getPlayerBoard().getWareHouse().getWarehouseResources().get(key);
                if (getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + key) != null)
                    numResources = numResources + getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + key);
            }
        }
        //Ok no extras
        for(String key : getPlayerBoard().getChest().getChestResources().keySet())
            numResources = numResources + getPlayerBoard().getChest().getChestResources().get(key);

        return numResources;
    }

    public void printPlayerCards(PrintWriter out)
    {
        int[] upper = new int[3];

        for(int col=0; col<3; col++)
        {
            int row;
            for(row=2; row>0; row--)
                if(this.playerBoard.getPlayerboardDevelopmentCards()[row][col] != null)
                    break;
            if(row==-1)
                row=0;
            upper[col]=row;
        }

        for(int k=0; k<3; k++)
        {
            if(this.playerBoard.getPlayerboardDevelopmentCards()[upper[k]][k]!=null)
                out.print("| "+this.playerBoard.getPlayerboardDevelopmentCards()[upper[k]][k].printCardProductionPower()+" ");
            else out.print("|                       ");
        }
        out.print("|");
        out.println();

        for(int k=0; k<3; k++) {
            if (this.playerBoard.getPlayerboardDevelopmentCards()[upper[k]][k] != null) {
                out.print("| Victory Points: " + this.playerBoard.getPlayerboardDevelopmentCards()[upper[k]][k].getVictoryPoints() + "    ");
                if (this.playerBoard.getPlayerboardDevelopmentCards()[upper[k]][k].getVictoryPoints() < 10)
                    out.print(" ");
            } else out.print("|                       ");
        }
        out.print("|");
        out.println();

    }

    /*
    @Override
    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);

            out.println("Welcome " + this.nickname + " to Master of Renaissance online!");
            out.println("Write QUIT to leave the game anytime you want");

            //Turn
            while (true) {

                if (this.getPlayerLeaderCards()[0] != null) {
                    //for (int i = 0; i < 2; i++) {
                    if (this.getPlayerLeaderCards()[1] == null && !this.getPlayerLeaderCards()[0].isPlayed()) {
                        this.getLeaderAction(in, out); // Remove with timer
                            /* startTime = System.currentTimeMillis();
                            while ((System.currentTimeMillis() - startTime) < maximumTime * 1000 && !player.getLeaderAction(in, out)) ;
                            endTime = System.currentTimeMillis() - startTime;
                    } else if (this.getPlayerLeaderCards()[1] != null && (!this.getPlayerLeaderCards()[0].isPlayed() || !this.getPlayerLeaderCards()[1].isPlayed())) {
                        this.getLeaderAction(in, out); // Remove with timer
                            /* startTime = System.currentTimeMillis();
                            while ((System.currentTimeMillis() - startTime) < maximumTime * 1000 && !player.getLeaderAction(in, out)) ;
                            endTime = System.currentTimeMillis() - startTime;
                    } else out.println("You have activated all your Leader cards. You can't do a Leader Action.");
                    // i = checkStatusPlayer(endTime, i, out);
                    //}
                } else out.println("You have discarded all your Leader cards. You can't do a Leader Action.");

                //for (int i = 0; i < 2; i++) {
                boolean correctAction = true;
                do {
                    switch (this.getAction(in, out)) {
                        case "0":
                            //startTime = System.currentTimeMillis();
                            correctAction = this.pickLineFromMarket(this.market, this.players, in, out);
                            //endTime = System.currentTimeMillis() - startTime;
                            break;
                        case "1":
                            //startTime = System.currentTimeMillis();
                            correctAction = this.buyDevelopmentCard(this.developmentCardsDecksGrid, in, out);
                            //endTime = System.currentTimeMillis() - startTime;
                            break;
                        case "2":
                            //startTime = System.currentTimeMillis();
                            correctAction = this.activateProduction(in, out);
                            //endTime = System.currentTimeMillis() - startTime;
                            break;
                    }
                } while (!correctAction); // Remove with timer
                //while ((System.currentTimeMillis() - startTime) < maximumTime * 1000 && !correctAction);
                //i = checkStatusPlayer(endTime, i, out);
                //}

                if (this.getPlayerLeaderCards()[0] != null) {
                    //for (int i = 0; i < 2; i++) {
                    if (this.getPlayerLeaderCards()[1] == null && !this.getPlayerLeaderCards()[0].isPlayed()) {
                        this.getLeaderAction(in, out); // Remove with timer
                            /* startTime = System.currentTimeMillis();
                            while ((System.currentTimeMillis() - startTime) < maximumTime * 1000 && !player.getLeaderAction(in, out)) ;
                            endTime = System.currentTimeMillis() - startTime;
                    } else if (this.getPlayerLeaderCards()[1] != null && (!this.getPlayerLeaderCards()[0].isPlayed() || !this.getPlayerLeaderCards()[1].isPlayed())) {
                        this.getLeaderAction(in, out); // Remove with timer
                            /* startTime = System.currentTimeMillis();
                            while ((System.currentTimeMillis() - startTime) < maximumTime * 1000 && !player.getLeaderAction(in, out)) ;
                            endTime = System.currentTimeMillis() - startTime;
                    }
                    // i = checkStatusPlayer(endTime, i, out);
                    //}
                }


                if (clientInput.equals("QUIT")) {
                    out.println("You left the game");
                    break;
                }
                //out.println("Ciao! Inserisci un comando: ");
                //else out.println(clientInput);
            }

            in.close();

            out.close();
            this.clientSocket.close();
            //this.wait();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //this.out.println("Lost connection");
        }
    }*/

    //Closing stream and socket
    public void disconnectClient() {
        try {
            //this.in.close();
            //this.out.close();
            this.clientSocket.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //this.out.println("Thanks for playing");
            //this.out.println("See you next time");
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);

            out.println("Hi " + this.nickname + "! Welcome to Masters of renaissance online!");
            //out.println("Write QUIT to leave the game anytime you want");
            while (true) {
                out.println("Inserisci un comando: ");
                String clientInput = in.readLine();
                if (clientInput.equals("QUIT")) {
                    out.println("You left the game");
                    break;
                }
                //out.println("Ciao! Inserisci un comando: ");
                //else out.println(clientInput);
            }

            in.close();

            out.close();
            this.clientSocket.close();
            //this.wait();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //this.out.println("Lost connection");
        }

    }

    //Closing stream and socket
    public void disconnectPlayer() {
        try {
            //this.in.close();
            //this.out.close();
            this.clientSocket.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //this.out.println("Thanks for playing");
            //this.out.println("See you next time");
        }
    }
}
