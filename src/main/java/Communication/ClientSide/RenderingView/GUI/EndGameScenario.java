package Communication.ClientSide.RenderingView.GUI;

import javafx.application.Platform;
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
        winner();
    }

    public void winner() {
        String s;
        if(this.handlerGUI.getClientMain().getWinner().equals(this.handlerGUI.getClientMain().getNickname())) s = "you";
        else s = this.handlerGUI.getClientMain().getWinner();
        this.handlerGUI.getGenericClassGUI().addLabelByCode("The winner is " + s + "!\nYour victory points: " + this.handlerGUI.getClientMain().getVictoryPoints(), this.anotherStage);
        this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("exit");
    }
}
