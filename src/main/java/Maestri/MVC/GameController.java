package Maestri.MVC;

import Communication.ServerSide.PlayerThread;
import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.MessageReceived.UpdateClientDevCardGridMessage;
import Message.MessageReceived.UpdateClientMarketMessage;
import Message.MessageReceived.UpdateClientPlayerBoardMessage;
import Message.MessageReceived.YourTurnMessage;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController{

    private final GameModel gameModel;
    private int currentPlayerNumber;

    private Set<PlayerThread> playerThreads = new HashSet<>();

    public GameController(List<PlayerThread> queueFIFO) {

        Player[] players = new Player[4];

        PlayerThread[] playersPlaying = new PlayerThread[4];

        ExecutorService playerThreadExecutor = Executors.newFixedThreadPool(4);
        Set<PlayerThread> playerThreads = new HashSet<>();

        List<Player> playersToPlay = new ArrayList<>();

        int numOfPlayers = 0;
        for (int i=0;i<4;i++) {
            try {
                //If there is a player adds it
                if (!queueFIFO.isEmpty()) {
                    playersPlaying[i] = queueFIFO.remove(0);
                    playersPlaying[i].setPlayerThreadNumber(i);
                    //playersPlaying[i].setNickName(playersPlaying[i].getNickName());
                    //Starts the thread
                    //playerThreadExecutor.execute(playersPlaying[i]);

                    playersPlaying[i].setGameController(this);

                    //Creates only the data of the player in the game model
                    players[i] = new Player(playersPlaying[i].getNickName(), i);
                    //System.out.println(playersPlaying[i].getNickName());
                    numOfPlayers++;

                }
            } catch (Exception e) {
                //If no clients waiting set other players null
                playersPlaying[i] = null;
            }
        }

        this.gameModel = new GameModel(players, numOfPlayers);

        for (int j=0;j<numOfPlayers;j++) {
            try {
                if(playersPlaying[j]!=null) {
                    playerThreadExecutor.execute(playersPlaying[j]);
                    this.playerThreads.add(playersPlaying[j]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Non c'è player");
            }
        }

    }

    public int getCurrentPlayerNumber() {
        return this.currentPlayerNumber;
    }

    public void nextCurrentPlayerNumber() {
        //Mettere controllo che ci sia più di un giocatore
        do{
            this.currentPlayerNumber++;
            if (this.currentPlayerNumber == 4) this.currentPlayerNumber = 0;
        }while(this.gameModel.getPlayers()[this.currentPlayerNumber]==null);
        System.out.println("Turn of player "+this.currentPlayerNumber);
        for(PlayerThread thread : this.playerThreads){
            if(thread.getPlayerThreadNumber()==this.currentPlayerNumber)
            {
                try{
                    thread.getSender().writeObject(new YourTurnMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Set<PlayerThread> getPlayerThreads() {
        return this.playerThreads;
    }

    public boolean checkPlayCards (Player currentPlayer, int c) {
        System.out.println("Check if you can play the card");
        //If not working add this in if:  && currentPlayer.getPlayerLeaderCards()[c].checkRequisites(currentPlayer.getPlayerBoard())
        if (currentPlayer.getPlayerLeaderCards()[c] != null && !currentPlayer.getPlayerLeaderCards()[c].isPlayed()) {

            //Before
            return currentPlayer.playLeaderCard(c);

            //After
            //return currentPlayer.playLeaderCard2(c);
        } else {
            System.out.println("Already played or discarded");
            return false;
        }

    }

    public boolean checkDiscardCards (Player currentPlayer, int c){

        if (currentPlayer.getPlayerLeaderCards()[c] != null && !currentPlayer.getPlayerLeaderCards()[c].isPlayed()) {
            return currentPlayer.discardLeaderCard(c);
        } else return false;

    }

    public boolean checkMarketAction (Player currentPlayer, String choice, int i, String wlChoice, String c) {

        if(c.contains("1"))
            if (currentPlayer.getPlayerBoard().getResourceMarbles()[1]==null) return false;

        if(c.contains("0"))
            if (currentPlayer.getPlayerBoard().getResourceMarbles()[0]==null) return false;

        if(wlChoice.contains("L"))
            for(String keys : currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().keySet())
                if (!keys.contains("extra")) return false;

        //If player picks row
        if(choice.equalsIgnoreCase("ROW"))
        {
            if(c.length()<4) {
                StringBuilder cBuilder = new StringBuilder(c);
                for(int k = cBuilder.length(); k<4; k++)
                    cBuilder.append("X");
                c = cBuilder.toString();
            }
            return this.gameModel.getMarket().updateRow(i, this.gameModel.getPlayers(), currentPlayer.getPlayerNumber(), wlChoice, c);
        }
        else
        {
            if(c.length()<3) {
                StringBuilder cBuilder = new StringBuilder(c);
                for(int k = cBuilder.length(); k<3; k++)
                    cBuilder.append("X");
                c = cBuilder.toString();
            }
            return this.gameModel.getMarket().updateColumn(i, this.gameModel.getPlayers(), currentPlayer.getPlayerNumber(), wlChoice, c);
        }
    }

    public boolean checkBuyDevCard(Player currentPlayer, int column, int l, int[] quantity, String[] wclChoice) {

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

        if (this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0] != null) {

            //Control on quantity and possibly discounts
            //If paidResources hashMap isn't equals to cardCost hashMap
            if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0].getDevelopmentCardCost())) {
                //Check for discounts
                if (currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0] != null && currentPlayer.getPlayerLeaderCards()[0].isPlayed()) {
                    paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0]) + 1);
                    //Check if player has activated only first discount
                    if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0].getDevelopmentCardCost())) {
                        if (currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1] != null && currentPlayer.getPlayerLeaderCards()[1].isPlayed()) {
                            paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1]) + 1);
                            //Check if player has activated both first and second discounts
                            if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0].getDevelopmentCardCost())) {
                                //Check if player has activated only second discount
                                paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0]) - 1);
                                if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0].getDevelopmentCardCost())) {
                                    //If resourcePaid isn't equal to cardCost, player hasn't inserted correct resource for buy the card
                                    //System.out.println("1");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        } else System.out.println("Card null");

        for (int k = 0; k < 4; k++) {
            int count = 0;
            for (int z = 0; z < wclChoice[k].length(); z++) {
                if (String.valueOf(wclChoice[k].charAt(z)).equals("W"))
                    count++;
            }
            if(count>0)
                if (currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get(resources.get(k)) != null) {
                    if (currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get(resources.get(k)) < count) {
                        //System.out.println("2");
                        return false;
                    }
                } else {
                    //System.out.println("3");
                    return false;
                }

            count = 0;
            for (int z = 0; z < wclChoice[k].length(); z++) {
                if (String.valueOf(wclChoice[k].charAt(z)).equals("C"))
                    count++;
            }
            if(count>0)
                if (currentPlayer.getPlayerBoard().getChest().getChestResources().get(resources.get(k)) != null) {
                    if (currentPlayer.getPlayerBoard().getChest().getChestResources().get(resources.get(k)) < count) {
                        //System.out.println("4");
                        return false;
                    }
                } else {
                    //System.out.println("5");
                    return false;
                }

            count = 0;
            for (int z = 0; z < wclChoice[k].length(); z++) {
                if (String.valueOf(wclChoice[k].charAt(z)).equals("L"))
                    count++;
            }
            if(count>0)
                if (currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + resources.get(k)) != null) {
                    if (currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + resources.get(k)) < count) {
                        //System.out.println("6");
                        return false;
                    }
                } else {
                    //System.out.println("7");
                    return false;
                }
        }
        return true;
    }

    public boolean checkActivateProduction(Player currentPlayer, int[] activation, String[] whichInput, int[] whichOutput) {

        int act = 0;
        for (int i : activation) act = act + i;
        System.out.println("Powers activated: "+act);
        if(act==0)
            return false;


        for(int k=0; k<activation.length; k++)
        {
            if(activation[k]==1) {
                String in = whichInput[k];

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

                int j=2;
                if (k < 3) {

                    //Check if player has any cards into the indicated position
                    for (j = 2; j > 0; j--)
                        if (currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k] != null)
                            break;

                    if (currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k] == null) {
                        return false;
                    }

                    //Check how many resources player has to spend
                    int totalResources = 0;
                    for(String keys : currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardInput().keySet())
                    {
                        System.out.println(keys);
                        System.out.println(currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardInput().get(keys));
                        totalResources = totalResources + currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardInput().get(keys);
                        System.out.println(totalResources);
                    }


                    int paidRes = 0;
                    for(int r=0; r<in.length(); r=r+3) {
                        paidRes = paidRes + Integer.parseInt(String.valueOf(in.charAt(r+1)));
                    }

                    System.out.println("Resources to pay: " + totalResources);
                    System.out.println("Resources paid: " + paidRes);
                    //Confront them with whichInput string: resourceCode - quantity - storage
                    //If player indicated less resources than that he had to pay, error
                    if(paidRes<totalResources) return false;

                } else {
                    if(k != 3) {
                        //Check if player has any cards into the indicated position and it is activated
                        if (currentPlayer.getPlayerBoard().getExtraProductionPowerInput()[k-4] == null || !currentPlayer.getPlayerLeaderCards()[k-4].isPlayed()) {
                            return false;
                        }
                    }
                }

                //Save all resources player has to pay in temporary maps
                for(int z=0; z<in.length(); z=z+3) {
                    int value = Integer.parseInt(String.valueOf(in.charAt(z)));
                    int quantity = Integer.parseInt(String.valueOf(in.charAt(z + 1)));
                    switch (String.valueOf(in.charAt(z+2))) {
                        case "W": {
                            paidWarehouseResources.put(resources.get(value),
                                    paidWarehouseResources.get(resources.get(value)) + quantity);
                            break;
                        }
                        case "C": {
                            paidChestResources.put(resources.get(value),
                                    paidChestResources.get(resources.get(value)) + quantity);
                            break;
                        }
                        case "L": {
                            if(currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra"+resources.get(value))==null) {
                                System.out.println("Extra warehouse error");
                                return false;
                            }
                            else
                                paidWarehouseResources.put("extra"+resources.get(value),
                                        paidWarehouseResources.get(resources.get(value)) + quantity);
                            break;
                        }
                        default: {
                            return false;
                        }
                    }
                }

                //Check if player has each correct resource in each correct storage

                for(String keys : paidWarehouseResources.keySet()) {
                    if (currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get(keys) < paidWarehouseResources.get(keys)) {
                        System.out.println("Incorrect warehouse resources");
                        return false;
                    }
                }

                for(String keys : paidChestResources.keySet()) {
                    if (currentPlayer.getPlayerBoard().getChest().getChestResources().get(keys) < paidChestResources.get(keys)) {
                        System.out.println("Incorrect chest resources");
                        return false;
                    }
                }

                //Check if player inserted all necessary resources to activate the production

                for (String res : paidChestResources.keySet())
                {
                    paidChestResources.put(res, paidChestResources.get(res) + paidWarehouseResources.get(res));
                    for(String extraRes : paidWarehouseResources.keySet())
                    {
                        // if(extraRes.contains(res))
                           // paidChestResources.put(res, paidChestResources.get(res) + paidWarehouseResources.get("extra"+res));
                    }
                }

                if(k<3)
                {
                    for(String res : currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardInput().keySet())
                    {
                        if (paidChestResources.get(res) < currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardInput().get(res)) {
                            System.out.println(res);
                            System.out.println(paidChestResources.get(res));
                            System.out.println(currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardInput().get(res));
                            System.out.println("Not enough resources");
                            return false;
                        }
                    }
                } else {
                        //if (paidChestResources.get(currentPlayer.getPlayerBoard().getExtraProductionPowerInput()[j-4]) < 1) {
                            //return false;
                       // }
                }
            }
        }
        return currentPlayer.activateProduction(activation, whichInput, whichOutput);
    }



    public GameModel getGameModel() {
        return this.gameModel;
    }

    public void broadcastMarket (UpdateClientMarketMessage updateClientMarketMessage) {

        for (PlayerThread playerThread : this.playerThreads) {

            try {
                //Reset avviene a inizio while in playerThread
                //playerThread.getSender().reset();
                playerThread.getSender().writeObject(updateClientMarketMessage);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Market broadcast not working for " + playerThread.getNickName());
            }
        }

    }

    public void broadcastDevCardsGrid (UpdateClientDevCardGridMessage updateClientDevCardGridMessage) {

        for (PlayerThread playerThread : this.playerThreads) {

            try {
                playerThread.getSender().writeObject(updateClientDevCardGridMessage);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Grid broadcast not working for " + playerThread.getNickName());
            }
        }

    }

    public void broadcastPlayerBoards() {

        for (PlayerThread playerThread : this.playerThreads) {

            try {
                playerThread.getSender().writeObject(new UpdateClientPlayerBoardMessage(this.gameModel.getPlayers()[playerThread.getPlayerThreadNumber()].getPlayerBoard()));
                System.out.println("playerboard sent to " + playerThread.getPlayerThreadNumber());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Player boards broadcast not working");
            }
        }

    }

}
