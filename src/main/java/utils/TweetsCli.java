package utils;

import data.Load;
import models.Tweet;

import java.io.IOException;
import java.util.ArrayList;

public class TweetsCli
{
    ArrayList<String> tweets;

    public TweetsCli(ArrayList<String> tweets)
    {
        this.tweets = tweets;
    }

    public int numberOfPages(int perPage)
    {
        if (this.tweets.size() % perPage == 0)
            return this.tweets.size()/perPage;
        return this.tweets.size()/perPage + 1;
    }

    public ArrayList<String> page(int page, int perPage) // Starts with page 1
    {
        ArrayList<String> result = new ArrayList<>();
        int start = this.tweets.size() - ((page + 1) * perPage);
        int index = 0;
        int exception = 0;
        do {
            try
            {
                result.add(this.tweets.get(start + index));
            }
            catch (Exception e)
            {
                exception++;
            }
            index++;
        } while (index != perPage);
        return result;
    }

    public String printTweet(ArrayList<String> page, int index) throws IOException // index starts from 0
    {
        if (index < page.size())
        {
            String[] tweetParts = page.get(index).split("-");
            String tweetId = tweetParts[2] + "-" + tweetParts[3];
            Tweet tweet = Load.findTweet(tweetId);
            StringBuilder response = new StringBuilder();
            if (tweetParts[0].equals("0"))
                response.append("* Retweeted by ").append(Load.findUser(Long.parseLong(tweetParts[1])).username).append("\n");
            response.append("@").append(tweet.getOwner()).append(":\n")
                    .append(tweet.getText()).append("\n")
                    .append(tweet.getKarma()).append(" Karma - ")
                    .append(tweet.getCommentsCount()).append(" Comments - ")
                    .append(tweet.getRetweetsCount()).append(" Retweets");
            return response.toString();
        }
        return "";
    }
}