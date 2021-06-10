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
        System.out.println("Ciao");
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
                this.handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index], 80, 80);
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
                int finalI = i;

                if(j == 0) str = "COINS";
                else if(j == 1) str = "SERVANT";
                else if(j == 2) str = "SCHIELD";
                else str = "STONE";
                String finalStr = str;

                arrayButtons[j].setOnAction(e -> {
                    resStart.add(finalStr);
                    if (finalI - 1 == 0) {
                        this.handlerGUI.getMsg().sendStartingRes(resStart);
                        discardStartingLeaders(stage, 1);
                    }
                });
            }
        }
    }

    public void discardStartingLeaders(Stage stage, int check) {
        Group root = new Group();
        //Creating buttons
        Button[] arrayButtons = new Button[4];
        int x = 0;
        int index = 0;
        for (LeaderCard startingLeader : this.clientMain.getLeaderCards()) {
            //Creating a graphic (image)
            Image img = new Image(startingLeader.getImage());
            arrayButtons[index] = new Button();
            this.handlerGUI.getGenericClassGUI().createIconButton(x, img, arrayButtons[index], 450, 150);
            x += 200;
            root.getChildren().add(arrayButtons[index]);
            index++;
        }

        //Setting the stage
        Scene scene = new Scene(root, 800, 500);
        stage.setTitle("Discard initial leader cards");
        stage.setScene(scene);
        stage.show();

        for(int i = 0; i < 4; i++) {
            int finalI = i;
            arrayButtons[i].setOnAction(e -> {
                this.handlerGUI.getMsg().sendDiscardedLeader(finalI);
                if(check == 2) this.handlerGUI.getPlotScenarioGUI().choiceAction(stage);
                // gestire problema prima carta deve non essere stampata
                else discardStartingLeaders( stage, 2);
            });
        }
    }
}
