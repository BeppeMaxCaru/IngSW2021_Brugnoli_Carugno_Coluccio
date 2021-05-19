package Maestri.MVC;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.PrintWriter;
import java.util.*;

public class GameController implements Runnable {

    private final GameModel gameModel;

    public GameController(List<Player> clientsWaiting) {
        this.gameModel = new GameModel(clientsWaiting);

    }

    @Override
    public void run() {
        System.out.println("New game started");

        for (int i = 0; i < this.gameModel.getPlayers().length; i++) {
            if (this.gameModel.getPlayers()[i] != null) {
                try {
                    Player currentPlayer = this.gameModel.getPlayers()[i];

                    Scanner in = currentPlayer.getInScannerReader();
                    PrintWriter out = currentPlayer.getOutPrintWriter();

                    out.println("It's your first turn");

                    out.println();

                    //Set starting PlayerBoard
                    Map<Integer, Integer> startingResources = new HashMap<>();
                    startingResources.put(0, 0);
                    startingResources.put(1, 1);
                    startingResources.put(2, 1);
                    startingResources.put(3, 2);

                    int numChosenResources = startingResources.get(i);
                    while (numChosenResources > 0) {
                        out.println("Choose your initial resources");
                        String resource = in.nextLine();
                        while (!resource.equals("COINS") && !resource.equals("SHIELDS") && !resource.equals("SERVANTS") && !resource.equals("STONES")) {
                            out.println("Not existing resource");
                            resource = in.nextLine();
                        }
                        currentPlayer.setStartingPlayerboard(resource);
                        numChosenResources--;
                    }

                    out.println("Waiting for other players setup");

                } catch (Exception e) {
                    //Sets current player to disconnected
                    this.gameModel.setPlayer(null, i);
                }
            }
        }

        //Now starts receiving command
        //Stops automatic setup like in first turn
        while (!this.gameModel.checkEndPlay()) {

            for (int i = 0; i < this.gameModel.getPlayers().length; i++) {
                if (this.gameModel.getPlayers()[i] != null) {
                    try {
                        Player currentPlayer = this.gameModel.getPlayers()[i];

                        Scanner in = currentPlayer.getInScannerReader();
                        PrintWriter out = currentPlayer.getOutPrintWriter();

                        out.println("It's your turn");

                        //Scanner in = new Scanner(new InputStreamReader(this.players[i].getClientSocket().getInputStream()));
                        //PrintWriter out = new PrintWriter(this.players[i].getClientSocket().getOutputStream(), true);


                        this.gameModel.getPlayers()[i].printAll(this.gameModel.getPlayers()[i].getOutPrintWriter());
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("MARKET GRID:");
                        this.gameModel.getMarket().printMarket(this.gameModel.getPlayers()[i].getOutPrintWriter());
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("DEVELOPMENT CARDS GRID:");
                        this.gameModel.getDevelopmentCardsDecksGrid().printGrid(this.gameModel.getPlayers()[i].getOutPrintWriter());

                        String action;
                        int corrAction;
                        do{
                            //FIrst receives the action
                            action = in.nextLine();
                            corrAction = 0;

                            switch (action.toUpperCase()) {
                                case "PLAY LEADER CARD": {
                                    //If the action received is this keeps saving parameter until it has all of them
                                    //Otherwise if it receives null before all the parameters are received doesn't call the method but reset the state to waiting action

                                    //First and only parameter is always an int that is the position of the leader card (see client main action flow)
                                    //The fact that's an int is already checked in client
                                    int position = Integer.parseInt(in.nextLine());
                                    //Si può controllare qui che int di position sia valido siccome è qui che si trova il
                                    //gamemodel ed il controllo è più affidabile

                                    //You can call the method with right parameters and update the view
                                    this.checkPlayCards(this.gameModel.getPlayers()[i], position);
                                    break;
                                }
                                case "DISCARD LEADER CARD": {
                                    //See PLAY LEADER CARD

                                    int position = Integer.parseInt(in.nextLine());

                                    this.checkDiscardCards(this.gameModel.getPlayers()[i], position);
                                    break;
                                }
                                case "PICK RESOURCES FROM MARKET": {

                                    //Row/column choice
                                    //First parameter is always either row or column (chek in client)
                                    String rowOrColumnChoice = in.nextLine();

                                    //Row/column index
                                    //Second parameter is always an int
                                    int index = Integer.parseInt(in.nextLine());

                                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                    //Warehouse/leaderCard choice
                                    //Qui bisogna far continuare a scegliere al player per ogni biglia + effetto speciale se disponibile
                                    String wlChoice = in.nextLine();

                                    //If he has 2 whiteMarbleLeaderCards
                                    String chosenMarble;
                                    chosenMarble=in.nextLine();

                                    if (this.checkMarketAction(this.gameModel.getPlayers()[i], rowOrColumnChoice, index, wlChoice, chosenMarble))
                                        corrAction++;
                                    break;
                                }
                                case "BUY DEVELOPMENT CARD": {

                                    //First parameter received
                                    //DevCard colour
                                    String colour = in.nextLine();

                                    //Second parameter received
                                    //DevCard level
                                    int level = in.nextInt();

                                    int[] quantity = new int[4];
                                    String[] deposit = new String[4];
                                    for (int k = 0; k < 4; k++)
                                    {
                                        quantity[k] = in.nextInt();
                                        deposit[k] = in.nextLine();
                                    }

                                    int position = in.nextInt();

                                    if(this.checkBuyDevCard(this.gameModel.getPlayers()[i], colour, 3 - level, position, quantity, deposit))
                                        corrAction++;
                                    break;
                                }
                                case "ACTIVATE PRODUCTION POWER": {

                                    int[] activation = new int[6];
                                    String[] whichInput = new String[2];
                                    int[] whichOutput = new int[3];

                                    for(int k = 0; k < 6; k++) {
                                        activation[k] = in.nextInt();
                                        if (activation[k] == 1) {
                                            whichInput[k] = in.nextLine();
                                            if(k >= 3) whichOutput[k - 3] = in.nextInt();
                                        }
                                    }

                                    if(this.checkActivateProduction(currentPlayer, activation, whichInput, whichOutput))
                                        corrAction++;
                                    break;
                                }
                                default: {
                                    out.println("Not valid action!");
                                    break;
                                }
                            }
                            //Player inserisce quit
                        }while (!action.equalsIgnoreCase("END TURN") && (corrAction < 1));

                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("Your turn has ended. Wait for other players...");
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println();

                    } catch (Exception e) {
                        //Player disconesso
                        //System.err.println(e.getMessage());
                        this.gameModel.getPlayers()[i] = null;

                    }
                }
            }

        }

    }


    /*public void run() {

        System.out.println("This is the new game");

        for (int i = 0; i < this.gameModel.getPlayers().length; i++) {
            //turnLock.lock();
            if (this.gameModel.getPlayers()[i] != null) {
                try {
                    Player currentPlayer = this.gameModel.getPlayers()[i];

                    Scanner in = currentPlayer.getInScannerReader();
                    PrintWriter out = currentPlayer.getOutPrintWriter();

                    out.println("It is your first turn");
                    out.println();

                    //Set starting PlayerBoard
                    Map<Integer, Integer> startingResources = new HashMap<>();
                    startingResources.put(0, 0);
                    startingResources.put(1, 1);
                    startingResources.put(2, 1);
                    startingResources.put(3, 2);

                    int numChosenResources = startingResources.get(i);
                    while (numChosenResources > 0) {
                        String resource = in.nextLine();
                        while (!resource.equals("COINS") && !resource.equals("SHIELDS") && !resource.equals("SERVANTS") && !resource.equals("STONES")) {
                            out.println("Not valid command.");
                            resource = in.nextLine();
                        }
                        currentPlayer.setStartingPlayerboard(resource);
                        numChosenResources--;
                    }

                    for (int cards = 0; cards < 2; cards++) {
                        boolean corrAction;
                        do {
                            String action = in.nextLine();
                            corrAction = this.checkDiscardCards(this.gameModel.getPlayers()[i], action);
                        } while (corrAction);
                    }

                    currentPlayer.getOutPrintWriter().println("Your turn has ended. Wait for other players...");
                    currentPlayer.getOutPrintWriter().println();


                } catch (Exception e) {
                    this.gameModel.getPlayers()[i] = null;
                    System.err.println(e.getMessage());
                }
            }
        }


        while (!this.gameModel.checkEndPlay()) {


            for (int i = 0; i < this.gameModel.getPlayers().length; i++) {
                if (this.gameModel.getPlayers()[i] != null) {
                    try {
                        Player currentPlayer = this.gameModel.getPlayers()[i];

                        Scanner in = currentPlayer.getInScannerReader();
                        PrintWriter out = currentPlayer.getOutPrintWriter();

                        out.println("It's your turn again");

                        //Scanner in = new Scanner(new InputStreamReader(this.players[i].getClientSocket().getInputStream()));
                        //PrintWriter out = new PrintWriter(this.players[i].getClientSocket().getOutputStream(), true);


                        this.gameModel.getPlayers()[i].printAll(this.gameModel.getPlayers()[i].getOutPrintWriter());
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("MARKET GRID:");
                        this.gameModel.getMarket().printMarket(this.gameModel.getPlayers()[i].getOutPrintWriter());
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("DEVELOPMENT CARDS GRID:");
                        this.gameModel.getDevelopmentCardsDecksGrid().printGrid(this.gameModel.getPlayers()[i].getOutPrintWriter());

                        String action;
                        int corrAction;
                        do{
                            action = in.nextLine();
                            corrAction = 0;

                            switch (action.toUpperCase()) {
                                case "PLAY LEADER CARD": {
                                    String choice = in.nextLine();
                                    this.checkPlayCards(this.gameModel.getPlayers()[i], choice);
                                    break;
                                }
                                case "DISCARD LEADER CARD": {
                                    String choice = in.nextLine();
                                    this.checkDiscardCards(this.gameModel.getPlayers()[i], choice);
                                    break;
                                }
                                case "PICK RESOURCES FROM MARKET": {

                                    //Row/column choice
                                    String rcChoice = in.nextLine();

                                    //Row/column index
                                    String choice = in.nextLine();

                                    //Warehouse/leaderCard choice
                                    String wlChoice = in.nextLine();

                                    //If he has 2 whiteMarbleLeaderCards
                                    String chosenMarble="0";

                                    if(currentPlayer.getPlayerBoard().getResourceMarbles()[0]!=null)
                                        chosenMarble=in.nextLine();

                                    if (this.checkMarketAction(this.gameModel.getPlayers()[i], rcChoice, choice, wlChoice, chosenMarble))
                                        corrAction++;
                                    break;
                                }
                                case "BUY DEVELOPMENT CARD": {

                                    //DevCard colour
                                    String colour = in.nextLine();

                                    //DevCard level
                                    String level = in.nextLine();

                                    //PlayerBoard grid position
                                    String position = in.nextLine();

                                    //From which store do you want to take resources
                                    String wclChoice = in.nextLine();

                                    //If he can pay discounted price
                                    String discountChoice="00";

                                    if(this.checkBuyDevCard(this.gameModel.getPlayers()[i], colour, level, position, wclChoice, discountChoice))
                                        corrAction++;
                                    break;
                                }
                                case "ACTIVATE PRODUCTION POWER": {

                                    String[] activation = new String[6];
                                    String[] fromWhere = new String[6];
                                    String whichInput = null;
                                    String[] whichOutput = new String[3];

                                    for(int index=0; index<6; index++)
                                    {
                                        activation[index]=in.nextLine();
                                        fromWhere[index]=in.nextLine();
                                        if(index==3)
                                            whichInput=in.nextLine();
                                        if(index>2)
                                            whichOutput[index-3]=in.nextLine();
                                    }

                                    if(this.checkActivateProduction(currentPlayer, activation, fromWhere, whichInput, whichOutput))
                                        corrAction++;
                                    break;
                                }
                                default: {
                                    out.println("Not valid action!");
                                    break;
                                }
                            }
                            //Player inserisce quit
                        }while (!action.equalsIgnoreCase("END TURN") && (corrAction < 1));

                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("Your turn has ended. Wait for other players...");
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println();

                    } catch (Exception e) {
                        //Player disconesso
                        //System.err.println(e.getMessage());
                        this.gameModel.getPlayers()[i] = null;

                    }
                }
            }


            for (int i = 0; i < this.gameModel.getPlayers().length; i++) {
                if (this.gameModel.getPlayers()[i] != null) {
                    try {
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("Game over!");
                        //There is a winner
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println(this.gameModel.getPlayers()[this.gameModel.checkWinner()].getNickname() + " wins the game with " + this.gameModel.getPlayers()[this.gameModel.checkWinner()].sumAllVictoryPoints() + " victory points");
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("You obtained " + this.gameModel.getPlayers()[i].sumAllVictoryPoints() + " victory points");
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
    }*/

    public void checkPlayCards (Player currentPlayer, int c) {

        PrintWriter out = currentPlayer.getOutPrintWriter();

        if (currentPlayer.getPlayerLeaderCards()[c] != null && !currentPlayer.getPlayerLeaderCards()[c].isPlayed()) {
            out.println("Played");
            currentPlayer.getPlayerLeaderCards()[c].printLeaderCard(out);
            currentPlayer.playLeaderCard(c);
        } else
            out.println("Not valid action.");

    }

    public boolean checkDiscardCards (Player currentPlayer, int c){

        PrintWriter out = currentPlayer.getOutPrintWriter();

        if (currentPlayer.getPlayerLeaderCards()[c] != null && !currentPlayer.getPlayerLeaderCards()[c].isPlayed()) {
            out.println("Discarded");
            currentPlayer.getPlayerLeaderCards()[c].printLeaderCard(out);
            currentPlayer.discardLeaderCard(c);
            return true;
        } else
            out.println("Not valid command.");

        return false;
    }

    public boolean checkMarketAction (Player currentPlayer, String choice, int i, String wlChoice, String c)
    {
        PrintWriter out = currentPlayer.getOutPrintWriter();

        if(c.contains("1"))
            if (currentPlayer.getPlayerBoard().getResourceMarbles()[1]==null) {
                out.println("Not valid command.");
                return false;
            }

        if(c.contains("0"))
            if (currentPlayer.getPlayerBoard().getResourceMarbles()[0]==null) {
                out.println("Not valid command.");
                return false;
            }

        if(wlChoice.toUpperCase().contains("L"))
            for(String keys : currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().keySet())
                if (!keys.contains("extra")) {
                    out.println("Not valid command.");
                    return false;
                }

        //If player picks row
        if(choice.equalsIgnoreCase("R"))
        {
            if(c.length()<4) {
                StringBuilder cBuilder = new StringBuilder(c);
                for(int k = cBuilder.length(); k<4; k++)
                {
                    cBuilder.append("X");
                }
                c = cBuilder.toString();
            }
            return this.gameModel.getMarket().updateRow(i, this.gameModel.getPlayers(), currentPlayer.getPlayerNumber(), wlChoice, c);
        }
        else
        {
            if(c.length()<3) {
                StringBuilder cBuilder = new StringBuilder(c);
                for(int k = cBuilder.length(); k<3; k++)
                {
                    cBuilder.append("X");
                }
                c = cBuilder.toString();
            }
            return this.gameModel.getMarket().updateColumn(i, this.gameModel.getPlayers(), currentPlayer.getPlayerNumber(), wlChoice, c);
        }
    }

    public boolean checkBuyDevCard(Player currentPlayer, String colour, int l, int p, int[] quantity, String[] wclChoice) {

        PrintWriter out = currentPlayer.getOutPrintWriter();

        Map<String, Integer> paidResources = new HashMap<>();
        paidResources.put("COINS", quantity[0]);
        paidResources.put("SERVANTS", quantity[1]);
        paidResources.put("SHIELDS", quantity[2]);
        paidResources.put("STONES", quantity[3]);

        Map<Integer, String> resources = new HashMap<>();
        resources.put(0, "COINS");
        resources.put(1, "SERVANTS");
        resources.put(2, "SHIELDS");
        resources.put(3, "STONES");

        int column=this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsColours().get(colour.toUpperCase());

        if(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0]!=null) {

            //Control on quantity and possibly discounts
            //If paidResources hashMap isn't equals to cardCost hashMap
            if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost())) {
                //Check for discounts
                if (currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0] != null && currentPlayer.getPlayerLeaderCards()[0].isPlayed()) {
                    paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0]) + 1);
                    //Check if player has activated only first discount
                    if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost())) {
                        if (currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1] != null && currentPlayer.getPlayerLeaderCards()[1].isPlayed()) {
                            paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1]) + 1);
                            //Check if player has activated both first and second discounts
                            if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost())) {
                                //Check if player has activated only second discount
                                paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0]) - 1);
                                if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost())) {
                                    //If resourcePaid isn't equal to cardCost, player hasn't inserted correct resource for buy the card
                                    out.println("Not valid command.");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int k=0; k<4; k++)
        {
            int count=0;
            for (int z=0; z<wclChoice[k].length(); z++)
            {
                if(String.valueOf(wclChoice[k].charAt(z)).equalsIgnoreCase("w"))
                    count++;
            }
            if(currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get(resources.get(k)) != count)
            {
                out.println("Not valid command.");
                return false;
            }

            count=0;
            for (int z=0; z<wclChoice[k].length(); z++)
            {
                if(String.valueOf(wclChoice[k].charAt(z)).equalsIgnoreCase("c"))
                    count++;
            }
            if(currentPlayer.getPlayerBoard().getChest().getChestResources().get(resources.get(k)) != count)
            {
                out.println("Not valid command.");
                return false;
            }

            count=0;
            for (int z=0; z<wclChoice[k].length(); z++)
            {
                if(String.valueOf(wclChoice[k].charAt(z)).equalsIgnoreCase("l"))
                    count++;
            }
            if(currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra"+resources.get(k)) != count)
            {
                out.println("Not valid command.");
                return false;
            }

        }

        if(!currentPlayer.getPlayerBoard().isCardBelowCompatible(p, this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0]))
        {
            out.println("Not valid command.");
            return false;
        }

        //Chiamata al metodo del gamemodel, controlli effettuati
        return this.gameModel.buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, l, p, wclChoice);
    }


    private boolean checkActivateProduction(Player currentPlayer, int[] activation, String[] whichInput, int[] whichOutput) {

        PrintWriter out = currentPlayer.getOutPrintWriter();

        Map<String, Integer> paidWarehouseResources = new HashMap<>();
        paidWarehouseResources.put("COINS", 0);
        paidWarehouseResources.put("SERVANTS", 0);
        paidWarehouseResources.put("SHIELDS", 0);
        paidWarehouseResources.put("STONES", 0);

        Map<String, Integer> paidChestResources = new HashMap<>();
        paidChestResources.put("COINS", 0);
        paidChestResources.put("SERVANTS", 0);
        paidChestResources.put("SHIELDS", 0);
        paidChestResources.put("STONES", 0);

        Map<Integer, String> resources = new HashMap<>();
        resources.put(0, "COINS");
        resources.put(1, "SERVANTS");
        resources.put(2, "SHIELDS");
        resources.put(3, "STONES");


        for(int k=0; k<activation.length; k++)
        {
            if(activation[k]==1) {

                int j=2;
                if (k < 3) {

                    //Check if player has any cards into the indicated position
                    for (j = 2; j > 0; j--)
                        if (currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k] != null)
                            break;

                    if (currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k] == null) {
                        out.println("Not valid command.");
                        return false;
                    }

                    //Check how many resources player has to spend
                    int totalResources = 0;
                    for(String keys : currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().keySet())
                        totalResources=totalResources+currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().get(keys);
                    //Confront them with whichInput string: resourceCode - quantity - storage
                    //If player indicated less resources than that he had to pay, error
                    if(whichInput.length<totalResources*3)
                    {
                        out.println("Not valid command.");
                        return false;
                    }

                } else {
                    if(k!=3)
                    {
                        //Check if player has any cards into the indicated position and it is activated
                        if (currentPlayer.getPlayerBoard().getExtraProductionPowerInput()[k-4] == null || !currentPlayer.getPlayerLeaderCards()[k-4].isPlayed()) {
                            out.println("Not valid command.");
                            return false;
                        }
                    }
                }

                //Save all resources player has to pay in temporary maps
                for(int z=0; z<whichInput.length-2; z=z+3) {
                    switch (String.valueOf(whichInput[z+2]).toUpperCase())
                    {
                        case "W":
                        {
                            paidWarehouseResources.put(resources.get(Integer.parseInt(whichInput[z])), Integer.parseInt(whichInput[z+1]));
                            break;
                        }
                        case "C":
                        {
                            paidChestResources.put(resources.get(Integer.parseInt(whichInput[z])), Integer.parseInt(whichInput[z+1]));
                            break;
                        }
                        case "L":
                        {
                            if(currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra"+resources.get(Integer.parseInt(whichInput[z])))==null)
                            {
                                out.println("Not valid command.");
                                return false;
                            }
                            else
                                paidWarehouseResources.put("extra"+resources.get(Integer.parseInt(whichInput[z])), Integer.parseInt(whichInput[z+1]));
                            break;
                        }
                    }
                }

                //Check if player has each correct resource in each correct storage

                for(String keys : paidWarehouseResources.keySet())
                    if(currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get(keys)<paidWarehouseResources.get(keys))
                    {
                        out.println("Not valid command.");
                        return false;
                    }
                for(String keys : paidChestResources.keySet())
                    if(currentPlayer.getPlayerBoard().getChest().getChestResources().get(keys)<paidChestResources.get(keys))
                    {
                        out.println("Not valid command.");
                        return false;
                    }

                //Check if player inserted all necessary resources to activate the production

                for (String res : paidChestResources.keySet())
                {
                    paidChestResources.put(res, paidChestResources.get(res) + paidWarehouseResources.get(res));
                    for(String extraRes : paidWarehouseResources.keySet())
                    {
                        if(extraRes.contains(res))
                            paidChestResources.put(res, paidChestResources.get(res) + paidWarehouseResources.get("extra"+res));
                    }
                }

                if(k<3)
                {
                    for(String res : currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().keySet())
                    {
                        if (paidChestResources.get(res) < currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().get(res))
                        {
                            out.println("Not valid command");
                            return false;
                        }
                    }
                } else {
                        if (paidChestResources.get(currentPlayer.getPlayerBoard().getExtraProductionPowerInput()[j-4]) < 1)
                        {
                            out.println("Not valid command");
                            return false;
                        }
                }
            }
        }
        return currentPlayer.activateProduction(activation, whichInput, whichOutput);
    }

}
