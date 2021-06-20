package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SyncScenarioGUI {

    private HandlerGUI handlerGUI;

    public SyncScenarioGUI(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
    }

    public void matchHasStarted(Stage stage) {
        this.handlerGUI.getGenericClassGUI().addLabelByCode("Match has started, your player number is " + this.handlerGUI.getClientMain().getPlayerNumber(), stage);

        if(this.handlerGUI.getClientMain().getPlayerNumber() != 0) this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("startingResources", stage);
        else {
            this.handlerGUI.getMsg().sendStartingRes(new ArrayList<>());
            this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("discardStartingLeaders", stage);
        }
    }

    public void startingResource(Stage stage, ArrayList<String> resStart) {
        int num;

        if(this.handlerGUI.getClientMain().getPlayerNumber() == 1 || this.handlerGUI.getClientMain().getPlayerNumber() == 2) num = 1;
        else num = 2;


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
            this.handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[index], 80, 80);
            x = x + 200;
            root.getChildren().add(arrayButtons[index]);
            index++;
        }

        //Setting the stage
        Scene scene = new Scene(root, 740, 130);
        stage.setTitle("Pick initial resource");
        stage.setScene(scene);
        stage.show();

        String str;
        for(int j = 0; j < 4; j++) {
            if(j == 0) str = "COINS";
            else if(j == 1) str = "SERVANTS";
            else if(j == 2) str = "SHIELDS";
            else str = "STONES";
            String finalStr = str;

            arrayButtons[j].setOnAction(e -> {
                resStart.add(finalStr);
                if (resStart.size() == num) {
                    this.handlerGUI.getMsg().sendStartingRes(resStart);
                    this.handlerGUI.updatePlayerBoard();
                    discardStartingLeaders(stage, 1, -1);
                }
                else startingResource(stage, resStart);
            });
        }
    }

    public void discardStartingLeaders(Stage stage, int times, int cardDiscarded) {
        Group root = new Group();
        //Creating buttons
        int numButtons;
        if(cardDiscarded == -1) numButtons = 4;
        else numButtons = 3;
        Button[] arrayButtons = new Button[numButtons];

        int x = 0;
        int index = 0;
        LeaderCard[] leaderCards = this.handlerGUI.getClientMain().getLeaderCards();
        for (int i = 0; i < leaderCards.length; i++) {
            LeaderCard startingLeader = leaderCards[i];
            //Creating a graphic (image)
            if (cardDiscarded != i) {
                Image img = new Image(startingLeader.getImage());
                arrayButtons[index] = new Button();
                this.handlerGUI.getGenericClassGUI().createIconButton(x, 20, img, arrayButtons[index], 450, 150);
                x += 200;
                root.getChildren().add(arrayButtons[index]);
                index++;
            }
        }

        //Setting the stage
        Scene scene = new Scene(root, 800, 300);
        stage.setTitle("Discard initial leader cards");
        stage.setScene(scene);
        stage.show();

        for(int i = 0; i < numButtons; i++) {
            int finalI = i;
            arrayButtons[i].setOnAction(e -> {
                this.handlerGUI.getMsg().sendDiscardedLeader(finalI);
                if (times == 2) {
                    this.handlerGUI.getPlotScenarioGUI().choiceAction(stage);
                    this.handlerGUI.AsyncReceiver();
                    this.handlerGUI.updateLeaderCard();
                }
                else discardStartingLeaders(stage, 2, finalI);
            });
        }
    }
}
