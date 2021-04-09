package Coluccio;

import Brugnoli.Playerboard;
import Carugno.LeaderCards.LeaderCard;
import Carugno.LeaderCards.LeaderCardDeck;

public class WhiteMarble extends Marble {

    /**
     * Override of the Marble method drawMarble
     */
    public void drawMarble(LeaderCardDeck leaderCardDeck, Playerboard playerboard) {

        /**
         * Check leaderCardDeck of the player
         */
        for(LeaderCard leaderCard: leaderCardDeck){
            if((leaderCard.isPlayed())&&(/*leaderCard.LeaderCardsType==WhiteMarbleLeaderCards*/))
            {
                /**
                 * If a whiteMarble leaderCard is activated, it is activated its effect
                 */
                leaderCard.activateAbility(Playerboard playerboard);
            }
            /**
             * If a whiteMarble leaderCard is not activated, white marbles has no effects
             */
        }
    }
}
