package models;

import data.Load;
import data.Save;
import utils.ConsoleColors;

import java.io.IOException;
import java.util.*;

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
    public String upperTweet;

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
        owner.homePageTweets.put("1-" + owner.id + "-" + this.id, this.tweetTime.getTime());
        this.upperTweet = "";
        Save.saveTweet(this);
    }

    // Tweet gets deleted
    public void deleted() throws IOException
    {
        this.visible = false;
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

    // Retweet button. It'll retweet a tweet if it's not retweeted before
    // and remove it from retweeted tweets if it had already been tweeted.
    public void retweet(User user) throws IOException
    {
        if (user.retweetedTweets.contains(this.id))
        {
            user.retweetedTweets.remove(this.id);
            user.homePageTweets.remove("0-" + user.id + "-" + this.id);
            this.removeFromRetweets(user.id + "");
        }
        else
        {
            user.retweetedTweets.add(this.id);
            Date date = new Date();
            user.homePageTweets.put("0-" + user.id + "-" + this.id, date.getTime());
            this.addToRetweets(user.id + "");
        }
        Save.saveTweet(this);
        Save.saveUser(user);
    }

    // Upvote button. It'll upvote a tweet if it hasn't been upvoted before,
    // remove its upvote if it had been upvoted before and finally,
    // turn the downvote to upvote if it has been downvoted before.
    public void upvote(User user) throws IOException
    {
        if (user.downvotedTweets.contains(this.id))
        {
            user.addUpvote(this);
            this.addToUpvotes(user);
            user.removeDownvote(this);
            this.removeFromDownvoted(user);
        }
        else if (user.upvotedTweets.contains(this.id))
        {
            user.removeUpvote(this);
            this.removeFromUpvoted(user);
        }
        else
        {
            user.addUpvote(this);
            this.addToUpvotes(user);
        }
        Save.saveTweet(this);
        Save.saveUser(user);
    }

    // Downvote button. Pretty much the same as the Upvote button.
    public void downvote(User user) throws IOException
    {
        if (user.upvotedTweets.contains(this.id))
        {
            user.addDownvote(this);
            this.addToDownvotes(user);
            user.removeUpvote(this);
            this.removeFromUpvoted(user);
        }
        else if (user.downvotedTweets.contains(this.id))
        {
            user.removeDownvote(this);
            this.removeFromDownvoted(user);
        }
        else
        {
            user.addDownvote(this);
            this.addToDownvotes(user);
        }
        Save.saveTweet(this);
        Save.saveUser(user);
    }

    // User saves this tweet.
    public void save(User user) throws IOException
    {
        if (!user.savedTweets.contains(this.id))
        {
            user.savedTweets.add(this.id);
            Save.saveUser(user);
        }
        else
            System.out.println(ConsoleColors.YELLOW_BRIGHT + "You have already saved this tweet.");
    }

    // User unsaves (?) this tweet.
    public void unsave(User user) throws IOException
    {
        if (user.savedTweets.contains(this.id))
        {
            user.savedTweets.remove(this.id);
            Save.saveUser(user);
        }
        else
            System.out.println(ConsoleColors.YELLOW_BRIGHT + "This tweet isn't in your saved tweets.");
    }

    // User reports this tweets.
    public void report(User user) throws IOException
    {
        if (user.id != this.getOwnerId())
        {
            if (!user.reportedTweets.contains(this.id))
            {
                this.reports++;
                if (this.reports>=10)
                {
                    this.visible = false;
                    Load.findUser(this.owner).reported(user);
                }
                Save.saveTweet(this);
                user.reportedTweets.add(this.id);
                Save.saveUser(user);
            }
            else
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "You have already reported this tweet.");
        }
        else
            System.out.println(ConsoleColors.RED_BRIGHT + "You can't report your own tweet!");
    }

    // For comments
    public void setUpperTweet(String upperTweet) throws IOException
    {
        this.upperTweet = upperTweet;
        Save.saveTweet(this);
    }

    // User shares tweet with a list of users
    public void share(User user, ArrayList<User> users) throws IOException
    {
        for (User dest : users)
        {
            user.addMessage(dest.id, "S:" + text);
            Save.saveUser(user);
            dest.addMessage(user.id, "R:" + text);
            Save.saveUser(dest);
        }
    }
}