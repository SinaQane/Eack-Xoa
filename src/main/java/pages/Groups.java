package pages;

import data.Load;
import data.Save;
import models.User;
import utils.ConsoleColors;
import utils.Input;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Groups
{
    public static void groups(User user) throws IOException, InterruptedException {
        boolean groupFlag = true;
        while (groupFlag)
        {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Groups");
            System.out.println("------------------------------------------------------");

            if (user.groups.keySet().size() > 0)
            {
                for (String key : user.groups.keySet())
                {
                    System.out.println(ConsoleColors.CYAN_BRIGHT + key);
                    StringBuilder value = new StringBuilder();
                    for (Long id : user.groups.get(key))
                    {
                        value.append(Load.findUser(id).username).append(" ");
                    }
                    System.out.println(ConsoleColors.CYAN + "Members: " + value.toString());
                    System.out.println("------------------------------------------------------");
                }
            }
            else
            {
                System.out.println(ConsoleColors.YELLOW_BRIGHT + "You don't have any groups...");
                System.out.println(ConsoleColors.CYAN_BRIGHT + "------------------------------------------------------");
            }

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "list of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "home: go back to the Home Page");
            System.out.println("new group x: create a new group named x");

            if (user.groups.keySet().size() > 0)
            {
                System.out.println("delete group x: delete existing group x");
                System.out.println("add group x y: add user y to group x");
                System.out.println("rem group x y: remove user y from group x");

            }

            boolean flag = true;
            while (flag)
            {
                String command = scanner.nextLine();
                if (command.equals("home"))
                {
                    groupFlag = false;
                    HomePage.homePage(user);
                    break;
                }
                else if (command.startsWith("new group"))
                {
                    if (user.groups.containsKey(command.substring(10)))
                        System.out.println(ConsoleColors.RED_BRIGHT + "Group already exists...");
                    else
                    {
                        List<Long> list = new LinkedList<>();
                        user.groups.put(command.substring(10), list);
                        Save.saveUser(user);
                    }
                    flag = false;
                }
                else if (command.startsWith("delete group"))
                {
                    if (user.groups.keySet().size() > 0)
                    {
                        String name = command.substring(13);
                        try
                        {
                            user.groups.remove(name);
                            Save.saveUser(user);
                        }
                        catch (Exception e)
                        {
                                System.out.println(ConsoleColors.RED_BRIGHT + "Invalid group name...");
                        }
                    }
                    else
                        System.out.println(ConsoleColors.RED_BRIGHT + "Invalid request...");
                    flag = false;
                }
                else if (command.startsWith("add group"))
                {
                    String[] names = command.substring(9).split(" ");
                    if (user.groups.containsKey(names[0]))
                    {
                        if (!user.groups.get(names[0]).contains(Load.findUser(names[1]).id))
                        {
                            user.groups.get(names[0]).add(Load.findUser(names[1]).id);
                            Save.saveUser(user);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "User already exists in this group...");
                    }
                    else
                        System.out.println(ConsoleColors.RED_BRIGHT + "Invalid group name...");
                    flag = false;
                }
                else if (command.startsWith("rem group"))
                {
                    String[] names = command.substring(9).split(" ");
                    if (user.groups.containsKey(names[0]))
                    {
                        if (user.groups.get(names[0]).contains(Load.findUser(names[1]).id))
                        {
                            user.groups.get(names[0]).remove(Load.findUser(names[1]).id);
                            Save.saveUser(user);
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "User doesn't exist in this group...");
                    }
                    else
                        System.out.println(ConsoleColors.RED_BRIGHT + "Invalid group name...");
                    flag = false;
                }
                else
                    System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
            }

        }
    }
}