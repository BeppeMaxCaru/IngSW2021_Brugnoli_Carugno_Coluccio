package Maestri.MVC.Model.GModel.GamePlayer;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Represents a player with all its information
 */
public class Player
        //implements Runnable
{

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

    private BufferedReader in;
    private Scanner inScan;
    private PrintWriter out;

    /**
     * Initializes a new player
     * @param clientSocket
     *
     */
    public Player(Socket clientSocket) {
        //this.playerNumber = playerNumber;
        this.playerBoard = new Playerboard();

        if(this.playerNumber > 1)
            this.playerBoard.getFaithPath().moveCross(1);

        this.playerLeaderCards = new LeaderCard[4];

        this.clientSocket = clientSocket;
        try {
            //this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            //Locking player from inserting input
            this.inScan = new Scanner(new InputStreamReader(this.clientSocket.getInputStream()));
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);

            out.println("Insert your nickname: ");
            //this.nickname = in.readLine();
            //Scanner turnScan = new Scanner(new InputStreamReader(this.clientSocket.getInputStream()));
            this.nickname = this.inScan.nextLine();
            out.println("Hi " + this.nickname + ", welcome to Master of Renaissance online! Waiting for the match starting...");

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

    public BufferedReader getInBufferedReader() {
        return this.in;
    }

    public Scanner getInScannerReader() {
        return this.inScan;
    }

    public PrintWriter getOutPrintWriter() {
        return this.out;
    }

    /**
     * Sets to the player its starting board depending on its number
     */
    public void setStartingPlayerboard(String resource) {

        int resourceNumWarehouse;

        resourceNumWarehouse = getPlayerBoard().getWareHouse().getWarehouseResources().get(resource);
        getPlayerBoard().getWareHouse().getWarehouseResources().put(resource, resourceNumWarehouse + 1);

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
     * Buys a development card
     * @param developmentCardsDecksGrid - developments cards available to buy
     * @return true if the player buys a card successfully
     */
    public boolean buyDevelopmentCard(DevelopmentCardsDecksGrid developmentCardsDecksGrid, int column, int level, int position, String[] wclChoice) {

        //Removes the resources from the player who bought the development card
        //according to the development card cost
        developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].payDevelopmentCard(this.playerBoard, wclChoice, out);
        //Ask the player where to place the new development card on his board
        this.playerBoard.placeNewDevelopmentCard(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0], position, out);
        //Updates the player victory points by adding to them the
        //victory points obtained from the new development card
        this.playerBoard.sumVictoryPoints(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].getVictoryPoints());
        //Removes the development card from the grid by removing it
        //from the deck where it was bought
        List<DevelopmentCard> reducedDeck = new ArrayList<>(Arrays.asList(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column]));
        reducedDeck.remove(0);
        developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column] = reducedDeck.toArray(developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column]);
        return true;
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /**
     * Activates the production powers on the player's player board
     */
    public boolean activateProduction(int[] activateCards, String[] fromWhereCards, List<String> inputs, List<String> outputs) {
        int i, j;
        int numResource;
        int redCross = 0;
        int numActivateProductionExtra = 0;
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
        outputResources.put("REDCROSS", 0);

        for(i = 0; !activateCards[i].equals(" "); i++) {
            switch (activateCards[i]) {
                case "p0":
                case "p1":
                case "p2":
                    if (activateCards[i].equals("p0")) j = 0;
                    else if (activateCards[i].equals("p1")) j = 1;
                    else j = 2;
                    for (i = 0; getPlayerBoard().getPlayerboardDevelopmentCards()[i][j] != null; i++) ;
                    i--;
                    out.println("This is the resources you have to pay: " + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput());
                    out.println("For this: " + getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput());
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
                    break;
                case "b":
                    for (String str : inputs) {
                        numResource = inputResources.get(str);
                        inputResources.put(str, numResource + 1);
                    }
                    numActivateProductionExtra++;
                    break;
                case "e0":
                case "e1":
                    if (activateCards[i].equals("e0")) j = 0;
                    else j = 1;
                    if (this.playerBoard.getExtraProductionPowerInput()[j] != null) {
                        out.println("You activate the extra production power that cost a " + this.playerBoard.getExtraProductionPowerInput()[j]);
                        numResource = inputResources.get(this.playerBoard.getExtraProductionPowerInput()[j]);
                        inputResources.put(this.playerBoard.getExtraProductionPowerInput()[j], numResource + 1);
                        numActivateProductionExtra++;
                    }
                    else return false;
                    break;
            }
        }

        // Controllo risorse nel chest/warehouse, e se true toglie dal chest/warehouse.
        if(this.getPlayerBoard().checkResourcesAvailability(inputResources, out)) {
            for(String key: inputResources.keySet()) {
                numResource = inputResources.get(key);
                for(j = 0; j < numResource; j++)
                    this.playerBoard.pickResource(key, fromWhereCards, out);
            }
        }
        else {
            out.println("Not enough resources to activate production!");
            return false;
        }

        // Scelta delle n risorse di extra production power e inserimento in outputResources
        while(numActivateProductionExtra > 0) {
            for (String str : outputs) {
                numResource = outputResources.get(str);
                outputResources.put(str, numResource + 1);
            }
            numActivateProductionExtra--;
        }

        // Adds output resources to the chest
        for(String key : outputResources.keySet()) {
            numResource = getPlayerBoard().getChest().getChestResources().get(key);
            if(key.equals("REDCROSS") && numResource != 0) redCross = redCross + numResource;
            else getPlayerBoard().getChest().getChestResources().put(key, numResource + outputResources.get(key));
        }

        // RedCross
        getPlayerBoard().getFaithPath().moveCross(redCross);

        return true;
    }

    /**
     * Asks the player to perform a leader action
     */
    /*
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

        while (!correctLeaderAction && attempts < 2) {
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

     */

    /**
     * Plays a leader card
     */
    public boolean playLeaderCard(int var) {

        this.playerLeaderCards[var].activateAbility(this.playerBoard);
        this.playerBoard.sumVictoryPoints(this.playerLeaderCards[var].getVictoryPoints());

        return true;
    }

    /**
     * Discards a leader card
     */
    public boolean discardLeaderCard(int var) {

        //Rimozione carta leader dal deck
        //this.playerLeaderCards[numLeaderCard].discard(this.playerBoard);
        List<LeaderCard> updatedPlayerLeaderCardList = new ArrayList<>(Arrays.asList(this.playerLeaderCards));
        updatedPlayerLeaderCardList.remove(var);
        this.playerLeaderCards = updatedPlayerLeaderCardList.toArray(this.playerLeaderCards);

        return true;
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

    public void printAll(PrintWriter out){
        out.println("YOUR RESOURCES:");
        out.println("COINS   : "+this.getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS")+" in warehouse, "+
                +this.getPlayerBoard().getChest().getChestResources().get("COINS")+" in chest");
        out.println("SERVANTS: "+this.getPlayerBoard().getWareHouse().getWarehouseResources().get("SERVANTS")+" in warehouse, "+
                +this.getPlayerBoard().getChest().getChestResources().get("SERVANTS")+" in chest");
        out.println("SHIELDS : "+this.getPlayerBoard().getWareHouse().getWarehouseResources().get("SHIELDS")+" in warehouse, "+
                +this.getPlayerBoard().getChest().getChestResources().get("SHIELDS")+" in chest");
        out.println("STONES  : "+this.getPlayerBoard().getWareHouse().getWarehouseResources().get("STONES")+" in warehouse, "+
                +this.getPlayerBoard().getChest().getChestResources().get("STONES")+" in chest");
        out.println();

        out.println("YOUR DEVELOPMENT CARDS: ");
        this.printPlayerCards(out);
        out.println();
        out.println("YOUR FAITH PATH POSITION    : "+this.getPlayerBoard().getFaithPath().getCrossPosition());

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

    /*@Override
    public void run() {
        //Lock turnLock = new ReentrantLock();
        //turnLock.lock();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);

            //out.println("Write QUIT to leave the game anytime you want");
            while (true) {
                //out.println("Inserisci un comando: ");
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
        //turnLock.unlock();
    }*/

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
