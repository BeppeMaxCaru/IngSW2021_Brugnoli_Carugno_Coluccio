package Communication.ClientSide.RenderingView.GUI;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Wait scenario
 */
public class WaitForYourTurnScenario implements Runnable{

    /**
     * The GUI's handler
     */
    private final HandlerGUI handlerGUI;

    /**
     * Other stage to be set
     */
    private final Stage anotherStage;

    /**
     * Prepares the scenario
     * @param handlerGUI The GUI's handler
     * @param anotherStage The stage to be set
     */
    public WaitForYourTurnScenario(HandlerGUI handlerGUI, Stage anotherStage) {
        this.handlerGUI = handlerGUI;
        this.anotherStage = anotherStage;
    }

    @Override
    public void run() {
        waitForYourTurnAction();
    }

    /**
     * Method that put the player on wait for its turn
     */
    public void waitForYourTurnAction() {
        this.handlerGUI.getGenericClassGUI().addLabelByCode("Your turn is ended, wait some minutes!", this.anotherStage);
    }
}
