package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.RenderingView;
import Communication.ClientSide.ServerReceiver;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.*;
import Message.SendingMessages;
import Message.ServerStartingMessage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class HandlerGUI extends Application implements RenderingView {

    int[] activate;

    int correctAction;

    // Attribute of GUI da qui ok
    private Stage stage;

    private GenericClassGUI genericClassGUI;
    private InitialScenarioGUI initialScenarioGUI;
    private SyncScenarioGUI syncScenarioGUI;
    private PlotScenarioGUI plotScenarioGUI;

    private ClientMain clientMain;

    private ObjectInputStream receiver;
    private ObjectOutputStream sender;
    private SendingMessages msg;


    @Override
    public void start(Stage stage) throws Exception {
        Parameters args = getParameters();
        this.clientMain = new ClientMain(args.getUnnamed().get(0), Integer.parseInt(args.getUnnamed().get(1)));

        this.genericClassGUI = new GenericClassGUI(this);
        this.initialScenarioGUI = new InitialScenarioGUI(this);
        this.syncScenarioGUI = new SyncScenarioGUI(this);
        this.plotScenarioGUI = new PlotScenarioGUI(this);



        //System.out.println(this.clientMain.getHostName());
        this.setStage(stage);
        this.initialScenarioGUI.nickname(this.stage);
    }

    public GenericClassGUI getGenericClassGUI() {
        return this.genericClassGUI;
    }

    public SyncScenarioGUI getSyncScenarioGUI() {
        return this.syncScenarioGUI;
    }

    public PlotScenarioGUI getPlotScenarioGUI() { return this.plotScenarioGUI; }

    public void setStage(Stage stage) { this.stage = stage; }

    public ClientMain getClientMain() { return this.clientMain; }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
        System.out.println(this.clientMain.getClass());
    }

    public SendingMessages getMsg() { return this.msg; }

    @Override
    public void notValidAction() {
        this.correctAction=0;
    }

    @Override
    public void notYourTurn() {
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
            this.getGenericClassGUI().LoadWTFOnTimer("matchHasStarted", stage);
            //updatePlayerBoard();
        } catch (Exception e) {
            this.error(e);
        }
    }

    public void updatePlayerBoard() {
        try {
            UpdateClientPlayerBoardMessage playerBoardMessage = (UpdateClientPlayerBoardMessage) this.receiver.readObject();
            this.clientMain.setPlayerboard(playerBoardMessage.getPlayerboard());
            System.out.println(playerBoardMessage.getPlayerboard().getClass());
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

    public void setActivation(int[] activate) {
        this.activate = activate;
    }

    @Override
    public void itsYourTurn() {
        this.plotScenarioGUI.choiceAction(this.stage);
    }

    @Override
    public void endTurn() {
        this.plotScenarioGUI.waitForYourTurn(this.stage);
    }
}
