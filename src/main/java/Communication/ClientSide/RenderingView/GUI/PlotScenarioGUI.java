package Communication.ClientSide.RenderingView.GUI;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PlotScenarioGUI {

    HandlerGUI handlerGUI;
    boolean checkLeaderAction;
    boolean checkNormalAction;

    public PlotScenarioGUI(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
        this.checkLeaderAction = false;
        this.checkNormalAction = false;
    }

    public void choiceAction(Stage stage) {
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

        if(!this.checkLeaderAction) {
            playLeaderCardButton.setOnAction(e -> {
                handlerGUI.setActionChoice("PLAY LEADER CARD");
                playDiscardLeaderCard(stage);
                this.checkLeaderAction = true;
            });

            discardLeaderCardButton.setOnAction(e -> {
                handlerGUI.setActionChoice("DISCARD LEADER CARD");
                playDiscardLeaderCard(stage);
                this.checkLeaderAction = true;
            });
        }

        if(!checkNormalAction) {
            pickResourceFromMarketButton.setOnAction(e -> {
                handlerGUI.setActionChoice("PICK RESOURCES FROM MARKET");
                market(stage);
                this.checkNormalAction = true;
            });

            buyDevelopmentCardButton.setOnAction(e -> {
                handlerGUI.setActionChoice("BUY DEVELOPMENT CARD");
                buyDevelopmentCard(stage);
                this.checkNormalAction = true;
            });

            activateProdButton.setOnAction(e -> {
                handlerGUI.setActionChoice("ACTIVATE PRODUCTION POWER");
                // method
                this.checkNormalAction = true;
            });
        }

        stage.setTitle("Choose the action!");
        stage.setScene(scene);
        stage.show();
    }

    public void playDiscardLeaderCard(Stage stage) {
        Group root = new Group();
        Button firstLeader = new Button();
        Button secondLeader = new Button();

        int x = 0;
        for (int i = 0; i < handlerGUI.getPlayerLeaders().length; i++) {
            //Creating a graphic (image)
            Image img = new Image(handlerGUI.getPlayerLeaders()[i].getImage());
            ImageView view = new ImageView(img);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            //Setting the location of the button
            if (i == 0 && !this.handlerGUI.getPlayerLeaders()[0].isPlayed()) {
                firstLeader.setTranslateX(x);
                firstLeader.setTranslateY(20);
                //Setting the size of the button
                firstLeader.setPrefSize(80, 80);
                //Setting a graphic to the button
                firstLeader.setGraphic(view);
                x = x + 200;
                root.getChildren().add(firstLeader);
            } else if (i == 1 && !this.handlerGUI.getPlayerLeaders()[1].isPlayed()) {
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
        stage.setTitle("Choose a leader card");
        stage.setScene(scene);
        stage.show();

        firstLeader.setOnAction(e -> {
            if(handlerGUI.getActionChoice().equals("PLAY LEADER CARD")) {
                handlerGUI.setPlayedLeader(0);
            }
            else {
                handlerGUI.setDiscardedLeader(0);
            }
            choiceAction(stage);
        });

        secondLeader.setOnAction(e -> {
            if(handlerGUI.getActionChoice().equals("DISCARD LEADER CARD")) {
                handlerGUI.setPlayedLeader(1);
            }
            else {
                handlerGUI.setDiscardedLeader(1);
            }
            choiceAction(stage);
        });

        if(!checkNormalAction) handlerGUI.getGenericClassGUI().LoadWTFOnTimer("choiceAction", stage);
        else waitForYouturn(stage);
    }

    public void market(Stage stage) {
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
        stage.setTitle("Pick resources from market");
        stage.setScene(scene);
        stage.show();

        int[] coordinates = new int[2];
        c1.setOnAction(e -> {
            coordinates[0] = 1;
            coordinates[1] = 0;
            handlerGUI.setMarketCoordinates(coordinates);
        });

        c2.setOnAction(e -> {
            coordinates[0] = 1;
            coordinates[1] = 1;
            handlerGUI.setMarketCoordinates(coordinates);
        });

        c3.setOnAction(e -> {
            coordinates[0] = 1;
            coordinates[1] = 2;
            handlerGUI.setMarketCoordinates(coordinates);
        });

        c4.setOnAction(e -> {
            coordinates[0] = 1;
            coordinates[1] = 3;
            handlerGUI.setMarketCoordinates(coordinates);
        });

        r1.setOnAction(e -> {
            coordinates[0] = 0;
            coordinates[1] = 0;
            handlerGUI.setMarketCoordinates(coordinates);
        });

        r2.setOnAction(e -> {
            coordinates[0] = 0;
            coordinates[1] = 1;
            handlerGUI.setMarketCoordinates(coordinates);
        });

        r3.setOnAction(e -> {
            coordinates[0] = 0;
            coordinates[1] = 2;
            handlerGUI.setMarketCoordinates(coordinates);
        });

        if(!this.checkLeaderAction) handlerGUI.getGenericClassGUI().LoadWTFOnTimer("choiceAction", stage);
        else waitForYouturn(stage);
    }


    public void drawnMarbles(int i, int j, int x, int y, GraphicsContext gc) {
        switch (handlerGUI.getMarket().getMarketArrangement()[i][j].getColour()) {
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

    public void buyDevelopmentCard(Stage stage) {
        Group root = new Group();
        //Creating buttons
        Button[] arrayButtons = new Button[12];

        int x = 10;
        int index = 0;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                //Creating a graphic (image)
                Image img = new Image(handlerGUI.getDevCardsGrid().getDevelopmentCardsDecks()[i][j][0].getImage());
                arrayButtons[index] = new Button();
                handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index]);
                x = x + 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }
        }

        //Setting the stage
        Scene scene = new Scene(root, 740, 130);
        stage.setTitle("Buy development card.");
        stage.setScene(scene);
        stage.show();

        int row = 0, column = 0;
        int[] coordinates = new int[2];
        for(int i = 0; i < 12; i++) {
            if(i == 4) { column = 0; row = 1; }
            else if(i == 8) { column = 0; row = 2; }
            coordinates[0] = row;
            coordinates[1] = column;
            arrayButtons[i].setOnAction(e -> {
                handlerGUI.setDevelopmentCardsGridCoordinates(coordinates);
                handlerGUI.getGenericClassGUI().LoadWTFOnTimer("choiceAction", stage);
            });
            column++;
        }
        if(!this.checkLeaderAction) handlerGUI.getGenericClassGUI().LoadWTFOnTimer("choiceAction", stage);
        else waitForYouturn(stage);
    }

    public void waitForYouturn(Stage stage) {
        handlerGUI.getGenericClassGUI().addLabelByCode("Your turn is ended, wait some minutes!", stage);
    }
}
