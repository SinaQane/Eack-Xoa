package pages;

import data.Load;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.TweetsCli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ViewTweet
{
    public static void viewTweet(User me, Tweet tweet, String lastPLace) throws IOException, InterruptedException
    {
        int page = 0;
        int perPage = me.tweetsPerPage;
        int currentTweet = perPage - 1;
        boolean viewLastTweet = false;
        boolean tweetFlag = true;
        while (tweetFlag)
        {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Tweet");
            System.out.println("------------------------------------------------------");
            String response = "@" + tweet.getOwner() + ":\n" +
                    tweet.getText() + "\n" +
                    tweet.getKarma() + " Karma - " +
                    tweet.getCommentsCount() + " Comments - " +
                    tweet.getRetweetsCount() + " Retweets";
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + response);
            System.out.println("------------------------------------------------------");

            ArrayList<String> comments = new ArrayList<>();

            for(String tweetString : tweet.comments)
            {
                Tweet tempTweet = Load.findTweet(tweetString);
                if (tempTweet.visible && Load.findUser(tempTweet.getOwner()).getIsActive()
                        && !me.muted.contains(Load.findUser(tempTweet.getOwner()).id + "")
                        && !Load.findUser(tempTweet.getOwner()).blocked.contains(me.id + "")
                        && !(Load.findUser(tempTweet.getOwner()).privateState && !Load.findUser(tempTweet.getOwner()).followers.contains(me.id + "")))
                    comments.add(tweetString);
            }

            Tweet currentVisibleTweet = null;

            if (comments.size()>0)
            {
                TweetsCli tweetsCli = new TweetsCli(comments);

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
                System.out.println(ConsoleColors.CYAN + "This tweet doesn't have any comments yet...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.RED_BRIGHT + "This page is private. Follow it to view its content...");
            System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");

            System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the last page");
            System.out.println("main owner: view main tweet's owner's page");
            System.out.println("main comment: leave a comment under main tweet");
            System.out.println("main upvote: upvote main tweet");
            System.out.println("main downvote: downvote main tweet");
            System.out.println("main retweet: retweet main tweet");
            System.out.println("main save: save/unsave main tweet");
            System.out.println("main report owner: report main tweet's owner");
            System.out.println("main report tweet: report main tweet");

            System.out.println("tweet: view current visible tweet and its comments");
            System.out.println("owner: view current visible tweet's owner's page");
            System.out.println("comment: leave a comment under current visible tweet");
            System.out.println("upvote: upvote current visible tweet");
            System.out.println("downvote: downvote current visible tweet");
            System.out.println("retweet: retweet current visible tweet");
            System.out.println("save: save/unsave current visible tweet");
            System.out.println("report owner: report current visible tweet's owner");
            System.out.println("report tweet: report current visible tweet");

            System.out.println("next: view next tweet in this page");
            System.out.println("previous: view previous tweet in this page");
            System.out.println("next page: view next page");
            System.out.println("previous page: view previous page");
            boolean flag = true;
            while (flag)
            {
                String command = scanner.nextLine().toLowerCase();
                switch (command)
                {
                    case "back":
                        flag = false;
                        tweetFlag = false;
                        if (lastPLace.equals("home"))
                            HomePage.homePage(me);
                        else if (lastPLace.equals("time line"))
                            Timeline.timeLine(me);
                        break;
                    case "main owner":
                        ViewUser.viewUser(me, Load.findUser(tweet.getOwnerId()), "t" + tweet.id);
                        flag = false;
                        break;
                    case "main comment":
                    case "comment":
                        System.out.println(ConsoleColors.RED + "This function isn't available yet");
                        // TODO
                        flag = false;
                        break;
                    case "main upvote":
                        tweet.upvote(me);
                        flag = false;
                        break;
                    case "main downvote":
                        tweet.downvote(me);
                        flag = false;
                        break;
                    case "main retweet":
                        tweet.retweet(me);
                        flag = false;
                        break;
                    case "main save":
                        if(me.savedTweets.contains(tweet.id))
                        {
                            tweet.unsave(me);
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet unsaved");
                        }
                        else
                        {
                            tweet.save(me);
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet saved");
                        }
                        flag = false;
                        break;
                    case "main report owner":
                        tweet.report(me);
                        flag = false;
                        break;
                    case "main report tweet":
                        Load.findUser(tweet.getOwnerId()).reported(me);
                        flag = false;
                        break;
                    case "tweet":
                        if (currentVisibleTweet != null)
                            ViewTweet.viewTweet(me, currentVisibleTweet, "t" + tweet.id);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "owner":
                        if (currentVisibleTweet != null)
                            ViewUser.viewUser(me, Load.findUser(currentVisibleTweet.getOwnerId()), "t" + tweet.id);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upvote":
                        if (currentVisibleTweet != null)
                            currentVisibleTweet.upvote(me);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "downvote":
                        if (currentVisibleTweet != null)
                            currentVisibleTweet.downvote(me);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "retweet":
                        if (currentVisibleTweet != null)
                            currentVisibleTweet.retweet(me);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "save":
                        if (currentVisibleTweet != null)
                        {
                            if(me.savedTweets.contains(currentVisibleTweet.id))
                            {
                                currentVisibleTweet.unsave(me);
                                System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet unsaved");
                            }
                            else
                            {
                                currentVisibleTweet.save(me);
                                System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet saved");
                            }
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "report owner":
                        if (currentVisibleTweet != null)
                            currentVisibleTweet.report(me);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "report tweet":
                        if (currentVisibleTweet != null)
                            Load.findUser(currentVisibleTweet.getOwnerId()).reported(me);
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
                        page--;
                        viewLastTweet = true;
                        flag = false;
                        break;
                    case "previous page":
                        page++;
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
