package Communication.ClientSide.RenderingView.GUI;

import javafx.stage.Stage;

/**
 * Error scenario
 */
public class ErrorScenario implements Runnable {

    /**
     * The GUI's handler
     */
    private final HandlerGUI handlerGUI;

    /**
     * The stage used to render the error
     */
    private final Stage stage;

    /**
     * The message to show
     */
    private final String message;

    /**
     * Prepares the error scenario
     * @param handlerGUI The GUI's handler
     * @param stage The stage to be set
     * @param message The message to show
     */
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
