package org.example;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoServerClientHandler extends Thread implements Runnable{
    private Socket clientSocket;
    private String nickname;

    private Player player;// = new Player();
    //this.player.chooseNickname();

    public EchoServerClientHandler(Socket clientSocket, GameModel gameModel) {
        this.clientSocket = clientSocket;
        //Start asking
        /*out.println("buongiorno");
        try {
            out.println("Insert your nickname: ");
            this.nickname = in.nextLine();
            Player player = new Player(this.nickname);
            //
            gameModel.addNewPlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("You weren't able to join a game");
            out.println("Try again next time");
        }
        //this.player = new Player();
        //this.player.chooseNickname();
        try {
            for (int i=0;i<this.player.getPlayerLeaderCards().length;i++) {
                this.player.setPlayerLeaderCard(i, gameModel.getLeaderCardDeck().drawOneLeaderCard());
            }
            for (int i=0;i<2;i++) {
                this.player.discardLeaderCard(in, out);
            }
        } catch (Exception e) {
            out.println("You weren't able to join a game");
            out.println("Try next time");
        }*/
    }

    //@Override
    public void run() {
        try {
            //Scanner in = new Scanner(this.clientSocket.getInputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(this.clientSocket.getOutputStream(), true);

            out.println("Insert your nickname: ");
            String clientInput = in.readLine();
            this.player = new Player(clientInput);

            out.println("Hi " + this.player.getNickname() + "! Welcome to Masters of renaissance online!");
            out.println("Write QUIT to leave the game anytime you want");
            while (true) {
                out.println("Inserisci un comando: ");
                clientInput = in.readLine();
                if (clientInput.equals("QUIT")) {
                    out.println("You left the game");
                    break;
                }
                //out.println("Ciao! Inserisci un comando: ");
                //else out.println(clientInput);
            }

            // Leggo e scrivo nella connessione finche' non ricevo "quit"
            /*if (gameModel.getCurrentPlayer()==this.player.getPlayerNumber()) {
                this.player.getLeaderAction(in, out);
                this.player.getAction(in, out);
                this.player.getLeaderAction(in, out);

                String line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                } else {
                    out.println("Received: " + line);
                    out.flush();
                }
            }*/
            // Chiudo gli stream e il socket

            in.close();
            out.close();
            this.clientSocket.close();
            //this.wait();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //this.out.println("Lost connection");
        }
    }

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
}