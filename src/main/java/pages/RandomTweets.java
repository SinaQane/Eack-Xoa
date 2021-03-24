package pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.Load;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.TweetsCli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RandomTweets
{
    public static ArrayList<String> tweets(User user) throws IOException
    {
        ArrayList<String> results = new ArrayList<>();
        String path = "./resources/tweets";
        File dir = new File(path);
        String[] paths = dir.list();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        Random random = new Random();
        assert paths != null;
        for (int i = 0; i<Math.min(paths.length / 2, 25); i++)
        {
            String tempPath = paths[random.nextInt(paths.length)];
            Tweet temp = gson.fromJson(Files.readString(Paths.get(path + "/" + tempPath)), Tweet.class);
            if (!user.followings.contains(temp.getOwnerId() + "")  && temp.getOwnerId() != user.id
                && !Load.findUser(temp.getOwnerId()).blocked.contains(user.id + "")
                    && !Load.findUser(temp.getOwnerId()).privateState)
                results.add(temp.id + "");
        }
        return results;
    }

    public static void randomTweets(User user) throws IOException, InterruptedException
    {
        int page = 0;
        int perPage = user.tweetsPerPage;
        int currentTweet = perPage - 1;
        boolean viewLastTweet = false;
        boolean timelineFlag = true;
        while (timelineFlag) {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Explore (Random Tweets)");
            System.out.println("------------------------------------------------------");

            Tweet currentVisibleTweet = null;

            ArrayList<String> tweets = tweets(user);

            boolean notRefreshed = true;
            while (notRefreshed)
            {
                if (tweets.size() > 0)
                {
                    TweetsCli tweetsCli = new TweetsCli(tweets);

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
                    System.out.println(ConsoleColors.YELLOW_BRIGHT + "Explore is empty...");
                    System.out.println("------------------------------------------------------");
                }

                System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "List of available commands: \n");
                System.out.println(ConsoleColors.PURPLE_BRIGHT + "explore: go back to the Explore page");
                System.out.println("refresh: refresh current visible tweets");

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
                    System.out.println("next: view next tweet");
                    System.out.println("previous: view previous tweet");
                    System.out.println("next page: view next page");
                    System.out.println("previous page: view previous page");
                }

                System.out.println("------------------------------------------------------");

                System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

                boolean flag = true;
                while (flag)
                {
                    String command = scanner.nextLine().toLowerCase();

                    List<String> lastPlace = new LinkedList<>();
                    lastPlace.add("explore");

                    switch (command)
                    {
                        case "main":
                            flag = false;
                            timelineFlag = false;
                            Explore.explore(user);
                            break;
                        case "refresh":
                            flag = false;
                            notRefreshed = false;
                            break;
                        case "comment":
                            System.out.println(ConsoleColors.RED + "This function isn't available yet");
                            // TODO add comment
                            flag = false;
                            break;
                        case "view owner":
                            if (currentVisibleTweet != null)
                                ViewUser.viewUser(user, Load.findUser(currentVisibleTweet.getOwnerId()), lastPlace);
                            else
                                System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                            flag = false;
                            break;
                        case "view tweet":
                            if (currentVisibleTweet != null)
                                ViewTweet.viewTweet(user, currentVisibleTweet, lastPlace);
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
                            page = 0;
                            viewLastTweet = true;
                            flag = false;
                            break;
                        case "save":
                            if (currentVisibleTweet != null)
                            {
                                if (user.savedTweets.contains(currentVisibleTweet.id))
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
}