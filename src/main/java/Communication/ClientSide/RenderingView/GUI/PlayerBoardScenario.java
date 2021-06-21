package Communication.ClientSide.RenderingView.GUI;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PlayerBoardScenario {

    private final HandlerGUI handlerGUI;

    public PlayerBoardScenario(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
    }

    public void PlayerBoard(Stage stage) {
        Image image = new Image("playerBoard.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(700);
        imageView.setPreserveRatio(true);
        Group root = new Group(imageView);

        putRedCross(root);
        putDevCards(root);
        putResourcesInChest(root);

        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Your playerboard");
        stage.setScene(scene);
        stage.show();
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
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitWidth(35);
        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);
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
                ImageView imageView = new ImageView();
                imageView.setImage(image);
                imageView.setX(x);
                imageView.setY(y);
                imageView.setFitWidth(35);
                imageView.setPreserveRatio(true);
                root.getChildren().add(imageView);
                numResource++;
                x += 25;
            }
        }

    }
}