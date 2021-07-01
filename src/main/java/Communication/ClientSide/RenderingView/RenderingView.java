package Communication.ClientSide.RenderingView;

import Message.MessageReceived.GameOverMessage;

import java.io.ObjectInputStream;

/**
 * Represents the user interface
 */
public interface RenderingView {

    /**
     *
     * @param e
     */
    default void error(Exception e){
        e.printStackTrace();
    }

    /**
     *
     * @param e
     */
    default void receiverError(Exception e){
        e.printStackTrace();
    }

    /**
     *
     * @param e
     */
    default void senderError(Exception e){
        e.printStackTrace();
    }

    /**
     * Prints the end turn message
     * @param turn indicates if the players completed setup or game turn
     */
    default void endTurn(String turn){
    }

    /**
     * Prints the quit message
     */
    default void quit(){
    }

    /**
     * Prints that the action isn't valid
     */
    default void notValidAction(){
    }

    default void endLocalGame(int localWinner){
    }

    /**
     * Prints that isn't the turn of the player
     */
    default void notYourTurn(){
    }

    default void endMultiplayerGame(GameOverMessage msg){
    }

    /**
     * Prints that is the turn of the player
     */
    default void itsYourTurn(){}

    default void update(){}

    /**
     * Check
     * @param e Exception received
     */
    default void connectionError(Exception e){
        e.printStackTrace();
    }

    /**
     *
     * @param e
     */
    default void setupError(Exception e){
        e.printStackTrace();
    }

    /**
     *
     * @param e
     */
    default void gameError(Exception e){
        e.printStackTrace();
    }

    /**
     *
     * @param errorMessage
     */
    default void serverError(String errorMessage){
    }

    /**
     *
     * @param e
     */
    default void invalidInputError(Exception e){
        e.printStackTrace();
    }

    /**
     *
     * @param objectInputStream
     */
    default void receivePing(ObjectInputStream objectInputStream) {}

    /**
     *
     * @param clientError
     */
    default void clientError(String clientError) {}

}
