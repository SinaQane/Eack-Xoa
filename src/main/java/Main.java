import data.Save;
import entry.Enter;
import utils.ConsoleColors;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        // Greetings
        System.out.println(ConsoleColors.BLUE + "Welcome to " +
                ConsoleColors.BLUE_BOLD + " Eack Xoa" +
                ConsoleColors.BLUE + ". World's worst social media.");

        // Enter the app
        Enter enter = new Enter();
        if (enter.user != null)
        {
            System.out.println(ConsoleColors.GREEN + "Logged in successfully.");
            // MainPage.mainPage(enter.user); Go to main page
        }

    }
}
