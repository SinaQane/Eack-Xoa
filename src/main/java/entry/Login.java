package entry;

import data.Load;
import models.User;
import utils.ConsoleColors;

import java.io.IOException;
import java.util.Scanner;

public class Login
{
    public User user;

    public Login() throws IOException
    {
        this.user = getUser();
    }

    public User getUser() throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(ConsoleColors.WHITE + "Enter your username:");
            String username = scanner.nextLine();
            if (username.equals("Create an account"))
            {
                SignUp signUp = new SignUp();
                return signUp.user;
            }
            System.out.println(ConsoleColors.WHITE + "Enter your password:");
            String password = scanner.nextLine();
            User result = Load.findUser(username);
            if (result != null)
            {
                if (password.equals(result.password))
                {
                    this.user = result;
                    break;
                }
                else
                {
                    System.out.println(ConsoleColors.RED + "The username or password is incorrect. Please try again...");
                    System.out.println(ConsoleColors.RED + "Don't have an account yet? You can go to Sign Up page by typing \"" +
                            ConsoleColors.RED_UNDERLINED + "Create an account" + ConsoleColors.RED + "\" as your username.");
                }
            }
            else
            {
                System.out.println(ConsoleColors.RED + "The username or password is incorrect. Please try again...");
                System.out.println(ConsoleColors.RED + "Don't have an account yet? You can go to Sign Up page by typing \"" +
                        ConsoleColors.RED_UNDERLINED + "Create an account" + ConsoleColors.RED + "\" as your username.");
            }
        }
        scanner.close();
        return this.user;
    }
}
