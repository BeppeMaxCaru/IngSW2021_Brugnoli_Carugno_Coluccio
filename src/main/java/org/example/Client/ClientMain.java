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
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;

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
                int corrAction = 0;
                //do
                while (!(action.equalsIgnoreCase("END TURN")
                    || action.equalsIgnoreCase("QUIT")) && (corrAction < 1)) {
                    action = stdIn.readLine();
                    //Puts in upper case the input
                    action = action.toUpperCase();

                    //Posso inviare il nickname del player qunado inserisce un comando per vedere se è il suo turno
                    //altrimenti lo ignoro

                    //IMPORTANT!!!
                    //out.println sends a null string that signals stdin error to the controller to
                    //reset its state at before the action
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
                                if (cardPosition != 0 || cardPosition != 1) throw new Exception();
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
                                if (cardPosition != 0 || cardPosition != 1) throw new Exception();
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
                            //Sends action
                            out.println(action);
                            //Receives OK
                            //System.out.println(in.readLine());

                            //Receives column or row
                            String parameter = null;
                            parameter = stdIn.readLine().toUpperCase();
                            if (parameter.equalsIgnoreCase("row")
                                || parameter.equalsIgnoreCase("column")) {
                                out.println(parameter);
                                //System.out.println(in.readLine());
                            } else {
                                //Resets action
                                System.err.println("Not valid parameter");
                                out.println();
                                break;
                            }

                            //Receives index
                            parameter = stdIn.readLine();
                            try {
                                //Checks if the leader card position exists
                                int index = Integer.parseInt(parameter);
                                if (index != 0 || index != 1) throw new Exception();
                                out.println(index);
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
                                corrAction++;
                            //break;
                        }
                        case "BUY DEVELOPMENT CARD": {

                            out.println(action);

                            String parameter = null;
                            parameter = stdIn.readLine().toUpperCase();
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

                            //From which store do you want to take resources
                            //String wclChoice = stdIn.readLine();
                            parameter = null;
                            while (parameter.equals("STOP")) {
                                //Keeps asking a place and the number of resources to take from
                                //that place
                                parameter = stdIn.readLine();
                                if (parameter.equalsIgnoreCase("Chest")
                                    || parameter.equalsIgnoreCase("Warehouse")
                                    || parameter.equalsIgnoreCase("Leader card")) {
                                    out.println(parameter);
                                } else {
                                    System.err.println("Not valid parameter");
                                    out.println();
                                    break;
                                }

                                parameter = stdIn.readLine();
                                if (parameter.equalsIgnoreCase("coins")
                                    || parameter.equalsIgnoreCase("stones")
                                    || parameter.equalsIgnoreCase("servants")
                                    || parameter.equalsIgnoreCase("shields")) {
                                    out.println(parameter);
                                } else {
                                    System.err.println("Not existing resource");
                                    out.println();
                                    break;
                                }

                                //Receives now quantity
                                parameter = stdIn.readLine();
                                try {
                                    int quantity = Integer.parseInt(parameter);
                                    if (quantity<0) throw new Exception();
                                    out.println(quantity);
                                } catch (Exception e) {
                                    System.err.println("Not valid parameter");
                                    out.println();
                                    break;
                                }
                            }

                            //Sends STOP to ask for discount and check enough resources selected
                            out.println(parameter);

                            parameter = stdIn.readLine();
                            if (parameter.equalsIgnoreCase("discount")) {

                            } else if (parameter.equalsIgnoreCase("buy")) {

                            } else {
                                System.err.println("Not valid command");
                                out.println();
                                break;
                            }


                            //If he can pay discounted price
                            //String discountChoice="00";

                            //if(this.checkBuyDevCard(this.gameModel.getPlayers()[i], colour, level, position, wclChoice, discountChoice))
                            //    corrAction++;
                            //break;
                        }
                        //Qua ancora non iniziato
                        case "ACTIVATE PRODUCTION POWER": {

                            String[] activation = new String[6];
                            String[] fromWhere = new String[6];
                            String whichInput = null;
                            String[] whichOutput = new String[3];

                            for(int index=0; index<6; index++)
                            {
                                //activation[index]=stdIn.();
                                //fromWhere[index]=stdIn.();
                                //if(index==3)
                                    //whichInput=stdIn.();
                                //if(index>2)
                                    //whichOutput[index-3]=stdIn.();
                            }

                            /*if(this.checkActivateProduction(currentPlayer, activation, fromWhere, whichInput, whichOutput))
                                corrAction++;
                            break;*/
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
}
