package com.chudnovskiy0;

public enum Suit {
    /**
     * Перечисление Suit. Представляет масти карт в игре. Каждая масть имеет свое имя.
     */
    CLUB("Clubs"),
    DIAMOND("Diamonds"),
    HEART("Hearts"),
    SPADE("Spades");

    /**
     * Поле suitName типа String. Хранит имя масти карты.
     */
    String suitName;

    /**
     * Конструктор перечисления Suit. Инициализирует поле suitName с заданным значением.
     * @param suitName
     */
    Suit(String suitName) {
        this.suitName = suitName;
    }

    /**
     * Возвращает строковое представление масти карты.
     * @return
     */
    public String toString() {
        return suitName;
    }
}
