package Communication.ClientSide.RenderingView;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCounter;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.GameOverMessage;
import javafx.stage.Stage;

public interface RenderingView {

    default void welcome(Stage stage, String nickName){
    }

    default void error(Exception e){
    }

    default void receiverError(Exception e){
    }

    default void senderError(Exception e){
    }

    default void matchHasStarted(Stage stage, int playerNumber){
    }

    default String startingResource(Stage stage){
        return "";
    }

    default int[] discardStartingLeaders(Stage stage, LeaderCard[] cards){
        return new int[]{0, 0};
    }

    default String nick(Stage stage){
        return "";
    }

    default String actionChoice(Stage stage){
        return "";
    }

    default int playLeader(Stage stage, LeaderCard[] cards){
        return 0;
    }

    default int discardLeader(Stage stage, LeaderCard[] cards){
        return 0;
    }

    default int[] marketCoordinates(Stage stage, Market market){
        return new int[]{0, 0};
    }

    default String resourcesDestination(Stage stage, LeaderCard[] cards, String parameter){
        return "";
    }

    default String whiteMarbleChoice(Stage stage){
        return "";
    }

    default int[] developmentCardsGridCoordinates(Stage stage, DevelopmentCardsDecksGrid grid, Playerboard playerboard){
        return new int[]{0, 0};
    }

    default String[][] payResources(Stage stage, LeaderCard[] cards){
        return null;
    }

    default int choosePosition(Stage stage){
        return 0;
    }

    default int activationProd(Stage stage, Playerboard playerboard, LeaderCard[] cards, int[] activation){
        return 0;
    }

    default String inputResourceProd(Stage stage, LeaderCard[] cards){
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

    default void lorenzoFaithPoints(Stage stage, int faithPoints){
    }

    default void endLocalGame(Stage stage, int winner, int victoryPoints){
    }

    default void drawActionCounter(Stage stage, ActionCountersDeck countersDeck, Playerboard playerboard, DevelopmentCardsDecksGrid developmentCardsDecksGrid){
    }

    default void notYourTurn(Stage stage){
    }

    default void endMultiplayerGame(Stage stage, GameOverMessage message){
    }

}
