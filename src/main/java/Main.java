import entry.Enter;
import pages.HomePage;
import utils.ConsoleColors;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        // Greetings
        System.out.println(ConsoleColors.BLUE_BRIGHT + "Welcome to " +
                ConsoleColors.BLUE_BOLD_BRIGHT + " Eack Xoa" +
                ConsoleColors.BLUE_BRIGHT + ". World's worst social media.");
        System.out.println("------------------------------------------------------");
        // Enter the app
        Enter enter = new Enter();
        if (enter.user != null)
        {

            System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "------------------------------------------------------");
            System.out.println("Logged in successfully.");
            System.out.println("------------------------------------------------------");
            HomePage.mainPage(enter.user);
        }
    }
}
