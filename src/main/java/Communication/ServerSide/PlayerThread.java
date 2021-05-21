package Communication.ServerSide;

import Maestri.MVC.GameController;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerThread implements Runnable {

    private String nickName;
    private int playerThreadNumber;

    private String currentPlayerNickname;

    private Socket playerSocket;
    private Scanner inScanner;
    private PrintWriter outPrintWriter;

    //The controller for this player
    private GameController gameController;

    public PlayerThread(Socket clientSocket) {
        try {
            this.playerSocket = clientSocket;
            this.inScanner = new Scanner(new InputStreamReader(this.playerSocket.getInputStream()));
            this.outPrintWriter = new PrintWriter(this.playerSocket.getOutputStream(), true);
            //this.gameController = gameController;

            //Chiedi username
            this.outPrintWriter.println("Insert your nickname: ");
            this.nickName = inScanner.nextLine();
            this.outPrintWriter.println("Welcome to Masters of Renaissance online!");
            this.outPrintWriter.println("Looking for a game in Florence...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPlayerThreadNumber() {
        return this.playerThreadNumber;
    }

    public void setPlayerThreadNumber(int playerThreadNumber) {
        this.playerThreadNumber = playerThreadNumber;
    }

    public void setCurrentPlayerNickname(String currentPlayerNickname) {
        this.currentPlayerNickname = currentPlayerNickname;
    }



    public Scanner getInScanner() {
        return this.inScanner;
    }

    public void setInScanner(Scanner inScanner) {
        this.inScanner = inScanner;
    }

    public PrintWriter getOutPrintWriter() {
        return this.outPrintWriter;
    }

    public void setOutPrintWriter(PrintWriter outPrintWriter) {
        this.outPrintWriter = outPrintWriter;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void run() {

        try {
            this.inScanner = new Scanner(new InputStreamReader(this.playerSocket.getInputStream()));
            this.outPrintWriter = new PrintWriter(this.playerSocket.getOutputStream(), true);

            this.outPrintWriter.println("New game started");

            String clientMessage = "";

            while (!clientMessage.equalsIgnoreCase("quit")) {

                clientMessage = this.inScanner.nextLine();

                //controlla se il comando ha all'inizio il nome del giocatore corrente o di un altro giocatore

                //If (message.getPlayer != currentPlayer) out.println("Not your turn);
                //Else
                //Switch sui messaggi ricevuti se è il suo turno altrimenti bloccato da controllo sopra
                //Dopo azione corretta uso il metodo BROADCAST del gameController e stampo a tutti la modifica avvenuta

                //Switch comandi

            }

        } catch (Exception e) {
            //Non funziona connessione client
        }
    }
}
