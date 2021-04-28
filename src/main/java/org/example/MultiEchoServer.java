package org.example;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiEchoServer {
    //Connection
    private int port;
    public MultiEchoServer(int port) {
        this.port = port;
    }

    //Clients handling
    private List<EchoServerClientHandler> clients = new ArrayList<>();

    //GameModel
    private GameModel game1;

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
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                EchoServerClientHandler newClient = new EchoServerClientHandler(socket);
                executor.submit(newClient);
                //Better to make the thread do everything!
                this.clients.add(newClient);
                //Modificare costruttore player -> togliere nickName
                //Player player = new Player();
                //player.chooseNickname();
                //executor.execute();
            } catch(IOException e) {
                break; // Entrerei qui se serverSocket venisse chiuso
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        MultiEchoServer echoServer = new MultiEchoServer(1234);
        echoServer.startServer();
    }
}
