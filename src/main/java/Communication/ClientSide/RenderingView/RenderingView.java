package Communication.ClientSide.RenderingView;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.GameOverMessage;

import java.util.ArrayList;

public interface RenderingView {

    default void error(Exception e){
    }

    default void receiverError(Exception e){
    }

    default void senderError(Exception e){
    }

    default ArrayList<String> getStartingResource(){
        return null;
    }

    default int[] getDiscardedStartingLeaders(){
        return new int[]{0, 0};
    }

    default String getNickName(){
        return "";
    }

    default String getActionChoice(){
        return "";
    }

    default int getPlayedLeader(){
        return 0;
    }

    default int getDiscardedLeader(){
        return 0;
    }

    default int[] getMarketCoordinates(){
        return new int[]{0, 0};
    }

    default String getResourcesDestination(String parameter){
        return "";
    }

    default String getWhiteMarbleChoice(){
        return "";
    }

    default int[] getDevelopmentCardsGridCoordinates(){
        return new int[]{0, 0};
    }

    default String[][] getPayedResources(){
        return null;
    }

    default int getChosenPosition(){
        return 0;
    }

    default int getActivationProd(int[] activation){
        return 0;
    }

    default String getInputResourceProd(){
        return "";
    }

    default String getOutputResourceProd(){
        return "";
    }

    default void endTurn(){
    }

    default void quit(){
    }

    default void notValidAction(){
    }

    default void lorenzoFaithPoints(){
    }

    default void endLocalGame(){
    }

    default void drawActionCounter(){
    }

    default void notYourTurn(){
    }

    default void endMultiplayerGame(){
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
