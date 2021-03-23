package utils;

import data.Load;
import models.User;

import java.io.IOException;
import java.util.ArrayList;

public class UsersCli
{
    User me;
    User user;

    public UsersCli(User me, User user)
    {
        this.me = me;
        this.user = user;
    }

    public int numberOfFollowersPages(int perPage)
    {
        if (user.followers.size() % perPage == 0)
            return user.followers.size()/perPage;
        return user.followers.size()/perPage + 1;
    }

    public int numberOfFollowingsPages(int perPage)
    {
        if (user.followings.size() % perPage == 0)
            return user.followings.size()/perPage;
        return user.followings.size()/perPage + 1;
    }

    public ArrayList<String> followersPage(int page, int perPage)
    {
        ArrayList<String> result = new ArrayList<>();
        int start = user.followers.size() - ((page + 1) * perPage);
        int index = 0;
        int exception = 0;
        do {
            try
            {
                result.add(user.followers.get(start + index));
            }
            catch (Exception e)
            {
                exception++;
            }
            index++;
        } while (index != perPage);
        return result;
    }

    public ArrayList<String> followingsPage(int page, int perPage)
    {
        ArrayList<String> result = new ArrayList<>();
        int start = user.followings.size() - ((page + 1) * perPage);
        int index = 0;
        int exception = 0;
        do {
            try
            {
                result.add(user.followings.get(start + index));
            }
            catch (Exception e)
            {
                exception++;
            }
            index++;
        } while (index != perPage);
        return result;
    }

    public String printUser(ArrayList<String> page, int index) throws IOException
    {
        if (index < page.size())
        {
            User loaded = Load.findUser(Long.parseLong(page.get(index)));
            StringBuilder result = new StringBuilder();
            if (me.followings.contains(loaded.id + ""))
                result.append(ConsoleColors.GREEN).append("Following").append("\n");
            else if (me.pending.contains(user.id + ""))
                result.append(ConsoleColors.YELLOW).append("Pending").append("\n");
            else
                result.append(ConsoleColors.RED).append("Not Following").append("\n");
            result.append(ConsoleColors.CYAN).append(loaded.name).append("\n")
                    .append("@").append(loaded.username).append("\n").append(loaded.bio);
            if (me.muted.contains(loaded.id + ""))
                result.append("\n").append(ConsoleColors.YELLOW + "Muted");
            if (user.followings.contains(me.id + ""))
                result.append("\n").append(ConsoleColors.WHITE + "Follows you");
            return result.toString();
        }
        return "";
    }

    public String printCurrentUser(ArrayList<String> page, int index) throws IOException
    {
        if (index < page.size())
        {
            User loaded = Load.findUser(Long.parseLong(page.get(index)));
            StringBuilder result = new StringBuilder();
            if (me.followings.contains(loaded.id + ""))
                result.append(ConsoleColors.GREEN_BRIGHT).append("Following").append("\n");
            else if (me.pending.contains(user.id + ""))
                result.append(ConsoleColors.YELLOW_BRIGHT).append("Pending").append("\n");
            else
                result.append(ConsoleColors.RED_BRIGHT).append("Not Following").append("\n");
            result.append(ConsoleColors.CYAN_BRIGHT).append(loaded.name).append("\n")
                    .append("@").append(loaded.username).append("\n").append(loaded.bio);
            if (me.muted.contains(loaded.id + ""))
                result.append("\n").append(ConsoleColors.YELLOW_BRIGHT + "Muted");
            if (user.followings.contains(me.id + ""))
                result.append("\n").append(ConsoleColors.WHITE_BRIGHT + "Follows you");
            return result.toString();
        }
        return "";
    }
}