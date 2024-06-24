package com.chudnovskiy0;

/**
 * Contains the Ranks of Cards, Names, and Values
 */
public enum Rank {
    /**
     * Перечисление Rank. Представляет ранги карт в игре. Каждый ранг имеет свое имя и значение.
     */
    ACE("Ace", 11),
    TWO("Two", 2),
    THREE("Three", 3),
    FOUR("Four",4),
    FIVE("Five",5),
    SIX("Six",6),
    SEVEN("Seven",7),
    EIGHT("Eight",8),
    NINE("Nine",9),
    TEN("Ten",10),
    JACK("Jack",10),
    QUEEN("Queen",10),
    KING("King",10);

    /**
     * Поле rankName типа String. Хранит имя ранга карты.
     */
    String rankName;

    /**
     * Поле rankValue типа int. Хранит числовое значение ранга карты.
     */
    int rankValue;

    /**
     * Конструктор перечисления Rank. Инициализирует поля rankName и rankValue с заданными значениями.
     * @param rankName
     * @param rankValue
     */
    Rank(String rankName, int rankValue){
        this.rankName = rankName;
        this.rankValue = rankValue;
    }

    /**
     * Возвращает строковое представление ранга карты.
     * @return
     */
    public String toString(){
        return rankName;
    }
}
