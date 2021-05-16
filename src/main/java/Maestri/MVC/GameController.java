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

    public void run() {

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
    }

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

    public boolean checkActivateProduction(Player currentPlayer, String[] activation, String[] fromWhere, String whichInput, String[] whichOutput) {

        PrintWriter out = currentPlayer.getOutPrintWriter();

        int[] active = new int[6];
        int[] inputs = new int[2];
        int[] outputs = new int[3];

        int numOfActions=0;


        for(int index=0; index<6; index++)
        {
            if(!activation[index].equals("0") && !activation[index].equals("1"))
            {
                out.println("Not valid command.");
                return false;
            }
            active[index] = Integer.parseInt(activation[index]);

            if(!fromWhere[index].equalsIgnoreCase("W") && !fromWhere[index].equalsIgnoreCase("C") && !fromWhere[index].equalsIgnoreCase("L"))
                return false;

            if(index==3)
            {
                for(int k=0; k<2; k++)
                {

                    if(!String.valueOf(whichInput.charAt(k)).equals("0") &&
                            !String.valueOf(whichInput.charAt(k)).equals("1") && !String.valueOf(whichInput.charAt(k)).equals("2") &&
                            !String.valueOf(whichInput.charAt(k)).equals("3") && !String.valueOf(whichInput.charAt(k)).equals("4"))
                    {
                        out.println("Not valid command.");
                        return false;
                    }
                    inputs[k]=Integer.parseInt(whichInput);
                }
            }

            if(index>2)
            {
                if(!whichOutput[index-3].equals("0") && !whichOutput[index-3].equals("1") && !whichOutput[index-3].equals("2") && !whichOutput[index-3].equals("3") && !whichOutput[index-3].equals("4"))
                {
                    out.println("Not valid command.");
                    return false;
                }
                outputs[index-3]=Integer.parseInt(whichInput);
            }

            boolean correctAction;

            if(active[index]==1)
            {
                correctAction=currentPlayer.activateProduction(fromWhere, inputs, outputs);
                if(!correctAction)
                    return false;
                else
                    numOfActions++;
            }

        }
        return numOfActions != 0;
    }

    }
