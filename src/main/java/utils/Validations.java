package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations
{
    // Natural number regex
    public static final Pattern NUMBER = Pattern.compile("^[0-9]+$");

    // Date regex yyyy-MM-dd
    public static final Pattern DATE_REGEX =
            Pattern.compile("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$");

    // Email regex
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("\\w+@[a-zA-Z]+.[a-z]+");

    // International phone number regex (like +989123456789)
    public static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|\n" +
                    "2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|\n" +
                    "4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$");

    /* Username regex with these rules:
        Only contains alphanumeric characters, underscore and dot.
        Underscore and dot can't be at the end or start of a username.
        Underscore and dot can't be next to each other.
        Underscore or dot can't be used multiple times in a row.
        Number of characters must be between 8 to 20.
     */
    public static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");

    public static boolean yesOrNo(String string)
    {
        return string.equals("y") || string.equals("n");
    }

    // Search in file "where" for string "what". returns true if "what" is in "where".
    public static boolean search(String where, String what)
    {
        File file = new File("src/main/resources/" + where + ".txt");
        try {
            Scanner scanner = new Scanner(file);

            //now read the file line by line...
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(what.equals(line)) {
                    return true;
                }
            }
            return false;
        } catch(FileNotFoundException e) {
            return false;
        }
    }

    public static boolean usernameIsAvailable(String username)
    {
        return !search("usernames", username);
    }

    public static boolean usernameIsValid(String username)
    {
        Matcher matcher = VALID_USERNAME_REGEX
                .matcher(username);
        return matcher.find();
    }

    public static boolean emailIsAvailable(String email)
    {
        return !search("emails", email);
    }

    public static boolean emailIsValid(String email)
    {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean phoneNumberIsAvailable(String phoneNumber)
    {
        return !search("phonenumbers", phoneNumber);
    }

    public static boolean phoneNumberIsValid(String phoneNumber)
    {
        Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(phoneNumber);
        return matcher.find();
    }

    public static boolean dateIsValid(String date)
    {
        Matcher matcher = DATE_REGEX.matcher(date);
        return matcher.find();
    }

    public static boolean isNaturalNumber(String number)
    {
        Matcher matcher = NUMBER.matcher(number);
        return matcher.find();
    }
}
