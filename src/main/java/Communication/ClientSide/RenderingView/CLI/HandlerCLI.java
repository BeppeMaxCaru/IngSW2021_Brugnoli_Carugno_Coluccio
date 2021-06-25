package Communication.ClientSide.RenderingView.CLI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.ServerReceiver;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.MessageReceived.UpdateClientDevCardGridMessage;
import Message.MessageReceived.UpdateClientLeaderCardsMessage;
import Message.MessageReceived.UpdateClientMarketMessage;
import Message.MessageReceived.UpdateClientPlayerBoardMessage;

import Message.SendingMessages;
import Message.ServerStartingMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class HandlerCLI {

    private final ClientMain clientMain;
    private ObjectInputStream receiver;
    private ObjectOutputStream sender;
    private final CLI cli;
    private SendingMessages msg;

    public HandlerCLI(ClientMain main){
        this.clientMain = main;
        this.cli = new CLI(this.clientMain);
    }

    public void execute(){

        this.clientMain.setNickname(this.cli.getNickName());

        int gameMode = this.cli.getGameMode();
        //Single player
        if(gameMode==0){

            Player[] localPlayers = new Player[2];
            localPlayers[0] = new Player(this.clientMain.getNickname(), 0);
            localPlayers[1] = new Player("Lorenzo the Magnificent", 1);

            this.clientMain.setLocalPlayers(localPlayers);
            this.clientMain.setMarket(new Market());
            this.clientMain.setDevelopmentCardsDecksGrid(new DevelopmentCardsDecksGrid());
            this.clientMain.setActionCountersDeck(new ActionCountersDeck());
            this.clientMain.setLeaderCardDeck(new LeaderCardDeck());
            this.clientMain.setPlayerboard(localPlayers[0].getPlayerBoard());

            //Put 4 leader cards into first player space
            for(int index = 0; index < localPlayers[0].getPlayerLeaderCards().length; index++)
                this.clientMain.getLocalPlayers()[0].setPlayerLeaderCard(index, this.clientMain.getLeaderCardDeck().drawOneLeaderCard());
            this.clientMain.setLeaderCards(this.clientMain.getLocalPlayers()[0].getPlayerLeaderCards());

            int[] discard = this.cli.getDiscardedStartingLeaders();
            this.clientMain.getLocalPlayers()[0].discardLeaderCard(discard[0]);
            this.clientMain.getLocalPlayers()[0].discardLeaderCard(discard[1]);

            new ServerSender(this.clientMain, 0, null).start();

        } else {//MultiPlayer

            try {
                Socket clientSocket = new Socket(this.clientMain.getHostName(), this.clientMain.getPort());
                this.receiver = new ObjectInputStream(clientSocket.getInputStream());
                this.sender = new ObjectOutputStream(clientSocket.getOutputStream());
                this.msg = new SendingMessages(this.clientMain, this.cli, this.sender);
            } catch (Exception e) {
                //Error connect
                this.cli.connectionError(e);
            }

            //Send nickname message to server
            try {
                this.msg.sendNickname();
            } catch (Exception e) {
                this.cli.setupError(e);
                return;
            }

            this.cli.setClientStarted();

            try {
                UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) this.receiver.readObject();
                this.clientMain.setMarket(updateClientMarketMessage.getMarket());
            } catch (Exception e) {
                this.cli.setupError(e);
                return;
            }

            try {
                UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) this.receiver.readObject();
                this.clientMain.setDevelopmentCardsDecksGrid(updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid());
            } catch (Exception e) {
                this.cli.setupError(e);
                return;
            }

            //Receives and sends starting resources message
            try {
                //Receive first message
                ServerStartingMessage startingMessage = (ServerStartingMessage) this.receiver.readObject();
                this.clientMain.setPlayerNumber(startingMessage.getPlayerNumber());
                this.clientMain.setLeaderCards(startingMessage.getLeaderCards());
                this.cli.setGameStarted();

                //Send player number and starting resources
                ArrayList<String> startingRes = this.cli.getStartingResource();
                this.msg.sendStartingRes(startingRes);

                try {
                    UpdateClientPlayerBoardMessage playerBoardMessage = (UpdateClientPlayerBoardMessage) this.receiver.readObject();
                    this.clientMain.setPlayerboard(playerBoardMessage.getPlayerboard());
                } catch (Exception e) {
                    this.cli.setupError(e);
                    return;
                }

                //Receive from input 2 leader cards to be discarded
                int[] cards = this.cli.getDiscardedStartingLeaders();

                //Sends starting excess leader card to discard
                this.msg.sendDiscardedLeader(cards[0]);
                this.msg.sendDiscardedLeader(cards[1]);

            } catch (Exception e) {
                this.cli.setupError(e);
            }

            try {
                UpdateClientLeaderCardsMessage leaderCardsMessage = (UpdateClientLeaderCardsMessage) this.receiver.readObject();
                this.clientMain.setLeaderCards(leaderCardsMessage.getLeaderCards());
            } catch (Exception e) {
                this.cli.setupError(e);
                return;
            }

            //Starts async phase
            new ServerSender(this.clientMain, gameMode, this.msg).start();
            new ServerReceiver(this.clientMain, this.cli, this.receiver).start();

        }
    }

}
