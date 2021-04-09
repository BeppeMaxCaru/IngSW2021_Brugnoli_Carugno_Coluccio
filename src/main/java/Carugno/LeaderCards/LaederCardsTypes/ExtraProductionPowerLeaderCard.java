package Carugno.LeaderCards.LaederCardsTypes;

import Carugno.DevelopmentCards.DevelopmentCard;
import Carugno.LeaderCards.LeaderCard;

import java.util.HashMap;
import java.util.Map;

public class ExtraProductionPowerLeaderCard extends LeaderCard {

    private final DevelopmentCard requisite;
    private final Map<String, Integer> input;
    private final Map<String, Integer> output;

    public ExtraProductionPowerLeaderCard(DevelopmentCard requisite,
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

}
