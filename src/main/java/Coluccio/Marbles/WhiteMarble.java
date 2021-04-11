package Coluccio.Marbles;

import Brugnoli.Player;
import Brugnoli.Playerboard;
import Carugno.LeaderCards.LaederCardsTypes.WhiteMarbleResourceLeaderCard;
import Carugno.LeaderCards.LeaderCard;
import Carugno.LeaderCards.LeaderCardDeck;
import Carugno.MVC.GameModel;
import Coluccio.Marble;

/**
 * White marbles has no effects until the related LeaderCard is activated
 */
public class WhiteMarble extends Marble {

    /**
     * Override of the Marble method drawMarble
     */
    @Override
    public void drawMarble(GameModel gameModel, Player player) {

        /**
         * Check leaderCardDeck of the player
         */
        for(LeaderCard leaderCard: player.getPlayerLeaderCards().getLeaderCardsDeck()){
            if((leaderCard.isPlayed())&&(leaderCard instanceof WhiteMarbleResourceLeaderCard))
            {
                /**
                 * If a whiteMarble leaderCard is activated, it is activated its effect
                 */
                leaderCard.activateAbility(player.getPlayerboard());
            }
            /**
             * If a whiteMarble leaderCard is not activated, white marble has no effects
             */
        }
    }
}
