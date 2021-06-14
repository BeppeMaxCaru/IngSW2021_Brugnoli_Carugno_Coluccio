package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraProductionPowerLeaderCard;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PlotScenarioGUI {

    private final HandlerGUI handlerGUI;
    private String action;

    public PlotScenarioGUI(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
    }

    public void choiceAction(Stage stage) {
        GridPane root = new GridPane();
        Button playLeaderCardButton = new Button("Play leader card");
        Button discardLeaderCardButton = new Button("Discard Leader Card");
        Button pickResourceFromMarketButton = new Button("Pick Resource From Market");
        Button buyDevelopmentCardButton = new Button("Buy Development Card");
        Button activateProdButton = new Button("Activate Production Power");
        Button exitButton = new Button("Submit");

        root.add(playLeaderCardButton, 1, 0);
        root.add(discardLeaderCardButton, 3, 0);
        root.add(pickResourceFromMarketButton, 0, 3);
        root.add(buyDevelopmentCardButton, 2, 3);
        root.add(activateProdButton, 4, 3);
        root.add(exitButton, 4, 5);

        Scene scene = new Scene(root, 750, 90);

            playLeaderCardButton.setOnAction(e -> {
                action = "PLAY LEADER CARD";
                playDiscardLeaderCard(stage);
            });

            discardLeaderCardButton.setOnAction(e -> {
                action = "DISCARD LEADER CARD";
                playDiscardLeaderCard(stage);
            });

            pickResourceFromMarketButton.setOnAction(e -> {
                market(stage);
            });

            buyDevelopmentCardButton.setOnAction(e -> {
                buyDevelopmentCard(stage);
            });

            activateProdButton.setOnAction(e -> {
                activateProductionDevCards(stage);
            });

            exitButton.setOnAction(e -> {
                this.handlerGUI.getMsg().sendEndTurn();
            });

        stage.setTitle("Choose the action!");
        stage.setScene(scene);
        stage.show();
    }

    public void playDiscardLeaderCard(Stage stage) {
        Group root = new Group();
        Button firstLeader = new Button();
        Button secondLeader = new Button();

        int x = 10;
        for (int i = 0; i < this.handlerGUI.getClientMain().getLeaderCards().length; i++) {
            //Creating a graphic (image)
            Image img = new Image(this.handlerGUI.getClientMain().getLeaderCards()[i].getImage());
            if (i == 0 && !this.handlerGUI.getClientMain().getLeaderCards()[0].isPlayed()) {
                this.handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, firstLeader, 450, 150);
                x += 200;
                root.getChildren().add(firstLeader);
            } else if (i == 1 && !this.handlerGUI.getClientMain().getLeaderCards()[1].isPlayed()) {
                this.handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, secondLeader, 450, 150);
                x += 200;
                root.getChildren().add(secondLeader);
            }
        }

        //Setting the stage
        Scene scene = new Scene(root, 420, 300);
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
                drawnMarbles(x, y, gc, this.handlerGUI.getClientMain().getMarket().getMarketArrangement()[i][j].getColour());
                x+=60;
            }
            y+=60;
        }
        drawnMarbles(x, y, gc, this.handlerGUI.getClientMain().getMarket().getExcessMarble().getColour());

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
                String[] resource;
                if(finalK < 4) {
                    resource = new String[3];
                    for(int i = 0; i < 4; i++) {
                        resource[i] = resourceMarbles(this.handlerGUI.getClientMain().getMarket().getMarketArrangement()[i][finalK].getColour());
                    }
                }
                else {
                    resource = new String[4];
                    for(int i = 0; i < 3; i++) {
                        resource[i] = resourceMarbles(this.handlerGUI.getClientMain().getMarket().getMarketArrangement()[finalK - 4][i].getColour());
                    }
                }
                putResources(stage, coordinates, resource);
            });
            if(k == 4) {
                x = 0;
                y = 0;
            }
            y++;
        }
    }

    public void putResources(Stage stage, int[] coordinates, String[] resource) {
        /*boolean checkExtraSpace = false;
        String whichWL;
        for(int i = 0; i < 2; i++) {
            if (this.clientMain.getLeaderCards()[i].isPlayed() && this.clientMain.getLeaderCards()[i] instanceof ExtraWarehouseSpaceLeaderCard) {
                checkExtraSpace = true;
            }
        }

        if(checkExtraSpace) { */
        String whichWl;
            for(int i = 0; i < resource.length; i++) {
                if(!resource[i].equals(" ")) {
                    Image image = new Image(resource[i]);
                    ImageView imageView = new ImageView();
                    Pane root = new Pane(imageView);
                    imageView.setImage(image);
                    imageView.setLayoutX(10);
                    imageView.setLayoutY(10);
                    imageView.setFitWidth(100);
                    imageView.setPreserveRatio(true);
                    Button warehouse = new Button("Warehouse");
                    Button extra = new Button("Extra Warehouse");
                    Button submit = new Button("submit");
                    warehouse.setLayoutX(50);
                    extra.setLayoutX(70);
                    root.getChildren().addAll(warehouse, extra, submit);

                    Scene scene = new Scene(root, 595, 355);
                    stage.setTitle("Put the resources in the stores.");
                    stage.setScene(scene);
                    stage.show();

                    warehouse.setOnAction(e -> {
                    });

                    extra.setOnAction(e -> {
                    });

                    submit.setOnAction(e -> {
                    });
                }

            }
        }
        //else this.handlerGUI.getMsg().sendMarketAction(, , , );
    //}

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
                gc.setFill(Color.FLORALWHITE);
                break;
        }
        gc.fillOval(x, y, 40, 40);
    }

    public String resourceMarbles(String colour) {
        String resource;
        switch (colour) {
            case " YELLOW ":
                resource = "coin.png";
                break;
            case " BLUE ":
                resource = "schield.png";
                break;
            case " GREY ":
                resource = "stone.png";
                break;
            case " PURPLE ":
                resource = "servant.png";
                break;
            case " RED ":
                resource = "redCross.png";
                break;
            default:
                resource = " ";
                break;
        }
        return resource;
    }

    public void buyDevelopmentCard(Stage stage) {
        Group root = new Group();
        //Creating buttons
        Button[] arrayButtons = new Button[12];
        String[][] pickedResources = new String[2][4];
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 4; c++) {
                if (r == 0) pickedResources[r][c] = "0";
                else pickedResources[r][c] = "";
            }
        }

        int x = 10;
        int y = 20;
        int index = 0;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 4; j++) {
                //Creating a graphic (image)
                Image img = new Image(this.handlerGUI.getClientMain().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[i][j][0].getImage());
                arrayButtons[index] = new Button();
                this.handlerGUI.getGenericClassGUI().createIconButton(x, y, img, arrayButtons[index], 450, 150);
                x += 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }
            x = 10;
            y += 300;
        }

        //Setting the stage
        Scene scene = new Scene(root, 800, 900);
        stage.setTitle("Buy development card.");
        stage.setScene(scene);
        stage.show();

        int row = 0, column = 0;
        int[] coordinates = new int[2];
        for(int i = 0; i < 12; i++) {
            if(i == 4) { column = 0; row = 1; }
            else if(i == 8) { column = 0; row = 2; }
            int finalRow = row;
            int finalColumn = column;
            arrayButtons[i].setOnAction(e -> {
                coordinates[0] = finalRow;
                coordinates[1] = finalColumn;
                DevelopmentCard developmentCard = this.handlerGUI.getClientMain().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[coordinates[0]][coordinates[1]][0];
                int numResource;
                if(developmentCard.getDevelopmentCardCost().get("COINS") != 0) numResource = 0;
                else if(developmentCard.getDevelopmentCardCost().get("STONES") != 0) numResource = 1;
                else if(developmentCard.getDevelopmentCardCost().get("SHIELDS") != 0) numResource = 2;
                else numResource = 3;
                putPayedResource(stage, coordinates, numResource, pickedResources, developmentCard, 0);
            });
            column++;
        }
    }

    public void putPayedResource(Stage stage, int[] coordinates, int numResource, String[][] pickedResources, DevelopmentCard developmentCard, int sameResources) {
        String nameImage;
        int num, numSame;
        Map<Integer, String> resources = new HashMap<>();
        resources.put(0, "COINS");
        resources.put(1, "STONES");
        resources.put(2, "SHIELDS");
        resources.put(3, "SERVANTS");

        if(developmentCard.getDevelopmentCardCost().get("STONES") != 0 && numResource < 1) num = 1;
        else if(developmentCard.getDevelopmentCardCost().get("SHIELDS") != 0 && numResource < 2) num = 2;
        else if(developmentCard.getDevelopmentCardCost().get("SERVANTS") != 0 && numResource < 3) num = 3;
        else num = 4;

        numSame = sameResources + 1;

        String resource = resources.get(numResource);
        switch (resource) {
            case "COINS":
                nameImage = "coin.png";
                break;
            case "STONES":
                nameImage = "stone.png";
                break;
            case "SHIELDS":
                nameImage = "shield.png";
                break;
            case "SERVANTS":
                nameImage = "servant.png";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resource);
        }

        Image image = new Image(nameImage);
        ImageView imageView = new ImageView();
        Pane root = new Pane(imageView);
        imageView.setImage(image);
        imageView.setLayoutX(10);
        imageView.setLayoutY(10);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        // non funziona il tooltip
        //Tooltip.install(imageView, new Tooltip((i + 1) + " of " + developmentCard.getDevelopmentCardCost().get(resource)));

        Button warehouse = new Button("Warehouse");
        warehouse.setLayoutX(300);
        warehouse.setLayoutY(60);
        Button chest = new Button("Chest");
        chest.setLayoutX(300);
        chest.setLayoutY(100);
        Button extra = new Button("Extra Warehouse");
        extra.setLayoutX(300);
        extra.setLayoutY(140);
        root.getChildren().addAll(warehouse, chest, extra);

        //Setting the stage
        Scene scene = new Scene(root, 500, 300);
        stage.setTitle("Where do you want to pick the resources?");
        stage.setScene(scene);
        stage.show();

        pickedResources[0][numResource] = developmentCard.getDevelopmentCardCost().get(resource).toString();
        warehouse.setOnAction(e -> {
            pickedResources[1][numResource] = pickedResources[1][numResource] + "w";
            if(numSame == developmentCard.getDevelopmentCardCost().get(resource)) {
                if(num < 4) putPayedResource(stage, coordinates, num, pickedResources, developmentCard, 0);
                else {
                    putCardOnPlayerBoard(stage, coordinates, pickedResources);
                }
            }
            else putPayedResource(stage, coordinates, numResource, pickedResources, developmentCard, numSame);
        });

        chest.setOnAction(e -> {
            pickedResources[1][numResource] = pickedResources[1][numResource] + "c";
            if(numSame == developmentCard.getDevelopmentCardCost().get(resource)) {
                if(num < 4) putPayedResource(stage, coordinates, num, pickedResources, developmentCard, 0);
                else {
                    putCardOnPlayerBoard(stage, coordinates, pickedResources);
                }
            }
            else putPayedResource(stage, coordinates, numResource, pickedResources, developmentCard, numSame);
        });

        extra.setOnAction(e -> {
            pickedResources[1][numResource] = pickedResources[1][numResource] + "l";
            if(numSame == developmentCard.getDevelopmentCardCost().get(resource)) {
                if(num < 4) putPayedResource(stage, coordinates, num, pickedResources, developmentCard, 0);
                else {
                    putCardOnPlayerBoard(stage, coordinates, pickedResources);
                }
            }
            else putPayedResource(stage, coordinates, numResource, pickedResources, developmentCard, numSame);
        });
    }

    public void putCardOnPlayerBoard(Stage stage, int[] coordinates, String[][] pickedResources) {
        int[] dimPile = new int[3];
        int i, x = 90;

        Image image = new Image("devCardPlayerBoard.png");
        ImageView imageView1 = new ImageView();
        imageView1.setImage(image);
        imageView1.setX(10);
        imageView1.setY(10);
        imageView1.setFitWidth(575);
        imageView1.setPreserveRatio(true);
        Pane root = new Pane(imageView1);

        // Trovo la lunghezza della pila di carte.
        /*for (i = 0; i < 3; i++) {
            if(this.clientMain.getPlayerboard().getPlayerboardDevelopmentCards()[i] != null)
                dimPile[i] = this.clientMain.getPlayerboard().getPlayerboardDevelopmentCards()[i].length - 1;
            else dimPile[i] = 0;
        } */

        //Creating buttons
        Button[] arrayButtons = new Button[3];
        for (i = 0; i < 3; i++) {
            //Creating a graphic (image)
            /*if(this.clientMain.getPlayerboard().getPlayerboardDevelopmentCards()[i] != null) {
                Image img = new Image(this.clientMain.getPlayerboard().getPlayerboardDevelopmentCards()[i][dimPile[i]].getImage());
                ImageView imageView2 = new ImageView();
                imageView2.setImage(image);
                imageView2.setLayoutX(10);
                imageView2.setLayoutY(10);
                imageView2.setFitWidth(200);
                imageView2.setPreserveRatio(true); */
                arrayButtons[i] = new Button("Click\nHere!");
                arrayButtons[i].setLayoutX(x);
                arrayButtons[i].setLayoutY(250);
                root.getChildren().add(arrayButtons[i]);
                x+= 170;
            //}
        }

        //Setting the stage
        Scene scene = new Scene(root, 600, 470);
        stage.setTitle("Where do you want to pick the resources from?");
        stage.setScene(scene);
        stage.show();

        for(int j = 0; j < 3; j++) {
            int finalJ = j;
            arrayButtons[j].setOnAction(e -> {
                int[] quantity = new int[4];
                for(int k = 0; k < quantity.length; k++) quantity[k] = Integer.parseInt(pickedResources[0][k]);
                this.handlerGUI.getMsg().sendBuyCardAction(coordinates[0], coordinates[1], quantity, pickedResources[1], finalJ);
                choiceAction(stage);
            });
        }
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
                handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[index], 250, 200);
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
                    if(handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][dimPile[finalJ]].getDevelopmentCardInput().get("COINS") != 0)
                        whichInput[finalJ] = whichInput[finalJ] + "0";
                    if(handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][dimPile[finalJ]].getDevelopmentCardInput().get("SHIELDS") != 0)
                        whichInput[finalJ] = whichInput[finalJ] + "1";
                    if(handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][dimPile[finalJ]].getDevelopmentCardInput().get("SERVANTS") != 0)
                        whichInput[finalJ] = whichInput[finalJ] + "2";
                    if(handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][dimPile[finalJ]].getDevelopmentCardInput().get("STONES") != 0)
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
                handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[index], 80, 80);
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
                    handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[index], 250, 200);
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
                choiceAction(stage);
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
                handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[index], 80, 80);
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
                choiceAction(stage);
            });

            for(int j = 0; j < 5; j++) {
                int finalJ = j;
                arrayButtons[j].setOnAction(e -> {
                    whichOutput.append(finalJ);
                });
            }
        }
    }

    public void waitForYourTurn(Stage stage) {
        handlerGUI.getGenericClassGUI().addLabelByCode("Your turn is ended, wait some minutes!", stage);
    }

}

