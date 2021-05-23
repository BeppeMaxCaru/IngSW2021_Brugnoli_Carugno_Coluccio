package Communication.ServerSide;

import Maestri.MVC.GameController;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayerThread implements Runnable {

    private String nickName;
    private int playerThreadNumber;

    private int currentPlayerNumber;

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

    public void setCurrentPlayerNumber(int currentPlayerNumber) {
        this.currentPlayerNumber = currentPlayerNumber;
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

        Player currentPlayer = this.gameController.getGameModel().getPlayers()[this.playerThreadNumber];

        //SYNC PHASE
        try {
            NicknameMessage nicknameMessage = (NicknameMessage) this.receiver.readObject();
            this.nickName = nicknameMessage.getNickname();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ServerStartingMessage serverStartingMessage = new ServerStartingMessage(
                    this.playerThreadNumber, currentPlayer.getPlayerLeaderCards());
            sender.writeObject(serverStartingMessage);
        } catch (Exception e) {
            e.printStackTrace();
            //Errore
        }

        try {
            StartingResourcesMessage startingResourcesMessage = (StartingResourcesMessage) this.receiver.readObject();
            if(this.currentPlayerNumber == startingResourcesMessage.getPlayerNumber())
            {
                while (!startingResourcesMessage.getStartingRes().isEmpty())
                    currentPlayer.setStartingPlayerboard(startingResourcesMessage.getStartingRes().remove(0));
                this.sender.writeObject(true);
            } else this.sender.writeObject(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int cards=0; cards<2; cards++)
        {
            try {
                DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) receiver.readObject();
                if(this.currentPlayerNumber == discardLeaderMessage.getPlayerNumber())
                {
                    currentPlayer.discardLeaderCard(discardLeaderMessage.getDiscarded());
                    this.sender.writeObject(true);
                } else this.sender.writeObject(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

            try {

                Object object = receiver.readObject();
                //Ci
                PlayLeaderMessage playMessage;
                DiscardLeaderMessage discardMessage;
                MarketResourcesMessage marketMessage;
                BuyCardMessage buyMessage;
                InputResourceMessage activationMessage;

                if (object instanceof PlayLeaderMessage) {

                    playMessage = (PlayLeaderMessage) object;
                    if(this.currentPlayerNumber == playMessage.getPlayerNumber()) {

                        //First and only parameter is always an int that is the position of the leader card
                        int position = playMessage.getPlayed();

                        if (this.gameController.checkPlayCards(currentPlayer, position))
                            this.sender.writeObject(true);
                        else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

                if (object instanceof DiscardLeaderMessage) {

                    discardMessage = (DiscardLeaderMessage) object;
                    if(this.currentPlayerNumber == discardMessage.getPlayerNumber()) {

                        //First and only parameter is always an int that is the position of the leader card
                        int position = discardMessage.getDiscarded();

                        if (this.gameController.checkDiscardCards(currentPlayer, position))
                            this.sender.writeObject(true);
                        else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

                if (object instanceof MarketResourcesMessage) {

                    marketMessage = (MarketResourcesMessage) object;
                    if(this.currentPlayerNumber == marketMessage.getPlayerNumber()) {

                        //Row/column choice
                        String rowOrColumnChoice = marketMessage.getRowColumnChoice();
                        //Row/column index
                        int index = marketMessage.getIndex();
                        //Warehouse/leaderCard choice
                        String wlChoice = marketMessage.getWarehouseLeaderChoice();
                        //If he has 2 whiteMarbleLeaderCards
                        String chosenMarble = marketMessage.getWhichWhiteMarbleChoice();

                        if (this.gameController.checkMarketAction(currentPlayer, rowOrColumnChoice, index, wlChoice, chosenMarble))
                            this.sender.writeObject(true);
                        else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

                if (object instanceof BuyCardMessage) {

                    buyMessage = (BuyCardMessage) object;

                    if(this.currentPlayerNumber == buyMessage.getPlayerNumber()) {

                        //DevCard colour
                        String colour = buyMessage.getColour();
                        int column = this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsColours().get(colour.toUpperCase());
                        //DevCard level
                        int level = 3 - buyMessage.getLevel();
                        //How much resources does the player spend
                        int[] quantity = buyMessage.getQuantity();
                        //From which shelf does the player pick resources
                        String[] deposit = buyMessage.getShelf();

                        if(this.gameController.checkBuyDevCard(currentPlayer, colour, level, quantity, deposit))
                        {
                            ArrayList<Integer> correctPositions = new ArrayList<>();

                            for (int pos=0; pos<3; pos++)
                                if(currentPlayer.getPlayerBoard().isCardBelowCompatible(pos, this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0]))
                                    correctPositions.add(pos);

                            if (correctPositions.size() == 0)
                                this.sender.writeObject(false);
                            else {
                                this.sender.writeObject(true);

                                ServerCardAvailabilityMessage availabilityMessage = new ServerCardAvailabilityMessage(correctPositions);
                                this.sender.writeObject(availabilityMessage);
                                this.sender.close();

                                DevCardPositionMessage positionMessage = (DevCardPositionMessage) this.receiver.readObject();

                                if (this.gameController.getGameModel().buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, level, positionMessage.getCardPosition(), deposit))
                                    this.sender.writeObject(true);
                                else this.sender.writeObject(false);
                            }
                        } else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

                if (object instanceof InputResourceMessage) {

                    activationMessage = (InputResourceMessage) object;

                    if(this.currentPlayerNumber == activationMessage.getPlayerNumber()) {

                        int[] activation = new int[6];
                        String[] whichInput = new String[6];
                        int[] whichOutput = new int[3];

                        for(int k = 0; k < 6; k++) {
                            if (activationMessage.getInputs() != null) {
                                activation[k]=1;
                                whichInput[k] = activationMessage.getInputs();
                                if(k >= 3) {
                                    OutputChoiceResourceMessage messageOutput = (OutputChoiceResourceMessage) this.receiver.readObject();
                                    whichOutput[k - 3] = Integer.parseInt(messageOutput.getResource());
                                }
                            } else activation[k]=0;
                        }

                        if(this.gameController.checkActivateProduction(currentPlayer, activation, whichInput, whichOutput))
                            this.sender.writeObject(true);
                        else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

            } catch (Exception e) {
                break;
            }
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
