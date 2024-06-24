package com.chudnovskiy0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardDeck {
    /**
     * Приватное неизменяемое поле deck типа ArrayList<Card>. Хранит колоду карт.
     */
    private final ArrayList<Card> deck;

    /**
     * Конструктор класса CardDeck. Создает пустую колоду карт.
     */
    public CardDeck(){
        deck = new ArrayList<>();
    }

    /**
     * Конструктор класса CardDeck с параметром makeDeck.
     * Если makeDeck равно true, создает колоду карт, добавляя все возможные комбинации масти
     * и достоинства.
     * @param makeDeck
     */
    public CardDeck(boolean makeDeck){
        deck = new ArrayList<>();
        if(makeDeck) {
            for(Suit suit : Suit.values()) {
                for(Rank rank : Rank.values()) {
                    deck.add(new Card(suit, rank));
                }
            }
        }
    }

    /**
     * Добавляет список карт cards в колоду.
     * @param cards
     */
    public void addCards(ArrayList<Card> cards){
        deck.addAll(cards);
    }

    /**
     * Возвращает строковое представление колоды карт.
     * @return
     */
    public String toString(){
        StringBuilder output = new StringBuilder();

        for(Card card: deck){
            output.append(card).append("\n");
        }
        return output.toString();
    }

    /**
     * Перемешивает карты в колоде.
     */
    public void shuffle(){
        Collections.shuffle(deck, new Random());
    }

    /**
     * Берет карту из верхней части колоды и удаляет ее из колоды, затем возвращает эту карту.
     * @return
     */
    public Card takeCard(){
        Card cardToTake = new Card(deck.get(0));
        deck.remove(0);
        return cardToTake;
    }

    /**
     * Проверяет, есть ли карты в колоде.
     * @return
     */
    public boolean hasCards() {
        return deck.size() > 0;
    }

    /**
     *
     * @return Возвращает количество оставшихся карт в колоде.
     */
    public int cardsLeft(){
        return deck.size();
    }

    /**
     *
     * @return Возвращает список карт в колоде.
     */
    public ArrayList<Card> getCards() {
        return deck;
    }

    /**
     * Очищает колоду, удаляя все карты.
     */
    public void emptyDeck(){
        deck.clear();
    }

    /**
     * Перезагружает колоду из отброшенных карт (discard). Добавляет карты из отброшенной колоды в текущую колоду,
     * перемешивает карты и очищает отброшенную колоду. Выводит сообщение о том, что карты закончились и колода была
     * создана заново из отброшенных карт.
     * @param discard
     */
    public void reloadDeckFromDiscard(CardDeck discard){
        addCards(discard.getCards());
        shuffle();
        discard.emptyDeck();
        System.out.println("<-= Ran out of cards, creating new deck from discard pile & shuffling deck. =->");
    }

    /**
     * Вложенный класс Card, представляющий отдельную карту.
     * Реализует интерфейс Comparable<Card> для сравнения карт по их значению.
     */
    public static class Card implements Comparable<Card> {

        private final Suit suit;
        private final Rank rank;

        public Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
        }

        public Card(Card card) {
            this.suit = card.getSuit();
            this.rank = card.getRank();
        }

        /**
         *
         * @return Возвращает значение карты.
         */
        public int getValue(){
            return rank.rankValue;
        }


        /**
         *
         * @return Возвращает масть карты.
         */
        public Suit getSuit(){
            return suit;
        }

        /**
         * Возвращает достоинство карты.
         * @return
         */
        public Rank getRank(){
            return rank;
        }

        /**
         *
         * @return Возвращает строковое представление карты в формате [достоинство of масть].
         */
        public String toString(){
            return ("["+rank+" of "+ suit + "] ("+ this.getValue() +")");

        }

        /**
         * Реализация метода интерфейса Comparable<Card> для сравнения карт по их значению.
         * @param c карта - масть достоинство.
         * @return
         */
        @Override
        public int compareTo(Card c) {
            return Integer.compare(getValue(), c.getValue());
        }
    }
}


