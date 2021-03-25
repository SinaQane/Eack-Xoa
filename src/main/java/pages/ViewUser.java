package pages;

import data.Load;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.MapUtil;
import utils.TweetsCli;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewUser
{
    public static void viewUser(User me, User user, List<String> lastPLace) throws IOException, InterruptedException
    {
        if (user.id.equals(me.id))
        {
            HomePage.homePage(me);
        }
        else
        {
            int page = 0;
            int perPage = me.tweetsPerPage;
            int currentTweet = perPage - 1;
            boolean viewLastTweet = false;
            boolean userFlag = true;
            while (userFlag)
            {
                Scanner scanner = Input.scanner();
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + user.username + "'s Page");
                System.out.println("------------------------------------------------------");
                if (me.followings.contains(user.id + ""))
                    System.out.println(ConsoleColors.GREEN_BRIGHT + "Following");
                else if (me.pending.contains(user.id + ""))
                    System.out.println(ConsoleColors.YELLOW_BRIGHT + "Pending");
                else
                    System.out.println(ConsoleColors.RED_BRIGHT + "Not following");
                if (user.followings.contains(me.id + ""))
                    System.out.println(ConsoleColors.WHITE_BRIGHT + "Follows you");
                System.out.println();
                System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + user.name);
                System.out.println(ConsoleColors.CYAN_BRIGHT + "@" + user.username);
                System.out.println("Followers: " + user.followers.size() + " & " +
                        "Followings: " + user.followings.size());
                if (user.infoState)
                {
                    System.out.println();
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "Email: " + user.email);
                    if (user.birthDate != null)
                    {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM/dd");
                        System.out.println("Birthday: " + dateFormat.format(user.birthDate));
                    }
                    else
                        System.out.println("Birthday: N/A");
                    if (!user.phoneNumber.equals(""))
                        System.out.println("Phone Number: " + user.phoneNumber);
                    else
                        System.out.println("Phone Number: N/A");
                }
                if (!user.bio.equals(""))
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "\n" + user.bio);
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
                if (user.blocked.contains(me.id + ""))
                {
                    System.out.println(ConsoleColors.RED_BRIGHT + "This user has blocked you. Adios...");
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");

                    System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
                    System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the last page");
                    System.out.println("mute: mute/unmute current visible account");
                    System.out.println("block: block current visible account");
                    boolean flag = true;
                    while (flag)
                    {
                        String command = scanner.nextLine().toLowerCase();
                        switch (command)
                        {
                            case "back":
                                flag = false;
                                userFlag = false;
                                if (lastPLace.get(lastPLace.size() - 1).equals("home"))
                                    HomePage.homePage(me);
                                else if (lastPLace.get(lastPLace.size() - 1).equals("timeline"))
                                    Timeline.timeLine(me);
                                else if (lastPLace.get(lastPLace.size() - 1).equals("explore"))
                                    Explore.explore(me);
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='u') // User
                                    ViewUser.viewUser(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='w') // Tweet
                                    ViewTweet.viewTweet(me, Load.findTweet(lastPLace.get(lastPLace.size() - 1).substring(1)), lastPLace.subList(0, lastPLace.size() - 1));
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='f') // Followers
                                    Followers.followers(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='i') // Followings
                                    Followings.followings(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                                break;
                            case "mute":
                                me.mute(user);
                                flag = false;
                                break;
                            case "block":
                                me.block(user);
                                flag = false;
                                break;
                        }
                    }
                }
                else if (user.privateState && !user.followers.contains(me.id + ""))
                {
                    System.out.println(ConsoleColors.RED_BRIGHT + "This page is private. Follow it to view its content...");
                    System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");

                    System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
                    System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the last page");
                    System.out.println("request: send a follow request to current visible account");
                    System.out.println("mute: mute/unmute current visible account");
                    System.out.println("block: block current visible account");
                    boolean flag = true;
                    while (flag)
                    {
                        String command = scanner.nextLine().toLowerCase();
                        switch (command)
                        {
                            case "back":
                                flag = false;
                                userFlag = false;
                                if (lastPLace.get(lastPLace.size() - 1).equals("home"))
                                    HomePage.homePage(me);
                                else if (lastPLace.get(lastPLace.size() - 1).equals("timeline"))
                                    Timeline.timeLine(me);
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='u') // User
                                    ViewUser.viewUser(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='w') // Tweet
                                    ViewTweet.viewTweet(me, Load.findTweet(lastPLace.get(lastPLace.size() - 1).substring(1)), lastPLace.subList(0, lastPLace.size() - 1));
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='f') // Followers
                                    Followers.followers(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='i') // Followings
                                    Followings.followings(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                                break;
                            case "request":
                                me.request(user);
                                flag = false;
                                break;
                            case "mute":
                                me.mute(user);
                                flag = false;
                                break;
                            case "block":
                                me.block(user);
                                flag = false;
                                break;
                        }
                    }
                }
                else
                {
                    ArrayList<String> homePageTweets = new ArrayList<>();

                    for(String tweetString : MapUtil.sortByValue(user.homePageTweets).keySet())
                    {
                        String[] homepageTweetParts = tweetString.split("-");
                        String homepageTweetId = homepageTweetParts[2] + "-" + homepageTweetParts[3];
                        Tweet tempTweet = Load.findTweet(homepageTweetId);
                        if (tempTweet.visible && Load.findUser(tempTweet.getOwner()).getIsActive()
                                && !me.muted.contains(Load.findUser(tempTweet.getOwner()).id + "")
                                && !Load.findUser(tempTweet.getOwner()).blocked.contains(me.id + "")
                                && !(Load.findUser(tempTweet.getOwner()).privateState && !Load.findUser(tempTweet.getOwner()).followers.contains(me.id + ""))
                                && tempTweet.upperTweet.equals(""))
                            homePageTweets.add(tweetString);
                    }

                    Tweet currentVisibleTweet = null;

                    if (homePageTweets.size()>0)
                    {
                        TweetsCli tweetsCli = new TweetsCli(homePageTweets);

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
                    System.out.println(ConsoleColors.PURPLE_BRIGHT + "back: go back to the last page");

                    System.out.println("dm: send a direct message to current visible account");
                    System.out.println("follow: follow current visible account");
                    System.out.println("unfollow: unfollow current visible account");
                    System.out.println("mute: mute/unmute current visible account");
                    System.out.println("block: block current visible account");
                    System.out.println("remove: remove current visible account from your followers");
                    System.out.println("followers: view current visible account's followers");
                    System.out.println("followings: view current visible account's followings");

                    if (currentVisibleTweet != null)
                    {
                        System.out.println("view tweet: view current visible tweet and its comments");
                        System.out.println("view owner: view current visible tweet's owner's page");
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
                    }

                    System.out.println("------------------------------------------------------");

                    System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

                    boolean flag = true;
                    while (flag) {
                        String command = scanner.nextLine().toLowerCase();
                        switch (command) {
                            case "back":
                                flag = false;
                                userFlag = false;
                                if (lastPLace.get(lastPLace.size() - 1).equals("home"))
                                    HomePage.homePage(me);
                                else if (lastPLace.get(lastPLace.size() - 1).equals("timeline"))
                                    Timeline.timeLine(me);
                                else if (lastPLace.get(lastPLace.size() - 1).equals("explore"))
                                    Explore.explore(me);
                                else if (lastPLace.get(lastPLace.size() - 1).equals("notifications"))
                                    Notifications.requests(me);
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='u') // User
                                    ViewUser.viewUser(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='w') // Tweet
                                    ViewTweet.viewTweet(me, Load.findTweet(lastPLace.get(lastPLace.size() - 1).substring(1)), lastPLace.subList(0, lastPLace.size() - 1));
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='f') // Followers
                                    Followers.followers(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='i') // Followings
                                    Followings.followings(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                                break;
                            case "dm":
                                System.out.println(ConsoleColors.RED + "This function isn't available yet");
                                // TODO add dm
                                flag = false;
                                break;
                            case "view owner":
                                if (currentVisibleTweet != null)
                                {
                                    lastPLace.add("u" + user.id);
                                    ViewUser.viewUser(me, Load.findUser(currentVisibleTweet.getOwnerId()), lastPLace);
                                }
                                else
                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                flag = false;
                                break;
                            case "view tweet":
                                if (currentVisibleTweet != null)
                                {
                                    lastPLace.add("u" + user.id);
                                    ViewTweet.viewTweet(me, currentVisibleTweet, lastPLace);
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
                                    me.comment(currentVisibleTweet, comment);
                                }
                                else
                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                flag = false;
                                break;
                            case "follow":
                                if (user.privateState)
                                    me.request(user);
                                else
                                    me.follow(user);
                                flag = false;
                                break;
                            case "unfollow":
                                me.unfollow(user);
                                flag = false;
                                break;
                            case "mute":
                                me.mute(user);
                                flag = false;
                                break;
                            case "block":
                                me.block(user);
                                flag = false;
                                break;
                            case "remove":
                                me.removeFollower(user);
                                flag = false;
                                break;
                            case "followers":
                                flag = false;
                                userFlag = false;
                                lastPLace.add("u" + user.id);
                                Followers.followers(me, user, lastPLace);
                                break;
                            case "followings":
                                flag = false;
                                userFlag = false;
                                lastPLace.add("u" + user.id);
                                Followings.followings(me, user, lastPLace);
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
                                    Load.findUser(currentVisibleTweet.getOwner()).reported(me);
                                else
                                    System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                                flag = false;
                                break;
                            case "report tweet":
                                if (currentVisibleTweet != null)
                                    currentVisibleTweet.report(me);
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
}