package pages;

import data.Load;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.UsersCli;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Followers
{
    public static void followers(User me, User user, List<String> lastPLace) throws IOException, InterruptedException
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
                UsersCli usersCli = new UsersCli(me, user);
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
                    if (!usersCli.printUser(usersCli.followersPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printUser(usersCli.followersPage(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                if (!usersCli.printUser(usersCli.followersPage(page, perPage), currentPerson).equals(""))
                {
                    currentVisiblePerson = Load.findUser(Long.parseLong(usersCli.followersPage(page, perPage).get(currentPerson)));
                    System.out.println(usersCli.printCurrentUser(usersCli.followersPage(page, perPage), currentPerson));
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                }
                for (int i = currentPerson - 1; i >= 0; i--)
                {
                    if (!usersCli.printUser(usersCli.followersPage(page, perPage), i).equals(""))
                    {
                        System.out.println(usersCli.printUser(usersCli.followersPage(page, perPage), i));
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
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "Followers list is empty...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the last page");

            if (currentVisiblePerson != null)
            {
                System.out.println("view: view current visible account");
                System.out.println("dm: send a direct message to current visible account");
                System.out.println("follow: follow current visible account");
                System.out.println("unfollow: unfollow current visible account");
                System.out.println("mute: mute/unmute current visible account");
                System.out.println("block: block current visible account");
                System.out.println("remove: remove current visible account from your followers");
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
                        followersFlag = false;
                        flag = false;
                        if (lastPLace.get(lastPLace.size() - 1).equals("home"))
                            HomePage.homePage(me);
                        else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='u') // User
                            ViewUser.viewUser(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                        else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='w') // Tweet
                            ViewTweet.viewTweet(me, Load.findTweet(lastPLace.get(lastPLace.size() - 1).substring(1)), lastPLace.subList(0, lastPLace.size() - 1));
                        else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='f') // Followers
                            Followers.followers(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                        else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='i') // Followings
                            Followings.followings(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                        break;
                    case "view":
                        if (currentVisiblePerson != null)
                        {
                            lastPLace.add("f" + user.id);
                            ViewUser.viewUser(me, currentVisiblePerson, lastPLace);
                            followersFlag = false;
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "dm":
                        if (currentVisiblePerson != null)
                        {
                            lastPLace.add("f" + user.id);
                            Chat.chat(me, currentVisiblePerson, lastPLace);
                            followersFlag = false;
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "follow":
                        if (currentVisiblePerson != null)
                            if (currentVisiblePerson.privateState)
                                me.request(currentVisiblePerson);
                            else
                                me.follow(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "unfollow":
                        if (currentVisiblePerson != null)
                            me.unfollow(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "mute":
                        if (currentVisiblePerson != null)
                            me.mute(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "block":
                        if (currentVisiblePerson != null)
                            me.block(currentVisiblePerson);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "remove":
                        if (currentVisiblePerson != null)
                            me.removeFollower(currentVisiblePerson);
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