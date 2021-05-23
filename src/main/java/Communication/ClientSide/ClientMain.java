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
            int playerNumber=0;
            try {

                //Decapsulates first message
                ServerStartingMessage startingMessage = (ServerStartingMessage) receiver.readObject();
                playerNumber = startingMessage.getPlayerNumber();

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

            try {
                String action = "";
                int mainAction = 0;
                //do
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //Utilizzare shutdwon output per bloccare half write del socket del client al server
                while (!(action.equalsIgnoreCase("END TURN") || action.equalsIgnoreCase("QUIT"))) {

                    System.out.println("Which action do you want to do?");
                    System.out.println("Write 'Play leader card'");
                    System.out.println("Write 'Discard leader card");
                    System.out.println("Write 'Pick resources from market'");
                    System.out.println("Write 'Buy development card'");
                    System.out.println("Write 'Activate production power'");
                    System.out.println("Write 'End turn' at the end of your turn");
                    action = consoleInput.nextLine().toUpperCase();

                    //out.println();
                    switch (action) {
                        case "PLAY LEADER CARD": {
                            System.out.println("Which card do you want to play?");
                            System.out.println("Write 0 or 1.");
                            String parameter = consoleInput.nextLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();

                                PlayLeaderMessage playLeaderMessage = new PlayLeaderMessage(playerNumber, cardPosition);
                                sender.writeObject(playLeaderMessage);
                                sender.close();

                                //La parte di ricezione puoi adesso metterla tutte in serverReiver
                                //Dove continua a decapsulare i messaggi che riceve e stamparli su CLI
                                boolean serverResponse = (boolean) receiver.readObject();

                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }
                            break;
                        }
                        case "DISCARD LEADER CARD": {
                            System.out.println("Which card do you want to discard?");
                            System.out.println("Write 0 or 1.");
                            String parameter = consoleInput.nextLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();

                                DiscardLeaderMessage normalDiscardLeaderMessage = new DiscardLeaderMessage(playerNumber, cardPosition);
                                sender.writeObject(normalDiscardLeaderMessage);
                                sender.close();

                                boolean serverResponse = (boolean) receiver.readObject();

                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }
                            break;
                        }
                        case "PICK RESOURCES FROM MARKET": {

                            if (mainAction == 1) {
                                System.err.println("Action already done");
                                break;
                            }

                            //Receives column or row
                            System.out.println("Do you want to pick row resources or column resources?");
                            System.out.println("Write 'ROW' or 'COLUMN'.");
                            String parameter = consoleInput.nextLine().toUpperCase();
                            if (!parameter.equals("ROW") && !parameter.equals("COLUMN")) {
                                System.err.println("Not valid parameter");
                                break;
                            }

                            //Receives index
                            if (parameter.equals("ROW")) {
                                System.out.println("Which row do you want to pick?");
                                System.out.println("Write a number between 0 and 2.");
                            } else {
                                System.out.println("Which column do you want to pick?");
                                System.out.println("Write a number between 0 and 3.");
                            }
                            String par = consoleInput.nextLine();
                            int index;
                            try {
                                //Checks if the leader card position exists
                                index = Integer.parseInt(par);
                                if (parameter.equals("ROW") && (index < 0 || index > 2)) throw new Exception();
                                if (parameter.equals("COLUMN") && (index < 0 || index > 3)) throw new Exception();
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }

                            //Receives deposit
                            System.out.println("If you activated your extra warehouse space, where do you want to store your resources?");
                            if (parameter.equals("ROW"))
                                System.out.println("Write w for warehouse, l for leader card, for each of 4 resources you picked");
                            else
                                System.out.println("Write w for warehouse, l for leader card, for each of 3 resources you picked");
                            String wlChoice = consoleInput.nextLine().toUpperCase();
                            try {
                                //Checks if player has written only 'w' and 'l' chars
                                if (parameter.equals("ROW") && wlChoice.length() != 4) throw new Exception();
                                if (parameter.equals("COLUMN") && wlChoice.length() != 3) throw new Exception();
                                for (int k = 0; k < wlChoice.length(); k++)
                                    if (!String.valueOf(wlChoice.charAt(k)).equals("W") && !String.valueOf(wlChoice.charAt(k)).equals("L"))
                                        throw new Exception();
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }

                            //Receives position of leader cards to activate to receive a resource from a white marble
                            System.out.println("If you activated both your white marble resources leader card, which one do you want to activate for each white marble you picked?");
                            System.out.println("if you activated only one white marble leader card, do you want to activate it?");
                            System.out.println("Write 0 for activate your fist leader card, 1 for activate your second leader card, for each white marble you picked");
                            System.out.println("Write X if you don't want to activate any leader card effect");
                            String chosenMarble = consoleInput.nextLine().toUpperCase();
                            try {
                                //Checks if player has written only '0', '1' or 'x' chars
                                if (chosenMarble.length() != 0)
                                    for (int k = 0; k < wlChoice.length(); k++)
                                        if (!String.valueOf(chosenMarble.charAt(k)).equals("0")
                                                && !String.valueOf(chosenMarble.charAt(k)).equals("1")
                                                && !String.valueOf(chosenMarble.charAt(k)).equals("X"))
                                            throw new Exception();
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }

                            MarketResourcesMessage resourcesMessage = new MarketResourcesMessage(playerNumber, parameter, index, wlChoice, chosenMarble);
                            sender.writeObject(resourcesMessage);
                            sender.close();

                            //If server responds OK, action is correct
                            try {
                                boolean serverResponse = (boolean) receiver.readObject();
                                //Adesso è necessario che sia il playerThread a tenere conto che
                                //che l'azione principale è già stata fatta
                                //quindi il contatore va si PlayerThread e va incrementato dopo
                                //aver inviato messaggio di ok
                                //se arriva nuovo messaggio di azione principale si dice no
                                if (serverResponse)
                                    mainAction++;
                                else System.out.println("Not valid action.");
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }
                            break;

                        }

                        case "BUY DEVELOPMENT CARD": {

                            if (mainAction == 1) {
                                System.err.println("Action already done");
                                break;
                            }

                            System.out.println("Which card do you want to buy?");
                            System.out.println("Write the correct colour: GREEN, YELLOW, BLUE or PURPLE, if existing in the grid");
                            String colour = consoleInput.nextLine().toUpperCase();
                            if (!colour.equals("GREEN") && !colour.equals("YELLOW") && !colour.equals("BLUE") && !colour.equals("PURPLE")) {
                                //Resets controller
                                System.err.println("Not valid parameter");
                                break;
                            }

                            System.out.println("Which level do you want to buy?");
                            System.out.println("Write the correct number between 1 and 3, if existing in the grid");
                            String lev = consoleInput.nextLine();
                            int level;
                            try {
                                level = Integer.parseInt(lev);
                                if (level < 1 || level > 3) throw new Exception();
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }

                            //Check
                            int[] quantity = new int[4];
                            String[] shelf = new String[4];

                            //Correspondence between resources and arrays index
                            Map<String, Integer> resources = new HashMap<>();
                            resources.put("COINS", 0);
                            resources.put("SERVANTS", 1);
                            resources.put("SHIELDS", 2);
                            resources.put("STONES", 3);

                            for (int k = 0; k < 4; k++) {
                                quantity[k] = 0;
                                shelf[k] = null;
                            }

                            //Which resource do you want to take
                            String parameter = "";
                            while (!parameter.equalsIgnoreCase("STOP")) {

                                System.out.println("Which resource do you want to pick to pay the development card?");
                                parameter = consoleInput.nextLine().toUpperCase();
                                if (parameter.equals("COINS") || parameter.equals("STONES") || parameter.equals("SERVANTS") || parameter.equals("SHIELDS")) {
                                    int index = resources.get(parameter);

                                    //Receives now quantity
                                    System.out.println("How much " + parameter + " do you want to pick?");
                                    System.out.println("Write the correct value.");
                                    parameter = consoleInput.nextLine();
                                    try {
                                        int q = Integer.parseInt(parameter);
                                        if (q + quantity[index] < 0) throw new Exception();
                                        if (q + quantity[index] > 7) throw new Exception();
                                        quantity[index] = q;
                                    } catch (Exception e) {
                                        System.err.println("Not valid parameter");
                                        break;
                                    }

                                    for (int z = 0; z < quantity[index]; z++) {
                                        //Keeps asking a place to take from resources
                                        System.out.println("From which store do you want to pick this resource?");
                                        System.out.println("Write WAREHOUSE, CHEST, or LEADER CARD if you activate your extra warehouse space leader card.");
                                        parameter = consoleInput.nextLine().toUpperCase();
                                        if (parameter.equals("CHEST") || parameter.equals("WAREHOUSE") || parameter.equals("LEADER CARD")) {
                                            shelf[index] = shelf[index] + parameter.charAt(0);
                                        } else {
                                            System.err.println("Not valid parameter");
                                            break;
                                        }
                                    }

                                } else {
                                    System.err.println("Not existing resource");
                                    break;
                                }
                            }

                            //Sending Card request
                            BuyCardMessage buyCard = new BuyCardMessage(colour, level, playerNumber, quantity, shelf);
                            sender.writeObject(buyCard);
                            sender.close();

                            int pos;
                            try {
                                ServerCardAvailabilityMessage serverMessage = (ServerCardAvailabilityMessage) receiver.readObject();

                                System.out.println("In which position of your development card grid do you want to place the bought card?");
                                System.out.println("You can put a level 1 card in an empty position or a level 2/3 card on a level 1/2 card.");
                                System.out.println("Write a correct position between 0 and 2.");
                                parameter = consoleInput.nextLine();
                                pos = Integer.parseInt(parameter);
                                while (!serverMessage.getCardPositions().contains(pos)) {
                                    System.out.println("Not valid position.");
                                    //Position where to place the card on the playerboard
                                    System.out.println("In which position of your development card grid do you want to place the bought card?");
                                    System.out.println("You can put a level 1 card in an empty position or a level 2/3 card on a level 1/2 card.");
                                    System.out.println("Write a correct position between 0 and 2.");
                                    parameter = consoleInput.nextLine();
                                    pos = Integer.parseInt(parameter);
                                }


                                //Sending card position
                                DevCardPositionMessage positionMessage = new DevCardPositionMessage(playerNumber, pos);
                                sender.writeObject(positionMessage);
                                sender.close();

                                //If server responds OK, action is correct
                                boolean serverResponse = (boolean) receiver.readObject();
                                if (serverResponse)
                                    mainAction++;
                                else System.out.println("Not valid action.");
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                break;
                            }
                            break;
                        }

                        case "ACTIVATE PRODUCTION POWER": {

                            if (mainAction == 1) {
                                System.err.println("Action already done");
                                break;
                            }

                            sender.writeObject(action);

                            int[] activation = {0, 0, 0, 0, 0, 0};
                            String[] commandsList = new String[6];
                            String[] whichInput = new String[6];
                            String[] whichOutput = new String[3];
                            String unknownCommand;
                            String command = null;
                            int j, i = 0;
                            String quant;
                            boolean anotherCommand = false;

                            //Correspondence between resources and index
                            Map<Integer, String> resources = new HashMap<>();
                            resources.put(0, "COINS");
                            resources.put(1, "SERVANTS");
                            resources.put(2, "SHIELDS");
                            resources.put(3, "STONES");

                            j = 0;
                            for (int index = 0; index < 6; index++) {

                                System.out.println("Which production power do you want to activate?");
                                if (activation[0] == 0)
                                    System.out.println("Write p0 if you want to activate the first production of your grid, if it's available");
                                if (activation[1] == 0)
                                    System.out.println("Write p1 if you want to activate the second production of your grid, if it's available");
                                if (activation[2] == 0)
                                    System.out.println("Write p2 if you want to activate the third production of your grid, if it's available");
                                if (activation[3] == 0)
                                    System.out.println("Write b if you want to activate the basic production power");
                                if (activation[4] == 0)
                                    System.out.println("Write e0 if you want to activate the first extra production power, if it's available");
                                if (activation[5] == 0)
                                    System.out.println("Write e1 if you want to activate the second extra production power, if it's available");
                                System.out.println("Write STOP if you don't want to activate production powers");
                                if (!anotherCommand) command = consoleInput.nextLine(); // Comandi: p0, p1, p2, b, e0, e1, STOP
                                anotherCommand = false;

                                if (!command.equalsIgnoreCase("STOP")) {
                                    commandsList[index] = command; // lista di comandi, nel check si controlla la loro correttezza
                                    if (command.equalsIgnoreCase("p0") || command.equalsIgnoreCase("p1") || command.equalsIgnoreCase("p2")) {
                                        if (command.equalsIgnoreCase("p0") && activation[0] == 0) {
                                            i = 0;
                                            activation[0] = 1;
                                        }
                                        if (command.equalsIgnoreCase("p1") && activation[1] == 0) {
                                            i = 1;
                                            activation[1] = 1;
                                        }
                                        if (command.equalsIgnoreCase("p2") && activation[2] == 0) {
                                            i = 2;
                                            activation[2] = 1;
                                        }
                                        for (int k = 0; k < 2; k++) {
                                            System.out.println("Which input resource do you want to spend?");
                                            for (int z = 0; z < 4; z++)
                                                System.out.println("Write " + z + " for " + resources.get(z));
                                            unknownCommand = consoleInput.nextLine(); // Comandi risorsa in input: 0, 1, 2, 3
                                            if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                whichInput[i] = whichInput[i] + unknownCommand;
                                                System.out.println("How much of them do you want to pick?");
                                                quant = consoleInput.nextLine(); // Comandi quantità: 1, 2
                                                if (quant.equals("1") || quant.equals("2")) {
                                                    if (whichInput[i].length() > 3) {
                                                        if (String.valueOf(whichInput[i].charAt(1)).equals("2")) {
                                                            System.out.println("Not valid command.");
                                                            break;
                                                        } else if (String.valueOf(whichInput[i].charAt(1)).equals("1") && quant.equals("2")) {
                                                            System.out.println("Not valid command.");
                                                            break;
                                                        }
                                                    }
                                                    whichInput[i] = whichInput[i] + quant;
                                                }

                                                if (quant.equals("1"))
                                                    System.out.println("From which store do you want to pick this resource?");
                                                else
                                                    System.out.println("From which store do you want to pick these resources?");
                                                System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                System.out.println("Write 'c' if you want to pick resources from chest");
                                                System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                whichInput[i] = whichInput[i] + consoleInput.nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                if (quant.equals("2")) k++;
                                            } else {
                                                command = unknownCommand;
                                                if (command.equalsIgnoreCase("p0") || command.equalsIgnoreCase("p1") || command.equalsIgnoreCase("p2"))
                                                    anotherCommand = true;
                                                k = 1;
                                            }
                                        }
                                    }

                                    if (!anotherCommand) {
                                        // Se attiva il potere di produzione base
                                        if (command.equalsIgnoreCase("b") && activation[3] == 0) {

                                            /*for (int k = 0; k < 2; k++) {
                                                whichInput[3] = whichInput[3] + stdIn.readLine();  // Comandi risorsa in input: 0, 1, 2, 3
                                                quant = stdIn.readLine(); // Comandi quantità: 1, 2
                                                whichInput[3] = whichInput[3] + quant;
                                                whichInput[3] = whichInput[3] + stdIn.readLine(); // Comandi: c, w, e.
                                                if(quant.equals("2")) k++;
                                            }
                                             */

                                            for (int k = 0; k < 2; k++) {

                                                System.out.println("Which input resource do you want to spend?");
                                                for (int z = 0; z < 4; z++)
                                                    System.out.println("Write " + z + " for " + resources.get(z));
                                                unknownCommand = consoleInput.nextLine(); // Comandi risorsa in input: 0, 1, 2, 3
                                                if (unknownCommand.equalsIgnoreCase("0") || unknownCommand.equalsIgnoreCase("1") || unknownCommand.equalsIgnoreCase("2") || unknownCommand.equalsIgnoreCase("3")) {
                                                    whichInput[3] = whichInput[3] + unknownCommand;
                                                    System.out.println("How much of them do you want to pick?");
                                                    quant = consoleInput.nextLine(); // Comandi quantità: 1, 2
                                                    if (quant.equals("1") || quant.equals("2")) {
                                                        if (whichInput[3].length() > 3) {
                                                            if (String.valueOf(whichInput[3].charAt(1)).equals("2")) {
                                                                System.out.println("Not valid command.");
                                                                break;
                                                            } else if (String.valueOf(whichInput[3].charAt(1)).equals("1") && quant.equals("2")) {
                                                                System.out.println("Not valid command.");
                                                                break;
                                                            }
                                                        }
                                                        whichInput[3] = whichInput[3] + quant;
                                                    }

                                                    if (quant.equals("1"))
                                                        System.out.println("From which store do you want to pick this resource?");
                                                    else
                                                        System.out.println("From which store do you want to pick these resources?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");
                                                    whichInput[3] = whichInput[3] + consoleInput.nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                    if (quant.equals("2")) k++;
                                                } else {
                                                    command = unknownCommand;
                                                    if (command.equalsIgnoreCase("p0") || command.equalsIgnoreCase("p1") || command.equalsIgnoreCase("p2"))
                                                        anotherCommand = true;
                                                    k = 1;
                                                }
                                            }
                                            activation[3] = 1;
                                        }
                                        // Risorse a scelta
                                        if (command.equalsIgnoreCase("b") || command.equalsIgnoreCase("e0") || command.equalsIgnoreCase("e1")) {

                                            if (command.equals("e0") && activation[4] == 0) {

                                                System.out.println("Which input resource do you want to spend?");
                                                for (int z = 0; z < 4; z++)
                                                    System.out.println("Write " + z + " for " + resources.get(z));

                                                unknownCommand = consoleInput.nextLine(); // Comandi risorsa in input: 0, 1, 2, 3

                                                if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                    whichInput[4] = whichInput[4] + unknownCommand;
                                                    whichInput[4] = whichInput[4] + "1";

                                                    System.out.println("From which store do you want to pick this resource?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                    String store = consoleInput.nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                    whichInput[4] = whichInput[4] + store; // Comandi from where: c, w, l
                                                    activation[4] = 1;
                                                }
                                            }
                                            if (command.equals("e1") && activation[5] == 0) {

                                                System.out.println("Which input resource do you want to spend?");
                                                for (int z = 0; z < 4; z++)
                                                    System.out.println("Write " + z + " for " + resources.get(z));

                                                unknownCommand = consoleInput.nextLine(); // Comandi risorsa in input: 0, 1, 2, 3

                                                if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                    whichInput[5] = whichInput[5] + unknownCommand;
                                                    whichInput[5] = whichInput[5] + "1";

                                                    System.out.println("From which store do you want to pick this resource?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                    String store = consoleInput.nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                    whichInput[5] = whichInput[5] + store; // Comandi from where: c, w, l
                                                    activation[5] = 1;
                                                }
                                            }

                                            System.out.println("Which output resource do you want to pick?");
                                            for (int z = 0; z < 4; z++)
                                                System.out.println("Write " + z + " for " + resources.get(z));
                                            System.out.println("Write 4 for REDCROSS");

                                            String com = consoleInput.nextLine();

                                            if (com.equals("0") || com.equals("1") || com.equals("2") || com.equals("3") || com.equals("4")) {
                                                if (command.equalsIgnoreCase("b"))
                                                    j = 0;
                                                if (command.equalsIgnoreCase("e0"))
                                                    j = 1;
                                                else j = 2;
                                                whichOutput[j] = whichOutput[j] + com; // Comandi: COINS, SHIELDS...
                                            }

                                        }
                                    }
                                }
                            }
                            if (checkActivateProduction(commandsList, activation, whichInput, whichOutput)) {
                                for (int k = 0; k < 6; k++) {
                                    if (activation[k] == 0) {
                                        InputResourceMessage inputResourceMessage = new InputResourceMessage(playerNumber, null);
                                        sender.writeObject(inputResourceMessage);
                                    } else {
                                            InputResourceMessage inputResourceMessage = new InputResourceMessage(playerNumber, whichInput[k]);
                                            sender.writeObject(inputResourceMessage);
                                        if (k == 3 || k == 4 || k == 5) {
                                            OutputChoiceResourceMessage outputChoiceResourceMessage = new OutputChoiceResourceMessage(playerNumber, whichOutput[k]);
                                            sender.writeObject(outputChoiceResourceMessage);
                                            sender.close();
                                        }
                                    }
                                }
                            } else break;
                            //If server responds OK, action is correct
                            try {
                                boolean serverResponse = (boolean) receiver.readObject();
                                if (serverResponse)
                                    mainAction++;
                                else System.out.println("Not valid action.");
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }
                            break;
                        }
                        default: {
                            System.out.println("Not valid action!");
                            break;
                        }
                    }
                    //Player inserisce quit
                }
            } catch (UnknownHostException e) {
                System.err.println("No info about host: " + hostName);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for connection to hostname: " + hostName);
                System.exit(1);
            }
        }
    }

        public static boolean checkActivateProduction (String[]commandList,int[] activation, String[] whichInput, String[]
        whichOutput){

            PrintWriter out = new PrintWriter(System.out);

            int j;

            for (int i = 0; commandList[i] != null; i++) {
                if (!commandList[i].equals("p0") && !commandList[i].equals("p1") && !commandList[i].equals("p2") &&
                        !commandList[i].equals("b") && !commandList[i].equals("e0") && !commandList[i].equals("e1")) {
                    System.out.println("Not valid command.");
                    return false;
                }
            }


            for (int index = 0; index < 6; index++) {
                if (activation[index] == 1) {
                    j = 0;
                    for (int i = 0; i < whichInput[index].length() / 3; i++) {
                        if (whichInput[index].charAt(j) != '0' && whichInput[index].charAt(j) != '1' && whichInput[index].charAt(j) != '2' && whichInput[index].charAt(j) != '3') {
                            System.out.println("Not valid command.");
                            return false;
                        }
                        if (whichInput[index].charAt(j + 1) != '1' && whichInput[index].charAt(j + 1) != '2') {
                            System.out.println("Not valid command.");
                            return false;
                        }
                        if (whichInput[index].charAt(j + 2) != 'c' && whichInput[index].charAt(j + 2) != 'w' && whichInput[index].charAt(j + 2) != 'e') {
                            System.out.println("Not valid command.");
                            return false;
                        }
                        j++;
                    }
                }
            }

            for (String s : whichOutput) {
                if (s != null) {
                    if (!s.equals("0") && !s.equals("1") && !s.equals("2") && !s.equals("3") && !s.equals("4")) {
                        System.out.println("Not valid command.");
                        return false;
                    }
                }
            }

            return true;
        }

        public String getNickname () {
            return this.nickname;
        }

        public Scanner getConsoleInput () {
            return this.consoleInput;
        }
    }

