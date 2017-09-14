package Poker;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.HashMap;

/**
 * Created by The Harlem Codetrotters on 16/04/2017.]
 *
 */
public class ResponseGenerator implements StatusListener{
    HashMap<String, GameState> currentGames = new HashMap<>();
    public static final String endGamePhrase = "The game has ended!";
    @Override
    public void onStatus(Status status) {
        System.out.println("\n\nUser status: " + status.getText() + " ************************************************************************");
        String output = "";

        ConfigurationBuilder statusConf = new ConfigurationBuilder();
        statusConf.setDebugEnabled(true)
                .setOAuthConsumerKey("YHOPXpCB9DURixhrBvWpQyZtv")
                .setOAuthConsumerSecret("YRM3PCwDkBrS2nxCH0xXj6nVKSIYcuTRRwT3Xf0PlA4A6bxfwH")
                .setOAuthAccessToken("858657093099950082-FBYjNOEP1Hiwm0B4zviPjvBDiOetxWv")
                .setOAuthAccessTokenSecret("QbqrlISSAFFY8Od0iWdCiYOTZDOc3ySQFy2ohyfkEDqqY");

        TwitterFactory tf = new TwitterFactory(statusConf.build());



        //access the twitter API using your twitter4j.properties file
        Twitter twitter = tf.getInstance();

//      send a tweet
//      Status makeStatus;
        String userName = status.getUser().getScreenName();
        boolean isGameStart = status.getText().contains(RunTwitterBot.startGameHashtag);

        if(status.getText().contains("End Game") && currentGames.containsKey(userName)){
            currentGames.remove(userName);
            output += "@" + userName + "\n" + "Game ended.\n If you would like to play again just call our Hashtag again";
            try {
                twitter.updateStatus(new StatusUpdate(output).inReplyToStatusId(status.getId()));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            System.out.println(output);
        } else if (isGameStart && currentGames.containsKey(userName)) {
            output += "@" + userName + "\n" + " your response was invalid, you are already in a game of poker. Try again.";
            try {
                twitter.updateStatus(new StatusUpdate(output).inReplyToStatusId(status.getId()));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            System.out.println(output);
        } else if(isGameStart && !currentGames.containsKey(userName)){
            System.out.println("Current games in play: " + currentGames);
            GameState gameState = new GameState(userName);
            currentGames.put(userName, gameState);

            output = "Welcome!\n";
            output += gameState.nextPartialRoundOutput(status.getText() + "Yes");
            try {
                twitter.updateStatus(new StatusUpdate("@" + userName + "\n" + output).inReplyToStatusId(status.getId()));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            System.out.println(output);
        } else if(currentGames.containsKey(userName)){
            String nextTweet = "@" + userName + "\n" + currentGames.get(userName).nextPartialRoundOutput(status.getText());
            try {
                twitter.updateStatus(new StatusUpdate(nextTweet).inReplyToStatusId(status.getId()));
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            if(nextTweet.contains(endGamePhrase)){
                currentGames.remove(userName);
            }

            System.out.println(nextTweet);

        }




    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int i) {

    }

    @Override
    public void onScrubGeo(long l, long l1) {

    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {

    }

    @Override
    public void onException(Exception e) {

    }
}