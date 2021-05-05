package org.example.Server;

import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class LobbyHandler implements Runnable{

    ArrayList<EchoServerClientHandler> clientsWaiting = new ArrayList<>();

    public LobbyHandler(ArrayList<EchoServerClientHandler> clientsWaiting) {
        this.clientsWaiting = clientsWaiting;
    }

    @Override
    public void run() {
        try {
            while(true) {
                ServerSocket serverSocket = new ServerSocket();
                Socket clientSocket = new Socket();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
