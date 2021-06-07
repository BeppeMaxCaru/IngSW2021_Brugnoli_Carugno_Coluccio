package Communication.ClientSide;

import Communication.ClientSide.RenderingView.GUI.HandlerGUI;
import Communication.ClientSide.RenderingView.HandlerCLI;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;

public class ClientMain {

    //Connection attributes
    private final String hostName;
    private final int port;

    //All game mods attributes
    private String nickname;
    private int playerNumber;
    private Market market;
    private DevelopmentCardsDecksGrid developmentCardsDecksGrid;

    //Multi player attributes
    private Playerboard playerboard;
    private LeaderCard[] leaderCards = new LeaderCard[4];

    //Single player attributes
    private ActionCountersDeck actionCountersDeck;
    private Player[] localPlayers;
    private LeaderCardDeck leaderCardDeck;

    public ClientMain(String hostname, int port) {
        this.hostName = hostname;
        this.port = port;
    }

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
            HandlerGUI gui = new HandlerGUI();
            new ServerReceiver(client, gui).start();
            HandlerGUI.launch();
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
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
}

