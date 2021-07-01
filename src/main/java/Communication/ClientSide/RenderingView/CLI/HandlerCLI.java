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
import java.util.Arrays;

public class HandlerCLI {

    /**
     * The client main where the game is running
     */
    private final ClientMain clientMain;

    /**
     * The socket to connect to the server
     */
    private Socket clientSocket;

    /**
     * Allows to read messages
     */
    private ObjectInputStream receiver;

    /**
     * Allows to send messages
     */
    private ObjectOutputStream sender;

    /**
     * The cli used for the game
     */
    private final CLI cli;

    /**
     * Utility class helpful to send messages
     */
    private SendingMessages msg;

    /**
     * Builds the handler for the cli
     * @param main client running the game
     */
    public HandlerCLI(ClientMain main){
        this.clientMain = main;
        this.cli = new CLI(this.clientMain);
    }

    /**
     * Starts Masters of Renaissance using the cli
     */
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

            //After
            this.clientMain.getLocalPlayers()[0].discardLeaderCard(this.cli.discarder());
            this.clientMain.setLeaderCards(this.clientMain.getLocalPlayers()[0].getPlayerLeaderCards());
            //System.out.println(Arrays.toString(this.clientMain.getLeaderCards()));
            this.clientMain.getLocalPlayers()[0].discardLeaderCard(this.cli.discarder());
            this.clientMain.setLeaderCards(this.clientMain.getLocalPlayers()[0].getPlayerLeaderCards());
            //System.out.println(Arrays.toString(this.clientMain.getLeaderCards()));

            new ServerSender(this.clientMain, 0, null).start();

        } else {//MultiPlayer

            try {
                this.clientSocket = new Socket(this.clientMain.getHostName(), this.clientMain.getPort());
                this.receiver = new ObjectInputStream(this.clientSocket.getInputStream());
                this.sender = new ObjectOutputStream(this.clientSocket.getOutputStream());
                this.msg = new SendingMessages(this.clientMain, this.cli, this.sender);
            } catch (Exception e) {
                //Error connect
                this.cli.connectionError(e);
            }

            //this.cli.receivePing(this.receiver);
            //System.out.println("Ping received");

            //Send nickname message to server
            try {
                this.msg.sendNickname();
            } catch (Exception e) {
                this.cli.setupError(e);
                return;
            }

            //this.cli.receivePing(this.receiver);

            this.cli.setClientStarted();

            try {
                UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) this.receiver.readObject();
                this.clientMain.setMarket(updateClientMarketMessage.getMarket());
            } catch (Exception e) {
                this.cli.setupError(e);
                return;
            }

            //this.cli.receivePing(this.receiver);

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

                try {

                    //System.out.println(Arrays.toString(this.clientMain.getLeaderCards()));

                    //Before
                    //this.msg.sendDiscardedLeader(this.cli.discardStartingCard());

                    //After
                    this.msg.sendDiscardedLeader(this.cli.discarder());

                    UpdateClientLeaderCardsMessage updateClientLeaderCardsMessage = (UpdateClientLeaderCardsMessage) this.receiver.readObject();
                    this.clientMain.setLeaderCards(updateClientLeaderCardsMessage.getLeaderCards());

                    //Check
                    System.out.println(Arrays.toString(this.clientMain.getLeaderCards()));

                    //Before
                    //this.msg.sendDiscardedLeader(this.cli.discardStartingCard());

                    //After
                    this.msg.sendDiscardedLeader(this.cli.discarder());

                    updateClientLeaderCardsMessage = (UpdateClientLeaderCardsMessage) this.receiver.readObject();
                    this.clientMain.setLeaderCards(updateClientLeaderCardsMessage.getLeaderCards());

                    //Check
                    System.out.println(Arrays.toString(this.clientMain.getLeaderCards()));

                } catch (Exception e) {
                    e.printStackTrace();
                    this.cli.setupError(e);
                }

                //Receive from input 2 leader cards to be discarded
                /*int[] cards = this.cli.getDiscardedStartingLeaders();

                //Sends starting excess leader card to discard
                this.msg.sendDiscardedLeader(cards[0]);
                this.msg.sendDiscardedLeader(cards[1]);*/

            } catch (Exception e) {
                this.cli.setupError(e);
            }

            /*try {
                UpdateClientLeaderCardsMessage leaderCardsMessage = (UpdateClientLeaderCardsMessage) this.receiver.readObject();
                this.clientMain.setLeaderCards(leaderCardsMessage.getLeaderCards());
            } catch (Exception e) {
                this.cli.setupError(e);
                return;
            }*/

            //Starts async phase
            new ServerSender(this.clientMain, gameMode, this.msg).start();
            new ServerReceiver(this.clientMain, this.cli, this.clientSocket,this.receiver).start();

        }
    }

}
