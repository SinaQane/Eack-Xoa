package pages;

import data.Load;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.UsersCli;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Blacklist
{
    public static void blacklist(User user) throws IOException, InterruptedException {
        int page = 0;
        int perPage = user.peoplePerPage;
        int currentPerson = perPage - 1;
        boolean viewLastPerson = false;
        boolean blacklistFlag = true;
        while (blacklistFlag)
        {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Followers");
            System.out.println("------------------------------------------------------");

            User currentVisiblePerson = null;

            if (user.blocked.size()>0)
            {
                UsersCli usersCli = new UsersCli(user, user);
                int numberOfPages = usersCli.numberOfBlacklistPages(perPage);

                page = (((numberOfPages + page) % numberOfPages) + numberOfPages) % numberOfPages;
                int numberOfPeople = usersCli.blacklistPage(page, perPage).size();
                if (viewLastPerson)
                {
                    currentPerson = numberOfPeople - 1;
                    viewLastPerson = false;
                }
                else
                    currentPerson = (((numberOfPeople + currentPerson) % numberOfPeople) + numberOfPeople) % numberOfPeople;

                for (int i = perPage; i > currentPerson; i--)
                {
                    if (!usersCli.printUser(usersCli.blacklistPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printUser(usersCli.blacklistPage(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                if (!usersCli.printUser(usersCli.blacklistPage(page, perPage), currentPerson).equals(""))
                {
                    currentVisiblePerson = Load.findUser(Long.parseLong(usersCli.blacklistPage(page, perPage).get(currentPerson)));
                    System.out.println(usersCli.printCurrentUser(usersCli.blacklistPage(page, perPage), currentPerson));
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                }
                for (int i = currentPerson - 1; i >= 0; i--)
                {
                    if (!usersCli.printUser(usersCli.blacklistPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printUser(usersCli.blacklistPage(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                System.out.println("Page " + (page + 1) + "/" +
                        numberOfPages + " - User " + (usersCli.blacklistPage(page, perPage).size() - currentPerson) +
                        "/" + usersCli.blacklistPage(page, perPage).size());
                System.out.println("------------------------------------------------------");
            }
            else
            {
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "Your blacklist is empty...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "home: go back to the Home Page");

            if (currentVisiblePerson != null)
            {
                System.out.println("view: view current visible account");
                System.out.println("unblock: unblock current visible account");
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
                    case "home":
                        blacklistFlag = false;
                        flag = false;
                        HomePage.homePage(user);
                        break;
                    case "view":
                        if (currentVisiblePerson != null)
                        {
                            List<String> lastPLace = new LinkedList<>();
                            lastPLace.add("blacklist");
                            ViewUser.viewUser(user, currentVisiblePerson, lastPLace);
                            blacklistFlag = false;
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "unblock":
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