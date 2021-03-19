package models;

import data.Save;
import utils.ConsoleColors;

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
    public boolean isPermitted;

    // Interactions with other users
    public List<String> followers = new LinkedList<>();
    public List<String> followings = new LinkedList<>();
    public List<String> blocked = new LinkedList<>();
    public List<String> muted = new LinkedList<>();
    public List<String> reported = new LinkedList<>();

    // Tweets
    public List<String> timelineTweets = new LinkedList<>();
    public List<String> userTweets = new LinkedList<>();
    public List<String> retweetedTweets = new LinkedList<>();
    public List<String> upvotedTweets = new LinkedList<>();
    public List<String> downvotedTweets = new LinkedList<>();
    public List<String> savedTweets = new LinkedList<>();
    public List<String> reportedTweets = new LinkedList<>();
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
        this.isPermitted = true; // TODO report tweet and user
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
        this.reports++;
        if (this.reports >= 10)
        {
            this.isPermitted = false;
            // TODO user won't be able to tweet, comment and retweet for n seconds after getting suspended.
        }
        Save.saveUser(this);
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
        this.timelineTweets.remove(tweet.id);
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
            this.timelineTweets.remove(tweet.id);
            tweet.removeFromRetweets(this.id + "");
        }
        else
        {
            this.retweetedTweets.add(tweet.id);
            this.timelineTweets.add(tweet.id);
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

    // Save tweet.
    public void save(Tweet tweet) throws IOException
    {
        if (!savedTweets.contains(tweet.id))
        {
            this.savedTweets.add(tweet.id);
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "You have already saved this tweet.");
    }

    // Unsave (?) tweet.
    public void unsave(Tweet tweet) throws IOException
    {
        if (savedTweets.contains(tweet.id))
        {
            this.savedTweets.remove(tweet.id);
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "This tweet isn't in your saved tweets.");
    }

    // This user follows another user.
    public void follow(User user) throws IOException
    {
        if (!this.followings.contains(user.id + ""))
        {
            this.followings.add(user.id + "");
            user.followers.add(this.id + "");
            Save.saveUser(user);
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "You have already followed this user.");
    }

    // This user unfollows another user.
    public void unfollow(User user) throws IOException
    {
        if (this.followings.contains(user.id + ""))
        {
            this.followings.remove(user.id + "");
            user.followers.remove(this.id + "");
            Save.saveUser(user);
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "You are not following this user.");
    }

    // This user blocks another user.
    public void block(User user) throws IOException
    {
        if (!this.blocked.contains(user.id + ""))
        {
            this.blocked.add(user.id + "");
            this.followings.remove(user.id + "");
            this.followers.remove(user.id + "");
            user.followings.remove(this.id + "");
            user.followers.remove(this.id + "");
            Save.saveUser(user);
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "You have already blocked this user.");
    }

    // This user unblocks another user.
    public void unblock(User user) throws IOException
    {
        if (this.blocked.contains(user.id + ""))
        {
            this.blocked.remove(user.id + "");
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "This user isn't in your Blacklist.");
    }

    // This user mutes another user.
    public void mute(User user) throws IOException
    {
        if (!this.muted.contains(user.id + ""))
        {
            this.muted.add(user.id + "");
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "You have already muted this user.");
    }

    // This user unmutes (?) another user.
    public void unmute(User user) throws IOException
    {
        if (this.muted.contains(user.id + ""))
        {
            this.muted.add(user.id + "");
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "This user wasn't muted.");
    }

    // This user reports a tweets.
    public void report(Tweet tweet) throws IOException
    {
        if (!this.reportedTweets.contains(tweet.id))
        {
            tweet.reported();
            this.reportedTweets.add(tweet.id);
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "You have already reported this tweet.");
    }

    // This user reports another user.
    public void report(User user) throws IOException
    {
        if (!this.reported.contains(user.id + ""))
        {
            user.reported();
            this.reported.add(user.id + "");
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW + "You have already reported this user.");
    }

    // Additional methods
    public void setIsActive(boolean isActive) throws IOException
    {
        this.isActive = isActive;
        Save.saveUser(this);
    }

    public boolean getIsActive()
    {
        return isActive;
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