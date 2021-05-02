package org.example;

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
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws IOException {

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

            for(int index=0; index<players[0].getPlayerLeaderCards().length; index++)
                players[0].setPlayerLeaderCard(index,localLeaderCardDeck.drawOneLeaderCard());
            for(int ind=0; ind<2; ind++)
                players[0].discardLeaderCard(input, output);

            boolean endGame = false;

            while (!endGame && players[0].getPlayerBoard().getFaithPath().getCrossPosition() < 24 && players[0].getPlayerBoard().getDevelopmentCardsBought() < 7 && players[1].getPlayerBoard().getFaithPath().getCrossPosition() < 24)
            {

                if(players[0].getPlayerLeaderCards()[0] != null)
                {
                    if(players[0].getPlayerLeaderCards()[1] == null && !players[0].getPlayerLeaderCards()[0].isPlayed())
                        players[0].getLeaderAction(input, output);
                    else if(players[0].getPlayerLeaderCards()[1]!=null &&
                            (!players[0].getPlayerLeaderCards()[0].isPlayed() || !players[0].getPlayerLeaderCards()[1].isPlayed()))
                        players[0].getLeaderAction(input, output);
                    else System.out.println("You have activated all your Leader cards. You can't do a Leader Action.");
                }
                    else System.out.println("You have discarded all your Leader cards. You can't do a Leader Action.");

                System.out.println("YOUR RESOURCES:");
                System.out.println("COINS   : "+players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("COINS")+" in warehouse, "+
                        +players[0].getPlayerBoard().getChest().getChestResources().get("COINS")+" in chest");
                System.out.println("SERVANTS: "+players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SERVANTS")+" in warehouse, "+
                        +players[0].getPlayerBoard().getChest().getChestResources().get("SERVANTS")+" in chest");
                System.out.println("SHIELDS : "+players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("SHIELDS")+" in warehouse, "+
                        +players[0].getPlayerBoard().getChest().getChestResources().get("SHIELDS")+" in chest");
                System.out.println("STONES  : "+players[0].getPlayerBoard().getWareHouse().getWarehouseResources().get("STONES")+" in warehouse, "+
                        +players[0].getPlayerBoard().getChest().getChestResources().get("STONES")+" in chest");
                System.out.println();

                System.out.println("YOUR DEVELOPMENT CARDS: ");
                players[0].printPlayerCards(input, output);

                System.out.println("YOUR FAITH PATH POSITION    : "+players[0].getPlayerBoard().getFaithPath().getCrossPosition());
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
                            players[0].pickLineFromMarket(localMarket, players, input, output);
                            correctAction = true;
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

                for (int i=0; i<4; i++)
                    if(localDevelopmentCardsDeckGrid.getDevelopmentCardsDecks()[0][i][0]==null)
                        endGame=true;

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

        }
        else {
            //Trial Beppe
            //Ask nickname before gameMode branch
            //Call player constructor directly on the server?
            //Player player = new Player();
            try (Socket clientSocket = new Socket(hostName, portNumber);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedReader stdIn = new BufferedReader((new InputStreamReader(System.in)))
            ) {
                String userInput;

                System.out.println(in.readLine());
                //out.println();

                while ((userInput = stdIn.readLine()) != null) {
                    out.println(userInput);
                    System.out.println("echo: " + in.readLine());
                }


                /*while ((in.readLine()) != null) {
                    System.out.println(in.readLine());
                    System.out.println("Received: " + in.readLine());
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
