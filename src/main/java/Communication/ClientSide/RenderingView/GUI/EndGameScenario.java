package Communication.ClientSide.RenderingView.GUI;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * End game scenario
 */
public class EndGameScenario implements Runnable{

    /**
     * The handler for the GUI
     */
    private final HandlerGUI handlerGUI;

    /**
     * Stage used for the final game visual
     */
    private final Stage anotherStage;

    /**
     * Builds the final scenario
     * @param handlerGUI the handler handling the GUI
     * @param anotherStage a second stage
     */
    public EndGameScenario(HandlerGUI handlerGUI, Stage anotherStage) {
        this.handlerGUI = handlerGUI;
        this.anotherStage = anotherStage;
    }

    @Override
    public void run() {
        winner();
    }

    /**
     * Method that shows how is the winner and the victory points of the current player
     */
    public void winner() {
        String s = this.handlerGUI.getClientMain().getWinner();
        this.handlerGUI.getGenericClassGUI().addLabelByCode("The winner is " + s + "!\nYour victory points: " + this.handlerGUI.getClientMain().getVictoryPoints(), this.anotherStage);
        this.handlerGUI.getGenericClassGUI().LoadWTFOnTimer("quit0");
    }
}
