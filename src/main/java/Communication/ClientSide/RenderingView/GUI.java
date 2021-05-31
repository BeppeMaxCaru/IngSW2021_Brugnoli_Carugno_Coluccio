package Communication.ClientSide.RenderingView;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.GameOverMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
        nickName(stage);
    }

    public int multiOrSinglePlayers(Stage stage) {
        GridPane root = new GridPane();
        int choice;
        ToggleButton button1 = new ToggleButton("Single player");
        ToggleButton button2 = new ToggleButton("Multi player");
        root.add(button1, 0, 0);
        root.add(button2, 1, 0);

        Scene scene = new Scene(root, 300, 100);

        if(button1.isSelected()) choice = 0;
        else choice = 1;

        /* button1.setOnAction(e -> {
            single player welcome
        }); */

        button2.setOnAction(e -> {
            welcome(stage);
        });

        stage.setTitle("Multi Or Single Players?");
        stage.setScene(scene);
        stage.show();

        return choice;
    }

    public void welcome(Stage stage) {
        addLabelByCode(stage, "Loading...\nHi " + this.nickname + "!\nWelcome to Master of Renaissance online!");
        matchHasStarted(stage, 1); // da togliere
    }

    public void matchHasStarted(Stage stage, int playerNumber) {
        // Chiamata dal client? Devo far apparire questo messaggio quando il server si connette alla socket.
        this.playerNumber = playerNumber;
        addLabelByCode(stage, "Match has started, your player number is " + this.playerNumber);
        if(playerNumber != 0) LoadWTFOnTimer(stage, "startingResource");
        else LoadWTFOnTimer(stage, "discardStartingLeaders");
    }


    public void addLabelByCode(Stage stage, String string) {
        var label = new Label(string);
        label.setFont(Font.font(32));
        var scene = new Scene(new StackPane(label), 600, 200);
        stage.setScene(scene);
        stage.show();
    }

    public void LoadWTFOnTimer(Stage stage, String method) {
        TimerTask task = new TimerTask() {

            public void run() {

                Platform.runLater(() -> {
                    try {
                        System.out.println("loading..");
                        switch(method) {
                            case "startingResource":
                                startingResource(stage);
                            case "discardStartingLeaders":
                                discardStartingLeaders(stage);
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });
            }
        };


        Timer timer = new Timer("Timer");
        long delay = 2000L;
        timer.schedule(task, delay);
    }


    @Override
    public String nickName(Stage stage){
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
            multiOrSinglePlayers(stage);
            //welcome(stage, field.getText());
        });

        GridPane.setHalignment(okBtn, HPos.RIGHT);

        root.add(lbl, 0, 0);
        root.add(field, 1, 0, 3, 1);
        root.add(okBtn, 2, 1);

        Scene scene = new Scene(root, 300, 100);

        stage.setTitle("Choose nickname");
        stage.setScene(scene);
        stage.show();

        this.nickname = field.getText();
        return nickname;
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
    public ArrayList<String> startingResource(Stage stage) {
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
            ToggleButton coinButton = new ToggleButton();
            ToggleButton servantButton = new ToggleButton();
            ToggleButton shieldButton = new ToggleButton();
            ToggleButton stoneButton = new ToggleButton();

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
            stage.setTitle("Pick initial resource");
            stage.setScene(scene);
            stage.show();

            coinButton.setOnAction(e -> {
                this.startingRes.add("COINS");
            });

            servantButton.setOnAction(e -> {
                this.startingRes.add("SERVANT");
            });

            shieldButton.setOnAction(e -> {
                this.startingRes.add("SCHIELD");
            });

            stoneButton.setOnAction(e -> {
                this.startingRes.add("STONE");
            });
        }

        LoadWTFOnTimer(stage, "discardStartingLeaders");

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
    public int[] discardStartingLeaders(Stage stage){
        for(int j = 0; j < 2; j++) {
            Group root = new Group();
            //Creating buttons
            ToggleButton firstLeader = new ToggleButton();
            ToggleButton secondLeader = new ToggleButton();
            ToggleButton thirdLeader = new ToggleButton();
            ToggleButton fourthLeader = new ToggleButton();

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

            int finalJ = j;
            firstLeader.setOnAction(e -> {
                this.startingDiscardedLeaders[finalJ] = 0;
            });

            secondLeader.setOnAction(e -> {
                this.startingDiscardedLeaders[finalJ] = 1;
            });

            thirdLeader.setOnAction(e -> {
                this.startingDiscardedLeaders[finalJ] = 2;
            });

            fourthLeader.setOnAction(e -> {
                this.startingDiscardedLeaders[finalJ] = 3;
            });
        }

        return startingDiscardedLeaders;
    }

    @Override
    public void setPlayerLeaders(LeaderCard[] playerLeaders) {
        this.playerLeaders = playerLeaders;
    }

    @Override
    public String actionChoice(Stage stage) {
        return actionChoice;
    }

    @Override
    public int playLeader(Stage stage) {
        return playLeader;
    }

    @Override
    public int discardLeader(Stage stage) {
        return discardLeader;
    }

    @Override
    public int[] marketCoordinates(Stage stage) {
        return marketCoordinates;
    }

    @Override
    public String resourcesDestination(Stage stage, String parameter) {
        return resourcesDestination;
    }

    @Override
    public String whiteMarbleChoice(Stage stage) {
        return whiteMarbleChoice;
    }

    @Override
    public int[] developmentCardsGridCoordinates(Stage stage) {
        return developmentCardsGridCoordinates;
    }

    @Override
    public String[][] payResources(Stage stage) {
        return payResources;
    }

    @Override
    public int choosePosition(Stage stage) {
        return choosePosition;
    }


    @Override
    public int activationProd(Stage stage, int[] activation) {
        return activationProd;
    }


    @Override
    public String inputResourceProd(Stage stage) {
        return inputResourceProd;
    }

    @Override
    public String outputResourceProd(Stage stage) {
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

    public void setClientStarted(){
        this.clientStarted=1;
    }
}
