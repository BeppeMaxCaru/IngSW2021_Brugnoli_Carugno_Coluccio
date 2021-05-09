package org.example.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

//Keeps printing all the server messages before allowing to insert
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
                //User must enter input before 90 seconds
                //Spostarlo in server
                //serverSocket.setSoTimeout(90000);
                String serverMessage = in.readLine();
                //Disables timer after input
                //Spostarlo in server
                //serverSocket.setSoTimeout(0);
                if (serverMessage == null) break;
                System.out.println("Received: " + serverMessage);
            }
        } catch (SocketTimeoutException e) {
            System.err.println("You've been expelled for inactivity");
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
