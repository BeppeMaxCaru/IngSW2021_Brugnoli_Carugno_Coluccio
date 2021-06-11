package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraProductionPowerLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraWarehouseSpaceLeaderCard;
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

    private final HandlerGUI handlerGUI;
    private boolean checkLeaderAction;
    private boolean checkNormalAction;
    private final ClientMain clientMain;
    private String action;

    public PlotScenarioGUI(HandlerGUI handlerGUI, ClientMain clientMain) {
        this.handlerGUI = handlerGUI;
        this.checkLeaderAction = false;
        this.checkNormalAction = false;
        this.clientMain = clientMain;
    }

    public void choiceAction(Stage stage) {
        GridPane root = new GridPane();
        Button playLeaderCardButton = new Button("Play leader card");
        Button discardLeaderCardButton = new Button("Discard Leader Card");
        Button pickResourceFromMarketButton = new Button("Pick Resource From Market");
        Button buyDevelopmentCardButton = new Button("Buy Development Card");
        Button activateProdButton = new Button("Activate Production Power");

        root.add(playLeaderCardButton, 1, 0);
        root.add(discardLeaderCardButton, 3, 0);
        root.add(pickResourceFromMarketButton, 0, 3);
        root.add(buyDevelopmentCardButton, 2, 3);
        root.add(activateProdButton, 4, 3);

        Scene scene = new Scene(root, 750, 90);

        if(!this.checkLeaderAction) {
            playLeaderCardButton.setOnAction(e -> {
                action = "PLAY LEADER CARD";
                playDiscardLeaderCard(stage);
                this.checkLeaderAction = true;
            });

            discardLeaderCardButton.setOnAction(e -> {
                action = "DISCARD LEADER CARD";
                playDiscardLeaderCard(stage);
                this.checkLeaderAction = true;
            });
        }

        if(!checkNormalAction) {
            pickResourceFromMarketButton.setOnAction(e -> {
                market(stage);
                this.checkNormalAction = true;
            });

            buyDevelopmentCardButton.setOnAction(e -> {
                buyDevelopmentCard(stage);
                this.checkNormalAction = true;
            });

            activateProdButton.setOnAction(e -> {
                activateProductionDevCards(stage);
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
        for (int i = 0; i < this.clientMain.getLeaderCards().length; i++) {
            //Creating a graphic (image)
            Image img = new Image(this.clientMain.getLeaderCards()[i].getImage());
            ImageView view = new ImageView(img);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            //Setting the location of the button
            if (i == 0 && !this.clientMain.getLeaderCards()[0].isPlayed()) {
                firstLeader.setTranslateX(x);
                firstLeader.setTranslateY(20);
                //Setting the size of the button
                firstLeader.setPrefSize(80, 80);
                //Setting a graphic to the button
                firstLeader.setGraphic(view);
                x = x + 200;
                root.getChildren().add(firstLeader);
            } else if (i == 1 && !this.clientMain.getLeaderCards()[1].isPlayed()) {
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
            if(action.equals("PLAY LEADER CARD")) this.handlerGUI.getMsg().sendPlayedLeader(0);
            else this.handlerGUI.getMsg().sendDiscardedLeader(0);
            choiceAction(stage);
        });

        secondLeader.setOnAction(e -> {
            if(action.equals("PLAY LEADER CARD")) this.handlerGUI.getMsg().sendPlayedLeader(1);
            else this.handlerGUI.getMsg().sendDiscardedLeader(1);
            choiceAction(stage);
        });

        if(!checkNormalAction) this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("choiceAction", stage);
        else waitForYouturn(stage);
    }

    public void market(Stage stage) {
        int x = 0, y;
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
                drawnMarbles(x, y, gc, this.clientMain.getMarket().getMarketArrangement()[i][j].getColour());
                x+=60;
            }
            y+=60;
        }
        drawnMarbles(x, y, gc, this.clientMain.getMarket().getExcessMarble().getColour());

        Pane rootButton = new Pane();
        Button[] buttonClick = new Button[7];
        x = 185;
        y = 190;
        for(int j = 0; j < 7; j++) {
            buttonClick[j] = new Button("Click\nHere!");
            rootButton.getChildren().add(buttonClick[j]);
            if(j < 4) {
                buttonClick[j].setLayoutX(x);
                buttonClick[j].setLayoutY(500);
                x+= 65;
            }
            else {
                buttonClick[j].setLayoutX(580);
                buttonClick[j].setLayoutY(y);
                y+= 65;
            }
        }
        root.getChildren().addAll(canvas, rootButton);

        Scene scene = new Scene(root, 650, 770);
        stage.setTitle("Pick resources from market");
        stage.setScene(scene);
        stage.show();

        x = 1;
        y = 0;
        int[] coordinates = new int[2];
        for(int k = 0; k < 7; k++) {
            int finalY = y;
            int finalX = x;
            int finalK = k;
            buttonClick[k].setOnAction(e -> {
                coordinates[0] = finalX;
                coordinates[1] = finalY;
                if(finalK < 4) { String[] resource = new String[3]; }
                else { String[] resource = new String[4]; }
                //putResources(stage, );
                if (!this.checkLeaderAction) choiceAction(stage);
                else waitForYouturn(stage);
            });
            if(k == 4) {
                x = 0;
                y = 0;
            }
            y++;
        }
    }

    public void putResources(Stage stage, String[] resource) {
        boolean checkExtraSpace = false;
        for(int i = 0; i < 2; i++) {
            if (this.clientMain.getLeaderCards()[i].isPlayed() && this.clientMain.getLeaderCards()[i] instanceof ExtraWarehouseSpaceLeaderCard) {
                checkExtraSpace = true;
            }
        }

        if(checkExtraSpace) {
            for(String s: resource) {

            }
        }
        //else this.handlerGUI.getMsg().sendMarketAction(, , , );
    }

    public void drawnMarbles(int x, int y, GraphicsContext gc, String colour) {
        switch (colour) {
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
                handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index], 250, 200);
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

    public void activateProductionDevCards(Stage stage) {
        Group root = new Group();
        int i;
        int[] dimPile = new int[3];
        int[] activate = new int[6];
        String[] whichInput = new String[6];
        int numBottons = 0;
        int x = 10;
        int index = 0;

        for (i = 0; i < 3; i++) {
            dimPile[i] = handlerGUI.getBoard().getPlayerboardDevelopmentCards()[i].length - 1;
            if (dimPile[i] != 0) numBottons++;
        }
        //Creating buttons
        Button[] arrayButtons = new Button[numBottons];
        for (int click = 0; click < numBottons + 1; click++) {
            for (i = 0; i < numBottons; i++) {
                //Creating a graphic (image)
                Image img = new Image(handlerGUI.getBoard().getPlayerboardDevelopmentCards()[i][dimPile[i]].getImage());
                arrayButtons[index] = new Button();
                handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index], 250, 200);
                x = x + 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }
            Button okBtn = new Button("Submit");
            okBtn.setLayoutX(300);
            okBtn.setLayoutY(300);
            Button noBtn = new Button("Decline");
            noBtn.setLayoutX(350);
            noBtn.setLayoutY(300);
            root.getChildren().addAll(okBtn, noBtn);

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            stage.setTitle("Buy development cards");
            stage.setScene(scene);
            stage.show();

            okBtn.setOnAction(e -> {
                activateBasicProductionPower(stage, activate, whichInput);
            });

            noBtn.setOnAction(e -> {
                activateBasicProductionPower(stage, null, whichInput);
            });


            for (int j = 0; j < numBottons; j++) {
                int finalJ = j;
                arrayButtons[j].setOnAction(e -> {
                    activate[finalJ] = 1;
                    if(clientMain.getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][dimPile[finalJ]].getDevelopmentCardInput().get("COINS") != 0)
                        whichInput[finalJ] = whichInput[finalJ] + "0";
                    if(clientMain.getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][dimPile[finalJ]].getDevelopmentCardInput().get("SHIELDS") != 0)
                        whichInput[finalJ] = whichInput[finalJ] + "1";
                    if(clientMain.getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][dimPile[finalJ]].getDevelopmentCardInput().get("SERVANTS") != 0)
                        whichInput[finalJ] = whichInput[finalJ] + "2";
                    if(clientMain.getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][dimPile[finalJ]].getDevelopmentCardInput().get("STONES") != 0)
                        whichInput[finalJ] = whichInput[finalJ] + "3";
                });
            }
        }
    }

    public void activateBasicProductionPower(Stage stage, int[] activate, String[] whichInput) {
        StringBuilder whichOutput = new StringBuilder();

        for(int i = 0; i < 3; i++) {
            String[] resources = new String[] {
                    "coin.png",
                    "servant.png",
                    "shield.png",
                    "stone.png"
            };

            if(i == 2) {
                resources = new String[] {
                        "redCross.png"
                };
            }

            Group root = new Group();
            //Creating buttons
            Button[] arrayButtons = new Button[5];

            int x = 10;
            int index = 0;
            for (String item : resources) {
                //Creating a graphic (image)
                Image img = new Image(item);
                arrayButtons[index] = new Button();
                handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index], 80, 80);
                x = x + 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }
            Button okBtn = new Button("Submit");
            okBtn.setLayoutX(300);
            okBtn.setLayoutY(300);
            Button noBtn = new Button("Decline");
            noBtn.setLayoutX(350);
            noBtn.setLayoutY(300);
            root.getChildren().addAll(okBtn, noBtn);

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            stage.setTitle("Activate basic production power");
            stage.setScene(scene);
            stage.show();

            okBtn.setOnAction(e -> {
                activate[3] = 1;
                activateExtraProdPower(stage, activate, whichOutput, whichInput);
            });

            noBtn.setOnAction(e -> {
                whichInput[3] = null;
                activateExtraProdPower(stage, activate, null, whichInput);
            });

            for(int j = 0; j < 4 && i < 2; j++) {
                int finalJ = j;
                arrayButtons[j].setOnAction(e -> {
                    whichInput[3] = whichInput[3] + finalJ;
                });
            }

            for(int j = 0; j < 5 && i == 2; j++) {
                int finalJ = j;
                arrayButtons[j].setOnAction(e -> {
                    whichOutput.append(finalJ);
                });
            }
        }
    }

    public void activateExtraProdPower(Stage stage, int[] activate, StringBuilder whichOutput, String[] whichInput) {
        int numBottons = 0;
        int indexLeader = 0;
        int index = 0;
        int x = 10;
        Group root = new Group();

        for(int i = 0; i < 2; i++) {
            if (handlerGUI.playerLeaders[i].isPlayed() && handlerGUI.playerLeaders[i] instanceof ExtraProductionPowerLeaderCard) {
                numBottons++;
                indexLeader = i;
            }
        }
        //Creating buttons
        Button[] arrayButtons = new Button[numBottons];
        for (int click = 0; click < numBottons + 1; click++) {
            for (int i = 0; i < numBottons; i++) {
                //Creating a graphic (image)
                if(numBottons == 1) {
                    Image img = new Image(handlerGUI.playerLeaders[indexLeader].getImage());
                    arrayButtons[index] = new Button();
                    handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index], 250, 200);
                    x = x + 200;
                    root.getChildren().add(arrayButtons[index]);
                    index++;
                }
            }

            Button okBtn = new Button("Submit");
            okBtn.setLayoutX(300);
            okBtn.setLayoutY(300);
            Button noBtn = new Button("Decline");
            noBtn.setLayoutX(350);
            noBtn.setLayoutY(300);
            root.getChildren().addAll(okBtn, noBtn);

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            stage.setTitle("Activate extra production power");
            stage.setScene(scene);
            stage.show();

            okBtn.setOnAction(e -> {
                pickResourceExtraProdPower(stage, activate, whichOutput);
            });

            noBtn.setOnAction(e -> {
                if(!checkLeaderAction) choiceAction(stage);
                else waitForYouturn(stage);
            });

            for(int j = 0; j < numBottons; j++) {
                int finalJ = j;
                arrayButtons[j].setOnAction(e -> {
                    activate[finalJ + 4] = 1;
                });
            }
        }
    }

    public void pickResourceExtraProdPower(Stage stage, int[] activate, StringBuilder whichOutput) {
        int num = 0;
        if(activate[4] == 1) num++;
        if(activate[5] == 1) num++;
        for(; num > 0; num--) {
            String[] resources = new String[]{
                    "coin.png",
                    "servant.png",
                    "shield.png",
                    "stone.png",
                    "redCross"
            };
            Group root = new Group();
            //Creating buttons
            Button[] arrayButtons = new Button[5];

            int x = 10;
            int index = 0;
            for (String item : resources) {
                //Creating a graphic (image)
                Image img = new Image(item);
                arrayButtons[index] = new Button();
                handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index], 80, 80);
                x = x + 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }
            Button okBtn = new Button("Submit");
            okBtn.setLayoutX(300);
            okBtn.setLayoutY(300);

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            stage.setTitle("Choose the resource you want to have.");
            stage.setScene(scene);
            stage.show();

            okBtn.setOnAction(e -> {
                handlerGUI.setActivation(activate);
                if(!checkLeaderAction) choiceAction(stage);
                else waitForYouturn(stage);
            });

            for(int j = 0; j < 5; j++) {
                int finalJ = j;
                arrayButtons[j].setOnAction(e -> {
                    whichOutput.append(finalJ);
                });
            }
        }
    }

    public void waitForYouturn(Stage stage) {
        handlerGUI.getGenericClassGUI().addLabelByCode("Your turn is ended, wait some minutes!", stage);
    }
}
