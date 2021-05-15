package Maestri.MVC;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class ViewTest extends Application {
    //contiene tutto il gamemodel ma modifica solo quello che gli viene notificato
    @Override
    public void start(Stage stage) {
        var javaVersion = System.getProperty("java.version");
        var javafxVersion = System.getProperty("javafx.version");

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion);
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}
