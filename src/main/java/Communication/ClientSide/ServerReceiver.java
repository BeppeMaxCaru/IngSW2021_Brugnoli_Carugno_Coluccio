package Communication.ClientSide;

import Communication.ServerSide.PlayerThread;

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
        while (true) {
            //Switch with different messages to send
            try {
                String message = this.in.nextLine();
            } catch (Exception e) {
                System.err.println("Server receiver error");
                e.printStackTrace();
                break;
            }
        }
    }
}
