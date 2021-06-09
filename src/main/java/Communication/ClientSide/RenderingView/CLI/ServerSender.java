package Communication.ClientSide.RenderingView.CLI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.RenderingView;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Message.*;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerSender extends Thread {

    private Socket socket;
    private final ClientMain clientMain;
    private final RenderingView view;
    private final int gameMode;
    private SendingMessages msg;

    public ServerSender (ClientMain clientMain, int gameMode, SendingMessages msg) {
        this.clientMain = clientMain;
        this.view = new CLI(this.clientMain);
        this.gameMode = gameMode;
        if(this.gameMode==1)
        {
            this.msg = msg;
        }

    }

    @Override
    public void run() {

            String action = "";
            int mainAction = 0;
            //Keeps sending messages with switch until quit for now
            do {

                try {

                    while (!action.equals("END TURN") && !action.equals("QUIT")) {

                        action = this.view.getActionChoice();

                        switch (action) {
                            case "P":
                            case "PLAY LEADER CARD": {
                                int leader = this.view.getPlayedLeader();
                                if(gameMode==0) {
                                    if(this.checkLocalLeaders(this.clientMain.getLocalPlayers()[0], leader))
                                    {
                                        if(!this.clientMain.getLocalPlayers()[0].playLeaderCard(leader))
                                            this.view.notValidAction();
                                    }
                                } else this.msg.sendPlayedLeader(leader);
                                break;
                            }
                            case "D":
                            case "DISCARD LEADER CARD": {
                                int leader = this.view.getDiscardedLeader();
                                if(this.gameMode==0){
                                    if(this.checkLocalLeaders(this.clientMain.getLocalPlayers()[0], leader)){
                                        if(!this.clientMain.getLocalPlayers()[0].discardLeaderCard(leader))
                                            this.view.notValidAction();
                                    }
                                } else this.msg.sendDiscardedLeader(leader);
                                break;
                            }
                            case "M":
                            case "PICK RESOURCES FROM MARKET": {

                                if(this.gameMode==0 && mainAction==1)
                                {
                                    this.view.notValidAction();
                                    break;
                                }

                                int[] coordinates = this.view.getMarketCoordinates();
                                String parameter;
                                int index;
                                if(coordinates[0] == 0) parameter = "ROW";
                                else parameter = "COLUMN";
                                index = coordinates[1];
                                String wlChoice = this.view.getResourcesDestination(parameter);
                                String chosenMarble = this.view.getWhiteMarbleChoice();

                                if(this.gameMode==0){
                                    if(this.checkLocalMarketAction(this.clientMain.getLocalPlayers()[0].getPlayerBoard(), wlChoice, chosenMarble))
                                        if(parameter.equals("ROW"))
                                        {
                                            if(this.clientMain.getMarket().updateRow(index, this.clientMain.getLocalPlayers(), 0, wlChoice, chosenMarble))
                                                mainAction++;
                                            else this.view.notValidAction();
                                        } else {
                                            if(this.clientMain.getMarket().updateColumn(index, this.clientMain.getLocalPlayers(), 0, wlChoice, chosenMarble))
                                                mainAction++;
                                            else this.view.notValidAction();
                                        }
                                } else {
                                    this.msg.sendMarketAction(parameter, index, wlChoice, chosenMarble);
                                }
                                break;
                            }
                            case "B":
                            case "BUY DEVELOPMENT CARD": {

                                if(this.gameMode==0 && mainAction==1)
                                {
                                    this.view.notValidAction();
                                    break;
                                }

                                int[] coordinates = this.view.getDevelopmentCardsGridCoordinates();
                                int column = coordinates[0];
                                int level = 3 - coordinates[1];

                                //Check
                                int[] quantity = new int[4];
                                String[] shelf;
                                String[][] pickedResources = this.view.getPayedResources();
                                for(int k = 0; k < quantity.length; k++)
                                    quantity[k] = Integer.parseInt(pickedResources[0][k]);
                                shelf = pickedResources[1];
                                int pos = this.view.getChosenPosition();

                                if(this.gameMode==0){
                                    if(this.checkLocalBuyCard(this.clientMain.getLocalPlayers()[0], column, level, quantity, shelf))
                                    {
                                        if(this.clientMain.getLocalPlayers()[0].buyDevelopmentCard(this.clientMain.getDevelopmentCardsDecksGrid(), column, level, pos, shelf))
                                            mainAction++;
                                        else this.view.notValidAction();
                                    }
                                } else this.msg.sendBuyCardAction(column, level, quantity, shelf, pos);
                                break;
                            }
                            case "A":
                            case "ACTIVATE PRODUCTION POWER": {

                                if(this.gameMode==0 && mainAction==1)
                                {
                                    this.view.notValidAction();
                                    break;
                                }

                                int[] activation = {0, 0, 0, 0, 0, 0};
                                String[] whichInput = new String[6];
                                String[] whichOutput = new String[3];

                                int stop;

                                do{
                                    stop = this.view.getActivationProd(activation);
                                    if(stop<6){
                                        activation[stop] = 1;
                                        this.view.setActivation(activation);
                                        whichInput[stop] = this.view.getInputResourceProd();
                                        if(stop>2){
                                            whichOutput[stop-3] = this.view.getOutputResourceProd();
                                        }
                                    }
                                } while (stop != 6);

                                int[] outputs = new int[3];
                                for(int k=0; k< whichOutput.length; k++)
                                    outputs[k] = Integer.parseInt(whichOutput[k]);

                                if(this.gameMode==0)
                                {
                                    if(this.checkLocalActivateProd(this.clientMain.getLocalPlayers()[0], activation, whichInput, whichOutput))
                                        if(this.clientMain.getLocalPlayers()[0].activateProduction(activation, whichInput, outputs))
                                            mainAction++;
                                        else this.view.notValidAction();
                                } else {
                                    for (int k = 0; k < 6; k++) {
                                        this.msg.sendActivationProdAction(k, activation, whichInput, whichOutput);
                                    }
                                }
                                break;
                            }
                            case "END TURN":
                            {

                                if(this.gameMode==0 && mainAction==0)
                                    this.view.notValidAction();
                                else if (this.gameMode==0 && mainAction==1)
                                {
                                    mainAction=0;
                                    action="";
                                    this.clientMain.getActionCountersDeck().drawCounter().activate(this.clientMain.getActionCountersDeck(), this.clientMain.getLocalPlayers()[1].getPlayerBoard(), this.clientMain.getDevelopmentCardsDecksGrid());
                                }
                                if(this.gameMode==1)
                                {
                                    this.msg.sendEndTurn();
                                }
                                this.view.endTurn();
                                if(this.gameMode==0) this.view.lorenzoFaithPoints();
                                break;
                            }
                            case "QUIT":
                            {
                                this.view.quit();
                                break;
                            }
                        }
                        //Player writes quit
                    }
                } catch (Exception e) {
                    this.view.senderError(e);
                    System.exit(1);
                }

            } while (!action.equals("QUIT") || !this.endLocalGame(this.clientMain.getLocalPlayers()));

            try {
                this.socket.close();
            } catch (Exception e) {
                this.view.senderError(e);
            }
    }

    public boolean endLocalGame(Player[] localPlayers){
        for(int k=0; k < 4; k++)
            if(this.clientMain.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[0][k][0]==null)
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

        if (this.clientMain.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0] != null) {

            //Control on quantity and possibly discounts
            //If paidResources hashMap isn't equals to cardCost hashMap
            if (!paidResources.equals(this.clientMain.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost())) {
                //Check for discounts
                if (player.getPlayerBoard().getDevelopmentCardDiscount()[0] != null && player.getPlayerLeaderCards()[0].isPlayed()) {
                    paidResources.put(player.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(player.getPlayerBoard().getDevelopmentCardDiscount()[0]) + 1);
                    //Check if player has activated only first discount
                    if (!paidResources.equals(this.clientMain.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost())) {
                        if (player.getPlayerBoard().getDevelopmentCardDiscount()[1] != null && player.getPlayerLeaderCards()[1].isPlayed()) {
                            paidResources.put(player.getPlayerBoard().getDevelopmentCardDiscount()[1], paidResources.get(player.getPlayerBoard().getDevelopmentCardDiscount()[1]) + 1);
                            //Check if player has activated both first and second discounts
                            if (!paidResources.equals(this.clientMain.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost())) {
                                //Check if player has activated only second discount
                                paidResources.put(player.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(player.getPlayerBoard().getDevelopmentCardDiscount()[0]) - 1);
                                if (!paidResources.equals(this.clientMain.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0].getDevelopmentCardCost())) {
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
