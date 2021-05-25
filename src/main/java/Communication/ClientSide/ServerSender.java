package Communication.ClientSide;

import Message.*;
import Message.ActivateProdMessage;
import Message.MessageSent.DiscardLeaderMessage;
import Message.MessageSent.PlayLeaderMessage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ServerSender extends Thread {

    private final Socket socket;
    private final ClientMain clientMain;
    private final ObjectOutputStream sender;

    public ServerSender (ClientMain clientMain, Socket socket, ObjectOutputStream sender) {
        this.socket = socket;
        this.clientMain = clientMain;
        this.sender = sender;
    }

    @Override
    public void run() {

        String action = "";
        //Keeps sending messages with switch until quit for now
        do {

            try {

                while (!action.equalsIgnoreCase("END TURN") && !action.equalsIgnoreCase("QUIT")) {

                    System.out.println("Which action do you want to do?");
                    System.out.println("Write 'Play leader card'");
                    System.out.println("Write 'Discard leader card'");
                    System.out.println("Write 'pick resources from Market'");
                    System.out.println("Write 'Buy development card'");
                    System.out.println("Write 'Activate production power'");
                    System.out.println("Write 'END TURN' at the end of your turn");
                    action = this.clientMain.getConsoleInput().nextLine().toUpperCase();

                    switch (action) {
                        case "P":
                        case "PLAY LEADER CARD": {
                            System.out.println("Which card do you want to play?");
                            System.out.println("Write 0 or 1.");
                            String parameter = this.clientMain.getConsoleInput().nextLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();

                                PlayLeaderMessage playLeaderMessage = new PlayLeaderMessage(this.clientMain.getPlayerNumber(), cardPosition);
                                this.sender.writeObject(playLeaderMessage);

                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }
                            break;
                        }
                        case "D":
                        case "DISCARD LEADER CARD": {
                            System.out.println("Which card do you want to discard?");
                            System.out.println("Write 0 or 1.");
                            String parameter = this.clientMain.getConsoleInput().nextLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();

                                DiscardLeaderMessage normalDiscardLeaderMessage = new DiscardLeaderMessage(this.clientMain.getPlayerNumber(), cardPosition);
                                this.sender.writeObject(normalDiscardLeaderMessage);

                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }
                            break;
                        }
                        case "M":
                        case "PICK RESOURCES FROM MARKET": {

                            this.clientMain.getMarket().printMarket();

                            //Receives column or row
                            System.out.println("Do you want to pick row resources or column resources?");
                            System.out.println("Write 'ROW' or 'COLUMN'.");
                            String parameter = this.clientMain.getConsoleInput().nextLine().toUpperCase();
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
                            String par = this.clientMain.getConsoleInput().nextLine();
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
                            String wlChoice = this.clientMain.getConsoleInput().nextLine().toUpperCase();
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
                            String chosenMarble = this.clientMain.getConsoleInput().nextLine().toUpperCase();
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

                            MarketResourcesMessage resourcesMessage = new MarketResourcesMessage(this.clientMain.getPlayerNumber(), parameter, index, wlChoice, chosenMarble);
                            this.sender.writeObject(resourcesMessage);
                            break;

                        }

                        case "B":
                        case "BUY DEVELOPMENT CARD": {

                            this.clientMain.getDevelopmentCardsDecksGrid().printGrid();

                            System.out.println("Which card do you want to buy?");
                            System.out.println("Write the correct colour: GREEN, YELLOW, BLUE or PURPLE, if existing in the grid");
                            String colour = this.clientMain.getConsoleInput().nextLine().toUpperCase();
                            if (!colour.equals("GREEN") && !colour.equals("YELLOW") && !colour.equals("BLUE") && !colour.equals("PURPLE")) {
                                //Resets controller
                                System.err.println("Not valid parameter");
                                break;
                            }

                            System.out.println("Which level do you want to buy?");
                            System.out.println("Write the correct number between 1 and 3, if existing in the grid");
                            String lev = this.clientMain.getConsoleInput().nextLine();
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
                                parameter = this.clientMain.getConsoleInput().nextLine().toUpperCase();
                                if (parameter.equals("COINS") || parameter.equals("STONES") || parameter.equals("SERVANTS") || parameter.equals("SHIELDS")) {
                                    int index = resources.get(parameter);

                                    //Receives now quantity
                                    System.out.println("How much " + parameter + " do you want to pick?");
                                    System.out.println("Write the correct value.");
                                    parameter = this.clientMain.getConsoleInput().nextLine();
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
                                        parameter = this.clientMain.getConsoleInput().nextLine().toUpperCase();
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

                            int pos;

                            System.out.println("In which position of your development card grid do you want to place the bought card?");
                            System.out.println("You can put a level 1 card in an empty position or a level 2/3 card on a level 1/2 card.");
                            System.out.println("Write a correct position between 0 and 2.");
                            parameter = this.clientMain.getConsoleInput().nextLine();

                            try {
                                pos = Integer.parseInt(parameter);
                            } catch (Exception e) {
                                System.out.println("Not valid position");
                                break;
                            }

                            //Sending Card request
                            BuyCardMessage buyCard = new BuyCardMessage(colour, level, this.clientMain.getPlayerNumber(), quantity, shelf, pos);
                            this.sender.writeObject(buyCard);
                            break;
                        }

                        case "A":
                        case "ACTIVATE PRODUCTION POWER": {

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
                                if (!anotherCommand) command = this.clientMain.getConsoleInput().nextLine(); // Comandi: p0, p1, p2, b, e0, e1, STOP
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
                                            unknownCommand = this.clientMain.getConsoleInput().nextLine(); // Comandi risorsa in input: 0, 1, 2, 3
                                            if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                whichInput[i] = whichInput[i] + unknownCommand;
                                                System.out.println("How much of them do you want to pick?");
                                                quant = this.clientMain.getConsoleInput().nextLine(); // Comandi quantità: 1, 2
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

                                                whichInput[i] = whichInput[i] + this.clientMain.getConsoleInput().nextLine().toUpperCase(); // Comandi from where: c, w, l
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
                                                unknownCommand = this.clientMain.getConsoleInput().nextLine(); // Comandi risorsa in input: 0, 1, 2, 3
                                                if (unknownCommand.equalsIgnoreCase("0") || unknownCommand.equalsIgnoreCase("1") || unknownCommand.equalsIgnoreCase("2") || unknownCommand.equalsIgnoreCase("3")) {
                                                    whichInput[3] = whichInput[3] + unknownCommand;
                                                    System.out.println("How much of them do you want to pick?");
                                                    quant = this.clientMain.getConsoleInput().nextLine(); // Comandi quantità: 1, 2
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
                                                    whichInput[3] = whichInput[3] + this.clientMain.getConsoleInput().nextLine().toUpperCase(); // Comandi from where: c, w, l
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

                                                unknownCommand = this.clientMain.getConsoleInput().nextLine(); // Comandi risorsa in input: 0, 1, 2, 3

                                                if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                    whichInput[4] = whichInput[4] + unknownCommand;
                                                    whichInput[4] = whichInput[4] + "1";

                                                    System.out.println("From which store do you want to pick this resource?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                    String store = this.clientMain.getConsoleInput().nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                    whichInput[4] = whichInput[4] + store; // Comandi from where: c, w, l
                                                    activation[4] = 1;
                                                }
                                            }
                                            if (command.equals("e1") && activation[5] == 0) {

                                                System.out.println("Which input resource do you want to spend?");
                                                for (int z = 0; z < 4; z++)
                                                    System.out.println("Write " + z + " for " + resources.get(z));

                                                unknownCommand = this.clientMain.getConsoleInput().nextLine(); // Comandi risorsa in input: 0, 1, 2, 3

                                                if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                    whichInput[5] = whichInput[5] + unknownCommand;
                                                    whichInput[5] = whichInput[5] + "1";

                                                    System.out.println("From which store do you want to pick this resource?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                    String store = this.clientMain.getConsoleInput().nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                    whichInput[5] = whichInput[5] + store; // Comandi from where: c, w, l
                                                    activation[5] = 1;
                                                }
                                            }

                                            System.out.println("Which output resource do you want to pick?");
                                            for (int z = 0; z < 4; z++)
                                                System.out.println("Write " + z + " for " + resources.get(z));
                                            System.out.println("Write 4 for REDCROSS");

                                            String com = this.clientMain.getConsoleInput().nextLine();

                                            if (com.equals("0") || com.equals("1") || com.equals("2") || com.equals("3") || com.equals("4")) {
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
                                    ActivateProdMessage prodMessage;
                                    if (activation[k] == 0) {
                                        prodMessage= new ActivateProdMessage(this.clientMain.getPlayerNumber(), null, null);
                                    } else {
                                        if (k <3) prodMessage = new ActivateProdMessage(this.clientMain.getPlayerNumber(), whichInput[k], null);
                                        else prodMessage = new ActivateProdMessage(this.clientMain.getPlayerNumber(), whichInput[k], whichOutput[3-k]);
                                    }
                                    this.sender.writeObject(prodMessage);
                                }
                            } else break;

                            break;
                        }
                        case "END TURN":
                        {
                            System.out.println("Your turn has ended, wait for other players");
                            break;
                        }
                        case "QUIT":
                        {
                            System.out.println("Your left the Game");
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
                System.err.println("No info about host");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for connection to host");
                System.exit(1);
            }


        } while (!action.equalsIgnoreCase("quit"));

        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Error while sending data to the server");
            e.printStackTrace();
        }
    }

    public static boolean checkActivateProduction (String[]commandList,int[] activation, String[] whichInput, String[] whichOutput) {

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
}
