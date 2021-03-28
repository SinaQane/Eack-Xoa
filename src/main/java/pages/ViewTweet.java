package pages;

import data.Load;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.DataStructuresUtil;
import utils.Input;
import utils.TweetsCli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewTweet
{
    public static void viewTweet(User me, Tweet tweet, List<String> lastPLace) throws IOException, InterruptedException
    {
        int page = 0;
        int perPage = me.tweetsPerPage;
        int currentTweet = perPage - 1;
        boolean viewLastTweet = false;
        boolean tweetFlag = true;
        while (tweetFlag) {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Tweet");
            System.out.println("------------------------------------------------------");
            Tweet upperTweet = null;
            if (!tweet.upperTweet.equals("")) {
                upperTweet = Load.findTweet(tweet.upperTweet);
                System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + "In reply to @" + upperTweet.getOwner() + ":");
                System.out.println(tweet.getText());
                System.out.println(tweet.getKarma() + " Karma - " + tweet.getCommentsCount() + " Comments - "
                        + tweet.getRetweetsCount() + " Retweets");
                System.out.println("------------------------------------------------------");
            }
            String response = "@" + tweet.getOwner() + ":\n" +
                    tweet.getText() + "\n" +
                    tweet.getKarma() + " Karma - " +
                    tweet.getCommentsCount() + " Comments - " +
                    tweet.getRetweetsCount() + " Retweets";
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + response);
            System.out.println("------------------------------------------------------");

            ArrayList<String> comments = new ArrayList<>();

            for (String tweetString : tweet.comments)
            {
                String[] tweetParts = tweetString.split("-");
                String tweetId = tweetParts[2] + "-" + tweetParts[3];
                Tweet tempTweet = Load.findTweet(tweetId);
                if (tempTweet.visible && Load.findUser(tempTweet.getOwner()).getIsActive()
                        && !me.muted.contains(Load.findUser(tempTweet.getOwner()).id + "")
                        && !Load.findUser(tempTweet.getOwner()).blocked.contains(me.id + "")
                        && !(Load.findUser(tempTweet.getOwner()).privateState && !Load.findUser(tempTweet.getOwner()).followers.contains(me.id + "")))
                    comments.add(tweetString);
            }

            Tweet currentVisibleTweet = null;

            if (comments.size() > 0) {
                TweetsCli tweetsCli = new TweetsCli(comments);

                int numberOfPages = tweetsCli.numberOfPages(perPage);

                page = (((numberOfPages + page) % numberOfPages) + numberOfPages) % numberOfPages;
                int numberOfTweets = tweetsCli.page(page, perPage).size();
                if (viewLastTweet) {
                    currentTweet = numberOfTweets - 1;
                    viewLastTweet = false;
                } else
                    currentTweet = (((numberOfTweets + currentTweet) % numberOfTweets) + numberOfTweets) % numberOfTweets;

                for (int i = perPage; i > currentTweet; i--) {
                    if (!tweetsCli.printTweet(tweetsCli.page(page, perPage), i).equals("")) {
                        System.out.println(ConsoleColors.CYAN + tweetsCli.printTweet(tweetsCli.page(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                if (!tweetsCli.printTweet(tweetsCli.page(page, perPage), currentTweet).equals("")) {
                    String[] currentVisibleTweetParts = tweetsCli.page(page, perPage).get(currentTweet).split("-");
                    String currentVisibleTweetId = currentVisibleTweetParts[2] + "-" + currentVisibleTweetParts[3];
                    currentVisibleTweet = Load.findTweet(currentVisibleTweetId);
                    System.out.println(ConsoleColors.CYAN_BRIGHT + tweetsCli.printTweet(tweetsCli.page(page, perPage), currentTweet));
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                }
                for (int i = currentTweet - 1; i >= 0; i--) {
                    if (!tweetsCli.printTweet(tweetsCli.page(page, perPage), i).equals("")) {
                        System.out.println(ConsoleColors.CYAN + tweetsCli.printTweet(tweetsCli.page(page, perPage), i));
                        System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                    }
                }
                System.out.println("Page " + (page + 1) + "/" +
                        numberOfPages + " - Tweet " + (tweetsCli.page(page, perPage).size() - currentTweet) +
                        "/" + tweetsCli.page(page, perPage).size());
                System.out.println("------------------------------------------------------");
            } else {
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "This tweet doesn't have any comments yet...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");

            System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the last page");

            if (upperTweet != null) {
                System.out.println("upper tweet: view upper tweet and its comments");
                System.out.println("upper owner: view upper tweet's owner's page");
                System.out.println("upper comment: leave a comment under upper tweet");
                System.out.println("upper upvote: upvote upper tweet");
                System.out.println("upper downvote: downvote upper tweet");
                System.out.println("upper retweet: retweet upper tweet");
                System.out.println("upper save: save/unsave upper tweet");
                System.out.println("upper report owner: report upper tweet's owner");
                System.out.println("upper report tweet: report upper tweet");
            }

            System.out.println("main owner: view main tweet's owner's page");
            System.out.println("main share: share main tweet with your friends");
            System.out.println("main comment: leave a comment under main tweet");
            System.out.println("main upvote: upvote main tweet");
            System.out.println("main downvote: downvote main tweet");
            System.out.println("main retweet: retweet main tweet");
            System.out.println("main save: save/unsave main tweet");
            System.out.println("main report owner: report main tweet's owner");
            System.out.println("main report tweet: report main tweet");

            if (currentVisibleTweet != null)
            {
                System.out.println("tweet: view current visible tweet and its comments");
                System.out.println("owner: view current visible tweet's owner's page");
                System.out.println("comment: leave a comment under current visible tweet");
                System.out.println("upvote: upvote current visible tweet");
                System.out.println("downvote: downvote current visible tweet");
                System.out.println("retweet: retweet current visible tweet");
                System.out.println("save: save/unsave current visible tweet");
                System.out.println("report owner: report current visible tweet's owner");
                System.out.println("report tweet: report current visible tweet");
            }

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
                        if (lastPLace.get(lastPLace.size() - 1).equals("home"))
                            HomePage.homePage(me);
                        else if (lastPLace.get(lastPLace.size() - 1).equals("timeline"))
                            Timeline.timeLine(me);
                        else if (lastPLace.get(lastPLace.size() - 1).equals("explore"))
                            Explore.explore(me);
                        else if (lastPLace.get(lastPLace.size() - 1).equals("saved"))
                            SavedTweets.savedTweets(me);
                        else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='u') // User
                            ViewUser.viewUser(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                        else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='w') // Tweet
                            ViewTweet.viewTweet(me, Load.findTweet(lastPLace.get(lastPLace.size() - 1).substring(1)), lastPLace.subList(0, lastPLace.size() - 1));
                        else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='f') // Followers
                            Followers.followers(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                        else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='i') // Followings
                            Followings.followings(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                        break;
                    case "upper tweet":
                        if (upperTweet != null)
                        {
                            lastPLace.add("w" + tweet.id);
                            ViewTweet.viewTweet(me, upperTweet, lastPLace);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upper owner":
                        if (upperTweet != null)
                        {
                            lastPLace.add("w" + tweet.id);
                            ViewUser.viewUser(me, Load.findUser(upperTweet.getOwnerId()), lastPLace);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upper comment":
                        if (upperTweet != null)
                        {
                            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your comment:");
                            String upperComment = scanner.nextLine();
                            me.comment(upperTweet, upperComment);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upper upvote":
                        if (upperTweet != null)
                            upperTweet.upvote(me);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upper downvote":
                        if (upperTweet != null)
                            upperTweet.downvote(me);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upper retweet":
                        if (upperTweet != null)
                            upperTweet.retweet(me);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upper save":
                        if (upperTweet != null)
                        {
                            if(me.savedTweets.contains(upperTweet.id))
                            {
                                upperTweet.unsave(me);
                                System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet unsaved");
                            }
                            else
                            {
                                upperTweet.save(me);
                                System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet saved");
                            }
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upper report owner":
                        if (upperTweet != null)
                            upperTweet.report(me);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upper report tweet":
                        if (upperTweet != null)
                            Load.findUser(upperTweet.getOwnerId()).reported(me);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "main owner":
                        lastPLace.add("u" + tweet.id);
                        ViewUser.viewUser(me, Load.findUser(tweet.getOwnerId()), lastPLace);
                        flag = false;
                        break;
                    case "main share":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your destinations:");
                        System.out.println(" You should type users like u/x and groups like g/x");
                        System.out.println(" e.g. u/user u/user g/group");
                        String[] items = scanner.nextLine().split(" ");
                        ArrayList<Long> destinations = new ArrayList<>();
                        for (String destination : items)
                        {
                            String[] parts = destination.split("/");
                            if (parts[0].equals("g"))
                                destinations = DataStructuresUtil.unionArrayLists(destinations, Groups.getUsers(me, parts[1]));
                            else if (parts[0].equals("u"))
                            {
                                try
                                {
                                    User dest = Load.findUser(parts[1]);
                                    ArrayList<Long> temp = new ArrayList<>();
                                    temp.add(dest.id);
                                    destinations = DataStructuresUtil.unionArrayLists(destinations, temp);
                                }
                                catch (Exception ignored) {}
                            }
                        }
                        tweet.share(me, destinations);
                        System.out.println(ConsoleColors.GREEN_BRIGHT + "Tweet was sent to valid destinations");
                        flag = false;
                        break;
                    case "main comment":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your comment:");
                        String mainComment = scanner.nextLine();
                        me.comment(tweet, mainComment);
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
                        {
                            lastPLace.add("w" + tweet.id);
                            ViewTweet.viewTweet(me, currentVisibleTweet, lastPLace);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "owner":
                        if (currentVisibleTweet != null)
                        {
                            lastPLace.add("w" + tweet.id);
                            ViewUser.viewUser(me, Load.findUser(currentVisibleTweet.getOwnerId()), lastPLace);
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
                            me.comment(tweet, comment);
                        }
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