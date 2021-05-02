package org.example;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoServerClientHandler extends Thread implements Runnable{
    private Socket clientSocket;
    private String nickname;
    private Scanner in;
    private PrintWriter out;

    private Player player;// = new Player();
    //this.player.chooseNickname();

    public EchoServerClientHandler(Socket clientSocket, GameModel gameModel) {
        this.clientSocket = clientSocket;
        //Online input
        try {
            this.in = new Scanner(this.clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Online output
        try {
            this.out = new PrintWriter(this.clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                this.player.discardLeaderCard(this.in, this.out);
            }
        } catch (Exception e) {
            out.println("You weren't able to join a game");
            out.println("Try next time");
        }
    }

    //@Override
    public void run(GameModel gameModel) {
        try {
            // Leggo e scrivo nella connessione finche' non ricevo "quit"
            while (true) {
                this.player.getLeaderAction(this.in, this.out);
                this.player.getAction(this.in, this.out);
                this.player.getLeaderAction(this.in, this.out);

                /*String line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                } else {
                    out.println("Received: " + line);
                    out.flush();
                }*/
                break;
            }
// Chiudo gli stream e il socket
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}