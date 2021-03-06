package models;

import data.Load;
import data.Save;
import utils.ConsoleColors;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.*;

public class User
{
    static private final Logger logger = LogManager.getLogger(User.class);

    // Account Info
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
    public HashMap<String, List<Long>> groups = new HashMap<>();
    public List<String> followers = new LinkedList<>();
    public List<String> followings = new LinkedList<>();
    public List<String> blocked = new LinkedList<>();
    public List<String> muted = new LinkedList<>();
    public List<String> reported = new LinkedList<>();
    public List<String> requests = new LinkedList<>();
    public List<String> pending = new LinkedList<>();

    // Notifications
    public List<String> newNotifications = new LinkedList<>();
    public List<String> oldNotifications = new LinkedList<>();

    // Tweets
    public HashMap<String, Long> homePageTweets = new HashMap<>();
    public List<String> userTweets = new LinkedList<>();
    public List<String> retweetedTweets = new LinkedList<>();
    public List<String> upvotedTweets = new LinkedList<>();
    public List<String> downvotedTweets = new LinkedList<>();
    public List<String> savedTweets = new LinkedList<>();
    public List<String> reportedTweets = new LinkedList<>();
    public Long lastTweetId = (long) 0;

    //Messages
    public HashMap<Long, ArrayList<String[]>> directMessages = new HashMap<>();

    // Privacy
    public boolean infoState;
    public int lastSeenState;
    public boolean privateState;
    private int reports;
    private long reportedUntil;

    // Preferences
    public int tweetsPerPage;
    public int peoplePerPage;
    public int notificationsPerPage;

    public User(String username, String password) throws IOException
    {
        Save.changeLastId(Load.loadLastId() + 1);
        this.id = Load.loadLastId();
        this.username = username;
        this.password = password;
        this.infoState = false;
        this.lastSeenState = 1;
        this.isActive = true;
        this.isPermitted = true;
        this.privateState = false;
        this.reports = 0;
        this.tweetsPerPage = 5;
        this.peoplePerPage = 10;
        this.notificationsPerPage = 15;
        Save.changeUsername("0", this.username);
        Save.saveUser(this);
    }

    // Change name in the Settings section.
    public void setName(String name) throws IOException
    {
        this.name = name;
        logger.warn(this.id + "'s name was changed.");
        Save.saveUser(this);
    }

    // Change birth date in the Settings section.
    public void setBirthDate(Date birthDate) throws IOException
    {
        this.birthDate = birthDate;
        logger.warn(this.id + "'s birthdate was changed.");
        Save.saveUser(this);
    }

    // Change username in the Settings section.
    public void setUsername(String username) throws IOException
    {
        Save.changeUsername(Objects.requireNonNullElse(this.username, "0"), username);
        this.username = username;
        logger.fatal(this.id + "'s username was changed.");
        Save.saveUser(this);
    }

    // Change email in the Settings section.
    public void setEmail(String email) throws IOException
    {
        Save.changeEmail(Objects.requireNonNullElse(this.email, "0"), email);
        this.email = email;
        logger.fatal(this.id + "'s email was changed.");
        Save.saveUser(this);
    }

    // Change phone number in the Settings section.
    public void setPhoneNumber(String phoneNumber) throws IOException
    {
        Save.changePhoneNumber(Objects.requireNonNullElse(this.phoneNumber, "0"), phoneNumber);
        this.phoneNumber = phoneNumber;
        logger.fatal(this.id + "'s phonenumber was changed.");
        Save.saveUser(this);
    }

    // Change the password in the Settings section.
    public void setPassword(String password) throws IOException
    {
        this.password = password;
        logger.error(this.id + "'s password was changed.");
        Save.saveUser(this);
    }

    // Sets everytime someone logs in
    public void setLastLogin(Date lastLogin) throws IOException
    {
        this.lastLogin = lastLogin;
        logger.error(this.id + " logged in.");
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
    public void setPrivateState(boolean privateState) throws IOException
    {
        this.privateState = privateState;
        logger.debug(this.id + " changed their private state.");
        Save.saveUser(this);
    }

    // User sets an info state in the Settings.
    // For Email, Phone number and Birthday. "true" for public and "false" for private.
    public void setInfoState(boolean publicInfo) throws IOException
    {
        this.infoState = publicInfo;
        logger.debug(this.id + " changed their info state.");
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
        logger.info(this.id + " changed their bio.");
        Save.saveUser(this);
    }

    // Deactivate the account.
    public void deactivate() throws IOException
    {
        logger.fatal("account " + this.id + " was reactivated.");
        this.setIsActive(false);
    }

    // Reactivate the account.
    public void reactivate() throws IOException
    {
        this.setIsActive(true);
    }

    // User gets reported by someone.
    // If the number of reports exceed a limit, the account will be limited.
    public void reported(User user) throws IOException //
    {
        if (!this.equals(user))
        {
            if (!user.reported.contains(this.id + ""))
            {
                logger.debug("account " + user.id + " reported " + this.id + ".");
                user.reported.add(this.id + "");
                this.reports++;
                if (this.reports >= 10)
                {
                    this.isPermitted = false;
                    Date reportTime = new Date();
                    this.reportedUntil = reportTime.getTime() + 604800000; // A week of suspension
                }
                Save.saveUser(this);
            }
            else
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "You have already reported this user.");
        }
        else
            System.out.println(ConsoleColors.RED_BRIGHT + "You can't report yourself!");
    }

    // User tweets something.
    public void tweet(String tweet) throws IOException
    {
        if (this.reportedUntil != 0)
        {
            if (this.reportedUntil > new Date().getTime())
                System.out.println(ConsoleColors.RED_BRIGHT + "Unfortunately you are suspended. You can't tweet right now.");
            else
            {
                logger.debug("account " + this.id + " just tweeted something.");
                this.reportedUntil = 0;
                new Tweet(this, tweet);
                Save.saveUser(this);
            }
        }
        else
        {
            logger.debug("account " + this.id + " just tweeted something.");
            new Tweet(this, tweet);
            Save.saveUser(this);
        }
    }

    // User puts a comment under another tweet
    public void comment(Tweet upperTweet, String comment) throws IOException
    {
        if (this.reportedUntil != 0)
        {
            if (this.reportedUntil > new Date().getTime())
                System.out.println(ConsoleColors.RED_BRIGHT + "Unfortunately you are suspended. You can't leave comments right now.");
            else
            {
                logger.debug("account " + this.id + " left a comment to " + upperTweet.id + ".");
                this.reportedUntil = 0;
                Tweet tweet = new Tweet(this, comment);
                tweet.setUpperTweet(upperTweet.id);
                upperTweet.comments.add("1-" + this.id + "-" + tweet.id);
                Save.saveTweet(upperTweet);
                Save.saveUser(this);
            }
        }
        else
        {
            logger.debug("account " + this.id + " left a comment to " + upperTweet.id + ".");
            Tweet tweet = new Tweet(this, comment);
            tweet.setUpperTweet(upperTweet.id);
            upperTweet.comments.add("1-" + this.id + "-" + tweet.id);
            Save.saveTweet(upperTweet);
            Save.saveUser(this);
        }
    }

    // User removes his/her tweet.
    public void deleteTweet(Tweet tweet) throws IOException
    {
        if (tweet.getOwnerId() == this.id)
        {
            logger.warn("account " + this.id + " deleted tweet " + tweet.id + ".");
            this.userTweets.remove(tweet.id);
            this.homePageTweets.remove("1-" + this.id + "-" + tweet.id);
            tweet.deleted();
            Save.saveUser(this);
        }
        else
        {
            System.out.println(ConsoleColors.RED_BRIGHT + "You don't have the permission to delete this tweet.");
        }
    }

    // This user follows another user.
    public void follow(User user) throws IOException
    {
        if (!this.followings.contains(user.id + ""))
        {
            logger.debug("account " + this.id + " started following " + user.id + ".");
            this.followings.add(user.id + "");
            user.followers.add(this.id + "");
            user.newNotifications.add(this.username + " started following you.");
            Save.saveUser(user);
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW_BRIGHT + "You have already followed this user.");
    }

    // This user unfollows another user.
    public void unfollow(User user) throws IOException
    {
        if (this.followings.contains(user.id + ""))
        {
            logger.debug("account " + this.id + " stopped following " + user.id + ".");
            this.followings.remove(user.id + "");
            user.followers.remove(this.id + "");
            user.newNotifications.add(this.username + " unfollowed you.");
            Save.saveUser(user);
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW_BRIGHT + "You are not following this user.");
    }

    // Request button. This user sends/unsends a request to follow another user.
    public void request(User user) throws IOException
    {
        if (this.equals(user))
            System.out.println(ConsoleColors.RED_BRIGHT + "You can't follow yourself!");
        else
        {
            if (!user.privateState)
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "This page isn't private.");
            else
            {
                if (this.pending.contains(user.id + ""))
                {
                    logger.debug("account " + this.id + " took back the follow request to " + user.id + ".");
                    this.pending.remove(user.id + "");
                    user.requests.remove(this.id + "");
                }
                else
                {
                    logger.debug("account " + this.id + " sent a follow request to " + user.id + ".");
                    this.pending.add(user.id + "");
                    user.requests.add(this.id + "");
                }
                Save.saveUser(user);
                Save.saveUser(this);
            }
        }
    }

    // This user accepts someone's request
    public void accept(User user) throws IOException
    {
        logger.debug("account " + this.id + " accepted a follow request from " + user.id + ".");
        this.requests.remove(user.id + "");
        this.followers.add(user.id + "");
        user.pending.remove(this.id + "");
        user.followings.add(this.id + "");
        this.newNotifications.add(user.username + " started following you.");
        user.newNotifications.add(this.username + " accepted your follow request.");
        Save.saveUser(user);
        Save.saveUser(this);
    }

    // This user rejects someone's request
    public void rejectWithNotifications(User user) throws IOException
    {
        logger.warn("account " + this.id + " rejected a follow request from " + user.id + ".");
        this.requests.remove(user.id + "");
        user.pending.remove(this.id + "");
        user.newNotifications.add(this.username + " rejected your follow request.");
        Save.saveUser(user);
        Save.saveUser(this);
    }

    public void rejectWithoutNotifications(User user) throws IOException
    {
        logger.warn("account " + this.id + " rejected a follow request from " + user.id + ".");
        this.requests.remove(user.id + "");
        user.pending.remove(this.id + "");
        Save.saveUser(user);
        Save.saveUser(this);
    }

    // This user accepts all the request
    public void acceptAll() throws IOException
    {
        for (String userId : this.requests)
        {
            User user = Load.findUser(Long.parseLong(userId));
            this.accept(user);
            logger.debug("account " + this.id + " accepted a follow request from " + user.id + ".");
        }
    }

    // Refresh all notifications
    public void refresh() throws IOException
    {
        while (this.newNotifications.size() > 0)
        {
            this.oldNotifications.add(this.newNotifications.get(0));
            this.newNotifications.remove(0);
            Save.saveUser(this);
        }
    }

    // Block button. This user blocks/unblocks another user.
    public void block(User user) throws IOException
    {
        if (this.equals(user))
            System.out.println(ConsoleColors.RED_BRIGHT + "You can't block yourself!");
        else
        {
            if (!this.blocked.contains(user.id + ""))
            {
                logger.fatal("account " + this.id + " blocked " + user.id + ".");
                this.blocked.add(user.id + "");
                this.followings.remove(user.id + "");
                this.followers.remove(user.id + "");
                user.followings.remove(this.id + "");
                user.followers.remove(this.id + "");
                Save.saveUser(user);
            }
            else
            {
                logger.debug("account " + this.id + " unblocked " + user.id + ".");
                this.blocked.remove(user.id + "");
            }
            Save.saveUser(this);
        }
    }

    public void removeFollower(User user) throws IOException
    {
        if (this.followers.contains(user.id + ""))
        {
            logger.warn("account " + this.id + " removed " + user.id + " from their followers.");
            this.followers.remove(user.id + "");
            Save.saveUser(user);
            Save.saveUser(this);
        }
        else
            System.out.println(ConsoleColors.YELLOW_BRIGHT + "This user doesn't follow you.");
    }

    // Mute button. This user mutes/unmutes another user.
    public void mute(User user) throws IOException
    {
        if (this.equals(user))
            System.out.println(ConsoleColors.RED_BRIGHT + "You can't mute yourself!");
        else
            {
            if (!this.muted.contains(user.id + ""))
            {
                logger.warn("account " + this.id + " muted " + user.id + ".");
                this.muted.add(user.id + "");
                Save.saveUser(this);
                System.out.println(ConsoleColors.GREEN_BRIGHT + "User muted.");
            }
            else
            {
                logger.debug("account " + this.id + " unmuted " + user.id + ".");
                this.muted.remove(user.id + "");
                Save.saveUser(this);
                System.out.println(ConsoleColors.GREEN_BRIGHT + "User unmuted.");
            }
        }
    }

    // Adds direct message
    public void addMessage(Long key, String value) throws IOException
    {
        ArrayList<String[]> arrayList;
        if (this.directMessages.containsKey(key))
            arrayList = this.directMessages.get(key);
        else
            arrayList = new ArrayList<>();
        String[] messageArray = new String[]{value, "f"};
        arrayList.add(messageArray);
        this.directMessages.put(key, arrayList);
        Save.saveUser(this);
    }

    // Sets the number of tweets per page
    public void setTweetsPerPage(int tweetsPerPage) throws IOException
    {
        this.tweetsPerPage = tweetsPerPage;
        logger.info("account " + this.id + " changed their preferences.");
        Save.saveUser(this);
    }

    // Sets the number of people per page
    public void setPeoplePerPage(int peoplePerPage) throws IOException
    {
        this.peoplePerPage = peoplePerPage;
        logger.info("account " + this.id + " changed their preferences.");
        Save.saveUser(this);
    }

    // Sets the number of notifications per page
    public void setNotificationsPerPage(int notificationsPerPage) throws IOException
    {
        this.notificationsPerPage = notificationsPerPage;
        logger.info("account " + this.id + " changed their preferences.");
        Save.saveUser(this);
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
