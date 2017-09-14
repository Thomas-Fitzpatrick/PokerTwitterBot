package Poker;

/**
 * Created by thoma on 10/04/2017.
 */
public class HumanPlayer extends Player {
    private String humanResponse;

    HumanPlayer(String name, DeckOfCards deck, int startingChips){
        super(name, deck, startingChips);
    }

    //sets the human response variable to the input string. This is for parsing the user's text input depending on the state of the game.
    public void setResponse(String response){
        humanResponse = response;
    }


    //determines whether the user wants to play another game
    public boolean getPlayResponse(){
        if(humanResponse.contains("Yes") || humanResponse.contains("yes") || humanResponse.contains("Y") || humanResponse.contains("y")){
            return true;
        }
        return false;
    }

    @Override
    public int makeBet() {
        int bet = 0;
        if(humanResponse.contains("Fold") || humanResponse.contains("fold") || humanResponse.contains("FOLD")){
            bet = 0;
        } else if(humanResponse.contains("Call") || humanResponse.contains("call") || humanResponse.contains("CALL")){
            bet = 1;
        } else if(humanResponse.contains("Raise") || humanResponse.contains("raise") || humanResponse.contains("RAISE")){
            bet = 2;
        }
        return bet;
    }

    @Override
    public int discard() {
        int[] discardCards = new int[3];
        int count = 4;

        String[] stringcards = humanResponse.split("[\\s,()]+");

        if(stringcards.length < 4){
            count = stringcards.length;
        }

        for(int i = 1; i < count; i++){
            discardCards[i-1] = Integer.parseInt(stringcards[i]);
            System.out.println(discardCards[i-1]);
        }
        hand.replaceCards(discardCards, count-1);
        return count-1;
    }
}
