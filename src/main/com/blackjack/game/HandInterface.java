package com.blackjack.game;


public interface HandInterface {
    int eval();
    boolean isBusted();
    boolean isBlackJack();
    int getBet();
    int getTotalCards();
    boolean canDraw();
    boolean canSplit();
}
