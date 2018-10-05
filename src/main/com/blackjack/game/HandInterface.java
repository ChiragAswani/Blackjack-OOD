package com.blackjack.game;

/**
 * Interface for interacting with hands in a non-mutating way
 */
public interface HandInterface
{
    int eval();
    boolean isBusted();
    boolean isBlackJack();
    int getBet();
    int getTotalCards();
    boolean canDraw();
    boolean canSplit();
}
