package com.blackjack.game;

import com.blackjack.hands.Player;

import java.util.Iterator;

/**
 * Handles totalling up winnings/losses from a round of play
 */
public final class BetTotaller
{
    private static double blackJackOdds = 1.5;

    /**
     * Calculates the total net winnings of the hands and updates their chip totals
     *
     * @param player the player
     * @param dealer the dealer
     * @return the total chips the player won or lost
     */
    public static int tallyUpResults( Player player, Player dealer )
    {
        Iterator<HandInterface> hands = player.getCompleteHands();
        HandInterface dealerHand = dealer.getCurrentHand();

        int totalWinnings = 0;
        while ( hands.hasNext() )
        {
            HandInterface hand = hands.next();
            int bet = hand.getBet();
            int winnings = 0;

            if ( ! hand.isBusted() )
            {
                // black jack
                if ( hand.isBlackJack() && ! dealerHand.isBlackJack() )
                {
                    winnings = (int) (bet * blackJackOdds);
                    player.addWinnings( bet + winnings );
                    dealer.addWinnings( -winnings );
                }
                // push
                else if ( hand.eval() == dealerHand.eval() )
                {
                    player.addWinnings( bet );
                }
                // win even odds
                else if ( dealerHand.isBusted() || hand.eval() > dealerHand.eval() )
                {
                    winnings = bet;
                    player.addWinnings( bet * 2 );
                    dealer.addWinnings( -bet );
                }
                // loss
                else
                {
                    winnings = -bet;
                    dealer.addWinnings( bet );
                }
            }
            else // you bust you lose
            {
                winnings = -bet;
                dealer.addWinnings( bet );
            }

            totalWinnings += winnings;
        }

         return totalWinnings;
    }
}
