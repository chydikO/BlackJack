package com.chudnovskiy0;

import java.util.Scanner;

public class Game {

    public static final int BET = 100;
    private final Scanner scanner = new Scanner(System.in);
    private final CardDeck deck;
    private final CardDeck discarded;

    private final Dealer dealer;
    private final Player player;
    private int wins, losses, pushes;

    public Game() {
        deck = new CardDeck(true);
        discarded = new CardDeck();
        dealer = new Dealer();
        player = new Player();

        deck.shuffle();
        game();
    }

    private void game() {
        int action;
        do {
            printWenNewGame();
            printGameInstruction();
            action = scanner.nextInt();
            if (action == 1) {
                startRound();
                if (dealer.getScore() <= 0 || player.getScore() <= 0) {
                    System.out.println("Game stopped....");
                    break;
                }
            }
        } while (action != 2);
        System.out.println(((dealer.getScore() > player.getScore()) ?
                dealer.toString() : player.toString()) + "\t WIN !");
        System.out.println("-= Good Bye =-");
        return;
    }

    private void printGameInstruction() {
        System.out.println("Press 1-> continue, 2-> stop game");
    }


    private void startRound() {

        //If this isn't the first round, display the users score and put their cards back in the deck
/*        if (dealer.getScore() > 0 && player.getScore() > 0) {
            printWenNewGame();
            dealer.printHand();
            player.printHand();
            dealer.getHand().discardHandToDeck(discarded);
            player.getHand().discardHandToDeck(discarded);
        }*/

        cardDistribution();
        showFirstHend();

        //Check if dealer has BlackJack to start
        if (isBlackJacOnTheHands()) {
            return;
        }

        //Check if player has blackjack to start
        //If we got to this point, we already know the dealer didn't have blackjack
        if (isPlayerHasBlackjack()) {
            return;
        }

        //Let the player decide what to do next
        //pass the decks in case they decide to hit
        player.makeDecision(deck, discarded);
        if (player.hasBlackjack()) {
            System.out.println("You win.");
            playerWin();
            discardingCards();
            return;
        }

        //Check if they busted
        if (isPlayerLoss()) {
            return;
        }

        //Now it's the dealer's turn
        dealer.printHand();
        while (dealer.getHand().calculatedValue() < 17) {
            dealer.hit(deck, discarded);
        }

        //Check who wins and count wins or losses
        if (dealer.getHand().calculatedValue() > 21) {
            System.out.println("Dealer busts");
            playerWin();
        } else if (dealer.getHand().calculatedValue() > player.getHand().calculatedValue()) {
            System.out.println("You lose.");
            playerLosses();
        } else if (player.getHand().calculatedValue() > dealer.getHand().calculatedValue()) {
            System.out.println("You win.");
            playerWin();
        } else {
            System.out.println("Push.");
            pushes++;
        }
        discardingCards();
        return;
    }

    private void playerLosses() {
        losses++;
        setScoreWenDealerWin(BET);
    }

    private void playerWin() {
        wins++;
        setScoreWenDealerWin(-BET);
    }

    private boolean isPlayerLoss() {
        if (player.getHand().calculatedValue() > 21) {
            System.out.println("You have gone over 21.");
            playerLosses();
            discardingCards();
            return true;
        }
        return false;
    }

    private boolean isPlayerHasBlackjack() {
        if (player.hasBlackjack()) {
            System.out.println("You have Blackjack! You win!");
            playerWin();
            discardingCards();
            return true;
        }
        return false;
    }

    private boolean isBlackJacOnTheHands() {
        if (dealer.hasBlackjack()) {
            dealer.printHand();
            if (player.hasBlackjack()) {
                System.out.println("You both have 21 - Push.");
                pushes++;
            } else {
                System.out.println("Dealer has BlackJack. You lose.");
                dealer.printHand();
                playerLosses();
            }
            discardingCards();
            return true;
        }
        return false;
    }

    private void discardingCards() {
        dealer.getHand().discardHandToDeck(discarded);
        player.getHand().discardHandToDeck(discarded);
        if (deck.cardsLeft() < 4) {
            deck.reloadDeckFromDiscard(discarded);
        }
    }

    private void printWenNewGame() {
        System.out.println("-".repeat(10) + "\tNew Game\t" + "-".repeat(10));
        System.out.println("Starting Next Round... Wins: " + wins + " Losses: " + losses + " Pushes: " + pushes);
    }

    private void setScoreWenDealerWin(int bet) {
        player.setScore(player.getScore() - bet);
        dealer.setScore(dealer.getScore() + bet);
    }

    private void showFirstHend() {
        dealer.printFirstHand();
        player.printHand();
    }

    private void cardDistribution() {
        for (int i = 0; i < 2; i++) {
            dealer.getHand().takeCardFromDeck(deck);
            player.getHand().takeCardFromDeck(deck);
        }
    }

    /**
     * Pause the game for a short time by putting the thread to sleep
     * We do this to slow down the game slightly, allowing the user to
     * read the cards being dealt and see what's going on... instead of getting
     * a ton of output all at once
     */
    public static void pause() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
