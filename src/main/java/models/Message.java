package models;

import data.Load;
import data.Save;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.Date;

public class Message
{
    static private final Logger logger = LogManager.getLogger(Message.class);

    public final long id;
    public final long sender;
    public final long receiver;
    public final String text;
    public boolean visible;
    public final Date messageTime;

    public Message(long sender, long receiver, String text) throws IOException
    {
        Save.changeLastMessage(Load.loadLastMessage() + 1);
        this.id = Load.loadLastMessage();
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.visible = true;
        this.messageTime = new Date();
        logger.info("message " + this.id + " was sent from " + sender + " to " + "destination.");
        Load.findUser(sender).addMessage(receiver, "s:" + this.id);
        Save.saveUser(Load.findUser(sender));
        Load.findUser(receiver).addMessage(sender, "r:" + this.id);
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