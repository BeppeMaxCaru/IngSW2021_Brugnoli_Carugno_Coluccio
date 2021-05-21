package Communication.ClientSide;

import Communication.ServerSide.PlayerThread;
import Maestri.MVC.Model.GModel.GamePlayer.Player;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerSender extends Thread {
    private Socket socket;
    private ClientMain clientMain;
    private PrintWriter out;
    private Scanner stdIn = new Scanner(System.in);

    public ServerSender (Socket socket, ClientMain clientMain) {
        this.socket = socket;
        this.clientMain = clientMain;

        try {
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.stdIn = new Scanner(System.in);
        } catch (Exception e) {
            System.out.println("Error 504");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        //inserire nickname
        String nickname = stdIn.nextLine();
        //this.clientMain.setNickname(nickname);
        out.println(nickname);

        String message;

        do {
            //Creazione messaggi da inviare va qua
            //Switch su classe da creare in base all'azione inserita
            message = stdIn.nextLine();
            out.println(message);
        } while (!message.equalsIgnoreCase("quit"));

        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Error while sending data to the server");
            e.printStackTrace();
        }
    }
}
