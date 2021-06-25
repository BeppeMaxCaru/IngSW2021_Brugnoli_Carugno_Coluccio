package Communication.ClientSide.RenderingView.GUI;

import javafx.stage.Stage;

public class EndGameScenario implements Runnable{

    private final HandlerGUI handlerGUI;
    private final Stage anotherStage;

    public EndGameScenario(HandlerGUI handlerGUI, Stage anotherStage) {
        this.handlerGUI = handlerGUI;
        this.anotherStage = anotherStage;
    }

    @Override
    public void run() {

    }
}
