package Communication.ClientSide.RenderingView;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import javafx.stage.Stage;

import java.util.ArrayList;

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

    default String start(Stage stage){
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

    default String[][] payDevelopmentCard(Stage stage, LeaderCard[] cards){
        return null;
    }

    default int choosePosition(Stage stage){
        return 0;
    }

}
