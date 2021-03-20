package pages;

import data.Load;
import models.Tweet;
import models.User;
import utils.ConsoleColors;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class HomePage
{
    public static void homePage(User user) throws IOException, InterruptedException
    {
        int num = 0;
        boolean homeFlag = true;
        while (homeFlag)
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Home Page");
            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + user.name);
            System.out.println(ConsoleColors.CYAN_BRIGHT + "@" + user.username);
            System.out.println("Followers: " + user.followers.size() + "          " +
                    "Followings: " + user.followings.size());
            if (user.birthDate != null)
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM/dd");
                System.out.println("Birthday: " + dateFormat.format(user.birthDate));
            }
            else
                System.out.println("Birthday: N/A");
            if (!user.phoneNumber.equals(""))
                System.out.println("Email: " + user.email + " & Phone Number: " + user.phoneNumber);
            else
                System.out.println("Email: " + user.email + " & Phone Number: N/A");
            if (!user.bio.equals(""))
                System.out.println(user.bio);
            System.out.println("------------------------------------------------------");

            int cnt = user.homePageTweets.size();
            Tweet tweet = null;
            if (cnt>0)
            {
                int index = (((cnt - 1 + num) % cnt) + cnt) % cnt;
                tweet = Load.findTweet(user.homePageTweets.get(index).substring(2));
                if (user.homePageTweets.get(index).charAt(0)=='0')
                    System.out.println("* Retweeted by " +user.username);
                System.out.println("@" + tweet.getOwner() + ":");
                System.out.println(tweet.getText());
                System.out.println(tweet.getKarma() + " Karma - " + tweet.getCommentsCount() + " Comments - " +
                        tweet.getRetweetsCount() + " Retweets");
                System.out.println("Tweet " + (1+index) + "/" + cnt);
            }
            else
                System.out.println("You Haven't tweeted anything yet...");

            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");
            System.out.println("followers: view your followers");
            System.out.println("followings: view your followings");
            System.out.println("tweet: tweet something...");
            System.out.println("delete: delete current visible tweet");
            System.out.println("upvote: upvote current visible tweet");
            System.out.println("downvote: downvote current visible tweet");
            System.out.println("retweet: retweet current visible tweet");
            System.out.println("save: save current visible tweet");
            System.out.println("next: view your next tweet");
            System.out.println("previous: view your previous tweet");
            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

            boolean flag = true;
            while (flag) {
                String command = scanner.nextLine().toLowerCase();
                switch (command) {
                    case "main":
                        flag = false;
                        homeFlag = false;
                        MainPage.mainPage(user);
                        break;
                    case "followers":
                    case "followings":
                        System.out.println(ConsoleColors.RED + "This function isn't available yet"); // TODO
                        break;
                    case "tweet":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your tweet here:");
                        String tweetStr = scanner.nextLine();
                        if (!tweetStr.equals(""))
                            user.tweet(tweetStr);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Tweets can't be empty.");
                        num = 0;
                        flag = false;
                        break;
                    case "delete":
                        if (tweet != null)
                            user.deleteTweet(tweet);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "upvote":
                        if (tweet != null)
                            tweet.upvote(user);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "downvote":
                        if (tweet != null)
                            tweet.downvote(user);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "retweet":
                        if (tweet != null)
                            tweet.retweet(user);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        num = 0;
                        flag = false;
                        break;
                    case "save":
                        if (tweet != null)
                            tweet.save(user);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "next":
                        num++;
                        flag = false;
                        break;
                    case "previous":
                        num--;
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