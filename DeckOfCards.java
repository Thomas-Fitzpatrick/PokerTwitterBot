package Poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
/**
 * Created by The Harlem Codetrotters on 02/02/2017.
 */
public class DeckOfCards {
    public static final ArrayList<String> types = new ArrayList<String>(Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10",  "J", "Q", "K"));
    public static final char[] suits = {'C', 'D', 'H', 'S'};
    private static final int DECK_ORIGINAL_SIZE = types.size()*suits.length;
    private int top = 0;

    private ArrayList<PlayingCard> deck = new ArrayList<>();

    public DeckOfCards(){
        initialize();
    }

    //method for initializing the deck by creating 52 new Playing card objects to fill it
    private void initialize(){
        for (String type : types) {
            for (Character suit: suits) {
                if(type == "A") {
                    deck.add(new PlayingCard(type, suit, 1, 14));
                } else if(type == "J") {
                    deck.add(new PlayingCard(type, suit, 11, 11));
                } else if(type == "Q") {
                    deck.add(new PlayingCard(type, suit, 12, 12));
                } else if(type == "K") {
                    deck.add(new PlayingCard(type, suit, 13, 13));
                } else {
                    deck.add(new PlayingCard(type, suit, Integer.parseInt(type), Integer.parseInt(type)));
                }
            }
        }
    }


    //Resets deck by wiping it and re-initializing
    public void reset(){
        deck.clear();
        initialize();
        top = 0;
        shuffle();
    }


    //shuffles deck by selecting random pairs and swapping them
    public void shuffle(){
        Random r = new Random();
        int times = DECK_ORIGINAL_SIZE*DECK_ORIGINAL_SIZE;
        for(int i = 0; i < times; i++){
            int firstIndex = r.nextInt(deck.size());
            int secondIndex = r.nextInt(deck.size());

            PlayingCard temp = deck.get(firstIndex);
            deck.set(firstIndex, deck.get(secondIndex));
            deck.set(secondIndex, temp);
        }
    }


    //Deals the next card in the deck, if deck is empty discarded cards are shuffled and added back in
    public PlayingCard dealNext(){
        PlayingCard nextCard = deck.get(top);
        top++;
        return nextCard;
        //we considered the problem of the deck being empty on draw and came to the conclusion
        //that it will never occur in our game.
    }

    //returns a string of all cards remaining in the deck
    @Override
    public String toString() {
        String output = "";
        int i = 0;
        for(PlayingCard card: deck){
            i++;
            output += card + "\t";
            if(i % 4 == 0){
                output += "\n";
            }
        }
        return output;
    }
}
