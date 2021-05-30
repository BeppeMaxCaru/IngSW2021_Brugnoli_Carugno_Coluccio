package Communication.ClientSide;

import Communication.ClientSide.RenderingView.RenderingView;
import Communication.ServerSide.PlayerThread;
import Message.Message;
import Message.MessageReceived.*;
import Message.MessageSent.QuitMessage;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReceiver extends Thread {

    private Socket socket;
    private PlayerThread playerThread;

    private ClientMain clientMain;

    private final ObjectInputStream receiver;

    private RenderingView view;

    public ServerReceiver(ClientMain clientMain, Socket socket, ObjectInputStream receiver, RenderingView view) {
        this.clientMain = clientMain;
        this.socket = socket;
        this.receiver = receiver;
        this.view = view;
    }

    @Override
    public void run() {
        Stage stage = null;
        //Keeps receiving messages and current player actions

        //ASYNC PHASE
        while (true) {

            //Switch with different messages to receive and prints results

            Message object = null;

            try {
                object = (Message) this.receiver.readObject();
            } catch (Exception e) {
                this.view.receiverError(e);
            }


            if (object instanceof NotYourTurnMessage) {

                try {
                    this.view.notYourTurn(stage);
                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }

            }


            if (object instanceof UpdateClientMarketMessage) {
                try {
                    UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) object;

                    this.clientMain.setMarket(updateClientMarketMessage.getMarket());
                    this.view.setMarket(updateClientMarketMessage.getMarket());

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof UpdateClientDevCardGridMessage) {
                try {
                    UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) object;

                    this.clientMain.setDevelopmentCardsDecksGrid(updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid());
                    this.view.setDevCardsGrid(updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid());

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof UpdateClientLeaderCardsMessage) {
                try {
                    UpdateClientLeaderCardsMessage updateClientLeaderCardsMessage = (UpdateClientLeaderCardsMessage) object;
                    this.clientMain.setLeaderCards(updateClientLeaderCardsMessage.getLeaderCards());
                    this.view.setPlayerLeaders(updateClientLeaderCardsMessage.getLeaderCards());
                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof UpdateClientPlayerBoardMessage) {
                try {

                    UpdateClientPlayerBoardMessage updateClientPlayerBoardMessage = (UpdateClientPlayerBoardMessage) object;
                    this.clientMain.setPlayerboard(updateClientPlayerBoardMessage.getPlayerboard());
                    this.view.setBoard(updateClientPlayerBoardMessage.getPlayerboard());

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof GameOverMessage) {

                try {
                    GameOverMessage gameOverMessage = (GameOverMessage) object;
                    this.view.setGameOverMsg(gameOverMessage);
                    this.view.endMultiplayerGame(stage);

                    //SHUT BOTH THREAD AND STREAM
                    this.receiver.close();
                    this.interrupt();

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }

            }

            if (object instanceof QuitMessage) {

                try {
                    QuitMessage quitMessage = (QuitMessage) object;
                    System.out.println(quitMessage.getQuitMessage());

                    //SHUT BOTH THREAD AND STREAM
                    receiver.close();
                    this.interrupt();

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }

            }

        }
    }
}
