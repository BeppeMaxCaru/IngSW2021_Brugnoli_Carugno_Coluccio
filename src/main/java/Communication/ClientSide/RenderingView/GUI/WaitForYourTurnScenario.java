package Communication.ClientSide.RenderingView.GUI;

import javafx.application.Platform;
import javafx.stage.Stage;

public class WaitForYourTurnScenario implements Runnable{

    private final HandlerGUI handlerGUI;
    private final Stage anotherStage;

    public WaitForYourTurnScenario(HandlerGUI handlerGUI, Stage anotherStage) {
        this.handlerGUI = handlerGUI;
        this.anotherStage = anotherStage;
    }

    @Override
    public void run() {
        waitForYourTurnAction();
    }

    public void waitForYourTurnAction() {
        this.handlerGUI.getGenericClassGUI().addLabelByCode("Your turn is ended, wait some minutes!", this.anotherStage);
    }
}
