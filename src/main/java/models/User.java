package models;

import data.Save;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User
{
    // Users directory for saving user's data
    // private final File usersDirectory = Config.getConfig("mainConfig").getProperty(File.class, "usersDirectory");

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
    public List<String> likedTweets = new LinkedList<>();
    public List<String> dislikedTweets = new LinkedList<>();
    public List<String> retweetedTweets = new LinkedList<>();
    public List<String> savedTweets = new LinkedList<>();
    public Long lastTweetId = (long) 0;

    // Privacy
    public boolean publicInfo; // For Email, Phone number and Birthday. "true" for public and "false" for private.
    public int lastSeenState; // "0" for no one, "1" for followings only and "2" for everyone.
    public boolean privatePage; // "true" is the page is private, "false" if not.
    private int reports; // If the number of reports exceed a limit, the account will be limited.

    public User(String username, String password) throws IOException
    {
        lastId ++;
        this.id = lastId;
        this.username = username;
        this.password = password;
        this.publicInfo = false;
        this.lastSeenState = 1;
        this.isActive = true;
        this.privatePage = false;
        this.reports = 0;
        Save.changeUsername("0", this.username);
        Save.saveUser(this);
    }

    public void setName(String name) throws IOException
    {
        this.name = name;
        Save.saveUser(this);
    }

    public void setBirthDate(Date birthDate) throws IOException
    {
        this.birthDate = birthDate;
        Save.saveUser(this);
    }

    public void setUsername(String username) throws IOException
    {
        Save.changeUsername(Objects.requireNonNullElse(this.username, "0"), username);
        this.username = username;
        Save.saveUser(this);
    }

    public void setEmail(String email) throws IOException
    {
        Save.changeEmail(Objects.requireNonNullElse(this.email, "0"), email);
        this.email = email;
        Save.saveUser(this);
    }

    public void setPhoneNumber(String phoneNumber) throws IOException
    {
        Save.changePhoneNumber(Objects.requireNonNullElse(this.phoneNumber, "0"), phoneNumber);
        this.phoneNumber = phoneNumber;
        Save.saveUser(this);
    }

    public void setPassword(String password) throws IOException
    {
        this.password = password;
        Save.saveUser(this);
    }

    public void setLastLogin(Date lastLogin) throws IOException
    {
        this.lastLogin = lastLogin;
        Save.saveUser(this);
    }

    public void setLastSeen(Date lastSeen) throws IOException
    {
        this.lastSeen = lastSeen;
        Save.saveUser(this);
    }

    public void setPublicInfo(boolean publicInfo) throws IOException
    {
        this.publicInfo = publicInfo;
        Save.saveUser(this);
    }

    public void setLastSeenState(int lastSeenState) throws IOException
    {
        this.lastSeenState = lastSeenState;
        Save.saveUser(this);
    }

    public void setBio(String bio) throws IOException
    {
        this.bio = bio;
        Save.saveUser(this);
    }

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

    public void deactivate() throws IOException
    {
        this.setIsActive(false);
    }

    public void reactivate() throws IOException
    {
        this.setIsActive(true);
    }

    public void reported() throws IOException
    {
        this.setReports(this.getReports() + 1);
    }

    public int getReports()
    {
        return reports;
    }

}