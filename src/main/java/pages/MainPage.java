package pages;

import models.User;
import utils.ConsoleColors;
import utils.Input;

import java.io.IOException;
import java.util.*;

public class MainPage
{
    public static void mainPage(User user) throws NoSuchElementException, IOException, InterruptedException
    {
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    user.setLastSeen(new Date());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(task, 60000);

        Scanner scanner = Input.scanner();

        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Main Page");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT +"List of available commands: \n");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "home: go to your Home Page");
        System.out.println("timeline: read your following accounts' tweets");
        System.out.println("explore: search for other accounts and read random tweets");
        System.out.println("notifications: read your latest notifications");
        System.out.println("direct: chat with other users and read their messages");
        System.out.println("settings: set your account's settings");
        System.out.println("log out: log out of your account");
        System.out.println("close: close the app");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

        boolean flag = true;
        while (flag)
        {
            String command = scanner.nextLine().toLowerCase();
            switch (command)
            {
                case "home":
                    flag = false;
                    HomePage.homePage(user);
                    break;
                case "timeline":
                    flag = false;
                    Timeline.timeLine(user);
                    break;
                case "explore":
                    Explore.explore(user);
                    flag = false;
                    break;
                case "notifications":
                    Notifications.notifications(user);
                    flag = false;
                    break;
                case "direct":
                    DirectMessages.directMessages(user);
                    flag = false;
                    break;
                case "log out":
                    System.out.println(ConsoleColors.WHITE_BRIGHT + "Logging out...");
                    timer.cancel();
                    FirstPage.firstPage();
                    break;
                case "settings":
                    flag = false;
                    Settings.settings(user);
                    break;
                case "close":
                    flag = false;
                    System.out.print(ConsoleColors.WHITE_BRIGHT + "Closing the app...");
                    timer.cancel();
                    System.exit(0);
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                    break;
            }
        }
    }
}