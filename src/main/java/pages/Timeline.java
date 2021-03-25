package pages;

import data.Load;
import models.Tweet;
import models.User;
import utils.TweetsCli;
import utils.ConsoleColors;
import utils.Input;
import utils.MapUtil;

import java.io.IOException;
import java.util.*;

public class Timeline
{
    public static ArrayList<String> tweets(User user) throws IOException
    {
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
                            && !Load.findUser(tempTweet.getOwner()).blocked.contains(user.id + "")
                            && (tempTweet.upperTweet.equals("") || tempTweetString.charAt(0)=='0'))
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
            if (userTempTweet.visible && Load.findUser(userTempTweet.getOwner()).getIsActive() && userTempTweet.upperTweet.equals(""))
                tempTweets.put(entry.getKey(), entry.getValue());
        }

        HashMap<String, Long> timelineTweets = (HashMap<String, Long>) MapUtil.sortByValue(tempTweets);
        Set<String> keySet = timelineTweets.keySet();
        return new ArrayList<>(keySet);
    }

    public static void timeLine(User user) throws IOException, InterruptedException
    {
        int page = 0;
        int perPage = user.tweetsPerPage;
        int currentTweet = perPage - 1;
        boolean viewLastTweet = false;
        boolean timelineFlag = true;
        while (timelineFlag) {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Timeline");
            System.out.println("------------------------------------------------------");

            Tweet currentVisibleTweet = null;

            ArrayList<String> tweets = tweets(user);

            if (tweets.size()>0)
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
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "Your timeline is empty...");
                System.out.println("------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "List of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");

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
                System.out.println("next: view next tweet in the timeline");
                System.out.println("previous: view previous tweet in the timeline");
                System.out.println("next page: view next page in the timeline");
                System.out.println("previous page: view previous page in the timeline");
            }

            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

            boolean flag = true;
            while (flag)
            {
                String command = scanner.nextLine().toLowerCase();

                List<String> lastPlace = new LinkedList<>();
                lastPlace.add("timeline");

                switch (command)
                {
                    case "main":
                        flag = false;
                        timelineFlag = false;
                        MainPage.mainPage(user);
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