package pages;

import data.Load;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.TweetsCli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SavedTweets
{
    public static void savedTweets(User user) throws IOException, InterruptedException
    {
        int page = 0;
        int perPage = user.tweetsPerPage;
        int currentTweet = perPage - 1;
        boolean viewLastTweet = false;
        boolean savedFlag = true;
        while (savedFlag)
        {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Saved Tweets");
            System.out.println("------------------------------------------------------");

            Tweet currentVisibleTweet = null;

            if (user.savedTweets.size()>0)
            {
                TweetsCli tweetsCli = new TweetsCli((ArrayList<String>) user.savedTweets);

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
                        System.out.println(ConsoleColors.CYAN + tweetsCli.printTweet(tweetsCli.page(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                if (!tweetsCli.printTweet(tweetsCli.page(page, perPage), currentTweet).equals(""))
                {
                    String[] currentVisibleTweetParts = tweetsCli.page(page, perPage).get(currentTweet).split("-");
                    String currentVisibleTweetId = currentVisibleTweetParts[2] + "-" + currentVisibleTweetParts[3];
                    currentVisibleTweet = Load.findTweet(currentVisibleTweetId);
                    System.out.println(ConsoleColors.CYAN_BRIGHT + tweetsCli.printTweet(tweetsCli.page(page, perPage), currentTweet));
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                }
                for (int i = currentTweet - 1; i >= 0; i--)
                {
                    if (!tweetsCli.printTweet(tweetsCli.page(page, perPage), i).equals(""))
                    {
                        System.out.println(ConsoleColors.CYAN + tweetsCli.printTweet(tweetsCli.page(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                System.out.println("Page " + (page + 1) + "/" +
                        numberOfPages + " - Tweet " + (tweetsCli.page(page, perPage).size() - currentTweet) +
                        "/" + tweetsCli.page(page, perPage).size());
                System.out.println("------------------------------------------------------");
            }
            else
            {
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "This user hasn't tweeted anything yet...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the direct messages");

            if (currentVisibleTweet != null)
            {
                System.out.println("view tweet: view current visible tweet and its comments");
                System.out.println("view owner: view current visible tweet's owner's page");
                System.out.println("comment: leave a comment under current visible tweet");
                System.out.println("upvote: upvote current visible tweet");
                System.out.println("downvote: downvote current visible tweet");
                System.out.println("retweet: retweet current visible tweet");
                System.out.println("save: unsave current visible tweet");
                System.out.println("report owner: report current visible tweet's owner");
                System.out.println("report tweet: report current visible tweet");
                System.out.println("next: view next tweet in this page");
                System.out.println("previous: view previous tweet in this page");
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
                        savedFlag = false;
                        DirectMessages.directMessages(user);
                        break;
                    case "view owner":
                        if (currentVisibleTweet != null)
                        {
                            List<String> lastPLace = new LinkedList<>();
                            lastPLace.add("saved");
                            ViewUser.viewUser(user, Load.findUser(currentVisibleTweet.getOwnerId()), lastPLace);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "view tweet":
                        if (currentVisibleTweet != null)
                        {
                            List<String> lastPLace = new LinkedList<>();
                            lastPLace.add("saved");
                            ViewTweet.viewTweet(user, currentVisibleTweet, lastPLace);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
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
                        flag = false;
                        break;
                    case "upvote":
                        if (currentVisibleTweet != null)
                            currentVisibleTweet.upvote(user);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "downvote":
                        if (currentVisibleTweet != null)
                            currentVisibleTweet.downvote(user);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "retweet":
                        if (currentVisibleTweet != null)
                            currentVisibleTweet.retweet(user);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "save":
                        if (currentVisibleTweet != null)
                        {
                            currentVisibleTweet.unsave(user);
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet unsaved");
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "report owner":
                        if (currentVisibleTweet != null)
                            Load.findUser(currentVisibleTweet.getOwner()).reported(user);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "report tweet":
                        if (currentVisibleTweet != null)
                            currentVisibleTweet.report(user);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "next":
                        currentTweet--;
                        flag = false;
                        break;
                    case "previous":
                        currentTweet++;
                        flag = false;
                        break;
                    case "next page":
                        page++;
                        viewLastTweet = true;
                        flag = false;
                        break;
                    case "previous page":
                        page--;
                        viewLastTweet = true;
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
