package org.example.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReceiver implements Runnable {
    private Socket serverSocket;
    private BufferedReader in;

    public ServerReceiver (Socket serverSocket) {
        this.serverSocket = serverSocket;
        try {
            this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String serverMessage = in.readLine();
                if (serverMessage == null) break;
                System.out.println("Received: " + serverMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
