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
            System.out.println(ConsoleColors.WHITE_BRIGHT + "Choose a username:");
            userName = scanner.nextLine().toLowerCase();
            if (userName.equals("i already have an account")) {
                Login login = new Login();
                return login.user;
            }
            if (!Validations.usernameIsValid(userName))
                System.out.println(ConsoleColors.RED_BRIGHT + "This username isn't valid. PLease choose another one...");
            if (!Validations.usernameIsAvailable(userName))
            {
                System.out.println(ConsoleColors.RED_BRIGHT + "This username has already been used. PLease choose another one...");
                System.out.println(ConsoleColors.CYAN + " Already have an account? You can go to the Login page by typing \"" +
                        ConsoleColors.CYAN_UNDERLINED + "I already have an account" + ConsoleColors.CYAN + "\".");
            }
        } while (!Validations.usernameIsValid(userName) || !Validations.usernameIsAvailable(userName));

        while (true)
        {
            System.out.println(ConsoleColors.WHITE_BRIGHT + "Choose a password:");
            password = scanner.nextLine();
            if (password.length()<8)
                System.out.println(ConsoleColors.RED_BRIGHT + "Password must be at least 8 characters long.");
            else
                break;
        }

        while (true)
        {
            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your name:");
            name = scanner.nextLine();
            if (name.equals(""))
                System.out.println(ConsoleColors.RED_BRIGHT + "Filling this part isn't optional.");
            else
                break;
        }

        do {
            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your email address:");
            email = scanner.nextLine();
            if (email.equals("I already have an account")) {
                Login login = new Login();
                return login.user;
            }
            if (!Validations.emailIsValid(email))
                System.out.println(ConsoleColors.RED_BRIGHT + "PLease enter a valid email address...");
            if (!Validations.emailIsAvailable(email))
            {
                System.out.println(ConsoleColors.RED_BRIGHT + "There's already an account with this email address registered.");
                System.out.println("PLease choose another one...");
                System.out.println(ConsoleColors.CYAN + " Already have an account? You can go to the Login page by typing \"" +
                        ConsoleColors.CYAN_UNDERLINED + "I already have an account" + ConsoleColors.CYAN + "\".");
            }
        } while (!Validations.emailIsValid(email) || !Validations.emailIsAvailable(email));

        while (true)
        {
            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your phone number (optional):");
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
                    System.out.println(ConsoleColors.RED_BRIGHT + "PLease enter a valid phone number..." +
                            "\n Acceptable numbers are in the format \"+11234567890\".");
                }
                if (!Validations.phoneNumberIsAvailable(phoneNumber))
                {
                    System.out.println(ConsoleColors.RED_BRIGHT + "There's already an account with this phone number registered.");
                    System.out.println("PLease choose another one...");
                    System.out.println(ConsoleColors.CYAN + " Already have an account? You can go to the Login page by typing \"" +
                            ConsoleColors.CYAN_UNDERLINED + "I already have an account" + ConsoleColors.CYAN + "\".");
                }
                if (Validations.phoneNumberIsValid(phoneNumber) && Validations.phoneNumberIsAvailable(phoneNumber))
                    break;
            }
        }

        while (true)
        {
            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your date of birth (optional):");
            date = scanner.nextLine();
            if (date.equals(""))
                break;
            else
            {
                if (!Validations.dateIsValid(date))
                    System.out.println(ConsoleColors.RED_BRIGHT + "PLease enter a valid date..." +
                            "\n Acceptable dates are in format \"yyyy-MM-dd\".");
                else
                    break;
            }
        }

        System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter a bio for your page (optional):");
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
        return this.user;
    }
}