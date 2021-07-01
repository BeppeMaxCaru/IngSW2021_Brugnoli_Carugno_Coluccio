package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.CLI.ServerSender;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class SyncScenarioGUI {

    /**
     * The GUI's handler
     */
    private HandlerGUI handlerGUI;

    /**
     *
     */
    private Stage stage;

    public SyncScenarioGUI(HandlerGUI handlerGUI, Stage stage) {
        this.handlerGUI = handlerGUI;
        this.stage = stage;
    }

    /**
     * Method that marks the start of the game and communicates the player number
     */
    public void matchHasStarted() {
        this.handlerGUI.getGenericClassGUI().addLabelByCode("Match has started, your player number is " + this.handlerGUI.getClientMain().getPlayerNumber(), this.stage);

        if(this.handlerGUI.getClientMain().getPlayerNumber() != 0) this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("startingResources");
        else {
            this.handlerGUI.getMsg().sendStartingRes(new ArrayList<>());
            this.handlerGUI.updatePlayerBoard();
            this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("discardStartingLeaders");
        }
    }

    /**
     * Method for choosing starting resource/s
     * @param resStart a list of choosing resource/s
     */
    public void startingResource(ArrayList<String> resStart) {
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
                    discardStartingLeaders(1, -1);
                }
                else startingResource(resStart);
            });
        }
    }

    /**
     * Method for discarding the two starting leader cards
     * @param times method iterator
     * @param cardDiscarded (at times = 2) first card discarded
     */
    public void discardStartingLeaders(int times, int cardDiscarded) {
        Group root = new Group();
        //Creating buttons
        int numButtons;
        if(cardDiscarded == -1) numButtons = 4;
        else numButtons = 3;
        Button[] arrayButtons = new Button[numButtons];

        int x = 0;
        int index = 0;
        LeaderCard[] leaderCards = this.handlerGUI.getClientMain().getLeaderCards();
        System.out.println(Arrays.toString(this.handlerGUI.getClientMain().getLeaderCards()));
        for (int i = 0; i < leaderCards.length; i++) {
            if (leaderCards[i] != null) {
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
        }

        //Setting the stage
        Scene scene = new Scene(root, 800, 300);
        stage.setTitle("Discard initial leader cards");
        stage.setScene(scene);
        stage.show();

        for(int i = 0; i < numButtons; i++) {
            int finalI = i;
            arrayButtons[i].setOnAction(e -> {
                // MultiPlayer
                if(this.handlerGUI.getGameMode() == 1) {
                    this.handlerGUI.getMsg().sendDiscardedLeader(finalI);
                    if (times == 2) {
                        this.handlerGUI.updateLeaderCard();
                        this.handlerGUI.AsyncReceiver();
                        Platform.runLater(this.handlerGUI.getPlayerBoardScenario());
                    }
                    else discardStartingLeaders(2, finalI);
                }
                // Single Player
                else {
                    if (times == 2) {
                        this.handlerGUI.getClientMain().getLocalPlayers()[0].discardLeaderCard(cardDiscarded);
                        this.handlerGUI.getClientMain().getLocalPlayers()[0].discardLeaderCard(finalI);
                        this.handlerGUI.getClientMain().setLeaderCards(this.handlerGUI.getClientMain().getLocalPlayers()[0].getPlayerLeaderCards());
                        this.handlerGUI.getPlotScenarioGUI().choiceAction();
                    }
                    else {
                        discardStartingLeaders(2, finalI);
                    }
                }
            });
        }
    }
}

