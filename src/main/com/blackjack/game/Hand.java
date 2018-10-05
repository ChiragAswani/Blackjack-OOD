package com.blackjack.game;

import com.blackjack.cards.Card;
import com.blackjack.cards.Rank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a single hand of Black Jack
 */
public final class Hand implements HandInterface
{
    public static final int BLACK_JACK = 21;
    public static final int DEALER_HARD_STOP = 17;

    private List<Card> cards;
    private StringBuffer buffer;
    public boolean IsDealerHand;
    private int bet;
    private boolean isSplit;
    private boolean acesWereSplitAlready;

    public Hand ( List<Card> cards, int bet )
    {
        this.cards = new ArrayList<Card>( cards );
        buffer = new StringBuffer();
        this.bet = bet;
        isSplit = false;
        acesWereSplitAlready = false;
    }

    /**
     * Increases hand by the card passed in
     * @param card
     */
    public void addCard( Card card )
    {
        cards.add( card );
    }

    /**
     * Increases the total bet on the hand, generally as a result of a double down
     * @param additionalBet
     */
    public void addToBet(int additionalBet)
    {
        bet += additionalBet;
    }

    /**
     * Current bet placed on this hand
     *
     * @return
     */
    public int getBet()
    {
        return bet;
    }

    /**
     * Gets the value of the current hand
     *
     * @return total current value
     */
    public int eval()
    {
        int value = 0;
        int aces = 0;
        for( Card card : cards ) {
            if ( card.rank == Rank.ace ) {
                aces++;
            }

            value += card.value();
        }

        // reduce by 10 for each ace until equal or under 21
        for ( int i = 0; i < aces; i++ ) {
            if ( value > BLACK_JACK) {
                value -= Rank.ten.value;
            }
        }

        return value;
    }

    /**
     * The total cards in the current hand
     * @return
     */
    public int getTotalCards()
    {
        return cards.size();
    }

    /**
     * Has the hand exceeded the black jack total
     * @return
     */
    public boolean isBusted()
    {
        return eval() > BLACK_JACK;
    }

    /**
     * Is the hand a BlackJack
     *  1) 2 cards
     *  2) Cards sum to 21
     *  3) The hand is not the result of a split
     * @return
     */
    public boolean isBlackJack()
    {
        if ( cards.size() != 2 || isSplit ) {
            return false;
        }

        int card1Val = cards.get(0).value();
        int card2Val = cards.get(1).value();

        return card1Val + card2Val == BLACK_JACK;
    }

    /**
     * Can the hand be split?
     *  1) Hand consists of 2 cards
     *  2) Hand's two cards are of the same value
     *  3) The current hand is not the result of 2 Ace's previously split
     * @return
     */
    public boolean canSplit()
    {
        return cards.size() == 2 && ( cards.get(0).value() == cards.get(1).value() ) && !acesWereSplitAlready;
    }

    /**
     * Is it legal for the player to draw another card on this hand
     * @return
     */
    public boolean canDraw()
    {
        return ! acesWereSplitAlready;
    }

    /**
     * Splits the hand and returns a new hand consisting of the same bet and just the 2nd card
     *
     * @return new hand
     */
    public Hand split()
    {
        if ( ! canSplit() )
        {
            throw new IllegalStateException("You cannot split this hand.");
        }

        isSplit = true;
        acesWereSplitAlready = this.cards.get(0).rank == Rank.ace && this.cards.get(1).rank == Rank.ace;

        Hand hand = new Hand( Arrays.asList( cards.remove( 1 ) ), bet );
        hand.isSplit = isSplit;
        hand.acesWereSplitAlready = acesWereSplitAlready;

        return hand;
    }

    public String toString()
    {
        int count = cards.size();
        for ( int i = 0; i < count; i++ )
        {
            if ( IsDealerHand && count == 2 && i == 1)
            {
                break;
            }

            buffer.append(cards.get(i)).append("  ");
        }

        buffer.delete(buffer.length() -2, buffer.length() );

        if ( count > 2 || ! IsDealerHand )
        {
            buffer.append(" = ").append(eval());
        }

        String str = buffer.toString();
        buffer.delete(0, buffer.length());
        return str;
    }
}
