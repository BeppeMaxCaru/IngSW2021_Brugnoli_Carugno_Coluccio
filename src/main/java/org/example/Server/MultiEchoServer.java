package org.example.Server;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
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

    public void startServer() {
        //4 threads for 4 players
        ExecutorService executor = Executors.newFixedThreadPool(4);
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            return;
        }
        System.out.println("Server ready for Masters of Renaissance");

        Timer timer = new Timer();
        try{
            timer.schedule(new GameModel(this.clients), 10000);
        } catch (Exception e){

        }

        while (true) {

            try {
                Socket clientSocket = serverSocket.accept();
                Player newClient = new Player(clientSocket);
                clients.add(newClient);

                executor.execute(newClient);
                //newClient.setName();
            } catch(IOException e) {
                System.out.println("Server failure!");
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }

    public void addClients() {
        try {

        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        MultiEchoServer echoServer = new MultiEchoServer(1234);
        echoServer.startServer();
    }
}
