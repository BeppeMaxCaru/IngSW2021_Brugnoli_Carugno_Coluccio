package org.example;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
//import org.graalvm.compiler.hotspot.nodes.PluginFactory_JumpToExceptionHandlerInCallerNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1";
        int portNumber = 1234;

        int gameMode = -1;

        String nickName;

        System.out.println("Welcome to Masters of Renaissance!");

        Scanner localInput = new Scanner(System.in);
        while (gameMode < 0 || gameMode > 1) {
            System.out.println("Write 0 for single-player or 1 for multiplayer: ");
            gameMode = localInput.nextInt();
        }

        if (gameMode == 0) {

            Player[] players = new Player[2];
            System.out.println("Write your nickname: ");
            Scanner nickInput = new Scanner(System.in);
            nickName = nickInput.nextLine();
            players[0] = new Player(nickName, 0);
            players[1] = new Player("Lorenzo il Magnifico",1);

            ActionCountersDeck localActionCountersDeck = new ActionCountersDeck();
            DevelopmentCardsDecksGrid localDevelopmentCardsDeckGrid = new DevelopmentCardsDecksGrid();
            Market localMarket = new Market();

            LeaderCardDeck localLeaderCardDeck = new LeaderCardDeck();

            for(int index=0; index<players[0].getPlayerLeaderCards().length; index++)
                players[0].setPlayerLeaderCard(index,localLeaderCardDeck.drawOneLeaderCard());
            for(int ind=0; ind<2; ind++)
                players[0].discardLeaderCard();

            boolean endGame=false;

            while (!endGame&&players[0].getPlayerBoard().getFaithPath().getCrossPosition()<25&&players[1].getPlayerBoard().getFaithPath().getCrossPosition()<25)
            {

                if(players[0].getPlayerLeaderCards()[0]!=null)
                {
                    if(players[0].getPlayerLeaderCards()[1]==null && !players[0].getPlayerLeaderCards()[0].isPlayed())
                        players[0].getLeaderAction();
                    else if(players[0].getPlayerLeaderCards()[1]!=null &&
                            (!players[0].getPlayerLeaderCards()[0].isPlayed() || !players[0].getPlayerLeaderCards()[1].isPlayed()))
                        players[0].getLeaderAction();
                    else System.out.println("You have activated all your Leader cards. You can't do a Leader Action.");
                }
                    else System.out.println("You have discarded all your Leader cards. You can't do a Leader Action.");

                boolean correctAction=true;
                do{
                    switch (players[0].getAction()) {
                        case 0:
                            players[0].pickLineFromMarket(localMarket, players);
                            break;
                        case 1:
                            correctAction=players[0].buyDevelopmentCard(localDevelopmentCardsDeckGrid);
                            break;
                        case 2:
                            correctAction=players[0].activateProduction();
                            break;
                    }
                }while (!correctAction);

                if(players[0].getPlayerLeaderCards()[0]!=null)
                    if(players[0].getPlayerLeaderCards()[1]==null && !players[0].getPlayerLeaderCards()[0].isPlayed())
                        players[0].getLeaderAction();
                    else if(players[0].getPlayerLeaderCards()[1]!=null &&
                            (!players[0].getPlayerLeaderCards()[0].isPlayed() || !players[0].getPlayerLeaderCards()[1].isPlayed()))
                        players[0].getLeaderAction();

                localActionCountersDeck.drawCounter().activate(localActionCountersDeck,players[1].getPlayerBoard(),localDevelopmentCardsDeckGrid);

                int[] availableDevCards = new int[4];
                for (int i=0; i<4; i++)
                {
                    availableDevCards[i]=12;
                    for(int k=0; k<3; k++){
                        for (int j=0; j<4; j++){
                            if(localDevelopmentCardsDeckGrid.getDevelopmentCardsDecks()[k][i][j]==null)
                                availableDevCards[i]--;
                        }
                    }
                    if(availableDevCards[i]==0)
                        endGame=true;
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
                System.out.println("You win!!");
                System.out.println("You reach the end of FaithPath.");
                System.out.println("You have obtained "+players[0].sumAllVictoryPoints()+" victory points!!");
            }

        }
        else{
            try (Socket clientSocket = new Socket(hostName, portNumber);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedReader stdIn = new BufferedReader((new InputStreamReader(System.in)))
            ) {
                String userInput;
                while ((userInput = stdIn.readLine()) != null) {
                    out.println(userInput);
                    System.out.println("echo: " + in.readLine());
                }
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
