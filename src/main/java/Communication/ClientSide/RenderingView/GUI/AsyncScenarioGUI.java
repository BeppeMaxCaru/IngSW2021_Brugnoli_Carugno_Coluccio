package Communication.ClientSide.RenderingView.GUI;

import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AsyncScenarioGUI {

    private HandlerGUI handlerGUI;

    public AsyncScenarioGUI(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
    }

    public void matchHasStarted(Stage stage) {
        handlerGUI.getGenericClassGUI().addLabelByCode("Match has started, your player number is " + handlerGUI.getPlayerNumber(), stage);

        if(handlerGUI.getPlayerNumber() != 0) handlerGUI.getGenericClassGUI().LoadWTFOnTimer("startingResources", stage);
        else handlerGUI.getGenericClassGUI().LoadWTFOnTimer("discardStartingLeaders", stage);
    }

    public void startingResource(Stage stage) {
        int i;

        if(handlerGUI.getPlayerNumber() == 1 || handlerGUI.getPlayerNumber() == 2) i = 1;
        else i = 2;

        for(; i > 0; i--) {
            String[] resources = new String[] {
                    "coin.png",
                    "servant.png",
                    "shield.png",
                    "stone.png"
            };

            Group root = new Group();
            //Creating buttons
            Button[] arrayButtons = new Button[4];

            int x = 10;
            int index = 0;
            for (String item : resources) {
                //Creating a graphic (image)
                Image img = new Image(item);
                arrayButtons[index] = new Button();
                handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index]);
                x = x + 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            stage.setTitle("Pick initial resource");
            stage.setScene(scene);
            stage.show();

            arrayButtons[0].setOnAction(e -> {
                handlerGUI.setStartingResource("COINS");
            });

            arrayButtons[1].setOnAction(e -> {
                handlerGUI.setStartingResource("SERVANT");
            });

            arrayButtons[2].setOnAction(e -> {
                handlerGUI.setStartingResource("SCHIELD");
            });

            arrayButtons[3].setOnAction(e -> {
                handlerGUI.setStartingResource("STONE");
            });
        }

        handlerGUI.getGenericClassGUI().LoadWTFOnTimer("discardStartingLeaders", stage);
    }

    public void discardStartingLeaders(Stage stage) {
        for(int j = 0; j < 2; j++) {
            Group root = new Group();
            //Creating buttons
            Button[] arrayButtons = new Button[4];

            int x = 0;
            int index = 0;
            for (LeaderCard startingLeader : this.handlerGUI.startingLeaders) {
                //Creating a graphic (image)
                Image img = new Image(startingLeader.getImage());
                arrayButtons[index] = new Button();
                handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index]);
                x += 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            stage.setTitle("Discard initial leader cards");
            stage.setScene(scene);
            stage.show();

            int finalJ = j;
            arrayButtons[0].setOnAction(e -> {
                handlerGUI.setDiscardedStartingLeaders(0, finalJ);
                arrayButtons[0].setVisible(false);
            });

            arrayButtons[1].setOnAction(e -> {
                handlerGUI.setDiscardedStartingLeaders(1, finalJ);
                arrayButtons[1].setVisible(false);
            });

            arrayButtons[2].setOnAction(e -> {
                handlerGUI.setDiscardedStartingLeaders(2, finalJ);
                arrayButtons[2].setVisible(false);
            });

            arrayButtons[3].setOnAction(e -> {
                handlerGUI.setDiscardedStartingLeaders(3, finalJ);
                arrayButtons[3].setVisible(false);
            });
        }

        while(handlerGUI.getCorrectAction() != 1) ;
        handlerGUI.getGenericClassGUI().LoadWTFOnTimer("choiceAction", stage);
    }
}
