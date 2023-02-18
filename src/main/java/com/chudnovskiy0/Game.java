package com.chudnovskiy0;

public class Game {

    public static final int BET = 100;
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
            action = Helper.scanner.nextInt();
            if (action == 1) {
                startRound();
                if (dealer.getScore() <= 0 || player.getScore() <= 0) {
                    System.out.println("Game stopped....");
                    break;
                }
                printScore();
            }
        } while (action != 2);
        printResultGame();
        printScore();
        System.out.println("-= Good Bye =-");
    }


    private void startRound() {
        cardDistribution();
        dealerShowFirstHend();

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
            playerWin("You win.");
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
            playerWin("Dealer busts");
        } else if (dealer.getHand().calculatedValue() > player.getHand().calculatedValue() || dealer.hasBlackjack()) {
            playerLosses("You lose.");
        } else if (player.getHand().calculatedValue() > dealer.getHand().calculatedValue()) {
            playerWin("You win.");
        } else {
            System.out.println("Push.");
            pushes++;
        }
        discardingCards();
    }

    private void playerLosses(String string) {
        System.out.println(string);
        losses++;
        setScoreWenDealerWin(BET);
    }

    private void playerWin(String string) {
        System.out.println(string);
        wins++;
        setScoreWenDealerWin(-BET);
    }

    private boolean isPlayerLoss() {
        if (player.getHand().calculatedValue() > 21) {
            playerLosses("You have gone over 21.");
            discardingCards();
            return true;
        }
        return false;
    }

    private boolean isPlayerHasBlackjack() {
        if (player.hasBlackjack()) {
            playerWin("You have Blackjack! You win!");
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
                dealer.printHand();
                playerLosses("Dealer has BlackJack. You lose.");
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

    private void dealerShowFirstHend() {
        dealer.printFirstHand();
        player.printHand();
    }

    private void cardDistribution() {
        for (int i = 0; i < 2; i++) {
            dealer.getHand().takeCardFromDeck(deck);
            player.getHand().takeCardFromDeck(deck);
        }
    }

    private void printScore() {
        System.out.println("Dealer:\t" + dealer.getScore() + " USD");
        System.out.println("Player:\t" + player.getScore() + " USD");
    }

    private void printResultGame() {
        StringBuilder result = new StringBuilder();
        if ((dealer.getScore() > player.getScore())) {
            result.append(dealer.toString()).append("\t WIN !");
        } else if (dealer.getScore() == player.getScore()) {
            result.append("in this game, everyone remained on their own");
        } else {
            result.append(player.toString()).append("\t WIN !");
        }
        System.out.println(result.toString());
    }

    private void printGameInstruction() {
        System.out.println("Press 1-> continue, 2-> stop game");
    }

    public static void pause() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
