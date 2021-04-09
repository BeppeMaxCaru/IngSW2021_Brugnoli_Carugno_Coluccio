package Coluccio.Marbles;

import Brugnoli.Playerboard;
import Carugno.LeaderCards.LaederCardsTypes.WhiteMarbleResourceLeaderCard;
import Carugno.LeaderCards.LeaderCard;
import Carugno.LeaderCards.LeaderCardDeck;
import Coluccio.Marble;

public class WhiteMarble extends Marble {

    /**
     * Override of the Marble method drawMarble
     */
    public void drawMarble(LeaderCardDeck leaderCardDeck, Playerboard playerboard) {

        /**
         * Check leaderCardDeck of the player
         */
        for(LeaderCard leaderCard: leaderCardDeck.getLeaderCardsDeck()){
            if((leaderCard.isPlayed())&&(leaderCard instanceof WhiteMarbleResourceLeaderCard))
            {
                /**
                 * If a whiteMarble leaderCard is activated, it is activated its effect
                 */
                leaderCard.activateAbility(playerboard);
            }
            /**
             * If a whiteMarble leaderCard is not activated, white marble has no effects
             */
        }
    }
}
