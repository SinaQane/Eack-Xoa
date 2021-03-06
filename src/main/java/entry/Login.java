package entry;

import data.Load;
import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.Validations;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class Login
{
    static private final Logger logger = LogManager.getLogger(Login.class);

    public User user;

    public Login() throws IOException
    {
        this.user = getUser();
    }

    public User getUser() throws IOException
    {
        Scanner scanner = Input.scanner();

        while (true) {
            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your username:");
            String username = scanner.nextLine().toLowerCase();
            if (username.equals("create an account"))
            {
                SignUp signUp = new SignUp();
                return signUp.user;
            }
            System.out.println(ConsoleColors.WHITE_BRIGHT + "Enter your password:");
            String password = scanner.nextLine();
            User result = Load.findUser(username);
            if (result != null)
            {
                if (password.equals(result.password))
                {
                    if (result.getIsActive())
                    {
                        this.user = result;
                        break;
                    }
                    else
                    {
                        System.out.println(ConsoleColors.YELLOW_BRIGHT + "This account has been deactivated. " +
                                "Would you like to reactivate it? (y/n)");
                        String response = scanner.nextLine();
                        while (!Validations.yesOrNo(response))
                        {
                            System.out.println(ConsoleColors.YELLOW_BRIGHT + "This account has been deactivated. " +
                                    "Would you like to reactivate it? (y/n)");
                            response = scanner.nextLine();
                        }
                        if (response.equals("y"))
                        {
                            System.out.println(ConsoleColors.GREEN_BRIGHT + "------------------------------------------------------");
                            System.out.println("Your Account has been reactivated.");
                            result.reactivate();
                            logger.fatal("account " + result.id + " was reactivated and logged into.");
                            this.user = result;
                            break;
                        }
                        else if (response.equals("n"))
                        {
                            System.out.println(ConsoleColors.YELLOW_BRIGHT + "Login to another account then.");
                            System.out.println(ConsoleColors.CYAN + " Don't have an account yet? You can go to Sign Up page by typing \"" +
                                    ConsoleColors.CYAN_UNDERLINED + "Create an account" + ConsoleColors.CYAN + "\" as your username.");
                        }
                    }
                }
                else
                {
                    logger.error("there was an unsuccessful login to account " + result.id + ".");
                    System.out.println(ConsoleColors.RED_BRIGHT + "The username or password is incorrect. Please try again...");
                    System.out.println(ConsoleColors.CYAN + " Don't have an account yet? You can go to Sign Up page by typing \"" +
                            ConsoleColors.CYAN_UNDERLINED + "Create an account" + ConsoleColors.CYAN + "\" as your username.");
                }
            }
            else
            {
                System.out.println(ConsoleColors.RED_BRIGHT + "The username or password is incorrect. Please try again...");
                System.out.println(ConsoleColors.CYAN + " Don't have an account yet? You can go to Sign Up page by typing \"" +
                        ConsoleColors.CYAN_UNDERLINED + "Create an account" + ConsoleColors.CYAN + "\" as your username.");
            }
        }
        Date loginDate = new Date();
        this.user.setLastLogin(loginDate);
        return this.user;
    }
}