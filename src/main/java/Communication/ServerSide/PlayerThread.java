package Communication.ServerSide;

import Maestri.MVC.GameController;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.*;
import Message.MessageReceived.*;
import Message.MessageSent.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerThread implements Runnable {

    private String nickName;
    private int playerThreadNumber;

    private Socket playerSocket;

    private ObjectInputStream receiver;
    private ObjectOutputStream sender;

    //The controller for this player
    private GameController gameController;

    private boolean mainAction = false;

    //private int yourTurnMessageCounter = 0;

    //Il game controller va assegnato con setter siccome creato prima
    public PlayerThread(Socket clientSocket) {
        try {
            this.playerSocket = clientSocket;

            this.sender = new ObjectOutputStream(clientSocket.getOutputStream());
            this.receiver = new ObjectInputStream(clientSocket.getInputStream());

            //this.gameController = gameController;

        } catch (Exception e) {
            //this.removePlayer();
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

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public ObjectOutputStream getSender() {
        return this.sender;
    }

    @Override
    public void run() {

        //SWITCH THAT READS MESSAGES AND SENDS RESPONSES/DOES ACTIONS

        //System.out.println("PlayerThread started");

        Player currentPlayer = this.gameController.getGameModel().getPlayers()[this.playerThreadNumber];

        //Pinger
        //Timer timer = new Timer();
        //timer.schedule();

        //this.ping();

        //SYNC PHASE
        try {
            this.playerSocket.setSoTimeout(120000);
            NicknameMessage nicknameMessage = (NicknameMessage) this.receiver.readObject();
            this.nickName = nicknameMessage.getNickname();
            this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].setNickname(this.nickName);
            //System.out.println("Nickname received " + this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].getNickname());
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("No nickname");
            return;
        }

        //this.ping();

        //in fase sync messaggio singolo in fase async broadcast
        //FIRST MESSAGE TO SETUP CLIENT MARKET
        try {
            UpdateClientMarketMessage updateClientMarketMessage = new UpdateClientMarketMessage(this.gameController.getGameModel().getMarket());
            this.sender.writeObject(updateClientMarketMessage);
            //System.out.println("Market sent");
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("Non viene broadcastato mercato in PlayerThread");
            return;
        }

        //this.ping();
        /*try {
            this.playerSocket.setSoTimeout(10000);
        } catch (Exception e) {
            System.out.println("Timeout scaduto");
        }*/

        //SECOND MESSAGE TO SETUP CLIENT MARKET
        try {
            UpdateClientDevCardGridMessage updateClientDevCardGridMessage = new UpdateClientDevCardGridMessage(this.gameController.getGameModel().getDevelopmentCardsDecksGrid());
            this.sender.writeObject(updateClientDevCardGridMessage);
            //System.out.println("DevCards sent");
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("Non viene broadcastata grid in PlayerThread");
            return;
        }

        //this.ping();

        try {
            ServerStartingMessage serverStartingMessage = new ServerStartingMessage(
                    this.playerThreadNumber, this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].getPlayerLeaderCards());
            this.sender.writeObject(serverStartingMessage);
            //System.out.println("Player number and leaders sent");
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("No starting message");
            return;
        }

        //this.ping();

        try {
            this.playerSocket.setSoTimeout(120000);
            StartingResourcesMessage startingResourcesMessage = (StartingResourcesMessage) this.receiver.readObject();
            while (!startingResourcesMessage.getStartingRes().isEmpty())
                currentPlayer.setStartingPlayerboard(startingResourcesMessage.getStartingRes().remove(0));
            //this.sender.writeObject( new ActionOutcomeMessage(true));
            //System.out.println("Starting res received");
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("No starting resources message");
            return;
        }

        //this.ping();

        try {
            UpdateClientPlayerBoardMessage playerBoardMessage = new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard());
            this.sender.writeObject(playerBoardMessage);
            //System.out.println("Playerboard sent");
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("Not playerBoard sent");
            return;
        }

        //this.ping();

        for(int cards=0; cards<2; cards++)
        {
            try {
                this.playerSocket.setSoTimeout(120000);
                DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) this.receiver.readObject();
                currentPlayer.discardLeaderCard(discardLeaderMessage.getDiscarded());
                //this.playerSocket.setSoTimeout(0);
                //this.sender.writeObject(new ActionOutcomeMessage(true));
                //System.out.println("Leader received");

            } catch (Exception e) {
                this.sendErrorMessage();
                this.removePlayer();
                e.printStackTrace();
                System.out.println("No discard leader card message");
                return;
            }
        }

        //this.ping();

        try {
            this.sender.reset();
            UpdateClientLeaderCardsMessage leaderCardsMessage = new UpdateClientLeaderCardsMessage(currentPlayer.getPlayerLeaderCards());
            this.sender.writeObject(leaderCardsMessage);
            //System.out.println("Leaders sent");
        } catch (Exception e) {
            this.sendErrorMessage();
            this.removePlayer();
            System.out.println("Not leader cards sent");
            return;
        }

        //this.ping();

        try {
            if(this.playerThreadNumber==0)
            {
                System.out.println("Turn of player " + this.playerThreadNumber);
                this.sender.writeObject(new YourTurnMessage());
                //this.playerSocket.setSoTimeout(10000);
            } else {
                this.sender.writeObject(new TurnOverMessage());
                //this.playerSocket.setSoTimeout(0);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            this.removePlayer();
        }


        try {
            this.playerSocket.setSoTimeout(0);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //this.playerSocket.setSoTimeout(10000);

        //BISOGNA ANCORA GESTIRE IL BREAK
        //ASYNC PHASE
        //I BROADCAST AVVENGONO SOLO IN QUESTA PHASE
        //Aggiornare aggiungendo i broadcast

        while (!this.gameController.getGameModel().checkEndPlay()) {

            this.ping();

            try {
                this.sender.reset();
            } catch (Exception e) {
                this.sendErrorMessage();
                //this.removePlayer();
                System.out.println("Reset sender not working");
                break;
            }

            if (this.gameController.getCurrentPlayerNumber() == this.playerThreadNumber) {
                try {
                    this.playerSocket.setSoTimeout(120000);
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("Player inactive [PlayerThread: line 264]");
                    //Lo espello
                    break;
                }
            } else if (this.gameController.getCurrentPlayerNumber() != this.playerThreadNumber) {
                try {
                    this.playerSocket.setSoTimeout(0);
                } catch (Exception e) {
                    //e.printStackTrace();
                    break;
                }
            }

            Message object;

            //Receive object
            //Check current player here
            try {
                object = (Message) this.receiver.readObject();
                System.out.println("Received message from player " + object.getPlayerNumber());
                if (this.gameController.getCurrentPlayerNumber() != object.getPlayerNumber())
                {
                    object = new NotYourTurnMessage();
                    this.sender.writeObject(object);
                    //this.yourTurnMessageCounter = 1;
                }
            } catch (Exception e) {
                this.sendErrorMessage();
                //this.removePlayer();
                //e.printStackTrace();
                System.out.println("Error in receiving in PlayerThread or current player inactive [PlayerThread: line 293]");
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
                    System.out.println("Play leader received");
                    //First and only parameter is always an int that is the position of the leader card
                    int position = playLeaderMessage.getPlayed();

                    if(this.gameController.checkPlayCards(currentPlayer, position))
                    {
                        this.sender.writeObject(new UpdateClientLeaderCardsMessage(currentPlayer.getPlayerLeaderCards()));
                        System.out.println("Leaders sent");
                    }

                } catch (Exception e) {
                    this.sendErrorMessage();
                    //this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread (play leader)");
                    break;
                }
            }

            //DISCARD LEADER MESSAGE
            if (object instanceof DiscardLeaderMessage) {
                try {
                    DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) object;
                    System.out.println("Discard leader received");
                    //First and only parameter is always an int that is the position of the leader card
                    int position = discardLeaderMessage.getDiscarded();

                    if (this.gameController.checkDiscardCards(currentPlayer, position))
                    {
                        this.sender.writeObject(new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard()));
                        System.out.println("Playerboard sent");
                        this.sender.writeObject(new UpdateClientLeaderCardsMessage(currentPlayer.getPlayerLeaderCards()));
                        System.out.println("Leaders sent");
                    }

                } catch (Exception e) {
                    this.sendErrorMessage();
                    //this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread (discard leader)");
                    break;
                }
            }

            //MARKET RESOURCE MESSAGE
            if (object instanceof MarketResourcesMessage) {
                try {
                    MarketResourcesMessage marketResourcesMessage = (MarketResourcesMessage) object;
                    System.out.println("Market action received");
                    if(!this.mainAction)
                    {
                        //Row/column choice
                        String rowOrColumnChoice = marketResourcesMessage.getRowColumnChoice();
                        //System.out.println(rowOrColumnChoice);
                        //Row/column index
                        int index = marketResourcesMessage.getIndex();
                        //System.out.println(index);
                        //Warehouse/leaderCard choice
                        String wlChoice = marketResourcesMessage.getWarehouseLeaderChoice();
                        //System.out.println(wlChoice);
                        //If he has 2 whiteMarbleLeaderCards
                        String chosenMarble = marketResourcesMessage.getWhichWhiteMarbleChoice();
                        //System.out.println(chosenMarble);

                        //Qui invece che outcome vanno messi i broadcast
                        //IN MARKET, GRID E ACTIVATE PRODUCTION

                        if (this.gameController.checkMarketAction(currentPlayer, rowOrColumnChoice, index, wlChoice, chosenMarble)) {
                            this.sender.writeObject(new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard()));
                            //this.gameController.broadcastPlayerBoards();
                            //System.out.println("Playerboard sent");
                            UpdateClientMarketMessage updateMarket = new UpdateClientMarketMessage(this.gameController.getGameModel().getMarket());
                            this.gameController.broadcastMarket(updateMarket);

                            this.mainAction = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.sendErrorMessage();
                    //this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread (market action)");
                    break;
                }
            }

            Map<Integer, String> resources = new HashMap<>();
            resources.put(0, "COINS");
            resources.put(1, "SERVANTS");
            resources.put(2, "SHIELDS");
            resources.put(3, "STONES");
            //BUY DEVELOPMENT CARD
            if (object instanceof BuyCardMessage) {
                try {
                    BuyCardMessage buyCardMessage = (BuyCardMessage) object;
                    System.out.println("Buy card received");
                    if(!this.mainAction)
                    {
                        //DevCard colour
                        int column = buyCardMessage.getColour();
                        //System.out.println("column "+column);
                        //DevCard level
                        int level = buyCardMessage.getLevel();
                        int row = 3-level;
                        //System.out.println("level "+level);
                        //How much resources does the player spend
                        int[] quantity = buyCardMessage.getQuantity();
                        //From which shelf does the player pick resources
                        String[] deposit = buyCardMessage.getShelf();
                        /*for(int i =0; i<4; i++){
                            //System.out.println(resources.get(i));
                            //System.out.println(quantity[i]);
                            //System.out.println(deposit[i]);
                            }
                         */

                        if (this.gameController.checkBuyDevCard(currentPlayer, column, level, quantity, deposit)) {

                            int pos = buyCardMessage.getPlayerboardPosition();
                            //System.out.println("Put in position "+pos);

                            if (currentPlayer.getPlayerBoard().isCardBelowCompatible(pos, this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[row][column][0])) {
                                if (this.gameController.getGameModel().buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, level, pos, deposit)) {
                                    this.sender.writeObject(new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard()));
                                    System.out.println("Playerboard sent");
                                    UpdateClientDevCardGridMessage updateClientDevCardGridMessage = new UpdateClientDevCardGridMessage(this.gameController.getGameModel().getDevelopmentCardsDecksGrid());
                                    this.gameController.broadcastDevCardsGrid(updateClientDevCardGridMessage);
                                    System.out.println("DevCards sent");
                                    this.mainAction = true;
                                } else System.out.println("Not valid model action");
                            } else System.out.println("Not valid isCardBelowCompatible");
                        } else System.out.println("Not valid controller check");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.sendErrorMessage();
                    //this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread (buy card action)");
                    break;
                }
            }

            //INPUT RESOURCE MESSAGE FOR PRODUCTION
            if (object instanceof ActivateProdMessage) {
                try {
                    ActivateProdMessage prodMessage = (ActivateProdMessage) object;
                    System.out.println("Activate prod received");

                    int[] activation;
                    String[] whichInput;
                    int[] whichOutput;

                    if (!this.mainAction) {
                        activation=prodMessage.getActivation();
                        whichInput=prodMessage.getInputs();
                        whichOutput=prodMessage.getOutputs();

                        /*
                        for (int k = 0; k < 6; k++) {
                            System.out.println("Activation power n."+k+": "+activation[k]);
                            System.out.println(whichInput[k]);
                            if (k >= 3) {
                                System.out.println(whichOutput[k - 3]);
                            }
                        }
                         */
                        if (this.gameController.checkActivateProduction(currentPlayer, activation, whichInput, whichOutput)) {
                            this.sender.writeObject(new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard()));
                            System.out.println("Playerboard sent");
                            this.mainAction = true;
                        } else System.out.println("Not valid controller check");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    this.sendErrorMessage();
                    //this.removePlayer();
                    System.out.println("Error in receiving in PlayerThread (activate prod)");
                    break;
                }
            }

            if (object instanceof EndTurnMessage) {
                if(this.mainAction)
                {
                    try {
                        //this.playerSocket.setSoTimeout(0);
                        this.sender.writeObject(new TurnOverMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.mainAction = false;
                    this.gameController.nextCurrentPlayerNumber();
                    //this.yourTurnMessageCounter = 0;
                } else System.out.println("No main action valid");
                //Salva come giocatore corrente nel gamecontroller/gamemodel
                //il giocatore successivo a questo per abilitarlo e bloccare questo
            }

            if (object instanceof QuitMessage) {
                //this.removePlayer();
                this.gameController.nextCurrentPlayerNumber();
                break;
            }

        }

        //Inviare messaggio dove si comunica vincitore + punti fatti
        try {
            this.sender.reset();
            //Ritorna numero player vincitore
            int winner = this.gameController.getGameModel().checkWinner();

            System.out.println(winner);
            String nickNameWinner = this.gameController.getGameModel().getPlayers()[winner].getNickname();
            //Punti vittoria di questo giocatore
            int victoryPoints = this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].sumAllVictoryPoints();
            GameOverMessage gameOverMessage = new GameOverMessage(nickNameWinner, victoryPoints);
            this.sender.writeObject(gameOverMessage);
            //this.removePlayer();
        } catch (Exception e) {
            this.sendErrorMessage();
            e.printStackTrace();
            //this.removePlayer();
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
            System.out.println("Giocatore staccato");
            //Move to net active player
            this.gameController.nextCurrentPlayerNumber();
            System.out.println(this.gameController.getCurrentPlayerNumber());
        } catch (Exception e) {
            System.out.println("Player removed and moved to the next one");
            e.printStackTrace();
        }
    }

    public void sendErrorMessage() {
        try {
            this.sender.reset();
            this.sender.writeObject(new ServerErrorMessage());
        } catch (Exception e) {
            System.out.println("Sending error message exception");
            //e.printStackTrace();
        }
    }

    private void ping() {
        try {
            this.sender.reset();
            this.sender.writeObject(new PingMessage());
        } catch (Exception e) {
            this.removePlayer();
            System.out.println("Player lost");
        }
    }

}
