package Carugno.LeaderCards.LaederCardsTypes;

import Brugnoli.Playerboard;
import Carugno.LeaderCards.LeaderCard;

import java.util.HashMap;
import java.util.Map;

public class ExtraWarehouseSpaceLeaderCard extends LeaderCard {

    private final Map<String, Integer> requisite;
    private final Map<String, Integer> resourceSpace;

    public ExtraWarehouseSpaceLeaderCard(String resourceCost, String resourceSpace) {
        super(3);

        this.requisite = new HashMap<>();
        this.requisite.put(resourceCost, 5);

        this.resourceSpace = new HashMap<>();
        this.resourceSpace.put(resourceSpace, 2);
    }

    @Override
    public boolean checkRequisites(Playerboard playerboard) {

    }
}
