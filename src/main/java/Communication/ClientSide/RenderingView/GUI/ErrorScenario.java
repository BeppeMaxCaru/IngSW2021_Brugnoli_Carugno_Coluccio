package Communication.ClientSide.RenderingView.GUI;

import javafx.stage.Stage;

public class ErrorScenario implements Runnable{
    private final HandlerGUI handlerGUI;
    private final Stage stage;
    private final String message;

    public ErrorScenario(HandlerGUI handlerGUI, Stage stage, String message) {
        this.handlerGUI = handlerGUI;
        this.stage = stage;
        this.message = message;
    }

    @Override
    public void run() {
        error();
    }

    /**
     * Method that shows the error
     */

    public void error() {
        this.handlerGUI.getGenericClassGUI().addLabelByCode(this.message, this.stage);
        this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("quit0");
    }
}
