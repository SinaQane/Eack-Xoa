package pages;

import entry.Validations;
import models.User;
import utils.ConsoleColors;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Settings
{
    public static void settings(User user) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        boolean active = true;
        while (active)
        {
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Settings Page");
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
            System.out.println("deactivate: deactivate your account");
            System.out.println("------------------------------------------------------");

            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a command:");

            boolean flag = true;
            while (flag)
            {
                String command = scanner.nextLine().toLowerCase();
                switch (command)
                {
                    case "main":
                        flag = false;
                        HomePage.mainPage(user);
                        break;
                    case "name":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new name:");
                        String newName = scanner.nextLine();
                        if (!newName.equals(""))
                            user.setName(newName);
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
                                user.setUsername(newUsername);
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
                                user.setEmail(newEmail);
                            else
                                System.out.println(ConsoleColors.RED_BRIGHT + "There's already an account with this email address registered.");
                        }
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "This email address isn't valid.");
                        flag = false;
                        break;
                    case "birthdate":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new birthdate:" +
                                "\n Acceptable dates are in format \"yyyy-MM-dd\"");
                        String newBirthdate = scanner.nextLine();
                        if (newBirthdate.equals(""))
                            user.setBirthDate(null);
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
                                "\n Acceptable numbers are in the format \"+11234567890\"");
                        String newPhoneNumber = scanner.nextLine();
                        if (newPhoneNumber.equals(""))
                            user.setPhoneNumber("");
                        else
                        {
                            if (Validations.phoneNumberIsValid(newPhoneNumber))
                            {
                                if (Validations.phoneNumberIsAvailable(newPhoneNumber))
                                    user.setPhoneNumber(newPhoneNumber);
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
                        flag = false;
                        break;
                    case "last seen":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new last seen status:");
                        System.out.println("You can choose from \"nobody\", \"followings only\" and \"everyone\".");
                        String newLastSeen = scanner.nextLine().toLowerCase();
                        if (newLastSeen.equals("nobody"))
                            user.setLastSeenState(0);
                        else if (newLastSeen.equals("followings only"))
                            user.setLastSeenState(1);
                        else if (newLastSeen.equals("everyone"))
                            user.setLastSeenState(2);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid command...");
                        flag = false;
                        break;
                    case "private":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new page public/private status:");
                        System.out.println("You can choose from \"public\" or \"private\".");
                        String newPrivateState = scanner.nextLine().toLowerCase();
                        if (newPrivateState.equals("public"))
                            user.setPrivateState(false);
                        else if (newPrivateState.equals("private"))
                            user.setPrivateState(true);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid command...");
                        flag = false;
                        break;
                    case "info":
                        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your new page info privacy status:");
                        System.out.println("You can choose from \"public\" or \"private\".");
                        String newInfoState = scanner.nextLine().toLowerCase();
                        if (newInfoState.equals("public"))
                            user.setInfoState(true);
                        else if (newInfoState.equals("private"))
                            user.setInfoState(false);
                        else
                            System.out.println(ConsoleColors.RED_BRIGHT + "Invalid command...");
                        flag = false;
                        break;
                    case "deactivate":
                        System.out.println(ConsoleColors.YELLOW_BRIGHT + "Are you sure you want to deactivate your account? (y/n)");
                        String deactivate = scanner.nextLine().toLowerCase();
                        if (deactivate.equals("y"))
                        {
                            System.out.print(ConsoleColors.YELLOW_BRIGHT + "Alright then... Deactivating");
                            Thread.sleep(500);
                            System.out.print(".");
                            Thread.sleep(500);
                            System.out.print(".");
                            Thread.sleep(500);
                            System.out.print(".");
                            user.deactivate();
                            flag = false;
                            active = false;
                        }
                        break;
                    default:
                        System.out.println(ConsoleColors.RED_BRIGHT + "Please enter a valid command:");
                        break;
                }
            }

        }

    }
}
