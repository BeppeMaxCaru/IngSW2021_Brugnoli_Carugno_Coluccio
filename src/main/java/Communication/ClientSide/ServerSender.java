package Communication.ClientSide;

import Communication.ServerSide.PlayerThread;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.NicknameMessage;
import Message.ServerStartingMessage;

import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerSender extends Thread {
    private Socket socket;
    private ClientMain clientMain;
    private PrintWriter out;

    private ObjectOutputStream sender;

    public ServerSender (ClientMain clientMain, Socket socket, ObjectOutputStream sender) {
        this.socket = socket;
        this.clientMain = clientMain;
        this.sender = sender;

    }

    @Override
    public void run() {

        //inserire nickname
        //String nickname = stdIn.nextLine();
        //this.clientMain.setNickname(nickname);
        //out.println(nickname);

        //Sends first message with nickname
        /*try {
            NicknameMessage nicknameMessage = new NicknameMessage(this.clientMain.getNickname());
            sender.writeObject(nicknameMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Nickname not accepted");
            return;
        }*/

        //Keeps sending messages with switch until quit for now
        String consoleInput;

        //Keeps sending messages with switch until quit for now
        do {
            //Creazione messaggi da inviare va qua
            //Switch su classe da creare in base all'azione inserita
            consoleInput = this.clientMain.getConsoleInput().nextLine();

        } while (!consoleInput.equalsIgnoreCase("quit"));

        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Error while sending data to the server");
            e.printStackTrace();
        }
    }
}
