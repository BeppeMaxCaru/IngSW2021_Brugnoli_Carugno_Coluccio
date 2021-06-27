package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
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

    private final HandlerGUI handlerGUI;
    //private final ClientMain clientMain;

    public GenericClassGUI(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
        //this.clientMain = clientMain;
    }

    public void addLabelByCode(String string, Stage stage) {
        var label = new Label(string);
        label.setFont(Font.font(32));
        var scene = new Scene(new StackPane(label), 600, 200);
        stage.setScene(scene);
        stage.show();
    }

    public void LoadWTFOnTimer(String method) {
        ArrayList<String> resStart = new ArrayList<>();
        TimerTask task = new TimerTask() {

            public void run() {

                Platform.runLater(() -> {
                    try {
                        System.out.println("loading..");
                        switch(method) {
                            case "startingResources":
                                handlerGUI.getSyncScenarioGUI().startingResource(resStart);
                                break;
                            case "discardStartingLeaders":
                                handlerGUI.getSyncScenarioGUI().discardStartingLeaders(1, -1);
                                break;
                            case "matchHasStarted":
                                handlerGUI.getSyncScenarioGUI().matchHasStarted();
                                break;
                            case "quit0":
                                System.exit(0);
                                break;
                            case "quit1":
                                handlerGUI.getMsg().sendQuitMessage();
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };

        Timer timer = new Timer("Timer");
        long delay = 4000L;
        timer.schedule(task, delay);
    }

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
