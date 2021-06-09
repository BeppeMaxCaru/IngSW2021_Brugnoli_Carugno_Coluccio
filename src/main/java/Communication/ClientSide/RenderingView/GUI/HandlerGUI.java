package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.RenderingView;
import Communication.ClientSide.ServerReceiver;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.GameOverMessage;
import Message.SendingMessages;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.util.*;

public class HandlerGUI extends Application implements RenderingView {

    String nickname;
    int playerNumber;

    //Return attributes
    ArrayList<String> startingRes;
    int[] startingDiscardedLeaders;
    String actionChoice;
    int playLeader;
    int discardLeader;
    int[] marketCoordinates;
    String resourcesDestination;
    String whiteMarbleChoice;
    int[] developmentCardsGridCoordinates;
    String[][] payResources;
    int choosePosition;
    int activationProd;
    String inputResourceProd;
    String outputResourceProd;
    int gameMode;

    //Input methods parameters
    //Initialized by clientMain/serverSender with setters
    LeaderCard[] startingLeaders;
    LeaderCard[] playerLeaders;
    Playerboard playerBoard;
    Market market;
    DevelopmentCardsDecksGrid grid;
    ActionCountersDeck counters;
    Playerboard lorenzoPlayerBoard;
    int localWinner;
    GameOverMessage gameOverMessage;

    //Game/turn handler attributes
    int clientStarted;
    int gameStarted;

    int correctAction;

    // Attribute of GUI da qui ok
    private Stage stage;

    private GenericClassGUI genericClassGUI;
    private InitialScenarioGUI initialScenarioGUI;
    private AsyncScenarioGUI asyncScenarioGUI;
    private PlotScenarioGUI plotScenarioGUI;

    private ClientMain clientMain;

    private ObjectInputStream receiver;
    private SendingMessages msg;

    /*public HandlerGUI() {

        this.genericClassGUI = new GenericClassGUI(this);
        this.initialScenarioGUI = new InitialScenarioGUI(this);
        this.asyncScenarioGUI = new AsyncScenarioGUI(this);
        this.plotScenarioGUI = new PlotScenarioGUI(this);
    } */

    @Override
    public void start(Stage stage) throws Exception {
        Parameters args = getParameters();
        this.clientMain = new ClientMain(args.getUnnamed().get(0), Integer.parseInt(args.getUnnamed().get(1)));

        this.genericClassGUI = new GenericClassGUI(this, this.clientMain);
        this.initialScenarioGUI = new InitialScenarioGUI(this, this.clientMain);
        this.asyncScenarioGUI = new AsyncScenarioGUI(this, this.clientMain);
        this.plotScenarioGUI = new PlotScenarioGUI(this, this.clientMain);

        System.out.println(this.clientMain.getHostName());
        this.setStage(stage);
        this.initialScenarioGUI.nickname(this.stage);
    }

    public GenericClassGUI getGenericClassGUI() {
        return this.genericClassGUI;
    }

    public InitialScenarioGUI getInitialScenarioGUI() {
        return this.initialScenarioGUI;
    }

    public AsyncScenarioGUI getAsyncScenarioGUI() {
        return this.asyncScenarioGUI;
    }

    public PlotScenarioGUI getPlotScenarioGUI() { return this.plotScenarioGUI; }

    public void setStage(Stage stage) { this.stage = stage; }

    public ClientMain getClientMain() { return this.clientMain; }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
        System.out.println(this.clientMain.getClass());
    }

    public void setMsg(SendingMessages msg) { this.msg = msg; }

    public SendingMessages getMsg() { return this.msg; }

    public void setReceiver(ObjectInputStream receiver) { this.receiver = receiver; }

    public ObjectInputStream getReceiver() { return this.receiver; }

    @Override
    public String getNickName() {
        return this.nickname;
    }

    public void setActionChoice(String action) { this.actionChoice = action; }

    @Override
    public int getCorrectAction() { return this.correctAction; }

    @Override
    public void setNickName(String nickname) { this.nickname = nickname; }

    @Override
    public void setGameMode(int gameMode) { this.gameMode = gameMode; }

    @Override
    public void setMarket(Market market) { this.market = market; }

    public Market getMarket() { return this.market; }

    @Override
    public void setDevCardsGrid(DevelopmentCardsDecksGrid grid) {
        this.grid = grid;
    }

    public DevelopmentCardsDecksGrid getDevCardsGrid( ) { return this.grid; }

    @Override
    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    @Override
    public int getPlayerNumber() {
        return this.playerNumber;
    }

    @Override
    public ArrayList<String> getStartingResource() {
        return startingRes;
    }

    public void setStartingResource(String string) {
        this.startingRes.add(string);
    }

    @Override
    public void setStartingLeaders(LeaderCard[] leaders) {
        this.startingLeaders = leaders;
    }

    @Override
    public void setBoard(Playerboard board) {
        this.playerBoard=board;
    }

    public Playerboard getBoard( ) {
        return this.playerBoard;
    }

    @Override
    public int[] getDiscardedStartingLeaders(){
        return startingDiscardedLeaders;
    }


    public void setDiscardedStartingLeaders(int leadersDiscarded, int index) {
        this.startingDiscardedLeaders[index] = leadersDiscarded;
    }

    @Override
    public void setPlayerLeaders(LeaderCard[] playerLeaders) {
        this.playerLeaders = playerLeaders;
    }

    public LeaderCard[] getPlayerLeaders() {return this.playerLeaders; }

    @Override
    public String getActionChoice() {
        return actionChoice;
    }

    @Override
    public int getPlayedLeader() {
        return playLeader;
    }

    public void setPlayedLeader(int playLeader) { this.playLeader = playLeader; }

    @Override
    public int getDiscardedLeader() {
        return discardLeader;
    }

    public void setDiscardedLeader(int discardLeader) { this.discardLeader = discardLeader; }

    @Override
    public int[] getMarketCoordinates() {
        return marketCoordinates;
    }

    public void setMarketCoordinates(int[] marketCoordinates) { this.marketCoordinates = marketCoordinates; }

    @Override
    public String getResourcesDestination(String parameter) {
        return resourcesDestination;
    }

    @Override
    public String getWhiteMarbleChoice() {
        return whiteMarbleChoice;
    }

    @Override
    public int[] getDevelopmentCardsGridCoordinates() {
        return developmentCardsGridCoordinates;
    }

    public void setDevelopmentCardsGridCoordinates(int[] coordinates) {
        this.developmentCardsGridCoordinates = coordinates;
    }

    @Override
    public String[][] getPayedResources() {
        return payResources;
    }

    @Override
    public int getChosenPosition() {
        return choosePosition;
    }

    @Override
    public int getActivationProd(int[] activation) {
        return activationProd;
    }

    @Override
    public String getInputResourceProd() {
        return inputResourceProd;
    }

    public void setInputResourceProd(String inputResourceProd) {
        this.inputResourceProd = inputResourceProd;
    }

    @Override
    public String getOutputResourceProd() {
        return outputResourceProd;
    }

    @Override
    public void setLocalWinner(int localWinner) {
        this.localWinner = localWinner;
    }

    @Override
    public void setCountersDeck(ActionCountersDeck deck) {
        this.counters = deck;
    }

    @Override
    public void setLorenzoPlayerBoard(Playerboard board) {
        this.lorenzoPlayerBoard = board;
    }

    @Override
    public void setGameOverMsg(GameOverMessage msg) {
        this.gameOverMessage = msg;
    }

    @Override
    public void setGameStarted() {
        this.gameStarted = 1;
    }

    @Override
    public void setClientStarted() {
        this.clientStarted=1;
    }

    @Override
    public int getGameMode() {
        return this.gameMode;
    }

    @Override
    public void notValidAction() {
        this.correctAction=0;
    }

    @Override
    public void notYourTurn() {
        this.correctAction=0;
    }
}
