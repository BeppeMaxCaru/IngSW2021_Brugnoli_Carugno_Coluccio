package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.CLI.ServerSender;
import Communication.ClientSide.RenderingView.RenderingView;
import Communication.ClientSide.ServerReceiver;
import Message.MessageReceived.UpdateClientDevCardGridMessage;
import Message.MessageReceived.UpdateClientLeaderCardsMessage;
import Message.MessageReceived.UpdateClientMarketMessage;
import Message.MessageReceived.UpdateClientPlayerBoardMessage;
import Message.SendingMessages;
import Message.ServerStartingMessage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Testing extends Application implements RenderingView {

    private ClientMain clientMain;

    private String hostname;
    private int port;

    private int gameMode;
    private ObjectInputStream receiver;
    private ObjectOutputStream sender;
    private SendingMessages msg;

    //FARE TUTTO IN START!!!!!!!!!!!!!!!
    @Override
    public void start(Stage stage) throws Exception {
        Parameters args = getParameters();

        this.hostname = args.getUnnamed().get(0);
        this.port = Integer.parseInt(args.getUnnamed().get(1));

        this.clientMain = new ClientMain(args.getUnnamed().get(0), Integer.parseInt(args.getUnnamed().get(1)));

        System.out.println(this.clientMain.getHostName());
        stage.setTitle("Welcome");

        //Ask gamemode

        if (this.gameMode == 0) {

        } else if (this.gameMode == 1) {

            try {
                Socket socket = new Socket(this.hostname, this.port);
                this.receiver = new ObjectInputStream(socket.getInputStream());
                this.sender = new ObjectOutputStream(socket.getOutputStream());
                //Check passing this
                this.msg = new SendingMessages(this.clientMain, this, this.sender);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                this.msg.sendNickname();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) this.receiver.readObject();
                this.clientMain.setMarket(updateClientMarketMessage.getMarket());
            } catch (Exception e) {
                this.error(e);
            }

            try {
                UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) this.receiver.readObject();
                this.clientMain.setDevelopmentCardsDecksGrid(updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid());
            } catch (Exception e) {
                this.error(e);
            }

            try {
                //Receive first message
                ServerStartingMessage startingMessage = (ServerStartingMessage) this.receiver.readObject();
                this.clientMain.setPlayerNumber(startingMessage.getPlayerNumber());
                this.clientMain.setLeaderCards(startingMessage.getLeaderCards());
                //Serve?
                //this.view.setGameStarted();

                //Send player number and starting resources
                ArrayList<String> startingRes = this.getStartingResource();
                this.msg.sendStartingRes(startingRes);

                try {
                    UpdateClientPlayerBoardMessage playerBoardMessage = (UpdateClientPlayerBoardMessage) this.receiver.readObject();
                    this.clientMain.setPlayerboard(playerBoardMessage.getPlayerboard());
                } catch (Exception e) {
                    this.error(e);
                    return;
                }

                //Receive from input 2 leader cards to be discarded
                int[] cards = this.getDiscardedStartingLeaders();

                //Sends starting excess leader card to discard
                this.msg.sendDiscardedLeader(cards[0]);
                this.msg.sendDiscardedLeader(cards[1]);

            } catch (Exception e) {
                this.error(e);
            }

            try {
                UpdateClientLeaderCardsMessage leaderCardsMessage = (UpdateClientLeaderCardsMessage) this.receiver.readObject();
                this.clientMain.setLeaderCards(leaderCardsMessage.getLeaderCards());
            } catch (Exception e) {
                this.error(e);
            }

            //Qui parte server reciver e metti server sender
            //Inserirlo
            new ServerReceiver(this.clientMain, this, this.receiver).start();
            //Mettere ServerSender
            //o riscrivere
        }

    }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
        System.out.println(this.clientMain.getClass());
    }

}
