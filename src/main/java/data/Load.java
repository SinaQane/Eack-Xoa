package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class Load
{
    public static User findUser(String username) throws IOException
    {
        String path = "./resources/users/" + username;
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
}
