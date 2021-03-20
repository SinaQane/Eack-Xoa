package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.Objects;

public class Load
{
    // For finding user by his/her Username
    public static User findUser(String username) throws IOException
    {
        String path = "./resources/users";
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
        String path = "./resources/users/" + id;
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
        String path = "./resources/tweets/" + id;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();
        Tweet result;
        try {
            result = gson.fromJson(Files.readString(Paths.get(path)), Tweet.class);
        } catch (NoSuchFileException e) {
            result = null;
        }
        return result;
    }

    public static long loadLastId() throws IOException
    {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get("./resources/id.txt"), StandardCharsets.UTF_8));
        return Long.parseLong(fileContent.get(0));
    }
}
