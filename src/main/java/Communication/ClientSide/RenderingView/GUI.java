package Communication.ClientSide.RenderingView;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.GameOverMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

public class GUI extends Application implements RenderingView {

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

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static void main(String[] args) {
       /*
       start network... per far partire la rete
       boolean useGUI;
       if(useGUI)
           launch();
       else runCLI(); */

        launch(args);
    }


    @Override
    public void start(Stage stage) {
        this.setStage(stage);
        nickName();
    }

    public void nickname(Stage stage) {
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(5));

        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.NEVER);
        root.getColumnConstraints().add(cons1);

        ColumnConstraints cons2 = new ColumnConstraints();
        cons2.setHgrow(Priority.ALWAYS);

        root.getColumnConstraints().addAll(cons1, cons2);

        RowConstraints rcons1 = new RowConstraints();
        rcons1.setVgrow(Priority.NEVER);

        RowConstraints rcons2 = new RowConstraints();
        rcons2.setVgrow(Priority.ALWAYS);

        root.getRowConstraints().addAll(rcons1, rcons2);

        Label lbl = new Label("Insert nickname:");
        TextField field = new TextField();
        Button okBtn = new Button("Ok");

        okBtn.setOnAction(e -> multiOrSinglePlayers());

        GridPane.setHalignment(okBtn, HPos.RIGHT);

        root.add(lbl, 0, 0);
        root.add(field, 1, 0, 3, 1);
        root.add(okBtn, 2, 1);

        Scene scene = new Scene(root, 300, 100);

        stage.setTitle("Choose nickname");
        stage.setScene(scene);
        stage.show();

        this.nickname = field.getText();
    }

    public void multiOrSinglePlayers() {
        GridPane root = new GridPane();
        Button button1 = new Button("Single player");
        Button button2 = new Button("Multi player");
        root.add(button1, 0, 0);
        root.add(button2, 1, 0);

        Scene scene = new Scene(root, 300, 100);

        /* button1.setOnAction(e -> {
            single player welcome
        }); */

        button2.setOnAction(e -> welcome());

        this.stage.setTitle("Multi Or Single Players?");
        this.stage.setScene(scene);
        this.stage.show();

        //setter di scelta di gioco __________________________________
    }

    public void welcome() {
        addLabelByCode("Loading...\nHi " + this.nickname + "!\nWelcome to Master of Renaissance online!");
        while(this.gameStarted != 1) ;
        LoadWTFOnTimer("matchHasStarted");
    }

    public void matchHasStarted() {
        addLabelByCode("Match has started, your player number is " + this.playerNumber);
        if(playerNumber != 0) LoadWTFOnTimer("startingResources");
        else LoadWTFOnTimer("discardStartingLeaders");
    }

    public void addLabelByCode(String string) {
        var label = new Label(string);
        label.setFont(Font.font(32));
        var scene = new Scene(new StackPane(label), 600, 200);
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void LoadWTFOnTimer(String method) {
        TimerTask task = new TimerTask() {

            public void run() {

                Platform.runLater(() -> {
                    try {
                        System.out.println("loading..");
                        switch(method) {
                            case "startingResources":
                                startingResource();
                            case "discardStartingLeaders":
                                discardStartingLeaders();
                            case "matchHasStarted":
                                matchHasStarted();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };


        Timer timer = new Timer("Timer");
        long delay = 3000L;
        timer.schedule(task, delay);
    }


    @Override
    public String getNickName(){
        return nickname;
    }

    public void nickName(){
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(5));

        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.NEVER);
        root.getColumnConstraints().add(cons1);

        ColumnConstraints cons2 = new ColumnConstraints();
        cons2.setHgrow(Priority.ALWAYS);

        root.getColumnConstraints().addAll(cons1, cons2);

        RowConstraints rcons1 = new RowConstraints();
        rcons1.setVgrow(Priority.NEVER);

        RowConstraints rcons2 = new RowConstraints();
        rcons2.setVgrow(Priority.ALWAYS);

        root.getRowConstraints().addAll(rcons1, rcons2);

        Label lbl = new Label("Insert nickname:");
        TextField field = new TextField();
        Button okBtn = new Button("Ok");

        okBtn.setOnAction(e -> {
            multiOrSinglePlayers();
            //welcome(stage, field.getText());
        });

        GridPane.setHalignment(okBtn, HPos.RIGHT);

        root.add(lbl, 0, 0);
        root.add(field, 1, 0, 3, 1);
        root.add(okBtn, 2, 1);

        Scene scene = new Scene(root, 300, 100);

        this.stage.setTitle("Choose nickname");
        this.stage.setScene(scene);
        this.stage.show();

        this.nickname = field.getText();
    }

    public void startingResource() {
        int i;
        this.startingRes = new ArrayList<>();

        if(this.playerNumber == 1 || this.playerNumber == 2) i = 1;
        else i = 2;

        for(; i > 0; i--) {
            String[] resources = new String[]{
                    "coin.png",
                    "servant.png",
                    "shield.png",
                    "stone.png"
            };

            Group root = new Group();
            //Creating buttons
            Button coinButton = new Button();
            Button servantButton = new Button();
            Button shieldButton = new Button();
            Button stoneButton = new Button();

            int x = 10;
            for (String item : resources) {
                //Creating a graphic (image)
                Image img = new Image(item);
                ImageView view = new ImageView(img);
                view.setFitHeight(80);
                view.setPreserveRatio(true);
                //Setting the location of the button
                switch (item) {
                    case "coin.png":
                        coinButton.setTranslateX(x);
                        coinButton.setTranslateY(20);
                        //Setting the size of the button
                        coinButton.setPrefSize(80, 80);
                        //Setting a graphic to the button
                        coinButton.setGraphic(view);
                        x = x + 200;
                        root.getChildren().add(coinButton);
                        break;
                    case "servant.png":
                        servantButton.setTranslateX(x);
                        servantButton.setTranslateY(20);
                        //Setting the size of the button
                        servantButton.setPrefSize(80, 80);
                        //Setting a graphic to the button
                        servantButton.setGraphic(view);
                        x = x + 200;
                        root.getChildren().add(servantButton);
                        break;
                    case "shield.png":
                        shieldButton.setTranslateX(x);
                        shieldButton.setTranslateY(20);
                        //Setting the size of the button
                        shieldButton.setPrefSize(80, 80);
                        //Setting a graphic to the button
                        shieldButton.setGraphic(view);
                        x = x + 200;
                        root.getChildren().add(shieldButton);
                        break;
                    default:
                        stoneButton.setTranslateX(x);
                        stoneButton.setTranslateY(20);
                        //Setting the size of the button
                        stoneButton.setPrefSize(80, 80);
                        //Setting a graphic to the button
                        stoneButton.setGraphic(view);
                        x = x + 200;
                        root.getChildren().add(stoneButton);
                        break;
                }
            }

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            this.stage.setTitle("Pick initial resource");
            this.stage.setScene(scene);
            this.stage.show();

            coinButton.setOnAction(e -> this.startingRes.add("COINS"));

            servantButton.setOnAction(e -> this.startingRes.add("SERVANT"));

            shieldButton.setOnAction(e -> this.startingRes.add("SCHIELD"));

            stoneButton.setOnAction(e -> this.startingRes.add("STONE"));
        }

        LoadWTFOnTimer("discardStartingLeaders");
    }

    public void discardStartingLeaders(){
        for(int j = 0; j < 2; j++) {
            Group root = new Group();
            //Creating buttons
            Button firstLeader = new Button();
            Button secondLeader = new Button();
            Button thirdLeader = new Button();
            Button fourthLeader = new Button();

            int x = 0;
            for (int i = 0; i < startingLeaders.length; i++) {
                //Creating a graphic (image)
                Image img = new Image(startingLeaders[i].getImage());
                ImageView view = new ImageView(img);
                view.setFitHeight(80);
                view.setPreserveRatio(true);
                //Setting the location of the button
                switch (i) {
                    case 0:
                        firstLeader.setTranslateX(x);
                        firstLeader.setTranslateY(20);
                        //Setting the size of the button
                        firstLeader.setPrefSize(80, 80);
                        //Setting a graphic to the button
                        firstLeader.setGraphic(view);
                        x = x + 200;
                        root.getChildren().add(firstLeader);
                        break;
                    case 1:
                        secondLeader.setTranslateX(x);
                        secondLeader.setTranslateY(20);
                        //Setting the size of the button
                        secondLeader.setPrefSize(80, 80);
                        //Setting a graphic to the button
                        secondLeader.setGraphic(view);
                        x = x + 200;
                        root.getChildren().add(secondLeader);
                        break;
                    case 2:
                        thirdLeader.setTranslateX(x);
                        thirdLeader.setTranslateY(20);
                        //Setting the size of the button
                        thirdLeader.setPrefSize(80, 80);
                        //Setting a graphic to the button
                        thirdLeader.setGraphic(view);
                        x = x + 200;
                        root.getChildren().add(thirdLeader);
                        break;
                    default:
                        fourthLeader.setTranslateX(x);
                        fourthLeader.setTranslateY(20);
                        //Setting the size of the button
                        fourthLeader.setPrefSize(80, 80);
                        //Setting a graphic to the button
                        fourthLeader.setGraphic(view);
                        x = x + 200;
                        root.getChildren().add(fourthLeader);
                        break;
                }
            }

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            stage.setTitle("Discard initial leader cards");
            stage.setScene(scene);
            stage.show();

            int finalJ = j;
            firstLeader.setOnAction(e -> {
                this.startingDiscardedLeaders[finalJ] = 0;
                firstLeader.setVisible(false);
            });

            secondLeader.setOnAction(e -> {
                this.startingDiscardedLeaders[finalJ] = 1;
                secondLeader.setVisible(false);
            });

            thirdLeader.setOnAction(e -> {
                this.startingDiscardedLeaders[finalJ] = 2;
                thirdLeader.setVisible(false);
            });

            fourthLeader.setOnAction(e -> {
                this.startingDiscardedLeaders[finalJ] = 3;
                fourthLeader.setVisible(false);
            });
        }
        // chiamta metodo choiceAction ________________________________
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
            // method
        });

        buyDevelopmentCardButton.setOnAction(e -> {
            this.actionChoice = "BUY DEVELOPMENT CARD";
            // method
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
        this.stage.setTitle("Discard initial leader cards");
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
            if(this.actionChoice.equals("PLAY LEADER CARD")) {
                this.playLeader = 1;
            }
            else {
                this.discardLeader = 1;
            }
            choiceAction();
        });
    }

    @Override
    public void setMarket(Market market) {
        this.market = market;
    }

    @Override
    public void setDevCardsGrid(DevelopmentCardsDecksGrid grid) {
        this.grid=grid;
    }

    @Override
    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
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
    public void setClientStarted(){
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
