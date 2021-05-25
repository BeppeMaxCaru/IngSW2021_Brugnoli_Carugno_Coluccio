package Communication.ClientSide;

import Communication.ServerSide.PlayerThread;
import Message.Message;
import Message.MessageReceived.ActionOutcomeMessage;
import Message.MessageReceived.GameOverMessage;
import Message.MessageReceived.QuitMessage;
import Message.MessageReceived.UpdateClientMarketMessage;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReceiver extends Thread {

    private Socket socket;
    private PlayerThread playerThread;

    private ClientMain clientMain;

    private final ObjectInputStream receiver;

    public ServerReceiver(ClientMain clientMain, Socket socket, ObjectInputStream receiver) {
        this.clientMain = clientMain;
        this.socket = socket;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        //Keeps receiving messages and current player actions

        //ASYNC PHASE
        while (true) {

            //Switch with different messages to receive and prints results

            Message object = null;

            try {
                object = (Message) receiver.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (object instanceof ActionOutcomeMessage) {

                try {
                    ActionOutcomeMessage actionOutcomeMessage = (ActionOutcomeMessage) object;
                    boolean outcome = actionOutcomeMessage.isActionOutcome();

                    if (outcome) {
                        System.out.println("Action successfully performed");
                    } else {
                        System.out.println("Action not valid");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in receiver");
                    break;
                }

            }

            if (object instanceof UpdateClientMarketMessage) {
                try {
                    UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) object;
                    this.clientMain.setMarket(updateClientMarketMessage.getMarket());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in receiver");
                    break;
                }
            }

            if (object instanceof GameOverMessage) {

                try {
                    GameOverMessage gameOverMessage = (GameOverMessage) object;
                    System.out.println("The winner is " + gameOverMessage.getWinner());
                    System.out.println("You made " + gameOverMessage.getWinner() + " victory points");

                    //SHUT BOTH THREAD AND STREAM
                    receiver.close();
                    this.interrupt();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in receiver");
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
                    e.printStackTrace();
                    System.out.println("Error in receiver");
                    break;
                }

            }

            //QUA SOTTO SI POSSONO INSERIRE MESSAGGI CHE AGGIORNANO
            //MERCATO E GRIGLIA

        }
    }
}
