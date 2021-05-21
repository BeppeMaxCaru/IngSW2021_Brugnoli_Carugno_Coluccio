package Communication.ServerSide;

import Maestri.MVC.GameController;
import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiEchoServer {
    //Connection
    private int port;
    public MultiEchoServer(int port) {
        this.port = port;
    }
    //Clients
    private ArrayList<Player> clients = new ArrayList<>();
    //Lobby
    private ArrayList<Socket> lobby = new ArrayList<>();
    //GamesQueue
    private ArrayList<GameModel> gamesInProgress = new ArrayList<>();
    //GameModel
    private GameModel game;

    //Chat
    private ArrayList<PlayerThread> queueFIFO = new ArrayList<>();

    public void startServer() {
        //4 threads for 4 players
        //ExecutorService clientExecutor = Executors.newFixedThreadPool(4);

        ExecutorService gameExecutor = Executors.newCachedThreadPool();

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());// Porta non disponibile
            System.out.println("Errore");
            return;
        }
        System.out.println("Server ready for Masters of Renaissance");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (clients.isEmpty() || clients.size()<2) throw new Exception();

                    //Margara
                    GameController gameController = new GameController(queueFIFO);
                    //Aggiornare
                    //gameExecutor.execute(newGame);


                    //Old
                    //GameController newGame = new GameController(clients);
                    //gameExecutor.execute(newGame);
                    //Debug
                    System.out.println("New game started");
                    //System.out.println(clients.size());
                } catch (Exception e) {
                    //Debug
                    System.out.println("Not enough players");
                    //System.out.println(clients.size());
                }
            }
        }, 0, 5000);

        while (true) {

            try {
                Socket playerSocket = serverSocket.accept();
                //Il Player adesso diventa PlayerThread
                //Viene creato
                PlayerThread newPlayer = new PlayerThread(playerSocket);
                //Viene aggiunto alla lista di attesa
                queueFIFO.add(newPlayer);

                //Old version
                //Player newClient = new Player(playerSocket);
                //newClient.setClientSocket(clientSocket);
                //clients.add(newClient);
                //clientExecutor.execute(newClient);
                //newClient.setName();
            } catch(IOException e) {
                System.out.println("Server failure!");
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        //clientExecutor.shutdown();
        gameExecutor.shutdown();
    }

    public static void main(String[] args) {
        MultiEchoServer echoServer = new MultiEchoServer(1234);
        echoServer.startServer();
    }
}
