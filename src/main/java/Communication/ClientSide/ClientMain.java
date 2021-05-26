package Communication.ClientSide;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.*;
import Message.MessageReceived.UpdateClientDevCardGridMessage;
import Message.MessageReceived.UpdateClientMarketMessage;
import Message.MessageSent.DiscardLeaderMessage;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientMain {

    private String hostName;
    private int port;

    private String nickname;
    private Scanner consoleInput = new Scanner(System.in);
    private int playerNumber;

    //TEST MEX MARKET
    private Market market;
    private DevelopmentCardsDecksGrid developmentCardsDecksGrid;

    public ClientMain(String hostname, int port) {
        this.hostName = hostname;
        this.port = port;
    }

    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        ClientMain client = new ClientMain(hostname, port);
        client.Execute();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void Execute() {

        String gameMode;
        Scanner consoleInput = new Scanner(System.in);

        System.out.println("Welcome to Masters of Renaissance!");

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

            Socket clientSocket;
            ObjectOutputStream sender;
            ObjectInputStream receiver;

            Market market;
            DevelopmentCardsDecksGrid developmentCardsDecksGrid;

            //Starts connection
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
                return;
            }

            //Per controllare inattività o disconnesione si può provare setSocketTimeout
            //sulla socket del playerThread e vedere se non arriva data dopo tot tempo

            //Send nickname message
            try {
                NicknameMessage nicknameMessage = new NicknameMessage(this.nickname);
                sender.writeObject(nicknameMessage);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Connection failed");
                return;
            }

            try {
                UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) receiver.readObject();
                this.market = updateClientMarketMessage.getMarket();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Client non setta market iniziale");
                return;
            }

            try {
                UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) receiver.readObject();
                this.developmentCardsDecksGrid = updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Client non riesce a settare grid iniziale");
                return;
            }

            //Receives and sends starting resources message
            try {
                //Decapsulates first message
                ServerStartingMessage startingMessage = (ServerStartingMessage) receiver.readObject();
                this.playerNumber = startingMessage.getPlayerNumber();

                //Asks starting resources
                Map<Integer, Integer> startingResources = new HashMap<>();
                startingResources.put(0, 0);
                startingResources.put(1, 1);
                startingResources.put(2, 1);
                startingResources.put(3, 2);

                System.out.println();
                System.out.println("Match has started, your player number is " + this.playerNumber);

                ArrayList<String> playerStartingResources = new ArrayList<>();
                String res;
                for (int resources = 0; resources < startingResources.get(this.playerNumber); resources++) {
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
                StartingResourcesMessage resourcesMessage = new StartingResourcesMessage(this.playerNumber, playerStartingResources);
                sender.writeObject(resourcesMessage);

                //Sends first starting excess leader card to discard
                System.out.println("Which starting leader card do you want to discard?");
                System.out.println();
                for (int i = 0; i < startingMessage.getLeaderCards().length; i++) {
                    System.out.println("Write " + i + " for this: ");
                    startingMessage.getLeaderCards()[i].printLeaderCard(System.out);
                }
                int card;
                try {
                    card = consoleInput.nextInt();
                    while (card < 0 || card > 3) {
                        System.out.println("Chose a correct card.");
                        card = consoleInput.nextInt();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in receiving setup");
                    return;
                }

                //Sends second starting excess leader card to discard
                DiscardLeaderMessage firstDiscardLeaderMessage = new DiscardLeaderMessage(this.playerNumber, card);
                sender.writeObject(firstDiscardLeaderMessage);

                System.out.println("Which starting leader card do you want to discard?");
                for (int i = 0; i < startingMessage.getLeaderCards().length; i++) {
                    if(i < card) {
                        System.out.println("Write " + i + " for this: ");
                        startingMessage.getLeaderCards()[i].printLeaderCard(System.out);
                    } else if (i>card) {
                        int k=i-1;
                        System.out.println("Write " + k + " for this: ");
                        startingMessage.getLeaderCards()[k].printLeaderCard(System.out);
                    }
                }
                try {
                    card = consoleInput.nextInt();
                    while (card < 0 || card > 2) {
                        System.out.println("Chose a correct card.");
                        card = consoleInput.nextInt();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Errore in scelta carta");
                    return;
                }

                firstDiscardLeaderMessage = new DiscardLeaderMessage(this.playerNumber, card);
                sender.writeObject(firstDiscardLeaderMessage);

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Initial resources setting failed");
            }

            //Starts async phase
            new ServerReceiver(this, clientSocket, receiver).start();
            new ServerSender(this, clientSocket, sender).start();

        }
    }

        public String getNickname () {
            return this.nickname;
        }

        public Scanner getConsoleInput () {
            return this.consoleInput;
        }

    public Market getMarket() {
        return this.market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public DevelopmentCardsDecksGrid getDevelopmentCardsDecksGrid() {
        return this.developmentCardsDecksGrid;
    }

    public void setDevelopmentCardsDecksGrid(DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
        this.developmentCardsDecksGrid = developmentCardsDecksGrid;
    }
}

