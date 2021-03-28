package pages;

import data.Load;
import models.Message;
import models.Tweet;
import models.User;
import utils.ConsoleColors;
import utils.Input;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DirectMessages
{
    public static void directMessages(User user) throws IOException, InterruptedException
    {
        Scanner scanner = Input.scanner();
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Direct Messages");
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
            System.out.println(ConsoleColors.CYAN_BRIGHT + "@" + person.username);
            System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
        }

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");
        System.out.println(ConsoleColors.PURPLE_BRIGHT + "u/x: go to user x's chat");

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
                if (Load.userExists(command.substring(2)))
                {
                    User otherPerson = Load.findUser(command.substring(2));
                    flag = false;
                    List<String> lastPlace = new LinkedList<>();
                    lastPlace.add("direct");
                    Chat.chat(user, otherPerson, lastPlace);
                }
                else
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
            }
        }
    }
}