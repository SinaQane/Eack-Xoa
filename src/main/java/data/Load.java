package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Message;
import models.Tweet;
import models.User;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Load
{
    public static boolean userExists(String username) throws IOException
    {
        File dir = new File("./src/main/resources/users");
        String[] paths = dir.list();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        assert paths != null;
        for (String tempPath : paths)
        {
            User temp = gson.fromJson(Files.readString(Paths.get("./src/main/resources/users/" + tempPath)), User.class);
            if (temp.username.equals(username))
                return true;
        }
        return false;
    }

    // For finding user by his/her Username
    public static User findUser(String username) throws IOException
    {
        String path = "./src/main/resources/users";
        File dir = new File(path);
        String[] paths = dir.list();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        User result = null;
        assert paths != null;
        for (String tempPath : paths)
        {
            User temp = gson.fromJson(Files.readString(Paths.get(path + "/" + tempPath)), User.class);
            if (temp.username.equals(username))
            {
                result = temp;
                break;
            }
        }
        return result;
    }

    // For finding user by his/her ID
    public static User findUser(long id) throws IOException
    {
        String path = "./src/main/resources/users/" + id;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        User result;
        try
        {
            result = gson.fromJson(Files.readString(Paths.get(path)), User.class);
        }
        catch (NoSuchFileException e)
        {
            result = null;
        }
        return result;
    }

    public static Tweet findTweet(String id) throws IOException
    {
        String path = "./src/main/resources/tweets/" + id;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        Tweet result;
        try
        {
            result = gson.fromJson(Files.readString(Paths.get(path)), Tweet.class);
        }
        catch (NoSuchFileException e)
        {
            result = null;
        }
        return result;
    }

    public static Message findMessage(String id) throws IOException
    {
        String path = "./src/main/resources/messages/" + id;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        Message result;
        try
        {
            result = gson.fromJson(Files.readString(Paths.get(path)), Message.class);
        }
        catch (NoSuchFileException e)
        {
            result = null;
        }
        return result;
    }

    public static long loadLastId() throws IOException
    {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("./src/main/resources/database/id.txt"), StandardCharsets.UTF_8));
        return Long.parseLong(fileContent.get(0));
    }

    public static long loadLastMessage() throws IOException
    {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("./src/main/resources/database/message.txt"), StandardCharsets.UTF_8));
        return Long.parseLong(fileContent.get(0));
    }
}