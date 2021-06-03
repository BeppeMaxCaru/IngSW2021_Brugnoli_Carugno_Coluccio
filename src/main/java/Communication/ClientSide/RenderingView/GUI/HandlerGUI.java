package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.RenderingView.RenderingView;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.GameOverMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

    // Attribute of GUI
    private Stage stage;
    private final GenericClassGUI genericClassGUI;
    private final InitialScenarioGUI initialScenarioGUI;
    private final AsyncScenarioGUI asyncScenarioGUI;

    public HandlerGUI() {
        this.genericClassGUI = new GenericClassGUI();
        this.initialScenarioGUI = new InitialScenarioGUI();
        this.asyncScenarioGUI = new AsyncScenarioGUI();
    }

    public void setStage(Stage stage) { this.stage = stage; }

    public static void main(String[] args) {
        launch(args);
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

    @Override
    public void start(Stage stage) {
        this.setStage(stage);
        initialScenarioGUI.setHandlerGUI(this);
        this.initialScenarioGUI.nickname(stage);
    }

    @Override
    public String getNickName() {
        return this.nickname;
    }

    public void choiceAction() {
        GridPane root = new GridPane();
        Button playLeaderCardButton = new Button("Play leader card");
        Button discardLeaderCardButton = new Button("Discard Leader Card");
        Button pickResourceFromMarketButton = new Button("Pick Resource From Market");
        Button buyDevelopmentCardButton = new Button("Buy Development Card");
        Button activateProdButton = new Button("Activate Production Power");

        root.add(playLeaderCardButton, 0, 0);
        root.add(discardLeaderCardButton, 2, 0);
        root.add(pickResourceFromMarketButton, 0, 1);
        root.add(buyDevelopmentCardButton, 2, 1);
        root.add(activateProdButton, 4, 1);

        Scene scene = new Scene(root, 500, 300);

        playLeaderCardButton.setOnAction(e -> {
            this.actionChoice = "PLAY LEADER CARD";
            playDiscardLeaderCard();
        });

        discardLeaderCardButton.setOnAction(e -> {
            this.actionChoice = "DISCARD LEADER CARD";
            playDiscardLeaderCard();
        });

        pickResourceFromMarketButton.setOnAction(e -> {
            this.actionChoice = "PICK RESOURCES FROM MARKET";
            market();
        });

        buyDevelopmentCardButton.setOnAction(e -> {
            this.actionChoice = "BUY DEVELOPMENT CARD";
            buyDevelopmentCard();
        });

        activateProdButton.setOnAction(e -> {
            this.actionChoice = "ACTIVATE PRODUCTION POWER";
            // method
        });

        this.stage.setTitle("Choose the action!");
        this.stage.setScene(scene);
        this.stage.show();

    }

    public void playDiscardLeaderCard() {
        Group root = new Group();
        Button firstLeader = new Button();
        Button secondLeader = new Button();

        int x = 0;
        for (int i = 0; i < playerLeaders.length; i++) {
            //Creating a graphic (image)
            Image img = new Image(playerLeaders[i].getImage());
            ImageView view = new ImageView(img);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            //Setting the location of the button
            if (i == 0 && !this.playerLeaders[0].isPlayed()) {
                firstLeader.setTranslateX(x);
                firstLeader.setTranslateY(20);
                //Setting the size of the button
                firstLeader.setPrefSize(80, 80);
                //Setting a graphic to the button
                firstLeader.setGraphic(view);
                x = x + 200;
                root.getChildren().add(firstLeader);
            } else if (i == 1 && !this.playerLeaders[1].isPlayed()) {
                secondLeader.setTranslateX(x);
                secondLeader.setTranslateY(20);
                //Setting the size of the button
                secondLeader.setPrefSize(80, 80);
                //Setting a graphic to the button
                secondLeader.setGraphic(view);
                x = x + 200;
                root.getChildren().add(secondLeader);
            }
        }

        //Setting the stage
        Scene scene = new Scene(root, 740, 130);
        this.stage.setTitle("Choose a leader card");
        this.stage.setScene(scene);
        this.stage.show();

        firstLeader.setOnAction(e -> {
            if(this.actionChoice.equals("PLAY LEADER CARD")) {
                this.playLeader = 0;
            }
            else {
                this.discardLeader = 0;
            }
            choiceAction();
        });

        secondLeader.setOnAction(e -> {
            if(this.actionChoice.equals("DISCARD LEADER CARD")) {
                this.playLeader = 1;
            }
            else {
                this.discardLeader = 1;
            }
            choiceAction();
        });

        genericClassGUI.LoadWTFOnTimer("choiceAction", stage);
    }

    public void market() {
        int x, y;
        //creating the image object
        Image image = new Image("plancia portabiglie.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(575);
        imageView.setPreserveRatio(true);
        Group root = new Group(imageView);

        Canvas canvas = new Canvas(600, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        y = 190;
        for(int i = 0; i < 3; i++) {
            x = 190;
            for(int j = 0; j < 4; j++) {
                drawnMarbles(i, j, x, y, gc);
                x+=60;
            }
            y+=60;
        }

        Pane rootButton = new Pane();
        Button c1 = new Button("Click\nHere!");
        rootButton.getChildren().add(c1);
        c1.setLayoutX(185);
        c1.setLayoutY(500);
        Button c2 = new Button("Click\nHere!");
        rootButton.getChildren().add(c2);
        c2.setLayoutX(253);
        c2.setLayoutY(500);
        Button c3 = new Button("Click\nHere!");
        rootButton.getChildren().add(c3);
        c3.setLayoutX(318);
        c3.setLayoutY(500);
        Button c4 = new Button("Click\nHere!");
        rootButton.getChildren().add(c4);
        c4.setLayoutX(383);
        c4.setLayoutY(500);
        Button r1 = new Button("Click\nHere!");
        rootButton.getChildren().add(r1);
        r1.setLayoutX(580);
        r1.setLayoutY(190);
        Button r2 = new Button("Click\nHere!");
        rootButton.getChildren().add(r2);
        r2.setLayoutX(580);
        r2.setLayoutY(255);
        Button r3 = new Button("Click\nHere!");
        rootButton.getChildren().add(r3);
        r3.setLayoutX(580);
        r3.setLayoutY(320);
        root.getChildren().addAll(canvas, rootButton);

        Scene scene = new Scene(root, 650, 770);
        this.stage.setTitle("Pick resources from market");
        this.stage.setScene(scene);
        this.stage.show();

        c1.setOnAction(e -> System.out.println("ciao"));
    }


    public void drawnMarbles(int i, int j, int x, int y, GraphicsContext gc) {
        switch (this.market.getMarketArrangement()[i][j].getColour()) {
            case " YELLOW ":
                gc.setFill(Color.GOLD);
                break;
            case " BLUE ":
                gc.setFill(Color.DEEPSKYBLUE);
                break;
            case " GREY ":
                gc.setFill(Color.GREY);
                break;
            case " PURPLE ":
                gc.setFill(Color.DARKSLATEBLUE);
                break;
            case " RED ":
                gc.setFill(Color.FIREBRICK);
                break;
            case " WHITE ":
                gc.setFill(Color.WHITE);
                break;
        }
        gc.fillOval(x, y, 40, 40);
    }

    public void buyDevelopmentCard() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                grid.getDevelopmentCardsDecks()[i][j][0].getImage();
            }
        }
    }

    @Override
    public int getCorrectAction() { return this.correctAction; }

    @Override
    public void setNickName(String nickname) { this.nickname = nickname; }

    @Override
    public void setGameMode(int gameMode) { this.gameMode = gameMode; }

    @Override
    public void setMarket(Market market) { this.market = market; }

    @Override
    public void setDevCardsGrid(DevelopmentCardsDecksGrid grid) {
        this.grid=grid;
    }

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

    @Override
    public void setStartingLeaders(LeaderCard[] leaders) {
        this.startingLeaders = leaders;
    }

    @Override
    public void setBoard(Playerboard board) {
        this.playerBoard=board;
    }

    @Override
    public int[] getDiscardedStartingLeaders(){
        return startingDiscardedLeaders;
    }

    @Override
    public void setDiscardedStartingLeaders(int[] leadersDiscarded) {
        this.startingDiscardedLeaders = leadersDiscarded;
    }

    @Override
    public void setPlayerLeaders(LeaderCard[] playerLeaders) {
        this.playerLeaders = playerLeaders;
    }

    @Override
    public String getActionChoice() {
        return actionChoice;
    }

    @Override
    public int getPlayedLeader() {
        return playLeader;
    }

    @Override
    public int getDiscardedLeader() {
        return discardLeader;
    }

    @Override
    public int[] getMarketCoordinates() {
        return marketCoordinates;
    }

    @Override
    public String getResourcesDestination() {
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

    @Override
    public String[][] getPayedResources() {
        return payResources;
    }

    @Override
    public int getChosenPosition() {
        return choosePosition;
    }

    @Override
    public int getActivationProd() {
        return activationProd;
    }

    @Override
    public String getInputResourceProd() {
        return inputResourceProd;
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
