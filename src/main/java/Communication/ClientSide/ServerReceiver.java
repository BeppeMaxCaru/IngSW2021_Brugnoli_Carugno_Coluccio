package Communication.ClientSide;

import Communication.ServerSide.PlayerThread;

import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class ServerReceiver extends Thread {

    private Socket socket;
    private PlayerThread playerThread;
    private Scanner in;

    public ServerReceiver(Socket socket, ClientMain clientMain) {
        this.socket = socket;
        this.playerThread = playerThread;

        try {
            this.in = new Scanner(new InputStreamReader(this.socket.getInputStream()));
        } catch (Exception e) {
            System.out.println("Input not working");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = this.in.nextLine();
            } catch (Exception e) {
                System.out.println("Error 404");
                e.printStackTrace();
                break;
            }
        }
    }
}
