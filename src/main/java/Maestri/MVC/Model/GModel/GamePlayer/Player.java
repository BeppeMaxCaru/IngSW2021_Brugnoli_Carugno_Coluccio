package Maestri.MVC.Model.GModel.GamePlayer;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.io.PrintWriter;
import java.util.*;

/**
 * Represents a player with all its information
 */
public class Player extends Thread implements Runnable {

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

    /**
     * Initializes a new player
     * @param nickname - nickname to assign to the player
     *
     */
    public Player(String nickname) {
        this.nickname = nickname;
        //this.playerNumber = playerNumber;
        this.playerBoard = new Playerboard();
        this.playerLeaderCards = new LeaderCard[4];
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

                    if (resourceNum.equals("0")) resource = "COINS";
                    else if (resourceNum.equals("1")) resource = "SHIELDS";
                    else if (resourceNum.equals("2")) resource = "SERVANTS";
                    else resource = "STONES";

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
     */
    public void pickLineFromMarket(Market market, Player[] players, Scanner in, PrintWriter out) {

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
                int var = Integer.parseInt(rowNum);
                market.updateRow(var, players, this.playerNumber, in, out);
            }
        }

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
        int numResourceChoice;
        int activateProduction = -1;
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
                while (activateProduction != 0 && activateProduction != 1) {
                    out.println("This is the resources you have to pay: " + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput());
                    out.println("For this:" + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput());
                    out.println("Do you want to activate this production power?: Write 1 if you want or 0 if you don't:");
                    activateProduction = in.nextInt();
                }
                if (activateProduction == 1) {
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
        activateProduction = -1;
        while(activateProduction != 0 && activateProduction != 1) {
            out.println("Do you want to activate also the basic production power?: Write 1 if you want or 0 if you don't:");
            activateProduction = in.nextInt();
        }
        if(activateProduction == 1) {
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
            activateProduction = -1;
            while(activateProduction != 0 && activateProduction != 1) {
                out.println("Do you want to activate the extra production power that cost a" + this.playerBoard.getExtraProductionPowerInput()[i] + "?: Write 1 if you want or 0 if you don't:");
                activateProduction = in.nextInt();
            }
            if(activateProduction == 1) {
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
            numResourceChoice = -1;
            while (numResourceChoice < 0 || numResourceChoice > 4) {
                out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES, 4 for REDCROSS");
                numResourceChoice = in.nextInt();
            }

            if (i==0) outputResources.put("COINS", outputResources.get("COINS") + 1);
            else if (i==1) outputResources.put("SHIELDS", outputResources.get("SHIELDS") + 1);
            else if (i==2) outputResources.put("SERVANTS", outputResources.get("SERVANTS") + 1);
            else if (i==3) outputResources.put("STONES", outputResources.get("STONES") + 1);
            else if (i==4) redCross = redCross + 1;

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
     * @return true if the player wants to perform it
     */
    public void getLeaderAction(Scanner in, PrintWriter out) {
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
            return;

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
    }

    /**
     * Plays a leader card
     */
    public boolean playLeaderCard(Scanner in, PrintWriter out) {
        int numLeaderCard;
        int k;
        int notPlayed=0;

        for(k=0; k<4; k++){
            if(this.playerLeaderCards[k]==null)
                break;
            if(!this.playerLeaderCards[k].isPlayed())
                notPlayed++;
        }

        //If there are cards that aren't played yet
        if(notPlayed<k)
        {
            //Scelta della carta leader da giocare
            do{
                out.println("What leader card do you want to play?:");
                for (int i = 0; i < k; i++) {
                    if(!this.playerLeaderCards[i].isPlayed())
                    {
                        out.println("Write" + i + "for this: ");
                        this.playerLeaderCards[i].printLeaderCard(out);
                    }
                }
                numLeaderCard = in.nextInt();

                if(this.playerLeaderCards[numLeaderCard].isPlayed()) {
                    out.println("You can't play this card, you have already played it.");
                    numLeaderCard = -1;
                }
            }while((numLeaderCard<0) || (numLeaderCard > k));

            if(this.playerLeaderCards[numLeaderCard]!=null){
                if((this.playerLeaderCards[numLeaderCard].checkRequisites(this.playerBoard))){
                    this.playerLeaderCards[numLeaderCard].activateAbility(this.playerBoard);
                    this.playerBoard.sumVictoryPoints(this.playerLeaderCards[numLeaderCard].getVictoryPoints());
                    return true;
                }
                else {
                    out.println("Not enough resources for play this Leader Card");
                    return false;
                }
            }else
            {
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
        int numLeaderCard = -1;
        int k;
        int notPlayed=0;

        for(k=0; k<4; k++){
            if(this.playerLeaderCards[k]==null)
                break;
            if(!this.playerLeaderCards[k].isPlayed())
                notPlayed++;
        }


        //If deck isn't empty and there are cards not played yet
        if(k>0&&notPlayed>0){
            if(k==1)
                numLeaderCard=0;
            else
            {
                //Scelta della carta leader da scartare
                while(numLeaderCard < 0 || numLeaderCard > k) {
                    out.println("What leader card do you want to discard?:");
                    for (int i = 0; i < k; i++) {
                        if(!this.playerLeaderCards[i].isPlayed())
                        {
                            out.println("Write " + i + " for this: ");
                            this.playerLeaderCards[i].printLeaderCard(out);
                        }
                    }
                    numLeaderCard = in.nextInt();
                    if(this.playerLeaderCards[numLeaderCard].isPlayed())
                    {
                        out.println("You can't discard this card, you have already played it.");
                        numLeaderCard=-1;
                    }
                }
            }
            out.println("Discarded");
            this.playerLeaderCards[numLeaderCard].printLeaderCard(out);

            //Rimozione carta leader dal deck
            //this.playerLeaderCards[numLeaderCard].discard(this.playerBoard);
            List<LeaderCard> updatedPlayerLeaderCardList = new ArrayList<>(Arrays.asList(this.playerLeaderCards));
            updatedPlayerLeaderCardList.remove(numLeaderCard);
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

    public void printPlayerCards(Scanner in, PrintWriter out)
    {
        for(int row=2; row>=0; row--)
        {

            for(int k=0; k<3; k++){
                if(this.playerBoard.getPlayerboardDevelopmentCards()[row][k]!=null)
                    System.out.print("| "+this.playerBoard.getPlayerboardDevelopmentCards()[row][k].printCardProductionPower()+" ");
                else System.out.print("|                       ");
            }
            System.out.print("|");
            System.out.println();

            for(int k=0; k<3; k++){
                if(this.playerBoard.getPlayerboardDevelopmentCards()[row][k]!=null)
                {
                    System.out.print("| Victory Points: "+this.playerBoard.getPlayerboardDevelopmentCards()[row][k].getVictoryPoints()+"    ");
                    if(this.playerBoard.getPlayerboardDevelopmentCards()[row][k].getVictoryPoints()<10)
                        System.out.print(" ");
                }
                else System.out.print("|                       ");
            }
            System.out.print("|");
            System.out.println();

            System.out.println();
        }
    }

    @Override
    public void run() {
        //this.getLeaderAction();
        //this.getAction();
        //this.getLeaderAction();
    }
}
