package models;

import data.Load;
import data.Save;

import java.io.IOException;
import java.util.Date;

public class Message
{
    public final long id;
    public final long sender;
    public final long receiver;
    public final String text;
    public boolean visible;
    public final Date messageTime;
    public boolean seen;

    public Message(long sender, long receiver, String text) throws IOException
    {
        Save.changeLastMessage(Load.loadLastMessage() + 1);
        this.id = Load.loadLastMessage();
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.visible = true;
        this.messageTime = new Date();
        this.seen = false;
        Load.findUser(sender).addMessage(receiver, "s:" + text);
        Save.saveUser(Load.findUser(sender));
        Load.findUser(receiver).addMessage(sender, "r:" + text);
        Save.saveUser(Load.findUser(receiver));
        Save.saveMessage(this);
    }

    /*
    Should I add this?
    public void deleteMessage() throws IOException
    {
        this.seen = true;
        Save.saveMessage(this);
    }
     */
}