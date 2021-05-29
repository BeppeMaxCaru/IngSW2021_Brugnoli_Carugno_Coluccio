package Communication.ClientSide.RenderingView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class GUI extends Application {

    String nickname;

    public static void main(String[] args) {
       /*
       start network... per far partire la rete
       boolean useGUI;
       if(useGUI)
           launch();
       else runCLI(); */

        launch(args);
    }

   /* @Override
    public void start(Stage stage) {
        stage.setTitle("Drawing Operations Test");
        Group root = new Group(); // per raggruppare oggetti
        Canvas canvas = new Canvas(300, 250); // tela su cui si scrive
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //drawShapes(gc);
        drawCards(gc);

        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show(); // renderizza
    }

    /* private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40}, new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90}, new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140}, new double[]{210, 210, 240, 240}, 4);
    }

    private void drawCards(GraphicsContext gc) {
       Image img = new Image("Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.jpg");
        gc.drawImage( img, 20, 20, 100, 100 );

        String[] cardNames =  new String[] {
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.jpg",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-2-1.jpg"
        };

        int x = 10;
        for(String item: cardNames) {
            Image img = new Image(item);
            gc.drawImage( img, 20, x, 100, 100 );
            x+=200;
        }
    } */


    @Override
    public void start(Stage stage) {
        nick(stage);
    }

    //@Override
    public String nick(Stage stage) {
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(5));

        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.NEVER);
        root.getColumnConstraints().add(cons1);

        ColumnConstraints cons2 = new ColumnConstraints();
        cons2.setHgrow(Priority.ALWAYS);

        root.getColumnConstraints().addAll(cons1, cons2);

        RowConstraints rcons1 = new RowConstraints();
        rcons1.setVgrow(Priority.NEVER);

        RowConstraints rcons2 = new RowConstraints();
        rcons2.setVgrow(Priority.ALWAYS);

        root.getRowConstraints().addAll(rcons1, rcons2);

        Label lbl = new Label("Insert nickname:");
        TextField field = new TextField();
        Button okBtn = new Button("Start game");

        okBtn.setOnAction(e -> {
            multiOrSinglePlayers(stage);
            //welcome(stage, field.getText());
        });

        GridPane.setHalignment(okBtn, HPos.RIGHT);

        root.add(lbl, 0, 0);
        root.add(field, 1, 0, 3, 1);
        root.add(okBtn, 2, 1);

        Scene scene = new Scene(root, 300, 100);

        stage.setTitle("Choose nickname");
        stage.setScene(scene);
        stage.show();

        this.nickname = field.getText();

        return field.getText();
    }

    public int multiOrSinglePlayers(Stage stage) {
        GridPane root = new GridPane();
        ToggleButton button1 = new ToggleButton("Single player");
        ToggleButton button2 = new ToggleButton("Multi player");
        root.add(button1, 0, 0);
        root.add(button2, 0, 1);

        Scene scene = new Scene(root, 300, 100);

        stage.setTitle("Multi Or Single Players?");
        stage.setScene(scene);
        stage.show();

        if(button1.isSelected()) return 0;
        else return 1;
    }

    public void welcome(Stage stage) {
        addLabelByCode(stage, "Loading...\nHi " + this.nickname + "!\nWelcome to Master of Renaissance online!");
        LoadWTFOnTimer(stage, "welcome");
    }

    public void matchHasStarted(Stage stage, int playerNumber) {
        addLabelByCode(stage, "Match has started, your player number is " + playerNumber);
    }

    public void startingResources(Stage stage) {
        stage.setTitle("Pick initial resources");
        Group root = new Group();
        Canvas canvas = new Canvas(730, 150);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        String[] resources =  new String[] {
                "coin.png",
                "servant.png",
                "shield.png",
                "stone.png"
        };

        int x = 10;
        for(String item: resources) {
            Image img = new Image(item);
            gc.drawImage( img, x, 20, 100, 100 );
            x+=200;
        }

        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));

        stage.show();
    }

    public void addLabelByCode(Stage stage, String string) {
        var label = new Label(string);
        label.setFont(Font.font(32));
        var scene = new Scene(new StackPane(label), 600, 200);
        stage.setScene(scene);
        stage.show();
    }

    static void LoadWTFOnTimer(Stage stage, String nameMethod) {
        TimerTask task = new TimerTask() {

            public void run() {

                Platform.runLater(() -> {
                    try {
                        System.out.println("loading..");


                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });
            }
        };


        Timer timer = new Timer("Timer");
        long delay = 5000L;
        timer.schedule(task, delay);
    }
}
