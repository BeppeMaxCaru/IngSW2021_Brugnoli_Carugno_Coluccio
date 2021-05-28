package Communication.ClientSide;

import Communication.ClientSide.RenderingView.RenderingView;
import Message.*;
import Message.ActivateProdMessage;
import Message.MessageSent.DiscardLeaderMessage;
import Message.MessageSent.PlayLeaderMessage;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerSender extends Thread {

    private final Socket socket;
    private final ClientMain clientMain;
    private final ObjectOutputStream sender;
    private final RenderingView view;

    public ServerSender (ClientMain clientMain, Socket socket, ObjectOutputStream sender, RenderingView view) {
        this.socket = socket;
        this.clientMain = clientMain;
        this.sender = sender;
        this.view = view;
    }

    @Override
    public void run() {

        Stage stage = new Stage();

        String action = "";
        //Keeps sending messages with switch until quit for now
        do {

            try {

                this.sender.reset();

                while (!action.equalsIgnoreCase("END TURN") && !action.equalsIgnoreCase("QUIT")) {

                    action = this.view.actionChoice(stage);

                    switch (action) {
                        case "P":
                        case "PLAY LEADER CARD": {

                            try {
                                int leader = this.view.playLeader(stage, this.clientMain.getLeaderCards());
                                PlayLeaderMessage playLeaderMessage = new PlayLeaderMessage(this.clientMain.getPlayerNumber(), leader);
                                this.sender.writeObject(playLeaderMessage);
                            } catch (Exception e) {
                                this.view.senderError(e);
                                break;
                            }
                            break;
                        }
                        case "D":
                        case "DISCARD LEADER CARD": {
                            System.out.println("Which card do you want to discard?");
                            for(int index =0; index<this.clientMain.getLeaderCards().length; index++)
                            {
                                if(this.clientMain.getLeaderCards()[index]!=null && !this.clientMain.getLeaderCards()[index].isPlayed())
                                {
                                    System.out.println("Write "+index+" for this");
                                    this.clientMain.getLeaderCards()[index].printLeaderCard();
                                }
                            }
                            String parameter = this.clientMain.getConsoleInput().nextLine();
                            try {
                                int leader = this.view.discardLeader(stage, this.clientMain.getLeaderCards());
                                DiscardLeaderMessage normalDiscardLeaderMessage = new DiscardLeaderMessage(this.clientMain.getPlayerNumber(), leader);
                                this.sender.writeObject(normalDiscardLeaderMessage);

                            } catch (Exception e) {
                                this.view.senderError(e);
                                break;
                            }
                            break;
                        }
                        case "M":
                        case "PICK RESOURCES FROM MARKET": {

                            int[] coordinates = this.view.marketCoordinates(stage, this.clientMain.getMarket());
                            String parameter;
                            int index;
                            if(coordinates[0] == 0) parameter = "ROW";
                            else parameter = "COLUMN";
                            index = coordinates[1];
                            String wlChoice = this.view.resourcesDestination(stage, this.clientMain.getLeaderCards(), parameter);
                            String chosenMarble = this.view.whiteMarbleChoice(stage);

                            MarketResourcesMessage resourcesMessage = new MarketResourcesMessage(this.clientMain.getPlayerNumber(), parameter, index, wlChoice, chosenMarble);
                            this.sender.writeObject(resourcesMessage);
                            break;

                        }

                        case "B":
                        case "BUY DEVELOPMENT CARD": {

                            int[] coordinates = this.view.developmentCardsGridCoordinates(stage,
                                    this.clientMain.getDevelopmentCardsDecksGrid(),
                                    this.clientMain.getPlayerboard());

                            int column = coordinates[0];
                            int level = coordinates[1];

                            //Check
                            int[] quantity = new int[4];
                            String[] shelf;
                            String[][] pickedResources = this.view.payDevelopmentCard(stage, this.clientMain.getLeaderCards());
                            for(int k = 0; k < quantity.length; k++)
                                quantity[k] = Integer.parseInt(pickedResources[0][k]);
                            shelf = pickedResources[1];

                            int pos = this.view.choosePosition(stage);

                            //Sending Card request

                            BuyCardMessage buyCard = new BuyCardMessage(column, level, this.clientMain.getPlayerNumber(), quantity, shelf, pos);
                            this.sender.writeObject(buyCard);
                            break;
                        }

                        case "A":
                        case "ACTIVATE PRODUCTION POWER": {

                            this.clientMain.getPlayerboard().printAll();
                            System.out.println("YOUR ACTIVATED LEADER CARDS :");
                            for(int ind =0; ind<this.clientMain.getLeaderCards().length; ind++)
                                if(this.clientMain.getLeaderCards()[ind]!=null && this.clientMain.getLeaderCards()[ind].isPlayed())
                                    this.clientMain.getLeaderCards()[ind].printLeaderCard();
                            System.out.println();


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

        } while (!action.equals("QUIT"));

        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Closing output stream and socket");
            //e.printStackTrace();
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
