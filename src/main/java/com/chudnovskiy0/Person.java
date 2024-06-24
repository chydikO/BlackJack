package com.chudnovskiy0;

public abstract class Person {

    /**
     * Приватное поле hand типа Hand. Хранит объект руки (Hand) для данного игрока или дилера.
     */
    private final Hand hand;

    /**
     * Приватное поле name типа String. Хранит имя игрока или дилера.
     */
    private String name;

    /**
     * Приватное поле score типа int. Хранит текущий счет игрока или дилера.
     */
    private int score;

    /**
     * Конструктор класса Person. Создает новый объект руки (Hand),
     * инициализирует имя пустой строкой и устанавливает начальный счет равным 1000.
     */
    public Person() {
        hand = new Hand();
        name = "";
        score = 1000;
    }

    /**
     * Возвращает объект руки (Hand) для данного игрока или дилера.
     * @return
     */
    public Hand getHand() {
        return hand;
    }

    /**
     *  Устанавливает имя игрока или дилера.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает текущий счет игрока или дилера.
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * Устанавливает текущий счет игрока или дилера.
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Выводит на экран текущую руку игрока или дилера, включая список карт и их общее кол-во очков.
     */
    public void printHand() {
        System.out.println(name + "'s hand looks like this:");
        System.out.println(hand + " Valued at: " + hand.calculatedValue());
    }

    /**
     *  Выполняет действие "взять карту" для игрока или дилера. Если в колоде deck нет карт,
     *  перезагружает колоду из отброшенных карт discard. Затем берет карту из колоды и добавляет ее в руку.
     *  Выводит на экран информацию о взятой карте и текущей руке.
     * @param deck
     * @param discard
     */
    public void hit(CardDeck deck, CardDeck discard) {
        if (!deck.hasCards()) {
            deck.reloadDeckFromDiscard(discard);
        }
        hand.takeCardFromDeck(deck);
        System.out.println(name + " gets a card");
        printHand();
        Game.pause();
    }

    /**
     * Проверяет, имеет ли игрок или дилер блэкджек (сумма очков руки равна 21).
     * Возвращает true, если имеет, иначе возвращает false.
     * @return
     */
    public boolean hasBlackjack() {
        return getHand().calculatedValue() == 21;
    }

    /**
     * Возвращает строковое представление объекта Person, включая имя игрока или дилера.
     * @return
     */
    @Override
    public String toString() {
        return "Person:\t" + name;
    }
}
