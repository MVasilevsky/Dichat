package chyatus.commands;

import chyatus.User;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Chat message
 *
 * @author M.Vasileusky
 */
public class Message implements Serializable {

    public static final int PLAIN_MESSAGE = 1;

    private int type;
    private Date date;
    private String text;
    private User sender;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Message(int type, String message, User sender) {
        this.type = type;
        this.text = message;
        this.date = new Date();
        this.sender = sender;
    }

    public Message(int type, String message, User sender, Date date) {
        this(type, message, sender);
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.type;
        hash = 53 * hash + Objects.hashCode(this.date);
        hash = 53 * hash + Objects.hashCode(this.text);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Message other = (Message) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        return true;
    }

}
