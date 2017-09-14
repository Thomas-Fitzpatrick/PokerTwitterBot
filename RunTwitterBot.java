package Poker;

/**
 * Created by The Harlem Codetrotters on 31/03/2017.
 */

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class RunTwitterBot {
    public static final String startGameHashtag = "#DealMeInHC";

    //if something goes wrong, we can see a TwitterException
    public static void main(String... args) throws TwitterException{

        ConfigurationBuilder streamConf = new ConfigurationBuilder();
        streamConf.setDebugEnabled(true)
                .setOAuthConsumerKey("YHOPXpCB9DURixhrBvWpQyZtv")
                .setOAuthConsumerSecret("YRM3PCwDkBrS2nxCH0xXj6nVKSIYcuTRRwT3Xf0PlA4A6bxfwH")
                .setOAuthAccessToken("858657093099950082-FBYjNOEP1Hiwm0B4zviPjvBDiOetxWv")
                .setOAuthAccessTokenSecret("QbqrlISSAFFY8Od0iWdCiYOTZDOc3ySQFy2ohyfkEDqqY");


        TwitterStream twitterStream = new TwitterStreamFactory(streamConf.build()).getInstance();

        StatusListener listener = new ResponseGenerator();

        FilterQuery fq = new FilterQuery();
        String keywords[] = {startGameHashtag, "@" + twitterStream.getScreenName()};
        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);



        //print a message so we know when it finishes
    }
}