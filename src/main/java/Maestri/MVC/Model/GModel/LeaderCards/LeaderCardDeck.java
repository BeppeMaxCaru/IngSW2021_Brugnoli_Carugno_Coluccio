package Maestri.MVC.Model.GModel.LeaderCards;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.DiscountDevelopmentCardsLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.ExtraProductionPowerLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.ExtraWarehouseSpaceLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.WhiteMarbleResourceLeaderCard;

import java.util.Arrays;
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
                "SERVANTS");
        this.leaderCardsDeck[1] = new DiscountDevelopmentCardsLeaderCard(
                "BLUE",
                "PURPLE",
                "SHIELDS");
        this.leaderCardsDeck[2] = new DiscountDevelopmentCardsLeaderCard(
                "GREEN",
                "BLUE",
                "STONES");
        this.leaderCardsDeck[3] = new DiscountDevelopmentCardsLeaderCard(
                "YELLOW",
                "PURPLE",
                "COINS");

        //Creation of ExtraProductionPowerLeaderCards
        this.leaderCardsDeck[4] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW",2),
                "SHIELDS");
        this.leaderCardsDeck[5] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("BLUE",2),
                "SERVANTS");
        this.leaderCardsDeck[6] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("PURPLE",2),
                "STONES");
        this.leaderCardsDeck[7] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("GREEN",2),
                "COINS");

        //Creation of ExtraWarehouseSpaceLeaderCards
        this.leaderCardsDeck[8] = new ExtraWarehouseSpaceLeaderCard("COINS","STONES");
        this.leaderCardsDeck[9] = new ExtraWarehouseSpaceLeaderCard("STONES","SERVANTS");
        this.leaderCardsDeck[10] = new ExtraWarehouseSpaceLeaderCard("SERVANTS","SHIELDS");
        this.leaderCardsDeck[11] = new ExtraWarehouseSpaceLeaderCard("SHIELDS","COINS");

        //Creation of WhiteMarbleResourceLeaderCards
        this.leaderCardsDeck[12] = new WhiteMarbleResourceLeaderCard("YELLOW",
                "BLUE",
                "SERVANTS");
        this.leaderCardsDeck[13] = new WhiteMarbleResourceLeaderCard("GREEN",
                "PURPLE",
                "SHIELDS");
        this.leaderCardsDeck[14] = new WhiteMarbleResourceLeaderCard("BLUE",
                "YELLOW",
                "STONES");
        this.leaderCardsDeck[15] = new WhiteMarbleResourceLeaderCard("PURPLE",
                "GREEN",
                "COINS");
    }

    /**
     * Shuffles the leader card deck and returns the first leader card
     */
    public LeaderCard drawOneLeaderCard(){
        LeaderCard leaderCardDrawn = this.leaderCardsDeck[0];
        List<LeaderCard> leaderCardList = Arrays.asList(this.leaderCardsDeck);
        leaderCardList.remove(0);
        this.leaderCardsDeck = leaderCardList.toArray(this.leaderCardsDeck);
        return leaderCardDrawn;
    }
}


