package Maestri.MVC.Model.GModel.MarbleMarket.Marbles;

import Maestri.MVC.Model.GModel.LeaderCards.LaederCardsTypes.WhiteMarbleResourceLeaderCard;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCard;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.GameModel;

/**
 * White marbles has no effects until the related LeaderCard is activated
 */
public class WhiteMarble extends Marble {

    /**
     * This method checks if the WhiteMarbleLeaderCard is activated.
     * If LeaderCard is activated by the player, the method activates card effect
     * If LeaderCard isn't activated, WhiteMarbles do nothing
     */
    @Override
    public void drawMarble(Player[] players, int playerNumber) {

        //Check leaderCardDeck of the player
        for(LeaderCard leaderCard: players[playerNumber].getPlayerLeaderCards().getLeaderCardsDeck()){
            if((leaderCard instanceof WhiteMarbleResourceLeaderCard)&&(leaderCard.isPlayed()))
            {
                //If a whiteMarble leaderCard is activated, it is activated its effect
                leaderCard.activateAbility(players[playerNumber].getPlayerboard());
            }
            //If a whiteMarble leaderCard is not activated, white marble has no effects
        }
    }
}
