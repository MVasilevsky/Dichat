package chyatus.commands;

import java.io.Serializable;

/**
 * Chat message
 *
 * @author M.Vasileusky
 */
public class Message implements Serializable {

    public static final int TYPE_SINGLE_MESSAGE = 1;
    public static final int TYPE_MULTI_MESSAGE = 2;

    public int type;
    public String text;

    public Message(int type, String message) {
        this.type = type;
        this.text = message;
    }

}
