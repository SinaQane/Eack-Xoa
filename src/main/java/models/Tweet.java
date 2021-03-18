package models;

import data.Save;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Tweet
{
    // Tweet info
    /* Tweet's id will be in the form "num1-num2" where "num1" is its owner's id,
    and "num2" is its number in owner's tweets' arrangement
    */
    public final String id;
    public final String owner;
    public final String text;
    public final Date tweetTime;
    public boolean visible;

    // Interactions of users with this tweet
    public List<String> likes = new ArrayList<>();
    public List<String> dislikes = new ArrayList<>();
    public List<String> comments = new LinkedList<>();
    public int reports;

    public Tweet(User owner, String text) throws IOException
    {
        owner.lastTweetId++;
        this.id = owner.id + "-" + owner.lastTweetId;
        this.owner = owner.username;
        this.text = text;
        this.reports = 0;
        this.tweetTime = new Date();
        this.visible = true;
        // TODO add tweet to its owner's tweets
        Save.saveTweet(this);
    }

}
