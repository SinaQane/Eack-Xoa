package pages;

import data.Load;
import models.Message;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
                    chat(user, otherPerson);
                }
                catch (Exception e)
                {
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                }
            }
        }
    }

    public static void chat(User me, User them) throws IOException, InterruptedException
    {
        Scanner scanner = Input.scanner();
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + them.username + "'s Chatroom");
        System.out.println("------------------------------------------------------");
        for (String[] messageArray : me.directMessages.get(them.id))
        {
            messageArray[1] = "t";
            StringBuilder text = new StringBuilder();
            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");
            if (messageArray[0].charAt(0) == 's')
            {
                Message message = Load.findMessage(messageArray[0].substring(1));
                text.append(sdfDate.format(message.messageTime)).append(" ").append(me.username).append(": ");
                text.append(message.text);
                System.out.println(ConsoleColors.WHITE_BRIGHT + text.toString());
            }
            else if (messageArray[0].charAt(0) == 'r')
            {
                Message message = Load.findMessage(messageArray[0].substring(1));
                text.append(sdfDate.format(message.messageTime)).append(" ").append(them.username).append(": ");
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
            chat (me, them);
        else if (command.equals(":q!")) // I know, I'm going to hell for this...
            directMessages(me);
        else
        {
            me.addMessage(them.id, "s" + command);
            chat (me, them);
        }
    }
}