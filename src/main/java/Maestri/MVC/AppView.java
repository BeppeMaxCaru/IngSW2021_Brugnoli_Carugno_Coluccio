package Maestri.MVC;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppView extends Application {
    public static void main(String[] args) {
       /*
       start network... per far partire la rete
       boolean useGUI;
       if(useGUI)
           launch();
       else runCLI(); */

        launch(args);
    }

    @Override
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
    } */

    private void drawCards(GraphicsContext gc) {
       /* Image img = new Image("Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.jpg");
        gc.drawImage( img, 20, 20, 100, 100 ); */

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
    }
}
