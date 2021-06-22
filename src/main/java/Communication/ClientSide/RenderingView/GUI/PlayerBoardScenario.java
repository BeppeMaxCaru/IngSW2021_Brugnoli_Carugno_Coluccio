package Communication.ClientSide.RenderingView.GUI;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PlayerBoardScenario {

    private final HandlerGUI handlerGUI;
    private final Stage anotherStage;

    public PlayerBoardScenario(HandlerGUI handlerGUI, Stage anotherStage) {
        this.handlerGUI = handlerGUI;
        this.anotherStage = anotherStage;
    }

    public void PlayerBoard() {
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

        Scene scene = new Scene(root, 700, 500);
        this.anotherStage.setTitle("Your playerboard");
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
             if(i != 3 && i != 4 && i != 10 && i != 11 && i != 17 && i != 18) x+= 35;
        }

        Image image = new Image("redCross.png");
        imageView(image, x, y, root);
    }

    public void putDevCards(Group root) {
        int x = 270;
        for (int i = 0; i < 3; i++) {
            //Creating a graphic (image)
            if (this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[i][0] != null) {
                Image img = new Image(this.handlerGUI.getClientMain().getPlayerboard().getPlayerboardDevelopmentCards()[i][0].getImage());
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
        Image image;
        for(String s: this.handlerGUI.getClientMain().getPlayerboard().getChest().getChestResources().keySet()) {
            switch (s) {
                case "COINS":
                    image = new Image("coin.png");
                    break;
                case "SHIELDS":
                    image = new Image("shield.png");
                    break;
                case "SERVANTS":
                    image = new Image("servant.png");
                    break;
                default:
                    image = new Image("stone.png");
                    break;
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
        Image image;
        boolean first = false;
        boolean second = false;

        for(String s: this.handlerGUI.getClientMain().getPlayerboard().getWareHouse().getWarehouseResources().keySet()) {
            switch (s) {
                case "COINS":
                    image = new Image("coin.png");
                    break;
                case "SHIELDS":
                    image = new Image("shield.png");
                    break;
                case "SERVANTS":
                    image = new Image("servant.png");
                    break;
                default:
                    image = new Image("stone.png");
                    break;
            }
            if(this.handlerGUI.getClientMain().getPlayerboard().getWareHouse().getWarehouseResources().get(s) == 1) {
                if(!first) {
                    imageView(image, 70, 215, root);
                    first = true;
                }
                else if(!second) {
                    imageView(image,50, 260, root);
                    second = true;
                }
                else imageView(image,40, 305, root);
            }
            else if(this.handlerGUI.getClientMain().getPlayerboard().getWareHouse().getWarehouseResources().get(s) == 2) {
                if(!second) {
                    imageView(image,50, 260, root);
                    imageView(image,90, 260, root);
                    second = true;
                }
                else {
                    imageView(image,40, 305, root);
                    imageView(image,73, 305, root);
                }
            }
            else {
                imageView(image,40, 305, root);
                imageView(image,73, 305, root);
                imageView(image,106, 305, root);
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