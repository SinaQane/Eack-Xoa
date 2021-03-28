package pages;

import data.Load;
import data.Save;
import models.Message;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Chat
{
    public static void chat(User one, User two, List<String> lastPLace) throws IOException, InterruptedException
    {
        if (!one.directMessages.containsKey(two.id))
        {
            ArrayList<String[]> arrayList = new ArrayList<>();
            one.directMessages.put(two.id, arrayList);
            two.directMessages.put(one.id, arrayList);
            Save.saveUser(one);
            Save.saveUser(two);
        }
        boolean chatFlag = true;
        while (chatFlag)
        {
            User me = Load.findUser(one.id);
            User them = Load.findUser(two.id);
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + them.username + "'s Chat");
            System.out.println("------------------------------------------------------");
            for (String[] messageArray : me.directMessages.get(them.id))
            {
                messageArray[1] = "t";
                StringBuilder text = new StringBuilder();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                if (messageArray[0].charAt(0) == 's')
                {
                    Message message = Load.findMessage(messageArray[0].substring(2));
                    text.append(simpleDateFormat.format(message.messageTime)).append(" ").append(Load.findUser(message.sender).username).append(": ");
                    text.append(message.text);
                    System.out.println(ConsoleColors.WHITE_BRIGHT + text.toString());
                }
                else if (messageArray[0].charAt(0) == 'r')
                {
                    Message message = Load.findMessage(messageArray[0].substring(2));
                    text.append(simpleDateFormat.format(message.messageTime)).append(" ").append(Load.findUser(message.sender).username).append(": ");
                    text.append(message.text);
                    System.out.println(ConsoleColors.CYAN_BRIGHT + text.toString());
                }
                else if (messageArray[0].charAt(0) == 'S')
                {
                    Tweet tweet = Load.findTweet(messageArray[0].substring(2));
                    text.append(me.username).append(": ").append("@").append(Load.findUser(tweet.owner).username);
                    text.append(": ").append(tweet.text);
                    System.out.println(ConsoleColors.WHITE_BRIGHT + text.toString());
                }
                else if (messageArray[0].charAt(0) == 'R')
                {
                    Tweet tweet = Load.findTweet(messageArray[0].substring(2));
                    text.append(them.username).append(": ").append("@").append(Load.findUser(tweet.owner).username);
                    text.append(": ").append(tweet.text);
                    System.out.println(ConsoleColors.CYAN_BRIGHT + text.toString());
                }
            }

            String command = scanner.nextLine();

            if (!command.equals(" ") && !command.equals(":q!"))
                new Message(me.id, them.id, command);
            else if (command.equals(":q!")) // I know, I'm going to hell for this...
            {
                chatFlag = false;
                if (lastPLace.get(lastPLace.size() - 1).equals("direct"))
                    DirectMessages.directMessages(me);
                else if (lastPLace.get(lastPLace.size() - 1).equals("explore"))
                    Explore.explore(me);
                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='u') // User
                    ViewUser.viewUser(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='f') // Followers
                    Followers.followers(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
                else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='i') // Followings
                    Followings.followings(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
            }
        }
    }
}