package com.chudnovskiy0;

import java.util.ArrayList;

public class Hand {
    /**
     * Приватное поле hand типа ArrayList<CardDeck.Card>. Хранит список карт в руке.
     */
    private final ArrayList<CardDeck.Card> hand;

    /**
     * Конструктор класса Hand. Создает пустую руку.
     */
    public Hand() {
        hand = new ArrayList<>();
    }

    /**
     * Берет карту из колоды deck и добавляет ее в руку игроку или дилеру.
     * @param deck
     */
    public void takeCardFromDeck(CardDeck deck) {
        hand.add(deck.takeCard());
    }

    /**
     * Отбрасывает карты из руки игрока или дилера в колоду с отбоем discardDeck
     * и очищает карты, которые находились в руке.
     * @param discardDeck
     */
    public void discardHandToDeck(CardDeck discardDeck) {
        discardDeck.addCards(hand);
        hand.clear();
    }

    /**
     * Возвращает строковое представление руки, перечисляя карты через дефис.
     * @return
     */
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (CardDeck.Card card : hand) {
            output.append(card).append(" - ");
        }
        return output.toString();
    }

    /**
     * Вычисляет сумму очков руки. Сначала суммирует значения всех карт в руке. Затем, если сумма превышает 21 и в
     * руке есть тузы, уменьшает значение каждого туза на 10, пока сумма не станет меньше или равной 21.
     * @return
     */
    public int calculatedValue() {
        int value = 0;
        int aceCount = 0;

        for (CardDeck.Card card : hand) {
            value += card.getValue();
            if (card.getValue() == 11) {
                aceCount++;
            }
        }
// Если у нас такая ситуация, когда у нас есть несколько тузов, например, если мы получили 10, а затем два или больше
// тузов, (10+11+1 > 21), то мы вернемся назад и установим каждый туз в 1, пока не получим сумму меньше 21,
// если это возможно.
        if (value > 21 && aceCount > 0) {
            while (aceCount > 0 && value > 21) {
                aceCount--;
                value -= 10;
            }
        }
        return value;
    }

    /**
     *
     * @param idx
     * @return Возвращает карту из руки по указанному индексу idx.
     */
    public CardDeck.Card getCard(int idx) {
        return hand.get(idx);
    }

    /**
     * Проверяет, есть ли в руке туз. Возвращает true, если в руке есть туз, иначе возвращает false.
     * @return
     */
    public boolean hasAce() {
        for (CardDeck.Card card : hand) {
            if (card.getRank() == Rank.ACE) {
                return true;
            }
        }
        return false;
    }
}
