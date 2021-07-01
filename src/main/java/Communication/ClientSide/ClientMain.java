package Communication.ClientSide;

import Communication.ClientSide.RenderingView.GUI.HandlerGUI;
import Communication.ClientSide.RenderingView.CLI.HandlerCLI;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import javafx.application.Application;

import java.util.HashMap;
import java.util.Map;

public class ClientMain{

    //Connection attributes
    /**
     * Host name
     */
    private final String hostName;

    /**
     * Port used for connection
     */
    private final int port;

    //All game mods attributes
    /**
     * The nickname chosen
     */
    private String nickname;

    /**
     * The player number assigned
     */
    private int playerNumber;

    /**
     * The market
     */
    private Market market;

    /**
     * The development cards grid
     */
    private DevelopmentCardsDecksGrid developmentCardsDecksGrid;

    //Multi player attributes
    /**
     * The client's player board
     */
    private Playerboard playerboard;

    /**
     * The client's leader cards
     */
    private LeaderCard[] leaderCards = new LeaderCard[4];

    /**
     * The game winner
     */
    private String winner;

    /**
     * The victory points to be collected
     */
    private int victoryPoints;

    //Single player attributes
    /**
     * Action counters for single player
     */
    private ActionCountersDeck actionCountersDeck;

    /**
     * Local player and Lorenzo il Magnifico
     */
    private Player[] localPlayers;

    /**
     * Leader cards available
     */
    private LeaderCardDeck leaderCardDeck;

    /**
     * Builds the client main
     * @param hostname The host name
     * @param port The port used
     */
    public ClientMain(String hostname, int port) {
        this.hostName = hostname;
        this.port = port;
    }

    /**
     * The client's main
     * @param args The arguments passed to the client
     */
    public static void main(String[] args) {
        if (args.length < 2) return;
        if (args.length > 3) return;

        String hostname = args[0];


        int port = Integer.parseInt(args[1]);
        ClientMain client = new ClientMain(hostname, port);
        if(args.length==3 && args[2].equals("--cli"))
        {
            HandlerCLI cli = new HandlerCLI(client);
            cli.execute();
        } else {
            //Testing gui = new Testing();
            //gui.setClientMain(client);
            Application.launch(HandlerGUI.class, args);
            //System.out.println("aki");
            //Application.launch(gui.getClass());
            //System.out.println("aki");

            //new ServerReceiver(client, gui).start();
            //HandlerGUI.launch(gui.getClass());
        }
    }

    /**
     * Returns the player's number
     * @return the player's number
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Returns the nickname
     * @return the nickname
     */
    public String getNickname () {
        return this.nickname;
    }

    /**
     * Returns the market
     * @return the market
     */
    public Market getMarket() {
        return this.market;
    }

    /**
     * Sets the market
     * @param market The market to be set
     */
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
        //this.leaderCards = new LeaderCard[4];
        //this.leaderCards = leaderCards;
        for (int i = 0; i < this.leaderCards.length; i++) {
            this.leaderCards[i] = leaderCards[i];
        }
    }

    public LeaderCard[] getLeaderCards() {
        return this.leaderCards;
    }

    public String getHostName() {
        return hostName;
    }


    public int getPort() {
        return port;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public ActionCountersDeck getActionCountersDeck() {
        return actionCountersDeck;
    }

    public void setActionCountersDeck(ActionCountersDeck actionCountersDeck) {
        this.actionCountersDeck = actionCountersDeck;
    }

    public Player[] getLocalPlayers() {
        return localPlayers;
    }

    public void setLocalPlayers(Player[] localPlayers) {
        this.localPlayers = localPlayers;
    }

    public LeaderCardDeck getLeaderCardDeck() {
        return leaderCardDeck;
    }

    public void setLeaderCardDeck(LeaderCardDeck leaderCardDeck) {
        this.leaderCardDeck = leaderCardDeck;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    public boolean checkLocalLeaders(Player player, int card) {
        return player.getPlayerLeaderCards()[card] != null && !player.getPlayerLeaderCards()[card].isPlayed();
    }

    public boolean checkLocalMarketAction(Playerboard board,  String choice, int i, String wlChoice, String leader) {

        if(choice.equalsIgnoreCase("ROW"))
        {
            if(wlChoice.length()!=4) return false;
        }
        else
        {
            if(wlChoice.length()!=3) return false;
        }

        if(leader.contains("1"))
            if (board.getResourceMarbles()[1]==null) return false;

        if(leader.contains("0"))
            if (board.getResourceMarbles()[0]==null) return false;

        int extraSpaces=0;
        if(wlChoice.contains("L"))
        {
            for(String keys : board.getWareHouse().getWarehouseResources().keySet())
                if (keys.contains("extra")) extraSpaces++;
            if(extraSpaces==0) return false;
        }

        if(choice.equalsIgnoreCase("ROW"))
        {
            if(leader.length()<4) {
                StringBuilder cBuilder = new StringBuilder(leader);
                for(int k = cBuilder.length(); k<4; k++)
                    cBuilder.append("X");
                leader = cBuilder.toString();
            }
            return this.market.updateRow(i, this.localPlayers, 0, wlChoice, leader);
        }
        else
        {
            if(leader.length()<3) {
                StringBuilder cBuilder = new StringBuilder(leader);
                for(int k = cBuilder.length(); k<3; k++)
                    cBuilder.append("X");
                leader = cBuilder.toString();
            }
            return this.market.updateColumn(i, this.localPlayers, 0, wlChoice, leader);
        }
    }

    public boolean checkLocalBuyCard(Player currentPlayer, int column, int l, int[] quantity, String[] wclChoice) {

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

        if (this.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0] != null) {

            //Control on quantity and possibly discounts
            //If paidResources hashMap isn't equals to cardCost hashMap
            if (!paidResources.equals(this.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0].getDevelopmentCardCost())) {
                //Check for discounts
                if (currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0] != null && currentPlayer.getPlayerLeaderCards()[0].isPlayed()) {
                    paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0]) + 1);
                    //Check if player has activated only first discount
                    if (!paidResources.equals(this.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0].getDevelopmentCardCost())) {
                        if (currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1] != null && currentPlayer.getPlayerLeaderCards()[1].isPlayed()) {
                            paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[1]) + 1);
                            //Check if player has activated both first and second discounts
                            if (!paidResources.equals(this.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0].getDevelopmentCardCost())) {
                                //Check if player has activated only second discount
                                paidResources.put(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0], paidResources.get(currentPlayer.getPlayerBoard().getDevelopmentCardDiscount()[0]) - 1);
                                if (!paidResources.equals(this.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[3-l][column][0].getDevelopmentCardCost())) {
                                    //If resourcePaid isn't equal to cardCost, player hasn't inserted correct resource for buy the card
                                    //System.out.println("1");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("Card null");
            return false;
        }

        for (int k = 0; k < 4; k++) {
            int count = 0;
            for (int z = 0; z < wclChoice[k].length(); z++) {
                if (String.valueOf(wclChoice[k].charAt(z)).equals("W"))
                    count++;
            }
            if(count>0)
                if (currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get(resources.get(k)) != null) {
                    if (currentPlayer.getPlayerBoard().getWareHouse().getWarehouseResources().get(resources.get(k)) < count) {
                        return false;
                    }
                } else {
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
                        return false;
                    }
                } else {
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
                        return false;
                    }
                } else {
                    return false;
                }
        }
        return true;
    }

    /**
     *
     * @param player
     * @param activation
     * @param whichInput
     * @return
     */
    public boolean checkLocalActivateProd(Player player, int[] activation, String[] whichInput) {
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
                        if (player.getPlayerBoard().getPlayerBoardDevelopmentCards()[j][k] != null)
                            break;

                    if (player.getPlayerBoard().getPlayerBoardDevelopmentCards()[j][k] == null) {
                        System.out.println("Not existing Development card in this position");
                        return false;
                    }

                    //Check how many resources player has to spend
                    int totalResources = 0;
                    for(String keys : player.getPlayerBoard().getPlayerBoardDevelopmentCards()[j][k].getDevelopmentCardInput().keySet())
                    {
                        totalResources = totalResources + player.getPlayerBoard().getPlayerBoardDevelopmentCards()[j][k].getDevelopmentCardInput().get(keys);
                    }

                    int paidRes = 0;
                    for(int r=0; r<in.length(); r=r+3) {
                        paidRes = paidRes + Integer.parseInt(String.valueOf(in.charAt(r+1)));
                    }

                    //Confront them with whichInput string: resourceCode - quantity - storage
                    //If player indicated less resources than that he had to pay, error
                    if(paidRes!=totalResources) return false;

                } else {
                    if(k == 3) {
                        int paidRes = 0;
                        for(int r=0; r<in.length(); r=r+3) {
                            paidRes = paidRes + Integer.parseInt(String.valueOf(in.charAt(r+1)));
                        }
                        //Confront them with whichInput string: resourceCode - quantity - storage
                        //If player indicated less resources than that he had to pay, error
                        if(paidRes!=2) return false;
                    } else {
                        //Check if player has any cards into the indicated position and it is activated
                        if (player.getPlayerBoard().getExtraProductionPowerInput()[k-4] == null) {
                            System.out.println("Card not existing or not activated");
                            return false;
                        }

                        int paidRes = 0;
                        for(int r=0; r<in.length(); r=r+3) {
                            paidRes = paidRes + Integer.parseInt(String.valueOf(in.charAt(r+1)));
                        }
                        //Confront them with whichInput string: resourceCode - quantity - storage
                        //If player indicated less resources than that he had to pay, error
                        if(paidRes!=1) return false;
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
                            if(player.getPlayerBoard().getWareHouse().getWarehouseResources().get("extra"+resources.get(value))==null) {
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
                    if (player.getPlayerBoard().getWareHouse().getWarehouseResources().get(keys) < paidWarehouseResources.get(keys)) {
                        System.out.println("Incorrect warehouse resources");
                        return false;
                    }
                }

                for(String keys : paidChestResources.keySet()) {
                    if (player.getPlayerBoard().getChest().getChestResources().get(keys) < paidChestResources.get(keys)) {
                        System.out.println("Incorrect chest resources");
                        return false;
                    }
                }

                //Check if player inserted all necessary resources to activate the production
                for (String res : paidChestResources.keySet())
                {
                    paidChestResources.put(res, paidChestResources.get(res) + paidWarehouseResources.get(res));
                    if(paidWarehouseResources.get("extra"+res)!=null)
                        paidChestResources.put(res, paidChestResources.get(res) + paidWarehouseResources.get("extra"+res));
                }

                if(k<3)
                {
                    for(String res : player.getPlayerBoard().getPlayerBoardDevelopmentCards()[j][k].getDevelopmentCardInput().keySet())
                    {
                        if (paidChestResources.get(res) < player.getPlayerBoard().getPlayerBoardDevelopmentCards()[j][k].getDevelopmentCardInput().get(res)) {
                            return false;
                        }
                    }
                } else {
                    if(k!=3){
                        if (paidChestResources.get(player.getPlayerBoard().getExtraProductionPowerInput()[k-4]) < 1) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Method that check if a player has a relation with vatican
     */
    public void checkRelationWithVatican() {
        for(int i = 0; i < 2; i++) {
            int crossPosition = getLocalPlayers()[i].getPlayerBoard().getFaithPath().getCrossPosition();
            if(getLocalPlayers()[i].getPlayerBoard().getFaithPath().getFaithPathTrack()[crossPosition].isPopeSpace()) {
                for (int k = 0; k < 2; k++) {
                    getLocalPlayers()[i].getPlayerBoard().getFaithPath().checkRelationWithVatican(crossPosition, getLocalPlayers()[i].getPlayerBoard());
                    //
                    System.out.println(i + " " + getLocalPlayers()[i].getPlayerBoard().getVictoryPoints());
                    //
                }
                break;
            }
        }
    }
}

