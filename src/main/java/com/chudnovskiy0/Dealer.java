package com.chudnovskiy0;

public class Dealer extends Person {

    public Dealer() {
        setName("Dealer");
    }

    public void printFirstHand() {
        System.out.println("The dealer's hand looks like this:");
        System.out.println(getHand().getCard(0));
        System.out.println("The second card is face down.\n");
    }

    public void dealerMakeDecision(CardDeck deck, CardDeck discarded) {
        /*do {
            hit(deck, discarded);
        } while (getHand().calculatedValue() < 17);*/
        while (getHand().calculatedValue() < 17 || (getHand().calculatedValue() == 17 && getHand().hasAce())) {
            hit(deck, discarded);
        }
    }

}
