package Communication.ClientSide.RenderingView.GUI;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraProductionPowerLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraWarehouseSpaceLeaderCard;
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


import java.util.HashMap;
import java.util.Map;

public class PlotScenarioGUI {

    private final HandlerGUI handlerGUI;
    private String action;

    public PlotScenarioGUI(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
    }

    public void choiceAction(Stage stage) {
        this.handlerGUI.getPlayerBoardScenario().PlayerBoard();

        GridPane root = new GridPane();
        int[] activate = new int[6];
        String[] whichOutput = new String[6];

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
            this.action = "PLAY LEADER CARD";
            playDiscardLeaderCard(stage);
        });

        discardLeaderCardButton.setOnAction(e -> {
            this.action = "DISCARD LEADER CARD";
            playDiscardLeaderCard(stage);
        });

        pickResourceFromMarketButton.setOnAction(e -> {
            market(stage);
        });

        buyDevelopmentCardButton.setOnAction(e -> {
            buyDevelopmentCard(stage);
        });

        activateProdButton.setOnAction(e -> activateProductionDevCards(stage, activate, whichOutput));

        exitButton.setOnAction(e -> {
            this.handlerGUI.getMsg().sendEndTurn();
            choiceAction(stage);
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
        //Qui va 2
        for (int i = 0; i < this.handlerGUI.getClientMain().getLeaderCards().length; i++) {
            //Creating a graphic (image)
            if (this.handlerGUI.getClientMain().getLeaderCards()[i] != null) {
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
        }

        Button decline = new Button("Decline");
        decline.setLayoutX(300);
        decline.setLayoutY(270);
        root.getChildren().add(decline);

        //Setting the stage
        Scene scene = new Scene(root, 420, 300);
        stage.setTitle("Choose a leader card");
        stage.setScene(scene);
        stage.show();

        decline.setOnAction(e -> {
            choiceAction(stage);
        });

        firstLeader.setOnAction(e -> {
            // Multiplayer
            if(this.handlerGUI.getGameMode() == 1) {
                if (action.equals("PLAY LEADER CARD")) this.handlerGUI.getMsg().sendPlayedLeader(0);
                else this.handlerGUI.getMsg().sendDiscardedLeader(0);
            }
            // single player
            else {
                if(this.handlerGUI.checkLocalLeaders(this.handlerGUI.getClientMain().getLocalPlayers()[0], 0))
                {
                    if(!this.handlerGUI.getClientMain().getLocalPlayers()[0].playLeaderCard(0))
                        this.handlerGUI.notValidAction();
                }
            }
            choiceAction(stage);
        });

        secondLeader.setOnAction(e -> {
            // Multiplayer
            if(this.handlerGUI.getGameMode() == 1) {
                if (action.equals("PLAY LEADER CARD")) this.handlerGUI.getMsg().sendPlayedLeader(1);
                else this.handlerGUI.getMsg().sendDiscardedLeader(1);
            }
            // single player
            else {
                if(this.handlerGUI.checkLocalLeaders(this.handlerGUI.getClientMain().getLocalPlayers()[0], 1))
                {
                    if(!this.handlerGUI.getClientMain().getLocalPlayers()[0].playLeaderCard(1))
                        this.handlerGUI.notValidAction();
                }
            }
            choiceAction(stage);
        });
    }

    public void market(Stage stage) {
        int x = 0, y;
        String[] whichWl = new String[4];
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
                    for(int i = 0; i < 3; i++) {
                        resource[i] = resourceMarbles(this.handlerGUI.getClientMain().getMarket().getMarketArrangement()[i][finalK].getColour());
                    }
                }
                else {
                    resource = new String[4];
                    for(int i = 0; i < 4; i++) {
                        resource[i] = resourceMarbles(this.handlerGUI.getClientMain().getMarket().getMarketArrangement()[finalK - 4][i].getColour());
                    }
                }
                putResources(stage, coordinates, resource, 0, whichWl, "X");
            });
            if(k == 3) {
                x = 0;
                y = -1;
            }
            y++;
        }
    }

    public void putResources(Stage stage, int[] coordinates, String[] resource, int index, String[] whichWl, String whiteMarble) {
        boolean checkExtraSpace = false;
        int numIndex = index + 1;
        String parameter;
        if(coordinates[0] == 0) parameter = "ROW";
        else parameter = "COLUMN";

        for(int i = 0; i < 2; i++) {
            if(this.handlerGUI.getClientMain().getLeaderCards()[i] != null){
                if (this.handlerGUI.getClientMain().getLeaderCards()[i].isPlayed() && this.handlerGUI.getClientMain().getLeaderCards()[i] instanceof ExtraWarehouseSpaceLeaderCard) {
                    checkExtraSpace = true;
                }
            }
        }

        if(checkExtraSpace) {
            if (!resource[index].equals(" ")) {
                Image image = new Image(resource[index]);
                ImageView imageView = new ImageView();
                Pane root = new Pane(imageView);
                imageView.setImage(image);
                imageView.setLayoutX(10);
                imageView.setLayoutY(10);
                imageView.setFitWidth(200);
                imageView.setPreserveRatio(true);

                Button warehouse = new Button("Warehouse");
                warehouse.setLayoutX(300);
                warehouse.setLayoutY(60);
                Button extra = new Button("Extra Warehouse");
                warehouse.setLayoutX(300);
                warehouse.setLayoutY(100);
                root.getChildren().addAll(warehouse, extra);

                Scene scene = new Scene(root, 595, 355);
                stage.setTitle("Put the resources in the stores.");
                stage.setScene(scene);
                stage.show();

                warehouse.setOnAction(e -> {
                    whichWl[index] = "W";
                    if(index == resource.length - 1) {
                        String whichWl2 = null;
                        for (String s : whichWl) {
                            if (s != null && whichWl2 == null) whichWl2 = s;
                            else if(s != null) whichWl2 += s;
                        }
                        this.handlerGUI.getMsg().sendMarketAction(parameter, coordinates[1], whichWl2, whiteMarble);
                        choiceAction(stage);
                    }
                    else putResources(stage, coordinates, resource, numIndex, whichWl, whiteMarble);
                });

                extra.setOnAction(e -> {
                    whichWl[index] = "L";
                    if(index == resource.length - 1) {
                        String whichWl2 = null;
                        for (String s : whichWl) {
                            if (s != null && whichWl2 == null) whichWl2 = s;
                            else if(s != null) whichWl2 += s;
                        }
                        this.handlerGUI.getMsg().sendMarketAction(parameter, coordinates[1], whichWl2, whiteMarble);
                        choiceAction(stage);
                    }
                    else putResources(stage, coordinates, resource, numIndex, whichWl, whiteMarble);
                });
            }
            else {
                if(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[0] != null) {
                    whiteMarble(stage, coordinates, resource, index, whichWl);
                }
            }
        }
        else {
            if(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[0] != null) {
                whiteMarble(stage, coordinates, resource, index, whichWl);
            }

            if(resource.length == 3) {
                this.handlerGUI.getMsg().sendMarketAction(parameter, coordinates[1], "WWW", whiteMarble);
                choiceAction(stage);
            }
            else {
                this.handlerGUI.getMsg().sendMarketAction(parameter, coordinates[1], "WWWW", whiteMarble);
                choiceAction(stage);
            }
        }
    }

    public void whiteMarble(Stage stage, int[] coordinates, String[] resource, int numIndex, String[] whichWl) {

        for(int i = 0; i < this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles().length; i++) {
            if(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[1] == null) {
                resource[numIndex] = resourceMarbles(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[0].getColour());
                putResources(stage, coordinates, resource, numIndex, whichWl, "0");
            }
            else {
                Group root = new Group();
                String resource1 = resourceMarbles(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[0].getColour());
                Image image1 = new Image(resource1);
                Button button1 = new Button();
                this.handlerGUI.getGenericClassGUI().createIconButton(10,50, image1, button1, 80, 80);

                String resource2 = resourceMarbles(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[1].getColour());
                Image image2 = new Image(resource2);
                Button button2 = new Button();
                this.handlerGUI.getGenericClassGUI().createIconButton(210,50, image2, button2, 80, 80);

                Button declineButton = new Button("I don't want to activate any leader cards.");
                declineButton.setLayoutX(220);
                declineButton.setLayoutY(70);

                root.getChildren().addAll(button1, button2, declineButton);

                Scene scene = new Scene(root, 595, 355);
                stage.setTitle("Choose the resource for the white marble.");
                stage.setScene(scene);
                stage.show();

                button1.setOnAction(e -> {
                    resource[numIndex] = resourceMarbles(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[0].getColour());
                    putResources(stage, coordinates, resource, numIndex, whichWl, "0");
                });

                button2.setOnAction(e -> {
                    resource[numIndex] = resourceMarbles(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[1].getColour());
                    putResources(stage, coordinates, resource, numIndex, whichWl, "1");
                });

                declineButton.setOnAction(e -> {
                    resource[numIndex] = "x";
                    putResources(stage, coordinates, resource, numIndex + 1, whichWl, "X");
                });
            }
        }
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
                else if(developmentCard.getDevelopmentCardCost().get("SERVANTS") != 0) numResource = 1;
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
        resources.put(1, "SERVANTS");
        resources.put(2, "SHIELDS");
        resources.put(3, "STONES");

        if(developmentCard.getDevelopmentCardCost().get("SERVANTS") != 0 && numResource < 1) num = 1;
        else if(developmentCard.getDevelopmentCardCost().get("SHIELDS") != 0 && numResource < 2) num = 2;
        else if(developmentCard.getDevelopmentCardCost().get("STONES") != 0 && numResource < 3) num = 3;
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
            pickedResources[1][numResource] = pickedResources[1][numResource] + "W";
            if(numSame == developmentCard.getDevelopmentCardCost().get(resource)) {
                if(num < 4) putPayedResource(stage, coordinates, num, pickedResources, developmentCard, 0);
                else {
                    putCardOnPlayerBoard(stage, coordinates, pickedResources);
                }
            }
            else putPayedResource(stage, coordinates, numResource, pickedResources, developmentCard, numSame);
        });

        chest.setOnAction(e -> {
            pickedResources[1][numResource] = pickedResources[1][numResource] + "C";
            if(numSame == developmentCard.getDevelopmentCardCost().get(resource)) {
                if(num < 4) putPayedResource(stage, coordinates, num, pickedResources, developmentCard, 0);
                else {
                    putCardOnPlayerBoard(stage, coordinates, pickedResources);
                }
            }
            else putPayedResource(stage, coordinates, numResource, pickedResources, developmentCard, numSame);
        });

        extra.setOnAction(e -> {
            pickedResources[1][numResource] = pickedResources[1][numResource] + "L";
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
        int i, x = 90;

        Image image = new Image("devCardPlayerBoard.png");
        ImageView imageView1 = new ImageView();
        imageView1.setImage(image);
        imageView1.setX(10);
        imageView1.setY(10);
        imageView1.setFitWidth(575);
        imageView1.setPreserveRatio(true);
        Pane root = new Pane(imageView1);

        int[] dimPile = new int[3];
        for(int col = 0; col < 3; col++) {
            int row;
            for(row = 2; row > 0; row--) {
                if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[row][col] != null)
                    break;
            }
            if(row == -1) row = 0;
            dimPile[col] = row;
        }

        //Creating buttons
        Button[] arrayButtons = new Button[3];
        for (i = 0; i < 3; i++) {
            //Creating a graphic (image)
            if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[dimPile[i]][i] != null) {
                Image img = new Image(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[dimPile[i]][i].getImage());
               ImageView imageView2 = new ImageView();
               imageView2.setImage(img);
               imageView2.setLayoutX(x);
               imageView2.setLayoutY(250);
               imageView2.setFitWidth(200);
               imageView2.setPreserveRatio(true);
           }
           arrayButtons[i] = new Button("Click\nHere!");
           arrayButtons[i].setLayoutX(x);
           arrayButtons[i].setLayoutY(250);
           root.getChildren().add(arrayButtons[i]);
           x+= 170;

        }

        //Setting the stage
        Scene scene = new Scene(root, 600, 470);
        stage.setTitle("Where do you want to put the development card?");
        stage.setScene(scene);
        stage.show();

        for(int j = 0; j < 3; j++) {
            int finalJ = j;
            arrayButtons[j].setOnAction(e -> {
                int[] quantity = new int[4];
                for(int k = 0; k < quantity.length; k++) quantity[k] = Integer.parseInt(pickedResources[0][k]);
                this.handlerGUI.getMsg().sendBuyCardAction(coordinates[1], 3 - coordinates[0], quantity, pickedResources[1], finalJ);
                choiceAction(stage);
            });
        }
    }

    public void activateProductionDevCards(Stage stage, int[] activate, String[] whichInput) {
        int i;
        int numBottons = 0;
        int x = 10;
        StringBuilder string = new StringBuilder();

        for (i = 0; i < 3; i++) {
            if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[i][0] != null) numBottons++;
        }

        Image image = new Image("devCardPlayerBoard.png");
        ImageView imageView1 = new ImageView();
        imageView1.setImage(image);
        imageView1.setX(10);
        imageView1.setY(10);
        imageView1.setFitWidth(575);
        imageView1.setPreserveRatio(true);
        Pane root = new Pane(imageView1);

        //Creating buttons
        Button[] arrayButtons = new Button[numBottons];
        for (i = 0; i < numBottons; i++) {
            //Creating a graphic (image)
            if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[i][0] != null) {
                Image img = new Image(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[i][0].getImage());
                arrayButtons[i] = new Button();
                this.handlerGUI.getGenericClassGUI().createIconButton(x, 50, img, arrayButtons[i], 250, 200);
                x = x + 200;
                root.getChildren().add(arrayButtons[i]);
            }
        }

        Button okBtn = new Button("Submit");
        if(numBottons > 0) {
            okBtn.setLayoutX(470);
            okBtn.setLayoutY(400);
            root.getChildren().add(okBtn);
        }

        Button noBtn = new Button("Decline");
        noBtn.setLayoutX(530);
        noBtn.setLayoutY(400);
        root.getChildren().add(noBtn);

        //Setting the stage
        Scene scene = new Scene(root, 600, 470);
        stage.setTitle("Activate production power");
        stage.setScene(scene);
        stage.show();

        okBtn.setOnAction(e -> {
            activateBasicProductionPower(stage, activate, whichInput, 0);
        });

        noBtn.setOnAction(e -> {
            for(int j = 0; j < 3; j++) activate[j] = 0;
            activateBasicProductionPower(stage, activate, whichInput, 0);
        });

        for (int j = 0; j < numBottons; j++) {
            int finalJ = j;
            arrayButtons[j].setOnAction(e -> {
                String num = "1";
                if(activate[finalJ] != 1) {
                    activate[finalJ] = 1;
                    if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][0].getDevelopmentCardInput().get("COINS") != 0) {
                        string.append("0");
                        if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][0].getDevelopmentCardInput().get("COINS") > 1)
                            num = "2";
                    }
                    if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][0].getDevelopmentCardInput().get("SHIELDS") != 0) {
                        string.append("1");
                        if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][0].getDevelopmentCardInput().get("SHIELDS") > 1)
                            num = "2";
                    }
                    if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][0].getDevelopmentCardInput().get("SERVANTS") != 0) {
                        string.append("2");
                        if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][0].getDevelopmentCardInput().get("SERVANTS") > 1)
                            num = "2";
                    }
                    if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][0].getDevelopmentCardInput().get("STONES") != 0) {
                        string.append("3");
                        if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[finalJ][0].getDevelopmentCardInput().get("STONES") > 0)
                            num = "2";
                    }
                    whichInput[finalJ] = string.toString();
                    putResourcePayedDevCard(stage, activate, whichInput, finalJ, num,0, null);
                }
                else activateProductionDevCards(stage, activate, whichInput);
            });
        }
    }

    public void putResourcePayedDevCard(Stage stage, int[] activate, String[] whichInput, int index, String num, int charResource, String[] whichOutput) {
        String nameImage;

        switch (whichInput[index].charAt(charResource)) {
            case '0':
                nameImage = "coin.png";
                break;
            case '1':
                nameImage = "servant.png";
                break;
            case '2':
                nameImage = "shield.png";
                break;
            case '3':
                nameImage = "stone.png";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + whichInput[index]);
        }

        Image image = new Image(nameImage);
        ImageView imageView = new ImageView();
        Pane root = new Pane(imageView);
        imageView.setImage(image);
        imageView.setLayoutX(10);
        imageView.setLayoutY(10);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

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

        warehouse.setOnAction(e -> {
            actionOnWhichInput(stage, activate, whichInput, index, num, "W", whichOutput);
        });

        chest.setOnAction(e -> {
            actionOnWhichInput(stage, activate, whichInput, index, num, "C", whichOutput);
        });

        extra.setOnAction(e -> {
            actionOnWhichInput(stage, activate, whichInput, index, num, "L", whichOutput);
        });
    }

    public void actionOnWhichInput(Stage stage, int[] activate, String[] whichInput, int index, String num, String reserve, String[] whichOutput) {
        if(whichInput[index].length() == 1 && num.equals("1")) {
            whichInput[index] += num + reserve;
            redirect(index, activate, stage, whichInput, whichOutput);
        }

        else if(whichInput[index].length() == 1 && num.equals("2")) {
            whichInput[index] += num + reserve;
            putResourcePayedDevCard(stage, activate, whichInput, index, num, 0, whichOutput);
        }

        else if(whichInput[index].length() == 3 && num.equals("2") && whichInput[index].charAt(2) == reserve.charAt(0)) {
            redirect(index, activate, stage, whichInput, whichOutput);
        }

        else if(whichInput[index].length() == 3 && num.equals("2") && whichInput[index].charAt(2) != reserve.charAt(0)) {
            whichInput[index] = whichInput[index].charAt(0) + "1" + whichInput[index].charAt(2) + whichInput[index].charAt(0) + "1" + reserve;
            redirect(index, activate, stage, whichInput, whichOutput);
        }
        else if(whichInput[index].length() == 2 && num.equals("1")) {
            whichInput[index] = whichInput[index].charAt(0) + "1" + reserve + whichInput[index].charAt(1);
            putResourcePayedDevCard(stage, activate, whichInput, index, num, 3, whichOutput);
        }

        else if(whichInput[index].length() == 4 && num.equals("1")) {
            whichInput[index] += "1" + reserve;
            redirect(index, activate, stage, whichInput, whichOutput);
        }
    }

    public void redirect(int index, int[] activate, Stage stage, String[] whichInput, String[] whichOutput) {
        if(index < 3 && (activate[0] != 1 || activate[1] != 1 || activate[2] != 1)) activateProductionDevCards(stage, activate, whichInput);
        else if(index < 3) activateBasicProductionPower(stage, activate, whichInput, 0);
        else if(index == 3) activateExtraProdPower(stage, activate, whichOutput, whichInput);
        else if(activate[4] != 1 || activate[5] != 1) activateExtraProdPower(stage, activate, whichOutput, whichInput);
    }

    public void activateBasicProductionPower(Stage stage, int[] activate, String[] whichInput, int index) {
        String[] whichOutput = new String[3];
        int indexPar = index + 1;
        int iter;
        String[] resources;

        resources = new String[] {
                "coin.png",
                "servant.png",
                "shield.png",
                "stone.png"
        };

        Group root = new Group();
        //Creating buttons
        Button[] arrayButtons = new Button[4];

        int x = 10;
        for (int i = 0; i < resources.length; i++) {
            //Creating a graphic (image)
            Image img = new Image(resources[i]);
            arrayButtons[i] = new Button();
            this.handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[i], 80, 80);
            x = x + 200;
            root.getChildren().add(arrayButtons[i]);
        }

        Button noBtn = new Button("I don't want to activate this production power!");
        noBtn.setLayoutX(10);
        noBtn.setLayoutY(120);
        root.getChildren().add(noBtn);

        //Setting the stage
        Scene scene = new Scene(root, 800, 150);
        stage.setTitle("Activate basic production power");
        stage.setScene(scene);
        stage.show();

        noBtn.setOnAction(e -> {
            activate[3] = 0;
            whichOutput[0] = "-1";
            whichInput[3] = "-1";
            activateExtraProdPower(stage, activate, whichOutput, whichInput);
        });

        for(int j = 0; j < 4; j++) {
            int finalJ = j;
            arrayButtons[j].setOnAction(e -> {
                if(index < 2) {
                    if(whichInput[3] == null) whichInput[3] = finalJ + "";
                    else whichInput[3] += finalJ;
                    activateBasicProductionPower(stage, activate, whichInput, indexPar);
                }
                else {
                    whichOutput[0] = String.valueOf(finalJ);
                    String num;
                    if(whichInput[3].charAt(0) == whichInput[3].charAt(1)) {
                        num = "2";
                        whichInput[3] = whichInput[3].charAt(0) + "";
                    }
                    else num = "1";
                    activate[3] = 1;
                    putResourcePayedDevCard(stage, activate, whichInput, 3, num, 0, whichOutput);
                }
            });
        }
    }

    public void activateExtraProdPower(Stage stage, int[] activate, String[] whichOutput, String[] whichInput) {
        int numBottons = 0;
        int indexLeader = 0;
        int index = 0;
        int x = 10;
        Group root = new Group();
        Image img;

        for(int i = 0; i < 2; i++) {
            if (this.handlerGUI.getClientMain().getLeaderCards()[i] != null && this.handlerGUI.getClientMain().getLeaderCards()[i].isPlayed() && this.handlerGUI.getClientMain().getLeaderCards()[i] instanceof ExtraProductionPowerLeaderCard) {
                numBottons++;
                indexLeader = i;
            }
        }
        if(numBottons > 0) {
            //Creating buttons
            Button[] arrayButtons = new Button[numBottons];
            for (int i = 0; i < numBottons; i++) {
                //Creating a graphic (image)
                if (numBottons == 1)
                    img = new Image(this.handlerGUI.getClientMain().getLeaderCards()[indexLeader].getImage());
                else img = new Image(this.handlerGUI.getClientMain().getLeaderCards()[i].getImage());
                arrayButtons[index] = new Button();
                this.handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[index], 250, 200);
                x = x + 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }

            Button okBtn = new Button("Submit");
            okBtn.setLayoutX(300);
            okBtn.setLayoutY(300);
            root.getChildren().add(okBtn);

            Button noBtn = new Button("I don't want to activate this production power!");
            noBtn.setLayoutX(350);
            noBtn.setLayoutY(300);
            root.getChildren().add(noBtn);

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            stage.setTitle("Activate extra production power");
            stage.setScene(scene);
            stage.show();

            okBtn.setOnAction(e -> {
                if(activate[4] == 0 && activate[5] == 0) {
                    this.handlerGUI.getMsg().sendActivationProdAction(activate, whichInput, whichOutput);
                    choiceAction(stage);
                }
                else {
                    int num = 0;
                    if(activate[4] == 1) num++;
                    if(activate[5] == 1) num++;
                    pickResourceExtraProdPower(stage, activate, whichOutput, whichInput, num);
                }
            });

            noBtn.setOnAction(e -> {
                activate[4] = 0;
                activate[5] = 0;
                whichInput[4] = null;
                whichInput[5] = null;
                whichOutput[1] = null;
                whichOutput[2] = null;
                this.handlerGUI.getMsg().sendActivationProdAction(activate, whichInput, whichOutput);
                choiceAction(stage);
            });

            for (int j = 0; j < numBottons; j++) {
                int finalJ = j;
                arrayButtons[j].setOnAction(e -> {
                    activate[finalJ + 4] = 1;
                    whichInput[finalJ + 4] = this.handlerGUI.getClientMain().getPlayerboard().getExtraProductionPowerInput()[finalJ];
                    putResourcePayedDevCard(stage, activate, whichInput, finalJ + 4, "1", 0, whichOutput);
                });
            }
        }
        else {
            this.handlerGUI.getMsg().sendActivationProdAction(activate, whichInput, whichOutput);
            choiceAction(stage);
        }
    }

    public void pickResourceExtraProdPower(Stage stage, int[] activate, String[] whichOutput, String[] whichInput, int num) {
        int numPar = num - 1;

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
            this.handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[index], 80, 80);
            x = x + 200;
            root.getChildren().add(arrayButtons[index]);
            index++;
        }

        //Setting the stage
        Scene scene = new Scene(root, 740, 130);
        stage.setTitle("Choose the resource you want to have.");
        stage.setScene(scene);
        stage.show();

        for(int j = 0; j < 5; j++) {
            int finalJ = j;
            arrayButtons[j].setOnAction(e -> {
                if(num == 2) whichOutput[1] = String.valueOf(finalJ);
                else if(num == 1) whichOutput[2] = String.valueOf(finalJ);
                if(numPar == 0) {
                    for (int k = 0; k < 6; k++)
                        this.handlerGUI.getMsg().sendActivationProdAction(activate, whichInput, whichOutput);
                    choiceAction(stage);
                }
                else pickResourceExtraProdPower(stage, activate, whichOutput, whichInput, numPar);
            });
        }
    }

    public void waitForYourchoiceAction(Stage stage) {
        this.handlerGUI.getGenericClassGUI().addLabelByCode("Your choiceAction is ended, wait some minutes!", stage);
    }
}

