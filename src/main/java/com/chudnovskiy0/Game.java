package com.chudnovskiy0;

import java.util.Scanner;

/**
 * Contains all Game logic
 */
public class Game {

    //Declare variables needed for Game class
    private Deck deck, discarded;

    private Dealer dealer;
    private Player player;
    private int wins, losses, pushes;



    /**
     * Constructor for Game, creates our variables and starts the Game
     */
    public Game(){

        //Create a new deck with 52 cards
        deck = new Deck(true);
        //Create a new empty deck
        discarded = new Deck();

        //Create the People
        dealer = new Dealer();
        player = new Player();


        //Shuffle the deck and start the first round
        deck.shuffle();
        startRound();
    }


    /**
     * Start a new round, display score, give out cards, check for BlackJack, ask player what they want to do
     */
    private void startRound(){

        //If this isn't the first round, display the users score and put their cards back in the deck
        if(wins>0 || losses>0 || pushes > 0){
            System.out.println();
            System.out.println("Starting Next Round... Wins: " + wins + " Losses: "+ losses+ " Pushes: "+pushes);
            dealer.getHand().discardHandToDeck(discarded);
            player.getHand().discardHandToDeck(discarded);
        }

        //Check to make sure the deck has at least 4 cards left to start
        //Technically this could mean there's 1-3 cards left in the deck that are usable, but we'll just reshuffle them and not worry about it
        if(deck.cardsLeft() < 4){
            //reload the deck from discard pile if we're out of cards
            deck.reloadDeckFromDiscard(discarded);
        }

        //Give the dealer two cards
        dealer.getHand().takeCardFromDeck(deck);
        dealer.getHand().takeCardFromDeck(deck);

        //Give the player two cards
        player.getHand().takeCardFromDeck(deck);
        player.getHand().takeCardFromDeck(deck);

        //Show the dealers hand with one card face down
        dealer.printFirstHand();

        //Show the player's hand
        player.printHand();

        //Check if dealer has BlackJack to start
        if(dealer.hasBlackjack()){
            //Show the dealer has BlackJack
            dealer.printHand();

            //Check if the player also has BlackJack
            if(player.hasBlackjack()){
                //End the round with a push
                System.out.println("You both have 21 - Push.");
                pushes++;
                //start a new round
                startRound();
            }
            else{
                System.out.println("Dealer has BlackJack. You lose.");
                dealer.printHand();
                losses++;
                //player lost, start a new round
                startRound();
            }
        }

        //Check if player has blackjack to start
        //If we got to this point, we already know the dealer didn't have blackjack
        if(player.hasBlackjack()){
            System.out.println("You have Blackjack! You win!");
            wins++;
            startRound();
        }

        //Let the player decide what to do next
        //pass the decks in case they decide to hit
        player.makeDecision(deck, discarded);

        //Check if they busted
        if(player.getHand().calculatedValue() > 21){
            System.out.println("You have gone over 21.");
            losses ++;
            startRound();
        }

        //Now it's the dealer's turn
        dealer.printHand();
        while(dealer.getHand().calculatedValue()<17){
            dealer.hit(deck, discarded);
        }

        //Check who wins and count wins or losses
        if(dealer.getHand().calculatedValue()>21){
            System.out.println("Dealer busts");
            wins++;
        }
        else if(dealer.getHand().calculatedValue() > player.getHand().calculatedValue()){
            System.out.println("You lose.");
            losses++;
        }
        else if(player.getHand().calculatedValue() > dealer.getHand().calculatedValue()){
            System.out.println("You win.");
            wins++;
        }
        else{
            System.out.println("Push.");
            pushes++;
        }

        //Start a new round
        startRound();
    }

    /**
     * Pause the game for a short time by putting the thread to sleep
     * We do this to slow down the game slightly, allowing the user to
     * read the cards being dealt and see what's going on... instead of getting
     * a ton of output all at once
     */
    public static void pause(){
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}





}
