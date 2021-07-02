package Communication.ClientSide.RenderingView;

import Message.MessageReceived.GameOverMessage;

import java.io.ObjectInputStream;

/**
 * Represents the user interface
 */
public interface RenderingView {

    default void error(Exception e){
        e.printStackTrace();
    }

    /**
     * Handles receiver exception
     * @param e Exception to be handled
     */
    default void receiverError(Exception e){
        e.printStackTrace();
    }

    /**
     * Handles server exception
     * @param e Exception to be handled
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
     * Handles the quit
     */
    default void quit(){
    }

    /**
     * Handles not valid action
     */
    default void notValidAction(){
    }

    /**
     * Handles end game
     * @param localWinner The local winner number
     */
    default void endLocalGame(int localWinner){
    }

    /**
     * Signals that it's not the turn of the player
     */
    default void notYourTurn(){
    }

    /**
     * Handles game over
     * @param msg Game over message
     */
    default void endMultiplayerGame(GameOverMessage msg){
    }

    /**
     * Signals the turn of the player
     */
    default void itsYourTurn(){}

    /**
     * Updates the player status
     */
    default void update(){}

    /**
     * Handles connection exception
     * @param e Exception to be handled
     */
    default void connectionError(Exception e){
        e.printStackTrace();
    }

    /**
     * Handles setup exception
     * @param e Exception to be handled
     */
    default void setupError(Exception e){
        e.printStackTrace();
    }

    /**
     * Handles game exception
     * @param e Exception to be handled
     */
    default void gameError(Exception e){
        e.printStackTrace();
    }

    /**
     * Handles the error message received from the server
     * @param errorMessage Error message
     */
    default void serverError(String errorMessage){
    }

    /**
     * Handles invalid input exception
     * @param e Exception to be handled
     */
    default void invalidInputError(Exception e){
        e.printStackTrace();
    }

    default void receivePing(ObjectInputStream objectInputStream) {}

    /**
     * Handles client's error
     * @param clientError Error message
     */
    default void clientError(String clientError) {}

}
