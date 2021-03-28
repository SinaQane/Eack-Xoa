package pages;

import data.Load;
import models.Message;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DirectMessages
{
    public static void directMessages(User user) throws IOException, InterruptedException
    {
        Scanner scanner = Input.scanner();
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Followers");
        System.out.println("------------------------------------------------------");

        for (Long id : user.directMessages.keySet())
        {
            User person = Load.findUser(id);
            String unreadMessages = "";
            int cnt = 0;
            for (String[] messageArray : user.directMessages.get(id))
            {
                if (messageArray[1].equals("f"))
                    cnt++;
            }
            if (cnt>0)
                unreadMessages += "  -  " + "(" + cnt + ")";
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + person.name + unreadMessages);
            System.out.println(ConsoleColors.CYAN_BRIGHT + person.username);
            System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
        }

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "u/x: go to user x's chatroom");

        System.out.println("------------------------------------------------------");

        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

        boolean flag = true;
        while (flag)
        {
            String command = scanner.nextLine().toLowerCase();
            if (command.equals("main"))
                MainPage.mainPage(user);
            else
            {
                try
                {
                    User otherPerson = Load.findUser(command.substring(2));
                    flag = false;
                    List<String> lastPlace = new LinkedList<>();
                    lastPlace.add("direct");
                    chat(user, otherPerson, lastPlace);
                }
                catch (Exception e)
                {
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                }
            }
        }
    }

    public static void chat(User me, User them, List<String> lastPLace) throws IOException, InterruptedException
    {
        Scanner scanner = Input.scanner();
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + them.username + "'s Chatroom");
        System.out.println("------------------------------------------------------");
        if (!me.directMessages.containsKey(them.id))
        {
            ArrayList<String[]> arrayList = new ArrayList<>();
            me.directMessages.put(them.id, arrayList);
        }
        for (String[] messageArray : me.directMessages.get(them.id))
        {
            messageArray[1] = "t";
            StringBuilder text = new StringBuilder();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            if (messageArray[0].charAt(0) == 's')
            {
                Message message = Load.findMessage(messageArray[0].substring(1));
                text.append(simpleDateFormat.format(message.messageTime)).append(" ").append(me.username).append(": ");
                text.append(message.text);
                System.out.println(ConsoleColors.WHITE_BRIGHT + text.toString());
            }
            else if (messageArray[0].charAt(0) == 'r')
            {
                Message message = Load.findMessage(messageArray[0].substring(1));
                text.append(simpleDateFormat.format(message.messageTime)).append(" ").append(them.username).append(": ");
                text.append(message.text);
                System.out.println(ConsoleColors.CYAN_BRIGHT + text.toString());
            }
            else if (messageArray[0].charAt(0) == 'S')
            {
                Tweet tweet = Load.findTweet(messageArray[0].substring(1));
                text.append(them.username).append(": ").append("@").append(Load.findUser(tweet.owner).username);
                text.append(": ").append(tweet.text);
                System.out.println(ConsoleColors.WHITE_BRIGHT + text.toString());
            }
            else if (messageArray[0].charAt(0) == 'R')
            {
                Tweet tweet = Load.findTweet(messageArray[0].substring(1));
                text.append(me.username).append(": ").append("@").append(Load.findUser(tweet.owner).username);
                text.append(": ").append(tweet.text);
                System.out.println(ConsoleColors.CYAN_BRIGHT + text.toString());
            }
        }
        String command = scanner.nextLine();
        if (command.equals(" "))
            chat (me, them, lastPLace);
        else if (command.equals(":q!")) // I know, I'm going to hell for this...
        {
            if (lastPLace.get(lastPLace.size() - 1).equals("direct"))
                directMessages(me);
            else if (lastPLace.get(lastPLace.size() - 1).equals("explore"))
                Explore.explore(me);
            else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='u') // User
                ViewUser.viewUser(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
            else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='f') // Followers
                Followers.followers(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
            else if (lastPLace.get(lastPLace.size() - 1).charAt(0)=='i') // Followings
                Followings.followings(me, Load.findUser(Long.parseLong(lastPLace.get(lastPLace.size() - 1).substring(1))), lastPLace.subList(0, lastPLace.size() - 1));
        }
        else
        {
            me.addMessage(them.id, "s" + command);
            chat (me, them, lastPLace);
        }
    }
}