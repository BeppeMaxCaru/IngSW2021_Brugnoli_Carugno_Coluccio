package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.RenderingView;
import Communication.ClientSide.ServerReceiver;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Message.MessageReceived.*;
import Message.SendingMessages;
import Message.ServerStartingMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HandlerGUI extends Application implements RenderingView {

    int[] activate;
    boolean turn = false;
    int gameMode;

    int correctAction;

    // Attribute of GUI da qui ok
    private Stage stage;

    private GenericClassGUI genericClassGUI;
    private InitialScenarioGUI initialScenarioGUI;
    private SyncScenarioGUI syncScenarioGUI;
    private PlotScenarioGUI plotScenarioGUI;
    private PlayerBoardScenario playerBoardScenario;
    private WaitForYourTurnScenario waitForYourTurnScenario;
    private EndGameScenario endGameScenario;

    private ClientMain clientMain;

    private ObjectInputStream receiver;
    private ObjectOutputStream sender;
    private SendingMessages msg;

    @Override
    public void start(Stage stage) throws Exception {
        Parameters args = getParameters();
        this.setStage(stage);
        this.clientMain = new ClientMain(args.getUnnamed().get(0), Integer.parseInt(args.getUnnamed().get(1)));

        this.genericClassGUI = new GenericClassGUI(this);
        this.initialScenarioGUI = new InitialScenarioGUI(this, this.stage);
        this.syncScenarioGUI = new SyncScenarioGUI(this, this.stage);
        this.plotScenarioGUI = new PlotScenarioGUI(this, this.stage);
        this.playerBoardScenario = new PlayerBoardScenario(this, new Stage());
        this.waitForYourTurnScenario = new WaitForYourTurnScenario(this, this.stage);
        this.endGameScenario = new EndGameScenario(this, this.stage);

        this.initialScenarioGUI.nickname();
    }

    public GenericClassGUI getGenericClassGUI() {
        return this.genericClassGUI;
    }

    public SyncScenarioGUI getSyncScenarioGUI() {
        return this.syncScenarioGUI;
    }

    public PlotScenarioGUI getPlotScenarioGUI() { return this.plotScenarioGUI; }

    public PlayerBoardScenario getPlayerBoardScenario() { return this.playerBoardScenario;}

    public void setStage(Stage stage) { this.stage = stage; }

    public ClientMain getClientMain() { return this.clientMain; }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
        System.out.println(this.clientMain.getClass());
    }

    public SendingMessages getMsg() { return this.msg; }

    public boolean getTurn() { return this.turn; }

    public int getGameMode() { return this.gameMode; }

    public void setGameMode(int gameMode) { this.gameMode = gameMode; }

    @Override
    public void notValidAction() {
        this.correctAction=0;
    }

    public void connectionSocket() {
        try {
            Socket socket = new Socket(this.clientMain.getHostName(), this.clientMain.getPort());
            this.receiver = new ObjectInputStream(socket.getInputStream());
            this.sender = new ObjectOutputStream(socket.getOutputStream());
            //Check passing this
            this.msg = new SendingMessages(this.clientMain, this, this.sender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNickname() {
        try {
            this.msg.sendNickname();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMarket() {
        try {
            UpdateClientMarketMessage updateClientMarketMessage = (UpdateClientMarketMessage) this.receiver.readObject();
            this.clientMain.setMarket(updateClientMarketMessage.getMarket());
            updateGridDevCard();
        } catch (Exception e) {
            this.error(e);
        }
    }

    public void updateGridDevCard() {
        try {
            UpdateClientDevCardGridMessage updateClientDevCardGridMessage = (UpdateClientDevCardGridMessage) this.receiver.readObject();
            this.clientMain.setDevelopmentCardsDecksGrid(updateClientDevCardGridMessage.getDevelopmentCardsDecksGrid());
            startingMessage();
        } catch (Exception e) {
            this.error(e);
        }
    }

    public void startingMessage() {
        try {
            ServerStartingMessage startingMessage = (ServerStartingMessage) this.receiver.readObject();
            this.clientMain.setPlayerNumber(startingMessage.getPlayerNumber());
            //System.out.println(startingMessage.getLeaderCards().length);
            this.clientMain.setLeaderCards(startingMessage.getLeaderCards());
            System.out.println(startingMessage.getLeaderCards()[0].getClass());
            this.getGenericClassGUI().LoadWTFOnTimer("matchHasStarted");
            //updatePlayerBoard();
        } catch (Exception e) {
            this.error(e);
        }
    }

    public void updatePlayerBoard() {
        try {
            UpdateClientPlayerBoardMessage playerBoardMessage = (UpdateClientPlayerBoardMessage) this.receiver.readObject();
            this.clientMain.setPlayerboard(playerBoardMessage.getPlayerboard());
            //System.out.println(playerBoardMessage.getPlayerboard().getClass());
        } catch (Exception e) {
            //System.out.println("Non arriva playerboard");
            this.error(e);
        }
    }

    public void updateLeaderCard() {
        try {
            UpdateClientLeaderCardsMessage leaderCardsMessage = (UpdateClientLeaderCardsMessage) this.receiver.readObject();
            this.clientMain.setLeaderCards(leaderCardsMessage.getLeaderCards());
            //System.out.println(this.clientMain.getPlayerboard().getVictoryPoints());
        } catch (Exception e) {
            this.error(e);
        }
    }

    public void AsyncReceiver() {
        new ServerReceiver(this.clientMain, this, this.receiver).start();
    }

    public void setActivation(int[] activate) {
        this.activate = activate;
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

    @Override
    public void itsYourTurn() {

        Platform.runLater(this.plotScenarioGUI);
        //Platform.runLater(this.playerBoardScenario);
    }


    @Override
    public void update() {
        Platform.runLater(this.playerBoardScenario);
    }


    @Override
    public void notYourTurn() {
    }

    @Override
    public void endTurn() {
        Platform.runLater(this.waitForYourTurnScenario);
        //Platform.runLater(this.playerBoardScenario);
    }

    @Override
    public void endMultiplayerGame(GameOverMessage msg) {
        getClientMain().setVictoryPoints(msg.getVictoryPoints());
        getClientMain().setWinner(msg.getWinner());
        Platform.runLater(this.endGameScenario);
    }
}
