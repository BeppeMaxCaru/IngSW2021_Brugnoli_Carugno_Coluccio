package Maestri.MVC.Model.GModel.GamePlayer;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.util.*;

public class Player {
    private String nickname;
    private Integer playerNumber;
    private Playerboard playerboard;
    private LeaderCard[] playerLeaderCards;

    public Player(String nickname, Integer playerNumber, Playerboard playerboard) {
        this.nickname = nickname;
        this.playerNumber = playerNumber;
        this.playerboard = playerboard;
        this.playerLeaderCards = new LeaderCard[4];
    }

    /** This method asks the player's nickname. */

    public String getNickname() {
        Scanner in = new Scanner(System.in);

        System.out.println("Player nickname:");
        nickname = in.nextLine();

        return this.nickname;
    }

    public Integer getPlayerNumber() {
        return this.playerNumber;
    }

    public void setPlayerNumber(Integer i) {
        playerNumber = i;
    }

    public Playerboard getPlayerboard() {
        return this.playerboard;
    }

    public LeaderCard[] getPlayerLeaderCards() {
        return this.playerLeaderCards;
    }

    public void setPlayerLeaderCards(int index, LeaderCard leaderCard) {
        this.playerLeaderCards[index] = leaderCard;
    }

    /** This method sets the player's initial resources due to his playerNumber. */

    public void setStartingPlayerBoard(Integer playerNumber) {
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
            if(playerNumber.equals(key)) {
                numChosenResources = startingResources.get(key)[0];
                numInitialRedCross = startingResources.get(key)[1];
                while(numChosenResources > 0) {
                    resourceNum = -1;
                    while(resourceNum < 0 || resourceNum > 3) {
                        System.out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
                        resourceNum = in.nextInt();
                    }
                    i = 0;
                    for (String key2 : getPlayerboard().getWareHouse().getWarehouseResources().keySet()) {
                        if(i == resourceNum) {
                            resourceNumWarehouse = getPlayerboard().getWareHouse().getWarehouseResources().get(key2);
                            getPlayerboard().getWareHouse().getWarehouseResources().put(key2, resourceNumWarehouse + 1);
                        }
                        i++;
                    }
                    numChosenResources--;
                }
                if(numInitialRedCross == 1) {
                    getPlayerboard().getFaithPath().moveCross(1);
                }
                break;
            }
        }
    }

    /** This method asks the player what action he wants to do. */

    public int getAction( ) {
        int actionNum = -1;
        Scanner in = new Scanner(System.in);

        while(actionNum < 0 || actionNum > 2) {
            System.out.println("What action do you want to do? Choose one of them:");
            System.out.println("Write 0 if you want to take resources from the market.");
            System.out.println("Write 1 if you want to buy a development card.");
            System.out.println("Write 2 if you want to activate the production.");
            actionNum = in.nextInt();
        }

        return actionNum;
    }

    /** This method allows the player to pick a line from the market, after choosing the first action. */

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
                market.updateColumn(columnNum, players, playerNumber);
            }
        }
        else {
            while(rowNum < 0 || rowNum > 2) {
                System.out.println("Choose the row's number you want to get the resources from:");
                System.out.println(market);
                rowNum = in.nextInt();
                market.updateRow(rowNum, players, playerNumber);
            }
        }

    }

    /** This method asks the player what development cards he wants to buy. */

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
        while (developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column].length==0) {
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

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Mega ciclo while di controllo va qua dentro
        Map<String, Integer> developmentCardCost = developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost();

        if (developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].checkResourcesAvailability(playerboard, developmentCardCost)) {
            if (developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].checkPlayerboardDevelopmentCardsCompatibility(playerboard)) {
                developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].payDevelopmentCard(this.playerboard);

                playerboard.placeNewDevelopmentCard(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0]);
                playerboard.sumVictoryPoints(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].getVictoryPoints());
                //aggiungere remove per togliere la carta
                List<DevelopmentCard> reducedDeck = Arrays.asList(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column]);
                reducedDeck.remove(0);
                developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column] = reducedDeck.toArray(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column]);
                return true;
            } else if (!developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].checkPlayerboardDevelopmentCardsCompatibility(playerboard)) {
                return false;
            }
        } else if (!developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].checkResourcesAvailability(playerboard, developmentCardCost)) {
            return false;
        }
        return false;
        //If buy isn't possible action is denied and player has to choose new action and start all over
    }

    public void activateProduction(DevelopmentCard devCard) {
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
        // Red Cross???
        Map<String, Integer> outputResources = new HashMap<>();
        outputResources.put("COINS", 0);
        outputResources.put("SHIELDS", 0);
        outputResources.put("SERVANTS", 0);
        outputResources.put("STONES", 0);

        for(j = 0; j < 3; j++) {
            for (i = 0; getPlayerboard().getPlayerboardDevelopmentCards()[i][j] != null; i++) ;
            while(activateProduction != 0 && activateProduction != 1) {
                System.out.println("This is the resources you have to pay: " + getPlayerboard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput());
                System.out.println("For this:" + getPlayerboard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput());
                System.out.println("Do you want to activate this production power?: Write 1 if you want or 0 if you don't:");
                activateProduction = in.nextInt();
            }
            if(activateProduction == 1) {
                for(String key: getPlayerboard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput().keySet()) {
                    numResource = inputResources.get(key);
                    inputResources.put(key, numResource + getPlayerboard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput().get(key));
                }
                for(String key: getPlayerboard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().keySet()) {
                    numResource = outputResources.get(key);
                    outputResources.put(key, numResource + getPlayerboard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().get(key));
                }
                // Aggiunta alla conta totale i redCross.
                redCross = redCross + getPlayerboard().getPlayerboardDevelopmentCards()[i][j].getFaithPoints();
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
            for(String s : getPlayerboard().activateBasicProductionPower()) {
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

        // Controllo risorse nel chest/warehouse, e se true toglie dal chest/warehouse.
        if(devCard.checkResourcesAvailability(playerboard, inputResources)) {
            for(String key: inputResources.keySet()) {
                numResource = inputResources.get(key);
                for(i = 0; i < numResource; i++)
                    playerboard.pickResource(key);
            }
        }

        // Aggiungi risorse al chest.
        for(String key: outputResources.keySet()) {
            numResource = getPlayerboard().getChest().getChestResources().get(key);
            getPlayerboard().getChest().getChestResources().put(key, numResource + outputResources.get(key));
        }

        //RedCross
        getPlayerboard().getFaithPath().moveCross(redCross);
    }

    /** This method asks the player if he wants to do a leader action. */

    public int getLeaderAction( ) {
        Scanner in = new Scanner(System.in);
        int leaderActionNum = -1;

        while(leaderActionNum != 0 && leaderActionNum != 1) {
            System.out.println("Do you want to do a leader action?: Write 1 if you want or 0 if you don't:");
            leaderActionNum = in.nextInt();
        }

        return leaderActionNum;
    }

    public void playLeaderCard(Market market) {
        Scanner in = new Scanner(System.in);
        int numLeaderCard=-1;

        //Scelta della carta leader da giocare
        while((numLeaderCard<0) || (numLeaderCard > this.playerLeaderCards.length)){
            System.out.println("What leader card do you want to play?:");
            for (int i = 0; i < this.playerLeaderCards.length; i++) {
                System.out.println("Write" + i + "for this:" + this.playerLeaderCards[i]);
            }
            numLeaderCard = in.nextInt();
            if(this.playerLeaderCards[numLeaderCard].checkRequisites(this.playerboard))
                this.playerLeaderCards[numLeaderCard].activateAbility(this);
                this.playerboard.sumVictoryPoints(this.playerLeaderCards[numLeaderCard].getVictoryPoints());
        }
    }

    /** This method allows the player to discard a leader card. */

    public void discardLeaderCard() {
        Scanner in = new Scanner(System.in);
        int numLeaderCard = -1;

        //Scelta della carta leader da scartare
        while(numLeaderCard < 0 || numLeaderCard > this.playerLeaderCards.length) {
            System.out.println("What leader card do you want to discard?:");
            for (int i = 0; i < this.playerLeaderCards.length; i++) {
                System.out.println("Write" + i + "for this:" + this.playerLeaderCards[i]);
            }
            numLeaderCard = in.nextInt();
        }

        //Rimozione carta leader dal deck
        this.playerLeaderCards[numLeaderCard].discard(this.playerboard);
        List<LeaderCard> updatedPlayerLeaderCardList = Arrays.asList(this.playerLeaderCards);
        updatedPlayerLeaderCardList.remove(numLeaderCard);
        this.playerLeaderCards = updatedPlayerLeaderCardList.toArray(this.playerLeaderCards);
    }

    public int sumAllVictoryPoints() {
        int victoryPoints = 0;
        int numResources = 0;

        // Punti delle tessere del tracciato fede, carte sviluppo e carte leader.
        victoryPoints = victoryPoints + getPlayerboard().getVictoryPoints();

        // Punti dalla posizione della croce.
        victoryPoints = victoryPoints + getPlayerboard().getFaithPath().getVictoryPoints();

        //Punti dalle risorse rimaste nel warehouse, chest, extraWarehouse.
        numResources = numResources + numResourcesReserve();
        victoryPoints = victoryPoints + numResources / 5;

        return victoryPoints;
    }

    public int numResourcesReserve() {
        int numResources = 0;

        for(String key: getPlayerboard().getWareHouse().getWarehouseResources().keySet())
            numResources = numResources + getPlayerboard().getWareHouse().getWarehouseResources().get(key);
        for(String key: getPlayerboard().getChest().getChestResources().keySet())
            numResources = numResources + getPlayerboard().getChest().getChestResources().get(key);
        // ExtraWarehouse ??

        return numResources;
    }
}
