package Communication.ServerSide;

import Maestri.MVC.GameController;
import Message.*;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class PlayerThread implements Runnable {

    private String nickName;
    private int playerThreadNumber;

    private String currentPlayerNickname;

    private Socket playerSocket;
    private Scanner inScanner;
    private PrintWriter outPrintWriter;

    private ObjectInputStream receiver;
    private ObjectOutputStream sender;

    //The controller for this player
    private GameController gameController;

    //Il game controller va assegnato con setter siccome creato prima
    public PlayerThread(Socket clientSocket) {
        try {
            this.playerSocket = clientSocket;

            this.receiver = new ObjectInputStream(clientSocket.getInputStream());
            this.sender = new ObjectOutputStream(clientSocket.getOutputStream());

            //this.inScanner = new Scanner(new InputStreamReader(this.playerSocket.getInputStream()));
            //this.outPrintWriter = new PrintWriter(this.playerSocket.getOutputStream(), true);
            //this.gameController = gameController;

            //Va fatto con oggetti
            //Chiedi username
            this.outPrintWriter.println("Insert your nickname: ");
            this.nickName = inScanner.nextLine();
            this.outPrintWriter.println("Welcome to Masters of Renaissance online!");
            this.outPrintWriter.println("Looking for a game in Florence...");

        } catch (Exception e) {
            e.printStackTrace();
            //Thread not working
            //Null this player
            return;
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

        //SWITCH THAT READS MESSAGES AND SENDS RESPONSED/DOES ACTIONS

        //SYNC PHASE
        try {
            NicknameMessage nicknameMessage = (NicknameMessage) this.receiver.readObject();
            this.nickName = nicknameMessage.getNickname();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Check this
        try {
            ServerStartingMessage serverStartingMessage = new ServerStartingMessage(
                    this.playerThreadNumber,
                    this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].getPlayerLeaderCards());
            sender.writeObject(serverStartingMessage);
        } catch (Exception e) {
            e.printStackTrace();
            //Errore
        }

        try {
            StartingResourcesMessage startingResourcesMessage = (StartingResourcesMessage) this.receiver.readObject();
            //Checks
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) receiver.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) receiver.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //QUI INIZIA LA FASE ASINCRONA
        //
        //SOPRA HO INIZIATO LA FASE SINCRONA
        //
        //VANNO MESSI I CONTROLLI, CHIAMATI E METODI, ECC
        //
        //QUA SOTTO C'è IL CICLO CON SWITCH FINCHè UNO NON VINCE
        //
        //

        while (true) {

            /*try {

                Object object = receiver.readObject();
                //Ci
                switch (receiver.readObject()) {
                    case (object):
                        break;
                    case (object instanceof DiscardLeaderMessage):
                        break;
                    case (object instanceof MarketResourcesMessage):
                        break;
                    case (object instanceof BuyCardMessage):
                        break;
                    case ()
                }
            } catch (Exception e) {
                break;
            }*/
            break;
        }


        //Async phase can start!!!
        try {
            //this.inScanner = new Scanner(new InputStreamReader(this.playerSocket.getInputStream()));
            //this.outPrintWriter = new PrintWriter(this.playerSocket.getOutputStream(), true);

            //this.outPrintWriter.println("New game started");

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
            //Disconnect current player
        }
    }
}
