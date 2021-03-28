package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Message;
import models.Tweet;
import models.User;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Save
{
    static private final Logger logger = LogManager.getLogger(Save.class);

    public static void saveUser(User user) throws IOException
    {
        String path = "./src/main/resources/users/" + user.id;
        File file = new File(path);
        if(file.getParentFile().mkdirs())
            logger.warn("users directory was created.");
        if (!file.exists())
        {
            if (file.createNewFile())
                logger.debug(user.id + "th user's file was created.");
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
        logger.debug(user.id + "th user's file was saved.");
    }

    public static void saveTweet(Tweet tweet) throws IOException
    {
        String path = "./src/main/resources/tweets/" + tweet.id;
        File file = new File(path);
        if(file.getParentFile().mkdirs())
            logger.warn("tweets directory was created.");
        if (!file.exists())
        {
            if (file.createNewFile())
                logger.debug("tweet " + tweet.id + "'s file was created.");
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
        logger.debug("tweet " + tweet.id + "'s file was saved.");
    }

    public static void saveMessage(Message message) throws IOException
    {
        String path = "./src/main/resources/messages/" + message.id;
        File file = new File(path);
        if(file.getParentFile().mkdirs())
        {
            logger.warn("messages directory was created.");
        }
        if (!file.exists())
        {
            if (file.createNewFile())
            {
                logger.debug("message " + message.id + "'s file was created.");
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        String data = gson.toJson(message);

        FileOutputStream fout = new FileOutputStream(path, false);
        PrintStream printStream = new PrintStream(fout);
        printStream.println(data);

        printStream.flush();
        printStream.close();
        fout.flush();
        fout.close();
        logger.debug("message " + message.id + "'s file was saved.");
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
        replaceLine( "./src/main/resources/database/usernames.txt", oldUsername, username);
        logger.debug("usernames list was updated.");
    }

    public static void changeEmail(String oldEmail, String email) throws IOException
    {
        replaceLine( "./src/main/resources/database/emails.txt", oldEmail, email);
        logger.debug("emails list was updated.");
    }

    public static void changePhoneNumber(String oldPhoneNumber, String phoneNumber) throws IOException
    {
        replaceLine( "./src/main/resources/database/phonenumbers.txt", oldPhoneNumber, phoneNumber);
        logger.debug("phonenumbers list was updated.");
    }

    public static void changeLastId(long newId) throws IOException
    {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("./src/main/resources/database/id.txt"), StandardCharsets.UTF_8));
        fileContent.set(0, newId + "");
        Files.write(Paths.get("./src/main/resources/database/id.txt"), fileContent, StandardCharsets.UTF_8);
        logger.info("last id was changed.");
    }

    public static void changeLastMessage(long newId) throws IOException
    {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("./src/main/resources/database/message.txt"), StandardCharsets.UTF_8));
        fileContent.set(0, newId + "");
        Files.write(Paths.get("./src/main/resources/database/message.txt"), fileContent, StandardCharsets.UTF_8);
        logger.info("last message was changed.");
    }
}