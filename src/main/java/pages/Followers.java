package pages;

import data.Load;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.UsersCli;

import java.io.IOException;
import java.util.Scanner;

public class Followers
{
    public static void followers(User user, String lastPLace) throws IOException, InterruptedException
    {
        int page = 0;
        int perPage = user.peoplePerPage;
        int currentPerson = perPage - 1;
        boolean viewLastPerson = false;
        boolean followersFlag = true;
        while (followersFlag)
        {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Followers");
            System.out.println("------------------------------------------------------");

            User currentVisiblePerson = null;

            if (user.followers.size()>0)
            {
                UsersCli usersCli = new UsersCli(user);
                int numberOfPages = usersCli.numberOfFollowersPages(perPage);

                page = (((numberOfPages + page) % numberOfPages) + numberOfPages) % numberOfPages;
                int numberOfPeople = usersCli.followersPage(page, perPage).size();
                if (viewLastPerson)
                {
                    currentPerson = numberOfPeople - 1;
                    viewLastPerson = false;
                }
                else
                    currentPerson = (((numberOfPeople + currentPerson) % numberOfPeople) + numberOfPeople) % numberOfPeople;

                for (int i = perPage; i > currentPerson; i--)
                {
                    if (!usersCli.printFollower(usersCli.followersPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printFollower(usersCli.followersPage(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                if (!usersCli.printFollower(usersCli.followersPage(page, perPage), currentPerson).equals(""))
                {
                    currentVisiblePerson = Load.findUser(Long.parseLong(usersCli.followersPage(page, perPage).get(currentPerson)));
                    System.out.println(usersCli.printCurrentFollower(usersCli.followersPage(page, perPage), currentPerson));
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                }
                for (int i = currentPerson - 1; i >= 0; i--)
                {
                    if (!usersCli.printFollower(usersCli.followersPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printFollower(usersCli.followersPage(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                System.out.println("Page " + (page + 1) + "/" +
                        numberOfPages + " - User " + (usersCli.followersPage(page, perPage).size() - currentPerson) +
                        "/" + usersCli.followersPage(page, perPage).size());
                System.out.println("------------------------------------------------------");
            }
            else
            {
                System.out.println(ConsoleColors.CYAN + "You don't have any followers...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the last page");
            System.out.println("view: view current visible account");
            System.out.println("dm: send a direct message to current visible account");
            System.out.println("follow: follow current visible account");
            System.out.println("unfollow: unfollow current visible account");
            System.out.println("mute: mute/unmute current visible account");
            System.out.println("block: block current visible account");
            // TODO block unblock...
            System.out.println("next: view next account in this page");
            System.out.println("previous: view previous account in this page");
            System.out.println("next page: view next page");
            System.out.println("previous page: view previous page");
            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

            boolean flag = true;
            while (flag)
            {
                String command = scanner.nextLine().toLowerCase();
                switch (command)
                {
                    case "back":
                        followersFlag = false;
                        flag = false;
                        if (lastPLace.equals("home"))
                            HomePage.homePage(user);
                        // TODO otherwise "lastPLace" would be someone's id. Use ViewTweet class
                        break;
                    case "view":
                    case "dm":
                        System.out.println(ConsoleColors.RED + "This function isn't available yet");
                        break;
                    case "follow":
                        if (currentVisiblePerson != null)
                            user.follow(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "unfollow":
                        if (currentVisiblePerson != null)
                            user.unfollow(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "mute":
                        if (currentVisiblePerson != null)
                            user.mute(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "block":
                        if (currentVisiblePerson != null)
                            user.block(currentVisiblePerson);
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
}
