package com.chudnovskiy0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardDeck {
    private final ArrayList<Card> deck;

    public CardDeck(){
        deck = new ArrayList<>();
    }

    public CardDeck(boolean makeDeck){
        deck = new ArrayList<>();
        if(makeDeck) {
            for(Suit suit : Suit.values()) {
                for(Rank rank : Rank.values()) {
                    deck.add(new Card(suit, rank));
                }
            }
        }
    }

    public void addCards(ArrayList<Card> cards){
        deck.addAll(cards);
    }

    public String toString(){
        StringBuilder output = new StringBuilder();

        for(Card card: deck){
            output.append(card).append("\n");
        }
        return output.toString();
    }

    public void shuffle(){
        Collections.shuffle(deck, new Random());
    }

    public Card takeCard(){
        Card cardToTake = new Card(deck.get(0));
        deck.remove(0);
        return cardToTake;
    }

    public boolean hasCards() {
        return deck.size() > 0;
    }

    public int cardsLeft(){
        return deck.size();
    }

    public ArrayList<Card> getCards() {
        return deck;
    }

    public void emptyDeck(){
        deck.clear();
    }

    public void reloadDeckFromDiscard(CardDeck discard){
        addCards(discard.getCards());
        shuffle();
        discard.emptyDeck();
        System.out.println("Ran out of cards, creating new deck from discard pile & shuffling deck.");
    }

    public static class Card implements Comparable<Card> {

        private final Suit suit;
        private final Rank rank;

        public Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
        }

        public Card(Card card) {
            this.suit = card.getSuit();
            this.rank = card.getRank();
        }

        public int getValue(){
            return rank.rankValue;
        }

        public Suit getSuit(){
            return suit;
        }
        public Rank getRank(){
            return rank;
        }

        public String toString(){
            return ("["+rank+" of "+ suit + "] ("+this.getValue()+")");

        }

        @Override
        public int compareTo(Card c) {
            return Integer.compare(getValue(), c.getValue());
        }
    }
}


