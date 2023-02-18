package com.chudnovskiy0;

public class Player extends Person {

    public Player() {
        setName("Player");
    }

    public void makeDecision(CardDeck deck, CardDeck discard) {
        int decision = 0;

        do {
            try {
                System.out.println("Would you like to: 1-> Hit or 2-> Stand");
                decision = Helper.scanner.nextInt();
            } catch (Exception e) {
                System.err.println("Invalid input");
                Helper.scanner.next();
            }
            if (decision == 1) {
                hit(deck, discard);
            }
        } while (decision != 2 && getHand().calculatedValue() < 20);
        System.out.println("You stand.");
    }
}
