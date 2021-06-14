package Communication.ServerSide;

import Maestri.MVC.GameController;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.*;
import Message.MessageReceived.*;
import Message.MessageSent.DiscardLeaderMessage;
import Message.MessageSent.EndTurnMessage;
import Message.MessageSent.PlayLeaderMessage;
import Message.MessageSent.QuitMessage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class PlayerThread implements Runnable {

    private String nickName;
    private int playerThreadNumber;

    private Socket playerSocket;
    private Scanner inScanner;
    private PrintWriter outPrintWriter;

    private ObjectInputStream receiver;
    private ObjectOutputStream sender;

    //The controller for this player
    private GameController gameController;

    private boolean mainAction = false;

    private int yourTurnMessageCounter = 0;

    //Il game controller va assegnato con setter siccome creato prima
    public PlayerThread(Socket clientSocket) {
        try {
            this.playerSocket = clientSocket;

            this.sender = new ObjectOutputStream(clientSocket.getOutputStream());
            this.receiver = new ObjectInputStream(clientSocket.getInputStream());

            //this.gameController = gameController;

        } catch (Exception e) {
            this.removePlayer();
            //Thread not working
            System.out.println("Thread not working");
            //Null this player
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

    public Scanner getInScanner() {
        return this.inScanner;
    }

    public Socket getPlayerSocket() { return this.playerSocket; }

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

    public ObjectOutputStream getSender() {
        return this.sender;
    }

    @Override
    public void run() {

        //SWITCH THAT READS MESSAGES AND SENDS RESPONSES/DOES ACTIONS

        System.out.println("PlayerThread started");

        Player currentPlayer = this.gameController.getGameModel().getPlayers()[this.playerThreadNumber];

        //SYNC PHASE
        try {
            NicknameMessage nicknameMessage = (NicknameMessage) this.receiver.readObject();
            this.nickName = nicknameMessage.getNickname();
            System.out.println("Nickname received");
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("No nickname");
            return;
        }


        //in fase sync messaggio singolo in fase async broadcast
        //FIRST MESSAGE TO SETUP CLIENT MARKET
        try {
            UpdateClientMarketMessage updateClientMarketMessage = new UpdateClientMarketMessage(this.gameController.getGameModel().getMarket());
            this.sender.writeObject(updateClientMarketMessage);
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("Non viene broadcastato mercato in PlayerThread");
            return;
        }

        //SECOND MESSAGE TO SETUP CLIENT MARKET
        try {
            UpdateClientDevCardGridMessage updateClientDevCardGridMessage = new UpdateClientDevCardGridMessage(this.gameController.getGameModel().getDevelopmentCardsDecksGrid());
            this.sender.writeObject(updateClientDevCardGridMessage);
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("Non viene broadcastata grid in PlayerThread");
            return;
        }

        try {
            ServerStartingMessage serverStartingMessage = new ServerStartingMessage(
                    this.playerThreadNumber, this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].getPlayerLeaderCards());
            this.sender.writeObject(serverStartingMessage);
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("No starting message");
            return;
        }


        try {
            StartingResourcesMessage startingResourcesMessage = (StartingResourcesMessage) this.receiver.readObject();
            while (!startingResourcesMessage.getStartingRes().isEmpty())
                currentPlayer.setStartingPlayerboard(startingResourcesMessage.getStartingRes().remove(0));
            //this.sender.writeObject( new ActionOutcomeMessage(true));
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("No starting resources message");
            return;
        }

        try {
            UpdateClientPlayerBoardMessage playerBoardMessage = new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard());
            this.sender.writeObject(playerBoardMessage);
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("Not playerBoard sent");
            return;
        }

        for(int cards=0; cards<2; cards++)
        {
            try {
                DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) this.receiver.readObject();
                currentPlayer.discardLeaderCard(discardLeaderMessage.getDiscarded());
                //this.sender.writeObject(new ActionOutcomeMessage(true));

            } catch (Exception e) {
                this.sendErrorMessage();
                this.removePlayer();
                System.out.println("No discard leader card message");
                return;
            }
        }

        try {
            this.sender.reset();
            UpdateClientLeaderCardsMessage leaderCardsMessage = new UpdateClientLeaderCardsMessage(currentPlayer.getPlayerLeaderCards());
            this.sender.writeObject(leaderCardsMessage);
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("Not leader cards sent");
            return;
        }

        try {
            if(this.playerThreadNumber==0)
            {
                System.out.println(this.playerThreadNumber);
                this.sender.writeObject(new YourTurnMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //BISOGNA ANCORA GESTIRE IL BREAK
        //ASYNC PHASE
        //I BROADCAST AVVENGONO SOLO IN QUESTA PHASE
        //Aggiornare aggiungendo i broadcast

        while (!this.gameController.getGameModel().checkEndPlay()) {

            try {
                this.sender.reset();
            } catch (Exception e) {
                this.sendErrorMessage();
                this.removePlayer();
                System.out.println("Reset sender not working");
            }

            Message object;
            this.mainAction=false;

            //Receive object
            //Check current player here
            try {
                object = (Message) this.receiver.readObject();
                if (this.gameController.getCurrentPlayerNumber() != object.getPlayerNumber())
                {
                    object = new NotYourTurnMessage();
                    this.sender.writeObject(object);
                    //this.yourTurnMessageCounter = 1;
                }
            } catch (Exception e) {
                this.sendErrorMessage();
                this.removePlayer();
                System.out.println("Error in receiving in PlayerThread");
                break;
            }

            //Controllare var ausiliaria
            //Controlla main action
            //int mainAction = 0;
            //boolean mainAction = false;

            //PlayLeaderMessage
            if (object instanceof PlayLeaderMessage) {
                try {
                    PlayLeaderMessage playLeaderMessage = (PlayLeaderMessage) object;

                    //First and only parameter is always an int that is the position of the leader card
                    int position = playLeaderMessage.getPlayed();

                    if(this.gameController.checkPlayCards(currentPlayer, position))
                        this.sender.writeObject(new UpdateClientLeaderCardsMessage(currentPlayer.getPlayerLeaderCards()));

                } catch (Exception e) {
                    this.sendErrorMessage();
                    this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread");
                    break;
                }
            }

            //DISCARD LEADER MESSAGE
            if (object instanceof DiscardLeaderMessage) {
                try {
                    DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) object;

                    //First and only parameter is always an int that is the position of the leader card
                    int position = discardLeaderMessage.getDiscarded();

                    if (this.gameController.checkDiscardCards(currentPlayer, position))
                        this.sender.writeObject(new UpdateClientLeaderCardsMessage(currentPlayer.getPlayerLeaderCards()));

                } catch (Exception e) {
                    this.sendErrorMessage();
                    this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread");
                    break;
                }
            }

            //MARKET RESOURCE MESSAGE
            if (object instanceof MarketResourcesMessage) {
                try {
                    MarketResourcesMessage marketResourcesMessage = (MarketResourcesMessage) object;

                    if(!this.mainAction)
                    {
                        //Row/column choice
                        String rowOrColumnChoice = marketResourcesMessage.getRowColumnChoice();
                        //Row/column index
                        int index = marketResourcesMessage.getIndex();
                        //Warehouse/leaderCard choice
                        String wlChoice = marketResourcesMessage.getWarehouseLeaderChoice();
                        //If he has 2 whiteMarbleLeaderCards
                        String chosenMarble = marketResourcesMessage.getWhichWhiteMarbleChoice();

                        //Qui invece che outcome vanno messi i broadcast
                        //IN MARKET, GRID E ACTIVATE PRODUCTION

                        if (this.gameController.checkMarketAction(currentPlayer, rowOrColumnChoice, index, wlChoice, chosenMarble)) {
                            UpdateClientMarketMessage updateMarket = new UpdateClientMarketMessage(this.gameController.getGameModel().getMarket());
                            this.gameController.broadcastMarket(updateMarket);
                            this.mainAction = true;
                        }
                    }

                } catch (Exception e) {
                    this.sendErrorMessage();
                    this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread");
                    break;
                }
            }

            //BUY DEVELOPMENT CARD
            if (object instanceof BuyCardMessage) {
                try {
                    BuyCardMessage buyCardMessage = (BuyCardMessage) object;

                    if(!this.mainAction)
                    {
                        //DevCard colour
                        int column = buyCardMessage.getColour();
                        //DevCard level
                        int level = buyCardMessage.getLevel();
                        //How much resources does the player spend
                        int[] quantity = buyCardMessage.getQuantity();
                        //From which shelf does the player pick resources
                        String[] deposit = buyCardMessage.getShelf();

                        if (this.gameController.checkBuyDevCard(currentPlayer, column, level, quantity, deposit)) {

                            int pos = buyCardMessage.getPlayerboardPosition();

                            if (currentPlayer.getPlayerBoard().isCardBelowCompatible(pos, this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0])) {
                                if (this.gameController.getGameModel().buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, level, pos, deposit)) {
                                    UpdateClientDevCardGridMessage updateClientDevCardGridMessage = new UpdateClientDevCardGridMessage(this.gameController.getGameModel().getDevelopmentCardsDecksGrid());
                                    this.gameController.broadcastDevCardsGrid(updateClientDevCardGridMessage);
                                    this.mainAction = true;
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    this.sendErrorMessage();
                    this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread");
                    break;
                }
            }

            //INPUT RESOURCE MESSAGE FOR PRODUCTION
            if (object instanceof ActivateProdMessage) {
                try {
                    ActivateProdMessage prodMessage = (ActivateProdMessage) object;

                    if(!this.mainAction)
                    {
                        int[] activation = new int[6];
                        String[] whichInput = new String[6];
                        int[] whichOutput = new int[3];

                        for (int k = 0; k < 6; k++) {
                            if (prodMessage.getInputs() != null) {
                                activation[k] = 1;
                                whichInput[k] = prodMessage.getInputs();
                                if (k >= 3) {
                                    whichOutput[k - 3] = Integer.parseInt(prodMessage.getOutputs());
                                }
                            } else {
                                activation[k] = 0;
                                whichInput[k] = "";
                                whichOutput[k] = -1;
                            }
                        }

                        if (this.gameController.checkActivateProduction(currentPlayer, activation, whichInput, whichOutput)) {
                            this.sender.writeObject(new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard()));
                            this.mainAction = true;
                        }
                    }


                } catch (Exception e) {
                    this.sendErrorMessage();
                    this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread");
                    break;
                }
            }

            if (object instanceof EndTurnMessage) {
                if(this.mainAction)
                {
                    try {
                        this.sender.writeObject(new TurnOverMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.mainAction = false;
                    this.gameController.nextCurrentPlayerNumber();
                    this.yourTurnMessageCounter = 0;
                }
                //Salva come giocatore corrente nel gamecontroller/gamemodel
                //il giocatore successivo a questo per abilitarlo e bloccare questo
            }

            if (object instanceof QuitMessage) {
                this.removePlayer();
                break;
            }

        }

        //Inviare messaggio dove si comunica vincitore + punti fatti
        try {
            this.sender.reset();
            //Ritorna numero player vincitore
            int winner = this.gameController.getGameModel().checkWinner();
            //Prende il nickname del vincitore
            String nicknameWinner = this.gameController.getGameModel().getPlayers()[winner].getNickname();
            //Punti vittoria di questo giocatore
            int victoryPoints = this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].sumAllVictoryPoints();
            GameOverMessage gameOverMessage = new GameOverMessage(nicknameWinner, victoryPoints);
            this.sender.writeObject(gameOverMessage);
            //this.removePlayer();
        } catch (Exception e) {
            this.sendErrorMessage();
            //e.printStackTrace();
            this.removePlayer();
            //System.err.println("Errore in mex game over");
        }

        //Il thread termina
        this.removePlayer();

    }

    public void removePlayer () {
        try {
            this.gameController.getGameModel().getPlayers()[this.playerThreadNumber] = null;
            this.sender.close();
            this.receiver.close();
            this.playerSocket.close();
        } catch (Exception e) {
            System.out.println("Rimosso giocatore");
            e.printStackTrace();
        }
    }

    public void sendErrorMessage() {
        try {
            this.sender.reset();
            this.sender.writeObject(new ServerErrorMessage());
        } catch (Exception e) {
            System.out.println("Error message");
            e.printStackTrace();
        }
    }

}
