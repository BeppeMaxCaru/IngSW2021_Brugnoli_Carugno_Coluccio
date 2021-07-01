package Communication.ClientSide.RenderingView.GUI;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GenericClassGUI {

    /**
     * The GUI's handler
     */
    private final HandlerGUI handlerGUI;

    /**
     * Builds a generic stage
     * @param handlerGUI The GUI's handler
     */
    public GenericClassGUI(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
    }

    /**
     * Method that shows on the stage a string
     * @param string the string
     * @param stage the stage
     */
    public void addLabelByCode(String string, Stage stage) {
        var label = new Label(string);
        label.setFont(Font.font(32));
        var scene = new Scene(new StackPane(label), 600, 200);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method that waits 4 seconds on a method
     * @param method the method that will be called after 4 seconds
     */
    public void LoadWTFOnTimer(String method) {
        ArrayList<String> resStart = new ArrayList<>();
        TimerTask task = new TimerTask() {

            public void run() {

                Platform.runLater(() -> {
                    try {
                        switch(method) {
                            case "startingResources":
                                handlerGUI.getSyncScenarioGUI().startingResource(resStart);
                                break;
                            case "discardStartingLeaders":
                                //Before
                                handlerGUI.getSyncScenarioGUI().discardStartingLeaders(1, -1);
                                break;
                            case "matchHasStarted":
                                handlerGUI.getSyncScenarioGUI().matchHasStarted();
                                break;
                            case "quit0":
                                System.exit(0);
                                break;
                            case "quit1": {
                                handlerGUI.getMsg().sendQuitMessage();
                                LoadWTFOnTimer("quit0");
                                break;
                            }
                        }

                    } catch (Exception e) {
                        Platform.runLater(new ErrorScenario(handlerGUI,handlerGUI.getStage(),"Error in loading..\nBye bye!"));
                    }
                });
            }
        };

        Timer timer = new Timer("Timer");
        long delay = 4000L;
        timer.schedule(task, delay);
    }

    /**
     * Method that create an icon button
     * @param x x coordinate on the stage
     * @param y y coordinate on the stage
     * @param img the image that will be the icon
     * @param button the button
     * @param height the button's height
     * @param width the button's width
     */
    public void createIconButton(int x, int y, Image img, Button button, int height, int width) {
        ImageView view = new ImageView(img);
        view.setFitHeight(height);
        view.setFitWidth(width);
        view.setPreserveRatio(true);

        //Setting the location of the button
        button.setTranslateX(x);
        button.setTranslateY(y);

        //Setting the size of the button
        button.setPrefSize(80, 80);

        //Setting a graphic to the button
        button.setGraphic(view);
    }
}
