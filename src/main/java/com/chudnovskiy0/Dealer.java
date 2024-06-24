package com.chudnovskiy0;

public class Dealer extends Person {

    /**
     * Конструктор класса Dealer. Устанавливает имя дилера как "Dealer".
     */
    public Dealer() {
        setName("Dealer");
    }

    /**
     * Выводит на экран первую карту дилера. Вторая карта остается закрытой.
     */
    public void printFirstHand() {
        System.out.println("The dealer's hand looks like this:");
        System.out.println(getHand().getCard(0));
        System.out.println("The second card is face down.\n");
    }

    /**
     * Принимает решение относительно действий дилера в игре. Дилер будет продолжать брать карты (hit) до тех пор,
     * пока сумма очков его руки не достигнет 17 или более,
     * или пока сумма очков его руки равна 17 и у него есть туз (hasAce).
     * @param deck
     * @param discarded
     */
    public void dealerMakeDecision(CardDeck deck, CardDeck discarded) {
        while (getHand().calculatedValue() < 17 || (getHand().calculatedValue() == 17 && getHand().hasAce())) {
            hit(deck, discarded);
        }
    }

}
