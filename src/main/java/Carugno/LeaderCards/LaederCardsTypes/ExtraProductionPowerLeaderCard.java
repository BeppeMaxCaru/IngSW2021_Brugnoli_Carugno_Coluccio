package Carugno.LeaderCards.LaederCardsTypes;

import Brugnoli.Playerboard;
import Carugno.DevelopmentCards.DevelopmentCardWithErrors;
import Carugno.LeaderCards.LeaderCard;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ExtraProductionPowerLeaderCard extends LeaderCard {

    private final DevelopmentCardWithErrors requisite;
    private final Map<String, Integer> input;
    private final Map<String, Integer> output;

    Scanner consoleInput = new Scanner(System.in);
    String resource1 = consoleInput.nextLine();
    String resource2 = consoleInput.nextLine();


    public ExtraProductionPowerLeaderCard(DevelopmentCardWithErrors requisite,
                                          String resourceInput) {
        super(4);

        this.requisite = requisite;

        this.input = new HashMap<>();
        this.input.put(resourceInput, 1);

        this.output = new HashMap<>();
        this.output.put("FAITHPOINTS", 1);

        //chiedi all'utente che risorsa vuole
        //bisogna mettere cotrollo sulla risorsa richiesta
        //si può utilizzare risorse del marcato?

    }

    @Override
    public void activateAbility(Playerboard playerboard) {
        System.out.println("Choose first resource to trade: ");
        resource1 = consoleInput.nextLine();
        while (!(playerboard.getWareHouse().getWarehouseResources().containsKey(resource1) ||
                (playerboard.getChest().getChestResources().containsKey(resource1)))) {
            System.out.println("Resource missing -> choose another first resource to trade: ");
            resource1 = consoleInput.nextLine();
        }
        System.out.println("Choose place from where to pick resource: ");

        System.out.println("Choose second resource to trade: ");
        resource2 = consoleInput.nextLine();
        while (!((playerboard.getWareHouse().getWarehouseResources().containsKey(resource2)) ||
                playerboard.getChest().getChestResources().containsKey(resource2))) {
            System.out.println(("Resource missing -> choose another second resource to trade: "));
            resource2 = consoleInput.nextLine();
        }

    }
}
