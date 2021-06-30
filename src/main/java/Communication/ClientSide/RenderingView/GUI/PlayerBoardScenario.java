package Communication.ClientSide.RenderingView.GUI;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraWarehouseSpaceLeaderCard;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PlayerBoardScenario implements Runnable {

    private final HandlerGUI handlerGUI;
    private final Stage anotherStage;

    public PlayerBoardScenario(HandlerGUI handlerGUI, Stage anotherStage) {

        this.handlerGUI = handlerGUI;
        this.anotherStage = anotherStage;

    }

    @Override
    public void run() {

        Image image = new Image("playerBoard.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(700);
        imageView.setPreserveRatio(true);
        Group root = new Group(imageView);

        putRedCross(root);
        putDevCards(root);
        putResourcesInChest(root);
        putResourcesInWarehouse(root);
        putExtraWarehouse(root);
        putResourceInExtraWarehouse(root);


        Scene scene = new Scene(root, 700, 600);
        this.anotherStage.setTitle("Player " + this.handlerGUI.getClientMain().getPlayerNumber() + ": your playerboard!");
        this.anotherStage.setScene(scene);
        this.anotherStage.show();

    }

    public void putRedCross(Group root) {
        int x = 25, y;
        int redCross = this.handlerGUI.getClientMain().getPlayerboard().getFaithPath().getCrossPosition();
        // y
        if(redCross < 3 || (redCross > 10 && redCross < 17)) y = 90;
        else if(redCross == 3 || redCross == 10 || redCross == 17 ) y = 55;
        else y = 20;
        // x
        for(int i = 0; i < redCross; i++) {
             if(i != 2 && i != 3 && i != 9 && i != 10 && i != 16 && i != 17) x+= 34;
        }

        Image image = new Image("redCross.png");
        imageView(image, x, y, root);

        if(this.handlerGUI.getGameMode() == 0) {
            x = 25;
            int blackCross = this.handlerGUI.getClientMain().getLocalPlayers()[1].getPlayerBoard().getFaithPath().getCrossPosition();
            // y
            if(blackCross < 3 || (blackCross > 10 && blackCross < 17)) y = 90;
            else if(blackCross == 3 || blackCross == 10 || blackCross == 17 ) y = 55;
            else y = 20;
            // x
            for(int i = 0; i < blackCross; i++) {
                if(i != 2 && i != 3 && i != 9 && i != 10 && i != 16 && i != 17) x+= 34;
            }

            Image image2 = new Image("croce.png");
            imageView(image2, x, y, root);
        }
    }

    public void putDevCards(Group root) {
        int x = 270;
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

        for (int i = 0; i < 3; i++) {
            //Creating a graphic (image)
            if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[dimPile[i]][i] != null) {
                Image img = new Image(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[dimPile[i]][i].getImage());
                ImageView imageView = new ImageView();
                imageView.setImage(img);
                imageView.setLayoutX(x);
                imageView.setLayoutY(230);
                imageView.setFitWidth(125);
                imageView.setPreserveRatio(true);
                root.getChildren().add(imageView);
            }
            x += 140;
        }
    }

    public void putResourcesInChest(Group root) {
        int x = 25;
        int y = 370;
        int numResource = 0;
        Image image = null;
        for(String s: this.handlerGUI.getClientMain().getPlayerboard().getChest().getChestResources().keySet()) {
            switch (s) {
                case "COINS": {
                    image = new Image("coin.png");
                    break;
                }
                case "SHIELDS": {
                    image = new Image("shield.png");
                    break;
                }
                case "SERVANTS": {
                    image = new Image("servant.png");
                    break;
                }
                case "STONES": {
                    image = new Image("stone.png");
                    break;
                }
            }
            for(int i = 0; i < this.handlerGUI.getClientMain().getPlayerboard().getChest().getChestResources().get(s); i++) {
                if(numResource == 4) {
                    x = 25;
                    y += 20;
                    numResource = 0;
                }
                imageView(image, x, y, root);
                numResource++;
                x += 25;
            }
        }
    }

    public void putResourcesInWarehouse(Group root) {
        Image image = null;
        int num;
        boolean first = false;
        boolean second = false;

        for(String s: this.handlerGUI.getClientMain().getPlayerboard().getWareHouse().getWarehouseResources().keySet()) {
            if (!s.contains("extra")) {
                switch (s) {
                    case "COINS": {
                        image = new Image("coin.png");
                        break;
                    }
                    case "SHIELDS": {
                        image = new Image("shield.png");
                        break;
                    }
                    case "SERVANTS": {
                        image = new Image("servant.png");
                        break;
                    }
                    case "STONES": {
                        image = new Image("stone.png");
                        break;
                    }
                }

                num = this.handlerGUI.getClientMain().getPlayerboard().getWareHouse().getWarehouseResources().get(s);
                if (num == 1) {
                    if (!first) {
                        imageView(image, 70, 215, root);
                        first = true;
                    } else if (!second) {
                        imageView(image, 50, 260, root);
                        second = true;
                    } else imageView(image, 40, 305, root);
                } else if (num == 2) {
                    if (!second) {
                        imageView(image, 50, 260, root);
                        imageView(image, 90, 260, root);
                        second = true;
                    } else {
                        imageView(image, 40, 305, root);
                        imageView(image, 73, 305, root);
                    }
                } else if (num == 3) {
                    imageView(image, 40, 305, root);
                    imageView(image, 73, 305, root);
                    imageView(image, 106, 305, root);
                }
            }
        }
    }

    public void putExtraWarehouse(Group root) {
        Image image = null;
        int x = 25;

        for(int i = 0; i < 2; i++) {
            if(this.handlerGUI.getClientMain().getLeaderCards()[i] != null) {
                LeaderCard leaderCard = this.handlerGUI.getClientMain().getLeaderCards()[i];
                if (leaderCard.isPlayed()) {
                    if (leaderCard instanceof ExtraWarehouseSpaceLeaderCard) {
                        switch (((ExtraWarehouseSpaceLeaderCard) leaderCard).getResourceSpace()) {
                            case "COINS": {
                                image = new Image("coinExtra.png");
                                break;
                            }
                            case "SHIELDS": {
                                image = new Image("shieldExtra.png");
                                break;
                            }
                            case "SERVANTS": {
                                image = new Image("servantExtra.png");
                                break;
                            }
                            case "STONES": {
                                image = new Image("stoneExtra.png");
                                break;
                            }
                        }

                        ImageView imageView = new ImageView();
                        imageView.setImage(image);
                        imageView.setX(x);
                        imageView.setY(520);
                        imageView.setFitWidth(150);
                        imageView.setPreserveRatio(true);
                        root.getChildren().add(imageView);
                        x = 200;
                    }
                }
            }
        }
    }

    public void putResourceInExtraWarehouse(Group root) {
        Image image = null;
        int num = 0;

        int x = 55;
        for(String key: this.handlerGUI.getClientMain().getPlayerboard().getWareHouse().getWarehouseResources().keySet()) {
            if(key.contains("extra")) {
                for (int i = 0; i < this.handlerGUI.getClientMain().getPlayerboard().getWareHouse().getWarehouseResources().get(key); i++) {
                    switch (key) {
                        case "extraCOINS":
                            image = new Image("coin.png");
                            break;
                        case "extraSHIELDS":
                            image = new Image("shield.png");
                            break;
                        case "extraSERVANTS":
                            image = new Image("servant.png");
                            break;
                        case "extraSTONES":
                            image = new Image("stone.png");
                            break;
                    }
                    imageView(image, x, 525, root);
                    if(num == 2) x += 260;
                    else x += 60;
                    num++;
                }
            }
        }
    }

    public void imageView(Image image, int x, int y, Group root) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(35);
        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);
    }
}