package Poker;

import java.util.*;

public class HandOfCards {

    /*


    For ranking the cards I used a general formula of highest cards*10^8 + next highest*10^6 + next highest*10^4 + ....

    For simpler evaluations such as full house I only needed to have two terms multiplied by a power of 10.

    Evaluations such as Three of a kind only had a small range of possible ranks seeing as its impossible to have two hands with the same rank.
     */
    static public final int PAIR_DEFAULT = 1000000;
    static public final int TWO_PAIR_DEFAULT = PAIR_DEFAULT + 1000000;
    static public final int THREE_OF_A_KIND_DEFAULT = TWO_PAIR_DEFAULT + 15;
    static public final int STRAIGHT_DEFAULT = THREE_OF_A_KIND_DEFAULT + 15;
    static public final int FLUSH_DEFAULT = STRAIGHT_DEFAULT + 1000000;
    static public final int FULL_HOUSE_DEFAULT = FLUSH_DEFAULT + 15;
    static public final int FOUR_OF_A_KIND_DEFAULT = FULL_HOUSE_DEFAULT + 15;
    static public final int STRAIGHT_FLUSH_DEFAULT = FOUR_OF_A_KIND_DEFAULT + 15;
    static public final int ROYAL_FLUSH_DEFAULT = STRAIGHT_FLUSH_DEFAULT + 15;


    static public final int SIZE_OF_HAND = 5;
    ArrayList<PlayingCard> hand = new ArrayList<PlayingCard>();
    private DeckOfCards deck;

    public HandOfCards(DeckOfCards deck){
        this.deck = deck;
        initialise();
    }

    public HandOfCards(PlayingCard a, PlayingCard b, PlayingCard c, PlayingCard d, PlayingCard e){
        hand.add(a);
        hand.add(b);
        hand.add(c);
        hand.add(d);
        hand.add(e);
        sort();
    }

    public void initialise(){
        for(int i = 0; i < SIZE_OF_HAND; i++){
            hand.add(deck.dealNext());
        }
        sort();
    }

    public void emptyHand(){
        hand.clear();
    }

    public void dealNewHand(){
        hand.clear();
        initialise();
    }

    public void replaceCards(int[] discardCards, int count){
        int position;
        for(int i = 0; i < count; i++){
            position = discardCards[i];
            hand.remove(position);
            hand.add(position, deck.dealNext());
        }
        sort();
    }

    //for sorting I simply implemented comparable in Playing card and used collections.sort
    private void sort(){
        Collections.sort(hand);
    }

    //Function which evaluates the rank of our hand. First checks what type of hand it is then evaluates a specific
    //score based off that hand's cards.
    public int getGameValue(){
        if(isRoyalFlush()){
            return ROYAL_FLUSH_DEFAULT;
        } else if (isStraightFlush()){
            return STRAIGHT_FLUSH_DEFAULT + straightflushValue();
        } else if(isFourOfAKind()) {
            return FOUR_OF_A_KIND_DEFAULT + fourOfAKindValue();
        } else if(isFullHouse()){
            return FULL_HOUSE_DEFAULT + fullHouseValue();
        } else if(isFlush()){
            return FLUSH_DEFAULT + flushValue();
        } else if(isStraight()){
            return STRAIGHT_DEFAULT + straightValue();
        } else if(isThreeOfAKind()){
            return THREE_OF_A_KIND_DEFAULT + threeOfAKindValue();
        } else if(isTwoPair()){
            return TWO_PAIR_DEFAULT + twoPairValue();
        } else if(isOnePair()){
            return PAIR_DEFAULT + onePairValue();
        } else {
            return highHandValue();
        }
    }

    public	int	getDiscardProbability(int cardPosition){
        int numOfInstances = numberOfInstances(cardPosition);

        if(isRoyalFlush()){
            return 0;						//best hand in game
        } else if(isStraightFlush()){


            if(cardPosition == 0) {			//slight possibility that a player may try to improve a straight flush
                return 1;
            } else {
                return 0;
            }
        } else if(isFourOfAKind()){

            if(numOfInstances == 1){		//allowing for players to discard the card that doesn't contribute to the hand
                return 100;
            }

        } else if(isFullHouse()){


            if(numOfInstances == 2){		//slight possibility of improving a full house to four of a kind
                return 1;
            }
            return 0;
        } else if(isFlush()){
            if(isBustedStraight()){			//checking to see if also a busted straight, may risk going for a straight flush
                if(cardPosition == getBustedStraightCard()){
                    return 2;
                } else {
                    return 0;
                }
            }

            if(cardPosition == 0) {
                return 1;
            } else {
                return 0;
            }
        } else if(isStraight()){
            if(isBustedFlush()){			//checking if busted flush, may risk going for straight or busted
                if(getBustedFlushCard() == cardPosition){
                    if(cardPosition == 0){
                        return 2;
                    } else {
                        return 1;

                    }
                } else {
                    return 0;
                }
            } else if(hand.get(0).getGameValue() == 10){
                return 0;
            } else if(cardPosition == 0) {
                return 1;										//slight chance they may try to improve a straight to a better one
            } else {
                return 0;
            }
        } else if(isThreeOfAKind()){
            if(numOfInstances != 3){			//checking if cardPosition is not in a triple
                return 100;
            } else {
                return 0;
            }
        } else if(isTwoPair()){


            if(numOfInstances == 1){			//checking if cardPosition is not in a pair
                return 100;
            }
        } else if(isOnePair()){
            if(isBustedFlush()){				//checking if busted flush
                if(getBustedFlushCard() == cardPosition){
                    if(numOfInstances == 2){
                        return 11;				//may risk one pair in favour of getting  a flush
                    } else {
                        return 100;				//if card doesnt contribute to pair we can discard safely
                    }
                } else {
                    return 0;
                }
            }
            if (isBustedStraight()) {
                if(getBustedStraightCard() == cardPosition){
                    if(numOfInstances == 2){
                        return 11;				////may risk one pair in favour of getting  a straight
                    } else {
                        return 100;
                    }
                } else {
                    return 0;
                }
            }
            if(numOfInstances != 2){			//checking if cardPosition is not in a pair
                return 100;
            }
            return 0;
        } else {
            if(isBustedFlush()){
                if(cardPosition == getBustedFlushCard()){
                    if(cardPosition == 4){
                        return 90;				//risking high card in favour of getting flush
                    }
                    return 100;
                }
                return 0;
            }
            if(isBustedStraight()){
                if(cardPosition == getBustedStraightCard()){
                    if(cardPosition == 4){
                        return 90;				//risking high card in favour of getting straight
                    }
                    return 100;
                }
                return 0;
            }

            if (cardPosition == 4) {
                return 0;
            } else if(cardPosition == 3){
                return 0;						//slight probability of returning the second highest card
            }
            return 100;
        }
        return 0;
    }


    //calculates whether the hand is a busted straight
    private boolean isBustedStraight(){
        if(getBustedStraightCard() == -1){
            return false;
        }

        return true;
    }

    //calculates which card in a busted straight is the bust
    private int getBustedStraightCard(){
        int bigBreaks = 0;
        int breaksOfTwo = 0;
        int difference = 0;

        //for this solution I counted the number of breaks of size two and breaks larger than that
        //I found that most cases of broken straights fell in to similar shapes of breaks of size one or two.
        //There were only a few edge cases with aces and straights that I had to account for outside of that.
        for(int i = 1; i < SIZE_OF_HAND; i++){
            difference = hand.get(i).getGameValue() - hand.get(i - 1).getGameValue();
            if(difference == 2){
                breaksOfTwo++;
            }else if(difference > 2){
                bigBreaks++;
            }
        }

        if(hand.get(4).getGameValue() == 14 && (hand.get(0).getGameValue() == 2 || hand.get(0).getGameValue() == 3)){			//Dealing with low straights including aces
            if(hand.get(3).getGameValue() == 7 || hand.get(3).getGameValue() == 6){
                return 4;
            } else if(hand.get(0).getGameValue() == 2 && (hand.get(1).getGameValue() == 3 || hand.get(1).getGameValue() == 4)){
                return 3;
            } else if(hand.get(0).getGameValue() == 3 && hand.get(1).getGameValue() == 4){
                return 3;
            } else if(isOnePair()){
                for(int i = 0; i < SIZE_OF_HAND; i++){
                    if(numberOfInstances(i) == 2){
                        return i;
                    }
                }
            } else {
                return -1;
            }
        }else if(isOnePair() && (hand.get(4).getGameValue() - hand.get(0).getGameValue() == 3 || hand.get(4).getGameValue() - hand.get(0).getGameValue() == 4)){											//dealing with broken straights which also have pairs

            for(int i = 0; i < SIZE_OF_HAND; i++){
                if(numberOfInstances(i) == 2){
                    return i;
                }
            }
        } else if(isOnePair()){
            return -1;
        } else if(breaksOfTwo == 1){
            if(bigBreaks == 1){
                if(isOnePair()){
                    return -1;
                } else if(hand.get(4).getGameValue() - hand.get(3).getGameValue() == 2 && hand.get(1).getGameValue() - hand.get(0).getGameValue() == 1){
                    return -1;
                } else if(hand.get(1).getGameValue() - hand.get(0).getGameValue() == 2 && hand.get(4).getGameValue() - hand.get(3).getGameValue() == 1){
                    return -1;
                } else if(hand.get(1).getGameValue() - hand.get(0).getGameValue() == 1){
                    return 4;
                } else if(hand.get(4).getGameValue() - hand.get(3).getGameValue() == 1){
                    return 0;
                } else if(hand.get(4).getGameValue() - hand.get(3).getGameValue() == 2 ){
                    return 0;
                } else if(hand.get(1).getGameValue() - hand.get(0).getGameValue() == 2){
                    return 4;
                }
            }else if(bigBreaks == 0){
                return 0;
            }
        } else if(breaksOfTwo == 0){
            if(bigBreaks == 1) {
                if(hand.get(1).getGameValue() - hand.get(0).getGameValue() == 1 && hand.get(4).getGameValue() - hand.get(3).getGameValue() == 1){
                    return -1;
                } else if (hand.get(1).getGameValue() - hand.get(0).getGameValue() == 1) {
                    return 4;
                } else if (hand.get(4).getGameValue() - hand.get(3).getGameValue() == 1) {
                    return 0;
                }
            } else{
                return -1;
            }
        } else if(breaksOfTwo == 2){
            if(bigBreaks == 0){
                if (hand.get(1).getGameValue() - hand.get(0).getGameValue() == 1) {
                    return 4;
                } else {
                    return 0;
                }
            }
        }
        return -1;
    }


    //function to calculate the number of instances of cards with the same value as the one at cardPosition
    private int numberOfInstances(int cardPosition){
        int numOfSameCards = 1;
        for(int i = 0; i < SIZE_OF_HAND; i++){
            if(i != cardPosition){
                if(hand.get(cardPosition).getFaceValue() == hand.get(i).getFaceValue()){
                    numOfSameCards++;
                }
            }
        }
        return numOfSameCards;
    }


    //calculates if the hand contains a busted Flush
    private boolean isBustedFlush(){
        HashMap<Character, Integer> occurrences = getSuitHash();

        if(occurrences.values().contains(4)){
            return true;
        }
        return false;
    }


    //calculates which card is the bust card of a busted flush
    private int getBustedFlushCard(){
        PlayingCard bustCard = hand.get(0);
        HashMap<Character, Integer> occurrences = getSuitHash();

        char bustSuit = ' ';
        for(Character suit: occurrences.keySet()){
            if(occurrences.get(suit) == 1){
                bustSuit = suit;
            }
        }

        for(PlayingCard card: hand){
            if(card.getSuit() == bustSuit){
                bustCard = card;
            }
        }

        return hand.indexOf(bustCard);
    }


    //creates a hash map of suits to occurances of cards in that suit, allowing for the detection of busted flushes
    private HashMap<Character, Integer> getSuitHash(){
        HashMap<Character, Integer> occurrences = new HashMap<>();
        char suit = ' ';
        for(PlayingCard card: hand){
            suit = card.getSuit();
            if(!occurrences.keySet().contains(suit)){
                occurrences.put(suit, 1);
            } else {
                occurrences.put(suit, occurrences.get(suit) + 1);
            }
        }
        return occurrences;
    }

    //calculates whether it is a royal flush or not
    public boolean isRoyalFlush(){
        if(isFlush()){
            if(hand.get(0).getType() == "10" &&
                    hand.get(1).getType() == "J" &&
                    hand.get(2).getType() == "Q" &&
                    hand.get(3).getType() == "K" &&
                    hand.get(4).getType() == "A") {
                return true;
            }
        }
        return false;
    }

    //calculates whether it is a straight flush or not
    public boolean isStraightFlush(){
        if(isFlush()){
            if(isStraight()){
                return true;
            }
        }
        return false;
    }

    //calculates whether it is four of a kind
    public boolean isFourOfAKind(){
        if((hand.get(0).getFaceValue() == hand.get(3).getFaceValue() || hand.get(1).getFaceValue() == hand.get(4).getFaceValue())){
            return true;
        }
        return false;
    }

    //calculates whether the hand is a full house
    public boolean isFullHouse(){
        if((hand.get(0).getFaceValue() == hand.get(2).getFaceValue() && hand.get(3).getFaceValue() == hand.get(4).getFaceValue()) ||
                hand.get(0).getFaceValue() == hand.get(1).getFaceValue() && hand.get(2).getFaceValue() == hand.get(4).getFaceValue()){
            return true;
        }
        return false;
    }

    //calculates whether the hand is a flush
    public boolean isFlush(){
        PlayingCard first = hand.get(0);
        for(PlayingCard card: hand){
            if(card.getSuit() != first.getSuit()){
                return false;
            }
        }
        return true;
    }

    //calculates whether the hand is a straight
    public boolean isStraight(){
        if(hand.get(4).getGameValue() == 14 && hand.get(0).getGameValue() == 2 && hand.get(1).getGameValue() == 3 && hand.get(2).getGameValue() == 4 && hand.get(3).getGameValue() == 5){
            return true;
        } else if(hand.get(0).getFaceValue() == hand.get(4).getFaceValue() - 4 &&
                hand.get(1).getFaceValue() + 1 == hand.get(2).getFaceValue() &&
                hand.get(2).getFaceValue() + 1 == hand.get(3).getFaceValue() &&
                hand.get(3).getFaceValue() + 1 == hand.get(4).getFaceValue()){
            return true;
        }
        return false;
    }

    //calculates whether the hand contains three of a kind
    public boolean isThreeOfAKind(){
        if(hand.get(0).getFaceValue() == hand.get(2).getFaceValue() ||
                hand.get(1).getFaceValue() == hand.get(3).getFaceValue() ||
                hand.get(2).getFaceValue() == hand.get(4).getFaceValue()){
            return true;
        }
        return false;
    }

    //calculates whether the hand contains two pairs
    public boolean isTwoPair(){
        if(hand.get(0).getFaceValue() == hand.get(1).getFaceValue() && hand.get(2).getFaceValue() == hand.get(3).getFaceValue() ||
                hand.get(0).getFaceValue() == hand.get(1).getFaceValue() && hand.get(3).getFaceValue() == hand.get(4).getFaceValue() ||
                hand.get(1).getFaceValue() == hand.get(2).getFaceValue() && hand.get(3).getFaceValue() == hand.get(4).getFaceValue()){
            return true;
        }
        return false;
    }

    //calculates whether the hand contains a pair
    public boolean isOnePair(){
        if(hand.get(0).getFaceValue() == hand.get(1).getFaceValue() ||
                hand.get(1).getFaceValue() == hand.get(2).getFaceValue() ||
                hand.get(2).getFaceValue() == hand.get(3).getFaceValue() ||
                hand.get(3).getFaceValue() == hand.get(4).getFaceValue()){
            return true;
        }
        return false;
    }

    //calculates if the hand's best score is simply the high card
    public boolean isHighHand(){
        if(!isOnePair() && !isTwoPair() && !isThreeOfAKind() && !isStraight() && !isFlush() && !isFullHouse() && !isFourOfAKind() && !isStraightFlush() && !isRoyalFlush()){
            return true;
        }
        return false;
    }

    //we can simply use the value of the top card for this calculation.
    private int straightflushValue(){
        return straightValue();
    }

    //We only need to look at the value of the four of a kind card for this as it's impossible to have two hands with the same 4 cards.
    private int fourOfAKindValue(){
        if(hand.get(0) == hand.get(1)){
            return hand.get(0).getGameValue();
        }
        return hand.get(4).getGameValue();
    }

    //In this evaluation we give more weight to the triple than the double so triple decides who wins before double is relevant.
    private int fullHouseValue(){
        return hand.get(2).getGameValue();
    }

    //For flush we have to weight all cards so that lower value cards arent relevant unlesss higher value ones are the same in two hands.
    private int flushValue(){

        return (int)(hand.get(4).getGameValue()*Math.pow(10, 4) + hand.get(3).getGameValue()*Math.pow(10, 3) + hand.get(2).getGameValue()*Math.pow(10, 2) +
                hand.get(1).getGameValue()*10 + hand.get(0).getGameValue());
    }

    //we only need to consider the highest card for this one.
    private int straightValue(){
        if(hand.get(4).getGameValue() == 14 && hand.get(0).getGameValue() == 2 && hand.get(1).getGameValue() == 3 && hand.get(2).getGameValue() == 4 && hand.get(3).getGameValue() == 5){
            return hand.get(3).getGameValue();
        }
        return hand.get(4).getGameValue();
    }

    //Because it is impossible for two hands to to have the same three of a kind value we only need to consider the
    //triple card value. Also when sorted, one of the triples will always be in the middle of the hand.
    private int threeOfAKindValue(){
        return hand.get(2).getGameValue();
    }

    //For this evaluation we need to weight the pairs first, then all of the remaining cards in order of their value.
    private int twoPairValue(){
        if(hand.get(0).getFaceValue() == hand.get(1).getFaceValue() && hand.get(2).getFaceValue() == hand.get(3).getFaceValue()){
            return (int)(hand.get(3).getGameValue()*Math.pow(10, 2) + hand.get(1).getGameValue()*10 + hand.get(4).getGameValue());
        } else if(hand.get(0).getFaceValue() == hand.get(1).getFaceValue() && hand.get(3).getFaceValue() == hand.get(4).getFaceValue()){
            return (int)(hand.get(4).getGameValue()*Math.pow(10, 2) + hand.get(1).getGameValue()*10 + hand.get(2).getGameValue());
        }
        return (int)(hand.get(4).getGameValue()*Math.pow(10, 2) + hand.get(2).getGameValue()*10 + hand.get(0).getGameValue());
    }

    //For this evaluation we need to weight the pair first, then all of the remaining cards in order of their value.
    private int onePairValue(){
        if(hand.get(0).getFaceValue() == hand.get(1).getFaceValue()){
            return (int)(hand.get(1).getGameValue()*Math.pow(10, 3) + hand.get(4).getGameValue()*Math.pow(10, 2) + hand.get(3).getGameValue()*10 + hand.get(2).getGameValue());
        } else if(hand.get(1).getFaceValue() == hand.get(2).getFaceValue()){
            return (int)(hand.get(2).getGameValue()*Math.pow(10, 3) + hand.get(4).getGameValue()*Math.pow(10, 2) + hand.get(3).getGameValue()*10 + hand.get(0).getGameValue());
        } else if(hand.get(2).getFaceValue() == hand.get(3).getFaceValue()){
            return (int)(hand.get(3).getGameValue()*Math.pow(10, 3) + hand.get(4).getGameValue()*Math.pow(10, 2) + hand.get(1).getGameValue()*10 + hand.get(0).getGameValue());
        }
        return (int)(hand.get(4).getGameValue()*Math.pow(10, 3) + hand.get(2).getGameValue()*Math.pow(10, 2) + hand.get(1).getGameValue()*10 + hand.get(0).getGameValue());
    }

    //Similarly to the flush, we need to consider the weights of all cards when comparing two highHands.
    private int highHandValue(){
        return (int)(hand.get(4).getGameValue()*Math.pow(10, 4) + hand.get(3).getGameValue()*Math.pow(10, 3) + hand.get(2).getGameValue()*Math.pow(10, 2) +
                hand.get(1).getGameValue()*10 + hand.get(0).getGameValue());
    }

    @Override
    public String toString() {
        String output = "";
        for(PlayingCard card: hand){
            output += card + " ";
        }
        return output;
    }

    public DeckOfCards getDeck() {
        return deck;
    }







}
