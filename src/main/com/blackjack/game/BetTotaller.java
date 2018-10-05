package com.blackjack.game;

import com.blackjack.hands.Player;

import java.util.Iterator;

public final class BetTotaller {
    public static int tallyUpResults( Player player, Player dealer ) {
        Iterator<HandInterface> hands = player.getCompleteHands();
        HandInterface dealerHand = dealer.getCurrentHand();

        int totalWinnings = 0;
        while ( hands.hasNext() ) {
            HandInterface hand = hands.next();
            int bet = hand.getBet();
            int winnings = 0;

            if ( ! hand.isBusted() ) {
                if ( hand.isBlackJack() && ! dealerHand.isBlackJack() ) {
                    winnings = bet;
                    player.addWinnings( bet + winnings );
                    dealer.addWinnings( -winnings );
                } else if ( hand.eval() == dealerHand.eval() ) {
                    player.addWinnings( bet );
                } else if ( dealerHand.isBusted() || hand.eval() > dealerHand.eval() ) {
                    winnings = bet;
                    player.addWinnings( bet * 2 );
                    dealer.addWinnings( -bet );
                } else {
                    winnings = -bet;
                    dealer.addWinnings( bet );
                }
            } else {
                winnings = -bet;
                dealer.addWinnings( bet );
            }
            totalWinnings += winnings;
        }
         return totalWinnings;
    }
}
