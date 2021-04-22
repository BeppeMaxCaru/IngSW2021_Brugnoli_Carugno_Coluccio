package Maestri.MVC.Model.GModel.GamePlayer;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraProductionPowerLeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.util.*;

/**
 * Represents a player with all its information
 */
public class Player {

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
     * @param playerNumber - index to track the player order
     */
    public Player(String nickname, Integer playerNumber) {
        this.nickname = nickname;
        this.playerNumber = playerNumber;
        this.playerBoard = new Playerboard();
        this.playerLeaderCards = new LeaderCard[4];
    }


    /**
     * Asks and sets the player's nickname
     */
    public void chooseNickname() {
        Scanner in = new Scanner(System.in);

        System.out.println("Choose your nickname to join the game: ");
        nickname = in.nextLine();

        this.nickname = nickname;
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
    public void setStartingPlayerboard() {
        Map<Integer, Integer[]> startingResources =  new HashMap<>();
        int numChosenResources;
        int numInitialRedCross;
        int resourceNum;
        int resourceNumWarehouse;
        int i;
        Scanner in = new Scanner(System.in);

        startingResources.put(0, new Integer[] {0, 0});
        startingResources.put(1, new Integer[] {1, 0});
        startingResources.put(2, new Integer[] {1, 1});
        startingResources.put(3, new Integer[] {2, 1});

        for (Integer key : startingResources.keySet()){
            if(this.playerNumber.equals(key)) {
                numChosenResources = startingResources.get(key)[0];
                numInitialRedCross = startingResources.get(key)[1];
                while(numChosenResources > 0) {
                    resourceNum = -1;
                    while(resourceNum < 0 || resourceNum > 3) {
                        System.out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
                        resourceNum = in.nextInt();
                    }
                    i = 0;
                    for (String key2 : getPlayerBoard().getWareHouse().getWarehouseResources().keySet()) {
                        if(i == resourceNum) {
                            resourceNumWarehouse = getPlayerBoard().getWareHouse().getWarehouseResources().get(key2);
                            getPlayerBoard().getWareHouse().getWarehouseResources().put(key2, resourceNumWarehouse + 1);
                        }
                        i++;
                    }
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
    public int getAction() {
        int actionNumber = -1;
        Scanner in = new Scanner(System.in);

        while(actionNumber < 0 || actionNumber > 2) {
            System.out.println("What action do you want to do? Choose one of them:");
            System.out.println("Write 0 if you want to take resources from the market.");
            System.out.println("Write 1 if you want to buy a development card.");
            System.out.println("Write 2 if you want to activate the production.");
            actionNumber = in.nextInt();
        }

        return actionNumber;
    }

    /**
     * Draws marbles from the market
     * @param market - market from where to draw marbles
     * @param players - players playing the game
     */
    public void pickLineFromMarket(Market market, Player[] players) {
        Scanner in = new Scanner(System.in);
        int rowColumnChoice = -1;
        int columnNum = -1;
        int rowNum = -1;

        // Scelta colonna/riga.
        while(rowColumnChoice != 0 && rowColumnChoice != 1) {
            System.out.println("Do you want to choose a column or a row from the market? Write 0 for column, 1 for row:");
            System.out.println(market);
            rowColumnChoice = in.nextInt();
        }

        // Scelta numero di colonna/riga e mette risorse nel warehouse.
        if(rowColumnChoice == 0) {
            while(columnNum < 0 || columnNum > 3) {
                System.out.println("Choose the column's number you want to get the resources from:");
                System.out.println(market);
                columnNum = in.nextInt();
                market.updateColumn(columnNum, players, this.playerNumber);
            }
        }
        else {
            while(rowNum < 0 || rowNum > 2) {
                System.out.println("Choose the row's number you want to get the resources from:");
                System.out.println(market);
                rowNum = in.nextInt();
                market.updateRow(rowNum, players, this.playerNumber);
            }
        }

    }

    /**
     * Buys a development card
     * @param developmentCardsDecksGrid
     * @return true if the player buys a card successfully
     */
    public boolean buyDevelopmentCard(DevelopmentCardsDecksGrid developmentCardsDecksGrid) {

        Scanner consoleInput = new Scanner(System.in);

        System.out.println("Available development cards colours: " + developmentCardsDecksGrid.getDevelopmentCardsColours());
        System.out.println("Choose development card colour: ");
        String colour = consoleInput.nextLine();
        System.out.println("");
        while (!developmentCardsDecksGrid.getDevelopmentCardsColours().containsKey(colour)) {
            System.out.println("Card of this colour doesn't exist!");
            System.out.println("Choose a valid development card colour: ");
            colour = consoleInput.nextLine();
            System.out.println("");
        }
        System.out.println("Available development cards levels: " + developmentCardsDecksGrid.getDevelopmentCardsLevels());
        System.out.println("Choose development card level: ");
        int level = consoleInput.nextInt();
        System.out.println("");
        //check if the input card exists otherwise choose again
        while (!developmentCardsDecksGrid.getDevelopmentCardsLevels().contains(level)) {
            System.out.println("Card of this level doesn't exist!");
            System.out.println("Choose a valid development card level: ");
            level = consoleInput.nextInt();
            System.out.println("");
        }
        int column = developmentCardsDecksGrid.getDevelopmentCardsColours().get(colour);
        //Check if selected pile is empty
        while (developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0]==null) {
            System.out.println("Empty development cards pile!");
            System.out.println("Choose new pile");
            System.out.println("");
            System.out.println("Available development cards colours: " + developmentCardsDecksGrid.getDevelopmentCardsColours());
            System.out.println("Choose development card colour: ");
            colour = consoleInput.nextLine();
            System.out.println("");
            while (!developmentCardsDecksGrid.getDevelopmentCardsColours().containsKey(colour)) {
                System.out.println("Card of this colour doesn't exist!");
                System.out.println("Choose a valid development card colour: ");
                colour = consoleInput.nextLine();
                System.out.println("");
            }
            System.out.println("Available development cards levels: " + developmentCardsDecksGrid.getDevelopmentCardsLevels());
            System.out.println("Choose development card level: ");
            level = consoleInput.nextInt();
            System.out.println("");
            //check if the input card exists otherwise choose again
            while (!developmentCardsDecksGrid.getDevelopmentCardsLevels().contains(level)) {
                System.out.println("Card of this level doesn't exist!");
                System.out.println("Choose a valid development card level: ");
                level = consoleInput.nextInt();
                System.out.println("");
            }
            column = developmentCardsDecksGrid.getDevelopmentCardsColours().get(colour);
        }

        //Asks player if he wants to activate discount perks
        Map<String, Integer> developmentCardCost = developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost();
        Integer resourceCost;
        int input=-1;
        Scanner discountChoice = new Scanner(System.in);
        //Checks the discounts that the player has available
        for(String res : this.playerBoard.getDevelopmentCardDiscount())
        {
            resourceCost=developmentCardCost.get(res);
            if(resourceCost!=0)
            {
                while(input<0||input>1)
                {
                    System.out.println("Do you want to pay the reduced price for this resource? Insert 0 for no, 1 for yes");
                    input=discountChoice.nextInt();
                }
                if(input==1)
                    developmentCardCost.put(res, resourceCost-1);
            }
        }

        //Checks if the player has enough resources to buy the new development card
        if (developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].checkResourcesAvailability(playerBoard, developmentCardCost)) {
            //Checks if the player can place the new development card on his board
            if (developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].checkPlayerboardDevelopmentCardsCompatibility(playerBoard)) {
                //Removes the resources from the player who bought the development card
                //according to the development card cost
                developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].payDevelopmentCard(this.playerBoard);
                //Ask the player where to place the new development card on his board
                playerBoard.placeNewDevelopmentCard(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0]);
                //Updates the player victory points by adding to them the
                //victory points obtained from the new development card
                playerBoard.sumVictoryPoints(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].getVictoryPoints());
                //Removes the development card from the grid by removing it
                //from the deck where it was bought
                List<DevelopmentCard> reducedDeck = new ArrayList<>(Arrays.asList(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column]));
                reducedDeck.remove(0);
                developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column] = reducedDeck.toArray(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column]);
                return true;
            } else if (!developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].checkPlayerboardDevelopmentCardsCompatibility(playerBoard)) {
                return false;
            }
        } else if (!developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].checkResourcesAvailability(playerBoard, developmentCardCost)) {
            return false;
        }
        //Returns false if the player wasn't able to buy the development card
        return false;
        //If buying the card isn't a possible action or is denied and player has to choose new action and start all over
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /**
     * Activates the production powers on the player's player board
     * @param devCard - development cards on the player board
     */
    public boolean activateProduction(DevelopmentCard devCard, ExtraProductionPowerLeaderCard extraprod) {
        int i, j;
        int numResource;
        int activateProduction = -1;
        int redCross = 0;
        Scanner in = new Scanner(System.in);
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

        for(j = 0; j < 3; j++) {
            for (i = 0; getPlayerBoard().getPlayerboardDevelopmentCards()[i][j] != null; i++) ;
            while(activateProduction != 0 && activateProduction != 1) {
                System.out.println("This is the resources you have to pay: " + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput());
                System.out.println("For this:" + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput());
                System.out.println("Do you want to activate this production power?: Write 1 if you want or 0 if you don't:");
                activateProduction = in.nextInt();
            }
            if(activateProduction == 1) {
                for(String key: getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput().keySet()) {
                    numResource = inputResources.get(key);
                    inputResources.put(key, numResource + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput().get(key));
                }
                for(String key: getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().keySet()) {
                    numResource = outputResources.get(key);
                    outputResources.put(key, numResource + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().get(key));
                }
                // Aggiunta alla conta totale i redCross
                redCross = redCross + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getFaithPoints();
            }
        }

        // Chiede se vuole attivare il potere di produzione base.
        activateProduction = -1;
        while(activateProduction != 0 && activateProduction != 1) {
            System.out.println("Do you want to activate also the basic production power?: Write 1 if you want or 0 if you don't:");
            activateProduction = in.nextInt();
        }
        if(activateProduction == 1) {
            i = 0;
            //Controllo checkResourcesAvailability
            for (String s : getPlayerBoard().activateBasicProductionPower()) {
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

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Richiesta di attivazione potere di produzione extra.
        activateProduction = -1;
        while(activateProduction != 0 && activateProduction != 1) {
            System.out.println("Do you want to activate the extra production power?: Write 1 if you want or 0 if you don't:");
            activateProduction = in.nextInt();
        }
        if(activateProduction == 1)
            extraprod.activateAbility(this.playerBoard);


        // Controllo risorse nel chest/warehouse, e se true toglie dal chest/warehouse.
        if(devCard.checkResourcesAvailability(this.playerBoard, inputResources)) {
            for(String key: inputResources.keySet()) {
                numResource = inputResources.get(key);
                for(i = 0; i < numResource; i++)
                    this.playerBoard.pickResource(key);
            }
        }
        else {
            System.out.println("Not enough resources to activate production!");
            return false;
        }

        //Adds output resources to the chest
        for(String key: outputResources.keySet()) {
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
    public boolean getLeaderAction() {
        Scanner in = new Scanner(System.in);
        int leaderActionNum = -1;

        while(leaderActionNum != 0 && leaderActionNum != 1) {
            System.out.println("Do you want to do a leader action?: Write 1 if you want or 0 if you don't:");
            leaderActionNum = in.nextInt();
        }

        if (leaderActionNum==1) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Plays a leader card
     */
    public void playLeaderCard() {
        Scanner in = new Scanner(System.in);
        int numLeaderCard=-1;

        //Scelta della carta leader da giocare
        while((numLeaderCard<0) || (numLeaderCard > this.playerLeaderCards.length)){
            System.out.println("What leader card do you want to play?:");
            for (int i = 0; i < this.playerLeaderCards.length; i++) {
                System.out.println("Write" + i + "for this:" + this.playerLeaderCards[i]);
            }
            numLeaderCard = in.nextInt();
            if((this.playerLeaderCards[numLeaderCard].checkRequisites(this.playerBoard))&&
                    (!this.playerLeaderCards[numLeaderCard].isPlayed()))
                this.playerLeaderCards[numLeaderCard].activateAbility(this.playerBoard);
                this.playerBoard.sumVictoryPoints(this.playerLeaderCards[numLeaderCard].getVictoryPoints());
        }
    }

    /**
     * Discards a leader card
     */
    public void discardLeaderCard() {
        Scanner in = new Scanner(System.in);
        int numLeaderCard = -1;

        int k=0;
        while(this.playerLeaderCards[k]!=null)
            k++;


        if(k>0){
            if(k==1)
                numLeaderCard=0;
            else
            {
                //Scelta della carta leader da scartare
                while(numLeaderCard < 0 || numLeaderCard > k) {
                    System.out.println("What leader card do you want to discard?:");
                    for (int i = 0; i < k; i++) {
                        System.out.println("Write " + i + " for this: " + this.playerLeaderCards[i]);
                    }
                    numLeaderCard = in.nextInt();
                }
            }

            //Rimozione carta leader dal deck
            this.playerLeaderCards[numLeaderCard].discard(this.playerBoard);
            List<LeaderCard> updatedPlayerLeaderCardList = new ArrayList<>(Arrays.asList(this.playerLeaderCards));
            updatedPlayerLeaderCardList.remove(numLeaderCard);
            this.playerLeaderCards = updatedPlayerLeaderCardList.toArray(this.playerLeaderCards);
        }


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
        victoryPoints = victoryPoints + numResources / 5;

        return victoryPoints;
    }

    /**
     * Returns all the player resources count
     * @return all the player resources count
     */
    public int numResourcesReserve() {
        int numResources = 0;

        for(String key: getPlayerBoard().getWareHouse().getWarehouseResources().keySet()) {
            numResources = numResources + getPlayerBoard().getWareHouse().getWarehouseResources().get(key);
            if(getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + key) != null)
                numResources = numResources + getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + key);
        }
        for(String key: getPlayerBoard().getChest().getChestResources().keySet())
            numResources = numResources + getPlayerBoard().getChest().getChestResources().get(key);

        return numResources;
    }
}
