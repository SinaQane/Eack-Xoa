package pages;

import data.Load;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.UsersCli;

import java.io.IOException;
import java.util.Scanner;

public class Followings
{
    public static void followings(User user, String lastPLace) throws IOException, InterruptedException
    {
        int page = 0;
        int perPage = user.peoplePerPage;
        int currentPerson = perPage - 1;
        boolean viewLastPerson = false;
        boolean followingsFlag = true;
        while (followingsFlag)
        {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Followings");
            System.out.println("------------------------------------------------------");

            User currentVisiblePerson = null;

            if (user.followings.size()>0)
            {
                UsersCli usersCli = new UsersCli(user);
                int numberOfPages = usersCli.numberOfFollowingsPages(perPage);

                page = (((numberOfPages + page) % numberOfPages) + numberOfPages) % numberOfPages;
                int numberOfPeople = usersCli.followingsPage(page, perPage).size();
                if (viewLastPerson)
                {
                    currentPerson = numberOfPeople - 1;
                    viewLastPerson = false;
                }
                else
                    currentPerson = (((numberOfPeople + currentPerson) % numberOfPeople) + numberOfPeople) % numberOfPeople;

                for (int i = perPage; i > currentPerson; i--)
                {
                    if (!usersCli.printFollowing(usersCli.followingsPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printFollowing(usersCli.followingsPage(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                if (!usersCli.printFollowing(usersCli.followingsPage(page, perPage), currentPerson).equals(""))
                {
                    currentVisiblePerson = Load.findUser(Long.parseLong(usersCli.followingsPage(page, perPage).get(currentPerson)));
                    System.out.println(usersCli.printCurrentFollowing(usersCli.followingsPage(page, perPage), currentPerson));
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                }
                for (int i = currentPerson - 1; i >= 0; i--)
                {
                    if (!usersCli.printFollowing(usersCli.followingsPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printFollowing(usersCli.followingsPage(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                System.out.println("Page " + (page + 1) + "/" +
                        numberOfPages + " - User " + (usersCli.followingsPage(page, perPage).size() - currentPerson) +
                        "/" + usersCli.followingsPage(page, perPage).size());
                System.out.println("------------------------------------------------------");
            }
            else
            {
                System.out.println(ConsoleColors.CYAN + "You don't have any followings...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the last page");
            System.out.println("view: view current visible account");
            System.out.println("dm: send a direct message to current visible account");
            System.out.println("unfollow: unfollow current visible account");
            System.out.println("mute: mute/unmute current visible account");
            System.out.println("block: block current visible account");
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
                        followingsFlag = false;
                        flag = false;
                        if (lastPLace.equals("home"))
                            HomePage.homePage(user);
                        else if (lastPLace.charAt(0)=='u')
                            ViewUser.viewUser(user, Load.findUser(Long.parseLong(lastPLace.substring(1))), "u" + user.id);
                        break;
                    case "view":
                        if (currentVisiblePerson != null)
                        {
                            followingsFlag = false;
                            flag = false;
                            ViewUser.viewUser(user, currentVisiblePerson, "u" + user.id);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        break;
                    case "dm": // TODO dm
                        System.out.println(ConsoleColors.RED + "This function isn't available yet");
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