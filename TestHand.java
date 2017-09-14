package Poker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by thomas on 03/03/2017.
 *
 * A test class I made to create specific types of hands to test probability calculation and boolean hand functions
 */
public class TestHand {
    private static Random r = new Random();



    //creates a random busted flush for testing probability calculations
    public static HandOfCards makeBustedFlush(){
        PlayingCard[] cards = getFlushCards();
        PlayingCard flushBustingCard = null;
        int randomSuitIndex = r.nextInt(4);
        int indexOfDiscardedCard = r.nextInt(5);

        while(DeckOfCards.suits[randomSuitIndex] == cards[indexOfDiscardedCard].getSuit()){
            randomSuitIndex = r.nextInt(4);
        }

        flushBustingCard = getCard(DeckOfCards.types.get(r.nextInt(13)), DeckOfCards.suits[randomSuitIndex]);

        cards[indexOfDiscardedCard] = flushBustingCard;

        HandOfCards hand = new HandOfCards(cards[0], cards[1], cards[2], cards[3], cards[4]);
        return hand;

    }


    //creates a flush hand to test probabilities
    public static HandOfCards makeFlush(){
        PlayingCard[] cards = getFlushCards();
        HandOfCards hand = new HandOfCards(cards[0], cards[1], cards[2], cards[3], cards[4]);
        return hand;
    }


    //creates an array of 5 playing cards all of the same suit, used to create a flush and broken flush hand
    private static PlayingCard[] getFlushCards(){
        String type = "";
        int typeIndex = 0;
        PlayingCard[] cards = new PlayingCard[5];
        ArrayList<Integer> usedCards = new ArrayList<>();
        char suit = DeckOfCards.suits[r.nextInt(4)];
        for (int i = 0; i < 5; i++) {
            typeIndex = r.nextInt(13);
            while(usedCards.contains(typeIndex)){
                typeIndex = r.nextInt(13);
            }
            cards[i] = getCard(DeckOfCards.types.get(typeIndex), suit);
            usedCards.add(typeIndex);
        }
        return cards;
    }


    //creates a random hand which is one off a straight
    public static HandOfCards makeBustedStraight(){
        PlayingCard[] cards = getStraightCards();
        PlayingCard straightBustingCard = null;
        int randomTypeIndex = r.nextInt(13);
        int indexOfDiscardedCard = r.nextInt(5);

        while(randomTypeIndex == cards[indexOfDiscardedCard].getFaceValue() - 1) {
            randomTypeIndex = r.nextInt(13);
        }
        straightBustingCard = getCard(DeckOfCards.types.get(randomTypeIndex), DeckOfCards.suits[r.nextInt(4)]);

        cards[indexOfDiscardedCard] = straightBustingCard;

        HandOfCards hand = new HandOfCards(cards[0], cards[1], cards[2], cards[3], cards[4]);
        return hand;
    }


    //gets a card with the specified type and suit
    private static PlayingCard getCard(String type, char suit){
        if(type == "A") {
            return new PlayingCard(type, suit, 1, 14);
        } else if(type == "J") {
            return new PlayingCard(type, suit, 11, 11);
        } else if(type == "Q") {
            return new PlayingCard(type, suit, 12, 12);
        } else if(type == "K") {
            return new PlayingCard(type, suit, 13, 13);
        } else {
            return new PlayingCard(type, suit, Integer.parseInt(type), Integer.parseInt(type));
        }
    }

    //creates an array of 5 sequential cards of random suit, used to create straight and busted straight hands
    private static PlayingCard[] getStraightCards(){
        int index = r.nextInt(9);
        PlayingCard[] cards = new PlayingCard[5];
        char suit = 'H';
        for (int i = index; i < index + 5; i++) {
            suit = DeckOfCards.suits[r.nextInt(4)];
            String type = DeckOfCards.types.get(i);
            if(type == "A") {
                cards[i - index] = new PlayingCard(type, suit, 1, 14);
            } else if(type == "J") {
                cards[i - index] = new PlayingCard(type, suit, 11, 11);
            } else if(type == "Q") {
                cards[i - index] = new PlayingCard(type, suit, 12, 12);
            } else if(type == "K") {
                cards[i - index] = new PlayingCard(type, suit, 13, 13);
            } else {
                cards[i - index] = new PlayingCard(type, suit, Integer.parseInt(type), Integer.parseInt(type));
            }
        }
        return cards;
    }


    //creates a random straight hand
    public static HandOfCards makeRandomStraight(){
        PlayingCard[] cards = getStraightCards();
        HandOfCards hand = new HandOfCards(cards[0], cards[1], cards[2], cards[3], cards[4]);
        return hand;
    }
}
