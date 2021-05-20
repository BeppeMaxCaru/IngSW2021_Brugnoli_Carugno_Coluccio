import Message.InputResourceMessage;
import Message.DiscardLeaderMessage;
import Message.OutputChoiceResourceMessage;
import Message.PlayLeaderMessage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class ClientMain {

    //Eventuale lista comandi da stampare per aiutare ma non guidare
    private List<String> commands = Arrays.asList("Play leader card",
            "Discard leader card");

    public void main(String[] args) throws IOException {

        //Passarli come paramteri del main
        String hostName = "127.0.0.1";
        int portNumber = 1234;
        String gameMode;
        String nickName;
        Scanner input = new Scanner(System.in);

        // Game mode
        System.out.println("Welcome to Masters of Renaissance!");
        System.out.println("Write 0 for single-player or 1 for multiplayer: ");
        gameMode = input.nextLine();
        while (!gameMode.equals("0") && !gameMode.equals("1")) {
            System.out.println("Number not valid!");
            System.out.println("Write 0 for single-player or 1 for multiplayer: ");
            gameMode = input.nextLine();
        }

        if (gameMode.equals("0")) {
/*
            Player[] players = new Player[2];
            System.out.println("Write your nickname: ");
            PrintWriter output = new PrintWriter(System.out,true);
            nickName = input.nextLine();

            players[0] = new Player(nickName);
            players[0].setPlayerNumber(0);
            players[1] = new Player("Lorenzo il Magnifico");
            players[1].setPlayerNumber(1);

            ActionCountersDeck localActionCountersDeck = new ActionCountersDeck();
            DevelopmentCardsDecksGrid localDevelopmentCardsDeckGrid = new DevelopmentCardsDecksGrid();
            Market localMarket = new Market();
            LeaderCardDeck localLeaderCardDeck = new LeaderCardDeck();

            for(int index = 0; index < players[0].getPlayerLeaderCards().length; index++)
                players[0].setPlayerLeaderCard(index,localLeaderCardDeck.drawOneLeaderCard());
            for(int ind = 0; ind < 2; ind++)
                players[0].discardLeaderCard(input, output);

            boolean endGame = false;

            while (!endGame && players[0].getPlayerBoard().getFaithPath().getCrossPosition() < 24 && players[0].getPlayerBoard().getDevelopmentCardsBought() < 7 && players[1].getPlayerBoard().getFaithPath().getCrossPosition() < 24) {
                if(players[0].getPlayerLeaderCards()[0] != null) {
                    if(players[0].getPlayerLeaderCards()[1] == null && !players[0].getPlayerLeaderCards()[0].isPlayed())
                        players[0].getLeaderAction(input, output);
                    else if(players[0].getPlayerLeaderCards()[1] != null && (!players[0].getPlayerLeaderCards()[0].isPlayed() || !players[0].getPlayerLeaderCards()[1].isPlayed()))
                        players[0].getLeaderAction(input, output);
                    else System.out.println("You have activated all your Leader cards. You can't do a Leader Action.");
                }
                    else System.out.println("You have discarded all your Leader cards. You can't do a Leader Action.");

                players[0].printAll(output);

                System.out.println("LORENZO BLACK CROSS POSITION: "+players[1].getPlayerBoard().getFaithPath().getCrossPosition());
                System.out.println();

                System.out.println("MARKET GRID:");
                localMarket.printMarket(output);
                System.out.println("DEVELOPMENT CARDS GRID:");
                localDevelopmentCardsDeckGrid.printGrid(output);

                boolean correctAction = true;
                do {
                    switch (players[0].getAction(input, output)) {
                        case "0":
                            correctAction = players[0].pickLineFromMarket(localMarket, players, input, output);
                            break;
                        case "1":
                            correctAction = players[0].buyDevelopmentCard(localDevelopmentCardsDeckGrid, input, output);
                            break;
                        case "2":
                            correctAction = players[0].activateProduction(input, output);
                            break;
                    }
                }while (!correctAction);

                if(players[0].getPlayerLeaderCards()[0]!=null)
                    if(players[0].getPlayerLeaderCards()[1]==null && !players[0].getPlayerLeaderCards()[0].isPlayed())
                        players[0].getLeaderAction(input, output);
                    else if(players[0].getPlayerLeaderCards()[1]!=null &&
                            (!players[0].getPlayerLeaderCards()[0].isPlayed() || !players[0].getPlayerLeaderCards()[1].isPlayed()))
                        players[0].getLeaderAction(input, output);

                localActionCountersDeck.drawCounter().activate(localActionCountersDeck,players[1].getPlayerBoard(),localDevelopmentCardsDeckGrid, output);
                System.out.println("New turn!!");

                for (int i=0; i<4; i++)
                    if(localDevelopmentCardsDeckGrid.getDevelopmentCardsDecks()[0][i][0]==null)
                    {
                        endGame=true;
                        break;
                    }

                System.out.println();
            }

            System.out.println("Game over.");

            if(endGame)
            {
                System.out.println("Lorenzo il Magnifico wins!!");
                System.out.println("There aren't any available DevelopmentCards of one type.");
            }

            if(players[1].getPlayerBoard().getFaithPath().getCrossPosition()==25)
            {
                System.out.println("Lorenzo il Magnifico wins!!");
                System.out.println("Lorenzo reaches the end of FaithPath.");
            }

            if(players[0].getPlayerBoard().getFaithPath().getCrossPosition()==25){
                System.out.println(players[0].getNickname()+" wins!!");
                System.out.println("You reach the end of FaithPath.");
                System.out.println("You have obtained "+players[0].sumAllVictoryPoints()+" victory points!!");
            }

 */

        }
        else {
            //Trial Beppe
            //Ask nickname before gameMode branch
            //Call player constructor directly on the server?
            //Player player = new Player();
            try {

                Socket clientSocket = new Socket("127.0.0.1", 1234);

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader((new InputStreamReader(System.in)));

                int playerNumber;

                //Forse non serve più
                // :(
                /*
                Thread serverReceiver = new Thread(() -> {
                    try {
                        while (true) {

                            String serverMessage = in.readLine();

                            if (serverMessage == null)
                                break;

                            System.out.println("Received: " + serverMessage);
                        }
                    } catch (SocketTimeoutException e) {
                        System.err.println("You've been expelled for inactivity");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                serverReceiver.start();

                 */

                //Welcome
                String message = in.readLine();
                System.out.println(message);
                //Looking for a match
                message = in.readLine();
                System.out.println(message);

                /*
                nickName = stdIn.readLine();
                out.println(nickName);
                 */

                //Match has started
                message = in.readLine();
                System.out.println(message);
                System.out.println(message.charAt(message.length() - 1));
                if (!String.valueOf(message.charAt(message.length() - 1)).equals("0")) {
                    message = in.readLine();
                    System.out.println(message);
                }
                playerNumber = Integer.parseInt(String.valueOf(message.charAt(message.length() - 1)));

                String numChosenResources = in.readLine();
                System.out.println(numChosenResources);
                String chosenRes;
                int t = Integer.parseInt(numChosenResources);

                if (t != 0)
                    while (t > 0) {
                        System.out.println("Choose your initial resources: do you want COINS, STONES, SERVANTS or SHIELDS?");
                        chosenRes = in.readLine().toUpperCase();
                        while (!chosenRes.equals("COINS") && !chosenRes.equals("SHIELDS") && !chosenRes.equals("SERVANTS") && !chosenRes.equals("STONES")) {
                            System.out.println(chosenRes);
                            System.out.println("Not existing resource");
                            chosenRes = in.readLine().toUpperCase();
                        }
                        out.println(chosenRes);
                        t--;
                    }

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
                    action = stdIn.readLine();
                    //Puts in upper case the input
                    action = action.toUpperCase();

                    //Posso inviare il nickname del player qunado inserisce un comando per vedere se è il suo turno
                    //altrimenti lo ignoro

                    //IMPORTANT!!!
                    //out.println sends a null string that signals stdin error to the controller to
                    //reset its state at before the action
                    out.flush();
                    //out.println();
                    switch (action) {
                        case "PLAY LEADER CARD": {
                            String parameter;
                            System.out.println("Which card do you want to play?");
                            System.out.println("Write 0 or 1.");
                            parameter = stdIn.readLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                //Posso mandare qualunque numero e la validità la verifica il controller
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();

                                PlayLeaderMessage playLeaderMessage = new PlayLeaderMessage(playerNumber, cardPosition);
                                FileOutputStream output = new FileOutputStream("ClientMessage");
                                ObjectOutputStream stream = new ObjectOutputStream(output);
                                stream.writeObject(action.toUpperCase());
                                stream.writeObject(playLeaderMessage);
                                stream.close();

                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                break;
                            }
                        }
                        case "DISCARD LEADER CARD": {
                            //Same for play leader card
                            System.out.println("Which card do you want to discard?");
                            System.out.println("Write 0 or 1.");
                            String parameter = stdIn.readLine();
                            try {
                                //Checks if the leader card position exists
                                int cardPosition = Integer.parseInt(parameter);
                                if (cardPosition != 0 && cardPosition != 1) throw new Exception();

                                DiscardLeaderMessage discardLeaderMessage = new DiscardLeaderMessage(playerNumber, cardPosition);
                                FileOutputStream output = new FileOutputStream("ClientMessage");
                                ObjectOutputStream stream = new ObjectOutputStream(output);
                                stream.writeObject(action.toUpperCase());
                                stream.writeObject(discardLeaderMessage);
                                stream.close();

                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                break;
                            }
                        }
                        case "PICK RESOURCES FROM MARKET": {

                            if (mainAction == 1) {
                                System.err.println("Action already done");
                                break;
                            }

                            //Sends action
                            out.println(action);
                            //Receives OK
                            //System.out.println(in.readLine());

                            //Receives column or row
                            String parameter;
                            System.out.println("Do you want to pick row resources or column resources?");
                            System.out.println("Write 'ROW' or 'COLUMN'.");
                            parameter = stdIn.readLine();
                            parameter = parameter.toUpperCase();
                            if (parameter.equals("ROW")
                                || parameter.equals("COLUMN")) {
                                out.println(parameter.charAt(0));
                                //System.out.println(in.readLine());
                            } else {
                                //Resets action
                                System.err.println("Not valid parameter");
                                out.println();
                                break;
                            }

                            //Receives index
                            if(parameter.equals("ROW"))
                            {
                                System.out.println("Which row do you want to pick?");
                                System.out.println("Write a number between 0 and 2.");
                            } else {
                                System.out.println("Which column do you want to pick?");
                                System.out.println("Write a number between 0 and 3.");
                            }
                            String par = stdIn.readLine();
                            try {
                                //Checks if the leader card position exists
                                int index = Integer.parseInt(par);
                                if (parameter.equals("ROW") && (index < 0 || index > 2)) throw new Exception();
                                if (parameter.equals("COLUMN") && (index < 0 || index > 3)) throw new Exception();
                                out.println(index);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                out.println();
                                break;
                            }

                            //Receives deposit
                            System.out.println("If you activated your extra warehouse space, where do you want to store your resources?");
                            if(parameter.equals("ROW"))
                                System.out.println("Write w for warehouse, l for leader card, for each of 4 resources you picked");
                            else
                                System.out.println("Write w for warehouse, l for leader card, for each of 3 resources you picked");
                            String wlChoice = stdIn.readLine();
                            try {
                                //Checks if player has written only 'w' and 'l' chars
                                if (wlChoice.length()==0) throw new Exception();

                                for(int k=0; k<wlChoice.length(); k++)
                                    if(!String.valueOf(wlChoice.charAt(k)).equalsIgnoreCase("w") || !String.valueOf(wlChoice.charAt(k)).equalsIgnoreCase("l")) throw new Exception();

                                out.println(wlChoice);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                out.println();
                                break;
                            }

                            //Receives position of leader cards to activate to receive a resource from a white marble
                            System.out.println("If you activated both your white marble resources leader card, which one do you want to activate for each white marble you picked?");
                            System.out.println("if you activated only one white marble leader card, do you want to activate it?");
                            System.out.println("Write 0 for activate your fist leader card, 1 for activate your second leader card, for each white marble you picked");
                            System.out.println("Write X if you don't want to activate any leader card effect");

                            String chosenMarble = stdIn.readLine();
                            try {
                                //Checks if player has written only '0', '1' or 'x' chars

                                if(chosenMarble.length()!=0)
                                    for(int k=0; k<wlChoice.length(); k++)
                                        if(!String.valueOf(chosenMarble.charAt(k)).equalsIgnoreCase("0")
                                                || !String.valueOf(chosenMarble.charAt(k)).equalsIgnoreCase("1")
                                                || !String.valueOf(chosenMarble.charAt(k)).equalsIgnoreCase("x")) throw new Exception();

                                out.println(chosenMarble);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                //Send null value to reset the controller to
                                //receive again a new action
                                out.println();
                                break;
                            }

                            //If server responds OK, action is correct
                            String correctAction = in.readLine();
                            if(correctAction.equals("OK"))
                                mainAction++;
                        }

                        case "BUY DEVELOPMENT CARD": {

                            if (mainAction == 1) {
                                System.err.println("Action already done");
                                break;
                            }

                            out.println(action);

                            System.out.println("Which card do you want to buy?");
                            System.out.println("Write the correct colour: GREEN, YELLOW, BLUE or PURPLE, if existing in the grid");
                            String parameter;
                            parameter = stdIn.readLine();
                            parameter = parameter.toUpperCase();
                            if (parameter.equalsIgnoreCase("green")
                                    || parameter.equalsIgnoreCase("yellow")
                                    || parameter.equalsIgnoreCase("blue")
                                    || parameter.equalsIgnoreCase("purple")) {
                                out.println(parameter);
                                //System.out.println(in.readLine());
                            } else {
                                //Resets controller
                                System.err.println("Not valid parameter");
                                out.println();
                                break;
                            }

                            System.out.println("Which level do you want to buy?");
                            System.out.println("Write the correct number between 1 and 3, if existing in the grid");

                            parameter = stdIn.readLine();
                            try {
                                int level = Integer.parseInt(parameter);
                                if (level<1 || level>3) throw new Exception();
                                out.println(level);
                            } catch (Exception e) {
                                System.err.println("Not valid parameter");
                                out.println();
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

                            for(int k=0; k<4; k++)
                            {
                                quantity[k]=0;
                                shelf[k]=null;
                            }

                            //Which resource do you want to take
                            parameter = "";
                            while (!parameter.equalsIgnoreCase("STOP")) {

                                System.out.println("Which resource do you want to pick to pay the development card?");
                                parameter = stdIn.readLine().toUpperCase();
                                if (parameter.equals("COINS") || parameter.equals("STONES") || parameter.equals("SERVANTS") || parameter.equals("SHIELDS")) {
                                    int index = resources.get(parameter);

                                    //Receives now quantity
                                    System.out.println("How much "+parameter+" do you want to pick?");
                                    System.out.println("Write the correct value.");
                                    parameter = stdIn.readLine();
                                    try {
                                        int q = Integer.parseInt(parameter);
                                        if (q<0) throw new Exception();
                                        if (q>7) throw new Exception();
                                        quantity[index]=q;
                                    } catch (Exception e) {
                                        System.err.println("Not valid parameter");
                                        out.println();
                                        break;
                                    }

                                    for(int z=0; z<quantity[index]; z++)
                                    {
                                        //Keeps asking a place to take from resources
                                        System.out.println("From which store do you want to pick this resource?");
                                        System.out.println("Write WAREHOUSE, CHEST, or LEADER CARD if you activate your extra warehouse space leader card.");
                                        parameter = stdIn.readLine().toUpperCase();
                                        if (parameter.equals("CHEST") || parameter.equals("WAREHOUSE") || parameter.equals("LEADER CARD")) {
                                            shelf[index]=shelf[index] + parameter.charAt(0);
                                        } else {
                                            System.err.println("Not valid parameter");
                                            out.println();
                                            break;
                                        }
                                    }

                                } else {
                                    System.err.println("Not existing resource");
                                    out.println();
                                    break;
                                }
                            }

                            for(int z=0; z<4; z++)
                            {
                                out.println(quantity[z]);
                                out.println(shelf[z]);
                            }

                            //Position where to place the card on the playerboard
                            System.out.println("In which position of your development card grid do you want to place the bought card?");
                            System.out.println("You can put a level 1 card in an empty position or a level 2/3 card on a level 1/2 card.");
                            System.out.println("Write a correct position between 0 and 2.");
                            parameter = stdIn.readLine();
                            try{

                                int pos = Integer.parseInt(parameter);
                                if(pos < 0 || pos > 2) throw new Exception();
                                out.println(pos);

                            } catch (Exception e) {
                                System.err.println("Not valid command");
                                out.println();
                                break;
                            }

                            //If server responds OK, action is correct
                            String correctAction = in.readLine();
                            if(correctAction.equals("OK"))
                                mainAction++;
                        }
                        case "ACTIVATE PRODUCTION POWER": {

                            if (mainAction == 1) {
                                System.err.println("Action already done");
                                break;
                            }

                            /* PlayLeaderMessage playLeaderMessage = new PlayLeaderMessage(playerNumber, cardPosition);
                            FileOutputStream output = new FileOutputStream("ClientMessage");
                            ObjectOutputStream stream = new ObjectOutputStream(output);
                            stream.writeObject(action.toUpperCase());
                            stream.writeObject(playLeaderMessage);
                            stream.close(); */

                            int[] activation = {0, 0, 0, 0, 0, 0};
                            String[] commandsList = new String[6];
                            String[] whichInput = new String[6];
                            String[] whichOutput = new String[3];
                            String unknownCommand;
                            String command = null;
                            int j, i=0;
                            String quant;
                            boolean anotherCommand = false;

                            //Correspondence between resources and index
                            Map<Integer, String> resources = new HashMap<>();
                            resources.put(0, "COINS");
                            resources.put(1, "SERVANTS");
                            resources.put(2, "SHIELDS");
                            resources.put(3, "STONES");



                            j = 0;
                            for(int index = 0; index < 6; index++) {

                                System.out.println("Which production power do you want to activate?");
                                if(activation[0]==0)
                                    System.out.println("Write p0 if you want to activate the first production of your grid, if it's available");
                                if(activation[1]==0)
                                    System.out.println("Write p1 if you want to activate the second production of your grid, if it's available");
                                if(activation[2]==0)
                                    System.out.println("Write p2 if you want to activate the third production of your grid, if it's available");
                                if(activation[3]==0)
                                    System.out.println("Write b if you want to activate the basic production power");
                                if(activation[4]==0)
                                    System.out.println("Write e0 if you want to activate the first extra production power, if it's available");
                                if(activation[5]==0)
                                    System.out.println("Write e1 if you want to activate the second extra production power, if it's available");
                                System.out.println("Write STOP if you don't want to activate production powers");
                                if(!anotherCommand) command = stdIn.readLine(); // Comandi: p0, p1, p2, b, e0, e1, STOP
                                anotherCommand = false;

                                if (!command.equalsIgnoreCase("STOP")) {
                                    commandsList[index] = command; // lista di comandi, nel check si controlla la loro correttezza
                                    if (command.equalsIgnoreCase("p0") || command.equalsIgnoreCase("p1") || command.equalsIgnoreCase("p2")) {
                                        if (command.equalsIgnoreCase("p0") && activation[0]==0) {
                                            i = 0;
                                            activation[0] = 1;
                                        }
                                        if (command.equalsIgnoreCase("p1") && activation[1]==0) {
                                            i = 1;
                                            activation[1] = 1;
                                        }
                                        if (command.equalsIgnoreCase("p2") && activation[2]==0) {
                                            i = 2;
                                            activation[2] = 1;
                                        }
                                        for (int k = 0; k < 2; k++) {
                                            System.out.println("Which input resource do you want to spend?");
                                            for(int z = 0; z < 4; z++)
                                                System.out.println("Write "+ z +" for "+ resources.get(z));
                                            unknownCommand = stdIn.readLine(); // Comandi risorsa in input: 0, 1, 2, 3
                                            if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                whichInput[i] = whichInput[i] + unknownCommand;
                                                System.out.println("How much of them do you want to pick?");
                                                quant = stdIn.readLine(); // Comandi quantità: 1, 2
                                                if(quant.equals("1") || quant.equals("2"))
                                                {
                                                    if(whichInput[i].length()>3)
                                                    {
                                                        if(String.valueOf(whichInput[i].charAt(1)).equals("2"))
                                                        {
                                                            System.out.println("Not valid command.");
                                                            break;
                                                        }
                                                        else if (String.valueOf(whichInput[i].charAt(1)).equals("1") && quant.equals("2"))
                                                        {
                                                            System.out.println("Not valid command.");
                                                            break;
                                                        }
                                                    }
                                                    whichInput[i] = whichInput[i] + quant;
                                                }

                                                if(quant.equals("1"))
                                                    System.out.println("From which store do you want to pick this resource?");
                                                else
                                                    System.out.println("From which store do you want to pick these resources?");
                                                System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                System.out.println("Write 'c' if you want to pick resources from chest");
                                                System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                whichInput[i] = whichInput[i] + stdIn.readLine().toUpperCase(); // Comandi from where: c, w, l
                                                if(quant.equals("2")) k++;
                                            }
                                            else {
                                                command = unknownCommand;
                                                if (command.equalsIgnoreCase("p0") || command.equalsIgnoreCase("p1") || command.equalsIgnoreCase("p2")) anotherCommand = true;
                                                k = 1;
                                            }
                                        }
                                    }

                                    if (!anotherCommand) {
                                        // Se attiva il potere di produzione base
                                        if (command.equalsIgnoreCase("b") && activation[3]==0) {

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
                                                for(int z = 0; z < 4; z++)
                                                    System.out.println("Write "+ z +" for "+ resources.get(z));
                                                unknownCommand = stdIn.readLine(); // Comandi risorsa in input: 0, 1, 2, 3
                                                if (unknownCommand.equalsIgnoreCase("0") || unknownCommand.equalsIgnoreCase("1") || unknownCommand.equalsIgnoreCase("2") || unknownCommand.equalsIgnoreCase("3")) {
                                                    whichInput[3] = whichInput[3] + unknownCommand;
                                                    System.out.println("How much of them do you want to pick?");
                                                    quant = stdIn.readLine(); // Comandi quantità: 1, 2
                                                    if(quant.equals("1") || quant.equals("2"))
                                                    {
                                                        if(whichInput[3].length()>3)
                                                        {
                                                            if(String.valueOf(whichInput[3].charAt(1)).equals("2"))
                                                            {
                                                                System.out.println("Not valid command.");
                                                                break;
                                                            }
                                                            else if (String.valueOf(whichInput[3].charAt(1)).equals("1") && quant.equals("2"))
                                                            {
                                                                System.out.println("Not valid command.");
                                                                break;
                                                            }
                                                        }
                                                        whichInput[3] = whichInput[3] + quant;
                                                    }

                                                    if(quant.equals("1"))
                                                        System.out.println("From which store do you want to pick this resource?");
                                                    else
                                                        System.out.println("From which store do you want to pick these resources?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");
                                                    whichInput[3] = whichInput[3] + stdIn.readLine().toUpperCase(); // Comandi from where: c, w, l
                                                    if(quant.equals("2")) k++;
                                                }
                                                else {
                                                    command = unknownCommand;
                                                    if (command.equalsIgnoreCase("p0") || command.equalsIgnoreCase("p1") || command.equalsIgnoreCase("p2")) anotherCommand = true;
                                                    k = 1;
                                                }
                                            }
                                            activation[3] = 1;
                                        }
                                        // Risorse a scelta
                                        if (command.equalsIgnoreCase("b") || command.equalsIgnoreCase("e0") || command.equalsIgnoreCase("e1")) {

                                            if (command.equals("e0") && activation[4]==0) {

                                                System.out.println("Which input resource do you want to spend?");
                                                for(int z=0; z<4; z++)
                                                    System.out.println("Write "+z+" for "+resources.get(z));

                                                unknownCommand = stdIn.readLine(); // Comandi risorsa in input: 0, 1, 2, 3

                                                if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                    whichInput[4] = whichInput[4] + unknownCommand;
                                                    whichInput[4] = whichInput[4] + "1";

                                                    System.out.println("From which store do you want to pick this resource?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                    String store = stdIn.readLine().toUpperCase(); // Comandi from where: c, w, l
                                                    whichInput[4] = whichInput[4] + store; // Comandi from where: c, w, l
                                                    activation[4] = 1;
                                                }
                                            }
                                            if (command.equals("e1") && activation[5] == 0) {

                                                System.out.println("Which input resource do you want to spend?");
                                                for(int z = 0; z < 4; z++)
                                                    System.out.println("Write "+ z +" for "+ resources.get(z));

                                                unknownCommand = stdIn.readLine(); // Comandi risorsa in input: 0, 1, 2, 3

                                                if (unknownCommand.equals("0") || unknownCommand.equals("1") || unknownCommand.equals("2") || unknownCommand.equals("3")) {
                                                    whichInput[5] = whichInput[5] + unknownCommand;
                                                    whichInput[5] = whichInput[5] + "1";

                                                    System.out.println("From which store do you want to pick this resource?");
                                                    System.out.println("Write 'w' if you want to pick resources from warehouse");
                                                    System.out.println("Write 'c' if you want to pick resources from chest");
                                                    System.out.println("Write 'l' if you want to pick resources from your extra space leader card, if it's available");

                                                    String store = stdIn.readLine().toUpperCase(); // Comandi from where: c, w, l
                                                    whichInput[5] = whichInput[5] + store; // Comandi from where: c, w, l
                                                    activation[5] = 1;
                                                }
                                            }

                                            System.out.println("Which output resource do you want to pick?");
                                            for(int z = 0; z < 4; z++)
                                                System.out.println("Write "+ z +" for "+resources.get(z));
                                            System.out.println("Write 4 for REDCROSS");

                                            String com = stdIn.readLine();

                                            if (com.equals("0") || com.equals("1") || com.equals("2") || com.equals("3") || com.equals("4"))
                                            {
                                                if(command.equalsIgnoreCase("b"))
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
                            if(checkActivateProduction(commandsList, activation, whichInput, whichOutput))
                            {
                                for(int k = 0; k < 6; k++)
                                {
                                    if(activation[k] == 0)
                                        out.println(0);
                                    else
                                    {
                                       for(i = 0; i < whichInput[k].length() / 3; i++) {
                                           InputResourceMessage inputResourceMessage = new InputResourceMessage(playerNumber, Character.getNumericValue(whichInput[k].charAt(0)), Character.getNumericValue(whichInput[k].charAt(1)), whichInput[k].charAt(2));
                                           FileOutputStream output = new FileOutputStream("ClientMessage");
                                           ObjectOutputStream stream = new ObjectOutputStream(output);
                                           stream.writeObject(inputResourceMessage);
                                           stream.close();
                                       }

                                       if(k == 3 || k == 4 || k == 5) {
                                           OutputChoiceResourceMessage outputChoiceResourceMessage = new OutputChoiceResourceMessage(playerNumber, Integer.parseInt(whichOutput[k]));
                                           FileOutputStream output = new FileOutputStream("ClientMessage");
                                           ObjectOutputStream stream = new ObjectOutputStream(output);
                                           stream.writeObject(outputChoiceResourceMessage);
                                           stream.close();
                                       }
                                    }
                                }
                            }
                            //If server responds OK, action is correct
                            String correctAction = in.readLine();
                            if(correctAction.equals("OK"))
                                mainAction++;
                        }
                        default: {
                            out.println("Not valid action!");
                            break;
                        }
                    }
                    //Player inserisce quit
                }//while (!action.equalsIgnoreCase("END TURN") && (corrAction < 1));

                //this.gameModel.getPlayers()[i].getOutPrintWriter().println("Your turn has ended. Wait for other players...");
                //this.gameModel.getPlayers()[i].getOutPrintWriter().println();


                /*while (true) {

                        String clientInput = stdIn.readLine();
                        //Trial
                        if (clientInput.equals("QUIT")) {
                            System.out.println("You left the game");
                            System.out.println("Bye bye");
                            break;
                        }
                        out.println(clientInput);
                }*/

                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //Usare shutdown output per bloccare client

            } catch (UnknownHostException e) {
                System.err.println("No info about host: " + hostName);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for connection to hostname: " + hostName);
                System.exit(1);
            }
        }

    }

    public static boolean checkActivateProduction(String[] commandList, int[] activation, String[] whichInput, String[] whichOutput) {

        PrintWriter out = new PrintWriter(System.out);

        int j;

        for(int i = 0; commandList[i] != null; i++) {
            if(!commandList[i].equals("p0") && !commandList[i].equals("p1") && !commandList[i].equals("p2") &&
                    !commandList[i].equals("b") && !commandList[i].equals("e0") && !commandList[i].equals("e1")) {
                out.println("Not valid command.");
                return false;
            }
        }


        for(int index = 0; index < 6; index++) {
            if (activation[index] == 1) {
                j = 0;
                for (int i = 0; i < whichInput[index].length() / 3; i++) {
                    if (whichInput[index].charAt(j) != '0' && whichInput[index].charAt(j) != '1' && whichInput[index].charAt(j) != '2' && whichInput[index].charAt(j) != '3') {
                        out.println("Not valid command.");
                        return false;
                    }
                    if (whichInput[index].charAt(j + 1) != '1' && whichInput[index].charAt(j + 1) != '2') {
                        out.println("Not valid command.");
                        return false;
                    }
                    if (whichInput[index].charAt(j + 2) != 'c' && whichInput[index].charAt(j + 2) != 'w' && whichInput[index].charAt(j + 2) != 'e') {
                        out.println("Not valid command.");
                        return false;
                    }
                    j++;
                }
            }
        }

        for (String s : whichOutput) {
            if (s != null) {
                if (!s.equals("0") && !s.equals("1") && !s.equals("2") && !s.equals("3") && !s.equals("4")) {
                    out.println("Not valid command.");
                    return false;
                }
            }
        }

        return true;
    }
}
