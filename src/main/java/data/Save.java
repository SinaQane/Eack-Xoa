package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Tweet;
import models.User;
import utils.ConsoleColors;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Save
{

    public static void saveUser(User user) throws IOException
    {
        String path = "./resources/users/" + user.username;
        File file = new File(path);
        if(file.getParentFile().mkdirs())
            System.out.println(ConsoleColors.GREEN + "Users directory was made successfully.");
        if (!file.exists())
        {
            if (file.createNewFile())
                System.out.println(ConsoleColors.GREEN + "User file was made successfully.");
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        String data = gson.toJson(user);

        FileOutputStream fout = new FileOutputStream(path, false);
        PrintStream printStream = new PrintStream(fout);
        printStream.println(data);

        printStream.flush();
        printStream.close();
        fout.flush();
        fout.close();
    }

    public static void saveTweet(Tweet tweet) throws IOException
    {
        String path = "./resources/tweets/" + tweet.id;
        File file = new File(path);
        if(file.getParentFile().mkdirs())
            System.out.println(ConsoleColors.GREEN + "Tweets directory was made successfully.");
        if (!file.exists())
        {
            if (file.createNewFile())
                System.out.println(ConsoleColors.GREEN + "Tweet file was made successfully.");
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        String data = gson.toJson(tweet);

        FileOutputStream fout = new FileOutputStream(path, false);
        PrintStream printStream = new PrintStream(fout);
        printStream.println(data);

        printStream.flush();
        printStream.close();
        fout.flush();
        fout.close();
    }

    public static void replaceLine(String path, String oldLine, String newLine) throws IOException
    {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));

        boolean flag = true;
        for (int i = 0; i < fileContent.size(); i++)
        {
            if (fileContent.get(i).equals(oldLine))
            {
                fileContent.set(i, newLine);
                flag = false;
                break;
            }
        }
        if (flag)
            fileContent.add(newLine);
        Files.write(Paths.get(path), fileContent, StandardCharsets.UTF_8);
    }

    public static void changeUsername(String oldUsername, String username) throws IOException
    {
        replaceLine( "./resources/usernames.txt", oldUsername, username);
    }

    public static void changeEmail(String oldEmail, String email) throws IOException
    {
        replaceLine( "./resources/emails.txt", oldEmail, email);
    }

    public static void changePhoneNumber(String oldPhoneNumber, String phoneNumber) throws IOException
    {
        replaceLine( "./resources/phonenumbers.txt", oldPhoneNumber, phoneNumber);
    }
}
