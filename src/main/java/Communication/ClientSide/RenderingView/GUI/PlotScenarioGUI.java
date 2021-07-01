package Communication.ClientSide.RenderingView.GUI;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.DiscountDevelopmentCardsLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraProductionPowerLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraWarehouseSpaceLeaderCard;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class PlotScenarioGUI implements Runnable{

    private final HandlerGUI handlerGUI;
    private final Stage stage;
    private String action;
    private boolean mainAction;

    public PlotScenarioGUI(HandlerGUI handlerGUI, Stage stage) {
        this.handlerGUI = handlerGUI;
        this.stage = stage;
        this.mainAction = false;
    }
    
    @Override
    public void run() {
        choiceAction( );
    }

    /**
     * Method for choosing the actions, finish the turn and quit the game
     */
    public void choiceAction() {
       if(this.handlerGUI.getGameMode() == 0) {
           if(this.handlerGUI.endLocalGame(this.handlerGUI.getClientMain().getLocalPlayers())) {
               Platform.runLater(this.handlerGUI.getEndGameScenario());
           }
           else Platform.runLater(this.handlerGUI.getPlayerBoardScenario());
       }

        Group root = new Group();
        int[] activate = new int[6];
        String[] whichOutput = new String[6];

        Button playLeaderCardButton = new Button("Play leader card");
        playLeaderCardButton.setLayoutX(10);
        playLeaderCardButton.setLayoutY(10);
        Button discardLeaderCardButton = new Button("Discard leader card");
        discardLeaderCardButton.setLayoutX(130);
        discardLeaderCardButton.setLayoutY(10);
        Button pickResourceFromMarketButton = new Button("Pick resource from market");
        pickResourceFromMarketButton.setLayoutX(10);
        pickResourceFromMarketButton.setLayoutY(60);
        Button buyDevelopmentCardButton = new Button("Buy development card");
        buyDevelopmentCardButton.setLayoutX(190);
        buyDevelopmentCardButton.setLayoutY(60);
        Button activateProdButton = new Button("Activate production power");
        activateProdButton.setLayoutX(350);
        activateProdButton.setLayoutY(60);
        Button quitButton = new Button("Quit the game");
        quitButton.setLayoutX(10);
        quitButton.setLayoutY(110);
        Button exitButton = new Button("Submit");
        exitButton.setLayoutX(110);
        exitButton.setLayoutY(110);


        root.getChildren().addAll(playLeaderCardButton, discardLeaderCardButton, pickResourceFromMarketButton,
                buyDevelopmentCardButton, activateProdButton, quitButton, exitButton);

        Scene scene = new Scene(root, 650, 160);

        playLeaderCardButton.setOnAction(e -> {
            this.action = "PLAY LEADER CARD";
            playDiscardLeaderCard();
        });

        discardLeaderCardButton.setOnAction(e -> {
            this.action = "DISCARD LEADER CARD";
            playDiscardLeaderCard();
        });

        pickResourceFromMarketButton.setOnAction(e -> market());

        buyDevelopmentCardButton.setOnAction(e -> buyDevelopmentCard());

        activateProdButton.setOnAction(e -> activateProductionDevCards(activate, whichOutput));

        exitButton.setOnAction(e -> {
            if(this.handlerGUI.getGameMode() == 1) this.handlerGUI.getMsg().sendEndTurn();
            else if(this.mainAction) {
                choiceAction();
                this.mainAction = false;
                this.handlerGUI.getClientMain().checkRelationWithVatican();
                this.handlerGUI.getClientMain().getActionCountersDeck().drawCounter().activate(this.handlerGUI.getClientMain().getActionCountersDeck(),
                        this.handlerGUI.getClientMain().getLocalPlayers()[1].getPlayerBoard(), this.handlerGUI.getClientMain().getDevelopmentCardsDecksGrid());
            }
            else this.handlerGUI.notValidAction();
        });

        quitButton.setOnAction(e -> {
            this.handlerGUI.getGenericClassGUI().addLabelByCode("You left the game\nBye bye!", this.stage);
            if(this.handlerGUI.getGameMode() == 1) this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("quit1");
            else this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("quit0");
        });

        this.stage.setTitle("Player " + this.handlerGUI.getClientMain().getPlayerNumber() + ": choose the action!");
        this.stage.setScene(scene);
        this.stage.show();
    }

    /**
     * Method for play or discard a leader card
     */
    public void playDiscardLeaderCard( ) {
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

        //Setting the this.stage
        Scene scene = new Scene(root, 420, 300);
        this.stage.setTitle("Choose a leader card");
        this.stage.setScene(scene);
        this.stage.show();

        decline.setOnAction(e -> choiceAction());

        firstLeader.setOnAction(e -> {
            // Multiplayer
            if(this.handlerGUI.getGameMode() == 1) {
                if (action.equals("PLAY LEADER CARD")) this.handlerGUI.getMsg().sendPlayedLeader(0);
                else this.handlerGUI.getMsg().sendDiscardedLeader(0);
            }
            // Single player
            else {
                if (action.equals("PLAY LEADER CARD")) {
                    if (this.handlerGUI.getClientMain().checkLocalLeaders(this.handlerGUI.getClientMain().getLocalPlayers()[0], 0)) {
                        if (!this.handlerGUI.getClientMain().getLocalPlayers()[0].playLeaderCard(0))
                            this.handlerGUI.notValidAction();
                        else this.handlerGUI.getClientMain().setLeaderCards(this.handlerGUI.getClientMain().getLocalPlayers()[0].getPlayerLeaderCards());
                    }
                }
                else {
                    if(this.handlerGUI.getClientMain().checkLocalLeaders(this.handlerGUI.getClientMain().getLocalPlayers()[0], 0)){
                        if(!this.handlerGUI.getClientMain().getLocalPlayers()[0].discardLeaderCard(0))
                            this.handlerGUI.notValidAction();
                        else this.handlerGUI.getClientMain().setLeaderCards(this.handlerGUI.getClientMain().getLocalPlayers()[0].getPlayerLeaderCards());
                    }
                }
            }
            choiceAction();
        });

        secondLeader.setOnAction(e -> {
            // Multiplayer
            if(this.handlerGUI.getGameMode() == 1) {
                if (action.equals("PLAY LEADER CARD")) this.handlerGUI.getMsg().sendPlayedLeader(1);
                else this.handlerGUI.getMsg().sendDiscardedLeader(1);
            }
            // Single player
            else {
                if (action.equals("PLAY LEADER CARD")) {
                    if (this.handlerGUI.getClientMain().checkLocalLeaders(this.handlerGUI.getClientMain().getLocalPlayers()[0], 1)) {
                        if (!this.handlerGUI.getClientMain().getLocalPlayers()[0].playLeaderCard(1))
                            this.handlerGUI.notValidAction();
                        else this.handlerGUI.getClientMain().setLeaderCards(this.handlerGUI.getClientMain().getLocalPlayers()[0].getPlayerLeaderCards());
                    }
                }
                else {
                    if(this.handlerGUI.getClientMain().checkLocalLeaders(this.handlerGUI.getClientMain().getLocalPlayers()[0], 1)){
                        if(!this.handlerGUI.getClientMain().getLocalPlayers()[0].discardLeaderCard(1))
                            this.handlerGUI.notValidAction();
                        else this.handlerGUI.getClientMain().setLeaderCards(this.handlerGUI.getClientMain().getLocalPlayers()[0].getPlayerLeaderCards());
                    }
                }
            }
            choiceAction();
        });
    }

    /**
     * Method to choose a line from the market
     */
    public void market() {
        int x = 0, y;
        boolean checkExtraSpace = false;
        StringBuilder whichWl = new StringBuilder();
        //creating the image object
        Image image = new Image("plancia portabiglie.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(575);
        imageView.setPreserveRatio(true);
        Group root = new Group(imageView);

        for(int i = 0; i < 2; i++) {
            if(this.handlerGUI.getClientMain().getLeaderCards()[i] != null){
                if (this.handlerGUI.getClientMain().getLeaderCards()[i].isPlayed() && this.handlerGUI.getClientMain().getLeaderCards()[i] instanceof ExtraWarehouseSpaceLeaderCard) {
                    checkExtraSpace = true;
                    break;
                }
            }
        }

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
        this.stage.setTitle("Pick resources from market");
        this.stage.setScene(scene);
        this.stage.show();

        x = 1;
        y = 0;
        int[] coordinates = new int[2];
        for(int k = 0; k < 7; k++) {
            int finalY = y;
            int finalX = x;
            int finalK = k;
            boolean finalCheckExtraSpace = checkExtraSpace;
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
                int i;
                for(i = 0; i < resource.length; i++) {
                    if(resource[i].equals("white"))
                        break;
                }
                // Se è stata attivata la carta leader delle white marble e ci sono white marble nella riga/colonna scelta
                if(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[0] != null && i < resource.length)
                    whiteMarble(coordinates, resource, i, whichWl, new StringBuilder());
                // Se è stata attivata la carta leader di extra warehouse
                else if(finalCheckExtraSpace) {
                    int j = 0;
                    while(j < resource.length && (resource[j].equals("white") || resource[j].equals("redCross"))) {
                        whichWl.append("W");
                        j++;
                    }
                    if(j == resource.length) {
                        if(coordinates[0] == 0) sendMessageMarket("ROW", coordinates, "WWW", "X");
                        else sendMessageMarket("COLUMN", coordinates, "WWWW", "X");
                    }
                    else putResources(coordinates, resource, j, whichWl, new StringBuilder("X"));
                }
                // Se non sono state attivate carte leader
                else {
                    String parameter;
                    if(coordinates[0] == 0) parameter = "ROW";
                    else parameter = "COLUMN";
                    if(resource.length == 3) sendMessageMarket(parameter, coordinates, "WWW", "X");
                    else sendMessageMarket(parameter, coordinates, "WWWW", "X");
                }
            });
            if(k == 3) {
                x = 0;
                y = -1;
            }
            y++;
        }
    }

    /**
     * Method to choose which white marble power to activate
     * @param coordinates array with the chosen row/column and the number of the line
     * @param resource array of resources of the chosen line
     * @param numIndex method iterator
     * @param whichWl string that tells from which store the player wants to pick the resource at the corresponding position
     * @param whiteMarble string built at each iteration that tells which resource the player wants to replace with the white marbles in the line
     */
    public void whiteMarble(int[] coordinates, String[] resource, int numIndex, StringBuilder whichWl, StringBuilder whiteMarble) {

        Group root = new Group();
        Button button1 = new Button();
        Button button2 = new Button();

        String resource1 = resourceMarbles(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[0].getColour());
        System.out.println(resource1);
        Image image1 = new Image(resource1);
        this.handlerGUI.getGenericClassGUI().createIconButton(10,50, image1, button1, 80, 80);

        if(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[1] != null) {
            String resource2 = resourceMarbles(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[1].getColour());
            Image image2 = new Image(resource2);
            this.handlerGUI.getGenericClassGUI().createIconButton(210, 50, image2, button2, 80, 80);
            root.getChildren().add(button2);
        }

        Button declineButton = new Button("Decline");
        declineButton.setLayoutX(10);
        declineButton.setLayoutY(150);

        root.getChildren().addAll(button1, declineButton);

        Scene scene = new Scene(root, 595, 355);
        this.stage.setTitle("Choose the resource for the white marble.");
        this.stage.setScene(scene);
        this.stage.show();

        System.out.println(resource1);

        button1.setOnAction(e -> {
            resource[numIndex] = resourceMarbles(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[0].getColour());
            whiteMarble.append("0");
            int i;
            for(i = numIndex + 1; i < resource.length; i++) {
                if(resource[i].equals("white"))
                    break;
            }
            if(i != resource.length) whiteMarble(coordinates, resource, i, whichWl, whiteMarble);
            else {
                int k = 0;
                while(k < resource.length && (resource[k].equals("white") || resource[k].equals("redCross"))) {
                    whichWl.append("W");
                    k++;
                }
                putResources(coordinates, resource, k, whichWl, whiteMarble);
            }
        });

        button2.setOnAction(e -> {
            resource[numIndex] = resourceMarbles(this.handlerGUI.getClientMain().getPlayerboard().getResourceMarbles()[1].getColour());
            whiteMarble.append("1");
            int i;
            for(i = numIndex + 1; i < resource.length; i++) {
                if(resource[i].equals("white"))
                    break;
            }
            if(i != resource.length) whiteMarble(coordinates, resource, i, whichWl, whiteMarble);
            else {
                int k = 0;
                while(k < resource.length && (resource[k].equals("white") || resource[k].equals("redCross"))) {
                    whichWl.append("W");
                    k++;
                }
                putResources(coordinates, resource, k, whichWl, whiteMarble);
            }
        });

        declineButton.setOnAction(e -> {
            int i = 0;
            while(i < resource.length && (resource[i].equals("white") || resource[i].equals("redCross"))) {
                whichWl.append("W");
                i++;
            }
            if(i == resource.length && whiteMarble.toString().equals("X")) {
                if(coordinates[0] == 0) sendMessageMarket("ROW", coordinates, "WWWW", whiteMarble.toString());
                else sendMessageMarket("COLUMN", coordinates, "WWWW", whiteMarble.toString());
            }
            else putResources(coordinates, resource, i, whichWl, whiteMarble);
        });
    }

    /**
     * Method to choose where to store the resources picked from the market
     * @param coordinates array with the chosen row/column and the number of the line
     * @param resource array of resources of the chosen line
     * @param index method iterator
     * @param whichWl string built at each iteration that tells from which store the player wants to pick the resource at the corresponding position
     * @param whiteMarble string that tells which resource the player wants to replace with the white marbles in the line
     */
    public void putResources(int[] coordinates, String[] resource, int index, StringBuilder whichWl, StringBuilder whiteMarble) {
        String parameter;
        if(coordinates[0] == 0) parameter = "ROW";
        else parameter = "COLUMN";
        boolean checkExtraSpace = false;

        for(int i = 0; i < 2; i++) {
            if(this.handlerGUI.getClientMain().getLeaderCards()[i] != null){
                if (this.handlerGUI.getClientMain().getLeaderCards()[i].isPlayed() && this.handlerGUI.getClientMain().getLeaderCards()[i] instanceof ExtraWarehouseSpaceLeaderCard) {
                    checkExtraSpace = true;
                    break;
                }
            }
        }

        if(checkExtraSpace) {
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
            extra.setLayoutX(300);
            extra.setLayoutY(100);
            root.getChildren().addAll(warehouse, extra);

            Scene scene = new Scene(root, 595, 355);
            this.stage.setTitle("Put the resources in the stores.");
            this.stage.setScene(scene);
            this.stage.show();

            warehouse.setOnAction(e -> {
                whichWl.append("W");
                int i = index + 1;
                while(i < resource.length && (resource[i].equals("white") || resource[i].equals("redCross"))) {
                    whichWl.append("W");
                    i++;
                }
                if (i == resource.length) sendMessageMarket(parameter, coordinates, whichWl.toString(), whiteMarble.toString());
                else putResources(coordinates, resource, i, whichWl, whiteMarble);
            });

            extra.setOnAction(e -> {
                whichWl.append("L");
                int i = index + 1;
                while(i < resource.length && (resource[i].equals("white") || resource[i].equals("redCross"))) {
                    whichWl.append("W");
                    i++;
                }
                if (i == resource.length) sendMessageMarket(parameter, coordinates, whichWl.toString(), whiteMarble.toString());
                else putResources(coordinates, resource, i, whichWl, whiteMarble);
            });
        }
        else {
            if(resource.length == 3) sendMessageMarket(parameter, coordinates, "WWW", whiteMarble.toString());
            else sendMessageMarket(parameter, coordinates, "WWWW", whiteMarble.toString());
        }
    }

    /**
     * Method that send the market message or sets the market action parameters in local GUI
     * @param parameter Row/column choice
     * @param coordinates array with the chosen row/column and the number of the line
     * @param choice string that tells from which store the player wants to pick the resource at the corresponding position
     * @param whiteMarble string that tells which resource the player wants to replace with the white marbles in the line
     */
    public void sendMessageMarket(String parameter, int[] coordinates, String choice, String whiteMarble) {
        if(this.handlerGUI.getGameMode() == 1)
            this.handlerGUI.getMsg().sendMarketAction(parameter, coordinates[1], choice, whiteMarble);
        else if(!this.mainAction) {
            if (this.handlerGUI.getClientMain().checkLocalMarketAction(this.handlerGUI.getClientMain().getLocalPlayers()[0].getPlayerBoard(), parameter, coordinates[1], choice, whiteMarble)) {
                mainAction = true;
            }
        }
        else this.handlerGUI.notValidAction();
        choiceAction();
    }

    /**
     * Method that combines the attribute colour of a marble with the creation of a shape with the correspondent colour
     * @param x x coordinates in the market
     * @param y y coordinates in the market
     * @param gc graphic context
     * @param colour colour of the marble
     */
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

    /**
     * Method that combines the marble's colour with the correspondent resource's image
     * @param colour colour of the marble
     * @return image
     */
    public String resourceMarbles(String colour) {
        String resource;
        switch (colour) {
            case " YELLOW ":
                resource = "coin.png";
                break;
            case " BLUE ":
                resource = "shield.png";
                break;
            case " GREY ":
                resource = "stone.png";
                break;
            case " PURPLE ":
                resource = "servant.png";
                break;
            case " RED ":
                resource = "redCross";
                break;
            default:
                resource = "white";
                break;
        }
        return resource;
    }

    /**
     * Method to buy a development card
     */
    public void buyDevelopmentCard() {
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
                if(this.handlerGUI.getClientMain().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[i][j][0] != null) {
                    Image img = new Image(this.handlerGUI.getClientMain().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[i][j][0].getImage());
                    arrayButtons[index] = new Button();
                    this.handlerGUI.getGenericClassGUI().createIconButton(x, y, img, arrayButtons[index], 450, 150);
                    root.getChildren().add(arrayButtons[index]);
                }
                index++;
                x += 200;
            }
            x = 10;
            y += 300;
        }

        Button decline = new Button("Decline");
        decline.setLayoutX(700);
        decline.setLayoutY(870);
        root.getChildren().add(decline);

        //Setting the this.stage
        Scene scene = new Scene(root, 800, 900);
        this.stage.setTitle("Buy development card.");
        this.stage.setScene(scene);
        this.stage.show();

        decline.setOnAction(e -> choiceAction());

        int row = 0, column = 0;
        int[] coordinates = new int[2];
        for(int i = 0; i < 12; i++) {
            if (i == 4) {
                column = 0;
                row = 1;
            } else if (i == 8) {
                column = 0;
                row = 2;
            }
            if (arrayButtons[i] != null) {
                int finalRow = row;
                int finalColumn = column;
                arrayButtons[i].setOnAction(e -> {
                    coordinates[0] = finalRow;
                    coordinates[1] = finalColumn;
                    DevelopmentCard developmentCard = this.handlerGUI.getClientMain().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[coordinates[0]][coordinates[1]][0];
                    int numResource;
                    if (developmentCard.getDevelopmentCardCost().get("COINS") != 0) numResource = 0;
                    else if (developmentCard.getDevelopmentCardCost().get("SERVANTS") != 0) numResource = 1;
                    else if (developmentCard.getDevelopmentCardCost().get("SHIELDS") != 0) numResource = 2;
                    else numResource = 3;
                    putPayedResource(coordinates, numResource, pickedResources, developmentCard, 0);
                });
            }
            column++;
        }
    }

    /**
     * Method to choose from which store to get the resources for purchasing a development card
     * @param coordinates coordinates of the card on the grid
     * @param numResource identification number of the resource
     * @param pickedResources a matrix where you put the quantity and from store you want to get the resource
     * @param developmentCard the purchased card
     * @param sameResources method iterator for a single resource
     */
    public void putPayedResource(int[] coordinates, int numResource, String[][] pickedResources, DevelopmentCard developmentCard, int sameResources) {
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
        Button discount = new Button("Use your discount on this resource!");
        discount.setLayoutX(300);
        discount.setLayoutY(200);

        for(int i = 0; i < 2; i++) {
            if (this.handlerGUI.getClientMain().getLeaderCards()[i] != null && this.handlerGUI.getClientMain().getLeaderCards()[i].isPlayed()) {
                if(this.handlerGUI.getClientMain().getLeaderCards()[i] instanceof DiscountDevelopmentCardsLeaderCard) {
                    if(resource.equals(((DiscountDevelopmentCardsLeaderCard) this.handlerGUI.getClientMain().getLeaderCards()[i]).getDiscount())) {
                        if(numSame == developmentCard.getDevelopmentCardCost().get(resource)) {
                            root.getChildren().add(discount);
                        }
                    }
                }
            }
        }

        root.getChildren().addAll(warehouse, chest, extra);

        //Setting the this.stage
        Scene scene = new Scene(root, 600, 300);
        this.stage.setTitle("Where do you want to pick the resources?");
        this.stage.setScene(scene);
        this.stage.show();

        discount.setOnAction(e -> {
            pickedResources[0][numResource] = String.valueOf(developmentCard.getDevelopmentCardCost().get(resource) - 1);
            if(num < 4) putPayedResource(coordinates, num, pickedResources, developmentCard, 0);
            else putCardOnPlayerBoard(coordinates, pickedResources);
        });

        pickedResources[0][numResource] = developmentCard.getDevelopmentCardCost().get(resource).toString();
        warehouse.setOnAction(e -> {
            pickedResources[1][numResource] = pickedResources[1][numResource] + "W";
            if(numSame == developmentCard.getDevelopmentCardCost().get(resource)) {
                if(num < 4) putPayedResource(coordinates, num, pickedResources, developmentCard, 0);
                else putCardOnPlayerBoard(coordinates, pickedResources);
            }
            else putPayedResource(coordinates, numResource, pickedResources, developmentCard, numSame);
        });

        chest.setOnAction(e -> {
            pickedResources[1][numResource] = pickedResources[1][numResource] + "C";
            if(numSame == developmentCard.getDevelopmentCardCost().get(resource)) {
                if(num < 4) putPayedResource(coordinates, num, pickedResources, developmentCard, 0);
                else putCardOnPlayerBoard(coordinates, pickedResources);
            }
            else putPayedResource(coordinates, numResource, pickedResources, developmentCard, numSame);
        });

        extra.setOnAction(e -> {
            pickedResources[1][numResource] = pickedResources[1][numResource] + "L";
            if(numSame == developmentCard.getDevelopmentCardCost().get(resource)) {
                if(num < 4) putPayedResource(coordinates, num, pickedResources, developmentCard, 0);
                else putCardOnPlayerBoard(coordinates, pickedResources);
            }
            else putPayedResource(coordinates, numResource, pickedResources, developmentCard, numSame);
        });
    }

    /**
     * Method to choose the place where to put the purchased development card
     * @param coordinates coordinates of the card on the grid
     * @param pickedResources a matrix where you put the quantity and from store you want to get the resource
     */
    public void putCardOnPlayerBoard(int[] coordinates, String[][] pickedResources) {
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
                if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[row][col] != null)
                    break;
            }
            if(row == -1) row = 0;
            dimPile[col] = row;
        }

        //Creating buttons
        Button[] arrayButtons = new Button[3];
        for (i = 0; i < 3; i++) {
            //Creating a graphic (image)
            if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[i]][i] != null) {
                Image img = new Image(this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[i]][i].getImage());
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

        //Setting the this.stage
        Scene scene = new Scene(root, 600, 470);
        this.stage.setTitle("Where do you want to put the development card?");
        this.stage.setScene(scene);
        this.stage.show();

        for(int j = 0; j < 3; j++) {
            int finalJ = j;
            arrayButtons[j].setOnAction(e -> {
                int[] quantity = new int[4];
                for(int k = 0; k < quantity.length; k++) quantity[k] = Integer.parseInt(pickedResources[0][k]);
                if(this.handlerGUI.getGameMode() == 1)
                    this.handlerGUI.getMsg().sendBuyCardAction(coordinates[1], 3 - coordinates[0], quantity, pickedResources[1], finalJ);
                else if(!this.mainAction) {
                    if(this.handlerGUI.getClientMain().checkLocalBuyCard(this.handlerGUI.getClientMain().getLocalPlayers()[0], coordinates[1], 3 - coordinates[0], quantity, pickedResources[1])) {
                        if(this.handlerGUI.getClientMain().getLocalPlayers()[0].buyDevelopmentCard(this.handlerGUI.getClientMain().getDevelopmentCardsDecksGrid(), coordinates[1], 3 - coordinates[0], finalJ, pickedResources[1]))
                            this.mainAction = true;
                        else this.handlerGUI.notValidAction();
                    }
                }
                choiceAction();
            });
        }
    }

    /**
     * Method to activate the production power of the development cards on the playerboard
     * @param activate array of chosen activate powers
     * @param whichInput array of string that tells which resource, what quantity and where to get it
     */
    public void activateProductionDevCards(int[] activate, String[] whichInput) {
        int i;
        int numBottons = 0;
        int x = 10;
        StringBuilder string = new StringBuilder();

        int[] dimPile = new int[3];
        for(int col = 0; col < 3; col++) {
            int row;
            for(row = 2; row > 0; row--) {
                if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[row][col] != null)
                    break;
            }
            if(row == -1) row = 0;
            dimPile[col] = row;
        }

        for (i = 0; i < 3; i++) {
            if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[i]][i] != null) numBottons++;
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
        Button[] arrayButtons = new Button[3];
        for (i = 0; i < 3; i++) {
            //Creating a graphic (image)
            if(this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[i]][i] != null) {
                Image img = new Image(this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[i]][i].getImage());
                arrayButtons[i] = new Button();
                this.handlerGUI.getGenericClassGUI().createIconButton(x, 100, img, arrayButtons[i], 250, 200);
                root.getChildren().add(arrayButtons[i]);
            }
            x = x + 190;
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

        //Setting the this.stage
        Scene scene = new Scene(root, 600, 470);
        this.stage.setTitle("Activate production power");
        this.stage.setScene(scene);
        this.stage.show();

        okBtn.setOnAction(e -> activateBasicProductionPower(activate, whichInput, 0));

        noBtn.setOnAction(e -> {
            for(int j = 0; j < 3; j++) activate[j] = 0;
            activateBasicProductionPower(activate, whichInput, 0);
        });


        for (int j = 0; j < 3; j++) {
            if (arrayButtons[j] != null) {
                int finalJ = j;
                arrayButtons[j].setOnAction(e -> {
                    String num = "1";
                    if (activate[finalJ] != 1) {
                        activate[finalJ] = 1;
                        if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[finalJ]][finalJ].getDevelopmentCardInput().get("COINS") != 0) {
                            string.append("0");
                            if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[finalJ]][finalJ].getDevelopmentCardInput().get("COINS") > 1)
                                num = "2";
                        }
                        if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[finalJ]][finalJ].getDevelopmentCardInput().get("SERVANTS") != 0) {
                            string.append("1");
                            if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[finalJ]][finalJ].getDevelopmentCardInput().get("SERVANTS") > 1)
                                num = "2";
                        }
                        if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[finalJ]][finalJ].getDevelopmentCardInput().get("SHIELDS") != 0) {
                            string.append("2");
                            if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[finalJ]][finalJ].getDevelopmentCardInput().get("SHIELDS") > 1)
                                num = "2";
                        }
                        if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[finalJ]][finalJ].getDevelopmentCardInput().get("STONES") != 0) {
                            string.append("3");
                            if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerBoardDevelopmentCards()[dimPile[finalJ]][finalJ].getDevelopmentCardInput().get("STONES") > 1)
                                num = "2";
                        }
                        whichInput[finalJ] = string.toString();
                        putResourcePayedDevCard(activate, whichInput, finalJ, num, 0, null);
                    } else activateProductionDevCards(activate, whichInput);
                });
            }
        }
    }

    /**
     *  Method to choose from which store to get the resources for activating a production power
     * @param activate array of chosen activate powers
     * @param whichInput array of string that tells which resource, what quantity and where to get it
     * @param index method iterator
     * @param num how many resources of the same type
     * @param charResource iterator on whichInput
     * @param whichOutput array of string that tells which resource the player wants to have
     */
    public void putResourcePayedDevCard(int[] activate, String[] whichInput, int index, String num, int charResource, String[] whichOutput) {
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

        //Setting the this.stage
        Scene scene = new Scene(root, 500, 300);
        this.stage.setTitle("Where do you want to pick the resources?");
        this.stage.setScene(scene);
        this.stage.show();

        warehouse.setOnAction(e -> actionOnWhichInput(activate, whichInput, index, num, "W", whichOutput));

        chest.setOnAction(e -> actionOnWhichInput(activate, whichInput, index, num, "C", whichOutput));

        extra.setOnAction(e -> actionOnWhichInput(activate, whichInput, index, num, "L", whichOutput));
    }

    /**
     * Method that build the whichInput string
     * @param activate array of chosen activate powers
     * @param whichInput array of string that tells which resource, what quantity and where to get it
     * @param index method iterator
     * @param num how many resources of the same type
     * @param reserve from which reserve the player want to pick the resource
     * @param whichOutput array of string that tells which resource the player wants to have
     */
    public void actionOnWhichInput(int[] activate, String[] whichInput, int index, String num, String reserve, String[] whichOutput) {
        if(whichInput[index].length() == 1 && num.equals("1")) {
            whichInput[index] += num + reserve;
            redirect(index, activate, whichInput, whichOutput);
        }

        else if(whichInput[index].length() == 1 && num.equals("2")) {
            whichInput[index] += num + reserve;
            putResourcePayedDevCard(activate, whichInput, index, num, 0, whichOutput);
        }

        else if(whichInput[index].length() == 3 && num.equals("2") && whichInput[index].charAt(2) == reserve.charAt(0)) {
            redirect(index, activate, whichInput, whichOutput);
        }

        else if(whichInput[index].length() == 3 && num.equals("2") && whichInput[index].charAt(2) != reserve.charAt(0)) {
            whichInput[index] = whichInput[index].charAt(0) + "1" + whichInput[index].charAt(2) + whichInput[index].charAt(0) + "1" + reserve;
            redirect(index, activate, whichInput, whichOutput);
        }
        else if(whichInput[index].length() == 2 && num.equals("1")) {
            whichInput[index] = whichInput[index].charAt(0) + "1" + reserve + whichInput[index].charAt(1);
            putResourcePayedDevCard(activate, whichInput, index, num, 3, whichOutput);
        }

        else if(whichInput[index].length() == 4 && num.equals("1")) {
            whichInput[index] += "1" + reserve;
            redirect(index, activate, whichInput, whichOutput);
        }
    }

    /**
     * Method that redirects to others method depending on which activation powers are activated
     * @param index method iterator
     * @param activate array of chosen activate powers
     * @param whichInput array of string that tells which resource, what quantity and where to get it
     * @param whichOutput array of string that tells which resource the player wants to have
     */
    public void redirect(int index, int[] activate, String[] whichInput, String[] whichOutput) {
        if(index < 3 && (activate[0] != 1 || activate[1] != 1 || activate[2] != 1)) activateProductionDevCards(activate, whichInput);
        else if(index < 3) activateBasicProductionPower(activate, whichInput, 0);
        else if(index == 3) activateExtraProdPower(activate, whichOutput, whichInput);
        else if(activate[4] != 1 || activate[5] != 1) activateExtraProdPower(activate, whichOutput, whichInput);
        else pickResourceExtraProdPower(activate, whichOutput, whichInput,2);
    }

    /**
     * Method to activate the basic production power
     * @param activate array of chosen activate powers
     * @param whichInput array of string that tells which resource, what quantity and where to get it
     * @param index method iterator
     */
    public void activateBasicProductionPower(int[] activate, String[] whichInput, int index) {
        String[] whichOutput = new String[3];
        int indexPar = index + 1;
        String put;
        String[] resources;

        if(index < 2) put = "INPUT";
        else put = "OUTPUT";

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

        //Setting the this.stage
        Scene scene = new Scene(root, 800, 150);
        this.stage.setTitle("Activate basic production power: " + put);
        this.stage.setScene(scene);
        this.stage.show();

        noBtn.setOnAction(e -> {
            activate[3] = 0;
            whichOutput[0] = "-1";
            whichInput[3] = "-1";
            activateExtraProdPower(activate, whichOutput, whichInput);
        });

        for(int j = 0; j < 4; j++) {
            int finalJ = j;
            arrayButtons[j].setOnAction(e -> {
                if(index < 2) {
                    if(whichInput[3] == null) whichInput[3] = finalJ + "";
                    else whichInput[3] += finalJ;
                    activateBasicProductionPower(activate, whichInput, indexPar);
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
                    putResourcePayedDevCard(activate, whichInput, 3, num, 0, whichOutput);
                }
            });
        }
    }

    /**
     * Method to activate the extra production power
     * @param activate array of chosen activate powers
     * @param whichOutput array of string that tells which resource the player wants to have
     * @param whichInput array of string that tells which resource, what quantity and where to get it
     */
    public void activateExtraProdPower(int[] activate, String[] whichOutput, String[] whichInput) {
        int numBottons = 0;
        int indexLeader = 0;
        int index = 0;
        int x = 10;
        Group root = new Group();
        Image img;
        int[] outputs = new int[3];

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
                if (numBottons == 1) img = new Image(this.handlerGUI.getClientMain().getLeaderCards()[indexLeader].getImage());
                else img = new Image(this.handlerGUI.getClientMain().getLeaderCards()[i].getImage());
                arrayButtons[index] = new Button();
                this.handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[index], 250, 200);
                x = x + 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }

            Button okBtn = new Button("Submit");
            okBtn.setLayoutX(10);
            okBtn.setLayoutY(300);
            root.getChildren().add(okBtn);

            Button noBtn = new Button("I don't want to activate this production power!");
            noBtn.setLayoutX(100);
            noBtn.setLayoutY(300);
            root.getChildren().add(noBtn);

            //Setting the this.stage
            Scene scene = new Scene(root, 740, 500);
            this.stage.setTitle("Activate extra production power");
            this.stage.setScene(scene);
            this.stage.show();

            okBtn.setOnAction(e -> {
                if(activate[4] == 0 && activate[5] == 0) {
                    if(this.handlerGUI.getGameMode() == 1) this.handlerGUI.getMsg().sendActivationProdAction(activate, whichInput, whichOutput);
                    else if(!this.mainAction) {
                        if(this.handlerGUI.getClientMain().checkLocalActivateProd(this.handlerGUI.getClientMain().getLocalPlayers()[0], activate, whichInput)) {
                            for (int k = 0; k < whichOutput.length; k++) {
                                if (whichOutput[k] != null) outputs[k] = Integer.parseInt(whichOutput[k]);
                                else outputs[k] = -1;
                            }
                            if (this.handlerGUI.getClientMain().getLocalPlayers()[0].activateProduction(activate, whichInput, outputs))
                                this.mainAction = true;
                        }
                        else this.handlerGUI.notValidAction();
                    }
                    choiceAction();
                }
                else {
                    int num = 0;
                    if(activate[4] == 1) num++;
                    if(activate[5] == 1) num++;
                    pickResourceExtraProdPower(activate, whichOutput, whichInput, num);
                }
            });

            noBtn.setOnAction(e -> {
                activate[4] = 0;
                activate[5] = 0;
                whichInput[4] = null;
                whichInput[5] = null;
                whichOutput[1] = "-1";
                whichOutput[2] = "-1";
                if(this.handlerGUI.getGameMode() == 1) this.handlerGUI.getMsg().sendActivationProdAction(activate, whichInput, whichOutput);
                else if(!this.mainAction) {
                    if(this.handlerGUI.getClientMain().checkLocalActivateProd(this.handlerGUI.getClientMain().getLocalPlayers()[0], activate, whichInput)) {
                        for (int k = 0; k < whichOutput.length; k++) {
                            if (whichOutput[k] != null) outputs[k] = Integer.parseInt(whichOutput[k]);
                            else outputs[k] = -1;
                        }
                        if (this.handlerGUI.getClientMain().getLocalPlayers()[0].activateProduction(activate, whichInput, outputs))
                            this.mainAction = true;
                    }
                    else this.handlerGUI.notValidAction();
                }
                choiceAction();
            });

            for (int j = 0; j < numBottons; j++) {
                int finalJ = j;
                arrayButtons[j].setOnAction(e -> {
                    activate[finalJ + 4] = 1;
                    whichInput[finalJ + 4] = this.handlerGUI.getClientMain().getPlayerboard().getExtraProductionPowerInput()[finalJ];
                    System.out.println(whichInput[finalJ + 4]);
                    switch (whichInput[finalJ + 4]) {
                        case "COINS":
                            whichInput[finalJ + 4] = "0";
                            break;
                        case "SERVANTS":
                            whichInput[finalJ + 4] = "1";
                            break;
                        case "SHIELDS":
                            whichInput[finalJ + 4] = "2";
                            break;
                        case "STONES":
                            whichInput[finalJ + 4] = "3";
                            break;
                    }
                    putResourcePayedDevCard(activate, whichInput, finalJ + 4, "1", 0, whichOutput);
                });
            }
        }
        else {
            if(this.handlerGUI.getGameMode() == 1) this.handlerGUI.getMsg().sendActivationProdAction(activate, whichInput, whichOutput);
            else if(!this.mainAction) {
                if(this.handlerGUI.getClientMain().checkLocalActivateProd(this.handlerGUI.getClientMain().getLocalPlayers()[0], activate, whichInput)) {
                    for (int k = 0; k < whichOutput.length; k++) {
                        if (whichOutput[k] != null) outputs[k] = Integer.parseInt(whichOutput[k]);
                        else outputs[k] = -1;
                    }
                    if (this.handlerGUI.getClientMain().getLocalPlayers()[0].activateProduction(activate, whichInput, outputs))
                        this.mainAction = true;
                }
                else this.handlerGUI.notValidAction();
            }
            choiceAction();
        }
    }

    /**
     * method to choose the output resources of the extra production power
     * @param activate array of chosen activate powers
     * @param whichOutput array of string that tells which resource the player wants to have
     * @param whichInput array of string that tells which resource, what quantity and where to get it
     * @param num how many extra powers are activated (method iterator)
     */
    public void pickResourceExtraProdPower(int[] activate, String[] whichOutput, String[] whichInput, int num) {
        int numPar = num - 1;
        int[] outputs = new int[3];

        String[] resources = new String[]{
                "coin.png",
                "servant.png",
                "shield.png",
                "stone.png",
                "redCross.png"
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

        //Setting the this.stage
        Scene scene = new Scene(root, 900, 130);
        this.stage.setTitle("Choose the resource you want to have.");
        this.stage.setScene(scene);
        this.stage.show();

        for(int j = 0; j < 5; j++) {
            int finalJ = j;
            arrayButtons[j].setOnAction(e -> {
                if(whichOutput[1] == null) whichOutput[1] = String.valueOf(finalJ);
                else whichOutput[2] = String.valueOf(finalJ);
                if(numPar == 0) {
                    if (this.handlerGUI.getGameMode() == 1) this.handlerGUI.getMsg().sendActivationProdAction(activate, whichInput, whichOutput);
                    else if (!this.mainAction) {
                        if (this.handlerGUI.getClientMain().checkLocalActivateProd(this.handlerGUI.getClientMain().getLocalPlayers()[0], activate, whichInput)) {
                            for (int i = 0; i < whichOutput.length; i++) {
                                if (whichOutput[i] != null) outputs[i] = Integer.parseInt(whichOutput[i]);
                                else outputs[i] = -1;
                            }
                            if (this.handlerGUI.getClientMain().getLocalPlayers()[0].activateProduction(activate, whichInput, outputs))
                                this.mainAction = true;
                        } else this.handlerGUI.notValidAction();
                    }
                    choiceAction();
                }
                else pickResourceExtraProdPower(activate, whichOutput, whichInput, numPar);
            });
        }
    }
}

