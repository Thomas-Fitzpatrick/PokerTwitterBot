package Poker;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Thomas Fitzpatrick on 29/01/2017.
 * Student Number 13443402
 *
 * Just a simple interface for testing the Gamestate and Poker game functionality in the console rather than
 * having to test through twitter only.
 */


public class ConsoleGame {

    public static void main(String[] args) {

        HashMap<String, GameState> currentGames = new HashMap<>();


        System.out.println(currentGames);




        String userName = "Thomas";
        boolean isGameStart = true;
        String response = "";

        for(;;){

            Scanner reader = new Scanner(System.in);  // Reading from System.in
            System.out.println("Your tweet: ");


            if (isGameStart && currentGames.containsKey(userName)) {
                System.out.println("@" + userName + "\n" + " your response was invalid, you are already in a game of poker. Try again.");
            } else if (isGameStart && !currentGames.containsKey(userName)) {
                GameState gameState = new GameState(userName);
                currentGames.put(userName, gameState);

                String output = "Welcome to THC!\n";
                output += gameState.nextPartialRoundOutput(response + "Yes");
                isGameStart = false;
                System.out.println(output);
            } else if (currentGames.containsKey(userName)) {
                String nextTweet = currentGames.get(userName).nextPartialRoundOutput(response);
                System.out.println(nextTweet.length());

                System.out.println("@" + userName + "\n" + nextTweet);
            }
            System.out.println("********************************************************************************");
            response = reader.nextLine();
        }





//        GameOfPoker game = new GameOfPoker(5, 5);
//        game.gameLoop();

//        String str = "@bottymboc 1 ,,2  ,3, 4";
//        String[] parts = str.split("[\\s,]+");
//        Integer[] p = new Integer[parts.length];
////        for(int i = 0; i < parts.length; i++){
////            p[i] = Integer.parseInt(parts[i]);
////        }
//        for(int i = 0; i < p.length; i++){
//            System.out.println(parts[i]);
//        }
//
//        System.out.println(parts[0] + " <0, 1> " + parts[1]);
//        //Bot.getNextInitialTweet();
//


//        Random r = new Random();
//        int howLikely = 0;
//        for(int i = 0; i < 1000; i++){
//            howLikely = -10 + r.nextInt(21);
//            System.out.println(howLikely);
//        }
//        howLikely = -10 + r.nextInt(20);
//        DeckOfCards deck = new DeckOfCards();
//        deck.shuffle();
//        AIPlayer player;
//        for(int i = 0; i < 100; i++){
//            player = new AIPlayer(deck,10);
//            System.out.println(getHandType(player.getHand()) + ": " + player.getHand() + ", probabilities: (" + probabilitiesToString(player.getHand()) + ")");
//            player.discard();
//            System.out.println("New Hand: " + player.getHand() + "\n");
//            deck.reset();
//        }



//
//		HandOfCards bustedStraight = null;
//		System.out.println("Testing busted Straights: \n");
//		for(int i = 0; i < 1000 ; i++){
//			bustedStraight = TestHand.makeBustedStraight();
//			System.out.println("Busted Straight: " + bustedStraight + ", probabilities: (" + probabilitiesToString(bustedStraight) + ")");
//		}
//
//		HandOfCards bustedFlush = null;
//		System.out.println("\n\nTesting Busted Flushes: \n");
//		for(int i = 0; i < 10 ; i++){
//			bustedFlush = TestHand.makeBustedFlush();
//			System.out.println("Busted Flush: " + bustedFlush + ", probabilities: (" + probabilitiesToString(bustedFlush) + ")");
//		}
//
//		HandOfCards randomHand = null;
//		DeckOfCards deck2 = new DeckOfCards();
//		System.out.println("\n\nTesting some random hands: \n");
//		for(int i = 0; i < 10; i++){
//			randomHand = new HandOfCards(deck2);
//			System.out.println(getHandType(randomHand) + ": " + randomHand + ", probabilities: (" + probabilitiesToString(randomHand) + ")");
//			deck2.reset();
//		}

//
//		System.out.println("\n\nSome specific test cases:");
//		HandOfCards bustedStraight9 = new HandOfCards(new PlayingCard("10", PlayingCard.HEARTS, 10, 10),
//				new PlayingCard("J", PlayingCard.SPADES, 11, 11),
//				new PlayingCard("Q", PlayingCard.CLUBS, 12, 12),
//				new PlayingCard("K", PlayingCard.SPADES, 13, 13),
//				new PlayingCard("A", PlayingCard.HEARTS, 14, 14));
//
//		System.out.println(bustedStraight9 + " " + getHandType(bustedStraight9));
//		System.out.println(probabilitiesToString(bustedStraight9));
//
//        HandOfCards bustedStraight10 = new HandOfCards(new PlayingCard("4", 'H', 4, 4),
//                new PlayingCard("4", 'S', 4, 4),
//                new PlayingCard("9", 'C', 9, 9),
//                new PlayingCard("10", 'S', 10, 10),
//                new PlayingCard("A", 'H', 14, 14));
//
//        System.out.println(bustedStraight10 + " " + getHandType(bustedStraight10));
//        System.out.println(probabilitiesToString(bustedStraight10));



    }

    public static String probabilitiesToString(HandOfCards hand){
        String output = "";
        for(int i = 0; i < HandOfCards.SIZE_OF_HAND; i++){
            output += hand.getDiscardProbability(i) + " ";
        }
        return output;
    }

    public static String getHandType(HandOfCards hand){
        if(hand.isRoyalFlush()){
            return "Royal Flush";
        } else if (hand.isStraightFlush()){
            return "Straight Flush";
        } else if(hand.isFourOfAKind()) {
            return "Four of a Kind";
        } else if(hand.isFullHouse()){
            return "Full House";
        } else if(hand.isFlush()){
            return "Flush";
        } else if(hand.isStraight()){
            return "Straight";
        } else if(hand.isThreeOfAKind()){
            return "Three of a Kind";
        } else if(hand.isTwoPair()){
            return "Two Pair";
        } else if(hand.isOnePair()){
            return "One Pair";
        } else {
            return "High Hand";
        }
    }
}
