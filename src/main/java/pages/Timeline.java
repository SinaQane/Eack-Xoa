package pages;

import data.Load;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.MapUtil;

import java.io.IOException;
import java.util.*;

public class Timeline
{
    public static void timeLine(User user) throws IOException, InterruptedException
    {
        int num = 0;
        boolean timelineFlag = true;
        while (timelineFlag) {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Timeline");
            System.out.println("------------------------------------------------------");

            HashMap<String, Long> tempTweets = new HashMap<>();

            for (String following : user.followings)
            {
                if (!user.muted.contains(following))
                {
                    User tempUser = Load.findUser(Long.parseLong(following));
                    for (Map.Entry<String, Long> entry : tempUser.homePageTweets.entrySet())
                    {
                        String tempTweetString = entry.getKey();
                        String[] tempTweetParts = tempTweetString.split("-");
                        String tempTweetId = tempTweetParts[2] + "-" + tempTweetParts[3];
                        Tweet tempTweet = Load.findTweet(tempTweetId);
                        if (tempTweet.visible && Load.findUser(tempTweet.getOwner()).getIsActive()
                                && !Load.findUser(tempTweet.getOwner()).blocked.contains(user.id + ""))
                            tempTweets.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            for (Map.Entry<String, Long> entry : user.homePageTweets.entrySet())
            {
                String userTweet = entry.getKey();
                String[] userTempTweetParts = userTweet.split("-");
                String userTempTweetId = userTempTweetParts[2] + "-" + userTempTweetParts[3];
                Tweet userTempTweet = Load.findTweet(userTempTweetId);
                if (userTempTweet.visible && Load.findUser(userTempTweet.getOwner()).getIsActive())
                    tempTweets.put(entry.getKey(), entry.getValue());
            }

            HashMap<String, Long> timelineTweets = (HashMap<String, Long>) MapUtil.sortByValue(tempTweets);
            Set<String> keySet = timelineTweets.keySet();
            ArrayList<String> listOfKeys = new ArrayList<>(keySet);

            int cnt = listOfKeys.size();
            for (int i = listOfKeys.size() - 1; i >= 0 ; i--)
            {
                String[] tweetParts = listOfKeys.get(i).split("-");
                String tweetId = tweetParts[2] + "-" + tweetParts[3];
                Tweet tweet = Load.findTweet(tweetId);
                if (tweetParts[0].equals("0"))
                    System.out.println("* Retweeted by " +Load.findUser(Long.parseLong(tweetParts[1])).username);
                System.out.println("@" + tweet.getOwner() + ":");
                System.out.println(tweet.getText());
                System.out.println(tweet.getKarma() + " Karma - " + tweet.getCommentsCount() + " Comments - " +
                        tweet.getRetweetsCount() + " Retweets");
                System.out.println("------------------------------------------------------");
            }
            /*
            Should I use this?

            int cnt = listOfKeys.size();
            Tweet tweet = null;
            if (cnt>0)
            {
                int index = (((cnt - 1 + num) % cnt) + cnt) % cnt;
                String[] tweetParts = listOfKeys.get(index).split("-");
                String tweetId = tweetParts[2] + "-" + tweetParts[3];
                tweet = Load.findTweet(tweetId);
                if (tweetParts[0].equals("0"))
                    System.out.println("* Retweeted by " +Load.findUser(Long.parseLong(tweetParts[1])).username);
                System.out.println("@" + tweet.getOwner() + ":");
                System.out.println(tweet.getText());
                System.out.println(tweet.getKarma() + " Karma - " + tweet.getCommentsCount() + " Comments - " +
                        tweet.getRetweetsCount() + " Retweets");
                System.out.println("Tweet " + (1+index) + "/" + cnt);
            }
            else
                System.out.println("You Haven't tweeted anything yet...");
            */

            // System.out.println("------------------------------------------------------");
            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "List of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");
            System.out.println("owner: visit tweet's owner's page");
            System.out.println("view: view current visible tweet and its comments");
            System.out.println("upvote: upvote current visible tweet");
            System.out.println("downvote: downvote current visible tweet");
            System.out.println("retweet: retweet current visible tweet");
            System.out.println("save: save current visible tweet");
            System.out.println("comment: leave a comment under current visible tweet");
            System.out.println("report owner: report current visible tweet's owner");
            System.out.println("report tweet: report current visible tweet");
            System.out.println("next: view next tweet in the timeline");
            System.out.println("previous: view previous tweet in the timeline");
            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

            boolean flag = true;
            while (flag) {
                String command = scanner.nextLine().toLowerCase();
                switch (command) {
                    case "main":
                        flag = false;
                        timelineFlag = false;
                        MainPage.mainPage(user);
                        break;
                    default:
                        System.out.println("Still Working on it...");

                }
            }
        }
    }
}

