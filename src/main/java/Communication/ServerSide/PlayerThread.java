package Communication.ServerSide;

import Maestri.MVC.GameController;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.*;
import Message.MessageReceived.*;
import Message.MessageSent.*;

import java.io.*;
import java.net.Socket;

/**
 * Client equivalent on server
 */
public class PlayerThread implements Runnable {

    /**
     * The nickname of the player thread
     */
    private String nickName;

    /**
     * The number assigned
     */
    private int playerThreadNumber;

    /**
     * The socket that keeps the connection
     */
    private Socket playerSocket;

    /**
     * Receives the messages
     */
    private ObjectInputStream receiver;

    /**
     * Sends the messages
     */
    private ObjectOutputStream sender;

    //The controller for this player
    /**
     * Interacts with the game model
     */
    private GameController gameController;

    /**
     * Checks if a main action has been performed
     */
    private boolean mainAction = false;

    //private int yourTurnMessageCounter = 0;

    //Il game controller va assegnato con setter siccome creato prima

    /**
     * Builds the player thread
     * @param clientSocket The socket used for the connection
     */
    public PlayerThread(Socket clientSocket) {
        try {
            this.playerSocket = clientSocket;

            this.sender = new ObjectOutputStream(clientSocket.getOutputStream());
            this.receiver = new ObjectInputStream(clientSocket.getInputStream());

            //this.gameController = gameController;

        } catch (Exception e) {
            //this.removePlayer();
            //Thread not working
            //System.out.println("Thread not working");
            //Null this player
        }
    }

    /**
     * Returns the nickname
     * @return the nickname
     */
    public String getNickName() {
        return this.nickName;
    }

    /**
     * Sets the nickname
     * @param nickName The nickname to set
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Returns the thread number
     * @return the thread number
     */
    public int getPlayerThreadNumber() {
        return this.playerThreadNumber;
    }

    /**
     * Sets the thread number
     * @param playerThreadNumber Thread number to be set
     */
    public void setPlayerThreadNumber(int playerThreadNumber) {
        this.playerThreadNumber = playerThreadNumber;
    }

    /**
     * Returns the game controller assigned to this player thread
     * @return the game controller assigned to this player thread
     */
    public GameController getGameController() {
        return this.gameController;
    }

    /**
     * Assigns the game controller to the thread
     * @param gameController Game controller to be assigned
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Returns the messages sender
     * @return the messages sender
     */
    public ObjectOutputStream getSender() {
        return this.sender;
    }

    @Override
    public void run() {

        //SWITCH THAT READS MESSAGES AND SENDS RESPONSES/DOES ACTIONS

        Player currentPlayer = this.gameController.getGameModel().getPlayers()[this.playerThreadNumber];

        //this.ping();

        //SYNC PHASE
        try {
            this.playerSocket.setSoTimeout(90000);
            NicknameMessage nicknameMessage = (NicknameMessage) this.receiver.readObject();
            this.nickName = nicknameMessage.getNickname();
            this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].setNickname(this.nickName + " #" + this.playerThreadNumber);
            //System.out.println("Nickname received " + this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].getNickname());
        } catch (Exception e) {
            this.sendErrorMessage("Error while starting setup");
            this.gameController.nextCurrentPlayerNumber();
            this.removePlayer();
            //System.out.println("No nickname");
            return;
        }

        //FIRST MESSAGE TO SETUP CLIENT MARKET
        try {
            UpdateClientMarketMessage updateClientMarketMessage = new UpdateClientMarketMessage(this.gameController.getGameModel().getMarket());
            this.sender.writeObject(updateClientMarketMessage);
            //System.out.println("Market sent");
        } catch (Exception e) {
            this.sendErrorMessage("Online error while sending market");
            this.removePlayer();
            this.gameController.nextCurrentPlayerNumber();
            //System.out.println("Non viene broadcastato mercato in PlayerThread");
            return;
        }

        //SECOND MESSAGE TO SETUP CLIENT MARKET
        try {
            UpdateClientDevCardGridMessage updateClientDevCardGridMessage = new UpdateClientDevCardGridMessage(this.gameController.getGameModel().getDevelopmentCardsDecksGrid());
            this.sender.writeObject(updateClientDevCardGridMessage);
            //System.out.println("DevCards sent");
        } catch (Exception e) {
            this.sendErrorMessage("Online error while sending development cards grid");
            this.removePlayer();
            this.gameController.nextCurrentPlayerNumber();
            //System.out.println("Non viene broadcastata grid in PlayerThread");
            return;
        }

        try {
            ServerStartingMessage serverStartingMessage = new ServerStartingMessage(
                    this.playerThreadNumber, this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].getPlayerLeaderCards());
            this.sender.writeObject(serverStartingMessage);
            //System.out.println("Player number and leaders sent");
        } catch (Exception e) {
            this.sendErrorMessage("Online error while sending your starting leader cards");
            this.removePlayer();
            this.gameController.nextCurrentPlayerNumber();
            //System.out.println("No starting message");
            return;
        }

        try {
            this.playerSocket.setSoTimeout(90000);
            StartingResourcesMessage startingResourcesMessage = (StartingResourcesMessage) this.receiver.readObject();
            while (!startingResourcesMessage.getStartingRes().isEmpty())
                currentPlayer.setStartingPlayerBoard(startingResourcesMessage.getStartingRes().remove(0));
            //this.sender.writeObject( new ActionOutcomeMessage(true));
            //System.out.println("Starting res received");
        } catch (Exception e) {
            this.sendErrorMessage("Online error during starting resources setup");
            this.removePlayer();
            this.gameController.nextCurrentPlayerNumber();
            //System.out.println("No starting resources message");
            return;
        }

        try {
            UpdateClientPlayerBoardMessage playerBoardMessage = new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard());
            this.sender.writeObject(playerBoardMessage);
            //System.out.println("Playerboard sent");
        } catch (Exception e) {
            this.sendErrorMessage("Online error while preparing your player board");
            this.removePlayer();
            this.gameController.nextCurrentPlayerNumber();
            //System.out.println("Not playerBoard sent");
            return;
        }

        for(int cards=0; cards<2; cards++)
        {
            try {
                this.playerSocket.setSoTimeout(90000);

                DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) this.receiver.readObject();
                currentPlayer.discardLeaderCard(discardLeaderMessage.getDiscarded());
                //System.out.println(Arrays.toString(this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].getPlayerLeaderCards()));

                this.sender.reset();
                this.sender.writeObject(new UpdateClientLeaderCardsMessage(currentPlayer.getPlayerLeaderCards()));

            } catch (Exception e) {
                this.sendErrorMessage("Online error during starting leader cards setup");
                this.removePlayer();
                this.gameController.nextCurrentPlayerNumber();
                //e.printStackTrace();
                //System.out.println("No discard leader card message");
                return;
            }
        }

        try {
            this.playerSocket.setSoTimeout(0);
            this.sender.reset();
            this.sender.writeObject(new TurnOverMessage("SETUP"));
        } catch (Exception e) {
            //e.printStackTrace();
            this.sendErrorMessage("Error during setup");
            this.removePlayer();
            this.gameController.nextCurrentPlayerNumber();
            return;
        }

        while (!this.gameController.checkSetupEnd());

        try {
            this.sender.reset();
            if(this.playerThreadNumber==0)
            {
                System.out.println("Turn of player " + this.playerThreadNumber);
                this.sender.writeObject(new YourTurnMessage());
                //this.playerSocket.setSoTimeout(10000);
            } else {
                this.sender.writeObject(new TurnOverMessage("TURN"));
                //this.playerSocket.setSoTimeout(0);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            this.removePlayer();
            this.gameController.nextCurrentPlayerNumber();
            return;
        }

        //Checks if there is either only one player left or someone has won
        while (!this.gameController.getGameModel().checkEndPlay()) {

            //this.ping();
            //Notifies to clients server actvity after every message
            this.gameController.broadcastPing();

            try {
                this.sender.reset();
            } catch (Exception e) {
                this.sendErrorMessage("Online game updates not working");
                this.removePlayer();
                this.gameController.nextCurrentPlayerNumber();
                //System.out.println("Reset sender not working");
                return;
            }

            if (this.gameController.getCurrentPlayerNumber() == this.playerThreadNumber) {
                try {
                    this.playerSocket.setSoTimeout(120000);
                } catch (Exception e) {
                    //e.printStackTrace();
                    this.sendErrorMessage("Expelled for inactivity");
                    //System.out.println("Player inactive");
                    this.removePlayer();
                    this.gameController.nextCurrentPlayerNumber();
                    return;
                }
            } else if (this.gameController.getCurrentPlayerNumber() != this.playerThreadNumber) {
                try {
                    this.playerSocket.setSoTimeout(0);
                } catch (Exception e) {
                    //e.printStackTrace();
                    this.removePlayer();
                    this.gameController.nextCurrentPlayerNumber();
                    return;
                }
            }

            Message object;

            //Receive object
            //Check current player here
            try {
                object = (Message) this.receiver.readObject();
                //System.out.println("Received message from player " + object.getPlayerNumber());
                if (this.gameController.getCurrentPlayerNumber() != object.getPlayerNumber())
                {
                    object = new NotYourTurnMessage();
                    this.sender.writeObject(object);
                    //this.yourTurnMessageCounter = 1;
                }
            } catch (Exception e) {
                this.sendErrorMessage("Online error during action choice");
                //this.removePlayer();
                //e.printStackTrace();
                //System.out.println("Error in receiving in PlayerThread or current player inactive [PlayerThread: line 293]");
                this.removePlayer();
                this.gameController.nextCurrentPlayerNumber();
                return;
            }

            //PlayLeaderMessage
            if (object instanceof PlayLeaderMessage) {
                try {
                    PlayLeaderMessage playLeaderMessage = (PlayLeaderMessage) object;
                    //System.out.println("Play leader received");
                    //First and only parameter is always an int that is the position of the leader card
                    int position = playLeaderMessage.getPlayed();

                    if(this.gameController.checkPlayCards(currentPlayer, position))
                    {
                        this.sender.writeObject(new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard()));
                        //System.out.println("Playerboard sent");
                        this.sender.writeObject(new UpdateClientLeaderCardsMessage(currentPlayer.getPlayerLeaderCards()));
                        //System.out.println("Leaders sent");

                    }

                } catch (Exception e) {
                    this.sendErrorMessage("Online error while playing leader card");
                    //this.removePlayer();
                    //System.out.println("Error in receiving in PlayerThread (play leader)");
                    this.removePlayer();
                    this.gameController.nextCurrentPlayerNumber();
                    return;
                }
            }

            //DISCARD LEADER MESSAGE
            if (object instanceof DiscardLeaderMessage) {
                try {
                    DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) object;
                    //System.out.println("Discard leader received");
                    //First and only parameter is always an int that is the position of the leader card
                    int position = discardLeaderMessage.getDiscarded();

                    if (this.gameController.checkDiscardCards(currentPlayer, position))
                    {
                        this.sender.writeObject(new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard()));
                        //System.out.println("Playerboard sent");
                        this.sender.writeObject(new UpdateClientLeaderCardsMessage(currentPlayer.getPlayerLeaderCards()));
                        //System.out.println("Leaders sent");

                    }

                    //System.out.println(Arrays.toString(this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].getPlayerLeaderCards()));

                } catch (Exception e) {
                    this.sendErrorMessage("Online error while discarding leader card");
                    //this.removePlayer();
                    //System.out.println("Error in receiving in PlayerThread (discard leader)");
                    this.removePlayer();
                    this.gameController.nextCurrentPlayerNumber();
                    return;
                }
            }

            //MARKET RESOURCE MESSAGE
            if (object instanceof MarketResourcesMessage) {
                try {
                    MarketResourcesMessage marketResourcesMessage = (MarketResourcesMessage) object;
                    //System.out.println("Market action received");
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

                        //IN MARKET, GRID & ACTIVATE PRODUCTION

                        if (this.gameController.checkMarketAction(currentPlayer, rowOrColumnChoice, index, wlChoice, chosenMarble)) {

                            //System.out.println("Playerboard sent");
                            this.gameController.broadCastMarketUpdated();
                            this.gameController.broadcastPlayerBoards();

                            this.mainAction = true;
                        }
                    }

                } catch (Exception e) {
                    //e.printStackTrace();
                    this.sendErrorMessage("Online error while drawing from market");
                    //this.removePlayer();
                    //System.out.println("Error in receiving in PlayerThread (market action)");
                    this.removePlayer();
                    this.gameController.nextCurrentPlayerNumber();
                    return;
                }
            }

            //BUY DEVELOPMENT CARD
            if (object instanceof BuyCardMessage) {
                try {
                    BuyCardMessage buyCardMessage = (BuyCardMessage) object;
                    //System.out.println("Buy card received");
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

                        if (this.gameController.checkBuyDevCard(currentPlayer, column, level, quantity, deposit)) {

                            int pos = buyCardMessage.getPlayerboardPosition();
                            //System.out.println("Put in position "+pos);

                            if (this.gameController.getGameModel().buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, level, pos, deposit)) {

                                this.sender.reset();
                                this.sender.writeObject(new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard()));
                                this.gameController.broadcastDevCardGridUpdated();

                                //System.out.println("DevCards sent");
                                this.mainAction = true;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.sendErrorMessage("Online error while buying a development card");
                    //this.removePlayer();
                    //System.out.println("Error in receiving in PlayerThread (buy card action)");
                    this.removePlayer();
                    this.gameController.nextCurrentPlayerNumber();
                    return;
                }
            }

            //INPUT RESOURCE MESSAGE FOR PRODUCTION
            if (object instanceof ActivateProdMessage) {
                try {
                    ActivateProdMessage prodMessage = (ActivateProdMessage) object;
                    //System.out.println("Activate prod received");

                    int[] activation;
                    String[] whichInput;
                    int[] whichOutput;

                    if (!this.mainAction) {
                        activation=prodMessage.getActivation();
                        whichInput=prodMessage.getInputs();
                        whichOutput=prodMessage.getOutputs();

                        if (this.gameController.checkActivateProduction(currentPlayer, activation, whichInput, whichOutput)) {
                            this.sender.reset();
                            this.sender.writeObject(new UpdateClientPlayerBoardMessage(currentPlayer.getPlayerBoard()));
                            //System.out.println("PlayerBoard sent");
                            this.mainAction = true;
                        } //else System.out.println("Not valid controller check");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    this.sendErrorMessage("Server error during activate production");
                    //this.removePlayer();
                    //System.out.println("Error in receiving in PlayerThread (activate prod)");
                    this.removePlayer();
                    this.gameController.nextCurrentPlayerNumber();
                    return;
                }
            }

            if (object instanceof EndTurnMessage) {
                if(this.mainAction)
                {
                    try {
                        //this.playerSocket.setSoTimeout(0);
                        this.sender.reset();
                        this.sender.writeObject(new TurnOverMessage("TURN"));
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                    this.mainAction = false;
                    this.gameController.getGameModel().relationWithVatican();
                    this.gameController.nextCurrentPlayerNumber();
                    //this.yourTurnMessageCounter = 0;
                } //else System.out.println("No main action valid");

            }

            if (object instanceof QuitMessage) {
                this.removePlayer();
                this.gameController.nextCurrentPlayerNumber();
                return;
            }

        }

        try {
            this.playerSocket.setSoTimeout(0);
            this.sender.writeObject(new TurnOverMessage("TURN"));
            if (this.playerThreadNumber != this.gameController.findLastPlayer()) {
                this.gameController.nextCurrentPlayerNumber();
            } else
                //If it is the last
                {
                this.sender.reset();
                int winner = this.gameController.getGameModel().checkWinner();
                String nickNameWinner = this.gameController.getGameModel().getPlayers()[winner].getNickname();
                //Get this player victory points
                int victoryPoints = this.gameController.getGameModel().getPlayers()[this.playerThreadNumber].sumAllVictoryPoints();
                GameOverMessage gameOverMessage = new GameOverMessage(nickNameWinner, victoryPoints);
                this.gameController.broadcastGameOver(gameOverMessage);

            }
        } catch (Exception e) {
            this.removePlayer();
            this.gameController.nextCurrentPlayerNumber();
            //e.printStackTrace();
        }

    }

    /**
     * Removes a player from the game
     */
    public void removePlayer () {
        try {
            this.gameController.getGameModel().getPlayers()[this.playerThreadNumber] = null;
            this.gameController.getPlayerThreads().remove(this);
            this.sender.close();
            this.receiver.close();
            this.playerSocket.close();

        } catch (Exception e) {
            //System.out.println("Player removed and moved to the next one");
            //e.printStackTrace();
        }
    }

    /**
     * Sends an error message
     * @param errorMessage Error message to show
     */
    public void sendErrorMessage(String errorMessage) {
        try {
            this.sender.reset();
            this.sender.writeObject(new ServerErrorMessage(errorMessage));
        } catch (Exception e) {
            //System.out.println("Sending error message exception");
            //e.printStackTrace();
        }
    }

    private void ping() {
        try {
            this.sender.reset();
            this.sender.writeObject(new PingMessage());
        } catch (Exception e) {
            this.removePlayer();
            this.gameController.nextCurrentPlayerNumber();
            //System.out.println("Player lost");
        }
    }

}
