package Maestri.MVC;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.InputStreamReader;
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

        for (int i=0;i < this.gameModel.getPlayers().length; i++) {
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
                                    this.checkPlayCards(this.gameModel.getPlayers()[i], choice);
                                    break;
                                }
                                case "DISCARD LEADER CARD": {
                                    //See PLAY LEADER CARD

                                    int position = Integer.parseInt(in.nextLine());

                                    this.checkDiscardCards(this.gameModel.getPlayers()[i], choice);
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
                                    String chosenMarble="0";
                                    if(currentPlayer.getPlayerBoard().getResourceMarbles()[0]!=null)
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
                                    int level = Integer.parseInt(in.nextLine());

                                    String parameter = null;
                                    while (!parameter.equalsIgnoreCase("stop") {

                                        //First is the resource
                                        parameter = in.nextLine();
                                        String resource = parameter;

                                        //Second is the deposit
                                        parameter = in.nextLine();
                                        String deposit = parameter;

                                        //Third is the quantity
                                        parameter = in.nextLine();
                                        int quantity = Integer.parseInt(parameter);

                                        //Serve mappa o oggetto in cui salvare tutte le risorse che gradualmente sceglie player
                                        //per poi controllare tramite metodo gamemodel che le abboa

                                    }



                                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                    //Finire qui con sconto NB reattivo non interattivo
                                    //PlayerBoard grid position
                                    //String position = in.nextLine();
                                    //From which store do you want to take resources
                                    //String wclChoice = in.nextLine();
                                    //If he can pay discounted price
                                    String discountChoice="00";
                                    if(this.checkBuyDevCard(this.gameModel.getPlayers()[i], colour, level, position, wclChoice, discountChoice))
                                        corrAction++;
                                    break;
                                }
                                /*case "ACTIVATE PRODUCTION POWER": {

                                    String[] activation = new String[6];
                                    String[] fromWhere = new String[6];
                                    String[] whichInput = new String[2];
                                    String[] whichOutput = new String[3];
                                    int j;

                                    activation[0] = "*"; // Comandi: p0, p1, p2, b, e0, e1.
                                    for(int index = 0; index < 6 && !activation[index].equals(" "); index++) {
                                        activation[index] = in.nextLine(); // Comandi: p0, p1, p2, b, e0, e1.
                                        if(activation[index].equals("p0") || activation[index].equals("p1") || activation[index].equals("p2")) {
                                            if(activation[index].equals("p0")) j = 0;
                                            else if(activation[index].equals("p1")) j = 1;
                                            else j = 2;
                                            for (i = 0; currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[i][j] != null; i++) ;
                                            for(int k = currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardInput().size(); k > 0; k--){
                                                fromWhere[index] = in.nextLine(); // Comandi: c, w, e.
                                            }
                                        }

                                        // Se attiva il potere di produzione base
                                        if(activation[index].equals("b"))
                                            for(int k = 0; k < 2; k++) {
                                                whichInput[k] = in.nextLine();
                                            }
                                        // Risorse a scelta
                                        if(activation[index].equals("b") || activation[index].equals("e0") || activation[index].equals("e1")) {
                                            whichOutput[j] = in.nextLine();
                                            j++;
                                        }
                                    }
                                    if(this.checkActivateProduction(currentPlayer, activation, fromWhere, whichInput, whichOutput))
                                        corrAction++;
                                    break;
                                } */
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

    public void checkPlayCards (Player currentPlayer, String choice) {

        PrintWriter out = currentPlayer.getOutPrintWriter();
        int c;

        if(choice.equals("0") || choice.equals("1"))
            c = Integer.parseInt(choice);
        else {
            out.println("Not valid command.");
            return;
        }

        if (currentPlayer.getPlayerLeaderCards()[c] != null && !currentPlayer.getPlayerLeaderCards()[c].isPlayed()) {
            out.println("Played");
            currentPlayer.getPlayerLeaderCards()[c].printLeaderCard(out);
            currentPlayer.playLeaderCard(c);
        } else
            out.println("Not valid command.");

    }

    public boolean checkDiscardCards (Player currentPlayer, String choice){

        PrintWriter out = currentPlayer.getOutPrintWriter();
        int c;

        if(choice.equals("0") || choice.equals("1") || choice.equals("2") || choice.equals("3"))
            c = Integer.parseInt(choice);
        else {
            out.println("Not valid command.");
            return false;
        }

        if (currentPlayer.getPlayerLeaderCards()[c] != null && !currentPlayer.getPlayerLeaderCards()[c].isPlayed()) {
            out.println("Discarded");
            currentPlayer.getPlayerLeaderCards()[c].printLeaderCard(out);
            currentPlayer.discardLeaderCard(c);
            return true;
        } else
            out.println("Not valid command.");

        return false;
    }

    public boolean checkMarketAction (Player currentPlayer, String choice, String index, String wlChoice, String chosenMarble)
    {
        PrintWriter out = currentPlayer.getOutPrintWriter();
        int c, i;

        //If chosenMarble is correct, it is parsed into integer
        if(chosenMarble.equals("0") || chosenMarble.equals("1"))
            c = Integer.parseInt(chosenMarble);
        else {
            out.println("Not valid command.");
            return false;
        }
        if (c==1 && currentPlayer.getPlayerBoard().getResourceMarbles()[1]==null) {
            out.println("Not valid command.");
            return false;
        }

        //If wlChoice doesn't contain only 'W' and 'L'
        for(int k=0; k<wlChoice.length(); k++)
        {
            if(!String.valueOf(wlChoice.charAt(k)).equalsIgnoreCase("W") && !String.valueOf(wlChoice.charAt(k)).equalsIgnoreCase("L"))
            {
                out.println("Not valid command.");
                return false;
            }
        }

        //If player picks row
        if(choice.equalsIgnoreCase("R"))
        {
            if(wlChoice.length()!=4)
            {
                if(wlChoice.isEmpty())
                    wlChoice="WWWW";
                else {
                    out.println("Not valid command.");
                    return false;
                }
            }

            if(index.equals("0") || index.equals("1") || index.equals("2"))
            {
                i = Integer.parseInt(index);
                this.gameModel.getMarket().updateRow(i, this.gameModel.getPlayers(), currentPlayer.getPlayerNumber(), wlChoice, c);
                return true;
            }
            else {
                out.println("Not valid command.");
                return false;
            }
        }
        else if(choice.equalsIgnoreCase("C"))
        {
            if(wlChoice.length()!=3)
            {
                if(wlChoice.isEmpty())
                    wlChoice="WWW";
                else {
                    out.println("Not valid command.");
                    return false;
                }
            }

            if(index.equals("0") || index.equals("1") || index.equals("2") || index.equals("3"))
            {
                i = Integer.parseInt(index);
                this.gameModel.getMarket().updateRow(i, this.gameModel.getPlayers(), currentPlayer.getPlayerNumber(), wlChoice, c);
                return true;
            }
            else {
                out.println("Not valid command.");
                return false;
            }
        }
        return false;
    }

    public boolean checkBuyDevCard(Player currentPlayer, String colour, String level, String position, String wclChoice, String discountChoice) {

        PrintWriter out = currentPlayer.getOutPrintWriter();

        int l, p;

        colour=colour.toUpperCase();

        if(!this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsColours().containsKey(colour))
        {
            out.println("Not valid command.");
            return false;
        }

        int column;
        column=this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsColours().get(colour);

        if(!level.equals("1") && !level.equals("2") && !level.equals("3"))
        {
            out.println("Not valid command.");
            return false;
        } else {
            l=3 - Integer.parseInt(level);
        }

        if(!position.equals("0") && !position.equals("1") && !position.equals("2"))
        {
            out.println("Not valid command.");
            return false;
        } else {
            p=Integer.parseInt(position);
        }


        if(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0]!=null)
        {
            int totalCost=0;
            for(String cost : this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost().keySet())
                totalCost=totalCost+this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost().get(cost);

            if(wclChoice.length()!=totalCost)
            {
                out.println("Not valid command.");
                return false;
            }

            //If wlChoice doesn't contain only 'W' and 'L'
            for(int k=0; k<wclChoice.length(); k++)
            {
                if(!String.valueOf(wclChoice.charAt(k)).equalsIgnoreCase("W") && !String.valueOf(wclChoice.charAt(k)).equalsIgnoreCase("C") && !String.valueOf(wclChoice.charAt(k)).equalsIgnoreCase("L"))
                {
                    out.println("Not valid command.");
                    return false;
                }
            }
        }

        int discounts=0;
        for(int k=0; k<currentPlayer.getPlayerBoard().getDevelopmentCardDiscount().length; k++)
        {
            if(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[k]!=null)
                discounts++;
            else break;
        }

        if(discountChoice.length()==discounts)
        {
            for (int k=0; k<discounts; k++)
                if( !String.valueOf(discountChoice.charAt(k)).equals("0") && !String.valueOf(discountChoice.charAt(k)).equals("1") )
                {
                    out.println("Not valid command.");
                    return false;
                }
        } else {
            //Empty string = 00
            if(discountChoice.isEmpty())
            {
                discountChoice="00";
                discountChoice.substring(0, discounts);
            }
            else {
                out.println("Not valid command.");
                return false;
            }
        }

        //Chiamata al metodo del gamemodel, controlli effettuati
        return this.gameModel.buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, l, p, wclChoice, discountChoice);
    }

}
