package Maestri.MVC.Model.GModel.GamePlayer.Playerboard;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marble;

import java.io.Serializable;

/**
 * Includes all the player's game components
 */
public class Playerboard implements Serializable {

    /**
     * Number of development cards bought
     */
    private int developmentCardsBought;

    /**
     * Development cards spaces where to place development cards bought
     */
    private final DevelopmentCard[][] playerBoardDevelopmentCards;

    /**
     * Unlimited resources reserve
     */
    private final Chest chest;

    /**
     * Limited resources reserve
     */
    private final WareHouse wareHouse;

    /**
     * Tracks the player position
     */
    private final FaithPath faithPath;

    /**
     * Victory points collected
     */
    private int victoryPoints;

    /**
     * Optional permanent perk received from leader cards that allows to receive one resource from a white marble
     */
    private final Marble[] resourceMarbles = new Marble[2];

    /**
     * Optional permanent perk received from leader cards that allows to receive a one resource discount to apply while buying a development card
     */
    private final String[] developmentCardDiscount = new String[2];

    /**
     * Optional permanent perk received from leader cards that indicates the input resource for extra production power
     */
    private final String[] extraProductionPowerInput =new String[2];

    /**
     * Initializes the player board
     */
    public Playerboard() {
        this.developmentCardsBought = 0;
        this.playerBoardDevelopmentCards = new DevelopmentCard[3][3];
        this.chest = new Chest();
        this.wareHouse = new WareHouse();
        this.faithPath = new FaithPath();
        this.victoryPoints = 0;
    }

    /**
     * Put the development card on the indicated position
     * @param developmentCard card bought
     * @param pos position on which put the card
     */
    public void setPlayerBoardDevelopmentCards(DevelopmentCard developmentCard, int pos) {
        this.playerBoardDevelopmentCards[developmentCard.getDevelopmentCardLevel()-1][pos] = developmentCard;
    }

    /**
     * This method returns the number of development cards bought by the player
     * @return the number of development cards bought by the player
     */
    public int getDevelopmentCardsBought() {
        return this.developmentCardsBought;
    }

    /**
     * This method returns which Marbles player has to pick instead of WhiteMarbles
     * @return player's available marbles
     */
    public Marble[] getResourceMarbles(){
        return this.resourceMarbles;
    }

    /**
     * Returns the development cards discounts
     * @return the development cards discounts
     */
    public String[] getDevelopmentCardDiscount() {
        return this.developmentCardDiscount;
    }

    /**
     * Returns the extra production power inputs
     * @return the extra production power inputs
     */
    public String[] getExtraProductionPowerInput(){
        return this.extraProductionPowerInput;
    }

    /**
     * Returns the player board's development cards
     * @return the player board's development cards
     */
    public DevelopmentCard[][] getPlayerBoardDevelopmentCards() {
        return this.playerBoardDevelopmentCards;
    }

    /**
     * Checks that the new development card can be placed on the top of a development card pile
     * @param pile the chosen development card pile
     * @param developmentCard the development card to place
     * @return true if the development card can be placed on top
     */
    public boolean isCardBelowCompatible(int pile, DevelopmentCard developmentCard) {

        int i = 0;
        while ((i < 3) && (this.playerBoardDevelopmentCards[i][pile] != null)) {
            i++;
        }
        //If the pile is full, card can't be inserted
        if (i == 3)
            return false;
        else {
            //If the first available position of the pile isn't compatible with the dev. card level, the card can't be inserted
            if (developmentCard.getDevelopmentCardLevel() != i + 1)
                return false;
            else
            {
                //If the pile isn't full and the first available position of the pile is compatible with the dev. card level, the card can be inserted
                this.playerBoardDevelopmentCards[i][pile]=developmentCard;
                this.developmentCardsBought = this.developmentCardsBought + 1;
                return true;
            }
        }
    }

    /**
     * Returns the chest
     * @return the chest
     */
    public Chest getChest() {
        return this.chest;
    }

    /**
     * Returns the warehouse
     * @return the warehouse
     */
    public WareHouse getWareHouse() {
        return this.wareHouse;
    }

    /**
     * Returns the faith path
     * @return the faith path
     */
    public FaithPath getFaithPath() {
        return this.faithPath;
    }

    /**
     * Returns the victory points
     * @return the victory points
     */
    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    /**
     * Sums new victory points to the player board's victory points
     * @param victoryPoints victory points to add
     */
    public void sumVictoryPoints(int victoryPoints) {
        this.victoryPoints = this.victoryPoints + victoryPoints;
    }

    /**
     * Removes a resource from the warehouse or the chest
     * @param resource resource to remove
     * @param fromWhat indicates if player wants to pick resource from (extra)warehouse or from chest
     * @param quantity how many resources player wants to pick
     */
    public void pickResource(String resource, String fromWhat, int quantity) {

        Integer whRes = this.wareHouse.getWarehouseResources().get(resource);
        Integer chRes = this.chest.getChestResources().get(resource);
        Integer esRes = this.wareHouse.getWarehouseResources().get("extra" + resource);

        fromWhat = fromWhat.toUpperCase();

        switch(fromWhat)
        {
            case "W":
            {
                this.wareHouse.getWarehouseResources().put(resource, whRes - quantity);
                break;
            }
            case "C":
            {
                this.chest.getChestResources().put(resource, chRes - quantity);
                break;
            }
            case "L":
            {
                this.wareHouse.getWarehouseResources().put("extra"+resource, esRes - quantity);
                break;
            }
        }
    }

    /**
     * Adds a white marble perk to the player
     * @param marble the marble perk to add
     */
    public void setResourceMarbles(Marble marble){
        int i = 0;
        while((i < this.resourceMarbles.length) && (this.resourceMarbles[i] != null)){
            i++;
        }
        this.resourceMarbles[i] = marble;
    }

    /**
     * Prints all the components of player's board
     */
    public void printAll(){
        System.out.println("YOUR RESOURCES:");

        System.out.print("COINS   : "+this.getWareHouse().getWarehouseResources().get("COINS")+" in warehouse, "+
                +this.getChest().getChestResources().get("COINS")+" in chest. ");
        if(this.getWareHouse().getWarehouseResources().get("extraCOINS") != null)
            System.out.println(this.getWareHouse().getWarehouseResources().get("extraCOINS") + " in extra space.");
        else System.out.println();

        System.out.print("SERVANTS: "+this.getWareHouse().getWarehouseResources().get("SERVANTS")+" in warehouse, "+
                +this.getChest().getChestResources().get("SERVANTS")+" in chest. ");
        if(this.getWareHouse().getWarehouseResources().get("extraSERVANTS") != null)
            System.out.println(this.getWareHouse().getWarehouseResources().get("extraSERVANTS") + " in extra space.");
        else System.out.println();

        System.out.print("SHIELDS : "+this.getWareHouse().getWarehouseResources().get("SHIELDS")+" in warehouse, "+
                +this.getChest().getChestResources().get("SHIELDS")+" in chest. ");
        if(this.getWareHouse().getWarehouseResources().get("extraSHIELDS") != null)
            System.out.println(this.getWareHouse().getWarehouseResources().get("extraSHIELDS") + " in extra space.");
        else System.out.println();

        System.out.print("STONES  : "+this.getWareHouse().getWarehouseResources().get("STONES")+" in warehouse, "+
                +this.getChest().getChestResources().get("STONES")+" in chest. ");
        if(this.getWareHouse().getWarehouseResources().get("extraSTONES") != null)
            System.out.println(this.getWareHouse().getWarehouseResources().get("extraSTONES") + " in extra space.");
        else System.out.println();

        System.out.println();

        System.out.println("YOUR DEVELOPMENT CARDS: ");
        this.printPlayerCards();
        System.out.println();
        System.out.println("YOUR FAITH PATH POSITION    : "+this.getFaithPath().getCrossPosition());

    }

    /**
     * Prints player development cards
     */
    public void printPlayerCards()
    {
        int[] upper = new int[3];

        for(int col=0; col<3; col++)
        {
            int row;
            for(row=2; row>0; row--)
                if(this.getPlayerBoardDevelopmentCards()[row][col] != null)
                    break;
            if(row==-1)
                row=0;
            upper[col]=row;
        }

        for(int k=0; k<3; k++)
        {
            if(this.getPlayerBoardDevelopmentCards()[upper[k]][k]!=null)
                System.out.print("| "+this.getPlayerBoardDevelopmentCards()[upper[k]][k].printCardProductionPower()+" ");
            else System.out.print("|                       ");
        }
        System.out.print("|");
        System.out.println();

        for(int k=0; k<3; k++) {
            if (this.getPlayerBoardDevelopmentCards()[upper[k]][k] != null) {
                System.out.print("| Victory Points: " + this.getPlayerBoardDevelopmentCards()[upper[k]][k].getVictoryPoints() + "    ");
                if (this.getPlayerBoardDevelopmentCards()[upper[k]][k].getVictoryPoints() < 10)
                    System.out.print(" ");
            } else System.out.print("|                       ");
        }
        System.out.print("|");
        System.out.println();

    }
}
