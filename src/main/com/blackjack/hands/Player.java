package com.blackjack.hands;

import com.blackjack.game.Hand;
import com.blackjack.game.HandInterface;
import com.blackjack.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Player {
    private int totalMoney;
    private List<Hand> activeHands;
    private List<HandInterface> completeHands;
    private boolean isDealer;
    private StringBuffer buffer;
    public final String Name;

    public Player()
    {
        this( false );
    }

    public Player(boolean isDealer) {
        this.isDealer = isDealer;
        activeHands = new ArrayList<Hand>();
        completeHands = new ArrayList<HandInterface>();
        totalMoney = isDealer ? 0 : 100;
        buffer = new StringBuffer();
        Name = ! isDealer ? "You" : "Dealer";
    }

    private boolean bet(int chipTotalToBet) {
        if (chipTotalToBet > totalMoney) {
            return false;
        }
        totalMoney -= chipTotalToBet;
        return  true;
    }

    public void addWinnings( int chipsTotal )
    {
        totalMoney += chipsTotal;
    }

    public int getCurrentTotalChips()
    {
        return totalMoney;
    }

    public void clearHands() {
        activeHands.clear();
        completeHands.clear();
    }

    public boolean hasActiveHands()
    {
        return activeHands.size() > 0;
    }

    public void completeCurrentHand() {
        if ( activeHands.size() == 0 ) {
            throw new UnsupportedOperationException("There are no current hands to complete");
        }
        completeHands.add( activeHands.remove(0) );
    }

    public void addCardToCurrentHand(Card card)
    {
        getCurrentHandInternal().addCard(card);
    }

    public void turnOffDealerPrinting() {
        if (! isDealer) {
            throw new UnsupportedOperationException("This player is not a dealer");
        }
        getCurrentHandInternal().IsDealerHand = false;
    }

    public Iterator<HandInterface> getCompleteHands()
    {
        return Collections.unmodifiableList(completeHands).iterator();
    }

    public void addHand( Hand hand ) {
        activeHands.add(hand);
        bet(hand.getBet());

        if (isDealer) {
            hand.IsDealerHand = true;
        }
    }

    public HandInterface getCurrentHand() {
        if ( activeHands.size() == 0 ) {
            throw new IllegalArgumentException("There are no current active hands");
        }

        return activeHands.get(0);
    }

    private Hand getCurrentHandInternal()
    {
        return activeHands.get(0);
    }

    public boolean canDoubleDown() {
        HandInterface hand = getCurrentHand();
        return hand.getTotalCards() == 2 && getCurrentTotalChips() >= getCurrentHand().getBet() && hand.canDraw();
    }

    public void doubleDown(Card card, int bet) {
        if (! canDoubleDown()) {
            throw new IllegalStateException("You cannot double down on this hand.");
        }

        Hand hand = getCurrentHandInternal();
        hand.addToBet(bet);
        hand.addCard(card);
        bet(bet);
        completeCurrentHand();
    }

    public boolean canDraw()
    {
        return getCurrentHand().canDraw();
    }

    public boolean canSplit() {
        HandInterface hand = getCurrentHand();
        return hand.canSplit() && getCurrentTotalChips() >= hand.getBet();
    }

    public void split( Card newCard1, Card newCard2 ) {
        if (! canSplit()) {
            throw new IllegalStateException("You cannot split this hand.");
        }
        Hand newHand = getCurrentHandInternal().split();
        getCurrentHandInternal().addCard(newCard1);
        newHand.addCard( newCard2 );
        activeHands.add( newHand );
        bet( newHand.getBet() );
    }

    @Override
    public String toString() {
        if (isDealer) {
            buffer.append("Dealer Hand: ").append(activeHands.size() > 0 ? activeHands.get(0) : "");
        } else {
            int handPos = 1;
            int totalHands = activeHands.size() + completeHands.size();
            for ( Hand hand : activeHands ) {
                appendHandToString(hand, totalHands, handPos, false );
                handPos++;
            }

            for ( HandInterface hand : completeHands ) {
                appendHandToString(hand, totalHands, handPos, true );
                handPos++;
            }
        }
        String str = buffer.toString();
        buffer.delete(0, buffer.length());
        return str;
    }

    private void appendHandToString(HandInterface hand, int totalCount, int position, boolean played ) {
        buffer.append( "Your Hand: " ).append(hand);
        buffer.append("\n");
    }
}
