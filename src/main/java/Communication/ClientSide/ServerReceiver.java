package Communication.ClientSide;

import Communication.ServerSide.PlayerThread;
import Message.Message;
import Message.MessageReceived.*;

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
                object = (Message) this.receiver.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (object instanceof NotYourTurnMessage) {

                try {
                    System.out.println("It's not your turn.");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in receiver");
                    break;
                }

            }


            if (object instanceof UpdateClientMarketMessage) {
                try {
                    UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) object;
                    //IL MERCATO NON VIENE SERIALIZZATO
                    //Market market = updateClientMarketMessage.getMarket();
                    //market.printMarket();
                    //updateClientMarketMessage.getMarket().printMarket();
                    this.clientMain.setMarket(updateClientMarketMessage.getMarket());
                    //updateClientMarketMessage.getMarket().printMarket();
                    //this.clientMain.getMarket().printMarket();
                    System.out.println("Updated market");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in receiver");
                    break;
                }
            }

            if (object instanceof UpdateClientDevCardGridMessage) {
                try {
                    UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) object;
                    //IL MERCATO NON VIENE SERIALIZZATO
                    //ADESSO SI CONRESET
                    this.clientMain.setDevelopmentCardsDecksGrid(updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid());
                    //updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid().printGrid();
                    // System.out.println("Updated dev card grid");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in receiver");
                    break;
                }
            }

            if (object instanceof UpdateClientLeaderCardsMessage) {
                try {
                    UpdateClientLeaderCardsMessage updateClientLeaderCardsMessage = (UpdateClientLeaderCardsMessage) object;
                    this.clientMain.setLeaderCards(updateClientLeaderCardsMessage.getLeaderCards());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in receiver");
                    break;
                }
            }

            if (object instanceof UpdateClientPlayerBoardMessage) {
                try {

                    UpdateClientPlayerBoardMessage updateClientPlayerBoardMessage = (UpdateClientPlayerBoardMessage) object;
                    this.clientMain.setPlayerboard(updateClientPlayerBoardMessage.getPlayerboard());

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
                    this.receiver.close();
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
