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
public class HandlerGUI extends Application implements RenderingView {

    int[] activate;
    boolean turn = false;
    int gameMode;

    int correctAction;

    // Attribute of GUI da qui ok
    private Stage stage;

    private GenericClassGUI genericClassGUI;
    private InitialScenarioGUI initialScenarioGUI;
    private SyncScenarioGUI syncScenarioGUI;
    private PlotScenarioGUI plotScenarioGUI;
    private PlayerBoardScenario playerBoardScenario;
    private WaitForYourTurnScenario waitForYourTurnScenario;
    private EndGameScenario endGameScenario;

    private ClientMain clientMain;

    private ObjectInputStream receiver;
    private ObjectOutputStream sender;
    private SendingMessages msg;


    @Override
    public void start(Stage stage) throws Exception {
        Parameters args = getParameters();
        this.setStage(stage);
        this.clientMain = new ClientMain(args.getUnnamed().get(0), Integer.parseInt(args.getUnnamed().get(1)));

        this.genericClassGUI = new GenericClassGUI(this);
        //this.initialScenarioGUI = new InitialScenarioGUI(this, this.stage);
        this.syncScenarioGUI = new SyncScenarioGUI(this, this.stage);
        this.plotScenarioGUI = new PlotScenarioGUI(this, this.stage);
        this.playerBoardScenario = new PlayerBoardScenario(this, new Stage());
        this.waitForYourTurnScenario = new WaitForYourTurnScenario(this, this.stage);
        this.endGameScenario = new EndGameScenario(this, this.stage);

        this.initialScenarioGUI = new InitialScenarioGUI(this, this.stage);
        this.initialScenarioGUI.nickname();
    }

    public GenericClassGUI getGenericClassGUI() {
        return this.genericClassGUI;
    }

    public SyncScenarioGUI getSyncScenarioGUI() {
        return this.syncScenarioGUI;
    }

    public PlotScenarioGUI getPlotScenarioGUI() { return this.plotScenarioGUI; }

    public PlayerBoardScenario getPlayerBoardScenario() { return this.playerBoardScenario; }

    public EndGameScenario getEndGameScenario() { return this.endGameScenario; }

    public void setStage(Stage stage) { this.stage = stage; }

    public ClientMain getClientMain() { return this.clientMain; }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
        System.out.println(this.clientMain.getClass());
    }

    public SendingMessages getMsg() { return this.msg; }


    public int getGameMode() { return this.gameMode; }

    public void setGameMode(int gameMode) { this.gameMode = gameMode; }

    @Override
    public void notValidAction() {
        this.correctAction=0;
    }

    public void connectionSocket() {
        try {
            Socket socket = new Socket(this.clientMain.getHostName(), this.clientMain.getPort());
            this.receiver = new ObjectInputStream(socket.getInputStream());
            this.sender = new ObjectOutputStream(socket.getOutputStream());
            //Check passing this
            this.msg = new SendingMessages(this.clientMain, this, this.sender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNickname() {
        try {
            this.msg.sendNickname();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMarket() {
        try {
            UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) this.receiver.readObject();
            this.clientMain.setMarket(updateClientMarketMessage.getMarket());
            updateGridDevCard();
        } catch (Exception e) {
            this.error(e);
        }
    }

    public void updateGridDevCard() {
        try {
            UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) this.receiver.readObject();
            this.clientMain.setDevelopmentCardsDecksGrid(updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid());
            startingMessage();
        } catch (Exception e) {
            this.error(e);
        }
    }

    public void startingMessage() {
        try {
            ServerStartingMessage startingMessage = (ServerStartingMessage) this.receiver.readObject();
            this.clientMain.setPlayerNumber(startingMessage.getPlayerNumber());
            //System.out.println(startingMessage.getLeaderCards().length);
            this.clientMain.setLeaderCards(startingMessage.getLeaderCards());
            System.out.println(startingMessage.getLeaderCards()[0].getClass());
            this.getGenericClassGUI().LoadWTFOnTimer("matchHasStarted");
            //updatePlayerBoard();
        } catch (Exception e) {
            this.error(e);
        }
    }

    public void updatePlayerBoard() {
        try {
            UpdateClientPlayerBoardMessage playerBoardMessage = (UpdateClientPlayerBoardMessage) this.receiver.readObject();
            this.clientMain.setPlayerboard(playerBoardMessage.getPlayerboard());
            //System.out.println(playerBoardMessage.getPlayerboard().getClass());
        } catch (Exception e) {
            //System.out.println("Non arriva playerboard");
            this.error(e);
        }
    }

    public void updateLeaderCard() {
        try {
            UpdateClientLeaderCardsMessage leaderCardsMessage = (UpdateClientLeaderCardsMessage) this.receiver.readObject();
            this.clientMain.setLeaderCards(leaderCardsMessage.getLeaderCards());
            //System.out.println(this.clientMain.getPlayerboard().getVictoryPoints());
        } catch (Exception e) {
            this.error(e);
        }
    }

    public void AsyncReceiver() {
        new ServerReceiver(this.clientMain, this, this.receiver).start();
    }

    public boolean endLocalGame(Player[] localPlayers){
        for(int k = 0; k < 4; k++)
            if(this.clientMain.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[0][k][0] == null) {
                this.clientMain.setWinner(localPlayers[0].getNickname());
                return true;
            }

        if(localPlayers[0].getPlayerBoard().getFaithPath().getCrossPosition() == 24) {
            this.clientMain.setWinner(localPlayers[0].getNickname());
            return true;
        }

        if(localPlayers[1].getPlayerBoard().getFaithPath().getCrossPosition() == 24) {
            this.clientMain.setWinner(localPlayers[1].getNickname());
            return true;
        }

        if(localPlayers[0].getPlayerBoard().getDevelopmentCardsBought() == 7) {
            this.clientMain.setWinner(localPlayers[0].getNickname());
            return true;
        }
        return false;
    }

    @Override
    public void itsYourTurn() {
        Platform.runLater(this.plotScenarioGUI);
    }

    @Override
    public void update() {
        Platform.runLater(this.playerBoardScenario);
    }

    @Override
    public void notYourTurn() {
    }

    @Override
    public void endTurn(String turn) {
        Platform.runLater(this.waitForYourTurnScenario);
    }

    @Override
    public void endMultiplayerGame(GameOverMessage msg) {
        getClientMain().setVictoryPoints(msg.getVictoryPoints());
        getClientMain().setWinner(msg.getWinner());
        Platform.runLater(this.endGameScenario);
    }

    @Override
    public void connectionError(Exception e) {
        //e.printStackTrace();
        System.err.println("Connection error");
        System.exit(-1);
    }

    @Override
    public void setupError(Exception e) {
        //e.printStackTrace();
        System.err.println("Error during setup");
        System.exit(-2);
    }

    @Override
    public void gameError(Exception e) {
        //e.printStackTrace();
        System.err.println("The application encountered a problem");
        System.out.println(-3);
    }

    @Override
    public void serverError(String error) {
    }

    @Override
    public void invalidInputError(Exception e) {
        System.err.println("Not valid input");
    }

    @Override
    public void error(Exception e){
        e.printStackTrace();
        System.err.println("Error");
    }

    @Override
    public void receiverError(Exception e){
        e.printStackTrace();
        System.err.println("Message corrupted");
        System.exit(-7);
    }

    @Override
    public void senderError(Exception e){
        e.printStackTrace();
        System.err.println("Not valid parameter");
    }
}
