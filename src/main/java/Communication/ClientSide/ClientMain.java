package Communication.ClientSide;

import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.*;
import Message.MessageReceived.UpdateClientDevCardGridMessage;
import Message.MessageReceived.UpdateClientLeaderCardsMessage;
import Message.MessageReceived.UpdateClientMarketMessage;
import Message.MessageReceived.UpdateClientPlayerBoardMessage;
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
    private Playerboard playerboard;
    private LeaderCard[] leaderCards = new LeaderCard[2];

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

            Player[] localPlayers = new Player[2];
            localPlayers[0] = new Player(this.nickname, 0);
            localPlayers[1] = new Player("Lorenzo the Magnificent", 1);

            this.market = new Market();
            this.developmentCardsDecksGrid = new DevelopmentCardsDecksGrid();
            LeaderCardDeck cardsDeck = new LeaderCardDeck();
            ActionCountersDeck actionCountersDeck = new ActionCountersDeck();

            //Put 4 leader cards into first player space
            for(int index = 0; index < localPlayers[0].getPlayerLeaderCards().length; index++)
                localPlayers[0].setPlayerLeaderCard(index, cardsDeck.drawOneLeaderCard());

            //Discard 2 leader cards
            for (int index = 0; index < 2; index++)
            {
                for (int cards = 0; cards < localPlayers[0].getPlayerLeaderCards().length - index; cards++)
                {
                    System.out.println("Write "+cards+" for this:");
                    localPlayers[0].getPlayerLeaderCards()[cards].printLeaderCard();
                }
                String card;
                try {
                    card = consoleInput.nextLine();
                    if(index==0)
                    {
                        while (!card.equals("0") && !card.equals("1") && !card.equals("2") && !card.equals("3")) {
                            System.out.println("Chose a correct card.");
                            card = consoleInput.nextLine();
                        }
                    } else {
                        while (!card.equals("0") && !card.equals("1") && !card.equals("2")) {
                            System.out.println("Chose a correct card.");
                            card = consoleInput.nextLine();
                        }
                    }
                    int discard = Integer.parseInt(card);
                    localPlayers[0].discardLeaderCard(discard);
                } catch (Exception e) {
                    System.out.println("Error in setup");
                    return;
                }
            }

            String action = "";
            do {
                while (!action.equals("END TURN") && !action.equals("QUIT")) {
                    System.out.println("Which action do you want to do?");
                    System.out.println("Write 'Play leader card'");
                    System.out.println("Write 'Discard leader card'");
                    System.out.println("Write 'pick resources from Market'");
                    System.out.println("Write 'Buy development card'");
                    System.out.println("Write 'Activate production power'");
                    System.out.println("Write 'END TURN' at the end of your turn");
                    action = this.consoleInput.nextLine().toUpperCase();

                    switch (action) {
                        case "P":
                        case "PLAY LEADER CARD": {
                            System.out.println("Which card do you want to play?");
                            for(int index =0; index < localPlayers[0].getPlayerLeaderCards().length; index++)
                            {
                                if(localPlayers[0].getPlayerLeaderCards()[index]!=null && !localPlayers[0].getPlayerLeaderCards()[index].isPlayed())
                                {
                                    System.out.println("Write "+index+" for this");
                                    localPlayers[0].getPlayerLeaderCards()[index].printLeaderCard();
                                }
                            }
                            String parameter = this.consoleInput.nextLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();

                                //Missing controller controls
                                localPlayers[0].playLeaderCard(cardPosition);

                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }
                            break;
                        }
                        case "D":
                        case "DISCARD LEADER CARD": {
                            System.out.println("Which card do you want to discard?");
                            for(int index =0; index<localPlayers[0].getPlayerLeaderCards().length; index++)
                            {
                                if(localPlayers[0].getPlayerLeaderCards()[index]!=null && !localPlayers[0].getPlayerLeaderCards()[index].isPlayed())
                                {
                                    System.out.println("Write "+index+" for this");
                                    localPlayers[0].getPlayerLeaderCards()[index].printLeaderCard();
                                }
                            }
                            String parameter = this.consoleInput.nextLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();

                                //Missing controller controls
                                localPlayers[0].discardLeaderCard(cardPosition);

                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                break;
                            }
                            break;
                        }
                        case "M":
                        case "PICK RESOURCES FROM MARKET": {

                            this.market.printMarket();

                            //Receives column or row
                            System.out.println("Do you want to pick row resources or column resources?");
                            System.out.println("Write 'ROW' or 'COLUMN'.");
                            String parameter = this.consoleInput.nextLine().toUpperCase();
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
                            String par = this.consoleInput.nextLine();
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
                            String wlChoice = this.consoleInput.nextLine().toUpperCase();
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

                            System.out.println("YOUR ACTIVATED LEADER CARDS:");
                            for(int ind =0; ind<localPlayers[0].getPlayerLeaderCards().length; ind++)
                                if(localPlayers[0].getPlayerLeaderCards()[ind]!=null && localPlayers[0].getPlayerLeaderCards()[ind].isPlayed())
                                    localPlayers[0].getPlayerLeaderCards()[ind].printLeaderCard();
                            System.out.println();

                            //Receives position of leader cards to activate to receive a resource from a white marble
                            System.out.println("If you activated both your white marble resources leader card, which one do you want to activate for each white marble you picked?");
                            System.out.println("if you activated only one white marble leader card, do you want to activate it?");
                            System.out.println("Write 0 for activate your fist leader card, 1 for activate your second leader card, for each white marble you picked");
                            System.out.println("Write X if you don't want to activate any leader card effect");
                            String chosenMarble = this.consoleInput.nextLine().toUpperCase();
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

                            //Missing controller controls
                            if (parameter.equals("ROW"))
                                this.market.updateRow(index, localPlayers, 0, wlChoice, chosenMarble);
                            else this.market.updateColumn(index, localPlayers, 0, wlChoice, chosenMarble);

                            break;

                        }

                        case "B":
                        case "BUY DEVELOPMENT CARD": {

                            localPlayers[0].getPlayerBoard().printAll();
                            this.getDevelopmentCardsDecksGrid().printGrid();

                            System.out.println("Which card do you want to buy?");
                            System.out.println("Write the correct colour: GREEN, YELLOW, BLUE or PURPLE, if existing in the grid");
                            String colour = this.consoleInput.nextLine().toUpperCase();
                            if (!colour.equals("GREEN") && !colour.equals("YELLOW") && !colour.equals("BLUE") && !colour.equals("PURPLE")) {
                                //Resets controller
                                System.err.println("Not valid parameter");
                                break;
                            }

                            System.out.println("Which level do you want to buy?");
                            System.out.println("Write the correct number between 1 and 3, if existing in the grid");
                            String lev = this.consoleInput.nextLine();
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
                            int index = 0;
                            while (!parameter.equalsIgnoreCase("STOP")) {

                                System.out.println("Which resource do you want to pick to pay the development card? Write STOP at the end.");
                                parameter = this.consoleInput.nextLine().toUpperCase();
                                if (parameter.equals("COINS") || parameter.equals("STONES") || parameter.equals("SERVANTS") || parameter.equals("SHIELDS") || parameter.equals("STOP")) {
                                    index = resources.get(parameter);

                                    //Receives now quantity
                                    System.out.println("How much " + parameter + " do you want to pick?");
                                    System.out.println("Write the correct value.");
                                    parameter = this.consoleInput.nextLine();
                                    try {
                                        int q = Integer.parseInt(parameter);
                                        if (q + quantity[index] < 0) throw new Exception();
                                        if (q + quantity[index] > 7) throw new Exception();
                                        quantity[index] = q;
                                    } catch (Exception e) {
                                        System.err.println("Not valid parameter");
                                        break;
                                    }

                                    //Player available leader cards
                                    System.out.println("YOUR ACTIVATED LEADER CARDS:");
                                    for(int ind =0; ind < localPlayers[0].getPlayerLeaderCards().length; ind++)
                                        if(localPlayers[0].getPlayerLeaderCards()[ind]!=null && localPlayers[0].getPlayerLeaderCards()[ind].isPlayed())
                                            localPlayers[0].getPlayerLeaderCards()[ind].printLeaderCard();
                                    System.out.println();

                                    for (int z = 0; z < quantity[index]; z++) {
                                        //Keeps asking a place to take from resources
                                        System.out.println("From which store do you want to pick this resource?");
                                        System.out.println("Write WAREHOUSE, CHEST, or LEADER CARD if you activate your extra warehouse space leader card.");
                                        parameter = this.consoleInput.nextLine().toUpperCase();
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
                            parameter = this.consoleInput.nextLine();

                            try {
                                pos = Integer.parseInt(parameter);
                            } catch (Exception e) {
                                System.out.println("Not valid position");
                                break;
                            }

                            //Missing controller controls
                            if(localPlayers[0].getPlayerBoard().isCardBelowCompatible(pos, this.developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][index][0]))
                                localPlayers[0].buyDevelopmentCard(this.developmentCardsDecksGrid, index, 3 - level, pos, shelf);
                            break;
                        }

                        case "A":
                        case "ACTIVATE PRODUCTION POWER": {

                            localPlayers[0].getPlayerBoard().printAll();
                            System.out.println("YOUR ACTIVATED LEADER CARDS :");
                            for(int ind =0; ind < localPlayers[0].getPlayerLeaderCards().length; ind++)
                                if(localPlayers[0].getPlayerLeaderCards()[ind]!=null && localPlayers[0].getPlayerLeaderCards()[ind].isPlayed())
                                    localPlayers[0].getPlayerLeaderCards()[ind].printLeaderCard();
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
                                if (!anotherCommand) command = this.consoleInput.nextLine().toLowerCase(); // Comandi: p0, p1, p2, b, e0, e1, STOP
                                anotherCommand = false;

                                if (!command.equalsIgnoreCase("STOP")) {
                                    commandsList[index] = command; // lista di comandi, nel check si controlla la loro correttezza
                                    if (command.equals("p0") || command.equals("p1") || command.equals("p2")) {
                                        if (command.equals("p0") && activation[0] == 0) {
                                            i = 0;
                                            activation[0] = 1;
                                        }
                                        if (command.equals("p1") && activation[1] == 0) {
                                            i = 1;
                                            activation[1] = 1;
                                        }
                                        if (command.equals("p2") && activation[2] == 0) {
                                            i = 2;
                                            activation[2] = 1;
                                        }
                                        for (int k = 0; k < 2; k++) {
                                            System.out.println("Which input resource do you want to spend?");
                                            for (int z = 0; z < 4; z++)
                                                System.out.println("Write " + z + " for " + resources.get(z));
                                            unknownCommand = this.consoleInput.nextLine(); // Comandi risorsa in input: 0, 1, 2, 3
                                            if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                whichInput[i] = whichInput[i] + unknownCommand;
                                                System.out.println("How much of them do you want to pick?");
                                                quant = this.consoleInput.nextLine(); // Comandi quantità: 1, 2
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

                                                whichInput[i] = whichInput[i] + this.consoleInput.nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                if (quant.equals("2")) k++;
                                            } else {
                                                command = unknownCommand.toLowerCase();
                                                if (command.equals("p0") || command.equals("p1") || command.equals("p2"))
                                                    anotherCommand = true;
                                                k = 1;
                                            }
                                        }
                                    }

                                    if (!anotherCommand) {
                                        // Se attiva il potere di produzione base
                                        if (command.equals("b") && activation[3] == 0) {

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
                                                unknownCommand = this.consoleInput.nextLine().toLowerCase(); // Comandi risorsa in input: 0, 1, 2, 3
                                                if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                    whichInput[3] = whichInput[3] + unknownCommand;
                                                    System.out.println("How much of them do you want to pick?");
                                                    quant = this.consoleInput.nextLine(); // Comandi quantità: 1, 2
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
                                                    whichInput[3] = whichInput[3] + this.consoleInput.nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                    if (quant.equals("2")) k++;
                                                } else {
                                                    command = unknownCommand;
                                                    if (command.equals("p0") || command.equals("p1") || command.equals("p2"))
                                                        anotherCommand = true;
                                                    k = 1;
                                                }
                                            }
                                            activation[3] = 1;
                                        }
                                        // Risorse a scelta
                                        if (command.equals("b") || command.equals("e0") || command.equals("e1")) {

                                            if (command.equals("e0") && activation[4] == 0) {

                                                System.out.println("Which input resource do you want to spend?");
                                                for (int z = 0; z < 4; z++)
                                                    System.out.println("Write " + z + " for " + resources.get(z));

                                                unknownCommand = this.consoleInput.nextLine(); // Comandi risorsa in input: 0, 1, 2, 3

                                                if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                    whichInput[4] = whichInput[4] + unknownCommand;
                                                    whichInput[4] = whichInput[4] + "1";

                                                    System.out.println("From which store do you want to pick this resource?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                    String store = this.consoleInput.nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                    whichInput[4] = whichInput[4] + store; // Comandi from where: c, w, l
                                                    activation[4] = 1;
                                                }
                                            }
                                            if (command.equals("e1") && activation[5] == 0) {

                                                System.out.println("Which input resource do you want to spend?");
                                                for (int z = 0; z < 4; z++)
                                                    System.out.println("Write " + z + " for " + resources.get(z));

                                                unknownCommand = this.consoleInput.nextLine(); // Comandi risorsa in input: 0, 1, 2, 3

                                                if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                    whichInput[5] = whichInput[5] + unknownCommand;
                                                    whichInput[5] = whichInput[5] + "1";

                                                    System.out.println("From which store do you want to pick this resource?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                    String store = this.consoleInput.nextLine().toUpperCase(); // Comandi from where: c, w, l
                                                    whichInput[5] = whichInput[5] + store; // Comandi from where: c, w, l
                                                    activation[5] = 1;
                                                }
                                            }

                                            System.out.println("Which output resource do you want to pick?");
                                            for (int z = 0; z < 4; z++)
                                                System.out.println("Write " + z + " for " + resources.get(z));
                                            System.out.println("Write 4 for REDCROSS");

                                            String com = this.consoleInput.nextLine();

                                            if (com.equals("0") || com.equals("1") || com.equals("2") || com.equals("3") || com.equals("4")) {
                                                if (command.equals("e0"))
                                                    j = 1;
                                                else j = 2;
                                                whichOutput[j] = whichOutput[j] + com; // Comandi: COINS, SHIELDS...
                                            }

                                        }
                                    }
                                }
                            }

                            //Missing controller and serverSender checkActivateProduction controls
                            int[] outputs = new int[3];
                            for(int k = 0; k<outputs.length; i++)
                                outputs[k] = Integer.parseInt(whichOutput[k]);

                            localPlayers[0].activateProduction(activation, whichInput, outputs);

                            break;
                        }
                        case "END TURN":
                        {
                            System.out.println("Your turn has ended.");
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
                }

                //
                actionCountersDeck.drawCounter().activate(actionCountersDeck, localPlayers[0].getPlayerBoard(), this.developmentCardsDecksGrid);

            }while(!action.equals("QUIT") || !this.endLocalGame());




        } else {

            Socket clientSocket;
            ObjectOutputStream sender;
            ObjectInputStream receiver;

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
                    System.out.println("Write COINS, STONES, SERVANTS or SHIELDS.");
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

                try {
                    UpdateClientPlayerBoardMessage playerBoardMessage = (UpdateClientPlayerBoardMessage) receiver.readObject();
                    this.playerboard = playerBoardMessage.getPlayerboard();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error in receiving playerboard");
                    return;
                }

                //Sends first starting excess leader card to discard
                System.out.println("Which starting leader card do you want to discard?");
                System.out.println();
                for (int i = 0; i < startingMessage.getLeaderCards().length; i++) {
                    System.out.println("Write " + i + " for this: ");
                    startingMessage.getLeaderCards()[i].printLeaderCard();
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
                        startingMessage.getLeaderCards()[i].printLeaderCard();
                    } else if (i>card) {
                        int k=i-1;
                        System.out.println("Write " + k + " for this: ");
                        startingMessage.getLeaderCards()[i].printLeaderCard();
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

                sender.reset();
                firstDiscardLeaderMessage = new DiscardLeaderMessage(this.playerNumber, card);
                sender.writeObject(firstDiscardLeaderMessage);

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Initial resources setting failed");
            }

            try {
                UpdateClientLeaderCardsMessage leaderCardsMessage = (UpdateClientLeaderCardsMessage) receiver.readObject();
                this.leaderCards = leaderCardsMessage.getLeaderCards();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error in receiving leader cards");
                return;
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

    public void setPlayerboard(Playerboard playerboard) {
        this.playerboard = playerboard;
    }

    public Playerboard getPlayerboard() {
        return this.playerboard;
    }

    public void setLeaderCards(LeaderCard[] leaderCards) {
        this.leaderCards = leaderCards;
    }

    public LeaderCard[] getLeaderCards() {
        return this.leaderCards;
    }

    public boolean endLocalGame(){
        return false;
    }
}

