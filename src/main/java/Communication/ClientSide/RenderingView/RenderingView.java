package Communication.ClientSide.RenderingView;

import Message.MessageReceived.GameOverMessage;

public interface RenderingView {

    default void error(Exception e){
    }

    default void receiverError(Exception e){
    }

    default void senderError(Exception e){
    }

    default void endTurn(){
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

}
