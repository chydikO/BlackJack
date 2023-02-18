package com.chudnovskiy0;

import java.util.ArrayList;

public class Hand {

    private final ArrayList<CardDeck.Card> hand;

    public Hand() {
        hand = new ArrayList<>();
    }

    public void takeCardFromDeck(CardDeck deck) {
        hand.add(deck.takeCard());
    }

    public void discardHandToDeck(CardDeck discardDeck) {
        discardDeck.addCards(hand);
        hand.clear();
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (CardDeck.Card card : hand) {
            output.append(card).append(" - ");
        }
        return output.toString();
    }

    public int calculatedValue() {
        int value = 0;
        int aceCount = 0;

        for (CardDeck.Card card : hand) {
            value += card.getValue();
            if (card.getValue() == 11) {
                aceCount++;
            }
        }
        //if we have a scenario where we have multiple aces, as may be the case of drawing 10, followed by two or more aces, (10+11+1 > 21)
        //go back and set each ace to 1 until get back under 21, if possible
        if (value > 21 && aceCount > 0) {
            while (aceCount > 0 && value > 21) {
                aceCount--;
                value -= 10;
            }
        }
        return value;
    }

    public CardDeck.Card getCard(int idx) {
        return hand.get(idx);
    }
}
