package pages;

import data.Load;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.NotificationsCli;
import utils.UsersCli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Notifications
{
    public static void notifications(User user) throws IOException, InterruptedException
    {
        Scanner scanner = Input.scanner();

        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Notifications");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT +"List of available commands: \n");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");

        if (user.privateState)
            System.out.println("requests: view your follow requests");

        System.out.println("new notifications: search for a tweet based on its context");
        System.out.println("old notifications: view random tweets from other users");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");
        boolean flag = true;
        while (flag)
        {
            String command = scanner.nextLine().toLowerCase();
            switch (command)
            {
                case "main":
                    flag = false;
                    MainPage.mainPage(user);
                    break;
                case "requests":
                    if (user.privateState)
                    {
                    flag = false;
                    requests(user);
                    }
                    else
                    {
                        System.out.println(ConsoleColors.YELLOW_BRIGHT + "Your page isn't private.");
                        System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                    }
                    break;
                case "new notifications":
                    flag = false;
                    printNotifications(user, "new");
                    break;
                case "old notifications":
                    flag = false;
                    printNotifications(user, "old");
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                    break;
            }
        }
    }

    public static void requests(User user) throws IOException, InterruptedException {
        int page = 0;
        int perPage = user.peoplePerPage;
        int currentPerson = perPage - 1;
        boolean viewLastPerson = false;
        boolean requestsFlag = true;
        while (requestsFlag)
        {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Requests");
            System.out.println("------------------------------------------------------");

            User currentVisiblePerson = null;

            if (user.requests.size()>0)
            {
                UsersCli usersCli = new UsersCli(user, user);
                int numberOfPages = usersCli.numberOfRequestsPages(perPage);

                page = (((numberOfPages + page) % numberOfPages) + numberOfPages) % numberOfPages;
                int numberOfPeople = usersCli.requestsPage(page, perPage).size();
                if (viewLastPerson)
                {
                    currentPerson = numberOfPeople - 1;
                    viewLastPerson = false;
                }
                else
                    currentPerson = (((numberOfPeople + currentPerson) % numberOfPeople) + numberOfPeople) % numberOfPeople;

                for (int i = perPage; i > currentPerson; i--)
                {
                    if (!usersCli.printUser(usersCli.requestsPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printUser(usersCli.requestsPage(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                if (!usersCli.printUser(usersCli.requestsPage(page, perPage), currentPerson).equals(""))
                {
                    currentVisiblePerson = Load.findUser(Long.parseLong(usersCli.requestsPage(page, perPage).get(currentPerson)));
                    System.out.println(usersCli.printCurrentUser(usersCli.requestsPage(page, perPage), currentPerson));
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                }
                for (int i = currentPerson - 1; i >= 0; i--)
                {
                    if (!usersCli.printUser(usersCli.requestsPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printUser(usersCli.requestsPage(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                System.out.println("Page " + (page + 1) + "/" +
                        numberOfPages + " - User " + (usersCli.requestsPage(page, perPage).size() - currentPerson) +
                        "/" + usersCli.requestsPage(page, perPage).size());
                System.out.println("------------------------------------------------------");
            }
            else
            {
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "Requests list is empty...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the notifications page");

            if (currentVisiblePerson != null)
            {
                System.out.println("view: view current visible account");
                System.out.println("accept: accept their follow request");
                System.out.println("good reject: reject their follow request without letting them know");
                System.out.println("bad reject: reject their follow request and letting them know");
                System.out.println("next: view next account in this page");
                System.out.println("previous: view previous account in this page");
                System.out.println("next page: view next page");
                System.out.println("previous page: view previous page");
            }

            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

            boolean flag = true;
            while (flag)
            {
                String command = scanner.nextLine().toLowerCase();
                switch (command)
                {
                    case "back":
                        flag = false;
                        notifications(user);
                        break;
                    case "view":
                        if (currentVisiblePerson != null)
                        {
                            ArrayList<String> lastPlace = new ArrayList<>();
                            lastPlace.add("notifications");
                            ViewUser.viewUser(user, currentVisiblePerson, lastPlace);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "accept":
                        if (currentVisiblePerson != null)
                            user.accept(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "good reject":
                        if (currentVisiblePerson != null)
                            user.rejectWithoutNotifications(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "bad reject":
                        if (currentVisiblePerson != null)
                            user.rejectWithNotifications(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "next":
                        currentPerson--;
                        flag = false;
                        break;
                    case "previous":
                        currentPerson++;
                        flag = false;
                        break;
                    case "next page":
                        page++;
                        viewLastPerson = true;
                        flag = false;
                        break;
                    case "previous page":
                        page--;
                        viewLastPerson = true;
                        flag = false;
                        break;
                    default:
                        System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                        break;
                }
            }
        }
    }

    public static void printNotifications(User user, String state) throws IOException, InterruptedException {
        int page = 0;
        int perPage = user.notificationsPerPage;
        boolean notificationsFlag = true;
        while (notificationsFlag)
        {
            Scanner scanner = Input.scanner();
            if (state.equals("old"))
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Old Notifications");
            else
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "New Notifications");
            System.out.println("------------------------------------------------------");

            boolean empty = false;

            if (state.equals("old"))
            {
                if (user.oldNotifications.size() > 0)
                {
                    NotificationsCli cli = new NotificationsCli((ArrayList<String>) user.oldNotifications);
                    int numberOfPages = cli.numberOfPages(perPage);
                    page = (((numberOfPages + page) % numberOfPages) + numberOfPages) % numberOfPages;
                    for (int i = numberOfPages; i >= 0 ; i--)
                    {
                        if (!cli.printNotification(cli.notificationsPage(page, perPage), i).equals(""))
                        {
                            System.out.println(ConsoleColors.CYAN_BRIGHT + cli.printNotification(cli.notificationsPage(page, perPage), i));
                            System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                        }
                    }
                }
                else
                {
                    System.out.println(ConsoleColors.YELLOW_BRIGHT + "Notifications list is empty...");
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    empty = true;
                }
            }
            else
            {
                if (user.oldNotifications.size() > 0)
                {
                    NotificationsCli cli = new NotificationsCli((ArrayList<String>) user.newNotifications);
                    int numberOfPages = cli.numberOfPages(perPage);
                    page = (((numberOfPages + page) % numberOfPages) + numberOfPages) % numberOfPages;
                    for (int i = numberOfPages; i >= 0 ; i--)
                    {
                        if (!cli.printNotification(cli.notificationsPage(page, perPage), i).equals(""))
                        {
                            System.out.println(ConsoleColors.CYAN_BRIGHT + cli.printNotification(cli.notificationsPage(page, perPage), i));
                            System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                        }
                    }
                }
                else
                {
                    System.out.println(ConsoleColors.YELLOW_BRIGHT + "Notifications list is empty...");
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    empty = true;
                }
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the notifications page");
            System.out.println("refresh: refresh this page");

            if (empty)
            {
                System.out.println("next page: view next page");
                System.out.println("previous page: view previous page");
            }

            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

            boolean flag = true;
            while (flag)
            {
                String command = scanner.nextLine().toLowerCase();
                switch (command)
                {
                    case "back":
                        flag = false;
                        user.refresh();
                        notifications(user);
                        break;
                    case "refresh":
                        user.refresh();
                        flag = false;
                        break;
                    case "next page":
                        page++;
                        flag = false;
                        break;
                    case "previous page":
                        page--;
                        flag = false;
                        break;
                }
            }
        }
    }
}