package Communication.ClientSide;

import Message.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class ClientMain {

    private String hostName;
    private int port;

    //Test
    private String nickname;
    private Scanner consoleInput = new Scanner(System.in);
    private int playerNumber;

    //Eventuale lista comandi da stampare per aiutare ma non guidare
    private List<String> commands = Arrays.asList("Play leader card", "Discard leader card");

    public ClientMain(String hostname, int port) {
        this.hostName = hostname;
        this.port = port;

        //Test
        //this.consoleInput = new Scanner(System.in);
        //System.out.println("Welcome to Master of Renaissance!");
        //System.out.println("Insert your nickname");
        //this.consoleInput.nextLine();
    }

    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        //ClientMain client = new ClientMain( "127.0.0.1", 1234);
        ClientMain client = new ClientMain(hostname, port);
        client.Execute();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void Execute() {

        //Passarli come parametri del main
        String gameMode;
        String nickName;
        Scanner consoleInput = new Scanner(System.in);

        // Game mode
        System.out.println("Welcome to Masters of Renaissance!");

        //Test
        System.out.println("Insert your nickname");
        this.nickname = this.consoleInput.nextLine();

        System.out.println("Write 0 for single-player or 1 for multiplayer: ");
        gameMode = consoleInput.nextLine();
        while (!gameMode.equals("0") && !gameMode.equals("1")) {
            System.out.println("Number not valid!");
            System.out.println("Write 0 for single-player or 1 for multiplayer: ");
            gameMode = consoleInput.nextLine();
        }

        if (gameMode.equals("0")) {

        } else {
            //Ask nickname before gameMode branch
            //Call player constructor directly on the server?
            //Player player = new Player();

            Socket clientSocket;
            ObjectOutputStream sender;
            ObjectInputStream receiver;

            //Starts connection
            //This should be the final client
            //Extremely simple! :)
            try {
                clientSocket = new Socket(this.hostName, this.port);
                sender = new ObjectOutputStream(clientSocket.getOutputStream());
                receiver = new ObjectInputStream(clientSocket.getInputStream());

                System.out.println("Loading...");
                System.out.println("Hi " + this.nickname + "!");
                System.out.println("Welcome to Master of Renaissance online!");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Connection failed");
                //Sono i return che garantiscono l'inizializzazione degli oggetti
                //permettedno di splittare un grosso try block in pezzi più piccoli
                return;
            }

            //Commento non c'entra nulla
            //Per controllare inattività o disconnesione si può provare setSocketTimeout
            //sulla socket del playerThread e vedere se non arriva data dopo tot tempo

            //1
            //Send nickname message
            try {
                NicknameMessage nicknameMessage = new NicknameMessage(this.nickname);
                sender.writeObject(nicknameMessage);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Connection failed");
                return;
            }

            //2 + 3 + 4
            //Receives and sends starting resources message
            try {

                //Decapsulates first message
                ServerStartingMessage startingMessage = (ServerStartingMessage) receiver.readObject();
                this.playerNumber = startingMessage.getPlayerNumber();

                //1
                //Asks starting resources
                Map<Integer, Integer> startingResources = new HashMap<>();
                startingResources.put(0, 0);
                startingResources.put(1, 1);
                startingResources.put(2, 1);
                startingResources.put(3, 2);

                System.out.println("Match has started, your player number is " + playerNumber);

                ArrayList<String> playerStartingResources = new ArrayList<>();
                String res;
                for (int resources = 0; resources < startingResources.get(playerNumber); resources++) {
                    System.out.println("Which starting resource do you want to pick?");
                    res = consoleInput.nextLine().toUpperCase();
                    while (!res.equals("COINS") && !res.equals("STONES") && !res.equals("SERVANTS") && !res.equals("SHIELDS")) {
                        System.out.println("Choose a correct resource");
                        System.out.println("Which starting resource do you want to pick?");
                        res = consoleInput.nextLine().toUpperCase();
                    }
                    playerStartingResources.add(res);
                }

                //Send player number and starting resources
                StartingResourcesMessage resourcesMessage = new StartingResourcesMessage(playerNumber, playerStartingResources);
                sender.writeObject(resourcesMessage);

                boolean serverResponse = (boolean) receiver.readObject();
                if(!serverResponse) throw new Exception();

                //2A
                //Sends first starting excess leader card to discard
                System.out.println("Which starting leader card do you want to discard?");
                for (int i = 0; i < startingMessage.getLeaderCards().length; i++) {
                    System.out.println("Write " + i + " for this: ");
                    //Out non si utilizza più
                    //PrintWriter scrive file non oggetti e quindi non è più adatto
                    //startingMessage.getLeaderCards()[i].printLeaderCard(out);
                    //Diventa così
                    //sender.writeObject(startingMessage);
                }
                int card = 0;
                try {
                    card = consoleInput.nextInt();
                    while (card < 0 || card > 3) {
                        System.out.println("Chose a correct card.");
                        card = consoleInput.nextInt();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //2B
                //Sends second starting excess leader card to discard
                DiscardLeaderMessage firstDiscardLeaderMessage = new DiscardLeaderMessage(playerNumber, card);
                //Devi togliere del tutto l'invio della stringa
                //Tanto sai già che tipo di messaggio ricevi tramite instanceOf
                sender.writeObject(firstDiscardLeaderMessage);

                serverResponse = (boolean) receiver.readObject();
                if(!serverResponse) throw new Exception();

                System.out.println("Which starting leader card do you want to discard?");
                for (int i = 0; i < 3; i++) {
                    System.out.println("Write " + i + " for this: ");
                    //Out non si utilizza più
                    //PrintWriter scrive file non oggetti e quindi non è più adatto
                    //startingMessage.getLeaderCards()[i].printLeaderCard(out);
                }
                card = 0;
                try {
                    card = consoleInput.nextInt();
                    while (card < 0 || card > 2) {
                        System.out.println("Chose a correct card.");
                        card = consoleInput.nextInt();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                firstDiscardLeaderMessage = new DiscardLeaderMessage(playerNumber, card);
                sender.writeObject(firstDiscardLeaderMessage);
                serverResponse = (boolean) receiver.readObject();
                if(!serverResponse) throw new Exception();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Initial resources setting failed");
                //clientSocket.close();
            }

            //4
            //Starts async phase
            new ServerReceiver(this, clientSocket, receiver).start();
            new ServerSender(this, clientSocket, sender).start();

            //Try catch + switch + while with !"quit"
            //Put it into ServerSender?
            try {

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Disconnected");
            }


        }
    }

        public String getNickname () {
            return this.nickname;
        }

        public Scanner getConsoleInput () {
            return this.consoleInput;
        }
    }

