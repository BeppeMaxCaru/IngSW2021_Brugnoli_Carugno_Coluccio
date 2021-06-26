package Maestri.MVC.Model.GModel.GamePlayer;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Represents a player with all its information
 */
public class Player
{

    /**
     * Name that the player chooses to join the game
     */
    private String nickname;

    /**
     * Order in which the player joined the game
     */
    private final Integer playerNumber;

    /**
     * Player board that the player is using to play the game
     */
    private final Playerboard playerBoard;

    /**
     * Leader cards that the player has available
     */
    private LeaderCard[] playerLeaderCards;

    /**
     * Player total victory points
     */
    private int playerTotalVictoryPoints = 0;

    private Socket clientSocket;


    /**
     * Initializes a new player
     *
     *
     */

    public Player(String nickname, int playerNumber){
        this.nickname=nickname;
        this.playerBoard=new Playerboard();
        this.playerNumber = playerNumber;
        if(this.playerNumber > 1) {
            this.playerBoard.getFaithPath().moveCross(1);
        }
        this.playerLeaderCards=new LeaderCard[4];
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    public String getNickname(){
        return this.nickname;
    }

    public void setNickname(String nick) {
        this.nickname = nick;
    }

    /**
     * Returns the player's nickname
     * @return the player's nickname
     */
    public Integer getPlayerNumber() {
        return this.playerNumber;
    }


    /**
     * Returns the player's board
     * @return the player's board
     */
    public Playerboard getPlayerBoard() {
        return this.playerBoard;
    }

    /**
     * Returns the player's leader cards
     * @return the player's leader cards
     */
    public LeaderCard[] getPlayerLeaderCards() {
        return this.playerLeaderCards;
    }

    /**
     * Sets to the player a leader card in the specified position of his leader cards deck
     * @param index - index where to set the leader card
     * @param leaderCard - leader card to set
     */
    public void setPlayerLeaderCard(int index, LeaderCard leaderCard) {
        this.playerLeaderCards[index] = leaderCard;
    }

    /**
     * Sets to the player its starting board depending on its number
     */
    public void setStartingPlayerboard(String resource) {

        int resourceNumWarehouse;

        resourceNumWarehouse = getPlayerBoard().getWareHouse().getWarehouseResources().get(resource);
        getPlayerBoard().getWareHouse().getWarehouseResources().put(resource, resourceNumWarehouse + 1);
    }

    /**
     * Asks the player which action he wants to perform
     * @return the chosen action's number
     */
    public String getAction(Scanner in, PrintWriter out) {
        String actionNumber;

        out.println("What action do you want to do? Choose one of them:");
        out.println("Write 0 if you want to take resources from the market.");
        out.println("Write 1 if you want to buy a development card.");
        out.println("Write 2 if you want to activate the production.");
        actionNumber = in.nextLine();
        while(!actionNumber.equals("0") && !actionNumber.equals("1") && !actionNumber.equals("2")) {
            out.println("Number not valid!");
            out.println("What action do you want to do? Choose one of them:");
            out.println("Write 0 if you want to take resources from the market.");
            out.println("Write 1 if you want to buy a development card.");
            out.println("Write 2 if you want to activate the production.");
            actionNumber = in.nextLine();
        }

        return actionNumber;
    }

    /**
     * Buys a development card
     * @param developmentCardsDecksGrid - developments cards available to buy
     * @return true if the player buys a card successfully
     */
    public boolean buyDevelopmentCard(DevelopmentCardsDecksGrid developmentCardsDecksGrid, int column, int level, int position, String[] wclChoice) {

        //Removes the resources from the player who bought the development card according to the development card cost
        developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column][0].payDevelopmentCard(this.playerBoard, wclChoice);

        //Place the dev card into the indicated position
        this.playerBoard.setPlayerboardDevelopmentCards(developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column][0], position);

        //Updates the player victory points by adding to them the victory points obtained from the new development card
        this.playerBoard.sumVictoryPoints(developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column][0].getVictoryPoints());

        //Removes the development card from the grid by removing it from the deck where it was bought
        List<DevelopmentCard> reducedDeck = new ArrayList<>(Arrays.asList(developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column]));
        reducedDeck.remove(0);
        developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column] = reducedDeck.toArray(developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column]);
        return true;
    }


    /**
     * Activates the production powers on the player's player board
     */
    public boolean activateProduction(int[] activateCards, String[] fromWhereCards, int[] whichOutput) {
        int j;
        int redCross = 0;

        Map<Integer, String> resources = new HashMap<>();
        resources.put(0, "COINS");
        resources.put(1, "SERVANTS");
        resources.put(2, "SHIELDS");
        resources.put(3, "STONES");

        //Pick resources where player has indicated
        for (j = 0; j < 6; j++) {
            if (activateCards[j] == 1) {
                //Pick resources
                for (int k = 0; k < fromWhereCards[j].length() - 2; k = k + 3) {
                    String res = resources.get(Integer.parseInt(String.valueOf(fromWhereCards[j].charAt(k))));
                    int quantity = Integer.parseInt(String.valueOf(fromWhereCards[j].charAt(k + 1)));
                    this.playerBoard.pickResource(res, (String.valueOf(fromWhereCards[j].charAt(k + 2))), quantity);
                }

                //Add outputResources
                if(j<3)
                {
                    //Add card production power to chest
                    int i;
                    for(i=2; i>0; i--)
                    {
                        if(this.playerBoard.getPlayerboardDevelopmentCards()[i][j]!=null)
                            break;
                    }
                    for(String s : this.playerBoard.getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().keySet())
                    {
                        if(s.equals("REDCROSS"))
                        {
                            if(this.playerBoard.getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().get(s)!=0)
                                redCross = redCross + this.playerBoard.getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().get(s);
                        }
                        else
                        {
                            if(this.playerBoard.getChest().getChestResources().get(s)!=null)
                                this.playerBoard.getChest().getChestResources().put(s, this.playerBoard.getChest().getChestResources().get(s) +  this.playerBoard.getPlayerboardDevelopmentCards()[i][j].getDevelopmentCardOutput().get(s));
                        }
                    }
                }
                else
                {
                    // Add outputResources to the chest
                    for(int z=0; z<3; z++) {
                        if(activateCards[3+z]==1)
                        {
                            if (whichOutput[z]==4)
                                redCross++;
                            else if(whichOutput[z] != -1)
                                this.playerBoard.getChest().getChestResources().put(resources.get(whichOutput[z]) , this.playerBoard.getChest().getChestResources().get(resources.get(whichOutput[z])) + 1);
                        }
                    }
                }
            }
        }

        // RedCross
        if(redCross!=0)
            getPlayerBoard().getFaithPath().moveCross(redCross);

        return true;
    }

    /**
     * Plays a leader card
     */
    public boolean playLeaderCard(int var) {

        if(!this.playerLeaderCards[var].checkRequisites(this.playerBoard))
            return false;

        this.playerLeaderCards[var].activateAbility(this.playerBoard);
        this.playerBoard.sumVictoryPoints(this.playerLeaderCards[var].getVictoryPoints());

        return true;
    }

    /**
     * Discards a leader card
     */
    public boolean discardLeaderCard(int var) {

        int cards = 0;

        for(int k=0; k<this.playerLeaderCards.length; k++)
            if(this.playerLeaderCards[k]!=null)
                cards++;

        List<LeaderCard> updatedPlayerLeaderCardList = new ArrayList<>(Arrays.asList(this.playerLeaderCards));
        updatedPlayerLeaderCardList.remove(var);
        this.playerLeaderCards = updatedPlayerLeaderCardList.toArray(this.playerLeaderCards);

        if(cards<=2)
            this.playerBoard.getFaithPath().moveCross(1);

        return true;
    }

    /**
     * This method sums all the player's victory points at the end of the game
     * @return the total victory points
     */
    public int sumAllVictoryPoints() {
        int victoryPoints = 0;
        int numResources = 0;

        // Punti delle tessere del tracciato fede, carte sviluppo e carte leader.
        victoryPoints = victoryPoints + getPlayerBoard().getVictoryPoints();

        // Punti dalla posizione della croce.
        //Non serve se modifichi move cross in modo tale che aggiunga i victory points di una cella ai punti
        //della player board quando il giocatore ci capita sopra così li tieni aggiornati
        victoryPoints = victoryPoints + getPlayerBoard().getFaithPath().getFaithPathTrack()[getPlayerBoard().getFaithPath().getCrossPosition()].getVictoryPoints();

        //Punti dalle risorse rimaste nel warehouse, chest, extraWarehouse.
        numResources = numResources + numResourcesReserve();
        victoryPoints = victoryPoints + (numResources / 5);

        return victoryPoints;
    }

    /**
     * Returns all the player resources count
     * @return all the player resources count
     */
    public int numResourcesReserve() {
        int numResources = 0;
        //Watch out for extras!
        for(String key : getPlayerBoard().getWareHouse().getWarehouseResources().keySet()) {
            if (!key.contains("extra")) {
                numResources = numResources + getPlayerBoard().getWareHouse().getWarehouseResources().get(key);
                if (getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + key) != null)
                    numResources = numResources + getPlayerBoard().getWareHouse().getWarehouseResources().get("extra" + key);
            }
        }
        //Ok no extras
        for(String key : getPlayerBoard().getChest().getChestResources().keySet())
            numResources = numResources + getPlayerBoard().getChest().getChestResources().get(key);

        return numResources;
    }

    //Closing stream and socket
    public void disconnectClient() {
        try {
            this.clientSocket.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    //Closing stream and socket
    public void disconnectPlayer() {
        try {
            this.clientSocket.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
