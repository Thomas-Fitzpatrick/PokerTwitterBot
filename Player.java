package Poker;

/**
 * Created by The Harlem Codetrotters on 10/04/2017.
 * Class for handling player objects, their hands, names and chips.
 * Allows for dynamic method calling depending on whether the player is AI controlled or human controlled.
 */

public abstract class Player {
    private String name;
    protected int lastBet;
    protected HandOfCards hand;
    int chips;

    Player(String name, DeckOfCards deck, int startingChips){
        this.name = name;
        hand = new HandOfCards(deck);
        chips = startingChips;
        lastBet = 0;
    }


    //returns a value of 0 for fold, 1 for call, 2 for raise
    public abstract int makeBet();


    //discards however many cards the player decides
    public abstract int discard();

    //takes winnings from a round
    public int winChips(int winnings){
        chips += winnings;
        return winnings;
    }

    public int giveChips(int amount){
        chips -= amount;
        return amount;
    }

    public HandOfCards getHand() {
        return hand;
    }

    public int getChips(){
        return chips;
    }

    public String getName() {
        return name;
    }

    public int getLastBet() {
        return lastBet;
    }

    public void setLastBet(int lastBet) {
        this.lastBet = lastBet;
    }

    public void resetLastBet(){
        this.lastBet = 0;
    }

    @Override
    public String toString() {
        return
                "name='" + name + '\'' +
//                ", hand=" + hand +
                ", chips=" + chips;
    }
}
