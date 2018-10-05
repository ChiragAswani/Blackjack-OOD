package com.blackjack.game;

/**
 * Models the various actions that can be taken in the game
 */
public enum Action
{
    HIT (0),
    STAND(1),
    SPLIT(2),
    DEAL(3),
    DOUBLEDOWN(4),
    PLAY(5),
    RESTART(6),
    QUIT(7);

    public final int value;
    private final String str;

    Action( int value )
    {
        this.value = value;
        str = name().charAt(0) + name().substring(1).toLowerCase();
    }

    @Override
    public String toString()
    {
         return str;
    }
}
