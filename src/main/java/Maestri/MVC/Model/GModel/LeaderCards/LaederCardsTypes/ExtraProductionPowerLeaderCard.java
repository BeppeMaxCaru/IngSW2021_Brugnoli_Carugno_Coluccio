package Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes;

import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;

import java.util.*;

public class ExtraProductionPowerLeaderCard extends LeaderCard {

    private final DevelopmentCard requisite;
    private final String input;

    public ExtraProductionPowerLeaderCard(DevelopmentCard requisite, String resourceInput) {
        super(4);
        this.requisite = requisite;//Colore e livello della carta richiesta per l'attivazione
        this.input=resourceInput;//Risorsa in input da scambiare con faithPoint e risorsa a scelta

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

        // Available resources from warehouse
        numWarehouseResources = player.getPlayerboard().getWareHouse().getWarehouseResources().get(this.input);

        // Available resources from chest
        numChestResources = player.getPlayerboard().getChest().getChestResources().get(this.input);

        // one input
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
                    numResources = player.getPlayerboard().getWareHouse().getWarehouseResources().get(input);
                    player.getPlayerboard().getWareHouse().getWarehouseResources().put(input, numResources - 1);
            }
            else{
                numResources = player.getPlayerboard().getChest().getChestResources().get(input);
                player.getPlayerboard().getChest().getChestResources().put(input, numResources - 1);
            }
            i--;
        }

        // output
        while(resourceOutputNum < 0 || resourceOutputNum > 3) {
            System.out.println("Choose one resource: Write 0 for COINS, 1 for SHIELDS, 2 for SERVANTS, 3 for STONES");
            resourceOutputNum = in.nextInt();
        }

        i = 0;
        for (String key : player.getPlayerboard().getChest().getChestResources().keySet()) {
            if(i == resourceOutputNum) {
                numResources = player.getPlayerboard().getChest().getChestResources().get(key);
                player.getPlayerboard().getChest().getChestResources().put(key, numResources + 1);
                break;
            }
            i++;
        }

        player.getPlayerboard().getFaithPath().moveCross(1);

    }

    //Controllo ExtraProd
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
