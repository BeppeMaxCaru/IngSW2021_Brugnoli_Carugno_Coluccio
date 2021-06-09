package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AsyncScenarioGUI {

    private final HandlerGUI handlerGUI;
    private final ClientMain clientMain;

    public AsyncScenarioGUI(HandlerGUI handlerGUI, ClientMain clientMain) {
        this.handlerGUI = handlerGUI;
        this.clientMain = clientMain;
    }

    public void matchHasStarted(Stage stage) {
        this.handlerGUI.getGenericClassGUI().addLabelByCode("Match has started, your player number is " + this.clientMain.getPlayerNumber(), stage);

        if(this.clientMain.getPlayerNumber() != 0) this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("startingResources", stage);
        else this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("discardStartingLeaders", stage);
    }

    public void startingResource(Stage stage) {
        int i;
        ArrayList<String> resStart = new ArrayList<>();

        if(this.clientMain.getPlayerNumber() == 1 || this.clientMain.getPlayerNumber() == 2) i = 1;
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
                this.handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index]);
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
                resStart.add("COINS");
            });

            arrayButtons[1].setOnAction(e -> {
                resStart.add("SERVANT");
            });

            arrayButtons[2].setOnAction(e -> {
                resStart.add("SCHIELD");
            });

            arrayButtons[3].setOnAction(e -> {
                resStart.add("STONE");
            });

            if(i - 1 == 0) this.handlerGUI.getMsg().sendStartingRes(resStart);
        }

        this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("discardStartingLeaders", stage);
    }

    public void discardStartingLeaders(Stage stage) {
        for(int j = 0; j < 2; j++) {
            Group root = new Group();
            //Creating buttons
            Button[] arrayButtons = new Button[4];

            int x = 0;
            int index = 0;
            for (LeaderCard startingLeader : this.clientMain.getLeaderCards()) {
                //Creating a graphic (image)
                Image img = new Image(startingLeader.getImage());
                arrayButtons[index] = new Button();
                this.handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index]);
                x += 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }

            //Setting the stage
            Scene scene = new Scene(root, 740, 130);
            stage.setTitle("Discard initial leader cards");
            stage.setScene(scene);
            stage.show();

            arrayButtons[0].setOnAction(e -> {
                this.handlerGUI.getMsg().sendDiscardedLeader(0);
                arrayButtons[0].setVisible(false);
            });

            arrayButtons[1].setOnAction(e -> {
                this.handlerGUI.getMsg().sendDiscardedLeader(1);
                arrayButtons[1].setVisible(false);
            });

            arrayButtons[2].setOnAction(e -> {
                this.handlerGUI.getMsg().sendDiscardedLeader(2);
                arrayButtons[2].setVisible(false);
            });

            arrayButtons[3].setOnAction(e -> {
                this.handlerGUI.getMsg().sendDiscardedLeader(3);
                arrayButtons[3].setVisible(false);
            });
        }

        //while(this.handlerGUI.getCorrectAction() != 1) ;
        this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("choiceAction", stage);
    }
}
