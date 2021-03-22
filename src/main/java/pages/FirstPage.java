package pages;

import entry.Enter;
import utils.ConsoleColors;

import java.io.IOException;

public class FirstPage {
    public static void firstPage() throws IOException, InterruptedException
    {
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
            MainPage.mainPage(enter.user);
        }
    }
}