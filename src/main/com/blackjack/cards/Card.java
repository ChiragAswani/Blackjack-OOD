package com.blackjack.cards;

public final class Card {
    public static final int FACE_VALUE = 10;

    public final Suit suit;
    public final Rank rank;

    public Card( Suit suit, Rank rank ) {
        this.suit = suit;
        this.rank = rank;
    }

    public int value()
    {
        return rank.isFace() ? FACE_VALUE : rank.value;
    }

    public String toString(){
        if (this.suit.toString() == "SPADES"){
            return ((char)'\u2660')+ this.rank.toString() + ((char)'\u2660');
        } else if (this.suit.toString() == "DIAMONDS"){
            return ((char)'\u2666')+ this.rank.toString() + ((char)'\u2666');
        } else if (this.suit.toString() == "CLUBS"){
            return ((char)'\u2663')+ this.rank.toString() + ((char)'\u2663');
        } else { //"HEARTS"
            return ((char)'\u2764')+ this.rank.toString() + ((char)'\u2764');
        }
    }
}
