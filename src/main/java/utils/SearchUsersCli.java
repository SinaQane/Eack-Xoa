package utils;

import data.Load;
import models.User;

import java.io.IOException;
import java.util.ArrayList;

public class SearchUsersCli
{
    User me;
    ArrayList<String> users = new ArrayList<>();

    public SearchUsersCli(User me, ArrayList<String> users)
    {
        this.me = me;
        this.users = users;
    }

    public int numberOfPages(int perPage)
    {
        if (users.size() % perPage == 0)
            return users.size()/perPage;
        return users.size()/perPage + 1;
    }

    public ArrayList<String> usersPage(int page, int perPage)
    {
        ArrayList<String> result = new ArrayList<>();
        int start = users.size() - ((page + 1) * perPage);
        int index = 0;
        int exception = 0;
        do {
            try
            {
                result.add(users.get(start + index));
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
            else if (me.pending.contains(loaded.id + ""))
                result.append(ConsoleColors.YELLOW).append("Pending").append("\n");
            else
                result.append(ConsoleColors.RED).append("Not Following").append("\n");
            result.append(ConsoleColors.CYAN).append(loaded.name).append("\n")
                    .append("@").append(loaded.username).append("\n").append(loaded.bio);
            if (me.muted.contains(loaded.id + ""))
                result.append("\n").append(ConsoleColors.YELLOW + "Muted");
            if (loaded.followings.contains(me.id + ""))
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
            else if (me.pending.contains(loaded.id + ""))
                result.append(ConsoleColors.YELLOW_BRIGHT).append("Pending").append("\n");
            else
                result.append(ConsoleColors.RED_BRIGHT).append("Not Following").append("\n");
            result.append(ConsoleColors.CYAN_BRIGHT).append(loaded.name).append("\n")
                    .append("@").append(loaded.username).append("\n").append(loaded.bio);
            if (me.muted.contains(loaded.id + ""))
                result.append("\n").append(ConsoleColors.YELLOW_BRIGHT + "Muted");
            if (loaded.followings.contains(me.id + ""))
                result.append("\n").append(ConsoleColors.WHITE_BRIGHT + "Follows you");
            return result.toString();
        }
        return "";
    }
}