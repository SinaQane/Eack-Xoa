package pages;

import data.Load;
import models.Tweet;
import models.User;
import utils.ConsoleColors;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HomePage
{
    public static void mainPage(User user) throws NoSuchElementException, IOException
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Main Page");
        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.BLUE_BRIGHT +"List of available commands:");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "home: go to your Home Page");
        System.out.println("timeline: read your following accounts' tweets");
        System.out.println("explore: search for other accounts and read random tweets");
        System.out.println("notifications: read your latest notifications");
        System.out.println("direct: chat with other users and read their messages");
        System.out.println("settings: set your account's settings");
        System.out.println("close: close the app");

        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

        boolean flag = true;
        while (flag)
        {
            String command = scanner.nextLine();
            switch (command)
            {
                case "home":
                    flag = false;
                    homePage(user);
                    break;
                case "timeline":
                    System.out.println(ConsoleColors.RED + "This function isn't available");
                    break;
                case "explore":
                    System.out.println(ConsoleColors.RED + "This function isn't available");
                    break;
                case "notifications":
                    System.out.println(ConsoleColors.RED + "This function isn't available");
                    break;
                case "direct":
                    System.out.println(ConsoleColors.RED + "This function isn't available");
                    break;
                case "settings":
                    System.out.println(ConsoleColors.RED + "This function isn't available");
                    break;
                case "close":
                    flag = false;
                    System.out.println(ConsoleColors.WHITE_BRIGHT + "Closing the app...");
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                    break;
            }
        }
    }

    public static void homePage(User user) throws IOException
    {
        while (true)
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Home Page");
            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + user.name);
            System.out.println(ConsoleColors.CYAN_BRIGHT + "@" + user.username);
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

            int cnt = user.timelineTweets.size();
            int num = 0;
            Tweet tweet = null;
            if (cnt>0)
            {
                int index = (((cnt - 1 + num) % cnt) + cnt) % cnt;
                tweet = Load.findTweet(user.timelineTweets.get(index));
                System.out.println("@" + tweet.getOwner() + ":");
                System.out.println(tweet.getText());
                System.out.println(tweet.getKarma() + " Karma - " + tweet.getCommentsCount() + " Comments - " +
                        tweet.getRetweetsCount() + " Retweets");
                System.out.println("Tweet " + (1+num) + "/" + cnt);
            }
            else
                System.out.println("You Haven't tweeted anything yet...");

            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.BLUE_BRIGHT + "list of available commands:");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");
            System.out.println("tweet: tweet something...");
            System.out.println("delete: delete current visible tweet");
            System.out.println("upvote: upvote current visible tweet");
            System.out.println("downvote: downvote current visible tweet");
            System.out.println("retweet: retweet current visible tweet");
            System.out.println("save: save current visible tweet");
            System.out.println("next: view your next tweet");
            System.out.println("previous: view your previous tweet");
            System.out.println("close: close the app");

            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");
            boolean flag = true;
            while (flag) {
                String command = scanner.nextLine();
                switch (command) {
                    case "main":
                        flag = false;
                        mainPage(user);
                        break;
                    case "tweet":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your tweet here:");
                        String tweetStr = scanner.nextLine();
                        user.tweet(tweetStr);
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
                            user.upvote(tweet);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "downvote":
                        if (tweet != null)
                            user.downvote(tweet);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "retweet":
                        if (tweet != null)
                            user.retweet(tweet);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                        flag = false;
                        break;
                    case "save":
                        if (tweet != null)
                            user.save(tweet);
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
                    case "close":
                        flag = false;
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Closing the app...");
                        break;
                    default:
                        System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                        break;
                }
            }
        }
    }
}