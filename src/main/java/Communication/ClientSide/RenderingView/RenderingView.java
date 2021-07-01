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

    default void receiverError(Exception e){
        e.printStackTrace();
    }

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

    default void connectionError(Exception e){
        e.printStackTrace();
    }

    default void setupError(Exception e){
        e.printStackTrace();
    }

    default void gameError(Exception e){
        e.printStackTrace();
    }

    default void serverError(String errorMessage){
    }

    default void invalidInputError(Exception e){
        e.printStackTrace();
    }

    default void receivePing(ObjectInputStream objectInputStream) {}

    default void clientError(String clientError) {}

}
