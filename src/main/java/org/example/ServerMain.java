package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class ServerMain {

    static int portNumber = 1234;

    public static void main( String args[]) {

        //String hostName = args[0];
        //int portNumber = Integer.parseInt(args[1]);

        System.out.println( "Server started!" );

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            //Gestire contenuto
            System.out.println("Cannot start server");
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            //Gestire contenuto
            System.out.println("Cannot accept client");
        }

        BufferedReader in;
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            //Gestire contenuto
        }

        String s = "";
        /*try {
            while ((s=in.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //Gestire
        }*/


    }
}
