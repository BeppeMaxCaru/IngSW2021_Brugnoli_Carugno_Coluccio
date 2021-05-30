package Communication.ClientSide.RenderingView;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.GameOverMessage;
import javafx.stage.Stage;

import java.util.ArrayList;

public interface RenderingView {

    default void error(Exception e){
    }

    default void receiverError(Exception e){
    }

    default void senderError(Exception e){
    }

    default ArrayList<String> startingResource(Stage stage){
        return null;
    }

    default int[] discardStartingLeaders(Stage stage){
        return new int[]{0, 0};
    }

    default String nickName(Stage stage){
        return "";
    }

    default String actionChoice(Stage stage){
        return "";
    }

    default int playLeader(Stage stage){
        return 0;
    }

    default int discardLeader(Stage stage){
        return 0;
    }

    default int[] marketCoordinates(Stage stage){
        return new int[]{0, 0};
    }

    default String resourcesDestination(Stage stage, String parameter){
        return "";
    }

    default String whiteMarbleChoice(Stage stage){
        return "";
    }

    default int[] developmentCardsGridCoordinates(Stage stage){
        return new int[]{0, 0};
    }

    default String[][] payResources(Stage stage){
        return null;
    }

    default int choosePosition(Stage stage){
        return 0;
    }

    default int activationProd(Stage stage, int[] activation){
        return 0;
    }

    default String inputResourceProd(Stage stage){
        return "";
    }

    default String outputResourceProd(Stage stage){
        return "";
    }

    default void endTurn(Stage stage){
    }

    default void quit(Stage stage){
    }

    default void notValidAction(Stage stage){
    }

    default void lorenzoFaithPoints(Stage stage){
    }

    default void endLocalGame(Stage stage){
    }

    default void drawActionCounter(Stage stage){
    }

    default void notYourTurn(Stage stage){
    }

    default void endMultiplayerGame(Stage stage){
    }

    default void setPlayerNumber(int playerNumber) {
    }

    default void setStartingLeaders(LeaderCard[] leaders){
    }

    default void setPlayerLeaders(LeaderCard[] leaders){
    }

    default void setBoard(Playerboard board){
    }

    default void setMarket(Market market){
    }

    default void setDevCardsGrid(DevelopmentCardsDecksGrid grid){
    }

    default void setCountersDeck(ActionCountersDeck deck){
    }

    default void setLorenzoPlayerBoard(Playerboard board){
    }

    default void setLocalWinner(int winner){
    }

    default void setGameOverMsg(GameOverMessage msg){
    }

    default void setGameStarted(){
    }

    default void setClientStarted(){
    }

}
