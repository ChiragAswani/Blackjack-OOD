package com.blackjack.game;

import com.blackjack.io.InputInterface;
import com.blackjack.cards.Card;
import com.blackjack.cards.PlayingDeck;
import com.blackjack.hands.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The primary event loop for running the game.
 */
public final class GameHandler
{
    private final PlayingDeck deck;
    private InputInterface userInterface;
    private final Player player;
    private final Player dealer;
    private final List<Action> initialActions;
    private Action currentAction;

    public GameHandler(final InputInterface userInterface, final PlayingDeck deck, final Player player )
    {
        this.userInterface = userInterface;
        this.deck = deck;
        this.player = player;
        dealer = new Player(true);

        initialActions =  Arrays.asList(Action.DEAL);
    }

    /**
     * Game loop
     */
    public void run()
    {
        currentAction = Action.RESTART;

        while ( currentAction != Action.QUIT )
        {
            switch ( currentAction )
            {
                case DEAL:
                    deal();
                    break;
                case PLAY:
                    play();
                    break;
                case HIT:
                    hit();
                    break;
                case STAND:
                    player.completeCurrentHand();
                    currentAction = Action.PLAY;
                    showHands();
                    break;
                case SPLIT:
                    player.split( deck.draw(), deck.draw() );
                    showHand(player);
                    currentAction = Action.PLAY;
                    break;
                case DOUBLEDOWN:
                    doubleDown();
                    break;
                case RESTART:
                    userInterface.showText("");
                    currentAction = userInterface.readUserInput( initialActions );
                    break;
            }
        }

    }

    /**
     * Processes a double down action
     */
    private void doubleDown()
    {
        int bet = player.getCurrentHand().getBet();
        Card card = deck.draw();
        outputDraw( player, card );
        player.doubleDown( card, bet );
        currentAction = Action.PLAY;
    }

    /**
     * Processes a deal action
     */
    private void deal()
    {
        shuffle();

        if ( player.getCurrentTotalChips() == 0 )
        {
            userInterface.showText("You are out of money!");
            currentAction = Action.QUIT;
            return;
        }

        int bet = userInterface.firstBet(player.getCurrentTotalChips());
        Hand currentHand = new Hand( Arrays.asList(deck.draw(), deck.draw()), bet );
        player.addHand( currentHand );

        Hand dealerHand = new Hand( Arrays.asList( deck.draw(), deck.draw() ), 0 );
        dealer.addHand( dealerHand );

        showHands();

        currentAction = Action.PLAY;
        // if someone or both have a black jack this round is done
        if ( dealerHand.isBlackJack() || currentHand.isBlackJack() )
        {
            player.completeCurrentHand();
            finalizeHand();
            currentAction = Action.RESTART;
        }
    }

    /**
     * Determines the next action to be processed and sets it
     */
    private void play()
    {
        if ( player.hasActiveHands() )
        {
            List<Action> actions = new ArrayList<Action>();
            actions.add(Action.STAND);

            if ( player.canDraw() ) {
                actions.add( Action.HIT );
            }

            if ( player.canDoubleDown() ) {
                actions.add( Action.DOUBLEDOWN );
            }

            if ( player.canSplit() ) {
                actions.add( Action.SPLIT);
            }
            currentAction = userInterface.readUserInput( actions );
        }
        else
        {
            HandInterface dealerHand = dealer.getCurrentHand();
            while ( dealerHand.eval() < Hand.DEALER_HARD_STOP )
            {
                Card card = deck.draw();
                dealer.addCardToCurrentHand(card);
                outputDraw(dealer, card);
            }

            if ( dealerHand.eval() > Hand.BLACK_JACK )
            {
                userInterface.showText("Dealer busted!");
            }

            finalizeHand();
            currentAction = Action.RESTART;
        }
    }

    /**
     * Processes a hit action
     */
    private void hit()
    {
        Card card = deck.draw();
        player.addCardToCurrentHand(card);
        HandInterface currentHand = player.getCurrentHand();
        outputDraw(player, card);
        if ( currentHand.isBusted() )
        {
            userInterface.showText("You busted!");
            player.completeCurrentHand();
        }
        else if ( currentHand.eval() == Hand.BLACK_JACK )
        {
            player.completeCurrentHand();
        }

        showHands();

        currentAction = Action.PLAY;
    }

    /**
     * Completes a round of play and moves the game to the next round options
     */
    private void finalizeHand()
    {
        dealer.turnOffDealerPrinting();
        userInterface.showText("\nResults:");
        showHands();

        outputHandResults( BetTotaller.tallyUpResults(player, dealer) );
        player.clearHands();
        dealer.clearHands();
    }

    /**
     * Displays the dealer and player's current hands
     */
    private void showHands()
    {
        userInterface.showText( dealer.toString() );
        userInterface.showText( player.toString() );
    }

    /**
     * Displays a hands hand
     *
     * @param player
     */
    private void showHand( Player player )
    {
        userInterface.showText( "\n" + player.toString() );
    }

    /**
     * Shows the card just drawn by a player
     *
     * @param player
     * @param card
     */
    private void outputDraw( Player player, Card card )
    {
        userInterface.showText("%s draw %s", player.Name, card);
    }

    private void outputHandResults( int totalEarnings )
    {
        userInterface.showText("%s %s $%d", player.Name, totalEarnings > 0 ? "won" : "lost", Math.abs(totalEarnings));
    }

    private void shuffle() {
        deck.shuffle();
    }
}
