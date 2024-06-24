package com.chudnovskiy0;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Game {
    /**
     * Приватное статическое неизменяемое поле logger типа Logger.
     * Используется для логирования сообщений и ошибок в игре.
     */
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    /**
     * Приватное неизменяемое поле deck типа CardDeck. Хранит колоду карт для игры.
     */
    private final CardDeck deck;

    /**
     * Приватное неизменяемое поле discarded типа CardDeck. Хранит отброшенные карты.
     */
    private final CardDeck discarded;

    /**
     * Приватное неизменяемое поле dealer типа Dealer. Хранит объект дилера в игре.
     */
    private final Dealer dealer;

    /**
     * Приватное неизменяемое поле player типа Player. Хранит объект игрока в игре.
     */
    private final Player player;

    /**
     * Приватные изменяемые поля wins, losses и pushes типа int. Хранят количество побед, поражений и ничьих в игре.
     */
    private int wins, losses, pushes;

    /**
     * Приватное неизменяемое поле soundPlayer типа SoundPlayer. Хранит объект для проигрывания звуковых эффектов в игре.
     */
    private final SoundPlayer soundPlayer;

    /**
     * Приватное изменяемое поле currentBet типа int. Хранит текущую ставку в игре, со значением по умолчанию равным 50.
     */
    private int currentBet = 50;

    /**
     * Приватное изменяемое поле currentBetBeforeRound типа int. Хранит значение текущей ставки перед началом раунда.
     */
    private int currentBetBeforeRound;

    /**
     * Конструктор класса Game. Создает объекты колоды карт, отброшенных карт, дилера, игрока и звукового плеера.
     * Затем перемешивает колоду карт и запускает игру
     */
    public Game() {
        deck = new CardDeck(true);
        discarded = new CardDeck();
        dealer = new Dealer();
        player = new Player();
        soundPlayer = new SoundPlayer();

        deck.shuffle();
        game();
    }

    /**
     * Метод game(). Организует игровой процесс. Создает и запускает таймер для увеличения ставки каждые 2 минуты.
     * В цикле игры запрашивает действие игрока, начинает новый раунд, проверяет условия победы или поражения,
     * обновляет счет и выводит результаты игры. По завершении игры отменяет таймер и выводит сообщение о том,
     * что игра закончилась
     */
    private void game() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        // Запуск таймера каждые 2 минуты
        executor.scheduleAtFixedRate(this::increaseBet, 0, 2, TimeUnit.MINUTES);

        int action;
        do {
            printWenNewGame();
            printGameInstruction();

            action = Helper.scanner.nextInt();
            if (action == 1) {
                currentBetBeforeRound = currentBet; // Сохранение текущего значения currentBet перед началом раунда
                startRound();
                if (dealer.getScore() <= 0 || player.getScore() <= 0) {
                    System.out.println("Game stopped....");
                    break;
                }
                printScore();
                if (currentBetBeforeRound != currentBet) {
                    currentBetBeforeRound = currentBet; // Обновление currentBetBeforeRound, если currentBet изменилось во время раунда
                }
            }
        } while (action != 2);

        // Отмена таймера после завершения игры
        executor.shutdown();

        printResultGame();
        printScore();
        exitGame("-= Good Bye =-");
    }

    /**
     * Начинает новый раунд игры. Проверяет наличие блэкджека у дилера и игрока. Затем игрок делает свой выбор,
     * дилер набирает карты, и происходит проверка на победу или поражение.
     */
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

    /**
     * Проверяет условия победы или поражения. Если сумма очков руки дилера превышает 21, игрок побеждает.
     * Если сумма очков руки дилера больше суммы очков руки игрока или у дилера блэкджек, игрок проигрывает.
     * Если сумма очков руки игрока больше суммы очков руки дилера, игрок побеждает.
     * В противном случае, происходит ничья.
     */
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

    //TODO: обьеденить 3 метода в один, в зависимости от того  что нужно сделать
    /**
     * Выводит сообщение о поражении игрока, воспроизводит звуковой эффект и обновляет счет игры.
     * @param message - текст сообщения
     */
    private void playerLosses(String message) {
        System.out.println(message);
        soundPlayer.playSound("game-over-mario.mp3");
        losses++;
        setScoreWenDealerWin(currentBetBeforeRound);
    }

    /**
     * Выводит сообщение о победе игрока, воспроизводит звуковой эффект и обновляет счет игры.
     * @param message - текст сообщения
     */
    private void playerWin(String message) {
        System.out.println(message);
        soundPlayer.playSound("fanfary-pobedy-mar.mp3");
        wins++;
        setScoreWenDealerWin(-currentBetBeforeRound);
    }

    /**
     * Воспроизводит звуковой эффект и выводит сообщение при завершении игры.
     * @param message
     */
    private void exitGame(String message) {
        soundPlayer.playSound("konec-igry-mar.mp3");
        System.out.println(message);
    }
    //------------------ TODO -----------------------

    /**
     * Проверяет, проиграл ли игрок. Если сумма очков руки игрока превышает 21, игрок проигрывает. Выводит сообщение
     * о том, что игрок превысил 21, вызывает метод playerLosses() для обработки поражения игрока, освобождает карты и
     * возвращает true. В противном случае, возвращает false.
     * @return
     */
    private boolean isPlayerLoss() {
        if (player.getHand().calculatedValue() > 21) {
            playerLosses("You have gone over 21.");
            discardingCards();
            return true;
        }
        return false;
    }

    /**
     * Проверяет, имеет ли игрок блэкджек. Если у игрока есть блэкджек, выводит сообщение о победе игрока,
     * вызывает метод playerWin() для обработки победы игрока, перемещает сыгранные карты в колоду с отбоем
     * и возвращает true. В противном случае,
     * возвращает false.
     * @return
     */
    private boolean isPlayerHasBlackjack() {
        if (player.hasBlackjack()) {
            playerWin("You have Blackjack! You win!");
            discardingCards();
            return true;
        }
        return false;
    }

    /**
     * Проверяет, есть ли блэкджек у дилера и/или игрока. Если у дилера есть блэкджек, выводит его карты и проверяет,
     * есть ли у игрока блэкджек. Если у обоих игроков есть блэкджек, выводит сообщение о ничьей. Если только у дилера
     * есть блэкджек, выводит его карты и вызывает метод playerLosses() для обработки поражения игрока.
     * Затем перемещает сыгранные карты в колоду с отбоем и возвращает true. В противном случае, возвращает false.
     * @return
     */
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

    /**
     * Отбрасывает карты дилера и игрока, в колоду с отбоем.
     * Если в колоде осталось меньше 4 карт, перезагружает колоду из карт которые находятся в колоде с отбоем.
     */
    private void discardingCards() {
        dealer.getHand().discardHandToDeck(discarded);
        player.getHand().discardHandToDeck(discarded);
        if (deck.cardsLeft() < 4) {
            deck.reloadDeckFromDiscard(discarded);
        }
    }

    /**
     *  Выводит сообщение о начале новой игры, отображая текущие победы, поражения и ничьи.
     */
    private void printWenNewGame() {
        System.out.println("-".repeat(10) + "\tNew Game\t" + "-".repeat(10));
        System.out.println("Starting Next Round... Wins: " + wins + " Losses: " + losses + " Pushes: " + pushes);
    }

    /**
     * Обновляет счет игры в зависимости от исхода игры. Если дилер побеждает, уменьшает счет игрока на ставку bet
     * и увеличивает счет дилера на ставку bet.
     * @param bet
     */
    private void setScoreWenDealerWin(int bet) {
        player.setScore(player.getScore() - bet);
        dealer.setScore(dealer.getScore() + bet);
    }

    /**
     * Выводит на экран первую карту дилера и руку игрока.
     */
    private void dealerShowFirstHand() {
        dealer.printFirstHand();
        player.printHand();
    }

    /**
     * Раздает карты дилеру и игроку. Каждому сдаются по 2 карты.
     */
    private void cardDistribution() {
        //TODO: добавить рандом очереди раздачи - кто первый берет карту
        for (int i = 0; i < 2; i++) {
            dealer.getHand().takeCardFromDeck(deck);
            player.getHand().takeCardFromDeck(deck);
        }
    }

    /**
     *  Выводит на экран текущий счет игры, отображая счет дилера и игрока.
     */
    private void printScore() {
        final String s = " USD";
        System.out.println("Dealer:\t" + dealer.getScore() + s);
        System.out.println("Player:\t" + player.getScore() + s);
    }

    /**
     * Выводит на экран результат игры, отображая победителя или сообщение о ничьей.
     */
    private void printResultGame() {
        StringBuilder result = new StringBuilder();
        final String str = "\t WIN !";
        if ((dealer.getScore() > player.getScore())) {
            result.append(dealer).append(str);
        } else if (dealer.getScore() == player.getScore()) {
            result.append("in this game, everyone remained on their own");
        } else {
            result.append(player).append(str);
        }
        System.out.println(result);
    }

    /**
     * Выводит на экран инструкцию для игры, указывая, какие клавиши нужно нажимать для продолжения или остановки игры.
     */
    private void printGameInstruction() {
        System.out.println("Press 1-> continue, 2-> stop game");
    }

    /**
     * Приостанавливает выполнение текущего потока на 5 секунд. Если во время паузы возникает исключение
     * InterruptedException, логирует ошибку с помощью logger и сохраняет состояние прерывания потока.
     */
    public static void pause() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "An error occurred while pausing the thread", e);
            Thread.currentThread().interrupt();        }
    }

    /**
     * Увеличивает текущую ставку на 50 и выводит сообщение о новой ставке.
     */
    private void increaseBet() {
        currentBet += 50;
        System.out.println("Bet increased to: " + currentBet);
    }

}
