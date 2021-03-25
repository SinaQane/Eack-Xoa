package utils;

import java.util.ArrayList;

public class NotificationsCli
{
    ArrayList<String> notifications;

    public NotificationsCli(ArrayList<String> notifications)
    {
        this.notifications = notifications;
    }

    public int numberOfPages(int perPage)
    {
        if (this.notifications.size() % perPage == 0)
            return this.notifications.size()/perPage;
        return this.notifications.size()/perPage + 1;
    }

    public ArrayList<String> notificationsPage(int page, int perPage)
    {
        ArrayList<String> result = new ArrayList<>();
        int start = this.notifications.size() - ((page + 1) * perPage);
        int index = 0;
        int exception = 0;
        do {
            try
            {
                result.add(this.notifications.get(start + index));
            }
            catch (Exception e)
            {
                exception++;
            }
            index++;
        } while (index != perPage);
        return result;
    }

    public String printNotification(ArrayList<String> page, int index)
    {
        if (index < page.size())
            return page.get(index);
        return "";
    }

}
