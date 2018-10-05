package com.blackjack.game;

import com.blackjack.cards.Card;
import com.blackjack.cards.Rank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class Hand implements HandInterface {
    public static final int blackjackVal = 21;
    public static final int dealer_stopping_value = 17;

    private List<Card> cards;
    private StringBuffer buffer;
    public boolean IsDealerHand;
    private int bet;
    private boolean isSplit;
    private boolean acesWereSplitAlready;

    public Hand ( List<Card> cards, int bet ) {
        this.cards = new ArrayList<Card>( cards );
        buffer = new StringBuffer();
        this.bet = bet;
        isSplit = false;
        acesWereSplitAlready = false;
    }

    public void addCard( Card card )
    {
        cards.add( card );
    }

    public void addToBet(int additionalBet)
    {
        bet += additionalBet;
    }

    public int getBet()
    {
        return bet;
    }

    public int eval() {
        int value = 0;
        int aces = 0;
        for( Card card : cards ) {
            if ( card.rank == Rank.ace ) {
                aces++;
            }

            value += card.value();
        }

        for ( int i = 0; i < aces; i++ ) {
            if ( value > blackjackVal) {
                value -= Rank.ten.value;
            }
        }

        return value;
    }

    public int getTotalCards()
    {
        return cards.size();
    }

    public boolean isBusted()
    {
        return eval() > blackjackVal;
    }

    public boolean isBlackJack() {
        if ( cards.size() != 2 || isSplit ) {
            return false;
        }

        int card1Val = cards.get(0).value();
        int card2Val = cards.get(1).value();

        return card1Val + card2Val == blackjackVal;
    }
    public boolean canSplit() {
        return cards.size() == 2 && ( cards.get(0).value() == cards.get(1).value() ) && !acesWereSplitAlready;
    }
    public boolean canDraw()
    {
        return ! acesWereSplitAlready;
    }

    public Hand split() {
        if ( ! canSplit() ) {
            throw new IllegalStateException("You cannot split this hand.");
        }

        isSplit = true;
        acesWereSplitAlready = this.cards.get(0).rank == Rank.ace && this.cards.get(1).rank == Rank.ace;

        Hand hand = new Hand( Arrays.asList( cards.remove( 1 ) ), bet );
        hand.isSplit = isSplit;
        hand.acesWereSplitAlready = acesWereSplitAlready;

        return hand;
    }

    public String toString() {
        int count = cards.size();
        for ( int i = 0; i < count; i++ ) {
            if ( IsDealerHand && count == 2 && i == 1) {
                break;
            }

            buffer.append(cards.get(i)).append("  ");
        }

        buffer.delete(buffer.length() -2, buffer.length() );

        if ( count > 2 || ! IsDealerHand ) {
            buffer.append(" = ").append(eval());
        }

        String str = buffer.toString();
        buffer.delete(0, buffer.length());
        return str;
    }
}
