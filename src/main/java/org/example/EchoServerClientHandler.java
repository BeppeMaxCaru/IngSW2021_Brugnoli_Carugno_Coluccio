package org.example;

import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class EchoServerClientHandler implements Runnable {
    private Socket socket;

    private Player player;// = new Player();
    //this.player.chooseNickname();

    public EchoServerClientHandler(Socket socket) {
        this.socket = socket;
        this.player = new Player();
        this.player.chooseNickname();
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            Player player = new Player();
            try {
                player.chooseNickname();
            } catch (Exception e) {
                e.printStackTrace();
                out.println("You weren't able to join a game");
            }

// Leggo e scrivo nella connessione finche' non ricevo "quit"
            while (true) {
                this.player.getLeaderAction();
                this.player.getAction();
                this.player.getLeaderAction();

                /*String line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                } else {
                    out.println("Received: " + line);
                    out.flush();
                }*/
            }
// Chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}