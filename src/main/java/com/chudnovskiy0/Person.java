package com.chudnovskiy0;

public abstract class Person {

    private Hand hand;
    private String name;
    private int score;

    public Person() {
        hand = new Hand();
        name = "";
        score = 1000;
    }

    public Hand getHand(){
        return hand;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void printHand() {
        System.out.println(name + "'s hand looks like this:");
        System.out.println(hand + " Valued at: " + hand.calculatedValue());
        System.out.println(score + " USD");
    }

    public void hit(CardDeck deck, CardDeck discard) {
        if (!deck.hasCards()) {
            deck.reloadDeckFromDiscard(discard);
        }
        hand.takeCardFromDeck(deck);
        System.out.println(name + " gets a card");
        printHand();
        Game.pause();
    }

    public boolean hasBlackjack() {
        if (getHand().calculatedValue() == 21) {
            return true;
        } else {
            return false;
        }
    }
}
