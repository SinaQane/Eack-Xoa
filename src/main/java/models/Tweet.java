package models;

import data.Load;
import data.Save;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Tweet
{
    // Tweet info
    /* Tweet's ID will be in the form "num1-num2" where "num1" is its owner's ID,
    and "num2" is its number in owner's tweets' arrangement
    */
    public final String id;
    public final long owner;
    public final String text;
    public final Date tweetTime;
    public boolean visible;

    // Interactions of users with this tweet
    public List<String> upvotes = new ArrayList<>();
    public List<String> downvotes = new ArrayList<>();
    public List<String> comments = new LinkedList<>();
    public List<String> retweets = new ArrayList<>();
    public int reports;

    public Tweet(User owner, String text) throws IOException
    {
        owner.lastTweetId++;
        this.id = owner.id + "-" + owner.lastTweetId;
        this.owner = owner.id;
        this.text = text;
        this.reports = 0;
        this.tweetTime = new Date();
        this.visible = true;
        owner.userTweets.add(this.id);
        owner.timelineTweets.add(this.id);
        Save.saveTweet(this);
    }

    // Tweet gets deleted
    public void deleted() throws IOException
    {
        this.visible = false;
        Save.saveTweet(this);
    }

    // Tweet gets reported
    public void reported() throws IOException
    {
        this.reports++;
        if (this.reports>=10)
        {
            this.visible = false;
            Load.findUser(this.owner).reported();
        }
        Save.saveTweet(this);
    }

    public void addToRetweets(String string)
    {
        retweets.add(string);
    }

    public void removeFromRetweets(String string)
    {
        retweets.remove(string);
    }

    public void addToUpvotes(User user)
    {
        this.upvotes.add(user.id + "");
    }

    public void removeFromUpvoted(User user)
    {
        this.upvotes.remove(user.id + "");
    }

    public void addToDownvotes(User user)
    {
        this.downvotes.add(user.id + "");
    }

    public void removeFromDownvoted(User user)
    {
        this.downvotes.remove(user.id + "");
    }

    // Returns owner's username
    public String getOwner() throws IOException
    {
        return Load.findUser(owner).username;
    }

    // Returns owner's ID
    public long getOwnerId()
    {
        return owner;
    }

    // Following methods are for representing tweet in CLI.
    public String getText()
    {
        return text;
    }

    public int getKarma()
    {
        return upvotes.size() - downvotes.size();
    }

    public int getCommentsCount()
    {
        return comments.size();
    }

    public int getRetweetsCount()
    {
        return retweets.size();
    }
}
