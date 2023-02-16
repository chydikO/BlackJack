package com.chudnovskiy0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardDeck {
    private ArrayList<Card> deck;

    public CardDeck(){
        deck = new ArrayList<Card>();
    }

    /**
     * Create a standard deck of cards
     * @param makeDeck makes a standard deck of cards if true
     */
    public Deck(boolean makeDeck){
        deck = new ArrayList<Card>();
        if(makeDeck){
            //Go through all the suits
            for(Suit suit : Suit.values()){
                //Go through all the ranks
                for(Rank rank : Rank.values()){
                    //add a new card containing each iterations suit and rank
                    deck.add(new Card(suit, rank));
                }
            }
        }
    }

    /**
     *
     * @param card The card being added to this deck
     */
    public void addCard(Card card){
        deck.add(card);
    }

    /**
     *
     * @param cards an arraylist of cards to be added to this deck
     */
    public void addCards(ArrayList<Card> cards){
        deck.addAll(cards);
    }

    /**
     *
     * @return Every value of the deck as a String with line separators
     */
    public String toString(){
        //A string to hold everything we're going to return
        String output = "";

        for(Card card: deck){
            output += card;
            output += "\n";
        }
        return output;
    }

    /**
     * Shuffle the deck of Cards at random
     */
    public void shuffle(){
        Collections.shuffle(deck, new Random());
    }

    /**
     *
     * @return The card taken from the deck
     */
    public Card takeCard(){

        //Take a copy of the first card from the deck
        Card cardToTake = new Card(deck.get(0));
        //Remove the card from the deck
        deck.remove(0);
        //Give the card back
        return cardToTake;

    }

    /**
     *
     * @return true if the deck still has cards left
     */
    public boolean hasCards(){
        if (deck.size()>0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     *
     * @return The number of cards left in the deck
     */
    public int cardsLeft(){
        return deck.size();
    }

    /**
     *
     * @return the arraylist containing all the cards in this deck
     */
    public ArrayList<Card> getCards() {
        return deck;
    }

    /**
     * Empties out this Deck
     */
    public void emptyDeck(){
        deck.clear();
    }


    /**
     * Take all the cards from a discarded deck and place them in this deck, shuffled.
     * Clear the old deck
     * @param discard - the deck we're getting the cards from
     */
    public void reloadDeckFromDiscard(Deck discard){
        this.addCards(discard.getCards());
        this.shuffle();
        discard.emptyDeck();
        System.out.println("Ran out of cards, creating new deck from discard pile & shuffling deck.");
    }

    private class Card implements Comparable<Card>{

        private final Suit suit;
        private final Rank rank;

        /**
         *
         * @param suit  The Suit of the card to be created
         * @param rank  The Rank of the card to be created
         */
        public Card(Suit suit, Rank rank){
            this.suit = suit;
            this.rank = rank;
        }

        /**
         * Copy constructor
         * @param card the card being copied
         */
        public Card(Card card){
            this.suit = card.getSuit();
            this.rank = card.getRank();
        }

        /**
         *
         * @return  The numerical value of the Card
         */
        public int getValue(){
            return rank.rankValue;
        }

        /**
         *
         * @return The suit of the Card.
         */
        public Suit getSuit(){
            return suit;
        }
        public Rank getRank(){
            return rank;
        }

        /**
         *
         * @return The Card as a readable string
         */
        public String toString(){
            return ("["+rank+" of "+ suit + "] ("+this.getValue()+")");

        }

        /**
         * Compare a card to another card, returning 1 if this is higher, -1 if this is lower, in case we want to sort cards
         * I didn't talk about this in the tutorial, but left it here, so you can see how it works
         * @param c the card being compared
         * @return 1 if greater, -1 if less, 0 if equal
         */
        @Override
        public int compareTo(Card c) {
            //if this card is greater than the other card
            if(this.getValue() > c.getValue()){
                return 1;
            }
            else if(this.getValue() < c.getValue()){
                return -1;
            }
            else{
                return 0;
            }
        }
    }
}
