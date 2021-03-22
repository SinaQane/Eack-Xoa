package utils;

import data.Load;
import models.User;

import java.io.IOException;
import java.util.ArrayList;

public class UsersCli
{
    User user;
    public UsersCli(User user)
    {
        this.user = user;
    }

    public int followersPages(int perPage)
    {
        if (user.followers.size() % perPage == 0)
            return user.followers.size()/perPage;
        return user.followers.size()/perPage + 1;
    }

    public int followingsPages(int perPage)
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

    public String printFollower(ArrayList<String> page, int index) throws IOException
    {
        if (index < page.size())
        {
            User loaded = Load.findUser(Long.parseLong(page.get(index)));
            StringBuilder result = new StringBuilder();
            if (user.followings.contains(loaded.id + ""))
                result.append(ConsoleColors.CYAN).append("Following").append("\n");
            else
                result.append(ConsoleColors.CYAN).append("Not Following").append("\n");
            result.append(ConsoleColors.CYAN).append(loaded.name).append("\n")
                    .append("@").append(loaded.username).append("\n").append(loaded.bio);
            if (user.muted.contains(loaded.id + ""))
                result.append("\n").append(ConsoleColors.RED + "Muted");
            return result.toString();
        }
        return "";
    }

    public String printFollowing(ArrayList<String> page, int index) throws IOException
    {
        if (index < page.size())
        {
            User loaded = Load.findUser(Long.parseLong(page.get(index)));
            StringBuilder result = new StringBuilder();
            result.append(ConsoleColors.CYAN).append(loaded.name).append("\n")
                    .append("@").append(loaded.username).append("\n").append(loaded.bio);
            if (user.muted.contains(loaded.id + ""))
                result.append("\n").append(ConsoleColors.RED + "Muted");
            return result.toString();
        }
        return "";
    }
}
