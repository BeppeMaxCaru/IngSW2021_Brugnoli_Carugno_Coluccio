package Maestri.MVC.Model.GModel.LeaderCards;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.DiscountDevelopmentCardsLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraProductionPowerLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.ExtraWarehouseSpaceLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardsTypes.WhiteMarbleResourceLeaderCard;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.BlueMarble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.GreyMarble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.PurpleMarble;
import Maestri.MVC.Model.GModel.MarbleMarket.Marbles.YellowMarble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Contains all the available leader cards of the game
 */
public class LeaderCardDeck {

    /**
     * Contains all the initial leader cards
     */
    private LeaderCard[] leaderCardsDeck;

    /**
     * Initializes the entire leader cards starting deck
     */
    public LeaderCardDeck(){
        this.leaderCardsDeck = new LeaderCard[16];

        //Creation of DiscountDevelopmentCardsLeaderCards
        this.leaderCardsDeck[0] = new DiscountDevelopmentCardsLeaderCard("YELLOW",
                "GREEN",
                "SERVANTS", "Masters of Renaissance_Cards_FRONT_3mmBleed_1-49-1.jpg");
        this.leaderCardsDeck[1] = new DiscountDevelopmentCardsLeaderCard(
                "BLUE",
                "PURPLE",
                "SHIELDS", "Masters of Renaissance_Cards_FRONT_3mmBleed_1-50-1.jpg");
        this.leaderCardsDeck[2] = new DiscountDevelopmentCardsLeaderCard(
                "GREEN",
                "BLUE",
                "STONES", "Masters of Renaissance_Cards_FRONT_3mmBleed_1-51-1.jpg");
        this.leaderCardsDeck[3] = new DiscountDevelopmentCardsLeaderCard(
                "YELLOW",
                "PURPLE",
                "COINS", "Masters of Renaissance_Cards_FRONT_3mmBleed_1-52-1.jpg");

        //Creation of ExtraProductionPowerLeaderCards
        this.leaderCardsDeck[4] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW",2),
                "SHIELDS", "Masters of Renaissance_Cards_FRONT_3mmBleed_1-61-1.jpg");
        this.leaderCardsDeck[5] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("BLUE",2),
                "SERVANTS", "Masters of Renaissance_Cards_FRONT_3mmBleed_1-62-1.jpg");
        this.leaderCardsDeck[6] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("PURPLE",2),
                "STONES", "Masters of Renaissance_Cards_FRONT_3mmBleed_1-63-1.jpg");
        this.leaderCardsDeck[7] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("GREEN",2),
                "COINS",  "Masters of Renaissance_Cards_FRONT_3mmBleed_1-64-1.jpg");

        //Creation of ExtraWarehouseSpaceLeaderCards
        this.leaderCardsDeck[8] = new ExtraWarehouseSpaceLeaderCard("COINS","STONES",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.jpg");
        this.leaderCardsDeck[9] = new ExtraWarehouseSpaceLeaderCard("STONES","SERVANTS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.jpg");
        this.leaderCardsDeck[10] = new ExtraWarehouseSpaceLeaderCard("SERVANTS","SHIELDS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-55-1.jpg");
        this.leaderCardsDeck[11] = new ExtraWarehouseSpaceLeaderCard("SHIELDS","COINS",
                "Masters of Renaissance_Cards_FRONT_3mmBleed_1-56-1.jpg");

        //Creation of WhiteMarbleResourceLeaderCards
        this.leaderCardsDeck[12] = new WhiteMarbleResourceLeaderCard("YELLOW",
                "BLUE", new PurpleMarble(), "Masters of Renaissance_Cards_FRONT_3mmBleed_1-57-1.jpg");
        this.leaderCardsDeck[13] = new WhiteMarbleResourceLeaderCard("GREEN",
                "PURPLE", new BlueMarble(), "Masters of Renaissance_Cards_FRONT_3mmBleed_1-58-1.jpg");
        this.leaderCardsDeck[14] = new WhiteMarbleResourceLeaderCard("BLUE",
                "YELLOW", new GreyMarble(), "Masters of Renaissance_Cards_FRONT_3mmBleed_1-59-1.jpg");
        this.leaderCardsDeck[15] = new WhiteMarbleResourceLeaderCard("PURPLE",
                "GREEN", new YellowMarble(), "Masters of Renaissance_Cards_FRONT_3mmBleed_1-60-1.jpg");

        //Instructions for mixing the array of cards, converting it into a list
        List<LeaderCard> cardsList = Arrays.asList(this.leaderCardsDeck);
        Collections.shuffle(cardsList);
        cardsList.toArray(this.leaderCardsDeck);
    }

    /**
     * Returns the initial leader cards deck
     * @return the initial leader cards deck
     */
    public LeaderCard[] getLeaderCardsDeck() {
        return this.leaderCardsDeck;
    }

    /**
     * Shuffles the leader card deck and returns the first leader card
     */
    public LeaderCard drawOneLeaderCard(){
        LeaderCard leaderCardDrawn = this.leaderCardsDeck[0];
        List<LeaderCard> leaderCardList = new ArrayList<>(Arrays.asList(this.leaderCardsDeck));
        leaderCardList.remove(0);
        this.leaderCardsDeck = leaderCardList.toArray(this.leaderCardsDeck);
        return leaderCardDrawn;
    }

}


