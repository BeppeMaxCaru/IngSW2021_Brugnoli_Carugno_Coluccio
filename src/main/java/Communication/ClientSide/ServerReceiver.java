package Communication.ClientSide;

import Communication.ServerSide.PlayerThread;
import Maestri.MVC.GameController;
import Message.MessageReceived.ActionOutcomeMessage;
import Message.MessageReceived.GameOverMessage;
import Message.MessageReceived.QuitMessage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerReceiver extends Thread {

    private Socket socket;
    private PlayerThread playerThread;
    private Scanner in;

    private ClientMain clientMain;

    private ObjectInputStream receiver;

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

            //Switch with different messages to receive
            //and prints results


            //
            //SIMO USA TANTI TRY CATCH PICCOLI INVECE DI UNO GROSSO
            //
            //
            //
            //
            //
            //
            //
            //
            //
            //


            Object object = null;

            try {
                object = receiver.readObject();
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
                }

            }

            if (object instanceof GameOverMessage) {

                try {
                    GameOverMessage gameOverMessage = (GameOverMessage) object;
                    System.out.println("The winner is " + gameOverMessage.getWinner());
                    System.out.println("You made " + gameOverMessage.getWinner() + " victory points");

                    //SHUT BOTH THREAD AND STREAM
                    //
                    //
                    //
                    //

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (object instanceof QuitMessage) {

                try {
                    QuitMessage quitMessage = (QuitMessage) object;
                    System.out.println(quitMessage.getQuitMessage());

                    //SHUT BOTH THREAD AND STREAM
                    //
                    //
                    //
                    //

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            //
            //QUA SOTTO SI POSSONO INSERIRE MESSAGGI CHE AGGIORNANO
            //MERCATO E GRIGLIA
            //
            //
            //
            //
            //
            //
            //
            //

        }
    }
}
