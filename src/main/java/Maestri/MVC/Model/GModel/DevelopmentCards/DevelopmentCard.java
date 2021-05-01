package Maestri.MVC.Model.GModel.DevelopmentCards;

import Maestri.MVC.Model.GModel.GameModel;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a development card
 */
public class DevelopmentCard {
    /**
     * Colour of the development card
     */
    private final String colour;
    /**
     * Level of the development card
     */
    private final int level;
    /**
     * Cost of the development card
     */
    private Map<String, Integer> cost;
    /**
     * Input of the production of the development card
     */
    private Map<String, Integer> input;
    /**
     * Output of the production of the development card
     */
    private Map<String, Integer> output;
    /**
     * Faith points received from the production
     */
    private int faithPoints;
    /**
     * Victory points of the card
     */
    private int victoryPoints;

    /**
     * Initializes a partial development card
     * @param colour - colour of the development card
     * @param level - level of the development card
     */
    public DevelopmentCard(String colour, int level) {
        this.colour = colour;
        this.level = level;
    }

    /**
     * Initializes a full development card
     * @param colour - colour of the development card
     * @param level - level of the development card
     * @param coinsCost - coins cost of the development card
     * @param stonesCost - stones cost of the development card
     * @param servantsCost - servants cost of the development card
     * @param shieldsCost - shields cost of the development card
     * @param coinsInput - coins input for the production of the development card
     * @param stonesInput - stones input for the production of the development card
     * @param servantsInput - servants input for the production of the development card
     * @param shieldsInput - shield input for the production of the development card
     * @param coinsOutput - coins output from the production of the development card
     * @param stonesOutput - stones output from the production of the development card
     * @param servantsOutput - servants output from the production of the development card
     * @param shieldsOutput - shields output from the production of the development card
     * @param faithPoints - faith points output from the production of the development card
     * @param victoryPoints - victory points of the development card
     */
    public DevelopmentCard(String colour, int level,
                           int coinsCost, int stonesCost, int servantsCost, int shieldsCost,
                           int coinsInput, int stonesInput, int servantsInput, int shieldsInput,
                           int coinsOutput, int stonesOutput, int servantsOutput, int shieldsOutput,
                           int faithPoints,
                           int victoryPoints) {

        this.colour = colour;
        this.level = level;

        this.cost = new HashMap<String, Integer>();
        this.cost.put("COINS", coinsCost);
        this.cost.put("STONES", stonesCost);
        this.cost.put("SERVANTS", servantsCost);
        this.cost.put("SHIELDS", shieldsCost);

        this.input = new HashMap<String, Integer>();
        this.input.put("COINS", coinsInput);
        this.input.put("STONES", stonesInput);
        this.input.put("SERVANTS", servantsInput);
        this.input.put("SHIELDS", shieldsInput);

        this.output = new HashMap<String, Integer>();
        this.output.put("COINS", coinsOutput);
        this.output.put("STONES", stonesOutput);
        this.output.put("SERVANTS", servantsOutput);
        this.output.put("SHIELDS", shieldsOutput);

        this.faithPoints = faithPoints;
        this.victoryPoints = victoryPoints;

    }

    /**
     * Returns the colour of the development card
     * @return the colour of the development card
     */
    public String getDevelopmentCardColour() {
        return this.colour;
    }

    /**
     * Returns the level of the development card
     * @return the level of the development card
     */
    public int getDevelopmentCardLevel() {
        return this.level;
    }

    /**
     * Returns the cost of the development card
     * @return the cost of the development card
     */
    public Map<String, Integer> getDevelopmentCardCost() {
        return this.cost;
    }

    /**
     * Returns the input for the production of the development card
     * @return the input for the production of the development card
     */
    public Map<String, Integer> getDevelopmentCardInput() {
        return this.input;
    }

    /**
     * Returns the output from the production of the development card
     * @return the output from the production of the development card
     */
    public Map<String, Integer> getDevelopmentCardOutput() {return this.output; }

    /**
     * Returns the faith points from the production of the development card
     * @return the faith points from the production of the development card
     */
    public int getFaithPoints() { return this.faithPoints; }

    /**
     * Returns the victory points of the development card
     * @return the victory points of the development card
     */
    public int getVictoryPoints() {
        return this.victoryPoints;
    }

    /**
     * Checks if the player can place the development card on his player board
     * @param playerboard - player's player board
     * @return true if the player can place the development card on his player board
     */
    public boolean checkPlayerboardDevelopmentCardsCompatibility(Playerboard playerboard) {
        //Gets the player board's spaces where development cards are places
        DevelopmentCard[][] playerDevelopmentCards = playerboard.getPlayerboardDevelopmentCards();
        //Checks if there are empty spaces or cards that are of the same colour of the
        //development card but whose level is one level lower
        if(this.getDevelopmentCardLevel()==1)
        {
            for(int i=0; i<3; i++)
            {
                if(playerDevelopmentCards[this.getDevelopmentCardLevel()-1][i]==null)
                    return true;
            }
        }
        else
        {
            for(int i=0; i<3; i++)
            {
                if((playerDevelopmentCards[this.getDevelopmentCardLevel()-2][i]!=null)&&
                        (playerDevelopmentCards[this.getDevelopmentCardLevel()-1][i]==null))
                    return true;
            }
        }

        System.out.println("Card not compatible with player board cards");
        System.out.println("");
        return false;
    }

    /**
     * Removes resources from the player's player board equals to the development card cost
     * @param playerboard - player's player board
     */
    public void payDevelopmentCard(Playerboard playerboard) {

        //For each cost resource to remove asks the player where to pick it from
        for (String key : this.cost.keySet()) {
            int resourcesToRemove = this.cost.get(key);
            for (int i=0;i<resourcesToRemove;i++) {
                playerboard.pickResource(key);
            }
        }
    }

    public String printCardReq(){
        StringBuilder req= new StringBuilder();
        for(String str:this.cost.keySet())
            if(this.cost.get(str)!=0)
            {
                req.append(this.cost.get(str));
                req.append(str, 0, 3);
                req.append(" ");
            }

        req.append("              ");
        return req.substring(0,16);
    }

    public String printCardProductionPower(){
        StringBuilder req= new StringBuilder();
        for(String str:this.input.keySet())
            if(this.input.get(str)!=0)
            {
                req.append(this.input.get(str));
                req.append(str, 0, 2);
                req.append(" ");
            }
        req.append("} ");
        for(String str:this.output.keySet())
            if(this.output.get(str)!=0)
            {
                req.append(this.output.get(str));
                req.append(str, 0, 2);
                req.append(" ");
            }

        req.append("                     ");
        return req.substring(0,21);
    }

}
