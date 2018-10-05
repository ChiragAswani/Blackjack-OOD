package com.blackjack;

import com.blackjack.game.GameHandler;
import com.blackjack.io.InputOutput;
import com.blackjack.cards.Deck;
import com.blackjack.cards.PlayingDeck;
import com.blackjack.hands.Player;

public final class App {
    public static void main(String[] args) {
        InputOutput readInputs = new InputOutput();
        PlayingDeck playingDeck = new Deck();
        new GameHandler(readInputs, playingDeck, new Player()).run();
    }
}
