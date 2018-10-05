package com.blackjack.game;

public enum Action {
    HIT (0), STAND(1), SPLIT(2), DEAL(3), DOUBLEDOWN(4), PLAY(5), RESTART(6), QUIT(7);

    public final int value;

    Action( int value ) {
        this.value = value;
    }

    @Override
    public String toString()
    {
         return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
