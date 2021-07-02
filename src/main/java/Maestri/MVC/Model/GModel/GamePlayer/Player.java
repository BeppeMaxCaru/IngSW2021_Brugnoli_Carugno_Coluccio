package Maestri.MVC.Model.GModel.GamePlayer;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.DiscountDevelopmentCardsLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraProductionPowerLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.WhiteMarbleResourceLeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

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
    private final LeaderCard[] playerLeaderCards;

    /**
     * Player total victory points
     */
    private int playerTotalVictoryPoints = 0;

    private int discarded = 0;

    /**
     * Initializes a new player
     * @param nickname nickname
     * @param playerNumber player number of the player in the game
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

    /**
     * Returns the nickname of the player
     * @return the nickname of the player
     */
    public String getNickname(){
        return this.nickname;
    }

    /**
     * Set the nickname of the player
     * @param nick nickname
     */
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
     * @param index index where to set the leader card
     * @param leaderCard leader card to set
     */
    public void setPlayerLeaderCard(int index, LeaderCard leaderCard) {
        this.playerLeaderCards[index] = leaderCard;
    }

    /**
     * Sets to the player its starting board depending on its number
     */
    public void setStartingPlayerBoard(String resource) {

        int resourceNumWarehouse;
        resourceNumWarehouse = getPlayerBoard().getWareHouse().getWarehouseResources().get(resource);
        getPlayerBoard().getWareHouse().getWarehouseResources().put(resource, resourceNumWarehouse + 1);
    }

    /**
     * Buys a development card
     * @param developmentCardsDecksGrid developments cards available to buy
     * @param column chosen column
     * @param level chosen level
     * @param position chosen position on which put the card
     * @param wclChoice choice shelf from which pick resources
     * @return true if the player buys a card successfully
     */
    public boolean buyDevelopmentCard(DevelopmentCardsDecksGrid developmentCardsDecksGrid, int column, int level, int position, String[] wclChoice) {

        //Deny buying 8th card always
        if (this.playerBoard.getDevelopmentCardsBought() == 7) return false;

        if (!this.playerBoard.isCardBelowCompatible(position, developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column][0]))
            return false;

        //Removes the resources from the player who bought the development card according to the development card cost
        developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column][0].payDevelopmentCard(this.playerBoard, wclChoice);

        //Place the dev card into the indicated position
        this.playerBoard.setPlayerBoardDevelopmentCards(developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column][0], position);

        //Updates the player victory points by adding to them the victory points obtained from the new development card
        this.playerBoard.sumVictoryPoints(developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column][0].getVictoryPoints());

        //Removes the development card from the grid by removing it from the deck where it was bought
        List<DevelopmentCard> reducedDeck = new ArrayList<>(Arrays.asList(developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column]));
        reducedDeck.remove(0);
        developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column] = reducedDeck.toArray(developmentCardsDecksGrid.getDevelopmentCardsDecks()[3-level][column]);
        return true;
    }

    /**
     *
     * @param activateCards powers activated
     * @param fromWhereCards indicates which input resources player wants to spend
     * @param whichOutput indicates which output resources player wants to store
     * @return true if the player activates production powers successfully
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
                        if(this.playerBoard.getPlayerBoardDevelopmentCards()[i][j]!=null)
                            break;
                    }
                    for(String s : this.playerBoard.getPlayerBoardDevelopmentCards()[i][j].getDevelopmentCardOutput().keySet())
                    {
                        if(s.equals("REDCROSS"))
                        {
                            if(this.playerBoard.getPlayerBoardDevelopmentCards()[i][j].getDevelopmentCardOutput().get(s)!=0)
                                redCross = redCross + this.playerBoard.getPlayerBoardDevelopmentCards()[i][j].getDevelopmentCardOutput().get(s);
                        }
                        else
                        {
                            if(this.playerBoard.getChest().getChestResources().get(s)!=null)
                                this.playerBoard.getChest().getChestResources().put(s, this.playerBoard.getChest().getChestResources().get(s) +  this.playerBoard.getPlayerBoardDevelopmentCards()[i][j].getDevelopmentCardOutput().get(s));
                        }
                    }
                }
                else
                {
                    // Add outputResources to the chest
                    if(activateCards[j]==1)
                    {
                        if (whichOutput[j-3]==4)
                            redCross++;
                        else if(whichOutput[j-3] != -1)
                            this.playerBoard.getChest().getChestResources().put(resources.get(whichOutput[j-3]) , this.playerBoard.getChest().getChestResources().get(resources.get(whichOutput[j-3])) + 1);
                        if(j!=3)
                            redCross++;
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
     * @param var card to play
     * @return true if the player activated the card successfully
     */
    public boolean playLeaderCard(int var) {

        int check;

        if (var == 0) {
            if (this.playerLeaderCards[var] != null) {
                check = 0;
            } else {
                return false;
            }
        } else if (var == 1) {
            if (this.playerLeaderCards[var] != null) {
                check = 1;
            } else {
                if (this.playerLeaderCards[var - 1] != null) {
                    check = 0;
                } else {
                    return false;
                }
            }
        } else return false;

        if(!this.playerLeaderCards[check].checkRequisites(this.playerBoard))
            return false;

        this.playerLeaderCards[check].activateAbility(this.playerBoard);
        this.playerBoard.sumVictoryPoints(this.playerLeaderCards[check].getVictoryPoints());

        if(this.playerLeaderCards[0].isPlayed() && this.playerLeaderCards[1].isPlayed()){
            if(this.playerLeaderCards[0].getClass().equals(this.playerLeaderCards[1].getClass())){
                if(this.playerLeaderCards[0] instanceof DiscountDevelopmentCardsLeaderCard){
                    if(((DiscountDevelopmentCardsLeaderCard) this.playerLeaderCards[0]).getDiscount().equals(this.playerBoard.getDevelopmentCardDiscount()[1])){
                        String temp = this.playerBoard.getDevelopmentCardDiscount()[1];
                        this.playerBoard.getDevelopmentCardDiscount()[1]=this.playerBoard.getDevelopmentCardDiscount()[0];
                        this.playerBoard.getDevelopmentCardDiscount()[0]=temp;
                    }
                }
                if(this.playerLeaderCards[0] instanceof ExtraProductionPowerLeaderCard){
                    if(((ExtraProductionPowerLeaderCard) this.playerLeaderCards[0]).getInput().equals(this.playerBoard.getExtraProductionPowerInput()[1])){
                        String temp = this.playerBoard.getExtraProductionPowerInput()[1];
                        this.playerBoard.getExtraProductionPowerInput()[1]=this.playerBoard.getExtraProductionPowerInput()[0];
                        this.playerBoard.getExtraProductionPowerInput()[0]=temp;
                    }
                }
                if(this.playerLeaderCards[0] instanceof WhiteMarbleResourceLeaderCard){
                    if(((WhiteMarbleResourceLeaderCard) this.playerLeaderCards[0]).getWhiteMarbleResource().equals(this.playerBoard.getResourceMarbles()[1])){
                        Marble temp = this.playerBoard.getResourceMarbles()[1];
                        this.playerBoard.getResourceMarbles()[1]=this.playerBoard.getResourceMarbles()[0];
                        this.playerBoard.getResourceMarbles()[0]=temp;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Plays a leader card
     * @param position card to play
     * @return true if the player activated the card successfully
     */
    public boolean playLeaderCardUpdated(int position) {

        if(!this.playerLeaderCards[position].checkRequisites(this.playerBoard))
            return false;

        this.playerLeaderCards[position].activateAbility(this.playerBoard);
        this.playerBoard.sumVictoryPoints(this.playerLeaderCards[position].getVictoryPoints());
        return true;
    }

    /**
     * Discards a leader card
     * @param var card to discard
     * @return true if the player discarded the card successfully
     */
    public boolean discardLeaderCard(int var) {

        int cards = 0;

        for(int k=0; k<this.playerLeaderCards.length; k++)
            if(this.playerLeaderCards[k]!=null)
                cards++;

        for (int k = 0; k < this.playerLeaderCards.length - 1; k++) {
            if (k < var) {
                this.playerLeaderCards[k] = this.playerLeaderCards[k];
            } else if (k >= var) {
                this.playerLeaderCards[k] = this.playerLeaderCards[k + 1];
            }
        }

        this.playerLeaderCards[this.playerLeaderCards.length - 1] = null;

        if(cards<=2)
            this.playerBoard.getFaithPath().moveCross(1);

        return true;
    }

    /**
     * Returns if the player ends the game
     * @return if the player ends the game
     */
    public boolean checkWinCondition() {

        return this.playerBoard.getFaithPath().getCrossPosition() >= 24 ||
                this.playerBoard.getDevelopmentCardsBought() >= 7;
    }

    /**
     * This method sums all the player's victory points at the end of the game
     * @return the total victory points
     */
    public int sumAllVictoryPoints() {
        int victoryPoints = 0;
        int numResources = 0;

        //Points of faith track cards, development cards and leaders
        victoryPoints = victoryPoints + getPlayerBoard().getVictoryPoints();

        //Points of faith cross
        victoryPoints = victoryPoints + getPlayerBoard().getFaithPath().getFaithPathTrack()[getPlayerBoard().getFaithPath().getCrossPosition()].getVictoryPoints();

        //Points of Resources stored in warehouse, chest and extra warehouse space
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

}
