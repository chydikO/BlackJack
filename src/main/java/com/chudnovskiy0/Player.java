package com.chudnovskiy0;

public class Player extends Person {

    /**
     * Конструктор класса Player. Создает новый объект игрока и устанавливает имя игрока как "Player".
     */
    public Player() {
        setName("Player");
    }

    /**
     * Позволяет игроку принять решение о действии в игре.
     * Запрашивает у игрока ввод: 1 для "Hit" (взять карту) или 2 для "Stand" (остаться).
     * Если игрок выбирает "Hit", вызывается метод hit() для взятия карты из колоды.
     * Продолжает запрашивать ввод, пока игрок не выберет "Stand" или сумма очков его руки не превысит 20.
     * Затем выводит сообщение, что игрок останавливается.
     * @param deck
     * @param discard
     */
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
