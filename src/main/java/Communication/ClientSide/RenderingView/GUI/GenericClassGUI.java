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

import java.util.Timer;
import java.util.TimerTask;

public class GenericClassGUI {

    HandlerGUI handlerGUI;

    public GenericClassGUI(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
    }

    public void addLabelByCode(String string, Stage stage) {
        var label = new Label(string);
        label.setFont(Font.font(32));
        var scene = new Scene(new StackPane(label), 600, 200);
        stage.setScene(scene);
        stage.show();
    }

    public void LoadWTFOnTimer(String method, Stage stage) {
        TimerTask task = new TimerTask() {

            public void run() {

                Platform.runLater(() -> {
                    try {
                        System.out.println("loading..");
                        switch(method) {
                            case "startingResources":
                                handlerGUI.getAsyncScenarioGUI().startingResource(stage);
                            case "discardStartingLeaders":
                                handlerGUI.getAsyncScenarioGUI().discardStartingLeaders(stage);
                            case "matchHasStarted":
                                handlerGUI.getAsyncScenarioGUI().matchHasStarted(stage);
                            case "choiceAction":
                                handlerGUI.getPlotScenarioGUI().choiceAction(stage);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };

        Timer timer = new Timer("Timer");
        long delay = 3000L;
        timer.schedule(task, delay);
    }

    public void createIconButton(int x, Image img, Button button) {
        ImageView view = new ImageView(img);
        view.setFitHeight(80);
        view.setPreserveRatio(true);

        //Setting the location of the button
        button.setTranslateX(x);
        button.setTranslateY(20);

        //Setting the size of the button
        button.setPrefSize(80, 80);

        //Setting a graphic to the button
        button.setGraphic(view);
    }
}
