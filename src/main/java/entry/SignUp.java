package entry;

import models.User;
import utils.ConsoleColors;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class SignUp
{
    public User user;

    public SignUp() throws IOException
    {
        this.user = makeUser();
    }

    public User makeUser() throws IOException
    {
        Scanner scanner = new Scanner(System.in);

        String userName, password, name, email, phoneNumber, date;

        do {
            System.out.println(ConsoleColors.WHITE + "Choose a username:");
            userName = scanner.nextLine();
            if (userName.equals("I already have an account")) {
                Login login = new Login();
                return login.user;
            }
            if (!Validations.usernameIsValid(userName))
                System.out.println(ConsoleColors.RED + "This username isn't valid. PLease choose another one...");
            if (!Validations.usernameIsAvailable(userName))
            {
                System.out.println(ConsoleColors.RED + "This username has already been used. PLease choose another one...");
                System.out.println("PLease choose another one or go to the Login page by typing \"" +
                        ConsoleColors.RED_UNDERLINED + "I already have an account" +
                        ConsoleColors.RED + "\" if you remember your password.");
            }
        } while (!Validations.usernameIsValid(userName) || !Validations.usernameIsAvailable(userName));

        while (true)
        {
            System.out.println(ConsoleColors.WHITE + "Choose a password:");
            password = scanner.nextLine();
            if (password.length()<8)
                System.out.println(ConsoleColors.RED + "Password must be at least 8 characters long.");
            else
                break;
        }

        while (true)
        {
            System.out.println(ConsoleColors.WHITE + "Enter your name:");
            name = scanner.nextLine();
            if (name.equals(""))
                System.out.println(ConsoleColors.RED + "Filling this part isn't optional.");
            else
                break;
        }

        do {
            System.out.println(ConsoleColors.WHITE + "Enter your email address:");
            email = scanner.nextLine();
            if (email.equals("I already have an account")) {
                Login login = new Login();
                return login.user;
            }
            if (!Validations.emailIsValid(email))
                System.out.println(ConsoleColors.RED + "PLease enter a valid email address...");
            if (!Validations.emailIsAvailable(email))
            {
                System.out.println(ConsoleColors.RED + "There's already an account with this email address registered.");
                System.out.println("PLease choose another one or go to the Login page by typing \"" +
                        ConsoleColors.RED_UNDERLINED + "I already have an account" +
                        ConsoleColors.RED + "\" if you remember your password.");
            }
        } while (!Validations.emailIsValid(email) || !Validations.emailIsAvailable(email));

        while (true)
        {
            System.out.println(ConsoleColors.WHITE + "Enter your phone number (optional):");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.equals(""))
                break;
            else
            {
                if (phoneNumber.equals("I already have an account"))
                {
                    Login login = new Login();
                    return login.user;
                }
                if (!Validations.phoneNumberIsValid(phoneNumber))
                {
                    System.out.println(ConsoleColors.RED + "PLease enter a valid phone number..." +
                            "\n Acceptable numbers are in format \"+11234567890\"");
                }
                if (!Validations.phoneNumberIsAvailable(phoneNumber))
                {
                    System.out.println(ConsoleColors.RED + "There's already an account with this phone number registered.");
                    System.out.println("PLease choose another one or go to the Login page by typing \"" +
                            ConsoleColors.RED_UNDERLINED + "I already have an account" +
                            ConsoleColors.RED + "\" if you remember your password.");
                }
                if (Validations.phoneNumberIsValid(phoneNumber) && Validations.phoneNumberIsAvailable(phoneNumber))
                    break;
            }
        }

        while (true)
        {
            System.out.println(ConsoleColors.WHITE + "Enter your date of birth (optional):");
            date = scanner.nextLine();
            if (date.equals(""))
                break;
            else
            {
                if (!Validations.dateIsValid(date))
                    System.out.println(ConsoleColors.RED + "PLease enter a valid date..." +
                            "\n Acceptable dates are in format \"yyyy-MM-dd\"");
                else
                    break;
            }
        }

        System.out.println(ConsoleColors.WHITE + "Enter a bio for your page (optional):");
        String bio = scanner.nextLine();

        this.user = new User(userName, password);
        this.user.setName(name);
        this.user.setEmail(email);
        this.user.setPhoneNumber(phoneNumber);
        this.user.setBio(bio);

        if (!date.equals(""))
        {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = null;
            try
            {
                birthDate = format.parse(date);
                this.user.setBirthDate(birthDate);
            }
            catch (Exception e)
            {
                System.out.println(ConsoleColors.RED + "Parsing date failed...");
            }
        }

        scanner.close();
        return this.user;
    }
}