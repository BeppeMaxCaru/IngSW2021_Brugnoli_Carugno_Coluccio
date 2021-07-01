package Communication.ClientSide.RenderingView;

import Message.MessageReceived.GameOverMessage;

import java.io.ObjectInputStream;

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

    default void endTurn(String turn){
    }

    default void quit(){
    }

    default void notValidAction(){
    }

    default void endLocalGame(int localWinner){
    }

    default void notYourTurn(){
    }

    default void endMultiplayerGame(GameOverMessage msg){
    }

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

}
