package org.example.Client;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;
import com.google.gson.Gson;

public class ClientMain {

    //Eventuale lista comandi da stampare per aiutare ma non guidare
    private List<String> commands = Arrays.asList("Play leader card",
            "Discard leader card");

    public static void main(String[] args) throws IOException {

        //Passarli come paramteri del main
        String hostName = "127.0.0.1";
        int portNumber = 1234;
        String gameMode;
        String nickName;
        Scanner input = new Scanner(System.in);

        // Game mode
        System.out.println("Welcome to Masters of Renaissance!");
        System.out.println("Write 0 for single-player or 1 for multiplayer: ");
        gameMode = input.nextLine();
        while (!gameMode.equals("0") && !gameMode.equals("1")) {
            System.out.println("Number not valid!");
            System.out.println("Write 0 for single-player or 1 for multiplayer: ");
            gameMode = input.nextLine();
        }

        if (gameMode.equals("0")) {
/*
            Player[] players = new Player[2];
            System.out.println("Write your nickname: ");
            PrintWriter output = new PrintWriter(System.out,true);
            nickName = input.nextLine();

            players[0] = new Player(nickName);
            players[0].setPlayerNumber(0);
            players[1] = new Player("Lorenzo il Magnifico");
            players[1].setPlayerNumber(1);

            ActionCountersDeck localActionCountersDeck = new ActionCountersDeck();
            DevelopmentCardsDecksGrid localDevelopmentCardsDeckGrid = new DevelopmentCardsDecksGrid();
            Market localMarket = new Market();
            LeaderCardDeck localLeaderCardDeck = new LeaderCardDeck();

            for(int index = 0; index < players[0].getPlayerLeaderCards().length; index++)
                players[0].setPlayerLeaderCard(index,localLeaderCardDeck.drawOneLeaderCard());
            for(int ind = 0; ind < 2; ind++)
                players[0].discardLeaderCard(input, output);

            boolean endGame = false;

            while (!endGame && players[0].getPlayerBoard().getFaithPath().getCrossPosition() < 24 && players[0].getPlayerBoard().getDevelopmentCardsBought() < 7 && players[1].getPlayerBoard().getFaithPath().getCrossPosition() < 24) {
                if(players[0].getPlayerLeaderCards()[0] != null) {
                    if(players[0].getPlayerLeaderCards()[1] == null && !players[0].getPlayerLeaderCards()[0].isPlayed())
                        players[0].getLeaderAction(input, output);
                    else if(players[0].getPlayerLeaderCards()[1] != null && (!players[0].getPlayerLeaderCards()[0].isPlayed() || !players[0].getPlayerLeaderCards()[1].isPlayed()))
                        players[0].getLeaderAction(input, output);
                    else System.out.println("You have activated all your Leader cards. You can't do a Leader Action.");
                }
                    else System.out.println("You have discarded all your Leader cards. You can't do a Leader Action.");

                players[0].printAll(output);

                System.out.println("LORENZO BLACK CROSS POSITION: "+players[1].getPlayerBoard().getFaithPath().getCrossPosition());
                System.out.println();

                System.out.println("MARKET GRID:");
                localMarket.printMarket(output);
                System.out.println("DEVELOPMENT CARDS GRID:");
                localDevelopmentCardsDeckGrid.printGrid(output);

                boolean correctAction = true;
                do {
                    switch (players[0].getAction(input, output)) {
                        case "0":
                            correctAction = players[0].pickLineFromMarket(localMarket, players, input, output);
                            break;
                        case "1":
                            correctAction = players[0].buyDevelopmentCard(localDevelopmentCardsDeckGrid, input, output);
                            break;
                        case "2":
                            correctAction = players[0].activateProduction(input, output);
                            break;
                    }
                }while (!correctAction);

                if(players[0].getPlayerLeaderCards()[0]!=null)
                    if(players[0].getPlayerLeaderCards()[1]==null && !players[0].getPlayerLeaderCards()[0].isPlayed())
                        players[0].getLeaderAction(input, output);
                    else if(players[0].getPlayerLeaderCards()[1]!=null &&
                            (!players[0].getPlayerLeaderCards()[0].isPlayed() || !players[0].getPlayerLeaderCards()[1].isPlayed()))
                        players[0].getLeaderAction(input, output);

                localActionCountersDeck.drawCounter().activate(localActionCountersDeck,players[1].getPlayerBoard(),localDevelopmentCardsDeckGrid, output);
                System.out.println("New turn!!");

                for (int i=0; i<4; i++)
                    if(localDevelopmentCardsDeckGrid.getDevelopmentCardsDecks()[0][i][0]==null)
                    {
                        endGame=true;
                        break;
                    }

                System.out.println();
            }

            System.out.println("Game over.");

            if(endGame)
            {
                System.out.println("Lorenzo il Magnifico wins!!");
                System.out.println("There aren't any available DevelopmentCards of one type.");
            }

            if(players[1].getPlayerBoard().getFaithPath().getCrossPosition()==25)
            {
                System.out.println("Lorenzo il Magnifico wins!!");
                System.out.println("Lorenzo reaches the end of FaithPath.");
            }

            if(players[0].getPlayerBoard().getFaithPath().getCrossPosition()==25){
                System.out.println(players[0].getNickname()+" wins!!");
                System.out.println("You reach the end of FaithPath.");
                System.out.println("You have obtained "+players[0].sumAllVictoryPoints()+" victory points!!");
            }

 */

        }
        else {
            //Trial Beppe
            //Ask nickname before gameMode branch
            //Call player constructor directly on the server?
            //Player player = new Player();
            try {

                Socket clientSocket = new Socket(hostName, portNumber);

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader((new InputStreamReader(System.in)));

                //Forse non serve più
                // :(
                Thread serverReceiver = new Thread(() -> {
                    try {
                        while (true) {

                            String serverMessage = in.readLine();

                            if (serverMessage == null)
                                break;

                            System.out.println("Received: " + serverMessage);
                        }
                    } catch (SocketTimeoutException e) {
                        System.err.println("You've been expelled for inactivity");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                serverReceiver.start();

                String action = null;
                int mainAction = 0;
                //do
                while (!(action.equalsIgnoreCase("END TURN")
                        || action.equalsIgnoreCase("QUIT")))
                        //&& (mainAction < 1))
                                                {
                    action = stdIn.readLine();
                    //Puts in upper case the input
                    action = action.toUpperCase();

                    //Posso inviare il nickname del player qunado inserisce un comando per vedere se è il suo turno
                    //altrimenti lo ignoro

                    //IMPORTANT!!!
                    //out.println sends a null string that signals stdin error to the controller to
                    //reset its state at before the action
                    out.flush();
                    //out.println();
                    switch (action) {
                        case "PLAY LEADER CARD": {
                            //Sends the command to the controller so it can change state
                            out.println(action);
                            //Confirms command reception
                            //Already received from thread that prints server messages
                            //System.out.println(in.readLine());
                            //Starts receiving parameters
                            String parameter;
                            parameter = stdIn.readLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                //Posso mandare qualunque numero e la validità la verifica il controller
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();
                                out.println(cardPosition);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                out.println();
                                break;
                            }
                        }
                        case "DISCARD LEADER CARD": {
                            //Same for play leader card
                            out.println(action);
                            //System.out.println(in.readLine());
                            String parameter = stdIn.readLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();
                                out.println(cardPosition);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                out.println();
                                break;
                            }
                        }
                        case "PICK RESOURCES FROM MARKET": {

                            if (mainAction == 1) {
                                System.err.println("Action already done");
                                break;
                            }

                            //Sends action
                            out.println(action);
                            //Receives OK
                            //System.out.println(in.readLine());

                            //Receives column or row
                            String parameter = null;
                            parameter = stdIn.readLine();
                            parameter = parameter.toUpperCase();
                            if (parameter.equals("ROW")
                                || parameter.equals("COLUMN")) {
                                out.println(parameter);
                                //System.out.println(in.readLine());
                            } else {
                                //Resets action
                                System.err.println("Not valid parameter");
                                out.println();
                                break;
                            }

                            //Receives index
                            String par = stdIn.readLine();
                            try {
                                //Checks if the leader card position exists
                                int index = Integer.parseInt(par);
                                if (parameter.equalsIgnoreCase("row") && (index < 0 || index > 2)) throw new Exception();
                                if (parameter.equalsIgnoreCase("column") && (index < 0 || index > 3)) throw new Exception();
                                out.println(index);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                out.println();
                                break;
                            }

                            //Receives deposit
                            String wlChoice = stdIn.readLine();
                            try {
                                //Checks if player has written only 'w' and 'l' chars
                                if (wlChoice.length()==0) throw new Exception();

                                for(int k=0; k<wlChoice.length(); k++)
                                    if(!String.valueOf(wlChoice.charAt(k)).equalsIgnoreCase("w") || !String.valueOf(wlChoice.charAt(k)).equalsIgnoreCase("l")) throw new Exception();

                                out.println(wlChoice);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                out.println();
                                break;
                            }

                            //Receives position of leader cards to activate to receive a resource from a white marble
                            String chosenMarble = stdIn.readLine();
                            try {
                                //Checks if player has written only '0', '1' or 'x' chars

                                if(chosenMarble.length()!=0)
                                    for(int k=0; k<wlChoice.length(); k++)
                                        if(!String.valueOf(chosenMarble.charAt(k)).equalsIgnoreCase("0")
                                                || !String.valueOf(chosenMarble.charAt(k)).equalsIgnoreCase("1")
                                                || !String.valueOf(chosenMarble.charAt(k)).equalsIgnoreCase("x")) throw new Exception();

                                out.println(chosenMarble);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                out.println();
                                break;
                            }

                            //Se va a buon fine fino a qui il comando si passa alla seconda fase dove
                            //il giocatore distribuisce una ad una le risorse
                            //Sceglie luogo e se warehouse sceglie anche lo scaffale
                            //altrimenti leaderCard se le ha??

                            //Come fare?

                            //Le biglie le prende il controller e dopo


                            //Questo glielo chiede il controller per ogni biglia pescata
                            //altrimento al metodo servono tre parametri
                            //Warehouse/leaderCard choice
                            //String wlChoice = stdIn.readLine();

                            //Anche questo lo chiede il controller
                            //verificando se il player ha abilità speciale o no
                            //If he has 2 whiteMarbleLeaderCards
                            //String chosenMarble="0";

                            //if(currentPlayer.getPlayerBoard().getResourceMarbles()[0]!=null)
                                //chosenMarble=stdIn.();

                            //if (this.checkMarketAction(this.gameModel.getPlayers()[i], rcChoice, choice, wlChoice, chosenMarble))
                                mainAction++;
                            //break;
                        }
                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        //Check discount mechanic
                        case "BUY DEVELOPMENT CARD": {

                            if (mainAction == 1) {
                                System.err.println("Action already done");
                                break;
                            }

                            out.println(action);

                            String parameter = null;
                            parameter = stdIn.readLine();
                            parameter = parameter.toUpperCase();
                            if (parameter.equalsIgnoreCase("green")
                                    || parameter.equalsIgnoreCase("yellow")
                                    || parameter.equalsIgnoreCase("blue")
                                    || parameter.equalsIgnoreCase("purple")) {
                                out.println(parameter);
                                //System.out.println(in.readLine());
                            } else {
                                //Resets controller
                                System.err.println("Not valid parameter");
                                out.println();
                                break;
                            }

                            parameter = stdIn.readLine();
                            try {
                                int level = Integer.parseInt(parameter);
                                if (level<1 || level>3) throw new Exception();
                                out.println(level);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                out.println();
                                break;
                            }

                            //Check
                            int[] quantity = new int[4];
                            String[] shelf = new String[4];

                            //Correspondence between resources and arrays index
                            Map<String, Integer> resources = new HashMap<>();
                            resources.put("COINS", 0);
                            resources.put("SERVANTS", 1);
                            resources.put("SHIELDS", 2);
                            resources.put("STONES", 3);

                            for(int k=0; k<4; k++)
                            {
                                quantity[k]=0;
                                shelf[k]=null;
                            }

                            //Which resource do you want to take
                            parameter = null;
                            while (!parameter.equalsIgnoreCase("STOP")) {

                                parameter = stdIn.readLine().toUpperCase();
                                if (parameter.equals("COINS") || parameter.equals("STONES") || parameter.equals("SERVANTS") || parameter.equals("SHIELDS")) {
                                    int index = resources.get(parameter);

                                    //Receives now quantity
                                    parameter = stdIn.readLine();
                                    try {
                                        int q = Integer.parseInt(parameter);
                                        if (q<0) throw new Exception();
                                        if (q>8) throw new Exception();
                                        quantity[index]=q;
                                    } catch (Exception e) {
                                        System.err.println("Not valid parameter");
                                        out.println();
                                        break;
                                    }

                                    for(int z=0; z<quantity[index]; z++)
                                    {
                                        //Keeps asking a place to take from resources
                                        parameter = stdIn.readLine().toUpperCase();
                                        if (parameter.equals("CHEST") || parameter.equals("WAREHOUSE") || parameter.equals("LEADER CARD")) {
                                            shelf[index]=shelf[index] + parameter.charAt(0);
                                        } else {
                                            System.err.println("Not valid parameter");
                                            out.println();
                                            break;
                                        }
                                    }

                                } else {
                                    System.err.println("Not existing resource");
                                    out.println();
                                    break;
                                }
                            }

                            for(int z=0; z<4; z++)
                            {
                                out.println(quantity[z]);
                                out.println(shelf[z]);
                            }

                            //Position where to place the card on the playerboard
                            parameter = stdIn.readLine();
                            try{

                                int pos = Integer.parseInt(parameter);
                                if(pos < 0 || pos > 2) throw new Exception();
                                out.println(pos);

                            } catch (Exception e) {
                                System.err.println("Not valid command");
                                out.println();
                                break;
                            }

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            //If he can pay discounted price
                            //String discountChoice="00";

                            //if(this.checkBuyDevCard(this.gameModel.getPlayers()[i], colour, level, position, wclChoice, discountChoice))
                            mainAction++;
                            //break;
                        }
                        //Qua ancora non iniziato
                        case "ACTIVATE PRODUCTION POWER": {

                            if (mainAction == 1) {
                                System.err.println("Action already done");
                                break;
                            }

                            int[] activation = {0, 0, 0, 0, 0, 0};
                            String[] commandsList = new String[6];
                            String[] fromWhere = new String[6];
                            String[] whichInput = new String[2];
                            String[] whichOutput = new String[3];
                            String unknownCommand;
                            String command = null;
                            int j, i;
                            boolean anotherCommand = false;

                            j = 0;
                            for(int index = 0; index < 6; index++) {

                                if(!anotherCommand) command = stdIn.readLine(); // Comandi: p0, p1, p2, b, e0, e1, STOP
                                anotherCommand = false;

                                if (!command.equalsIgnoreCase("STOP")) {
                                    commandsList[index] = command; // lista di comandi, nel check si controlla la loro correttezza
                                    if (command.equalsIgnoreCase("p0") || command.equalsIgnoreCase("p1") || command.equalsIgnoreCase("p2")) {
                                        if (command.equalsIgnoreCase("p0")) {
                                            i = 0;
                                            activation[0] = 1;
                                        }
                                        else if (command.equalsIgnoreCase("p1")) {
                                            i = 1;
                                            activation[1] = 1;
                                        }
                                        else {
                                            i = 2;
                                            activation[2] = 1;
                                        }
                                        for (int k = 0; k < 2; k++) {
                                            unknownCommand = stdIn.readLine();// Comandi: c, w, e.
                                            if (unknownCommand.equalsIgnoreCase("c") || unknownCommand.equalsIgnoreCase("w") || unknownCommand.equalsIgnoreCase("e"))
                                                fromWhere[i] = fromWhere[i] + unknownCommand;
                                            else {
                                                command = unknownCommand;
                                                if (command.equalsIgnoreCase("p0") || command.equalsIgnoreCase("p1") || command.equalsIgnoreCase("p2")) anotherCommand = true;
                                                k = 1;
                                            }
                                        }
                                    }

                                    if (!anotherCommand) {
                                        // Se attiva il potere di produzione base
                                        if (command.equalsIgnoreCase("b")) {
                                            for (int k = 0; k < 2; k++) {
                                                whichInput[k] = stdIn.readLine(); // Comandi: COINS, SHIELDS...
                                                unknownCommand = stdIn.readLine(); // Comandi: c, w, e.
                                                fromWhere[3] = fromWhere[index] + unknownCommand;
                                            }
                                            activation[3] = 1;
                                        }
                                        // Risorse a scelta
                                        if (command.equalsIgnoreCase("b") || command.equalsIgnoreCase("e0") || command.equalsIgnoreCase("e1")) {
                                            whichOutput[j] = stdIn.readLine(); // Comandi: COINS, SHIELDS...
                                            j++;
                                            if (command.equals("e0")) {
                                                fromWhere[4] = stdIn.readLine();// Comandi: c, w, e.
                                                activation[4] = 1;
                                            } else if (command.equals("e1")) {
                                                fromWhere[5] = stdIn.readLine();// Comandi: c, w, e.
                                                activation[5] = 1;
                                            }
                                        }
                                    }
                                }
                            }
                            if(checkActivateProduction(commandsList, activation, fromWhere, whichInput, whichOutput))
                                mainAction++;
                            break;
                        }
                        default: {
                            out.println("Not valid action!");
                            break;
                        }
                    }
                    //Player inserisce quit
                }//while (!action.equalsIgnoreCase("END TURN") && (corrAction < 1));

                //this.gameModel.getPlayers()[i].getOutPrintWriter().println("Your turn has ended. Wait for other players...");
                //this.gameModel.getPlayers()[i].getOutPrintWriter().println();


                /*while (true) {

                        String clientInput = stdIn.readLine();
                        //Trial
                        if (clientInput.equals("QUIT")) {
                            System.out.println("You left the game");
                            System.out.println("Bye bye");
                            break;
                        }
                        out.println(clientInput);
                }*/

            } catch (UnknownHostException e) {
                System.err.println("No info about host: " + hostName);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for connection to hostname: " + hostName);
                System.exit(1);
            }
        }

    }

    public static boolean checkActivateProduction(String[] commandList, int[] activation, String[] fromWhere, String[] whichInput, String[] whichOutput) {

        PrintWriter out = new PrintWriter(System.out);

        List<String> inputs = new ArrayList<>();
        List<String> outputs = new ArrayList<>();
        int extraProdActivate = 0;

        for(int i = 0; commandList[i] != null; i++) {
            if(!commandList[i].equals("p0") && !commandList[i].equals("p1") && !commandList[i].equals("p2") &&
                    !commandList[i].equals("b") && !commandList[i].equals("e0") && !commandList[i].equals("e1")) {
                out.println("Not valid command.");
                return false;
            }
        }


        for(int index = 0; index < 6; index++) {
            if(activation[index] == 1) {
                if(index == 0 || index == 1 || index == 2 || index == 3) {
                    for(int i = 0; i < fromWhere[index].length(); i++) {
                        if(fromWhere[index].charAt(i) != 'c' && fromWhere[index].charAt(i) != 'w' && fromWhere[index].charAt(i) != 'e') {
                            out.println("Not valid command.");
                            return false;
                        }
                    }
                    if(index == 3) {
                        for(int k = 0; k < 2; k++) {
                            if (!whichInput[k].equals("COINS") && !whichInput[k].equals("SHIELDS") && !whichInput[k].equals("SERVANTS") && !whichInput[k].equals("STONES") && !whichInput[k].equals("REDCROSS")) {
                                out.println("Not valid command.");
                                return false;
                            }
                            inputs.add(whichInput[k]);
                        }
                        extraProdActivate++;
                    }
                }
                else {
                    if(!fromWhere[index].equals("c") && !fromWhere[index].equals("w") && !fromWhere[index].equals("e")) {
                        out.println("Not valid command.");
                        return false;
                    }
                    extraProdActivate++;
                }
            }
        }

        for(int k = 0; k < extraProdActivate; k++) {
            if (!whichOutput[k].equals("COINS") && !whichOutput[k].equals("SHIELDS") && !whichOutput[k].equals("SERVANTS") && !whichOutput[k].equals("STONES") && !whichOutput[k].equals("REDCROSS")) {
                out.println("Not valid command.");
                return false;
            }
            outputs.add(whichOutput[k]);
        }

        return true;
    }
}
