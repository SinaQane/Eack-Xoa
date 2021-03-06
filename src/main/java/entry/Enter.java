package entry;

import models.User;
import utils.ConsoleColors;
import utils.Input;
import utils.Validations;

import java.io.IOException;
import java.util.Scanner;

public class Enter
{
    // Check if the user is new to the app or already has an account
    public User user;

    public Enter() throws IOException
    {
        this.user = user();
    }

    public User user() throws IOException
    {
        Scanner scanner = Input.scanner();

        System.out.println(ConsoleColors.WHITE_BRIGHT + "Do you have an account? (y/n)");

        String isRegistered = scanner.nextLine().toLowerCase();

        while (!Validations.yesOrNo(isRegistered))
        {
            System.out.println(ConsoleColors.WHITE_BRIGHT + "Do you have an account? (y/n)");
            isRegistered = scanner.nextLine();
        }
        switch (isRegistered)
        {
            case "y":
                Login login = new Login();
                this.user = login.user;
                break;
            case "n":
                SignUp signUp = new SignUp();
                this.user = signUp.user;
                break;
        }
        return this.user;
    }
}