package Communication.ClientSide;

import Communication.ClientSide.RenderingView.RenderingView;
import Maestri.MVC.GameController;
import Message.Message;
import Message.MessageReceived.*;
import Message.MessageSent.PingMessage;
import Message.MessageSent.QuitMessage;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReceiver extends Thread {

    /**
     * Client main running the game
     */
    private final ClientMain clientMain;

    /**
     * Messages receiver
     */
    private final ObjectInputStream receiver;

    /**
     * Interface with methods adapted to the GUI
     */
    private final RenderingView view;

    /**
     * Socket to connect to the server
     */
    private Socket clientSocket;

    /**
     * Receiver used to read messages from the server
     * @param clientMain The client running the game
     * @param view The class with the methods overridden
     * @param clientSocket The socket with the connection
     * @param receiver The messages receiver
     */
    public ServerReceiver(ClientMain clientMain, RenderingView view, Socket clientSocket,  ObjectInputStream receiver) {
        this.clientMain = clientMain;
        this.view = view;
        this.clientSocket = clientSocket;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        //Keeps receiving messages and current player actions

        //ASYNC PHASE
        while (true) {

            //Switch with different messages to receive and prints results
            Message object;

            try {
                //this.receiver.reset();
                this.clientSocket.setSoTimeout(360000);
                object = (Message) this.receiver.readObject();
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Server down");
                this.view.receiverError(e);
                break;
            }

            if (object instanceof PingMessage) {
                try {
                    //System.out.println("ping ok -> connection stable");
                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }

            if(object instanceof YourTurnMessage){
                try {
                    this.view.itsYourTurn();
                } catch (Exception e) {
                    e.printStackTrace();
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof NotYourTurnMessage) {
                try {
                    this.view.notYourTurn();
                } catch (Exception e) {
                    e.printStackTrace();
                    this.view.receiverError(e);
                    break;
                }
            }

            if(object instanceof TurnOverMessage) {
                try {
                    TurnOverMessage turnOverMessage = (TurnOverMessage) object;
                    this.view.endTurn(turnOverMessage.getTurn());
                } catch (Exception e) {
                    e.printStackTrace();
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof UpdateClientMarketMessage) {
                try {

                    UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) object;
                    this.clientMain.setMarket(updateClientMarketMessage.getMarket());

                    //UpdateClientPlayerBoardMessage updateClientPlayerBoardMessage = (UpdateClientPlayerBoardMessage)

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof UpdateClientDevCardGridMessage) {
                try {

                    UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) object;
                    this.clientMain.setDevelopmentCardsDecksGrid(updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid());

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof UpdateClientLeaderCardsMessage) {
                try {

                    UpdateClientLeaderCardsMessage updateClientLeaderCardsMessage = (UpdateClientLeaderCardsMessage) object;
                    this.clientMain.setLeaderCards(updateClientLeaderCardsMessage.getLeaderCards());

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof UpdateClientPlayerBoardMessage) {
                try {

                    UpdateClientPlayerBoardMessage updateClientPlayerBoardMessage = (UpdateClientPlayerBoardMessage) object;
                    this.clientMain.setPlayerboard(updateClientPlayerBoardMessage.getPlayerboard());

                    //System.out.println(this.clientMain.getPlayerboard().getWareHouse().getWarehouseResources().toString());
                    //System.out.println("Ricevuto " + updateClientPlayerBoardMessage.getPlayerboard().getWareHouse().getWarehouseResources().toString());

                    //System.out.println("Qui viene fatto update singolo");
                    this.view.update();
                    //System.out.println("Ricevuta playerboard");

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }

            if (object instanceof GameOverMessage) {

                try {
                    GameOverMessage gameOverMessage = (GameOverMessage) object;
                    System.out.println("Received game over mex");
                    this.view.endMultiplayerGame(gameOverMessage);

                    //SHUT BOTH THREAD AND STREAM
                    this.receiver.close();
                    //Using a simple return?
                    return;

                    //this.interrupt();

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
                    this.receiver.close();
                    //this.interrupt();
                    return;

                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }

            }

            if (object instanceof ServerErrorMessage) {
                try {
                    ServerErrorMessage serverErrorMessage = (ServerErrorMessage) object;
                    this.view.serverError(serverErrorMessage.getErrorMessage());
                    return;
                } catch (Exception e) {
                    this.view.receiverError(e);
                    break;
                }
            }
        }
    }
}
