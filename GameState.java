package Poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by thoma on 17/04/2017.
 */

/*
    This class serves as the interface betweent the Twitter API and the poker game itself.
    It maintains a single game object including the deck and all players and their current status.
    Only two public methods, getNextPartialRoundOutput and the constructor.
 */
public class GameState {
    int stateNumber;                        //core state of current game, gets incremented between stages of the round/game
    int lastRaise;                         //Keeps track of index of most recent person to raise
    private int startingChips = 10;

    int pot = 0;
    int currentRoundBet = 1;
    boolean canRaise = true;
    boolean isFoldedUser = false;


    private HumanPlayer humanPlayer;
    private ArrayList<Player> playersInGame = new ArrayList<>();
    private ArrayList<Player> playersInRound = new ArrayList<>();

    private DeckOfCards deck;
    private int numPlayers = 5;
    private ArrayList<String> names = new ArrayList<String>(Arrays.asList("Tom", "Jam", "Fin", "Stn"));

    public GameState(String humanPlayerName) {
        this.deck = new DeckOfCards();
        this.deck.shuffle();
        humanPlayer = new HumanPlayer(humanPlayerName, deck, startingChips);
        stateNumber = 0;
        createPlayers(humanPlayerName);
        playersInRound.addAll(playersInGame);
        lastRaise = playersInRound.size() - 1;

    }


    //method to  create the AI players and setup the playersInGame list
    private void createPlayers(String humanPlayerName){
        Collections.shuffle(names);
        playersInGame.add(humanPlayer);
        AIPlayer player;
        for(int i = 0; i < numPlayers - 1; i++) {
            player = new AIPlayer(names.get(i), deck, startingChips);
            playersInGame.add(player);
        }
    }


    //loops through playersInGame and removes players with no chips
    private void removeBrokePlayers(){
        ArrayList<Integer> brokePlayers = new ArrayList<>();

        for(int i = 0; i < playersInGame.size();){
            if(playersInGame.get(i).getChips() > 0){
                i++;
            } else {
                playersInGame.remove(i);
            }
        }
    }


    /*
        Core function of Bot, this returns the next appropriate string based on the current state of the game.
     */
    public String nextPartialRoundOutput(String humanInput){
        humanPlayer.setResponse(humanInput);

        if(stateNumber == 0){
            stateNumber = 1;
            return startRound();
        } else if(stateNumber == 1){
            stateNumber = 2;
            return firstBettingRound();
        } else if(stateNumber == 2){
            stateNumber = 3;
            return discardRound();
        } else if(stateNumber == 3){
            stateNumber++;
            return secondBettingRound();
        } else if(stateNumber == 5) {
            stateNumber = 0;
            discardRound();
            secondBettingRound();
            return roundEnd();
        } else {
            stateNumber = 0;
            return roundEnd();
        }

    }


    /*
        Generates output for the first section of the game round, the start round, detailing the players hand and asking
        if they want to call, raise or fold. Also allows for second round of betting if necessary depending on if
        somone raised after the player.
    */
    private String startRound(){
        boolean playingAgain = humanPlayer.getPlayResponse();
        String output = "";
        output += "Your hand: " + getHumanHand() + "\n";
        output += getCurrentChips();

        if(playingAgain) {
            if (humanPlayer.getChips() > 1) {
                output += "Call, raise or fold?";
            } else if (humanPlayer.getChips() == 1) {
                output += "You only have " + humanPlayer.getChips() + ".\n Call or fold?";
            } else {
                output += ResponseGenerator.endGamePhrase + "\nYou have no chips. You lose.";
            }
        } else {
            Player winner = playersInGame.get(0);
            for(Player player: playersInGame){
                if(player.getChips() > winner.getChips()){
                    winner = player;
                }
            }
            output += ResponseGenerator.endGamePhrase + winner.getName() + " won the game!";
        }
        return output;
    }


    /*
        Tells the user the restults of the first round of betting and asks which cards they would like ot replace.
     */
    private String firstBettingRound(){
        String firstBettingOutput = "";
        int humanTurn = humanPlayer.makeBet();
        if(humanTurn == 2 && humanPlayer.getChips() > 1){
            currentRoundBet += 1;
        }
        int humanBet = currentRoundBet - humanPlayer.getLastBet();
        if(humanTurn != 0) {
            humanPlayer.giveChips(humanBet);
            pot += humanBet;
            humanPlayer.setLastBet(humanBet);
        }
        firstBettingOutput += getStatus();


        String botBets = takeBets();
        firstBettingOutput += botBets;
        firstBettingOutput += "Pot: " + pot + "\n";

        if(botBets.contains("raised")){
            canRaise = false;
            firstBettingOutput += "Call or fold?\n";
            stateNumber--;
        } else {
            lastRaise = playersInRound.size() -1;
            resetLastBets();
            firstBettingOutput += "Discard which cards?(eg. 0,1,2)\n";
            currentRoundBet = 1;
        }

        if(humanTurn == 0){
            isFoldedUser = true;
            stateNumber = 5;
            return getStatus() + "You folded from the round.\nRespond to see the results of the round.\n";

        }
        return firstBettingOutput;

    }

    /*
        simulates round of discarding cards and tells user the results of this. Then asks for their bet for the next round.
     */
    private String discardRound(){
        String discardRoundTweet = "";
        humanPlayer.discard();
        discardRoundTweet += "You now have: " + humanPlayer.getHand() + "\n";

        playersInRound.remove(humanPlayer);
        //remaining players can discard cards
        if(playersInRound.size() > 0) {
            for (Player player : playersInRound) {
                discardRoundTweet += player.getName() + " discarded " + player.discard() + "\n";
            }
        }
        playersInRound.add(humanPlayer);
        if(humanPlayer.getChips() > 1) {
            discardRoundTweet += "Call, raise or fold?";
        } else if(humanPlayer.getChips() == 1){
            discardRoundTweet += "You only have " + humanPlayer.getChips() + ".\n Call or fold?";
        } else {
            discardRoundTweet += ResponseGenerator.endGamePhrase + "\nYou have no chips and cannot call.\nYou lose.";
        }
        return discardRoundTweet;
    }

    //Simulates second round of AI bets and allows for a follow up bet from the user if one of the bots raised.
    private String secondBettingRound(){
        String secondBettingOutput = "";
        int humanTurn = humanPlayer.makeBet();
        if(humanTurn == 2){
            currentRoundBet += 1;
        }
        int humanBet = currentRoundBet - humanPlayer.getLastBet();
        if(humanTurn != 0) {
            humanPlayer.giveChips(humanBet);
            pot += humanBet;
            humanPlayer.setLastBet(humanBet);
            secondBettingOutput += "You bet " + humanBet + " chips.\n";
        }


        secondBettingOutput += "You now have " + humanPlayer.getChips() + " chips.\n";


        String botBets = takeBets();
        secondBettingOutput += botBets;
        secondBettingOutput += "Pot contains: " + pot + "\n";

        if(botBets.contains("raised")){
            canRaise = false;
            secondBettingOutput += "Call or fold?\n";
            stateNumber--;
        } else {

            resetLastBets();
            secondBettingOutput += "Respond to see the results of the round.\n";
            currentRoundBet = 1;

        }

        if(humanTurn == 0){
            isFoldedUser = true;
            secondBettingOutput += "You folded.\nRespond to see the results.";

        }
        return secondBettingOutput;


    }

    //Calculates winner of round and tells user results. Asks if user would like to play again.
    private String roundEnd(){
        String endOfRoundString = "";

        Player winner = playersInRound.get(0);
        if(isFoldedUser){
            playersInRound.remove(humanPlayer);
        }
        isFoldedUser = false;

        for (Player player : playersInRound) {
            if (player.getHand().getGameValue() > winner.getHand().getGameValue()) {
                winner = player;
            }
            //players win round and chips distributed, we're gonna have to deal with the output eventually too...
        }
        winner.winChips(pot);


        endOfRoundString += winner.getName() + " wins " + pot + " chips!\n";
        endOfRoundString += winner.getHand() + "\n";
        pot = 0;
        playersInRound.clear();
        playersInRound.addAll(playersInGame);
        lastRaise = playersInRound.size() -1;

        getCurrentChips();

        removeBrokePlayers();
        if(playersInGame.size() == 1){
            if(playersInGame.get(0) == humanPlayer){
                endOfRoundString += ResponseGenerator.endGamePhrase + "\nYou have won the game!!\n" +
                        "If you would like to play again, tweet " + RunTwitterBot.startGameHashtag;
            } else {
                endOfRoundString += ResponseGenerator.endGamePhrase + "\n" + playersInGame.get(0).getName() + "has won the game!!\n" +
                        "If you would like to play again, tweet " + RunTwitterBot.startGameHashtag;
            }
        } else {
            endOfRoundString += "Would you like to play another round?(y or n)";
        }

        deck.reset();
        for(Player player: playersInGame){
            player.getHand().dealNewHand();
        }

        return endOfRoundString;
    }


    //Function to generate bets of AI remaining in the round
    private String takeBets(){
        playersInRound.remove(humanPlayer);
        Player player;
        int playerBet = 0;
        String placedBets = "";
        int finalRaise = 0;
        int nextBet;
        int j = 0;

        for (int i = 0; i < playersInRound.size() && j < lastRaise;) {
            j++;
            player = playersInRound.get(i);
            nextBet = currentRoundBet - player.getLastBet();
            playerBet = player.makeBet();
            if (playerBet > 0) {
                if(playerBet == 1) {
                    player.giveChips(nextBet);
                    player.setLastBet(nextBet);
                    placedBets += player.getName() + " called\n";
                } else if(playerBet == 2){
                    finalRaise = i;
                    if(canRaise) {
                        currentRoundBet += 1;
                        nextBet = currentRoundBet - player.getLastBet();
                        placedBets += player.getName() + " raised\n";
                        player.giveChips(nextBet);
                        player.setLastBet(nextBet);
                    } else {
                        placedBets += player.getName() + " called\n";
                        player.giveChips(nextBet);
                        player.setLastBet(nextBet);
                    }
                }
                pot += nextBet;
                i++;
            } else {
//                if(playersInRound.size() == 1){
//                    i++;
//                } else {
                    placedBets += player.getName() + " folded\n";
                    playersInRound.remove(i);
//                }
            }
        }
        lastRaise = finalRaise;
        playersInRound.add(humanPlayer);
        return placedBets;
    }


    //resets bets of AI (for use between betting rounds)
    private void resetLastBets(){
        for(Player player: playersInRound){
            player.resetLastBet();
        }
    }


    //Returns string of current players and their chips
    private String getCurrentChips(){
        String output = "";
        for(Player player: playersInGame){
            output += player.getName() + ": " + player.getChips() + "\n";
        }
        return output;
    }


    //return strings of the current player's hand and chips.
    private String getStatus(){
        return "You have " + humanPlayer.getHand() + " and " + humanPlayer.getChips() + " chips.\n";
    }


    private HandOfCards getHumanHand(){
        return humanPlayer.getHand();
    }



}
