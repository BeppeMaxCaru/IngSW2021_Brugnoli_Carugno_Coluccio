package Communication.ServerSide;

import Maestri.MVC.GameController;
import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server
 */
public class MultiEchoServer {
    //Connection
    /**
     * Port used by the server
     */
    private final int port;

    /**
     * Builds the server
     * @param port Port used by the server
     */
    public MultiEchoServer(int port) {
        this.port = port;
    }
    //Clients
    private ArrayList<Player> clients = new ArrayList<>();
    //Lobby
    private ArrayList<Socket> lobby = new ArrayList<>();
    //GamesQueue
    private ArrayList<GameModel> gamesInProgress = new ArrayList<>();

    //basta che abbiano nome diverso nella stessa partita
    //Meglio nome diverso ovunque
    private HashSet<String> playersNicknames = new HashSet<>();

    //Chat
    private ArrayList<PlayerThread> queueFIFO = new ArrayList<>();

    /**
     * Starts the server
     */
    public void startServer() {
        //4 threads for 4 players
        //ExecutorService clientExecutor = Executors.newFixedThreadPool(4);

        ExecutorService gameExecutor = Executors.newCachedThreadPool();

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());// Porta non disponibile
            System.err.println("Port error");
            return;
        }
        System.out.println("Server ready for Masters of Renaissance");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {

                    if (queueFIFO.isEmpty() || queueFIFO.size()<2) throw new Exception();

                    //Passa hashset di nickname e verifica appena si connettono
                    GameController gameController = new GameController(queueFIFO);
                    //gameExecutor.execute(gameController);

                    //System.out.println("New game started");
                } catch (Exception e) {
                    //System.out.println("Not enough players");
                }
            }
        }, 0, 30000);

        while (true) {

            try {
                Socket playerSocket = serverSocket.accept();

                //Il Player adesso diventa PlayerThread
                //Viene creato
                PlayerThread newPlayer = new PlayerThread(playerSocket);

                //if
                //Viene aggiunto alla lista di attesa
                queueFIFO.add(newPlayer);
                System.out.println("Queue: " + queueFIFO.size());
                //this.playersNicknames.add(newPlayer.getNickName());

            } catch(IOException e) {
                System.out.println("Server failure!");
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        //clientExecutor.shutdown();
        gameExecutor.shutdown();
    }

    /**
     * The main of the server
     * @param args Arguments passed to the server
     */
    public static void main(String[] args) {
        MultiEchoServer echoServer = new MultiEchoServer(1234);
        echoServer.startServer();
    }


}
