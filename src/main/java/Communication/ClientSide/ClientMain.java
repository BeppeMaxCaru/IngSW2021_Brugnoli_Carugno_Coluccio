package Communication.ClientSide;

import Communication.ClientSide.RenderingView.CLI;
import Communication.ClientSide.RenderingView.RenderingView;
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

    private int playerNumber;

    //TEST MEX MARKET
    private Market market;
    private DevelopmentCardsDecksGrid developmentCardsDecksGrid;
    private Playerboard playerboard;
    private LeaderCard[] leaderCards = new LeaderCard[4];

    public ClientMain(String hostname, int port) {
        this.hostName = hostname;
        this.port = port;
    }

    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        RenderingView view = new CLI();

        ClientMain client = new ClientMain(hostname, port);
        client.Execute(view);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void Execute(RenderingView view) {

        int gameMode;

        this.nickname = view.getNickName();
        view.setClientStarted();

        gameMode = view.getGameMode();

        if (gameMode==0) {

            view.setGameStarted();

            Player[] localPlayers = new Player[2];
            localPlayers[0] = new Player(this.nickname, 0);
            localPlayers[1] = new Player("Lorenzo the Magnificent", 1);
            view.setLorenzoPlayerBoard(localPlayers[1].getPlayerBoard());

            this.market = new Market();
            view.setMarket(this.market);
            this.developmentCardsDecksGrid = new DevelopmentCardsDecksGrid();
            view.setDevCardsGrid(this.developmentCardsDecksGrid);
            LeaderCardDeck cardsDeck = new LeaderCardDeck();
            ActionCountersDeck actionCountersDeck = new ActionCountersDeck();
            view.setCountersDeck(actionCountersDeck);

            //Put 4 leader cards into first player space
            for(int index = 0; index < localPlayers[0].getPlayerLeaderCards().length; index++)
                localPlayers[0].setPlayerLeaderCard(index, cardsDeck.drawOneLeaderCard());
            view.setStartingLeaders(localPlayers[0].getPlayerLeaderCards());

            int[] discard = view.getDiscardedStartingLeaders();
            localPlayers[0].discardLeaderCard(discard[0]);
            localPlayers[0].discardLeaderCard(discard[1]);
            view.setPlayerLeaders(localPlayers[0].getPlayerLeaderCards());

            view.setBoard(localPlayers[0].getPlayerBoard());

            int mainAction = 0;
            String action;
            do{
                action = view.getActionChoice();

                switch (action) {
                    case "P":
                    case "PLAY LEADER CARD": {

                        int cardPosition;
                        try {
                            cardPosition = view.getPlayedLeader();
                        } catch (Exception e) {
                            view.error(e);
                            break;
                        }

                        try {
                            if(this.checkLocalLeaders(localPlayers[0], cardPosition))
                            {
                                localPlayers[0].playLeaderCard(cardPosition);
                                view.setPlayerLeaders(localPlayers[0].getPlayerLeaderCards());
                            }
                            else view.notValidAction();

                        } catch (Exception e) {
                            view.error(e);
                            break;
                        }

                        break;
                    }
                    case "D":
                    case "DISCARD LEADER CARD": {

                        int cardPosition;
                        try {
                            //Checks if the leader card position exists
                            cardPosition = view.getDiscardedLeader();

                        } catch (Exception e) {
                            view.error(e);
                            break;
                        }

                        try {
                            if(this.checkLocalLeaders(localPlayers[0], cardPosition))
                            {
                                localPlayers[0].discardLeaderCard(cardPosition);
                                view.setPlayerLeaders(localPlayers[0].getPlayerLeaderCards());
                            }
                            else
                                view.notValidAction();

                        } catch (Exception e) {
                            view.error(e);
                            break;
                        }
                        break;
                    }
                    case "M":
                    case "PICK RESOURCES FROM MARKET": {

                        if(mainAction==1) {
                            view.notValidAction();
                            break;
                        }

                        int[] coordinates = view.getMarketCoordinates();
                        String parameter;
                        int index;
                        if(coordinates[0] == 0) parameter = "ROW";
                        else parameter = "COLUMN";
                        index = coordinates[1];
                        String wlChoice = view.getResourcesDestination(parameter);
                        String chosenMarble = view.getWhiteMarbleChoice();

                        //If player picks row
                        if(parameter.equals("ROW"))
                        {
                            if(chosenMarble.length()<4) {
                                StringBuilder cBuilder = new StringBuilder(chosenMarble);
                                for(int k = cBuilder.length(); k<4; k++)
                                    cBuilder.append("X");
                                chosenMarble = cBuilder.toString();
                            }
                        }
                        else
                        {
                            if(chosenMarble.length()<3) {
                                StringBuilder cBuilder = new StringBuilder(chosenMarble);
                                for(int k = cBuilder.length(); k<3; k++)
                                    cBuilder.append("X");
                                chosenMarble = cBuilder.toString();
                            }
                        }

                        try {
                            if(this.checkLocalMarketAction(localPlayers[0].getPlayerBoard(), wlChoice, chosenMarble)) {
                                if (parameter.equals("ROW"))
                                    this.market.updateRow(index, localPlayers, 0, wlChoice, chosenMarble);
                                else this.market.updateColumn(index, localPlayers, 0, wlChoice, chosenMarble);
                                mainAction++;
                                view.setMarket(this.market);
                                view.setBoard(localPlayers[0].getPlayerBoard());
                            } else
                                view.notValidAction();
                        } catch (Exception e) {
                            view.notValidAction();
                            break;
                        }
                        break;

                    }

                    case "B":
                    case "BUY DEVELOPMENT CARD": {

                        if(mainAction==1) {
                            view.notValidAction();
                            break;
                        }

                        int[] coordinates = view.getDevelopmentCardsGridCoordinates();
                        int column = coordinates[0];
                        int level = 3 - coordinates[1];

                        //Check
                        int[] quantity = new int[4];
                        String[] shelf;

                        String[][] pickedResources = view.getPayedResources();
                        for(int k = 0; k < quantity.length; k++)
                            quantity[k] = Integer.parseInt(pickedResources[0][k]);
                        shelf = pickedResources[1];
                        int pos = view.getChosenPosition();


                        try {
                            if(this.checkLocalBuyCard(localPlayers[0], column, level, quantity, shelf ))
                                if(localPlayers[0].getPlayerBoard().isCardBelowCompatible(pos, this.developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0]))
                                {
                                    localPlayers[0].buyDevelopmentCard(this.developmentCardsDecksGrid, column, level, pos, shelf);
                                    mainAction++;
                                    view.setDevCardsGrid(this.developmentCardsDecksGrid);
                                    view.setBoard(localPlayers[0].getPlayerBoard());
                                } else view.notValidAction();
                            else view.notValidAction();
                        } catch (Exception e) {
                            view.error(e);
                            break;
                        }
                        break;
                    }

                    case "A":
                    case "ACTIVATE PRODUCTION POWER": {

                        if(mainAction==1) {
                            view.notValidAction();
                            break;
                        }

                        int[] activation = {0, 0, 0, 0, 0, 0};
                        String[] whichInput = new String[6];
                        String[] whichOutput = new String[3];

                        int stop;
                        int[] outputs = new int[3];
                        do{
                            stop = view.getActivationProd(activation);
                            if(stop<6){
                                activation[stop] = 1;
                                whichInput[stop] = view.getInputResourceProd();
                                if(stop>2){
                                    whichOutput[stop-3] = view.getOutputResourceProd();
                                }

                                for(int k = 0; k<outputs.length; k++)
                                    outputs[k] = Integer.parseInt(whichOutput[k]);

                            }
                        } while (stop != 6);


                        try {
                            if (this.checkLocalActivateProd(localPlayers[0], activation, whichInput, whichOutput)) {
                                localPlayers[0].activateProduction(activation, whichInput, outputs);
                                mainAction++;
                                view.setBoard(localPlayers[0].getPlayerBoard());
                            } else view.notValidAction();
                        } catch (Exception e) {
                            view.error(e);
                            break;
                        }
                        break;
                    }
                    case "END TURN":
                    {
                        if(mainAction==1)
                        {
                            view.endTurn();
                            view.setLorenzoPlayerBoard(localPlayers[1].getPlayerBoard());
                            view.setCountersDeck(actionCountersDeck);
                            view.drawActionCounter();
                            view.lorenzoFaithPoints();
                            mainAction = 0;
                        } else {
                            view.notValidAction();
                        }
                        break;
                    }
                    case "QUIT":
                    {
                        view.quit();
                        return;
                    }
                    default: {
                        view.notValidAction();
                        break;
                    }
                }
            }while(!this.endLocalGame(localPlayers));

            if(localPlayers[0].getPlayerBoard().getDevelopmentCardsBought()==7 || localPlayers[0].getPlayerBoard().getFaithPath().getCrossPosition()==24)
            {
                view.setLocalWinner(0);
                view.endLocalGame();
            } else {
                view.setLocalWinner(0);
                view.endLocalGame();
            }

        } else {

            Socket clientSocket;
            ObjectOutputStream sender;
            ObjectInputStream receiver;

            //Starts connection
            try {
                clientSocket = new Socket(this.hostName, this.port);
                sender = new ObjectOutputStream(clientSocket.getOutputStream());
                receiver = new ObjectInputStream(clientSocket.getInputStream());

                //Print welcome message
                view.setClientStarted();

            } catch (Exception e) {
                view.error(e);
                return;
            }

            //Per controllare inattività o disconnesione si può provare setSocketTimeout
            //sulla socket del playerThread e vedere se non arriva data dopo tot tempo

            //Send nickname message to server
            try {
                NicknameMessage nicknameMessage = new NicknameMessage(this.nickname);
                sender.writeObject(nicknameMessage);
            } catch (Exception e) {
                view.error(e);
                return;
            }

            try {
                UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) receiver.readObject();
                this.market = updateClientMarketMessage.getMarket();
                view.setMarket(this.market);
            } catch (Exception e) {
                view.error(e);
                return;
            }

            try {
                UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) receiver.readObject();
                this.developmentCardsDecksGrid = updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid();
                view.setDevCardsGrid(this.developmentCardsDecksGrid);
            } catch (Exception e) {
                view.error(e);
                return;
            }

            //Receives and sends starting resources message
            try {
                //Decapsulates first message
                ServerStartingMessage startingMessage = (ServerStartingMessage) receiver.readObject();
                this.playerNumber = startingMessage.getPlayerNumber();

                //Set view attribute playerNumber to be printed
                view.setPlayerNumber(this.playerNumber);
                //Print starting match message
                view.setGameStarted();

                //Receive from input starting resource(s)
                ArrayList<String> playerStartingResources = view.getStartingResource();

                //Send player number and starting resources
                StartingResourcesMessage resourcesMessage = new StartingResourcesMessage(this.playerNumber, playerStartingResources);
                sender.writeObject(resourcesMessage);

                try {
                    UpdateClientPlayerBoardMessage playerBoardMessage = (UpdateClientPlayerBoardMessage) receiver.readObject();
                    this.playerboard = playerBoardMessage.getPlayerboard();
                    view.setBoard(this.playerboard);
                } catch (Exception e) {
                    view.error(e);
                    return;
                }

                //Set view starting leader cards to be printed
                view.setStartingLeaders(startingMessage.getLeaderCards());
                //Receive from input 2 leader cards to be discarded
                int[] cards = view.getDiscardedStartingLeaders();

                //Sends first starting excess leader card to discard
                DiscardLeaderMessage firstDiscardLeaderMessage = new DiscardLeaderMessage(this.playerNumber, cards[0]);
                sender.writeObject(firstDiscardLeaderMessage);

                //Sends second starting excess leader card to discard
                sender.reset();
                firstDiscardLeaderMessage = new DiscardLeaderMessage(this.playerNumber, cards[1]);
                sender.writeObject(firstDiscardLeaderMessage);

            } catch (Exception e) {
                view.error(e);
            }

            try {
                UpdateClientLeaderCardsMessage leaderCardsMessage = (UpdateClientLeaderCardsMessage) receiver.readObject();
                this.leaderCards = leaderCardsMessage.getLeaderCards();
                view.setPlayerLeaders(this.leaderCards);
            } catch (Exception e) {
                view.error(e);
                return;
            }

            //Starts async phase
            new ServerReceiver(this, clientSocket, receiver, view).start();
            new ServerSender(this, clientSocket, sender, view).start();

        }
    }

    public String getNickname () {
        return this.nickname;
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

    public boolean endLocalGame(Player[] localPlayers){
        for(int k=0; k < 4; k++)
            if(this.developmentCardsDecksGrid.getDevelopmentCardsDecks()[0][k][0]==null)
                return true;

        if(localPlayers[0].getPlayerBoard().getFaithPath().getCrossPosition()==24)
            return true;

        if(localPlayers[1].getPlayerBoard().getFaithPath().getCrossPosition()==24)
            return true;

        return localPlayers[0].getPlayerBoard().getDevelopmentCardsBought() == 7;
    }

    public boolean checkLocalLeaders(Player player, int card) {
        //
        return player.getPlayerLeaderCards()[card] != null && !player.getPlayerLeaderCards()[card].isPlayed();
    }

    public boolean checkLocalMarketAction(Playerboard board, String wlChoice, String leader) {

        if(leader.contains("1"))
            if (board.getResourceMarbles()[1]==null) return false;

        if(leader.contains("0"))
            if (board.getResourceMarbles()[0]==null) return false;

        if(wlChoice.contains("L"))
            for(String keys : board.getWareHouse().getWarehouseResources().keySet())
                if (!keys.contains("extra")) return false;

        return true;
    }

    public boolean checkLocalBuyCard(Player player, int column, int level, int[] quantity, String[] wclChoice) {
        Map<String, Integer> paidResources = new HashMap<>();
        paidResources.put("COINS", quantity[0]);
        paidResources.put("SERVANTS", quantity[1]);
        paidResources.put("SHIELDS", quantity[2]);
        paidResources.put("STONES", quantity[3]);

        Map<Integer, String> resources = new HashMap<>();
        resources.put(0, "COINS");
        resources.put(1, "SERVANTS");
        resources.put(2, "SHIELDS");
        resources.put(3, "STONES");

        if(wclChoice[0].length() + wclChoice[1].length() + wclChoice[2].length() + wclChoice[3].length() == 0)
            return false;

        if (quantity[0] + quantity[1] + quantity[2] + quantity[3] ==0)
            return false;

        if (this.developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0] != null) {

            //Control on quantity and possibly discounts
            //If paidResources hashMap isn't equals to cardCost hashMap
            if (!paidResources.equals(this.developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost())) {
                //Check for discounts
                if (player.getPlayerBoard().getDevelopmentCardDiscount()[0] != null && player.getPlayerLeaderCards()[0].isPlayed()) {
                    paidResources.put(player.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(player.getPlayerBoard().getDevelopmentCardDiscount()[0]) + 1);
                    //Check if player has activated only first discount
                    if (!paidResources.equals(this.developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost())) {
                        if (player.getPlayerBoard().getDevelopmentCardDiscount()[1] != null && player.getPlayerLeaderCards()[1].isPlayed()) {
                            paidResources.put(player.getPlayerBoard().getDevelopmentCardDiscount()[1], paidResources.get(player.getPlayerBoard().getDevelopmentCardDiscount()[1]) + 1);
                            //Check if player has activated both first and second discounts
                            if (!paidResources.equals(this.developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost())) {
                                //Check if player has activated only second discount
                                paidResources.put(player.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(player.getPlayerBoard().getDevelopmentCardDiscount()[0]) - 1);
                                if (!paidResources.equals(this.developmentCardsDecksGrid.getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost())) {
                                    //If resourcePaid isn't equal to cardCost, player hasn't inserted correct resource for buy the card
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int k = 0; k < 4; k++) {
            int count = 0;
            for (int z = 0; z < wclChoice[k].length(); z++) {
                if (String.valueOf(wclChoice[k].charAt(z)).equalsIgnoreCase("w"))
                    count++;
            }
            if (player.getPlayerBoard().getWareHouse().getWarehouseResources().get(resources.get(k)) != count) {
                return false;
            }

            count = 0;
            for (int z = 0; z < wclChoice[k].length(); z++) {
                if (String.valueOf(wclChoice[k].charAt(z)).equalsIgnoreCase("c"))
                    count++;
            }
            if (player.getPlayerBoard().getChest().getChestResources().get(resources.get(k)) != count) {
                return false;
            }

            count = 0;
            for (int z = 0; z < wclChoice[k].length(); z++) {
                if (String.valueOf(wclChoice[k].charAt(z)).equalsIgnoreCase("l"))
                    count++;
            }

            if(count>0 && player.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + resources.get(k)) == null)
                return false;

            if(player.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + resources.get(k)) != null)
            {
                if (player.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + resources.get(k)) != count) {
                    return false;
                }
            }

        }
        return true;
    }

    public boolean checkLocalActivateProd(Player player, int[] activate, String[] inputs, String[] outputs) {

        Map<String, Integer> paidWarehouseResources = new HashMap<>();
        paidWarehouseResources.put("COINS", 0);
        paidWarehouseResources.put("SERVANTS", 0);
        paidWarehouseResources.put("SHIELDS", 0);
        paidWarehouseResources.put("STONES", 0);

        Map<String, Integer> paidChestResources = new HashMap<>();
        paidChestResources.put("COINS", 0);
        paidChestResources.put("SERVANTS", 0);
        paidChestResources.put("SHIELDS", 0);
        paidChestResources.put("STONES", 0);

        Map<Integer, String> resources = new HashMap<>();
        resources.put(0, "COINS");
        resources.put(1, "SERVANTS");
        resources.put(2, "SHIELDS");
        resources.put(3, "STONES");

        int j;


        int activated=0;
        for (int index = 0; index < 6; index++) {
            if (activate[index] == 1) {
                j = 0;
                for (int i = 0; i < inputs[index].length() / 3; i++) {
                    if (inputs[index].charAt(j) != '0' && inputs[index].charAt(j) != '1' && inputs[index].charAt(j) != '2' && inputs[index].charAt(j) != '3') {
                        return false;
                    }
                    if (inputs[index].charAt(j + 1) != '1' && inputs[index].charAt(j + 1) != '2') {
                        return false;
                    }
                    if (inputs[index].charAt(j + 2) != 'c' && inputs[index].charAt(j + 2) != 'w' && inputs[index].charAt(j + 2) != 'e') {
                        return false;
                    }
                    j++;
                }
                activated++;
            }
        }

        if(activated==0) return false;

        for (String s : outputs) {
            if (s != null) {
                if (!s.equals("0") && !s.equals("1") && !s.equals("2") && !s.equals("3") && !s.equals("4")) {
                    return false;
                }
            }
        }

        for(int k=0; k<activate.length; k++)
        {
            if(activate[k]==1) {

                j=2;
                if (k < 3) {

                    //Check if player has any cards into the indicated position
                    for (j = 2; j > 0; j--)
                        if (player.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k] != null)
                            break;

                    if (player.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k] == null) {
                        return false;
                    }

                    //Check how many resources player has to spend
                    int totalResources = 0;
                    for(String keys : player.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().keySet())
                        totalResources=totalResources+player.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().get(keys);
                    //Confront them with whichInput string: resourceCode - quantity - storage
                    //If player indicated less resources than that he had to pay, error
                    if(inputs.length<totalResources*3) return false;

                } else {
                    if(k != 3) {
                        //Check if player has any cards into the indicated position and it is activated
                        if (player.getPlayerBoard().getExtraProductionPowerInput()[k-4] == null || !player.getPlayerLeaderCards()[k-4].isPlayed()) {
                            return false;
                        }
                    }
                }

                //Save all resources player has to pay in temporary maps
                for(int z=0; z<inputs.length-2; z=z+3) {
                    switch (String.valueOf(inputs[z+2]).toUpperCase()) {
                        case "W": {
                            paidWarehouseResources.put(resources.get(Integer.parseInt(inputs[z])), Integer.parseInt(inputs[z+1]));
                            break;
                        }
                        case "C": {
                            paidChestResources.put(resources.get(Integer.parseInt(inputs[z])), Integer.parseInt(inputs[z+1]));
                            break;
                        }
                        case "L": {
                            if(player.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra"+resources.get(Integer.parseInt(inputs[z])))==null) {
                                return false;
                            }
                            else
                                paidWarehouseResources.put("extra"+resources.get(Integer.parseInt(inputs[z])), Integer.parseInt(inputs[z+1]));
                            break;
                        }
                    }
                }

                //Check if player has each correct resource in each correct storage

                for(String keys : paidWarehouseResources.keySet())
                    if(player.getPlayerBoard().getWareHouse().getWarehouseResources().get(keys)<paidWarehouseResources.get(keys)) {
                        return false;
                    }
                for(String keys : paidChestResources.keySet())
                    if(player.getPlayerBoard().getChest().getChestResources().get(keys)<paidChestResources.get(keys)) {
                        return false;
                    }

                //Check if player inserted all necessary resources to activate the production

                for (String res : paidChestResources.keySet())
                {
                    paidChestResources.put(res, paidChestResources.get(res) + paidWarehouseResources.get(res));
                    for(String extraRes : paidWarehouseResources.keySet())
                    {
                        if(extraRes.contains(res))
                            paidChestResources.put(res, paidChestResources.get(res) + paidWarehouseResources.get("extra"+res));
                    }
                }

                if(k<3)
                {
                    for(String res : player.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().keySet())
                    {
                        if (paidChestResources.get(res) < player.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().get(res)) {
                            return false;
                        }
                    }
                } else {
                    if (paidChestResources.get(player.getPlayerBoard().getExtraProductionPowerInput()[j-4]) < 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

