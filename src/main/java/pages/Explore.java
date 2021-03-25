package pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.Load;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.SearchUsersCli;
import utils.TweetsCli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Explore
{
    public static void explore(User user) throws IOException, InterruptedException
    {
        Scanner scanner = Input.scanner();

        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Explore");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT +"List of available commands: \n");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");
        System.out.println("search for user: search for a user by their name or username");
        System.out.println("search for tweet: search for a tweet based on its context");
        System.out.println("random: view random tweets from other users");
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
                case "search for user":
                    flag = false;
                    searchUser(user);
                    break;
                case "search for tweet":
                    flag = false;
                    searchTweet(user);
                    break;
                case "random":
                    flag = false;
                    RandomTweets.randomTweets(user);
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                    break;
            }
        }
    }

    public static void searchUser(User user) throws IOException, InterruptedException
    {
        Scanner scanner = Input.scanner();

        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Search");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT +"List of available commands: \n");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "explore: go back to the Explore page");
        System.out.println("search: look for a user");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

        boolean flag = true;
        while (flag)
        {
            String firstCommand = scanner.nextLine().toLowerCase();
            switch (firstCommand)
            {
                case "explore":
                    flag = false;
                    explore(user);
                    break;
                case "search":
                    boolean searchFlag = true;
                    while (searchFlag)
                    {
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Search For User");
                        System.out.println("------------------------------------------------------");

                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a non-empty string:");

                        int page = 0;
                        int perPage = user.peoplePerPage;
                        int currentPerson = perPage - 1;
                        boolean viewLastPerson = false;
                        boolean commandFlag = true;
                        while (commandFlag)
                        {
                            String command = scanner.nextLine().toLowerCase();
                            if (command.equals(""))
                            {
                                System.out.println(ConsoleColors.RED_BRIGHT + "please enter a non-empty string:");
                            }
                            else {
                                boolean usersListFlag = true;
                                while (usersListFlag)
                                {
                                    ArrayList<String> results = new ArrayList<>();
                                    String path = "./resources/users";
                                    File dir = new File(path);
                                    String[] paths = dir.list();
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gsonBuilder.setPrettyPrinting();
                                    Gson gson = gsonBuilder.create();
                                    assert paths != null;
                                    for (String tempPath : paths)
                                    {
                                        User temp = gson.fromJson(Files.readString(Paths.get(path + "/" + tempPath)), User.class);
                                        if ((temp.name.contains(command) || temp.username.contains(command)) && !temp.id.equals(user.id))
                                            results.add(temp.id + "");
                                    }

                                    User currentVisiblePerson = null;

                                    if (results.size() > 0)
                                    {
                                        SearchUsersCli usersCli = new SearchUsersCli(user, results);

                                        int numberOfPages = usersCli.numberOfPages(perPage);
                                        page = (((numberOfPages + page) % numberOfPages) + numberOfPages) % numberOfPages;
                                        int numberOfPeople = usersCli.usersPage(page, perPage).size();

                                        if (viewLastPerson)
                                        {
                                            currentPerson = numberOfPeople - 1;
                                            viewLastPerson = false;
                                        }
                                        else
                                            currentPerson = (((numberOfPeople + currentPerson) % numberOfPeople) + numberOfPeople) % numberOfPeople;

                                        for (int i = perPage; i > currentPerson; i--)
                                        {
                                            if (!usersCli.printUser(usersCli.usersPage(page, perPage), i).equals(""))
                                            {
                                                System.out.println(usersCli.printUser(usersCli.usersPage(page, perPage), i));
                                                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                                            }
                                        }
                                        if (!usersCli.printUser(usersCli.usersPage(page, perPage), currentPerson).equals(""))
                                        {
                                            currentVisiblePerson = Load.findUser(Long.parseLong(usersCli.usersPage(page, perPage).get(currentPerson)));
                                            System.out.println(usersCli.printCurrentUser(usersCli.usersPage(page, perPage), currentPerson));
                                            System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                                        }
                                        for (int i = currentPerson - 1; i >= 0; i--)
                                        {
                                            if (!usersCli.printUser(usersCli.usersPage(page, perPage), i).equals(""))
                                            {
                                                System.out.println(usersCli.printUser(usersCli.usersPage(page, perPage), i));
                                                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                                            }
                                        }
                                        System.out.println("Page " + (page + 1) + "/" +
                                                numberOfPages + " - User " + (usersCli.usersPage(page, perPage).size() - currentPerson) +
                                                "/" + usersCli.usersPage(page, perPage).size());
                                        System.out.println("------------------------------------------------------");
                                    }
                                    else
                                    {
                                        System.out.println(ConsoleColors.YELLOW_BRIGHT + "No results...");
                                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                                    }

                                    System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
                                    System.out.println(ConsoleColors.PURPLE_BRIGHT + "search: go back to the Search page");
                                    System.out.println(ConsoleColors.PURPLE_BRIGHT + "search user: search for another user");

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

                                    boolean resultFlag = true;
                                    while (resultFlag)
                                    {
                                        String lastCommand = scanner.nextLine().toLowerCase();

                                        List<String> lastPlace = new LinkedList<>();
                                        lastPlace.add("explore");

                                        switch (lastCommand)
                                        {
                                            case "search":
                                                searchFlag = false;
                                                commandFlag = false;
                                                usersListFlag = false;
                                                resultFlag = false;
                                                break;
                                            case "search user":
                                                commandFlag = false;
                                                usersListFlag = false;
                                                resultFlag = false;
                                                break;
                                            case "view":
                                                if (currentVisiblePerson != null)
                                                {
                                                    ViewUser.viewUser(user, currentVisiblePerson, lastPlace);
                                                    searchFlag = false;
                                                    commandFlag = false;
                                                    usersListFlag = false;
                                                }
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "dm":
                                                System.out.println(ConsoleColors.RED + "This function isn't available yet");
                                                // TODO add dm
                                                resultFlag = false;
                                                break;
                                            case "follow":
                                                if (currentVisiblePerson != null)
                                                    if (currentVisiblePerson.privateState)
                                                        user.request(currentVisiblePerson);
                                                    else
                                                        user.follow(currentVisiblePerson);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "unfollow":
                                                if (currentVisiblePerson != null)
                                                    user.unfollow(currentVisiblePerson);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "mute":
                                                if (currentVisiblePerson != null)
                                                    user.mute(currentVisiblePerson);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "block":
                                                if (currentVisiblePerson != null)
                                                    user.block(currentVisiblePerson);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "remove":
                                                if (currentVisiblePerson != null)
                                                    user.removeFollower(currentVisiblePerson);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "next":
                                                currentPerson--;
                                                resultFlag = false;
                                                break;
                                            case "previous":
                                                currentPerson++;
                                                resultFlag = false;
                                                break;
                                            case "next page":
                                                page++;
                                                viewLastPerson = true;
                                                resultFlag = false;
                                                break;
                                            case "previous page":
                                                page--;
                                                viewLastPerson = true;
                                                resultFlag = false;
                                                break;
                                            default:
                                                System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    flag = false;
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                    break;
            }
        }
    }

    public static void searchTweet(User user) throws IOException, InterruptedException
    {
        Scanner scanner = Input.scanner();

        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Search");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT +"List of available commands: \n");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "explore: go back to the Explore page");
        System.out.println("search: look for a tweet");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

        boolean flag = true;
        while (flag)
        {
            String firstCommand = scanner.nextLine().toLowerCase();
            switch (firstCommand)
            {
                case "explore":
                    flag = false;
                    explore(user);
                    break;
                case "search":
                    boolean searchFlag = true;
                    while (searchFlag)
                    {
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Search For Tweet");
                        System.out.println("------------------------------------------------------");

                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a non-empty string:");

                        int page = 0;
                        int perPage = user.tweetsPerPage;
                        int currentTweet = perPage - 1;
                        boolean viewLastTweet = false;
                        boolean commandFlag = true;
                        while (commandFlag)
                        {
                            String command = scanner.nextLine().toLowerCase();
                            if (command.equals(""))
                            {
                                System.out.println(ConsoleColors.RED_BRIGHT + "please enter a non-empty string:");
                            }
                            else
                            {
                                boolean tweetsListFlag = true;
                                while (tweetsListFlag)
                                {
                                    ArrayList<String> results = new ArrayList<>();
                                    String path = "./resources/tweets";
                                    File dir = new File(path);
                                    String[] paths = dir.list();
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gsonBuilder.setPrettyPrinting();
                                    Gson gson = gsonBuilder.create();
                                    assert paths != null;
                                    for (String tempPath : paths)
                                    {
                                        Tweet temp = gson.fromJson(Files.readString(Paths.get(path + "/" + tempPath)), Tweet.class);
                                        if (temp.text.contains(command)  && !Load.findUser(temp.getOwnerId()).blocked.contains(user.id + "")
                                                && !(!Load.findUser(temp.getOwnerId()).followers.contains(user.id + "")
                                                && Load.findUser(temp.getOwnerId()).privateState
                                                && temp.getOwnerId() != user.id))
                                            results.add(temp.id + "");
                                    }

                                    Tweet currentVisibleTweet = null;

                                    if (results.size() > 0)
                                    {
                                        TweetsCli tweetsCli = new TweetsCli(results);

                                        int numberOfPages = tweetsCli.numberOfPages(perPage);

                                        page = (((numberOfPages + page) % numberOfPages) + numberOfPages) % numberOfPages;
                                        int numberOfTweets = tweetsCli.page(page, perPage).size();
                                        if (viewLastTweet)
                                        {
                                            currentTweet = numberOfTweets - 1;
                                            viewLastTweet = false;
                                        }
                                        else
                                            currentTweet = (((numberOfTweets + currentTweet) % numberOfTweets) + numberOfTweets) % numberOfTweets;

                                        for (int i = perPage; i > currentTweet; i--)
                                        {
                                            if (!tweetsCli.printTweet(tweetsCli.page(page, perPage), i).equals(""))
                                            {
                                                System.out.println(ConsoleColors.CYAN_BRIGHT + tweetsCli.printTweet(tweetsCli.page(page, perPage), i));
                                                System.out.println("------------------------------------------------------");
                                            }
                                        }
                                        if (!tweetsCli.printTweet(tweetsCli.page(page, perPage), currentTweet).equals(""))
                                        {
                                            String[] currentVisibleTweetParts = tweetsCli.page(page, perPage).get(currentTweet).split("-");
                                            String currentVisibleTweetId = currentVisibleTweetParts[2] + "-" + currentVisibleTweetParts[3];
                                            currentVisibleTweet = Load.findTweet(currentVisibleTweetId);
                                            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + tweetsCli.printTweet(tweetsCli.page(page, perPage), currentTweet));
                                            System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                                        }
                                        for (int i = currentTweet - 1; i >= 0; i--)
                                        {
                                            if (!tweetsCli.printTweet(tweetsCli.page(page, perPage), i).equals(""))
                                            {
                                                System.out.println(ConsoleColors.CYAN_BRIGHT + tweetsCli.printTweet(tweetsCli.page(page, perPage), i));
                                                System.out.println("------------------------------------------------------");
                                            }
                                        }
                                        System.out.println("Page " + (page + 1) + "/" +
                                                numberOfPages + " - Tweet " + (tweetsCli.page(page, perPage).size() - currentTweet) +
                                                "/" + tweetsCli.page(page, perPage).size());
                                        System.out.println("------------------------------------------------------");
                                    }
                                    else
                                    {
                                        System.out.println(ConsoleColors.YELLOW_BRIGHT + "No results...");
                                        System.out.println("------------------------------------------------------");
                                    }

                                    System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
                                    System.out.println(ConsoleColors.PURPLE_BRIGHT + "search: go back to the Search page");
                                    System.out.println(ConsoleColors.PURPLE_BRIGHT + "search tweet: search for another tweet");

                                    if (currentVisibleTweet != null)
                                    {
                                        System.out.println("view owner: visit tweet's owner's page");
                                        System.out.println("view tweet: view current visible tweet and its comments");
                                        System.out.println("upvote: upvote current visible tweet");
                                        System.out.println("downvote: downvote current visible tweet");
                                        System.out.println("retweet: retweet current visible tweet");
                                        System.out.println("save: save/unsave current visible tweet");
                                        System.out.println("comment: leave a comment under current visible tweet");
                                        System.out.println("report owner: report current visible tweet's owner");
                                        System.out.println("report tweet: report current visible tweet");
                                        System.out.println("next: view next account in this page");
                                        System.out.println("previous: view previous account in this page");
                                        System.out.println("next page: view next page");
                                        System.out.println("previous page: view previous page");
                                    }

                                    System.out.println("------------------------------------------------------");

                                    System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

                                    boolean resultFlag = true;
                                    while (resultFlag)
                                    {
                                        String lastCommand = scanner.nextLine().toLowerCase();

                                        List<String> lastPlace = new LinkedList<>();
                                        lastPlace.add("explore");

                                        switch (lastCommand)
                                        {
                                            case "search":
                                                searchFlag = false;
                                                commandFlag = false;
                                                tweetsListFlag = false;
                                                resultFlag = false;
                                                break;
                                            case "search user":
                                                commandFlag = false;
                                                tweetsListFlag = false;
                                                resultFlag = false;
                                                break;
                                            case "comment":
                                                if (currentVisibleTweet != null)
                                                {
                                                    System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your comment:");
                                                    String comment = scanner.nextLine();
                                                    user.comment(currentVisibleTweet, comment);
                                                }
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "view owner":
                                                if (currentVisibleTweet != null)
                                                {
                                                    ViewUser.viewUser(user, Load.findUser(currentVisibleTweet.getOwnerId()), lastPlace);
                                                    searchFlag = false;
                                                    commandFlag = false;
                                                    tweetsListFlag = false;
                                                }
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "view tweet":
                                                if (currentVisibleTweet != null)
                                                {
                                                    ViewTweet.viewTweet(user, currentVisibleTweet, lastPlace);
                                                    searchFlag = false;
                                                    commandFlag = false;
                                                    tweetsListFlag = false;
                                                }
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "upvote":
                                                if (currentVisibleTweet != null)
                                                    currentVisibleTweet.upvote(user);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "downvote":
                                                if (currentVisibleTweet != null)
                                                    currentVisibleTweet.downvote(user);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "retweet":
                                                if (currentVisibleTweet != null)
                                                    currentVisibleTweet.retweet(user);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "save":
                                                if (currentVisibleTweet != null)
                                                {
                                                    if(user.savedTweets.contains(currentVisibleTweet.id))
                                                    {
                                                        currentVisibleTweet.unsave(user);
                                                        System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet unsaved");
                                                    }
                                                    else
                                                    {
                                                        currentVisibleTweet.save(user);
                                                        System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet saved");
                                                    }
                                                }
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "report owner":
                                                if (currentVisibleTweet != null)
                                                    Load.findUser(currentVisibleTweet.getOwner()).reported(user);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "report tweet":
                                                if (currentVisibleTweet != null)
                                                    currentVisibleTweet.report(user);
                                                else
                                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                                resultFlag = false;
                                                break;
                                            case "next":
                                                currentTweet--;
                                                resultFlag = false;
                                                break;
                                            case "previous":
                                                currentTweet++;
                                                resultFlag = false;
                                                break;
                                            case "next page":
                                                page++;
                                                viewLastTweet = true;
                                                resultFlag = false;
                                                break;
                                            case "previous page":
                                                page--;
                                                viewLastTweet = true;
                                                resultFlag = false;
                                                break;
                                            default:
                                                System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    flag = false;
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                    break;
            }
        }
    }
}