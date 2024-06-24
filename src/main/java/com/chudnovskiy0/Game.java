package com.chudnovskiy0;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {

    private final CardDeck deck;
    private final CardDeck discarded;


    private final Dealer dealer;
    private final Player player;
    private int wins, losses, pushes;
    private SoundPlayer soundPlayer;
    private int currentBet = 100;

    public Game() {
        deck = new CardDeck(true);
        discarded = new CardDeck();
        dealer = new Dealer();
        player = new Player();
        soundPlayer = new SoundPlayer();

        deck.shuffle();
        game();
    }

    private void game() {
        int action;

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        // Запуск таймера каждые 2 минуты
        executor.scheduleAtFixedRate(this::increaseBet, 0, 2, TimeUnit.MINUTES);

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

        // Отмена таймера после завершения игры
        executor.shutdown();

        printResultGame();
        printScore();
        exitGame("-= Good Bye =-");
    }

    private void exitGame(String message) {
        soundPlayer.playSound("konec-igry-mar.mp3");
        System.out.println(message);
    }


    private void startRound() {
        cardDistribution();
        dealerShowFirstHand();

        // Проверка на наличие блэкджека у дилера
        if (isBlackJacOnTheHands()) {
            return;
        }

        // Проверка на наличие блэкджека у игрока
        // если мы здесь, то у диллера нет blackjack
        if (isPlayerHasBlackjack()) {
            return;
        }

        // Набор карт игроком
        player.makeDecision(deck, discarded);
        if (player.hasBlackjack()) {
            playerWin("You win.");
            discardingCards();
            return;
        }

        // Проверка на проигрыш игрока
        if (isPlayerLoss()) {
            return;
        }

        // Набор карт дилером
        dealer.printHand();
        dealer.dealerMakeDecision(deck, discarded);

        // Проверка на победу или поражение
        checkWinLoss();
        discardingCards();
    }

    private void checkWinLoss() {
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
    }

    private void playerLosses(String message) {
        System.out.println(message);
        soundPlayer.playSound("game-over-mario.mp3");
        losses++;
        setScoreWenDealerWin(currentBet);
    }

    private void playerWin(String message) {
        System.out.println(message);
        soundPlayer.playSound("fanfary-pobedy-mar.mp3");
        wins++;
        setScoreWenDealerWin(-currentBet);
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

    private void dealerShowFirstHand() {
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
        final String s = " USD";
        System.out.println("Dealer:\t" + dealer.getScore() + s);
        System.out.println("Player:\t" + player.getScore() + s);
    }

    private void printResultGame() {
        StringBuilder result = new StringBuilder();
        final String str = "\t WIN !";
        if ((dealer.getScore() > player.getScore())) {
            result.append(dealer).append(str);
        } else if (dealer.getScore() == player.getScore()) {
            result.append("in this game, everyone remained on their own");
        } else {
            result.append(player.toString()).append(str);
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

    private void increaseBet() {
        currentBet += 50;
        System.out.println("Bet increased to: " + currentBet);
    }

}
