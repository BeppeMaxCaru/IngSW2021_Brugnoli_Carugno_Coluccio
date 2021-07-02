package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.RenderingView;
import Communication.ClientSide.ServerReceiver;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.MessageReceived.*;
import Message.SendingMessages;
import Message.ServerStartingMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * GUI's handler
 */
public class HandlerGUI extends Application implements RenderingView {

    int[] activate;
    boolean turn = false;

    /**
     * The game mode chosen
     */
    int gameMode;


    int correctAction;

    /**
     * Application stage
     */
    private Stage stage;

    /**
     * Generic scenario
     */
    private GenericClassGUI genericClassGUI;

    /**
     * Initial scenario
     */
    private InitialScenarioGUI initialScenarioGUI;

    /**
     * Sync phase scenario
     */
    private SyncScenarioGUI syncScenarioGUI;

    /**
     * Actions scenario
     */
    private PlotScenarioGUI plotScenarioGUI;

    /**
     * Player board scenario
     */
    private PlayerBoardScenario playerBoardScenario;

    /**
     * Wait scenario
     */
    private WaitForYourTurnScenario waitForYourTurnScenario;

    /**
     * End game scenario
     */
    private EndGameScenario endGameScenario;
    private ErrorScenario errorScenario;

    /**
     * Client running the application
     */
    private ClientMain clientMain;

    /**
     * Socket used for connection
     */
    private Socket clientSocket;

    /**
     * Messages receiver
     */
    private ObjectInputStream receiver;

    /**
     * Messages sender
     */
    private ObjectOutputStream sender;

    /**
     * Utility to help sending messages
     */
    private SendingMessages msg;


    @Override
    public void start(Stage stage) throws Exception {
        Parameters args = getParameters();
        this.setStage(stage);
        this.clientMain = new ClientMain(args.getUnnamed().get(0), Integer.parseInt(args.getUnnamed().get(1)));

        this.genericClassGUI = new GenericClassGUI(this);
        this.syncScenarioGUI = new SyncScenarioGUI(this, this.stage);
        this.plotScenarioGUI = new PlotScenarioGUI(this, this.stage);
        this.playerBoardScenario = new PlayerBoardScenario(this, new Stage());
        this.waitForYourTurnScenario = new WaitForYourTurnScenario(this, this.stage);
        this.endGameScenario = new EndGameScenario(this, this.stage);
        this.initialScenarioGUI = new InitialScenarioGUI(this, this.stage);

        this.initialScenarioGUI.nickname();
    }

    /**
     * Returns the generic class GUI
     * @return the generic class GUI
     */
    public GenericClassGUI getGenericClassGUI() {
        return this.genericClassGUI;
    }

    /**
     * Returns the sync scenario
     * @return the sync scenario
     */
    public SyncScenarioGUI getSyncScenarioGUI() {
        return this.syncScenarioGUI;
    }

    /**
     * Returns plot scenario
     * @return plot scenario
     */
    public PlotScenarioGUI getPlotScenarioGUI() { return this.plotScenarioGUI; }

    /**
     * Returns player board scenario
     * @return player board scenario
     */
    public PlayerBoardScenario getPlayerBoardScenario() { return this.playerBoardScenario; }

    /**
     * Returns end game scenario
     * @return end game scenario
     */
    public EndGameScenario getEndGameScenario() { return this.endGameScenario; }

    /**
     * Returns the stage
     * @return the stage
     */
    public Stage getStage( ) { return this.stage; }

    /**
     * Sets the stage
     * @param stage Stage to be set
     */
    public void setStage(Stage stage) { this.stage = stage; }

    /**
     * Returns client
     * @return client
     */
    public ClientMain getClientMain() { return this.clientMain; }

    /**
     * Returns utility class
     * @return utility class
     */
    public SendingMessages getMsg() { return this.msg; }

    /**
     * Returns game mode
     * @return game mode
     */
    public int getGameMode() { return this.gameMode; }

    /**
     * Sets game mode
     * @param gameMode game mode to be set
     */
    public void setGameMode(int gameMode) { this.gameMode = gameMode; }

    @Override
    public void notValidAction() {
        this.correctAction=0;
    }

    /**
     * Establishes connection
     */
    public void connectionSocket() {
        try {
            this.clientSocket = new Socket(this.clientMain.getHostName(), this.clientMain.getPort());
            this.receiver = new ObjectInputStream(this.clientSocket.getInputStream());
            this.sender = new ObjectOutputStream(this.clientSocket.getOutputStream());
            //Check passing this
            this.msg = new SendingMessages(this.clientMain, this, this.sender);
        } catch (Exception e) {
            //e.printStackTrace();
            this.connectionError(e);
        }
    }

    /**
     * Sends the nickname
     */
    public void sendNickname() {
        try {
            this.msg.sendNickname();
        } catch (Exception e) {
            //e.printStackTrace();
            this.setupError(e);
        }
    }

    /**
     * Updates the market
     */
    public void updateMarket() {
        try {
            UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) this.receiver.readObject();
            this.clientMain.setMarket(updateClientMarketMessage.getMarket());
            updateGridDevCard();
        } catch (Exception e) {
            this.setupError(e);
        }
    }

    /**
     * Updates the development cards grid
     */
    public void updateGridDevCard() {
        try {
            UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) this.receiver.readObject();
            this.clientMain.setDevelopmentCardsDecksGrid(updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid());
            startingMessage();
        } catch (Exception e) {
            this.setupError(e);
        }
    }

    /**
     * Handles starting messages
     */
    public void startingMessage() {
        try {
            ServerStartingMessage startingMessage = (ServerStartingMessage) this.receiver.readObject();
            this.clientMain.setPlayerNumber(startingMessage.getPlayerNumber());
            //System.out.println(startingMessage.getLeaderCards().length);
            this.clientMain.setLeaderCards(startingMessage.getLeaderCards());
            //System.out.println(startingMessage.getLeaderCards()[0].getClass());
            this.getGenericClassGUI().LoadWTFOnTimer("matchHasStarted");
            //updatePlayerBoard();
        } catch (Exception e) {
            this.setupError(e);
        }
    }

    /**
     * Updates player board
     */
    public void updatePlayerBoard() {
        try {
            UpdateClientPlayerBoardMessage playerBoardMessage = (UpdateClientPlayerBoardMessage) this.receiver.readObject();
            this.clientMain.setPlayerboard(playerBoardMessage.getPlayerboard());
        } catch (Exception e) {
            //System.out.println("Non arriva playerboard");
            this.setupError(e);
        }
    }

    /**
     * Update leader cards
     */
    public void updateLeaderCard() {
        try {
            UpdateClientLeaderCardsMessage leaderCardsMessage = (UpdateClientLeaderCardsMessage) this.receiver.readObject();
            this.clientMain.setLeaderCards(leaderCardsMessage.getLeaderCards());
        } catch (Exception e) {
            this.setupError(e);
        }
    }

    /**
     * Starts async receiver
     */
    public void AsyncReceiver() {
        new ServerReceiver(this.clientMain, this, this.clientSocket,this.receiver).start();
    }

    /**
     * Checks local game end
     */
    public boolean endLocalGame(Player[] localPlayers){
        for(int k = 0; k < 4; k++)
            if(this.clientMain.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[0][k][0] == null) {
                this.clientMain.setWinner(localPlayers[1].getNickname());
                this.clientMain.setVictoryPoints(localPlayers[0].sumAllVictoryPoints());
                return true;
            }

        if(localPlayers[0].getPlayerBoard().getFaithPath().getCrossPosition() == 24) {
            this.clientMain.setWinner(localPlayers[0].getNickname());
            this.clientMain.setVictoryPoints(localPlayers[0].sumAllVictoryPoints());
            return true;
        }

        if(localPlayers[1].getPlayerBoard().getFaithPath().getCrossPosition() == 24) {
            this.clientMain.setWinner(localPlayers[1].getNickname());
            this.clientMain.setVictoryPoints(localPlayers[0].sumAllVictoryPoints());
            return true;
        }

        if(localPlayers[0].getPlayerBoard().getDevelopmentCardsBought() == 7) {
            this.clientMain.setWinner(localPlayers[0].getNickname());
            this.clientMain.setVictoryPoints(localPlayers[0].sumAllVictoryPoints());
            return true;
        }
        return false;
    }

    @Override
    public void itsYourTurn() { Platform.runLater(this.plotScenarioGUI); }

    @Override
    public void update() { Platform.runLater(this.playerBoardScenario); }

    @Override
    public void endTurn(String turn) { Platform.runLater(this.waitForYourTurnScenario); }

    @Override
    public void endMultiplayerGame(GameOverMessage msg) {
        getClientMain().setVictoryPoints(msg.getVictoryPoints());
        getClientMain().setWinner(msg.getWinner());
        Platform.runLater(this.endGameScenario);
    }

    @Override
    public void connectionError(Exception e) { Platform.runLater(new ErrorScenario(this, this.stage, "Connection error")); }

    @Override
    public void setupError(Exception e) { Platform.runLater(new ErrorScenario(this, this.stage, "Error during setup")); }

    @Override
    public void gameError(Exception e) { Platform.runLater(new ErrorScenario(this, this.stage, "The application encountered a problem")); }

    @Override
    public void serverError(String error) { Platform.runLater(new ErrorScenario(this, this.stage, error)); }

    @Override
    public void invalidInputError(Exception e) { Platform.runLater(new ErrorScenario(this, this.stage, "Not valid input")); }

    @Override
    public void error(Exception e) { Platform.runLater(new ErrorScenario(this, this.stage, "Error")); }

    @Override
    public void receiverError(Exception e) { Platform.runLater(new ErrorScenario(this, this.stage, "Error occurred while receiving data")); }

    @Override
    public void senderError(Exception e) { Platform.runLater(new ErrorScenario(this, this.stage, "Error occurred while sending data")); }
}
