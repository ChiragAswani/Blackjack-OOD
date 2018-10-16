package triantaena;

import com.blackjack.game.Action;
import com.blackjack.game.BetTotaller;
import com.blackjack.game.Hand;
import com.blackjack.game.HandInterface;
import com.blackjack.io.InputInterface;
import com.blackjack.cards.Card;
import com.blackjack.cards.PlayingDeckInterface;
import com.blackjack.hands.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TrientaEnaGameHandler {
    private final PlayingDeckInterface deck;
    private InputInterface userInterface;
    private final List<Player> players;
    private final Player dealer;
    private final List<com.blackjack.game.Action> initialActions;
    private com.blackjack.game.Action currentAction;
    private final Player player;

    public TrientaEnaGameHandler(final InputInterface userInterface, final PlayingDeckInterface deck, final List<Player> players) {
        this.userInterface = userInterface;
        this.deck = deck;
        this.players = players;
        dealer = players.get(0);
        initialActions =  Arrays.asList(com.blackjack.game.Action.DEAL);
        player = new Player();
    }

    public void run() {
        currentAction = com.blackjack.game.Action.RESTART;
        while ( currentAction != com.blackjack.game.Action.QUIT ) {
            switch ( currentAction ) {
                case DEAL:
                    deal();
                    break;
                case HIT:
                    hit();
                    break;
                case STAND:
                    player.completeCurrentHand();
                    currentAction = com.blackjack.game.Action.PLAY;
                    showHands();
                    break;
            }
        }

    }

    private void doubleDown() {
        int bet = player.getCurrentHand().getBet();
        Card card = deck.draw();
        outputDraw( player, card );
        player.doubleDown( card, bet );
        currentAction = com.blackjack.game.Action.PLAY;
    }
    private void deal() {
        shuffle();

        if ( player.getCurrentTotalChips() == 0 )
        {
            userInterface.showText("You are out of money!");
            currentAction = com.blackjack.game.Action.QUIT;
            return;
        }

        int bet = userInterface.firstBet(player.getCurrentTotalChips());
        Hand currentHand = new Hand( Arrays.asList(deck.draw(), deck.draw()), bet );
        player.addHand( currentHand );

        Hand dealerHand = new Hand( Arrays.asList( deck.draw(), deck.draw() ), 0 );
        dealer.addHand( dealerHand );

        showHands();

        currentAction = com.blackjack.game.Action.PLAY;
        if ( dealerHand.isBlackJack() || currentHand.isBlackJack() )
        {
            player.completeCurrentHand();
            finalizeHand();
            currentAction = com.blackjack.game.Action.RESTART;
        }
    }
    private void play() {
        if ( player.hasActiveHands() )
        {
            List<com.blackjack.game.Action> actions = new ArrayList<com.blackjack.game.Action>();
            actions.add(com.blackjack.game.Action.STAND);

            if ( player.canDraw() ) {
                actions.add( com.blackjack.game.Action.HIT );
            }

            if ( player.canDoubleDown() ) {
                actions.add( com.blackjack.game.Action.DOUBLEDOWN );
            }

            if ( player.canSplit() ) {
                actions.add( com.blackjack.game.Action.SPLIT);
            }
            currentAction = userInterface.readUserInput( actions );
        } else {
            HandInterface dealerHand = dealer.getCurrentHand();
            while ( dealerHand.eval() < Hand.dealer_stopping_value)
            {
                Card card = deck.draw();
                dealer.addCardToCurrentHand(card);
                outputDraw(dealer, card);
            }

            if ( dealerHand.eval() > Hand.blackjackVal)
            {
                userInterface.showText("Dealer busted!");
            }

            finalizeHand();
            currentAction = com.blackjack.game.Action.RESTART;
        }
    }

    private void hit() {
        Card card = deck.draw();
        player.addCardToCurrentHand(card);
        HandInterface currentHand = player.getCurrentHand();
        outputDraw(player, card);
        if ( currentHand.isBusted() )
        {
            userInterface.showText("You busted!");
            player.completeCurrentHand();
        }
        else if ( currentHand.eval() == Hand.blackjackVal)
        {
            player.completeCurrentHand();
        }

        showHands();

        currentAction = Action.PLAY;
    }

    private void finalizeHand() {
        dealer.turnOffDealerPrinting();
        userInterface.showText("\nResults:");
        showHands();

        outputHandResults( BetTotaller.tallyUpResults(player, dealer) );
        player.clearHands();
        dealer.clearHands();
    }

    private void showHands() {
        userInterface.showText( dealer.toString() );
        userInterface.showText( player.toString() );
    }

    private void showHand( Player player )
    {
        userInterface.showText( "\n" + player.toString() );
    }

    private void outputDraw( Player player, Card card )
    {
        userInterface.showText("%s draw %s", player.Name, card);
    }

    private void outputHandResults( int totalEarnings ) {
        userInterface.showText("%s %s $%d", player.Name, totalEarnings > 0 ? "won" : "lost", Math.abs(totalEarnings));
    }

    private void shuffle() {
        deck.shuffle();
    }
}

