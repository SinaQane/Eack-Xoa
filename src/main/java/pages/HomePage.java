package pages;

import data.Load;
import models.Tweet;
import models.User;
import utils.TweetsCli;
import utils.ConsoleColors;
import utils.Input;
import utils.MapUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class HomePage
{
    public static void homePage(User user) throws IOException, InterruptedException
    {
        int page = 0;
        int perPage = user.tweetsPerPage;
        int currentTweet = perPage - 1;
        boolean viewLastTweet = false;
        boolean homeFlag = true;
        while (homeFlag)
        {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Home Page");
            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + user.name);
            System.out.println(ConsoleColors.CYAN_BRIGHT + "@" + user.username);
            System.out.println("Followers: " + user.followers.size() + " & " +
                    "Followings: " + user.followings.size());
            System.out.println();
            System.out.println("Email: " + user.email);
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
            if (!user.bio.equals(""))
                System.out.println("\n" + user.bio);
            System.out.println("------------------------------------------------------");

            ArrayList<String> homePageTweets = new ArrayList<>();

            for(String tweetString : MapUtil.sortByValue(user.homePageTweets).keySet())
            {
                String[] homepageTweetParts = tweetString.split("-");
                String homepageTweetId = homepageTweetParts[2] + "-" + homepageTweetParts[3];
                Tweet tempTweet = Load.findTweet(homepageTweetId);
                if (tempTweet.visible && Load.findUser(tempTweet.getOwner()).getIsActive() && (tempTweet.upperTweet.equals("") || tweetString.charAt(0)=='0'))
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
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "You haven't tweeted anything yet...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");
            System.out.println("followers: view your followers");
            System.out.println("followings: view your followings");
            System.out.println("tweet: tweet something...");

            if (currentVisibleTweet != null)
            {
                System.out.println("view tweet: view current visible tweet and its comments");
                System.out.println("view owner: view current visible tweet's owner's page");
                System.out.println("comment: leave a comment under current visible tweet");
                System.out.println("delete: delete current visible tweet");
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
            while (flag)
            {
                String command = scanner.nextLine().toLowerCase();

                List<String> lastPlace = new LinkedList<>();
                lastPlace.add("home");

                switch (command)
                {
                    case "main":
                        flag = false;
                        homeFlag = false;
                        MainPage.mainPage(user);
                        break;
                    case "followers":
                        flag = false;
                        homeFlag = false;
                        Followers.followers(user, user, lastPlace);
                        break;
                    case "followings":
                        flag = false;
                        homeFlag = false;
                        Followings.followings(user, user, lastPlace);
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
                    case "view owner":
                        if (currentVisibleTweet != null)
                        {
                            ViewUser.viewUser(user, Load.findUser(currentVisibleTweet.getOwnerId()), lastPlace);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "view tweet":
                        if (currentVisibleTweet != null)
                        {
                            ViewTweet.viewTweet(user, currentVisibleTweet, lastPlace);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "tweet":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your tweet here:");
                        String tweetStr = scanner.nextLine();
                        if (!tweetStr.equals(""))
                            user.tweet(tweetStr);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Tweets can't be empty.");
                        page = 0;
                        viewLastTweet = true;
                        flag = false;
                        break;
                    case "delete":
                        if (currentVisibleTweet != null)
                            user.deleteTweet(currentVisibleTweet);
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