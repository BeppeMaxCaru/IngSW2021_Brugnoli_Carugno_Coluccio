package Maestri.MVC;

import Communication.ServerSide.PlayerThread;
import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.*;

import java.io.*;
import java.util.*;

public class GameController implements Runnable {

    //update
    private final GameModel gameModel;

    //Margara
    public GameController(List<PlayerThread> queueFIFO) {

        Player[] players = new Player[4];

        //Non serve più ordine dei player almeno nel controller
        //Meglio tenere l'ordine!
        Set<PlayerThread> playerThreads = new HashSet<>();
        PlayerThread[] playersPlaying = new PlayerThread[4];

        List<Player> playersToPlay = new ArrayList<>();

        //Chat version
        for (int i=0;i<4;i++) {
            try {
                //If there is a player adds it
                if (!queueFIFO.isEmpty()) {
                    playersPlaying[i] = queueFIFO.remove(0);
                    playersPlaying[i].setPlayerThreadNumber(i);
                    playersPlaying[i].run();
                    //Check this
                    playersPlaying[i].setGameController(this);

                    //Old
                    //playersToPlay.add(clientsWaiting.remove(0));
                    //playersToPlay.get(0).setPlayerNumber(i);
                    //playersToPlay.get(i).getOutPrintWriter().println("Match has started, your player number is " + i);

                    //Creates only the data of the player in the game model
                    players[i] = new Player(playersPlaying[i].getNickName(), i);

                    if(i!=0){
                        playersPlaying[i].getOutPrintWriter().println("Wait for other players turn...");
                        //playersToPlay.get(0).getOutPrintWriter().println("Wait for other players turn...");
                    }

                }
            } catch (Exception e) {
                //If no clients waiting set other players null
                playersPlaying[i] = null;
            }
        }

        //Cosa passo al gameModel?? Player o PLayerThread o si può rimuovere del tutto e gestire l'ordine tramite controller?
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Must update constructor
        //this.gameModel = new GameModel(playersPlaying);

        //Temporary solution
        this.gameModel = new GameModel(players);

    }

    @Override
    public void run() {
        //System.out.println("New game started");

        for (int i = 0; i < this.gameModel.getPlayers().length; i++) {
            if (this.gameModel.getPlayers()[i] != null) {

            }
        }

        //Now starts receiving command
        //Stops automatic setup like in first turn
        while (!this.gameModel.checkEndPlay()) {

            for (int i = 0; i < this.gameModel.getPlayers().length; i++) {
                if (this.gameModel.getPlayers()[i] != null) {
                    try {
                        Player currentPlayer = this.gameModel.getPlayers()[i];

                        Scanner in = currentPlayer.getInScannerReader();
                        PrintWriter out = currentPlayer.getOutPrintWriter();

                        out.println("It's your turn");

                        //Scanner in = new Scanner(new InputStreamReader(this.players[i].getClientSocket().getInputStream()));
                        //PrintWriter out = new PrintWriter(this.players[i].getClientSocket().getOutputStream(), true);

                        this.gameModel.getPlayers()[i].printAll(this.gameModel.getPlayers()[i].getOutPrintWriter());
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("MARKET GRID:");
                        this.gameModel.getMarket().printMarket(this.gameModel.getPlayers()[i].getOutPrintWriter());
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("DEVELOPMENT CARDS GRID:");
                        this.gameModel.getDevelopmentCardsDecksGrid().printGrid(this.gameModel.getPlayers()[i].getOutPrintWriter());



                        this.gameModel.getPlayers()[i].getOutPrintWriter().println("Your turn has ended. Wait for other players...");
                        this.gameModel.getPlayers()[i].getOutPrintWriter().println();

                    } catch (Exception e) {
                        //Player disconesso
                        //System.err.println(e.getMessage());
                        this.gameModel.getPlayers()[i] = null;

                    }
                }
            }

        }

    }

    public boolean checkPlayCards (Player currentPlayer, int c) {

        PrintWriter out = currentPlayer.getOutPrintWriter();

        if (currentPlayer.getPlayerLeaderCards()[c] != null && !currentPlayer.getPlayerLeaderCards()[c].isPlayed()) {
            out.println("Played");
            currentPlayer.getPlayerLeaderCards()[c].printLeaderCard(out);
            currentPlayer.playLeaderCard(c);
        } else return false;

        return currentPlayer.playLeaderCard(c);

    }

    public boolean checkDiscardCards (Player currentPlayer, int c){

        PrintWriter out = currentPlayer.getOutPrintWriter();

        if (currentPlayer.getPlayerLeaderCards()[c] != null && !currentPlayer.getPlayerLeaderCards()[c].isPlayed()) {
            return currentPlayer.discardLeaderCard(c);
        } else return false;

    }

    public boolean checkMarketAction (Player currentPlayer, String choice, int i, String wlChoice, String c)
    {
        PrintWriter out = currentPlayer.getOutPrintWriter();

        if(c.contains("1"))
            if (currentPlayer.getPlayerBoard().getResourceMarbles()[1]==null) return false;

        if(c.contains("0"))
            if (currentPlayer.getPlayerBoard().getResourceMarbles()[0]==null) return false;

        if(wlChoice.toUpperCase().contains("L"))
            for(String keys : currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().keySet())
                if (!keys.contains("extra")) return false;

        //If player picks row
        if(choice.equalsIgnoreCase("R"))
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

    public boolean checkBuyDevCard(Player currentPlayer, String colour, int l, int[] quantity, String[] wclChoice) {

        PrintWriter out = currentPlayer.getOutPrintWriter();

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

        int column = this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsColours().get(colour.toUpperCase());

        if (this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0] != null) {

            //Control on quantity and possibly discounts
            //If paidResources hashMap isn't equals to cardCost hashMap
            if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost())) {
                //Check for discounts
                if (currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0] != null && currentPlayer.getPlayerLeaderCards()[0].isPlayed()) {
                    paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0]) + 1);
                    //Check if player has activated only first discount
                    if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost())) {
                        if (currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1] != null && currentPlayer.getPlayerLeaderCards()[1].isPlayed()) {
                            paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1]) + 1);
                            //Check if player has activated both first and second discounts
                            if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost())) {
                                //Check if player has activated only second discount
                                paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0]) - 1);
                                if (!paidResources.equals(this.gameModel.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[l][column][0].getDevelopmentCardCost())) {
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
            if (currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get(resources.get(k)) != count) {
                return false;
            }

            count = 0;
            for (int z = 0; z < wclChoice[k].length(); z++) {
                if (String.valueOf(wclChoice[k].charAt(z)).equalsIgnoreCase("c"))
                    count++;
            }
            if (currentPlayer.getPlayerBoard().getChest().getChestResources().get(resources.get(k)) != count) {
                return false;
            }

            count = 0;
            for (int z = 0; z < wclChoice[k].length(); z++) {
                if (String.valueOf(wclChoice[k].charAt(z)).equalsIgnoreCase("l"))
                    count++;
            }
            if (currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + resources.get(k)) != count) {
                return false;
            }

        }
        return true;
    }

    public boolean checkActivateProduction(Player currentPlayer, int[] activation, String[] whichInput, int[] whichOutput) {

        PrintWriter out = currentPlayer.getOutPrintWriter();

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


        for(int k=0; k<activation.length; k++)
        {
            if(activation[k]==1) {

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
                    for(String keys : currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().keySet())
                        totalResources=totalResources+currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().get(keys);
                    //Confront them with whichInput string: resourceCode - quantity - storage
                    //If player indicated less resources than that he had to pay, error
                    if(whichInput.length<totalResources*3) return false;

                } else {
                    if(k != 3) {
                        //Check if player has any cards into the indicated position and it is activated
                        if (currentPlayer.getPlayerBoard().getExtraProductionPowerInput()[k-4] == null || !currentPlayer.getPlayerLeaderCards()[k-4].isPlayed()) {
                            return false;
                        }
                    }
                }

                //Save all resources player has to pay in temporary maps
                for(int z=0; z<whichInput.length-2; z=z+3) {
                    switch (String.valueOf(whichInput[z+2]).toUpperCase()) {
                        case "W": {
                            paidWarehouseResources.put(resources.get(Integer.parseInt(whichInput[z])), Integer.parseInt(whichInput[z+1]));
                            break;
                        }
                        case "C": {
                            paidChestResources.put(resources.get(Integer.parseInt(whichInput[z])), Integer.parseInt(whichInput[z+1]));
                            break;
                        }
                        case "L": {
                            if(currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra"+resources.get(Integer.parseInt(whichInput[z])))==null) {
                                return false;
                            }
                            else
                                paidWarehouseResources.put("extra"+resources.get(Integer.parseInt(whichInput[z])), Integer.parseInt(whichInput[z+1]));
                            break;
                        }
                    }
                }

                //Check if player has each correct resource in each correct storage

                for(String keys : paidWarehouseResources.keySet())
                    if(currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get(keys)<paidWarehouseResources.get(keys)) {
                        return false;
                    }
                for(String keys : paidChestResources.keySet())
                    if(currentPlayer.getPlayerBoard().getChest().getChestResources().get(keys)<paidChestResources.get(keys)) {
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
                    for(String res : currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().keySet())
                    {
                        if (paidChestResources.get(res) < currentPlayer.getPlayerBoard().getPlayerboardDevelopmentCards()[j][k].getDevelopmentCardCost().get(res)) {
                            return false;
                        }
                    }
                } else {
                        if (paidChestResources.get(currentPlayer.getPlayerBoard().getExtraProductionPowerInput()[j-4]) < 1) {
                            return false;
                        }
                }
            }
        }
        return currentPlayer.activateProduction(activation, whichInput, whichOutput);
    }

    public void broadcastMarketChange (GameModel gameModel) {

        for (Player player : this.gameModel.getPlayers()) {
            try {
                this.gameModel.getMarket().printMarket(player.getOutPrintWriter());
            } catch (Exception e) {
                //Player è null
            }
        }
    }

    public void broadcastGridChange (GameModel gameModel) {
        for (Player player : this.gameModel.getPlayers()) {
            try {
                this.gameModel.getDevelopmentCardsDecksGrid().printGrid(player.getOutPrintWriter());
            } catch (Exception e) {
                //Player è null
            }
        }
    }

    public void removePlayer (String nickname, GameModel gameModel) {
        for (Player player : this.gameModel.getPlayers()) {
            try {
                if (player.getNickname().equals(nickname)) player = null;
            } catch (Exception e) {
                //Skippa
            }
        }
    }

    public GameModel getGameModel() {
        return this.gameModel;
    }
}
