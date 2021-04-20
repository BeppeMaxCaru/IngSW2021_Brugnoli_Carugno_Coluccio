package Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;

import java.util.*;

/**
 * These LeaderCards give an extra Production Power to the player
 * He spends a specific resource and he obtains a faithPoint and a Resource to be chosen
 */
public class ExtraProductionPowerLeaderCard extends LeaderCard {

    /**
     * DevelopmentCard required for the activation
     */
    private final DevelopmentCard requisite;

    /**
     * Resource to be spent
     */
    private final String input;

    /**
     * Constructor associates inputs by LeaderCardDeck to attributes of the class
     */
    public ExtraProductionPowerLeaderCard(DevelopmentCard requisite, String resourceInput) {
        super(4);
        this.requisite = requisite;
        this.input=resourceInput;

    }

    @Override
    public void activateAbility(Player player) {

        int numWarehouseResources;
        int numChestResources;
        int fromWhat = -1;
        int resourceOutputNum = -1;
        int numResources;
        int i = 1;

        Scanner in = new Scanner(System.in);

        numWarehouseResources = player.getPlayerBoard().getWareHouse().getWarehouseResources().get(this.input);
        numChestResources = player.getPlayerBoard().getChest().getChestResources().get(this.input);

        while(i > 0) {
            if((numChestResources>0)&&(numWarehouseResources>0))
            {
                while(fromWhat != 0 && fromWhat != 1) {
                    System.out.println("Do you want to choose the resource from warehouse or from chest? Write 0 for warehouse or 1 for chest:");
                    fromWhat = in.nextInt();
                }
            }
            else if (numChestResources==0)
                fromWhat=0;
            else
                fromWhat=1;

            if(fromWhat == 0 ) {
                    numResources = player.getPlayerBoard().getWareHouse().getWarehouseResources().get(input);
                    player.getPlayerBoard().getWareHouse().getWarehouseResources().put(input, numResources - 1);
            }
            else{
                numResources = player.getPlayerBoard().getChest().getChestResources().get(input);
                player.getPlayerBoard().getChest().getChestResources().put(input, numResources - 1);
            }
            i--;
        }

        while(resourceOutputNum < 0 || resourceOutputNum > 3) {
            System.out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
            resourceOutputNum = in.nextInt();
        }

        i = 0;
        for (String key : player.getPlayerBoard().getChest().getChestResources().keySet()) {
            if(i == resourceOutputNum) {
                numResources = player.getPlayerBoard().getChest().getChestResources().get(key);
                player.getPlayerBoard().getChest().getChestResources().put(key, numResources + 1);
                break;
            }
            i++;
        }

        player.getPlayerBoard().getFaithPath().moveCross(1);

        this.setPlayed(true);
    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {
        boolean check=false;
        for(int i=0; i<3; i++)
        {
            if (playerboard.getPlayerboardDevelopmentCards()[this.requisite.getDevelopmentCardLevel()-1][i].getDevelopmentCardColour().equals(this.requisite.getDevelopmentCardColour()))
                check=true;
        }
        return check;
    }
}
