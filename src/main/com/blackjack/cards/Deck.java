package com.blackjack.cards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public final class Deck implements PlayingDeckInterface {
    public static final int CARDS_IN_A_DECK = 52;

    private ArrayList<Card> cards;
    private Random selector;
    private StringBuffer buffer;
    private Iterator<Card> iterator;

    public Deck() {
        cards = new ArrayList<Card>(CARDS_IN_A_DECK);
        buildDeck();
        selector = new Random(System.nanoTime());

        buffer = new StringBuffer();
    }

    private void buildDeck() {
        int index = 0;
        for ( Rank value : Rank.values()) {
            for ( Suit suit : Suit.values() ) {
                cards.add(index, new Card( suit, value ));
                index++;
            }
        }
    }

    private void resetDeck() {
        iterator = cards.iterator();
    }

    @Override
    public void shuffle()
    {
        for ( int i = 0; i < cards.size(); i++ )
        {
            int toSwap = selector.nextInt(51);
            Card tmpCard = cards.get(i);
            cards.set(i, cards.get(toSwap));
            cards.set(toSwap, tmpCard);
        }

        resetDeck();
    }

    @Override
    public Card draw() {
        if ( ! iterator.hasNext() ) {
            throw new UnsupportedOperationException("There are no more cards to be drawn");
        }

        return iterator.next();
    }

    public String toString() {
        for ( int i = 0; i < CARDS_IN_A_DECK; i++ ) {
            buffer.append(cards.get(i));
            if ( i != CARDS_IN_A_DECK- 1) {
                buffer.append(cards.get(i));
            }
        }
        String str = buffer.toString();
        buffer.delete(0, buffer.length());

        return str;
    }
}
