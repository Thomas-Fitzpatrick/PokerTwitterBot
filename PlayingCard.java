package Poker;

public class PlayingCard implements Comparable<PlayingCard>{

    private String type;
    private char suit;
    private int faceValue, gameValue;


    public PlayingCard(String type, char suit, int faceValue, int gameValue) {
        this.type = type;
        this.suit = suit;
        this.faceValue = faceValue;
        this.gameValue = gameValue;
    }

    @Override
    public int compareTo(PlayingCard card) {
        Integer this_value = this.gameValue;
        Integer other_value = card.gameValue;
        return this_value.compareTo(other_value);
    }


    @Override
    public String toString() {      // Simple toString to output PlayingCard objects
        return "" + type + suit;
    }

    //getters for playingCard objects
    public String getType() {
        return type;
    }

    public char getSuit() {
        return suit;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public int getGameValue() {
        return gameValue;
    }
}
