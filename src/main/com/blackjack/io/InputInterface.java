package com.blackjack.io;

import com.blackjack.game.Action;

import java.io.Closeable;
import java.util.List;

public interface InputInterface {
    void showText(String format, Object... args);
    int firstBet(final int bet);
    Action readUserInput(final List<Action> actions);
}
