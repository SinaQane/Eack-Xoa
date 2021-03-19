package models;

import data.Save;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User
{
    // Account Info
    static Long lastId = (long) 0;
    public final Long id;
    public String username;
    public String password;
    public String name;

    public Date birthDate;
    public String email;
    public String phoneNumber;
    public String bio;
    public Date lastLogin;
    public Date lastSeen;

    public boolean isActive;

    // Interactions with other users
    public List<String> followersUsernames = new LinkedList<>();
    public List<String> followingsUsernames = new LinkedList<>();
    public List<String> blockedUsernames = new LinkedList<>();
    public List<String> mutedUsernames = new LinkedList<>();

    // Tweets
    public List<String> userTweets = new LinkedList<>();
    public List<String> upvotedTweets = new LinkedList<>();
    public List<String> downvotedTweets = new LinkedList<>();
    public List<String> retweetedTweets = new LinkedList<>();
    public List<String> savedTweets = new LinkedList<>();
    public Long lastTweetId = (long) 0;

    // Privacy
    public boolean infoState;
    public int lastSeenState;
    public boolean privateState;
    private int reports;

    public User(String username, String password) throws IOException
    {
        lastId ++;
        this.id = lastId;
        this.username = username;
        this.password = password;
        this.infoState = false;
        this.lastSeenState = 1;
        this.isActive = true;
        this.privateState = false;
        this.reports = 0;
        Save.changeUsername("0", this.username);
        Save.saveUser(this);
    }

    // Change name in the Settings section.
    public void setName(String name) throws IOException
    {
        this.name = name;
        Save.saveUser(this);
    }

    // Change birth date in the Settings section.
    public void setBirthDate(Date birthDate) throws IOException
    {
        this.birthDate = birthDate;
        Save.saveUser(this);
    }

    // Change username in the Settings section.
    public void setUsername(String username) throws IOException
    {
        Save.changeUsername(Objects.requireNonNullElse(this.username, "0"), username);
        this.username = username;
        Save.saveUser(this);
    }

    // Change email in the Settings section.
    public void setEmail(String email) throws IOException
    {
        Save.changeEmail(Objects.requireNonNullElse(this.email, "0"), email);
        this.email = email;
        Save.saveUser(this);
    }

    // Change phone number in the Settings section.
    public void setPhoneNumber(String phoneNumber) throws IOException
    {
        Save.changePhoneNumber(Objects.requireNonNullElse(this.phoneNumber, "0"), phoneNumber);
        this.phoneNumber = phoneNumber;
        Save.saveUser(this);
    }

    // Change the password in the Settings section.
    public void setPassword(String password) throws IOException
    {
        this.password = password;
        Save.saveUser(this);
    }

    // Updated by the app every n seconds.
    public void setLastLogin(Date lastLogin) throws IOException
    {
        this.lastLogin = lastLogin;
        Save.saveUser(this);
    }

    // Updated by the app every n seconds.
    public void setLastSeen(Date lastSeen) throws IOException
    {
        this.lastSeen = lastSeen;
        Save.saveUser(this);
    }

    // User sets private or public page state in the Settings.
    // "true" is the page is private, "false" if not.
    public void setPrivateState(boolean privateState)
    {
        this.privateState = privateState;
    }

    // User sets an info state in the Settings.
    // For Email, Phone number and Birthday. "true" for public and "false" for private.
    public void setInfoState(boolean publicInfo) throws IOException
    {
        this.infoState = publicInfo;
        Save.saveUser(this);
    }

    // User sets last seen state in the Settings.
    // "0" for no one, "1" for followings only and "2" for everyone.
    public void setLastSeenState(int lastSeenState) throws IOException
    {
        this.lastSeenState = lastSeenState;
        Save.saveUser(this);
    }

    // User sets a bio in the Sign Up section or the Settings.
    public void setBio(String bio) throws IOException
    {
        this.bio = bio;
        Save.saveUser(this);
    }

    // Deactivate the account.
    public void deactivate() throws IOException
    {
        this.setIsActive(false);
    }

    // Reactivate the account.
    public void reactivate() throws IOException
    {
        this.setIsActive(true);
    }

    // User gets reported by someone.
    // If the number of reports exceed a limit, the account will be limited.
    public void reported() throws IOException
    {
        this.setReports(this.getReports() + 1);
    }

    // User tweets something.
    public void tweet(String tweet) throws IOException
    {
        new Tweet(this, tweet);
        Save.saveUser(this);
    }

    // User removes his/her tweet.
    public void deleteTweet(Tweet tweet) throws IOException
    {
        this.userTweets.remove(tweet.id);
        tweet.deleted();
        Save.saveUser(this);
    }

    // Retweet button. It'll retweet a tweet if it's not retweeted before
    // and remove it from retweeted tweets if it had already been tweeted.
    public void retweet(Tweet tweet) throws IOException
    {
        if (retweetedTweets.contains(tweet.id))
        {
            this.retweetedTweets.remove(tweet.id);
            tweet.removeFromRetweets(this.id + "");
        }
        else
        {
            this.retweetedTweets.add(tweet.id);
            tweet.addToRetweets(this.id + "");
        }
        Save.saveTweet(tweet);
        Save.saveUser(this);
    }

    // Upvote button. It'll upvote a tweet if it hasn't been upvoted before,
    // remove its upvote if it had been upvoted before and finally,
    // turn the downvote to upvote if it has been downvoted before.
    public void upvote(Tweet tweet) throws IOException
    {
        if (downvotedTweets.contains(tweet.id))
        {
            this.addUpvote(tweet);
            tweet.addToUpvotes(this);
            this.removeDownvote(tweet);
            tweet.removeFromDownvoted(this);
        }
        else if (upvotedTweets.contains(tweet.id))
        {
            this.removeUpvote(tweet);
            tweet.removeFromUpvoted(this);
        }
        else
        {
            this.addUpvote(tweet);
            tweet.addToUpvotes(this);
        }
        Save.saveTweet(tweet);
        Save.saveUser(this);
    }

    // Downvote button. Pretty much the same as the Upvote button.
    public void downvote(Tweet tweet) throws IOException
    {
        if (upvotedTweets.contains(tweet.id))
        {
            this.addDownvote(tweet);
            tweet.addToDownvotes(this);
            this.removeUpvote(tweet);
            tweet.removeFromUpvoted(this);
        }
        else if (downvotedTweets.contains(tweet.id))
        {
            this.removeDownvote(tweet);
            tweet.removeFromDownvoted(this);
        }
        else
        {
            this.addDownvote(tweet);
            tweet.addToDownvotes(this);
        }
        Save.saveTweet(tweet);
        Save.saveUser(this);
    }

    // Additional methods
    public void setIsActive(boolean isActive) throws IOException
    {
        this.isActive = isActive;
        Save.saveUser(this);
    }

    public void setReports(int reports) throws IOException
    {
        this.reports = reports;
        Save.saveUser(this);
    }

    public boolean getIsActive()
    {
        return isActive;
    }

    public int getReports()
    {
        return reports;
    }

    public void addTweet(Tweet tweet)
    {
        this.userTweets.add(tweet.id);
    }

    public void addUpvote(Tweet tweet)
    {
        this.upvotedTweets.add(tweet.id);
    }

    public void removeUpvote(Tweet tweet)
    {
        this.upvotedTweets.remove(tweet.id);
    }

    public void addDownvote(Tweet tweet)
    {
        this.downvotedTweets.add(tweet.id);
    }

    public void removeDownvote(Tweet tweet)
    {
        this.downvotedTweets.remove(tweet.id);
    }
}