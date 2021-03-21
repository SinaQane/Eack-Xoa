package pages;

import entry.Validations;
import models.User;
import utils.ConsoleColors;
import utils.Input;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Settings
{
    public static void settings(User user) throws IOException, InterruptedException
    {
        boolean settingsFlag = true;
        while (settingsFlag)
        {
            Scanner scanner = Input.scanner();
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Settings");
            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT +"List of available commands: \n");
            System.out.println(ConsoleColors.PURPLE_BRIGHT + "main: go back to the Main Page");
            System.out.println("name: change your name");
            System.out.println("username: change your username");
            System.out.println("email: change your email");
            System.out.println("birthdate: change your birthdate");
            System.out.println("phone number: change your phone number");
            System.out.println("bio: change your bio");
            System.out.println("last seen: change your last seen status privacy");
            System.out.println("private: change your account's public/private status");
            System.out.println("info: change your info share privacy status");
            System.out.println("password: change your password");
            System.out.println("deactivate: deactivate your account");
            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

            boolean deactivated = false;
            boolean flag = true;
            while (flag)
            {
                String command = scanner.nextLine().toLowerCase();
                switch (command)
                {
                    case "main":
                        flag = false;
                        settingsFlag = false;
                        MainPage.mainPage(user);
                        break;
                    case "name":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new name:");
                        String newName = scanner.nextLine();
                        if (!newName.equals(""))
                        {
                            user.setName(newName);
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                            System.out.println("Your name was changed successfully.");
                            System.out.println("------------------------------------------------------");
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Name can not be empty.");
                        flag = false;
                        break;
                    case "username":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new username:");
                        String newUsername = scanner.nextLine().toLowerCase();
                        if (Validations.usernameIsAvailable(newUsername))
                        {
                            if (Validations.usernameIsValid(newUsername))
                            {
                                user.setUsername(newUsername);
                                System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                                System.out.println("Your username was changed successfully.");
                                System.out.println("------------------------------------------------------");
                            }
                            else
                                System.out.println(ConsoleColors.RED_BRIGHT + "This username isn't valid.");
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "This username has already been used.");
                        flag = false;
                        break;
                    case "email":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new email:");
                        String newEmail = scanner.nextLine();
                        if (Validations.emailIsValid(newEmail))
                        {
                            if (Validations.emailIsAvailable(newEmail))
                            {
                                user.setEmail(newEmail);
                                System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                                System.out.println("Your email was changed successfully.");
                                System.out.println("------------------------------------------------------");
                            }
                            else
                                System.out.println(ConsoleColors.RED_BRIGHT + "There's already an account with this email address registered.");
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "This email address isn't valid.");
                        flag = false;
                        break;
                    case "birthdate":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new birthdate:" +
                                "\n Acceptable dates are in format \"yyyy-MM-dd\".");
                        String newBirthdate = scanner.nextLine();
                        if (newBirthdate.equals(""))
                        {
                            user.setBirthDate(null);
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                            System.out.println("Your birthdate was changed successfully.");
                            System.out.println("------------------------------------------------------");
                        }
                        else
                        {
                            if (Validations.dateIsValid(newBirthdate))
                            {
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date birthDate;
                                try
                                {
                                    birthDate = format.parse(newBirthdate);
                                    user.setBirthDate(birthDate);
                                    System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                                    System.out.println("Your birthdate was changed successfully.");
                                    System.out.println("------------------------------------------------------");
                                }
                                catch (Exception e)
                                {
                                    System.out.println(ConsoleColors.RED + "Parsing date failed...");
                                }
                            }
                            else
                                System.out.println(ConsoleColors.RED_BRIGHT + "This date isn't valid.");
                        }
                        flag = false;
                        break;
                    case "phone number":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new phone number:" +
                                "\n Acceptable numbers are in the format \"+11234567890\".");
                        String newPhoneNumber = scanner.nextLine();
                        if (newPhoneNumber.equals(""))
                        {
                            user.setPhoneNumber("");
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                            System.out.println("Your phone number was changed successfully.");
                            System.out.println("------------------------------------------------------");
                        }
                        else
                        {
                            if (Validations.phoneNumberIsValid(newPhoneNumber))
                            {
                                if (Validations.phoneNumberIsAvailable(newPhoneNumber))
                                {
                                    user.setPhoneNumber(newPhoneNumber);
                                    System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                                    System.out.println("Your phone number was changed successfully.");
                                    System.out.println("------------------------------------------------------");
                                }
                                else
                                    System.out.println(ConsoleColors.RED_BRIGHT + "There's already an account with this phone number registered.");
                            }
                            else
                                System.out.println(ConsoleColors.RED_BRIGHT + "This phone number isn't valid.");
                        }
                        flag = false;
                        break;
                    case "bio":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new bio:");
                        String newBio = scanner.nextLine();
                        user.setBio(newBio);
                        System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                        System.out.println("Your bio was changed successfully.");
                        System.out.println("------------------------------------------------------");
                        flag = false;
                        break;
                    case "last seen":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new last seen status:");
                        System.out.println(" You can choose from \"nobody\", \"followings only\" and \"everyone\".");
                        String newLastSeen = scanner.nextLine().toLowerCase();
                        switch (newLastSeen) {
                            case "nobody":
                                user.setLastSeenState(0);
                                System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                                System.out.println("Your last seen status is now visible to nobody.");
                                System.out.println("------------------------------------------------------");
                                break;
                            case "followings only":
                                user.setLastSeenState(1);
                                System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                                System.out.println("Your last seen status is now visible to your followings only.");
                                System.out.println("------------------------------------------------------");
                                break;
                            case "everyone":
                                user.setLastSeenState(2);
                                System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                                System.out.println("Your last seen status is now visible to everyone.");
                                System.out.println("------------------------------------------------------");
                                break;
                            default:
                                System.out.println(ConsoleColors.RED_BRIGHT + "Invalid command...");
                                break;
                        }
                        flag = false;
                        break;
                    case "private":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new page public/private status:");
                        System.out.println(" You can choose from \"public\" or \"private\".");
                        String newPrivateState = scanner.nextLine().toLowerCase();
                        if (newPrivateState.equals("public"))
                        {
                            user.setPrivateState(false);
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                            System.out.println("Your page is now public.");
                            System.out.println("------------------------------------------------------");
                        }
                        else if (newPrivateState.equals("private"))
                        {
                            user.setPrivateState(true);
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                            System.out.println("Your page is now private.");
                            System.out.println("------------------------------------------------------");
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid command...");
                        flag = false;
                        break;
                    case "info":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new page info privacy status:");
                        System.out.println(" You can choose from \"public\" or \"private\".");
                        String newInfoState = scanner.nextLine().toLowerCase();
                        if (newInfoState.equals("public"))
                        {
                            user.setInfoState(true);
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                            System.out.println("Your page's info is now public.");
                            System.out.println("------------------------------------------------------");
                        }
                        else if (newInfoState.equals("private"))
                        {
                            user.setInfoState(false);
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                            System.out.println("Your page's info is now private.");
                            System.out.println("------------------------------------------------------");
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid command...");
                        flag = false;
                        break;
                    case "password":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your current password:");
                        boolean pass = true;
                        while (pass)
                        {
                            String oldPassword = scanner.nextLine();
                            if (oldPassword.equals(user.password))
                            {
                                while (true)
                                {
                                    System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new password:");
                                    String newPassword = scanner.nextLine();
                                    if (newPassword.length()<8)
                                        System.out.println(ConsoleColors.RED_BRIGHT + "Password must be at least 8 characters long.");
                                    else
                                    {
                                        user.setPassword(newPassword);
                                        System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                                        System.out.println("Your password was changed successfully.");
                                        System.out.println("------------------------------------------------------");
                                        pass = false;
                                        break;
                                    }
                                }
                            }
                            else if (oldPassword.equals("go back"))
                                pass = false;
                            else
                                {
                                System.out.println(ConsoleColors.RED_BRIGHT + "The password you have entered is wrong." +
                                        "\n Try again or cancel by typing \"" + ConsoleColors.RED_UNDERLINED
                                        + "Go back" + ConsoleColors.RED_BRIGHT + "\".");
                            }
                        }
                        flag = false;
                        break;
                    case "deactivate":
                        System.out.println(ConsoleColors.YELLOW_BRIGHT + "Are you sure you want to deactivate your account? (y/n)");
                        String deactivate = scanner.nextLine().toLowerCase();
                        if (deactivate.equals("y"))
                        {
                            System.out.print(ConsoleColors.YELLOW_BRIGHT + "Alright then... Deactivating");
                            Thread.sleep(1000);
                            System.out.print(".");
                            Thread.sleep(1000);
                            System.out.print(".");
                            Thread.sleep(1000);
                            System.out.println(".");
                            user.deactivate();
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                            System.out.println("Your account was deactivated successfully. Hope to see you back soon...");
                            System.out.println("------------------------------------------------------");
                            flag = false;
                            deactivated = true;

                        }
                        else if (deactivate.equals("n"))
                            System.out.print(ConsoleColors.GREEN_BRIGHT + "Hooooof, Going back to settings page then...");
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid command...");
                        break;
                    default:
                        System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                        break;
                }
            }
            if (deactivated)
                settingsFlag = false; // break;
        }
        FirstPage.firstPage();
    }
}