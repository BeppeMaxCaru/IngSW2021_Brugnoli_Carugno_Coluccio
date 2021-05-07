package org.example.Server;

import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//Coincide con GamesHandler e va tutto nel GameModel che diventa Runnable
public class ModifyGameModelWithThis implements Runnable {

    ArrayList<Player> clientsWaiting = new ArrayList<>();

    public ModifyGameModelWithThis(ArrayList<Player> clientsWaiting) {
        try {
            if (clientsWaiting.size() > 1) throw new Exception();

            for (int i = 0; i < 4; i++) {
                //Tolgo fino a quattro giocatori dalla lista di attesa del server
                //Aggiungere controllo se ce ne sono meno di 4
                if (clientsWaiting.isEmpty()) break;
                this.clientsWaiting.add(clientsWaiting.remove(0));
            }

        } catch (Exception e) {
            //Non faccio nulla se giocatori non sufficienti, cioè maggiori di 1
        }
        this.clientsWaiting = clientsWaiting;
    }

    //Fixare run per avviare gioco
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
