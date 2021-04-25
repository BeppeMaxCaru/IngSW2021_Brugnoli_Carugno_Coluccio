package org.example;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import org.graalvm.compiler.hotspot.nodes.PluginFactory_JumpToExceptionHandlerInCallerNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientMain {

    //Optional classes for local game
    private DevelopmentCardsDecksGrid localDevelopmentCardsDeckGrid;
    private ActionCountersDeck localActionCountersDeck;
    private Player player;
    //private int gameMode;

    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1";
        int portNumber = 1234;

        int gameMode;

        System.out.println("Welcome to Masters of Renaissance!");
        Scanner localInput = new Scanner(System.in);
        System.out.println("Write 0 for single-player or 1 for multiplayer: ");
        //String gameMode = localInput.nextLine();

        //serve metodo per tenere main di tipo static!!!!!!!!!
        while (true) {
            try {
                String input = localInput.nextLine();
                gameMode = Integer.parseInt(input);
                if (gameMode == 0 || gameMode == 1) break;

            } catch (NumberFormatException n) {
                System.out.println("Number not valid!");
                System.out.println("Write 0 for single-player or 1 for multiplayer: ");
            }
        }

        //this.player = new Player();

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
