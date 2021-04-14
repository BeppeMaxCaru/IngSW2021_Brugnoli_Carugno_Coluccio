package Maestri.MVC.Model.GModel.LeaderCards;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCard;
import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.DiscountDevelopmentCardsLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.ExtraProductionPowerLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.ExtraWarehouseSpaceLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.WhiteMarbleResourceLeaderCard;

import java.util.Arrays;
import java.util.List;

public class LeaderCardDeck {
    private LeaderCard[] leaderCardsDeck;

    //inizializza una per una
    public LeaderCardDeck(){
        this.leaderCardsDeck = new LeaderCard[16];

        this.leaderCardsDeck[0] = new DiscountDevelopmentCardsLeaderCard(
                new DevelopmentCard("YELLOW",1),
                new DevelopmentCard("GREEN",1),
                "SERVANTS");
        this.leaderCardsDeck[1] = new DiscountDevelopmentCardsLeaderCard(
                new DevelopmentCard("BLUE",1),
                new DevelopmentCard("PURPLE",1),
                "SHIELDS");
        this.leaderCardsDeck[2] = new DiscountDevelopmentCardsLeaderCard(
                new DevelopmentCard("GREEN",1),
                new DevelopmentCard("BLUE",1),
                "STONES");
        this.leaderCardsDeck[3] = new DiscountDevelopmentCardsLeaderCard(
                new DevelopmentCard("YELLOW",1),
                new DevelopmentCard("PURPLE",1),
                "COINS");

        this.leaderCardsDeck[4] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("YELLOW",2),
                "SHIELDS");
        this.leaderCardsDeck[5] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("BLUE",2),
                "SERVANTS");
        this.leaderCardsDeck[6] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("PURPLE",2),
                "STONES");
        this.leaderCardsDeck[7] = new ExtraProductionPowerLeaderCard(new DevelopmentCard("GREEN",2),
                "COINS");

        this.leaderCardsDeck[8] = new ExtraWarehouseSpaceLeaderCard("COINS","STONES");
        this.leaderCardsDeck[9] = new ExtraWarehouseSpaceLeaderCard("STONES","SERVANTS");
        this.leaderCardsDeck[10] = new ExtraWarehouseSpaceLeaderCard("SERVANTS","SHIELDS");
        this.leaderCardsDeck[11] = new ExtraWarehouseSpaceLeaderCard("SHIELDS","COINS");

        this.leaderCardsDeck[12] = new WhiteMarbleResourceLeaderCard(new DevelopmentCard("YELLOW",1),
                new DevelopmentCard("BLUE",1),
                "SERVANTS");
        this.leaderCardsDeck[13] = new WhiteMarbleResourceLeaderCard(new DevelopmentCard("GREEN",1),
                new DevelopmentCard("PURPLE",1),
                "SHIELDS");
        this.leaderCardsDeck[14] = new WhiteMarbleResourceLeaderCard(new DevelopmentCard("BLUE",1),
                new DevelopmentCard("YELLOW",1),
                "STONES");
        this.leaderCardsDeck[15] = new WhiteMarbleResourceLeaderCard(new DevelopmentCard("PURPLE",1),
                new DevelopmentCard("GREEN",1),
                "COINS");

    }

    public LeaderCard drawOneLeaderCard(){
        LeaderCard leaderCardDrawn = this.leaderCardsDeck[0];
        List<LeaderCard> leaderCardList = Arrays.asList(this.leaderCardsDeck);
        leaderCardList.remove(0);
        this.leaderCardsDeck = leaderCardList.toArray(this.leaderCardsDeck);
        return leaderCardDrawn;
    }
}


